package communication;

import java.util.logging.Level;

/**
 * @author      Evandro Souza <tecnologia05@velti.com.br>
 * @version     2.42
 */
public class Operations {

    /*Constante que define o numero de tentativas de reenvio de um comando*/
    private static final int CON_RETRY = 10;
    
    public int defineMetric(Connection c, int value) {
    	System.out.println(">>>>>Definindo metrica");
    	Protocol p = new Protocol(Protocol.CMD_METRIC, 
    			c.getDevice().getAddress(), value);
    	char[] receivedData = getResponse(c, p);
    	if (receivedData != null && receivedData.length > 0) {
    		String cmd = "";
        	for(int i = 0; i < receivedData.length; i++) {
        		cmd += Integer.toHexString((int)receivedData[i]) + " ";
        	}
        	System.out.println("Resposta: " + cmd);
    		try {
    			return takeStatus(receivedData);
             } catch (Exception ex) {
                return Protocol.STATUS_ERROR;
             }
    	}
    	return Protocol.STATUS_ERROR;
    }
    
    public int enableRate(Connection c) {
    	System.out.println(">>>>>Habilitando avaliacao");
    	char[] receivedData = getResponse(c, new Protocol(Protocol.CMD_RATE_ENABLE, 
    			c.getDevice().getAddress()));
    	if (receivedData != null && receivedData.length > 0) {
    		String cmd = "";
        	for(int i = 0; i < receivedData.length; i++) {
        		cmd += Integer.toHexString((int)receivedData[i]) + " ";
        	}
        	System.out.println("Resposta: " + cmd);
    		try {
    			return takeStatus(receivedData);
             } catch (Exception ex) {
                return Protocol.STATUS_ERROR;
             }
    	}
    	return Protocol.STATUS_ERROR;
    }
    
    public int disableRate(Connection c) {
    	System.out.println(">>>>>Desabilitando avaliacao");
    	char[] receivedData = getResponse(c, new Protocol(Protocol.CMD_RATE_DISABLE, 
    			c.getDevice().getAddress()));
    	if (receivedData != null && receivedData.length > 0) {
    		String cmd = "";
        	for(int i = 0; i < receivedData.length; i++) {
        		cmd += Integer.toHexString((int)receivedData[i]) + " ";
        	}
        	System.out.println("Resposta: " + cmd);
    		try {
    			return takeStatus(receivedData);
             } catch (Exception ex) {
                return Protocol.STATUS_ERROR;
             }
    	}
    	return Protocol.STATUS_ERROR;
    }
    
    public int[][] getReceivedRates(Connection c) {
    	return c.getRates();
    }
    
    private char[] getResponse(Connection c, Protocol command) {
        char[] data = null;
        System.out.println("Passei0");
        boolean sent = c.send(command);
        int waitTime, count = 0;
        while (count < CON_RETRY) {
        	waitTime = (int) (c.getTimeOut() / 100); 
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            if(sent) {
	            while (data == null && waitTime > 0) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException ex) {
	                }
	                data = c.getData();
	                waitTime--;
	            }
	            if (data == null) {
	            	System.out.println("Passei1");
	            	sent = c.send(command);
	            } else {
	            	count = CON_RETRY;
	            }
            } else {
            	try {
					Thread.sleep(c.getTimeOut());
				} catch (InterruptedException e) {
				}
				System.out.println("Passei2");
            	sent = c.send(command);
            }
            count++;
        }
        return data;
    }

    private int takeStatus(char[] data) {
    	return (data[Protocol.INDEX_COMMAND] & 0xF0);
    }
}
