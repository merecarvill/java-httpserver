package scarvill.httpserver.cobspec;

import scarvill.httpserver.HTTPService;
import scarvill.httpserver.Logger;
import scarvill.httpserver.Server;
import scarvill.httpserver.routes.Router;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        Router router = Cobspec.configuredRouter(arguments.publicDirectory);
        HTTPService service = new HTTPService(router, new Logger(System.out));
        Server server = new Server(arguments.port, service);

        server.start();
    }
}
