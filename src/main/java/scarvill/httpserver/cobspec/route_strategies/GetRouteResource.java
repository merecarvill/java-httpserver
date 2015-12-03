package scarvill.httpserver.cobspec.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

public class GetRouteResource implements Function<Request, Response> {
    private Resource resource;

    public GetRouteResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Response apply(Request request) {
        String contentLength = "Content-Length: " + resource.getData().length + "\r\n";
        return new ResponseBuilder().setStatus(Status.OK)
                                    .setHeaders(new String[]{contentLength})
                                    .setBody(new String(resource.getData()))
                                    .build();
    }
}
