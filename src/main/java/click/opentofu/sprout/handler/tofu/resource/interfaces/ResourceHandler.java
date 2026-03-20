package click.opentofu.sprout.handler.tofu.resource.interfaces;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public interface ResourceHandler {
    default void mainWorkerResource (boolean fileExists, Path filePath, String mergeString) throws IOException {
        if (!fileExists) {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, mergeString.getBytes(), StandardOpenOption.CREATE_NEW);
        } else {
            Files.write(filePath, mergeString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    default String buildTofuResourceMainFile (String region, String account) { return null; };
}
