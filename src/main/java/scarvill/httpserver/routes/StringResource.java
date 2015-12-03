package scarvill.httpserver.routes;

public class StringResource implements Resource{
    private String data;

    public StringResource(String data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return data.getBytes();
    }

    @Override
    public void setData(byte[] data) {
        this.data = new String(data);
    }
}
