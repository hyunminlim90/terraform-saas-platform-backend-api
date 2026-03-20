package click.opentofu.sprout.util;

import java.io.IOException;

public class AutoCloseUtils implements AutoCloseable {
    
    private final Process process;

    public AutoCloseUtils (ProcessBuilder processBuilder) throws IOException {
        this.process = processBuilder.start();
    }

    public Process getProcess() {
        return this.process;
    }

    public void close() {
        this.process.destroy();
    }
}
