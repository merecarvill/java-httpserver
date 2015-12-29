package scarvill.httpserver.server;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class LoggerTest {
    @Test
    public void testLogsRequestToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/a/route")
            .setParameter("name", "value")
            .setParameter("foo", "bar")
            .setHeader("Header", "a header")
            .setHeader("Other", "other header")
            .setBody("body".getBytes())
            .build();

        String expectedLog =
            "*** Received Request ***\n" +
                "MethodLine: GET /a/route HTTP/1.1\n" +
                "Parameters: foo=bar name=value\n" +
                "Headers:\n" +
                "- Header: a header\n" +
                "- Other: other header\n" +
                "Body-length: 4\n" +
                "\n";

        logger.logRequest(request);

        assertEquals(expectedLog, out.toString());
    }

    @Test
    public void testLogsResponseToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        Response response = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Header", "a header")
            .setHeader("Other", "other header")
            .setBody("body".getBytes())
            .build();

        String expectedLog =
            "*** Sent Response ***\n" +
                "Status: 200 OK\n" +
                "Headers:\n" +
                "- Header: a header\n" +
                "- Other: other header\n" +
                "Body-length: 4\n" +
                "\n";

        logger.logResponse(response);

        assertEquals(expectedLog, out.toString());
    }

    @Test
    public void testLogsExceptionMessageToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));

        String expectedLog =
            "*** Server Exception ***\n" +
                "exception message\n";

        logger.logException("exception message");

        assertEquals(expectedLog, out.toString());
    }
}