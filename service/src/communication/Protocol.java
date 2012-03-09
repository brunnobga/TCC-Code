package communication;

/**
 * @author      Evandro Souza
 * @version     0.3
 */
public class Protocol {
	
	protected static final int START_BYTE = 0x55;
	protected static final int END_BYTE = 0x16;
	
	protected static final int INDEX_XBEE_ADDRESS_START = 1;
	protected static final int INDEX_XBEE_ADDRESS_END = 2;
	protected static final int INDEX_COMMAND = 3;
	protected static final int INDEX_PARAM1 = 4;
	protected static final int INDEX_PARAM2 = 5;
	protected static final int INDEX_CHECK_SUM = 6;
	
	protected static final int CMD_METRIC = 0x1;
	protected static final int CMD_RATE_ENABLE = 0x2;
	protected static final int CMD_RATE_DISABLE = 0x3;
	protected static final int CMD_RATE_VALUE = 0x4;

	protected static final int VALUE_METRIC_DSIS = 0x0;
	protected static final int VALUE_METRIC_DSCQS = 0x1;
	protected static final int VALUE_METRIC_SSCQE = 0x2;
	protected static final int VALUE_METRIC_SDSCE = 0x3;
	
	public static final int STATUS_OK = 0x80;
	public static final int STATUS_ERROR = 0xC0;
	
	protected static final int BODY_SIZE = 8;
	
	protected Protocol(int cmd, String address) {
        int index = 0;
    	body = new char[BODY_SIZE];
        body[index++] = START_BYTE;
        body[index++] = (char)(Integer.valueOf(address) & 0xFF);
        body[index++] = (char)(Integer.valueOf(address) >> 8);
        body[index++] = (char)(cmd);
        body[index++] = (char)0x0;
        body[index++] = (char)0x0;
        body[index++] = (char)getCheckSum();
        body[index++] = END_BYTE;
    }
	
	protected Protocol(int cmd, String address, int param) {
        int index = 0;
    	body = new char[BODY_SIZE];
        body[index++] = START_BYTE;
        body[index++] = (char)(Integer.valueOf(address) & 0xFF);
        body[index++] = (char)(Integer.valueOf(address) >> 8);
        body[index++] = (char)(cmd);
        body[index++] = (char)param;
        body[index++] = (char)0x0;
        body[index++] = (char)getCheckSum();
        body[index++] = END_BYTE;
    }
	
    protected Protocol(int cmd, String address, int param1, int param2) {
        int index = 0;
    	body = new char[BODY_SIZE];
        body[index++] = START_BYTE;
        body[index++] = (char)(Integer.valueOf(address) & 0xFF);
        body[index++] = (char)(Integer.valueOf(address) >> 8);
        body[index++] = (char)(cmd);
        body[index++] = (char)param1;
        body[index++] = (char)param2;
        body[index++] = (char)getCheckSum();
        body[index++] = END_BYTE;
    }

    protected char[] getBody() {
        return body;
    }
    
    private int getCheckSum() {
    	int i = 0;
    	int checkSum = 0;
    	for(i = 0; i < INDEX_CHECK_SUM; i++) {
    		checkSum += (int)body[i];
    	}
    	return checkSum;
    }
    
    private char[] body;
}