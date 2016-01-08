package scarvill.httpserver;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.server.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class TTTSpike {

    public static void main(String[] args) throws IOException {
        Server server = new Server(new TTTConfiguration());

        server.start();
    }

    private static class TTTConfiguration implements ServerConfiguration {

        @Override
        public int getPort() {
            return 5000;
        }

        @Override
        public String getPublicDirectory() {
            return null;
        }

        @Override
        public Serveable getService() {
            Logger nullLogger = new Logger(new PrintStream(new ByteArrayOutputStream()));
            Function<Request, Response> router = new TTTRouter();

            return new HttpService(nullLogger, router);
        }
    }

    private static class TTTRouter implements Function<Request, Response> {

        @Override
        public Response apply(Request request) {
            String[] board = parseBoardString(request.getParameterValue("board"));

            ArrayList<Integer> availableSpaces = new ArrayList<>();
            for (int spaceId = 0; spaceId < board.length; spaceId++) {
                if (board[spaceId].equals("_")) {
                    availableSpaces.add(spaceId);
                }
            }

            int computerMove = availableSpaces.get(new Random().nextInt(availableSpaces.size()));

            return new ResponseBuilder()
                .setStatus(Status.OK)
                .setBody(String.valueOf(computerMove).getBytes())
                .build();
        }

        private String[] parseBoardString(String board) {
            return board.split(",");
        }
    }
}