package click.opentofu.sprout.handler.python.interfaces;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public interface PythonHandler {
    
    default void mainWorkerPython (Path filePath, String mergeString) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, mergeString.getBytes(), StandardOpenOption.CREATE_NEW);
        } else {
            Files.write(filePath, mergeString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    default String buildAwsPythonFile (String ROOT_PATH, String authEmailId, String uuid, String region, String awsAccessKey, String awsSecretAccessKey, String awsSessionToken) { return null; };
}
