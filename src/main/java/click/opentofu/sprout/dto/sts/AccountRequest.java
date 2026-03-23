package click.opentofu.sprout.dto.sts;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder
public class AccountRequest {
    private String accountId;
    private String alias;
    private String roleName;
    private String authEmailId;
    private String awsAccessKey;
    private String awsSecretAccessKey;
    private String awsSessionToken;
    private String createdAt;
    private String updatedAt;
}
