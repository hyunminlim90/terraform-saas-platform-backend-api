package click.opentofu.sprout.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProcessUtils {
    
    public void execTofuCommand(
        List<String> command,
        Path workingDir,
        SseEmitter emitter
    ) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir.toFile());
        builder.redirectErrorStream(true);

        String joinedCommand = String.join(" ", command);

        log.info("=================================================================");
        log.info("▶ Starting command: {}...", joinedCommand);
        log.info("=================================================================");

        try {
            emitter.send(SseEmitter.event().data("▶ Executing: " + joinedCommand + "\n"));

            try (AutoCloseUtils autoCloseUtils = new AutoCloseUtils(builder)) {

                Process process = autoCloseUtils.getProcess();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        emitter.send(SseEmitter.event().data(line));
                        log.info("📦 {}", line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    String errorMsg = String.format("❌ %s failed with exit code %d\n", joinedCommand, exitCode);
                    emitter.send(SseEmitter.event().data(errorMsg));
                }

                emitter.send(SseEmitter.event().data("\n✅ " + joinedCommand + " completed\n"));
            }
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
