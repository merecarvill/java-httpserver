package scarvill.httpserver.routes;

public interface Resource {
    byte[] getData();

    void setData(byte[] data);
}
