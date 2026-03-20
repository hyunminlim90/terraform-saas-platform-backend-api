package click.opentofu.sprout.handler.entity.implementations;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.DriverOptGroup;
import click.opentofu.sprout.handler.entity.entity.LabelGroup;
import click.opentofu.sprout.handler.entity.entity.TofuEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.TofuRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("entity_builder_sprout")
public class EntityBuilder implements EntityHandler {
    
    private final TofuRepository tofuRepository;

    @Override
    public void buildEntityAndSave(JsonNode parameters, String resourceSaveName, String authUserIndex) {
        try {
            TofuEntity tofuEntity = TofuEntity.builder()
                .resourceSaveName(resourceSaveName)
                .tenantId(authUserIndex)

                .taskId(parameters.path("task_id").asText())
                .taskArn(parameters.path("task_arn").asText())
                .accountId(parameters.path("account_id").asText())
                .region(parameters.path("region").asText())
                .name(parameters.path("task_id").asText())

                /** Declarations for DTO-to-Entity Mappings */

                .containerDefinitions(parameters.path("container_definitions").asText())
                .cpu(parameters.path("cpu").asText())
                .enableFaultInjection(parameters.path("enable_fault_injection").asBoolean())
                .sizeInGib(parameters.path("size_in_gib").asText())
                .executionRoleArn(parameters.path("execution_role_arn").asText())
                .family(parameters.path("family").asText())
                .ipcMode(parameters.path("ipc_mode").asText())
                .memory(parameters.path("memory").asText())
                .networkMode(parameters.path("network_mode").asText())
                .pidMode(parameters.path("pid_mode").asText())
                .expression(parseToListString(parameters.path("expression")))
                .constraintsType(parseToListString(parameters.path("constraints_type")))
                .containerName(parameters.path("container_name").asText())
                .proxyType(parameters.path("proxy_type").asText())
                .appPorts(parameters.path("app_ports").asText())
                .egressIgnoredIps(parameters.path("egress_ignored_ips").asText())
                .ignoredUid(parameters.path("ignored_uid").asText())
                .proxyEgressPort(parameters.path("proxy_egress_port").asText())
                .proxyIngressPort(parameters.path("proxy_ingress_port").asText())
                .requiresCompatibilities(parseToListString(parameters.path("requires_compatibilities")))
                .operatingSystemFamily(parameters.path("operating_system_family").asText())
                .cpuArchitecture(parameters.path("cpu_architecture").asText())
                .skipDestroy(parameters.path("skip_destroy").asBoolean())
                .taskRoleArn(parameters.path("task_role_arn").asText())
                .trackLatest(parameters.path("track_latest").asBoolean())
                .volumeName(parseToListString(parameters.path("volume_name")))
                .hostPath(parseToListString(parameters.path("host_path")))
                .configureAtLaunch(parseToListString(parameters.path("configure_at_launch")))
                .autoprovision(parseToListString(parameters.path("autoprovision")))
                .driver(parseToListString(parameters.path("driver")))
                .scope(parseToListString(parameters.path("scope")))
                .efsFileSystemId(parseToListString(parameters.path("efs_file_system_id")))
                .efsRootDirectory(parseToListString(parameters.path("efs_root_directory")))
                .transitEncryption(parseToListString(parameters.path("transit_encryption")))
                .transitEncryptionPort(parseToListString(parameters.path("transit_encryption_port")))
                .accessPointId(parseToListString(parameters.path("access_point_id")))
                .iam(parseToListString(parameters.path("iam")))
                .fsxFileSystemId(parseToListString(parameters.path("fsx_file_system_id")))
                .fsxRootDirectory(parseToListString(parameters.path("fsx_root_directory")))
                .credentialsParameter(parseToListString(parameters.path("credentials_parameter")))
                .domain(parseToListString(parameters.path("domain")))

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            List<LabelGroup> labels = parseToLabelGroupList(parameters.path("labels"), tofuEntity);
            tofuEntity.setLabels(labels);

            List<DriverOptGroup> driverOpts = parseToDriverOptGroupList(parameters.path("driver_opts"), tofuEntity);
            tofuEntity.setDriverOpts(driverOpts);

            tofuRepository.save(tofuEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
