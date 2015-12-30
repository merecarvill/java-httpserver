package scarvill.httpserver.request;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static scarvill.httpserver.request.Method.*;

public class HttpRequestTest {
    @Test
    public void testParsesRequestMethod() {
        assertEquals(GET, new HttpRequest(rawRequestWith(GET)).parse().getMethod());
        assertEquals(POST, new HttpRequest(rawRequestWith(POST)).parse().getMethod());
        assertEquals(PUT, new HttpRequest(rawRequestWith(PUT)).parse().getMethod());
        assertEquals(PATCH, new HttpRequest(rawRequestWith(PATCH)).parse().getMethod());
        assertEquals(DELETE, new HttpRequest(rawRequestWith(DELETE)).parse().getMethod());
        assertEquals(OPTIONS, new HttpRequest(rawRequestWith(OPTIONS)).parse().getMethod());
        assertEquals(HEAD, new HttpRequest(rawRequestWith(HEAD)).parse().getMethod());
    }

    private String rawRequestWith(Method method) {
        return method.toString() + " /uri HTTP/1.1\r\n\r\n";
    }

    @Test
    public void testParsesUnsupportedMethodInRawHTTPRequest() {
        Request request = new HttpRequest("FOO /uri HTTP/1.1\r\n\r\n").parse();

        assertEquals(Method.UNSUPPORTED, request.getMethod());
    }

    @Test
    public void testParsesRawHTTPRequestURI() {
        Request request = new HttpRequest("GET /uri HTTP/1.1\r\n\r\n").parse();

        assertEquals("/uri", request.getURI());
    }

    @Test
    public void testParsesRawHTTPRequestWithQueryStringParameters() {
        Request request = new HttpRequest("GET /uri?foo=bar&bar=baz HTTP/1.1\r\n\r\n").parse();

        assertEquals("/uri", request.getURI());
        assertEquals("bar", request.getParameters().get("foo"));
        assertEquals("baz", request.getParameters().get("bar"));
    }

    @Test
    public void testIgnoresNonParameterQueryStringElements() {
        Request request = new HttpRequest("GET /uri?ignored&bar=baz HTTP/1.1\r\n\r\n").parse();

        assertEquals("/uri", request.getURI());
        assertEquals("baz", request.getParameters().get("bar"));
    }

    @Test
    public void testTranslatesSpecialCharacterCodesInQueryString() {
        String query = "message=%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26" +
            "%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";
        String translatedMessage =
            "<, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";

        Request request = new HttpRequest("GET /uri?" + query + " HTTP/1.1\r\n\r\n").parse();

        assertEquals(translatedMessage, request.getParameters().get("message"));
    }

    @Test
    public void testParsesRawHTTPRequestHeaders() {
        String rawRequest = "GET /uri HTTP/1.1\r\n" +
            "Header: a header\r\n" +
            "Other: other header\r\n" +
            "\r\n";
        Request request = new HttpRequest(rawRequest).parse();

        assertEquals("a header", request.getHeaders().get("Header"));
        assertEquals("other header", request.getHeaders().get("Other"));
    }

    @Test
    public void testParsesRawHTTPRequestWithNoBody() {
        Request request = new HttpRequest("GET /uri HTTP/1.1\r\n\r\n").parse();

        assertArrayEquals(new byte[]{}, request.getBody());
    }

    @Test
    public void testParsesRawHTTPRequestWithBody() {
        Request request = new HttpRequest("GET /uri HTTP/1.1\r\n\r\n", "body".getBytes()).parse();

        assertArrayEquals("body".getBytes(), request.getBody());
    }
}