package scarvill.httpserver.routes;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.*;

public class FileResourceTest {

    @Test
    public void testReturnsFileData() throws IOException {
        String fileContents = "foo";
        Path filepath = testFile();
        Files.write(filepath, fileContents.getBytes(), StandardOpenOption.WRITE);

        FileResource resource = new FileResource(filepath);

        assertEquals(fileContents, new String(resource.getData(), StandardCharsets.UTF_8));
    }

    @Test
    public void testModifiesFileData() {
        String fileContents = "foo";
        Path filepath = testFile();
        FileResource resource = new FileResource(filepath);

        resource.setData(fileContents.getBytes());

        assertEquals(fileContents, new String(resource.getData(), StandardCharsets.UTF_8));
    }

    @Test
    public void testOverwritesExistingData() throws IOException {
        String newFileContents = "baz";
        Path filepath = testFile();
        Files.write(filepath, "foobar".getBytes(), StandardOpenOption.WRITE);

        FileResource resource = new FileResource(filepath);
        resource.setData(newFileContents.getBytes());

        assertEquals(newFileContents, new String(resource.getData(), StandardCharsets.UTF_8));
    }

    private Path testFile() {
        try {
            return Files.createTempFile("testfile", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}