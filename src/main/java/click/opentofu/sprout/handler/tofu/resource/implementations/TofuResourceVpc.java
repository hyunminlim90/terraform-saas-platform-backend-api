package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_vpc")
public class TofuResourceVpc implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_vpc { type = map(map(any)) }\n\n")
            .append("resource aws_vpc product {\n")
            .append("    for_each = var.aws_vpc\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    assign_generated_ipv6_cidr_block     = length(trim(lookup(each.value, \"assign_generated_ipv6_cidr_block\", \"\"), \" \")) > 0 ? lookup(each.value, \"assign_generated_ipv6_cidr_block\", \"\") == \"true\" ? true : false : null\n")
            .append("    cidr_block                           = length(trim(lookup(each.value, \"cidr_block\", \"\"), \" \")) > 0 ? lookup(each.value, \"cidr_block\", \"\") : null\n")
            .append("    enable_dns_hostnames                 = length(trim(lookup(each.value, \"enable_dns_hostnames\", \"\"), \" \")) > 0 ? lookup(each.value, \"enable_dns_hostnames\", \"\") == \"true\" ? true : false : null\n")
            .append("    enable_dns_support                   = length(trim(lookup(each.value, \"enable_dns_support\", \"\"), \" \")) > 0 ? lookup(each.value, \"enable_dns_support\", \"\") == \"true\" ? true : false : null\n")
            .append("    enable_network_address_usage_metrics = length(trim(lookup(each.value, \"enable_network_address_usage_metrics\", \"\"), \" \")) > 0 ? lookup(each.value, \"enable_network_address_usage_metrics\", \"\") == \"true\" ? true : false : null\n")
            .append("    instance_tenancy                     = length(trim(lookup(each.value, \"instance_tenancy\", \"\"), \" \")) > 0 ? lookup(each.value, \"instance_tenancy\", \"\") : null\n")
            .append("    ipv4_ipam_pool_id                    = length(trim(lookup(each.value, \"ipv4_ipam_pool_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipv4_ipam_pool_id\", \"\") : null\n")
            .append("    ipv4_netmask_length                  = length(trim(lookup(each.value, \"ipv4_netmask_length\", \"\"), \" \")) > 0 ? try(tonumber(lookup(each.value, \"ipv4_netmask_length\", \"\")), null) : null\n")
            .append("    ipv6_cidr_block                      = length(trim(lookup(each.value, \"ipv6_cidr_block\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipv6_cidr_block\", \"\") : null\n")
            .append("    ipv6_cidr_block_network_border_group = length(trim(lookup(each.value, \"ipv6_cidr_block_network_border_group\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipv6_cidr_block_network_border_group\", \"\") : null\n")
            .append("    ipv6_ipam_pool_id                    = length(trim(lookup(each.value, \"ipv6_ipam_pool_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipv6_ipam_pool_id\", \"\") : null\n")
            .append("    ipv6_netmask_length                  = length(trim(lookup(each.value, \"ipv6_netmask_length\", \"\"), \" \")) > 0 ? try(tonumber(lookup(each.value, \"ipv6_netmask_length\", \"\")), null) : null\n")
            .append("    region                               = length(trim(lookup(each.value, \"region\", \"\"), \" \")) > 0 ? lookup(each.value, \"region\", \"\") : null\n")

            /** End of Reusable Terraform Module Code Substitution */

            .append("    tags = length(trim(lookup(each.value, \"tags\", \"\"), \" \")) > 0 ? merge({ for index, tag in split(\"{{__,__}}\", lookup(each.value, \"tags\", \"\")) : element(split(\"{{__:__}}\", tag), 0) => element(split(\"{{__:__}}\", tag), 1) }) : null\n\n")
            .append("    lifecycle {\n")
            .append("        ignore_changes = [\n")
            .append("        ]\n")
            .append("    }\n")
            .append("}\n");
        return definedMainFile.toString();
    }
}
