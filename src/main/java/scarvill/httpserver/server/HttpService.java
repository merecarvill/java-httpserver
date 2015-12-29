package scarvill.httpserver.server;

import scarvill.httpserver.request.HttpRequest;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Function;

public class HttpService implements Serveable {
    private final Function<Request, Response> router;
    private final Logger logger;
    private final ServerIO io;

    public HttpService(Logger logger, Function<Request, Response> router) {
        this.router = router;
        this.logger = logger;
        this.io = new ServerIO();
    }

    public Runnable serve(Socket clientSocket) {
        return () -> {
            try (BufferedReader in =
                     new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedOutputStream out =
                     new BufferedOutputStream(clientSocket.getOutputStream())
            ) {
                handleClientTransaction(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void handleClientTransaction(BufferedReader in, BufferedOutputStream out) throws IOException {
        Response response = generateResponse(in);
        logger.logResponse(response);
        io.writeResponse(out, response);
    }

    private Response generateResponse(BufferedReader in) {
        try {
            Request request = io.readRequest(in);
            logger.logRequest(request);

            return router.apply(request);
        } catch (IOException e) {
            logger.logException(e.getMessage());

            return serverErrorResponse();
        }
    }

    private Response serverErrorResponse() {
        return new ResponseBuilder()
            .setStatus(Status.SERVER_ERROR)
            .setBody(Status.SERVER_ERROR.toString().getBytes())
            .build();
    }
}
