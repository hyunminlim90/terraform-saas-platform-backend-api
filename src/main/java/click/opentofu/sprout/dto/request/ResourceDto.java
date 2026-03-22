package click.opentofu.sprout.dto.request;

import java.util.List;
import java.util.Map;

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
public class ResourceDto {
    private Map<String, Object> token;
    private String regionCode;
    private String authUserIndex;
    private String resourceSaveName;
    private Map<String, Object> resource;
    private Boolean isNextCallable;
    private List<Map<String, Object>> draftVersion;
    private List<List<Map<String, Object>>> duplicateDraftVersion;
    private String uniqueId;
    private String operation;
    private String moduleName;
}
