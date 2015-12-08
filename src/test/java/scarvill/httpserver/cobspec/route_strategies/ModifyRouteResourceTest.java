package scarvill.httpserver.cobspec.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.routes.StringResource;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ModifyRouteResourceTest {
    @Test
    public void testChangesAssociatedResourceToMatchRequestBody() throws Exception {
        Resource resource = new StringResource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource);
        Request request = new RequestBuilder().setBody("new data".getBytes()).build();

        handler.apply(request);

        assertArrayEquals("new data".getBytes(), resource.getData());
    }

    @Test
    public void testReturnsStatusOKResponse() throws Exception {
        Resource resource = new StringResource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource);
        Request request = new RequestBuilder().setBody("new data".getBytes()).build();

        Response response = handler.apply(request);

        assertEquals(Status.OK, response.getStatus());
    }
}