package scarvill.httpserver.server;

import org.junit.Test;
import scarvill.httpserver.request.HttpRequest;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HttpResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class ServerIOTest {
    @Test
    public void testReadsRequestFromStream() throws IOException {
        byte[] rawRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        Request request = new ServerIO().readRequest(in);

        assertEquals(Method.GET, request.getMethod());
        assertEquals("/", request.getURI());
    }

    @Test
    public void testWritesResponseToStream() throws IOException {
        Response expectedResponse = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Header", "a header")
            .setBody("body".getBytes())
            .build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String expectedResponseString = new String(new HttpResponse().generate(expectedResponse));

        new ServerIO().writeResponse(outputStream, expectedResponse);

        assertEquals(expectedResponseString, new String(outputStream.toByteArray()));
    }
}
