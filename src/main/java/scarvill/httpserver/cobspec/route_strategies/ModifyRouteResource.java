package scarvill.httpserver.cobspec.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

public class ModifyRouteResource implements Function<Request, Response> {
    private Resource resource;

    public ModifyRouteResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Response apply(Request request) {
        resource.setData(request.getBody().getBytes());

        return new ResponseBuilder().setStatus(Status.OK).build();
    }
}
