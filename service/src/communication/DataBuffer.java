package communication;

/**
 *
 * @author evandro.souza
 */
public class DataBuffer {

    private char[] data;

    protected DataBuffer(char[] data) {
        this.data = data;
    }

    protected char[] getData() {
        return data;
    }

    protected void setData(char[] data) {
        this.data = data;
    }
}
