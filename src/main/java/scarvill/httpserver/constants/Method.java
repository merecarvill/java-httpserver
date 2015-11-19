package scarvill.httpserver.constants;

public class Method {
    public static final String GET = "GET";
    public static final String HEAD = "HEAD";
    public static final String OPTIONS = "OPTIONS";
    public static final String PUT = "PUT";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";

    public static String[] allMethods() {
        return new String[]{GET, HEAD, OPTIONS, PUT, POST, DELETE};
    }
}