package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VirtualRouterTest {

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() {
        Request request = new RequestBuilder().setURI("/unconfigured").build();
        Router router = new VirtualRouter();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoMethodHandler() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Router router = new VirtualRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.POST, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, routerResponse.getStatus());
    }

    @Test
    public void testReturnsResultOfApplyingCorrespondingMethodHandler() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Status expectedResponseStatus = Status.OK;
        Router router = new VirtualRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(expectedResponseStatus, routerResponse.getStatus());
    }

    @Test
    public void testDynamicallyHandlesOptionsRequests() {
        Request request = new RequestBuilder().setMethod(Method.OPTIONS).setURI("/").build();
        Router router = new VirtualRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(routerResponse.getHeaders().get("Allow").contains("GET"));
        assertTrue(routerResponse.getHeaders().get("Allow").contains("OPTIONS"));

        router.addRoute("/", Method.POST, new GiveStaticResponse(response));
        Response newRouterResponse = router.routeRequest(request);

        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("GET"));
        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("POST"));
        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("OPTIONS"));
    }
}