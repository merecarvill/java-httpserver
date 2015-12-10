package scarvill.httpserver.cobspec;

import scarvill.httpserver.cobspec.route_strategies.EchoRequestParameters;
import scarvill.httpserver.cobspec.route_strategies.GetRouteResource;
import scarvill.httpserver.cobspec.route_strategies.GiveStaticResponse;
import scarvill.httpserver.cobspec.route_strategies.ModifyRouteResource;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.FileResource;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.routes.Router;
import scarvill.httpserver.routes.StringResource;

import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class Cobspec {

    public static Router configuredRouter(String publicDirectory) {
        Router router = new Router();

        router.addRoute("/", Method.GET, new GetRouteResource(
            new StringResource(indexPage(publicDirectory))));

        router.addRoute("/parameters", Method.GET, new EchoRequestParameters());

        router.addRoute("/redirect", Method.GET, giveRedirectResponse("http://localhost:5000/"));

        router.addRoute("/method_options", Method.GET, giveStatusResponse(Status.OK));
        router.addRoute("/method_options", Method.PUT, giveStatusResponse(Status.OK));
        router.addRoute("/method_options", Method.POST, giveStatusResponse(Status.OK));
        router.addRoute("/method_options", Method.HEAD, giveStatusResponse(Status.OK));

        Resource formResource = new StringResource("");
        router.addRoute("/form", Method.GET, new GetRouteResource(formResource));
        router.addRoute("/form", Method.POST, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.PUT, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.DELETE, new ModifyRouteResource(formResource));

        Resource file1 = new FileResource(Paths.get(publicDirectory + "/file1"));
        router.addRoute("/file1", Method.GET, new GetRouteResource(file1));

        Resource file2 = new FileResource(Paths.get(publicDirectory + "/file2"));
        router.addRoute("/file2", Method.GET, new GetRouteResource(file2));

        Resource textfile = new FileResource(Paths.get(publicDirectory + "/text-file.txt"));
        router.addRoute("/text-file.txt", Method.GET, new GetRouteResource(textfile));

        Resource partialContent =
            new FileResource(Paths.get(publicDirectory + "/partial_content.txt"));
        router.addRoute("/partial_content.txt", Method.GET, new GetRouteResource(partialContent));

        Resource patchContent = new FileResource(Paths.get(publicDirectory + "/patch-content.txt"));
        router.addRoute("/patch-content.txt", Method.GET, new GetRouteResource(patchContent));

        Resource jpeg = new FileResource(Paths.get(publicDirectory + "/image.jpeg"));
        router.addRoute("/image.jpeg", Method.GET, new GetRouteResource(jpeg));

        Resource png = new FileResource(Paths.get(publicDirectory + "/image.png"));
        router.addRoute("/image.png", Method.GET, new GetRouteResource(png));

        Resource gif = new FileResource(Paths.get(publicDirectory + "/image.gif"));
        router.addRoute("/image.gif", Method.GET, new GetRouteResource(gif));

        return router;
    }

    private static String indexPage(String publicDirectory) {
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            directoryListEntries(publicDirectory) +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";
    }

    private static String directoryListEntries(String publicDirectory) {
        String directoryList = "";
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(publicDirectory));
            for (Path entry : stream) {
                String filename = entry.getFileName().toString();
                directoryList += "<li><a href=/" + filename + ">" + filename + "</a></li>\n";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  directoryList;
    }

    private static Function<Request, Response> giveRedirectResponse(String redirectLocation) {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.FOUND)
                .setHeaders(new String[]{"Location: " + redirectLocation + "\r\n"})
                .build());
    }

    private static Function<Request, Response> giveStatusResponse(Status status) {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(status)
                .build());
    }
}