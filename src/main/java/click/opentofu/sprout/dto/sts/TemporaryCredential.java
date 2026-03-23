package click.opentofu.sprout.dto.sts;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder
public class TemporaryCredential {
    private String authEmailId;
    private String awsAccessKey;
    private String awsSecretAccessKey;
    private String awsSessionToken;
    private Boolean isNextCallable;
    
    @Builder.Default
    private String uuid = UUID.randomUUID().toString();
}
