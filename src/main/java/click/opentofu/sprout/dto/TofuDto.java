package click.opentofu.sprout.dto;

import java.time.LocalDateTime;
import java.util.Map;

import click.opentofu.sprout.handler.entity.entity.TofuEntity;

public record TofuDto (

    String tenantId,
    String taskId,
    String taskArn,
    String accountId,
    String region,
    String name,

    /** Record component definitions */

    String containerDefinitions,
    String cpu,
    Boolean enableFaultInjection,
    String sizeInGib,
    String executionRoleArn,
    String family,
    String ipcMode,
    String memory,
    String networkMode,
    String pidMode,

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version
) {
    public static TofuDto from(TofuEntity e) {
        return new TofuDto(

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getTaskId(),
            e.getTaskArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),

            /** JavaBeans getter method declaration */

            e.getContainerDefinitions(),
            e.getCpu(),
            e.getEnableFaultInjection(),
            e.getSizeInGib(),
            e.getExecutionRoleArn(),
            e.getFamily(),
            e.getIpcMode(),
            e.getMemory(),
            e.getNetworkMode(),
            e.getPidMode(),
            
            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
