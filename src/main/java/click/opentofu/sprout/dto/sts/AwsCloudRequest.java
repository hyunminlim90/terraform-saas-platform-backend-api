package click.opentofu.sprout.dto.sts;

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
public class AwsCloudRequest {
    /*
     * 
     * 백엔드로 api request body 으로 데이터를 보낼 때 프론트에서는 객체를 object 로 칭하기 때문에 파라미터 이름을 *OjbectList 로 사용 함. (자바에서는 Map)
     * 
     * selectedAccountObjectList 의 객체 타입 (리액트 코드)
        export interface AwsAccountList {
            accountId: string;
            alias: string;
            roleName: string;
            authEmail: string;
            awsAccessKey: string;
            awsSecretAccessKey: string;
            awsSessionToken: string;
            createdAt: string;
            updatedAt: string;
        }
     */
    private List<Map<String, Object>> selectedAccountObjectList;
    private List<String> selectedRegions;
    private String selectedScriptTypeValue;
    // 현재 사용되고 있지 않는 것 같은데 어디에 사용되는거지 ? [delete]
    private String uniqueId;
    private Boolean isNextCallable;
}
