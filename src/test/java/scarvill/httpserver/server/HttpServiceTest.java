package scarvill.httpserver.server;

import org.junit.Test;
import scarvill.httpserver.response.HttpResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.GiveStaticResponse;
import scarvill.httpserver.routing.RouteRequest;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpServiceTest {
    @Test
    public void testLogsHTTPTransactions() {
        byte[] rawRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        MockSocket clientSocket =
            new MockSocket(new ByteArrayInputStream(rawRequest), new ByteArrayOutputStream());
        Response expectedResponse = new ResponseBuilder().setStatus(Status.OK).build();
        ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(logStream));
        HttpService service = new HttpService(logger, new GiveStaticResponse(expectedResponse));

        service.serve(clientSocket).run();

        assertTrue(new String(logStream.toByteArray()).contains("Received Request"));
        assertTrue(new String(logStream.toByteArray()).contains("Sent Response"));
    }

    @Test
    public void testSendsResponseToClientRequest() throws Exception {
        byte[] rawRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MockSocket clientSocket = new MockSocket(inputStream, outputStream);
        Response expectedResponse = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Header", "a header")
            .setBody("body".getBytes())
            .build();
        Logger logger = new Logger(new NullPrintStream());
        HttpService service = new HttpService(logger, new GiveStaticResponse(expectedResponse));
        String expectedResponseString = new String(new HttpResponse().generate(expectedResponse));

        service.serve(clientSocket).run();

        assertEquals(expectedResponseString, new String(outputStream.toByteArray()));
    }

    @Test
    public void testSends500ServerErrorResponseWhenAnIOExceptionIsRaised() throws Exception {
        InputStream inputStream = new FailingInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MockSocket clientSocket = new MockSocket(inputStream, outputStream);
        Logger logger = new Logger(new NullPrintStream());
        HttpService service = new HttpService(logger, new RouteRequest());

        service.serve(clientSocket).run();

        assertTrue(new String(outputStream.toByteArray()).contains(Status.SERVER_ERROR.toString()));
    }

    private class MockSocket extends Socket {
        private final OutputStream outputStream;
        private final InputStream inputStream;

        public MockSocket(InputStream inputStream, OutputStream outputStream) {
            this.outputStream = outputStream;
            this.inputStream = inputStream;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
    }

    private class FailingInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException();
        }
    }

    private class NullPrintStream extends PrintStream {
        public NullPrintStream() {
            super(new ByteArrayOutputStream());
        }
    }
}