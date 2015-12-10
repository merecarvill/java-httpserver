package scarvill.httpserver;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

import java.io.PrintStream;

public class Logger {
    private PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void logRequest(Request request) {
        out.println("*** Received Request ***");
        out.println("Method: " + request.getMethod().toString());
        out.println("Path: " + request.getURI());
        out.print("Parameters:");
        request.getParameters().forEach((name, value) -> out.print(" " + name + "=" + value));
        out.println();
        out.println("Headers:");
        request.getHeaders().forEach((keyword, content) -> out.println("- " + keyword + ": " + content));
        out.println("Body-length: " + request.getBody().length);
        out.println();
    }

    public void logResponse(Response response) {
        out.println("*** Sent Response ***");
        out.println("Status: " + response.getStatus().toString());
        out.println("Headers:");
        response.getHeaders().forEach((keyword, content) -> out.println("- " + keyword + ": " + content));
        out.println("Body-length: " + response.getBody().length);
        out.println();
    }
}
