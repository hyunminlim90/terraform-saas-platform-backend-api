package click.opentofu.sprout.handler.entity.implementations.instance;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.instance.entity.InstanceEntity;
import click.opentofu.sprout.handler.entity.entity.instance.relation.EbsDevTagGroup;
import click.opentofu.sprout.handler.entity.entity.instance.relation.PrivateIpAddressGroup;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_instance")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final InstanceRepository instanceRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            InstanceEntity instanceEntity = InstanceEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .instanceId(createOnlyId)
                // .instanceArn(parameters.path("instance_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .ami(parameters.path("ami").asText())
                .instanceType(parameters.path("instance_type").asText())
                .availabilityZone(parameters.path("availability_zone").asText())
                .subnetId(parameters.path("subnet_id").asText())
                .privateIp(parameters.path("private_ip").asText())
                .keyName(parameters.path("key_name").asText())
                .iamInstanceProfile(parameters.path("iam_instance_profile").asText())
                .tenancy(parameters.path("tenancy").asText())
                .hostId(parameters.path("host_id").asText())
                .placementGroup(parameters.path("placement_group").asText())
                .placementGroupId(parameters.path("placement_group_id").asText())
                .hostResourceGroupArn(parameters.path("host_resource_group_arn").asText())
                .marketType(parameters.path("market_type").asText())
                .instanceInterruptionBehavior(parameters.path("instance_interruption_behavior").asText())
                .maxPrice(parameters.path("max_price").asText())
                .spotInstanceType(parameters.path("spot_instance_type").asText())
                .validUntil(parameters.path("valid_until").asText())
                .ltId(parameters.path("lt_id").asText())
                .ltName(parameters.path("lt_name").asText())
                .ltVersion(parameters.path("lt_version").asText())
                .networkInterfaceId(parameters.path("network_interface_id").asText())
                .capacityReservationPreference(parameters.path("capacity_reservation_preference").asText())
                .capacityReservationId(parameters.path("capacity_reservation_id").asText())
                .capacityReservationResourceGroupArn(parameters.path("capacity_reservation_resource_group_arn").asText())
                .amdSevSnp(parameters.path("amd_sev_snp").asText())
                .nestedVirtualization(parameters.path("nested_virtualization").asText())
                .cpuCredits(parameters.path("cpu_credits").asText())
                .autoRecovery(parameters.path("auto_recovery").asText())
                .httpEndpoint(parameters.path("http_endpoint").asText())
                .httpProtocolIpv6(parameters.path("http_protocol_ipv6").asText())
                .httpTokens(parameters.path("http_tokens").asText())
                .instanceMetadataTags(parameters.path("instance_metadata_tags").asText())
                .hostnameType(parameters.path("hostname_type").asText())
                .rootDevKmsKeyId(parameters.path("root_dev_kms_key_id").asText())
                .rootDevVolumeType(parameters.path("root_dev_volume_type").asText())
                .ipv6AddressCount(parameters.path("ipv6_address_count").asText())
                .placementPartitionNumber(parameters.path("placement_partition_number").asText())
                .coreCount(parameters.path("core_count").asText())
                .threadsPerCore(parameters.path("threads_per_core").asText())
                .httpPutResponseHopLimit(parameters.path("http_put_response_hop_limit").asText())
                .rootDevIops(parameters.path("root_dev_iops").asText())
                .rootDevThroughput(parameters.path("root_dev_throughput").asText())
                .rootDevVolumeSize(parameters.path("root_dev_volume_size").asText())
                .userData(parameters.path("user_data").asText())
                .associatePublicIpAddress(parameters.path("associate_public_ip_address").asBoolean())
                .disableApiStop(parameters.path("disable_api_stop").asBoolean())
                .disableApiTermination(parameters.path("disable_api_termination").asBoolean())
                .ebsOptimized(parameters.path("ebs_optimized").asBoolean())
                .forceDestroy(parameters.path("force_destroy").asBoolean())
                .getPasswordData(parameters.path("get_password_data").asBoolean())
                .hibernation(parameters.path("hibernation").asBoolean())
                .monitoring(parameters.path("monitoring").asBoolean())
                .sourceDestCheck(parameters.path("source_dest_check").asBoolean())
                .userDataReplaceOnChange(parameters.path("user_data_replace_on_change").asBoolean())
                .enablePrimaryIpv6(parameters.path("enable_primary_ipv6").asBoolean())
                .priEniDeleteOnTermination(parameters.path("pri_eni_delete_on_termination").asBoolean())
                .enabled(parameters.path("enabled").asBoolean())
                .enableResourceNameDnsAaaaRecord(parameters.path("enable_resource_name_dns_aaaa_record").asBoolean())
                .enableResourceNameDnsARecord(parameters.path("enable_resource_name_dns_a_record").asBoolean())
                .rootDevDeleteOnTermination(parameters.path("root_dev_delete_on_termination").asBoolean())
                .rootDevEncrypted(parameters.path("root_dev_encrypted").asBoolean())
                .rootDevTags(parseRootDevTags(parameters.path("root_dev_tags"))) 
                .volumeTags(parseVolumeTags(parameters.path("volume_tags"))) 
                .ipv6Addresses(parseToListString(parameters.path("ipv6_addresses")))
                .secondaryPrivateIps(parseToListString(parameters.path("secondary_private_ips")))
                .vpcSecurityGroupIds(parseToListString(parameters.path("vpc_security_group_ids")))
                .securityGroups(parseToListString(parameters.path("security_groups")))
                .ebsDevDeviceName(parseToListString(parameters.path("ebs_dev_device_name")))
                .ebsDevKmsKeyId(parseToListString(parameters.path("ebs_dev_kms_key_id")))
                .ebsDevSnapshotId(parseToListString(parameters.path("ebs_dev_snapshot_id")))
                .ebsDevVolumeType(parseToListString(parameters.path("ebs_dev_volume_type")))
                .ephDevDeviceName(parseToListString(parameters.path("eph_dev_device_name")))
                .virtualName(parseToListString(parameters.path("virtual_name")))
                .secondarySubnetId(parseToListString(parameters.path("secondary_subnet_id")))
                .interfaceType(parseToListString(parameters.path("interface_type")))
                .ebsDevIops(parseToListString(parameters.path("ebs_dev_iops")))
                .ebsDevThroughput(parseToListString(parameters.path("ebs_dev_throughput")))
                .ebsDevVolumeSize(parseToListString(parameters.path("ebs_dev_volume_size")))
                .networkCardIndex(parseToListString(parameters.path("network_card_index")))
                .deviceIndex(parseToListString(parameters.path("device_index")))
                .privateIpAddressCount(parseToListString(parameters.path("private_ip_address_count")))
                .ebsDevDeleteOnTermination(parseToListString(parameters.path("ebs_dev_delete_on_termination")))
                .ebsDevEncrypted(parseToListString(parameters.path("ebs_dev_encrypted")))
                .noDevice(parseToListString(parameters.path("no_device")))
                .secEniDeleteOnTermination(parseToListString(parameters.path("sec_eni_delete_on_termination")))

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            List<EbsDevTagGroup> ebsDevTags = parseToEbsDevTagGroupList(parameters.path("ebs_dev_tags"), instanceEntity);
            instanceEntity.setEbsDevTags(ebsDevTags);

            List<PrivateIpAddressGroup> privateIpAddresses = parseToPrivateIpAddressGroupList(parameters.path("private_ip_addresses"), instanceEntity);
            instanceEntity.setPrivateIpAddresses(privateIpAddresses);
            
            /** Declarations of Join Entity Converter Methods */

            instanceRepository.save(instanceEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
