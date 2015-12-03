package scarvill.httpserver.routes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileResource implements Resource {
    private Path filepath;

    public FileResource(Path filepath) {
        this.filepath = filepath;
    }

    @Override
    public byte[] getData() {
        try {
            return Files.readAllBytes(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setData(byte[] data) {
        try {
            File file = new File(filepath.toString());
            FileOutputStream fileStream = new FileOutputStream(file, false);
            fileStream.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
