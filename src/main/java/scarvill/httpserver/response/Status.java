package scarvill.httpserver.response;

public enum Status {
    OK("200 OK"),
    NO_CONTENT("204 No Content"),
    PARTIAL_CONTENT("206 Partial Content"),
    FOUND("302 Found"),
    BAD_REQUEST("400 Bad Request"),
    UNAUTHORIZED("401 Unauthorized"),
    NOT_FOUND("404 Not Found"),
    METHOD_NOT_ALLOWED("405 Method Not Allowed");

    private final String statusString;

    Status(String statusString) {
        this.statusString = statusString;
    }

    public String toString() {
        return this.statusString;
    }
}
