package click.opentofu.sprout.handler.tofu.module.interfaces;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public interface ModuleHandler {
    default void mainWorkerModule (boolean fileExists, Path filePath, String mergeString) throws IOException {
        if (!fileExists) {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, mergeString.getBytes(), StandardOpenOption.CREATE_NEW);
        } else {
            Files.write(filePath, mergeString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    default String buildTofuModuleMainFile (String region, String access_key, String secret_key, String token) { return null; };
    default String buildTofuModuleConfigFile (List<Map<String, Object>> resourceConfigs, String operation) { return null; };
    default String buildTofuModuleStateFile (List<Map<String, Object>> resourceConfigs) { return null; };
    default String buildTofuTextareaFile (String textarea) { return null; };
}
