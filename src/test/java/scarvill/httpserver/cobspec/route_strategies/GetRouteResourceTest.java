package scarvill.httpserver.cobspec.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.routes.StringResource;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class GetRouteResourceTest {
    @Test
    public void testReturnsResponseWithResourceData() throws Exception {
        Resource resource = new StringResource("data");
        Function<Request, Response> handler = new GetRouteResource(resource);
        Request request = new RequestBuilder().build();
        Response response = handler.apply(request);

        assertArrayEquals("data".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsResponseWithContentLengthHeader() throws Exception {
        Resource resource = new StringResource("data");
        Function<Request, Response> handler = new GetRouteResource(resource);
        Request request = new RequestBuilder().build();
        Response response = handler.apply(request);
        String expectedHeader = "Content-Length: " + resource.getData().length + "\r\n";

        assertTrue(response.getHeaders().contains(expectedHeader));
    }
}