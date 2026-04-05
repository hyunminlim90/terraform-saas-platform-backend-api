package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_instance")
public class TofuModuleInstance implements ModuleHandler {

    @Override
    public String buildTofuModuleMainFile(String region, String access_key, String secret_key, String token) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("provider \"aws\" {\n")
            .append("    region     = \"" + region + "\"\n")
            .append("    access_key = \"" + access_key + "\"\n")
            .append("    secret_key = \"" + secret_key + "\"\n")
            .append("    token      = \"" + token + "\"\n")
            .append("}\n\n")
            .append("locals {\n")
            .append("    context = yamldecode(file(\"./config.yaml\")).context\n")
            .append("    config  = yamldecode(templatefile(\"./config.yaml\", local.context))\n")
            .append("}\n\n")
            /**
             * 
             * 디버깅 시 locals.config 도 주석 처리 필요.
             */
            // .append("output \"debug\" {\n")
            // .append("    value = templatefile(\"./config.yaml\", local.context)\n")
            // .append("}\n\n")
            .append("module \"aws_instance\" {\n")
            .append("    source = \"../../tofu_resource/aws_instance\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_instance = {\n")
            .append("        for index, aws_instance in try(each.value.aws_instance, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_instance.name)}\" => {\n")

            /** Start of Substitution */

            .append("                ami                                     = aws_instance.ami\n")
            .append("                instance_type                           = aws_instance.instance_type\n")
            .append("                availability_zone                       = aws_instance.availability_zone\n")
            .append("                subnet_id                               = aws_instance.subnet_id\n")
            .append("                private_ip                              = aws_instance.private_ip\n")
            .append("                key_name                                = aws_instance.key_name\n")
            .append("                iam_instance_profile                    = aws_instance.iam_instance_profile\n")
            .append("                tenancy                                 = aws_instance.tenancy\n")
            .append("                host_id                                 = aws_instance.host_id\n")
            .append("                placement_group                         = aws_instance.placement_group\n")
            .append("                placement_group_id                      = aws_instance.placement_group_id\n")
            .append("                host_resource_group_arn                 = aws_instance.host_resource_group_arn\n")
            .append("                ipv6_address_count                      = aws_instance.ipv6_address_count\n")
            .append("                placement_partition_number              = aws_instance.placement_partition_number\n")
            .append("                associate_public_ip_address             = aws_instance.associate_public_ip_address\n")
            .append("                disable_api_stop                        = aws_instance.disable_api_stop\n")
            .append("                disable_api_termination                 = aws_instance.disable_api_termination\n")
            .append("                ebs_optimized                           = aws_instance.ebs_optimized\n")
            .append("                force_destroy                           = aws_instance.force_destroy\n")
            .append("                get_password_data                       = aws_instance.get_password_data\n")
            .append("                hibernation                             = aws_instance.hibernation\n")
            .append("                monitoring                              = aws_instance.monitoring\n")
            .append("                source_dest_check                       = aws_instance.source_dest_check\n")
            .append("                user_data_replace_on_change             = aws_instance.user_data_replace_on_change\n")
            .append("                enable_primary_ipv6                     = aws_instance.enable_primary_ipv6\n")
            .append("                ipv6_addresses                          = aws_instance.ipv6_addresses\n")
            .append("                secondary_private_ips                   = aws_instance.secondary_private_ips\n")
            .append("                vpc_security_group_ids                  = aws_instance.vpc_security_group_ids\n")
            .append("                security_groups                         = aws_instance.security_groups\n")
            .append("                user_data                               = aws_instance.user_data\n")
            .append("                market_type                             = aws_instance.market_type\n")
            .append("                instance_interruption_behavior          = aws_instance.instance_interruption_behavior\n")
            .append("                max_price                               = aws_instance.max_price\n")
            .append("                spot_instance_type                      = aws_instance.spot_instance_type\n")
            .append("                valid_until                             = aws_instance.valid_until\n")
            .append("                lt_id                                   = aws_instance.lt_id\n")
            .append("                lt_name                                 = aws_instance.lt_name\n")
            .append("                lt_version                              = aws_instance.lt_version\n")
            .append("                network_interface_id                    = aws_instance.network_interface_id\n")
            .append("                pri_eni_delete_on_termination           = aws_instance.pri_eni_delete_on_termination\n")
            .append("                capacity_reservation_preference         = aws_instance.capacity_reservation_preference\n")
            .append("                capacity_reservation_id                 = aws_instance.capacity_reservation_id\n")
            .append("                capacity_reservation_resource_group_arn = aws_instance.capacity_reservation_resource_group_arn\n")
            .append("                amd_sev_snp                             = aws_instance.amd_sev_snp\n")
            .append("                core_count                              = aws_instance.core_count\n")
            .append("                nested_virtualization                   = aws_instance.nested_virtualization\n")
            .append("                threads_per_core                        = aws_instance.threads_per_core\n")
            .append("                cpu_credits                             = aws_instance.cpu_credits\n")
            .append("                enabled                                 = aws_instance.enabled\n")
            .append("                auto_recovery                           = aws_instance.auto_recovery\n")
            .append("                http_endpoint                           = aws_instance.http_endpoint\n")
            .append("                http_protocol_ipv6                      = aws_instance.http_protocol_ipv6\n")
            .append("                http_put_response_hop_limit             = aws_instance.http_put_response_hop_limit\n")
            .append("                http_tokens                             = aws_instance.http_tokens\n")
            .append("                instance_metadata_tags                  = aws_instance.instance_metadata_tags\n")
            .append("                enable_resource_name_dns_aaaa_record    = aws_instance.enable_resource_name_dns_aaaa_record\n")
            .append("                enable_resource_name_dns_a_record       = aws_instance.enable_resource_name_dns_a_record\n")
            .append("                hostname_type                           = aws_instance.hostname_type\n")
            .append("                root_dev_delete_on_termination          = aws_instance.root_dev_delete_on_termination\n")
            .append("                root_dev_encrypted                      = aws_instance.root_dev_encrypted\n")
            .append("                root_dev_iops                           = aws_instance.root_dev_iops\n")
            .append("                root_dev_kms_key_id                     = aws_instance.root_dev_kms_key_id\n")
            .append("                root_dev_throughput                     = aws_instance.root_dev_throughput\n")
            .append("                root_dev_volume_size                    = aws_instance.root_dev_volume_size\n")
            .append("                root_dev_volume_type                    = aws_instance.root_dev_volume_type\n")
            .append("                root_dev_tags                           = aws_instance.root_dev_tags\n")
            .append("                ebs_dev_delete_on_termination           = aws_instance.ebs_dev_delete_on_termination\n")
            .append("                ebs_dev_device_name                     = aws_instance.ebs_dev_device_name\n")
            .append("                ebs_dev_encrypted                       = aws_instance.ebs_dev_encrypted\n")
            .append("                ebs_dev_iops                            = aws_instance.ebs_dev_iops\n")
            .append("                ebs_dev_kms_key_id                      = aws_instance.ebs_dev_kms_key_id\n")
            .append("                ebs_dev_snapshot_id                     = aws_instance.ebs_dev_snapshot_id\n")
            .append("                ebs_dev_throughput                      = aws_instance.ebs_dev_throughput\n")
            .append("                ebs_dev_volume_size                     = aws_instance.ebs_dev_volume_size\n")
            .append("                ebs_dev_volume_type                     = aws_instance.ebs_dev_volume_type\n")
            .append("                ebs_dev_tags                            = aws_instance.ebs_dev_tags\n")
            .append("                eph_dev_device_name                     = aws_instance.eph_dev_device_name\n")
            .append("                virtual_name                            = aws_instance.virtual_name\n")
            .append("                no_device                               = aws_instance.no_device\n")
            .append("                secondary_subnet_id                     = aws_instance.secondary_subnet_id\n")
            .append("                network_card_index                      = aws_instance.network_card_index\n")
            .append("                device_index                            = aws_instance.device_index\n")
            .append("                interface_type                          = aws_instance.interface_type\n")
            .append("                sec_eni_delete_on_termination           = aws_instance.sec_eni_delete_on_termination\n")
            .append("                private_ip_address_count                = aws_instance.private_ip_address_count\n")
            .append("                private_ip_addresses                    = aws_instance.private_ip_addresses\n")
            .append("                volume_tags                             = aws_instance.volume_tags\n")
            .append("                tags                                    = aws_instance.tags\n")

            /** End of Substitution */

            .append("            }\n")
            .append("    }\n")
            .append("}\n");
        return definedPythonFile.toString();
    }

    @Override
    public String buildTofuModuleConfigFile(List<Map<String, Object>> resourceConfigs, String operation) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/instance#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String instanceId = (String) resourceConfig.get("instance_id");
                if (!instanceId.contains("create-only-")) {
                    if (instanceId.startsWith("arn:")) {
                        String resource = instanceId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        instanceId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + instanceId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_instance_id");
            }
        }

        /** Start of YAML Field Mapping */

        definedPythonFile
            .append("    ami:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ami") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ami");
                    }
                }

        definedPythonFile
            .append("    instance_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("instance_type") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_instance_type");
                    }
                }

        definedPythonFile
            .append("    availability_zone:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("availability_zone") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_availability_zone");
                    }
                }

        definedPythonFile
            .append("    subnet_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("subnet_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_subnet_id");
                    }
                }

        definedPythonFile
            .append("    private_ip:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("private_ip") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_private_ip");
                    }
                }

        definedPythonFile
            .append("    key_name:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("key_name") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_key_name");
                    }
                }

        definedPythonFile
            .append("    iam_instance_profile:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("iam_instance_profile") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_iam_instance_profile");
                    }
                }

        definedPythonFile
            .append("    tenancy:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("tenancy") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_tenancy");
                    }
                }

        definedPythonFile
            .append("    host_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("host_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_host_id");
                    }
                }

        definedPythonFile
            .append("    placement_group:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("placement_group") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_placement_group");
                    }
                }

        definedPythonFile
            .append("    placement_group_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("placement_group_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_placement_group_id");
                    }
                }

        definedPythonFile
            .append("    host_resource_group_arn:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("host_resource_group_arn") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_host_resource_group_arn");
                    }
                }

        definedPythonFile
            .append("    market_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("market_type") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_market_type");
                    }
                }

        definedPythonFile
            .append("    instance_interruption_behavior:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("instance_interruption_behavior") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_instance_interruption_behavior");
                    }
                }

        definedPythonFile
            .append("    max_price:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("max_price") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_max_price");
                    }
                }

        definedPythonFile
            .append("    spot_instance_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("spot_instance_type") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_spot_instance_type");
                    }
                }

        definedPythonFile
            .append("    valid_until:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("valid_until") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_valid_until");
                    }
                }

        definedPythonFile
            .append("    lt_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("lt_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_lt_id");
                    }
                }

        definedPythonFile
            .append("    lt_name:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("lt_name") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_lt_name");
                    }
                }

        definedPythonFile
            .append("    lt_version:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("lt_version") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_lt_version");
                    }
                }

        definedPythonFile
            .append("    network_interface_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("network_interface_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_network_interface_id");
                    }
                }

        definedPythonFile
            .append("    capacity_reservation_preference:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("capacity_reservation_preference") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_capacity_reservation_preference");
                    }
                }

        definedPythonFile
            .append("    capacity_reservation_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("capacity_reservation_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_capacity_reservation_id");
                    }
                }

        definedPythonFile
            .append("    capacity_reservation_resource_group_arn:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("capacity_reservation_resource_group_arn") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_capacity_reservation_resource_group_arn");
                    }
                }

        definedPythonFile
            .append("    amd_sev_snp:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("amd_sev_snp") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_amd_sev_snp");
                    }
                }

        definedPythonFile
            .append("    nested_virtualization:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("nested_virtualization") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_nested_virtualization");
                    }
                }

        definedPythonFile
            .append("    cpu_credits:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("cpu_credits") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_cpu_credits");
                    }
                }

        definedPythonFile
            .append("    auto_recovery:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("auto_recovery") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_auto_recovery");
                    }
                }

        definedPythonFile
            .append("    http_endpoint:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("http_endpoint") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_http_endpoint");
                    }
                }

        definedPythonFile
            .append("    http_protocol_ipv6:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("http_protocol_ipv6") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_http_protocol_ipv6");
                    }
                }

        definedPythonFile
            .append("    http_tokens:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("http_tokens") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_http_tokens");
                    }
                }

        definedPythonFile
            .append("    instance_metadata_tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("instance_metadata_tags") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_instance_metadata_tags");
                    }
                }

        definedPythonFile
            .append("    hostname_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("hostname_type") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_hostname_type");
                    }
                }

        definedPythonFile
            .append("    root_dev_kms_key_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_kms_key_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_kms_key_id");
                    }
                }

        definedPythonFile
            .append("    root_dev_volume_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_volume_type") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_volume_type");
                    }
                }

        definedPythonFile
            .append("    ipv6_address_count:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv6_address_count") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_address_count");
                    }
                }

        definedPythonFile
            .append("    placement_partition_number:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("placement_partition_number") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_placement_partition_number");
                    }
                }

        definedPythonFile
            .append("    core_count:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("core_count") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_core_count");
                    }
                }

        definedPythonFile
            .append("    threads_per_core:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("threads_per_core") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_threads_per_core");
                    }
                }

        definedPythonFile
            .append("    http_put_response_hop_limit:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("http_put_response_hop_limit") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_http_put_response_hop_limit");
                    }
                }

        definedPythonFile
            .append("    root_dev_iops:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_iops") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_iops");
                    }
                }

        definedPythonFile
            .append("    root_dev_throughput:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_throughput") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_throughput");
                    }
                }

        definedPythonFile
            .append("    root_dev_volume_size:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_volume_size") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_volume_size");
                    }
                }

        definedPythonFile
            .append("    associate_public_ip_address:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("associate_public_ip_address") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_associate_public_ip_address");
                    }
                }

        definedPythonFile
            .append("    disable_api_stop:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("disable_api_stop") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_disable_api_stop");
                    }
                }

        definedPythonFile
            .append("    disable_api_termination:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("disable_api_termination") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_disable_api_termination");
                    }
                }

        definedPythonFile
            .append("    ebs_optimized:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ebs_optimized") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_optimized");
                    }
                }

        definedPythonFile
            .append("    force_destroy:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("force_destroy") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_force_destroy");
                    }
                }

        definedPythonFile
            .append("    get_password_data:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("get_password_data") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_get_password_data");
                    }
                }

        definedPythonFile
            .append("    hibernation:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("hibernation") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_hibernation");
                    }
                }

        definedPythonFile
            .append("    monitoring:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("monitoring") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_monitoring");
                    }
                }

        definedPythonFile
            .append("    source_dest_check:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("source_dest_check") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_source_dest_check");
                    }
                }

        definedPythonFile
            .append("    user_data_replace_on_change:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("user_data_replace_on_change") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_user_data_replace_on_change");
                    }
                }

        definedPythonFile
            .append("    enable_primary_ipv6:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_primary_ipv6") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_primary_ipv6");
                    }
                }

        definedPythonFile
            .append("    pri_eni_delete_on_termination:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("pri_eni_delete_on_termination") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_pri_eni_delete_on_termination");
                    }
                }

        definedPythonFile
            .append("    enabled:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enabled") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enabled");
                    }
                }

        definedPythonFile
            .append("    enable_resource_name_dns_aaaa_record:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_resource_name_dns_aaaa_record") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_resource_name_dns_aaaa_record");
                    }
                }

        definedPythonFile
            .append("    enable_resource_name_dns_a_record:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_resource_name_dns_a_record") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_resource_name_dns_a_record");
                    }
                }

        definedPythonFile
            .append("    root_dev_delete_on_termination:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_delete_on_termination") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_delete_on_termination");
                    }
                }

        definedPythonFile
            .append("    root_dev_encrypted:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("root_dev_encrypted") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_encrypted");
                    }
                }

        definedPythonFile
            .append("    user_data:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        String userData = (String) resourceConfig.get("user_data");
                        if (userData == null || userData.isEmpty()) {
                            definedPythonFile
                                .append("    - \"\"\n");
                        } else {
                            String instanceId = (String) resourceConfig.get("instance_id");
                            if (!instanceId.contains("create-only-")) {
                                if (instanceId.startsWith("arn:")) {
                                    String resource = instanceId.split(":", 6)[5];
                                    String[] tokens = resource.split("[/:]");
                                    instanceId = String.join("-", tokens);
                                }
                            }
                            definedPythonFile
                                .append("    - \"" + instanceId + "_userData.sh\"\n");
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_user_data");
                    }
                }

        definedPythonFile
            .append("    root_dev_tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> rootDevTags = (Map<String, Object>) resourceConfig.get("root_dev_tags");
                        if (rootDevTags != null && !rootDevTags.isEmpty()) {
                            String rootDevTagResult = rootDevTags.entrySet().stream() 
                                .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                .collect(Collectors.joining("{{__,__}}"));
                            definedPythonFile.append("        - \"" + rootDevTagResult + "\"\n");
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Root Dev Tag does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_root_dev_tags");
                    }
                }

        definedPythonFile
            .append("    volume_tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> volumeTags = (Map<String, Object>) resourceConfig.get("volume_tags");
                        if (volumeTags != null && !volumeTags.isEmpty()) {
                            String volumeTagResult = volumeTags.entrySet().stream() 
                                .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                .collect(Collectors.joining("{{__,__}}"));
                            definedPythonFile.append("        - \"" + volumeTagResult + "\"\n");
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Volume Tag does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_volume_tags");
                    }
                }

        definedPythonFile
            .append("    ipv6_addresses:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ipv6Addresses = (List<Object>) resourceConfig.get("ipv6_addresses");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ipv6Addresses.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_addresses");
                    }
                }

        definedPythonFile
            .append("    secondary_private_ips:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> secondaryPrivateIps = (List<Object>) resourceConfig.get("secondary_private_ips");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", secondaryPrivateIps.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_secondary_private_ips");
                    }
                }

        definedPythonFile
            .append("    vpc_security_group_ids:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> vpcSecurityGroupIds = (List<Object>) resourceConfig.get("vpc_security_group_ids");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", vpcSecurityGroupIds.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_vpc_security_group_ids");
                    }
                }
                
        definedPythonFile
            .append("    security_groups:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> securityGroups = (List<Object>) resourceConfig.get("security_groups");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", securityGroups.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_security_groups");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_device_name:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevDeviceName = (List<Object>) resourceConfig.get("ebs_dev_device_name");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevDeviceName.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_device_name");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_kms_key_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevKmsKeyId = (List<Object>) resourceConfig.get("ebs_dev_kms_key_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevKmsKeyId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_kms_key_id");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_snapshot_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevSnapshotId = (List<Object>) resourceConfig.get("ebs_dev_snapshot_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevSnapshotId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_snapshot_id");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_volume_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevVolumeType = (List<Object>) resourceConfig.get("ebs_dev_volume_type");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevVolumeType.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_volume_type");
                    }
                }

        definedPythonFile
            .append("    eph_dev_device_name:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ephDevDeviceName = (List<Object>) resourceConfig.get("eph_dev_device_name");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ephDevDeviceName.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_eph_dev_device_name");
                    }
                }
        
        definedPythonFile
            .append("    virtual_name:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> virtualName = (List<Object>) resourceConfig.get("virtual_name");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", virtualName.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_virtual_name");
                    }
                }

        definedPythonFile
            .append("    secondary_subnet_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> secondarySubnetId = (List<Object>) resourceConfig.get("secondary_subnet_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", secondarySubnetId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_secondary_subnet_id");
                    }
                }
            
        definedPythonFile
            .append("    interface_type:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> interfaceType = (List<Object>) resourceConfig.get("interface_type");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", interfaceType.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_interface_type");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_iops:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevIops = (List<Object>) resourceConfig.get("ebs_dev_iops");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevIops.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_iops");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_throughput:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevThroughput = (List<Object>) resourceConfig.get("ebs_dev_throughput");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevThroughput.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_throughput");
                    }
                }
        
        definedPythonFile
            .append("    ebs_dev_volume_size:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevVolumeSize = (List<Object>) resourceConfig.get("ebs_dev_volume_size");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevVolumeSize.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_volume_size");
                    }
                }
        
        definedPythonFile
            .append("    network_card_index:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> networkCardIndex = (List<Object>) resourceConfig.get("network_card_index");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", networkCardIndex.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_network_card_index");
                    }
                }

        definedPythonFile
            .append("    device_index:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> deviceIndex = (List<Object>) resourceConfig.get("device_index");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", deviceIndex.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_device_index");
                    }
                }

        definedPythonFile
            .append("    private_ip_address_count:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> privateIpAddressCount = (List<Object>) resourceConfig.get("private_ip_address_count");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", privateIpAddressCount.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_private_ip_address_count");
                    }
                }
            
        definedPythonFile
            .append("    ebs_dev_delete_on_termination:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevDeleteOnTermination = (List<Object>) resourceConfig.get("ebs_dev_delete_on_termination");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevDeleteOnTermination.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_delete_on_termination");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_encrypted:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ebsDevEncrypted = (List<Object>) resourceConfig.get("ebs_dev_encrypted");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ebsDevEncrypted.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_encrypted");
                    }
                }

        definedPythonFile
            .append("    no_device:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> noDevice = (List<Object>) resourceConfig.get("no_device");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", noDevice.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_no_device");
                    }
                }

        definedPythonFile
            .append("    sec_eni_delete_on_termination:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> secEniDeleteOnTermination = (List<Object>) resourceConfig.get("sec_eni_delete_on_termination");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", secEniDeleteOnTermination.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_sec_eni_delete_on_termination");
                    }
                }

        definedPythonFile
            .append("    ebs_dev_tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> ebsDevTags = (List<Map<String, Object>>) resourceConfig.get("ebs_dev_tags");
                        if (ebsDevTags != null && !ebsDevTags.isEmpty()) {
                            List<String> ebsDevTagStrings = new ArrayList<>();
                            for (Map<String, Object> item : ebsDevTags) {
                                List<String> keyValuePairs = new ArrayList<>();
                                for (Map.Entry<String, Object> entry : item.entrySet()) {
                                    keyValuePairs.add(entry.getKey() + "{{__:__}}" + entry.getValue());
                                }
                                ebsDevTagStrings.add(String.join("{{__,__}}", keyValuePairs));
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", ebsDevTagStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Ebs Dev Tag does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ebs_dev_tags");
                    }
                }
        
        definedPythonFile
            .append("    private_ip_addresses:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> privateIpAddresses = (List<List<String>>) resourceConfig.get("private_ip_addresses");
                        if (privateIpAddresses != null && !privateIpAddresses.isEmpty()) {
                            List<String> privateIpAddressStrings = new ArrayList<>();
                            for (List<String> item : privateIpAddresses) {
                                String joinedInner = String.join("{{__,__}}", item);
                                privateIpAddressStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", privateIpAddressStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Private Ip Address does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_private_ip_addresses");
                    }
                }
        
        /** End of YAML Field Mapping */

        definedPythonFile
            .append("    tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> tags = (Map<String, Object>) resourceConfig.get("tags");
                        if (tags != null && !tags.isEmpty()) {
                            String tagResult = tags.entrySet().stream() 
                                .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                .collect(Collectors.joining("{{__,__}}"));
                            definedPythonFile.append("        - \"" + tagResult + "\"\n");
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Tag does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_tags");
                    }
                }
        definedPythonFile
            .append("\n")
            .append("awscloud:\n")
            .append("    \"tofu\":\n");
        if (operation != null && !operation.isBlank() && operation.contains("destroy")) {
            definedPythonFile
                .append("        \"aws_instance\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_instance\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                ami: \"${ami[" + index + "]}\",\n")
                            .append("                instance_type: \"${instance_type[" + index + "]}\",\n")
                            .append("                availability_zone: \"${availability_zone[" + index + "]}\",\n")
                            .append("                subnet_id: \"${subnet_id[" + index + "]}\",\n")
                            .append("                private_ip: \"${private_ip[" + index + "]}\",\n")
                            .append("                key_name: \"${key_name[" + index + "]}\",\n")
                            .append("                iam_instance_profile: \"${iam_instance_profile[" + index + "]}\",\n")
                            .append("                tenancy: \"${tenancy[" + index + "]}\",\n")
                            .append("                host_id: \"${host_id[" + index + "]}\",\n")
                            .append("                placement_group: \"${placement_group[" + index + "]}\",\n")
                            .append("                placement_group_id: \"${placement_group_id[" + index + "]}\",\n")
                            .append("                host_resource_group_arn: \"${host_resource_group_arn[" + index + "]}\",\n")
                            .append("                market_type: \"${market_type[" + index + "]}\",\n")
                            .append("                instance_interruption_behavior: \"${instance_interruption_behavior[" + index + "]}\",\n")
                            .append("                max_price: \"${max_price[" + index + "]}\",\n")
                            .append("                spot_instance_type: \"${spot_instance_type[" + index + "]}\",\n")
                            .append("                valid_until: \"${valid_until[" + index + "]}\",\n")
                            .append("                lt_id: \"${lt_id[" + index + "]}\",\n")
                            .append("                lt_name: \"${lt_name[" + index + "]}\",\n")
                            .append("                lt_version: \"${lt_version[" + index + "]}\",\n")
                            .append("                network_interface_id: \"${network_interface_id[" + index + "]}\",\n")
                            .append("                capacity_reservation_preference: \"${capacity_reservation_preference[" + index + "]}\",\n")
                            .append("                capacity_reservation_id: \"${capacity_reservation_id[" + index + "]}\",\n")
                            .append("                capacity_reservation_resource_group_arn: \"${capacity_reservation_resource_group_arn[" + index + "]}\",\n")
                            .append("                amd_sev_snp: \"${amd_sev_snp[" + index + "]}\",\n")
                            .append("                nested_virtualization: \"${nested_virtualization[" + index + "]}\",\n")
                            .append("                cpu_credits: \"${cpu_credits[" + index + "]}\",\n")
                            .append("                auto_recovery: \"${auto_recovery[" + index + "]}\",\n")
                            .append("                http_endpoint: \"${http_endpoint[" + index + "]}\",\n")
                            .append("                http_protocol_ipv6: \"${http_protocol_ipv6[" + index + "]}\",\n")
                            .append("                http_tokens: \"${http_tokens[" + index + "]}\",\n")
                            .append("                instance_metadata_tags: \"${instance_metadata_tags[" + index + "]}\",\n")
                            .append("                hostname_type: \"${hostname_type[" + index + "]}\",\n")
                            .append("                root_dev_kms_key_id: \"${root_dev_kms_key_id[" + index + "]}\",\n")
                            .append("                root_dev_volume_type: \"${root_dev_volume_type[" + index + "]}\",\n")
                            .append("                ipv6_address_count: \"${ipv6_address_count[" + index + "]}\",\n")
                            .append("                placement_partition_number: \"${placement_partition_number[" + index + "]}\",\n")
                            .append("                core_count: \"${core_count[" + index + "]}\",\n")
                            .append("                threads_per_core: \"${threads_per_core[" + index + "]}\",\n")
                            .append("                http_put_response_hop_limit: \"${http_put_response_hop_limit[" + index + "]}\",\n")
                            .append("                root_dev_iops: \"${root_dev_iops[" + index + "]}\",\n")
                            .append("                root_dev_throughput: \"${root_dev_throughput[" + index + "]}\",\n")
                            .append("                root_dev_volume_size: \"${root_dev_volume_size[" + index + "]}\",\n")
                            .append("                associate_public_ip_address: \"${associate_public_ip_address[" + index + "]}\",\n")
                            .append("                disable_api_stop: \"${disable_api_stop[" + index + "]}\",\n")
                            .append("                disable_api_termination: \"${disable_api_termination[" + index + "]}\",\n")
                            .append("                ebs_optimized: \"${ebs_optimized[" + index + "]}\",\n")
                            .append("                force_destroy: \"${force_destroy[" + index + "]}\",\n")
                            .append("                get_password_data: \"${get_password_data[" + index + "]}\",\n")
                            .append("                hibernation: \"${hibernation[" + index + "]}\",\n")
                            .append("                monitoring: \"${monitoring[" + index + "]}\",\n")
                            .append("                source_dest_check: \"${source_dest_check[" + index + "]}\",\n")
                            .append("                user_data_replace_on_change: \"${user_data_replace_on_change[" + index + "]}\",\n")
                            .append("                enable_primary_ipv6: \"${enable_primary_ipv6[" + index + "]}\",\n")
                            .append("                pri_eni_delete_on_termination: \"${pri_eni_delete_on_termination[" + index + "]}\",\n")
                            .append("                enabled: \"${enabled[" + index + "]}\",\n")
                            .append("                enable_resource_name_dns_aaaa_record: \"${enable_resource_name_dns_aaaa_record[" + index + "]}\",\n")
                            .append("                enable_resource_name_dns_a_record: \"${enable_resource_name_dns_a_record[" + index + "]}\",\n")
                            .append("                root_dev_delete_on_termination: \"${root_dev_delete_on_termination[" + index + "]}\",\n")
                            .append("                root_dev_encrypted: \"${root_dev_encrypted[" + index + "]}\",\n")
                            .append("                user_data: \"${user_data[" + index + "]}\",\n")
                            .append("                root_dev_tags: \"${root_dev_tags[" + index + "]}\",\n")
                            .append("                volume_tags: \"${volume_tags[" + index + "]}\",\n")
                            .append("                ipv6_addresses: \"${ipv6_addresses[" + index + "]}\",\n")
                            .append("                secondary_private_ips: \"${secondary_private_ips[" + index + "]}\",\n")
                            .append("                vpc_security_group_ids: \"${vpc_security_group_ids[" + index + "]}\",\n")
                            .append("                security_groups: \"${security_groups[" + index + "]}\",\n")
                            .append("                ebs_dev_device_name: \"${ebs_dev_device_name[" + index + "]}\",\n")
                            .append("                ebs_dev_kms_key_id: \"${ebs_dev_kms_key_id[" + index + "]}\",\n")
                            .append("                ebs_dev_snapshot_id: \"${ebs_dev_snapshot_id[" + index + "]}\",\n")
                            .append("                ebs_dev_volume_type: \"${ebs_dev_volume_type[" + index + "]}\",\n")
                            .append("                eph_dev_device_name: \"${eph_dev_device_name[" + index + "]}\",\n")
                            .append("                virtual_name: \"${virtual_name[" + index + "]}\",\n")
                            .append("                secondary_subnet_id: \"${secondary_subnet_id[" + index + "]}\",\n")
                            .append("                interface_type: \"${interface_type[" + index + "]}\",\n")
                            .append("                ebs_dev_iops: \"${ebs_dev_iops[" + index + "]}\",\n")
                            .append("                ebs_dev_throughput: \"${ebs_dev_throughput[" + index + "]}\",\n")
                            .append("                ebs_dev_volume_size: \"${ebs_dev_volume_size[" + index + "]}\",\n")
                            .append("                network_card_index: \"${network_card_index[" + index + "]}\",\n")
                            .append("                device_index: \"${device_index[" + index + "]}\",\n")
                            .append("                private_ip_address_count: \"${private_ip_address_count[" + index + "]}\",\n")
                            .append("                ebs_dev_delete_on_termination: \"${ebs_dev_delete_on_termination[" + index + "]}\",\n")
                            .append("                ebs_dev_encrypted: \"${ebs_dev_encrypted[" + index + "]}\",\n")
                            .append("                no_device: \"${no_device[" + index + "]}\",\n")
                            .append("                sec_eni_delete_on_termination: \"${sec_eni_delete_on_termination[" + index + "]}\",\n")
                            .append("                ebs_dev_tags: \"${ebs_dev_tags[" + index + "]}\",\n")
                            .append("                private_ip_addresses: \"${private_ip_addresses[" + index + "]}\",\n")

                            /** End of YAML Leaf Node Substitution */

                            .append("                tags: \"${tags[" + index + "]}\"\n")
                            .append("            }\n");
                    }
        }
        return definedPythonFile.toString();
    }

    @Override
    public String buildTofuModuleStateFile(List<Map<String, Object>> resourceConfigs) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("{\n")
            .append("    \"version\": 4,\n")
            .append("    \"resources\": [\n")
            .append("        {\n")
            .append("            \"module\": \"module.aws_instance[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_instance\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String instanceId = (String) resourceConfig.get("instance_id");
                        if (!instanceId.contains("create-only-")) {
                            if (instanceId.startsWith("arn:")) {
                                String resource = instanceId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                instanceId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + instanceId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("instance_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("instance_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_instance_id");
                    }
                    if (index < resourceConfigs.size() - 1) {
                        definedPythonFile.append(",");
                    }
                    definedPythonFile.append("\n");
                }
        definedPythonFile
            .append("            ]\n")
            .append("        }\n")
            .append("    ]\n")
            .append("}\n");
        return definedPythonFile.toString();
    }

    @Override
    public String buildTofuTextareaFile(String textarea) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append(textarea);
        return definedPythonFile.toString();
    }
}
