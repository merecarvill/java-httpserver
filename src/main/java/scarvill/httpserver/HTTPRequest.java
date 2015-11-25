package scarvill.httpserver;

import scarvill.httpserver.constants.Method;

import java.util.HashMap;

import static scarvill.httpserver.constants.Method.*;

public class HTTPRequest {
    private String rawRequest;

    public HTTPRequest(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public Request parse() {
        return new Request.Builder()
            .setMethod(parseMethod())
            .setURI(parseURI())
            .setParameters(parseParameters())
            .setBody(parseBody())
            .build();
    }

    private Method parseMethod() {
        String method = rawRequest.split(" ")[0];
        switch (method) {
            case "GET":     return GET;
            case "HEAD":    return HEAD;
            case "OPTIONS": return OPTIONS;
            case "PUT":     return PUT;
            case "POST":    return POST;
            case "DELETE":  return DELETE;
            default:        return NULL_METHOD;
        }
    }

    private String parseURI() {
        return rawRequest.split(" ")[1].split("\\?")[0];
    }

    private HashMap<String, String> parseParameters() {
        if (hasQueryStringParameters()) {
            return new QueryString().parse(rawRequest.split(" ")[1].split("\\?")[1]);
        } else {
            return new HashMap<>();
        }
    }

    private String parseBody() {
        String bodyDelimiter = "\r\n\r\n";
        int bodyStartIndex = rawRequest.indexOf(bodyDelimiter);

        if (bodyStartIndex == -1) {
            return "";
        } else {
            return rawRequest.substring(bodyStartIndex + bodyDelimiter.length());
        }
    }

    private boolean hasQueryStringParameters() {
        return rawRequest.split(" ")[1].split("\\?").length > 1;
    }
}
