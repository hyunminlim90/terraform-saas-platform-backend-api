package click.opentofu.sprout.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionUtils {

    private static final Set<String> TOO_MANY_REQUEST_ERRORS = Set.of(
        "exec_tofu_apply_semaphore_queue_is_full",
        "exec_tofu_plan_semaphore_queue_is_full",
        "exec_tofu_destroy_semaphore_queue_is_full",
        "exec_tofu_plan_destroy_semaphore_queue_is_full"
    );

    public static ResponseEntity<Map<String, Object>> handleAsyncException(Throwable exception) {
        if (exception instanceof CompletionException) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                return buildErrorResponse(cause);
            } else {
                return buildUnknownError(exception);
            }
        }

        Throwable cause = exception.getCause();
        if (cause != null) {
            return buildErrorResponse(cause);
        } else {
            return buildUnknownError(exception);
        }
    }

    private static ResponseEntity<Map<String, Object>> buildErrorResponse(Throwable cause) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorType", cause.getClass().getSimpleName());
        errorResponse.put("result", cause.getMessage());

        if (cause instanceof IOException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else if (cause instanceof SQLException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } else if (cause instanceof InterruptedException) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        } else if (cause instanceof RuntimeException) {

            String message = cause.getMessage();

            if (
                "is_authorized_for_write".equals(message) ||
                "general_utils_validate_allowed_email_id_unauthorized_email".equals(message) )
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            if (TOO_MANY_REQUEST_ERRORS.contains(message)) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
            } 

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private static ResponseEntity<Map<String, Object>> buildUnknownError(Throwable exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorType", exception.getClass().getSimpleName());
        errorResponse.put("result", "Unknown error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
