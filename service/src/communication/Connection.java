package communication;

import henry.equipamento.comunicacao.serial.ComunicacaoSerial;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Device;


/**
 *
 * @author evandro.souza
 */
public class Connection extends Thread {

    private static final int CONNECTION_TIMEOUT = 50;
    private static final int CONNECTION_RETRY = 4;
    
    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    public Connection(Device device) {
        this.device = device;
        timeOut = device.getTimeOut();
        serialCom = new ComunicacaoSerial("/dev/ttyS0",
                9600, true, true);
        dataBuffer = new ArrayList<DataBuffer>();
        rateBuffer = new ArrayList<DataBuffer>();
    }

    public Device getDevice() {
        return device;
    }

    protected ComunicacaoSerial getSerialCom() {
        return serialCom;
    }

    public void connect() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        synchronized (device) {
            if (!device.isConnected()) {
                logger.log(Level.INFO, "Estabelecendo conexao com " + device.toString()
                        + ". Time Out: " + device.getTimeOut());
                if (!serialCom.conectado()) {
                    while (run && !serialCom.conectado()) {
                        try {
                            serialCom.abrePorta(CONNECTION_TIMEOUT);
                            serialCom.setaParametrosComunicacao(ComunicacaoSerial.DATABITS_8,
                                    ComunicacaoSerial.STOPBITS_1, ComunicacaoSerial.PARITY_NONE);
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    device.setConnected(true);
                    logger.log(Level.INFO, device.toString()
                            + ". Time Out: " + device.getTimeOut() + " conectado!");
                } else if (serialCom.conectado()) {
                    logger.log(Level.INFO, "Uma conexao Serial aberta em " + device.toString() + "nao foi fechada!");
                }
            } else {
                logger.log(Level.INFO, "Tentativa de conexao com um equipamento ja conectado!" + device.toString());
            }
        }
    }

    public void disconnect() {
        synchronized (device) {
            if (device.isConnected()) {
                logger.log(Level.INFO, "Finalizando uma conex�o com " + device.toString() + "...");
                while (serialCom.conectado()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                    serialCom.fechaCom();
                }
                device.setConnected(false);
                logger.log(Level.INFO, "Conex�o com " + device.toString() + " finalizada!");
            } else {
                logger.log(Level.INFO, "Tentativa de desconex�o com um equipamento sem conex�o!"
                        + device.toString());
            }
        }
    }

    protected void reconnect() {
        synchronized (device) {
            disconnect();
            if (!device.isConnected()) {
                logger.log(Level.INFO, "Estabelecendo conexao com " + device.toString()
                        + ". Time Out: " + device.getTimeOut());
                if (!serialCom.conectado()) {
                    try {
                        serialCom.abrePorta(CONNECTION_TIMEOUT);
                        serialCom.setaParametrosComunicacao(ComunicacaoSerial.DATABITS_8,
                                ComunicacaoSerial.STOPBITS_1, ComunicacaoSerial.PARITY_NONE);
                        serialCom.fechaCom();
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (serialCom.conectado()) {
                    logger.log(Level.INFO, "Uma conexao Serial aberta em " + device.toString() + "nao foi fechada!");
                }
                device.setConnected(true);
                logger.log(Level.INFO, device.toString()
                        + ". Time Out: " + device.getTimeOut() + " conectado!");
            } else {
                logger.log(Level.INFO, "Tentativa de conexao com um equipamento ja conectado!"
                        + device.toString());
            }
        }        
    }

    public void setStop() {
        run = false;
    }

    @Override
    public void run() {
        connect();
        while (run) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
            if (device.isConnected()) {
                if (serialCom.getQuantidadeDadosRecebidos() > 0) {
                    char[] receivedData = new char[Protocol.BODY_SIZE], buff = null;
                    int retry = 0;
                    while (buff == null && retry < CONNECTION_RETRY) {
                    	buff = serialCom.recebeDados();
                        retry++;
                    }
                    if (buff != null) {
                        int size = buff.length;
                        if (buff[0] == Protocol.START_BYTE) {
                        	System.arraycopy(buff, 0, receivedData, 0, buff.length);
                            while (size < Protocol.BODY_SIZE) {
                                retry = 0;
                                buff = null;
                                while (retry < CONNECTION_RETRY && buff == null) {
                                    if (serialCom.getQuantidadeDadosRecebidos() > 0) {
                                        buff = serialCom.recebeDados();
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    retry++;
                                }
                                if (buff != null) {
                                	if(buff.length + size >= Protocol.BODY_SIZE) {
                                		System.arraycopy(buff, 0, receivedData, size, Protocol.BODY_SIZE - size);
                                	} else {
                                		System.arraycopy(buff, 0, receivedData, size, buff.length);
                                	}
                                    size += receivedData.length;
                                } else {
                                    size = Protocol.BODY_SIZE;
                                }
                            }
                            if (receivedData.length > 0 ) {
                                addBuffer(receivedData);
                            }
                        }
                    }
                }
            }
        }
        disconnect();
    }

    private synchronized void addBuffer(char[] data) {
    	if(data[Protocol.INDEX_COMMAND] == Protocol.CMD_RATE_VALUE) {
    		System.out.println(">>>>>Nota recebida!");
    		rateBuffer.add(new DataBuffer(data));
    	} else {
    		dataBuffer.add(new DataBuffer(data));
    	}
    }

    protected synchronized boolean send(Protocol c) {
        if (c != null) {
            if (device.isConnected()) {
                serialCom.HabilitarEscrita();
                String cmd = "";
            	for(int i = 0; i < c.getBody().length; i++) {
            		cmd += Integer.toHexString((int)c.getBody()[i]) + " ";
            	}
                System.out.println("Enviando comando: " + cmd);
                serialCom.enviarDados(c.getBody());
                serialCom.HabilitarLeitura();
                return true;
            } else {
                logger.log(Level.SEVERE, "Tentativa de comunica��o com um equipamento desconectado!"
                        + device.toString() + "...");
            }
        } else {
            logger.log(Level.SEVERE, "Tentativa de envio de comando nulo!"
                    + device.toString() + "...");
        }
        return false;
    }

    protected synchronized char[] getData() {
        synchronized (dataBuffer) {
        	if(dataBuffer.size() > 0) {
        		char[] data = dataBuffer.get(0).getData();
        		dataBuffer.remove(0);
        		return data;
        	} else {
        		return null;
        	}
        }
    }

    protected synchronized int[][] getRates() {
        synchronized (rateBuffer) {
        	if(rateBuffer.size() > 0) {
        		int[][] data = new int[rateBuffer.size()][2];
        		int i = 0;
        		for(DataBuffer aux : rateBuffer) {
        			data[i][0] = aux.getData()[Protocol.INDEX_PARAM1];
        			data[i++][1] = aux.getData()[Protocol.INDEX_PARAM2];
        		}
        		rateBuffer.clear();
        		return data;
        	} else {
        		return null;
        	}
        }
    }
    
    public int getTimeOut() {
        return timeOut;
    }
    
    private final Device device;
    private ComunicacaoSerial serialCom;
    private final ArrayList<DataBuffer> dataBuffer, rateBuffer;;
    private boolean run = true;
    private int timeOut;
}
