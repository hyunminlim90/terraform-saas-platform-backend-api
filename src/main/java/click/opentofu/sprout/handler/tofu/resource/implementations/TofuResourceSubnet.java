package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_subnet")
public class TofuResourceSubnet implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_subnet { type = map(map(any)) }\n\n")
            .append("resource aws_subnet product {\n")
            .append("    for_each = var.aws_subnet\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    vpc_id = length(trim(lookup(each.value, \"vpc_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"vpc_id\", \"\") : null\n")
            .append("    assign_ipv6_address_on_creation = length(trim(lookup(each.value, \"assign_ipv6_address_on_creation\", \"\"), \" \")) > 0 ? lookup(each.value, \"assign_ipv6_address_on_creation\", \"\") == \"true\" ? true : false : null\n")
            .append("    availability_zone = length(trim(lookup(each.value, \"availability_zone\", \"\"), \" \")) > 0 ? lookup(each.value, \"availability_zone\", \"\") : null\n")
            .append("    availability_zone_id = length(trim(lookup(each.value, \"availability_zone_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"availability_zone_id\", \"\") : null\n")
            .append("    cidr_block = length(trim(lookup(each.value, \"cidr_block\", \"\"), \" \")) > 0 ? lookup(each.value, \"cidr_block\", \"\") : null\n")
            .append("    customer_owned_ipv4_pool = length(trim(lookup(each.value, \"customer_owned_ipv4_pool\", \"\"), \" \")) > 0 ? lookup(each.value, \"customer_owned_ipv4_pool\", \"\") : null\n")
            .append("    enable_dns64 = length(trim(lookup(each.value, \"enable_dns64\", \"\"), \" \")) > 0 ? lookup(each.value, \"enable_dns64\", \"\") == \"true\" ? true : false : null\n")
            .append("    enable_lni_at_device_index = length(trim(lookup(each.value, \"enable_lni_at_device_index\", \"\"), \" \")) > 0 ? try(tonumber(lookup(each.value, \"enable_lni_at_device_index\", \"\")), null) : null\n")
            .append("    enable_resource_name_dns_a_record_on_launch = length(trim(lookup(each.value, \"enable_resource_name_dns_a_record_on_launch\", \"\"), \" \")) > 0 ? lookup(each.value, \"enable_resource_name_dns_a_record_on_launch\", \"\") == \"true\" ? true : false : null\n")
            .append("    enable_resource_name_dns_aaaa_record_on_launch = length(trim(lookup(each.value, \"enable_resource_name_dns_aaaa_record_on_launch\", \"\"), \" \")) > 0 ? lookup(each.value, \"enable_resource_name_dns_aaaa_record_on_launch\", \"\") == \"true\" ? true : false : null\n")
            .append("    ipv6_cidr_block = length(trim(lookup(each.value, \"ipv6_cidr_block\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipv6_cidr_block\", \"\") : null\n")
            .append("    ipv6_native = length(trim(lookup(each.value, \"ipv6_native\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipv6_native\", \"\") == \"true\" ? true : false : null\n")
            .append("    map_customer_owned_ip_on_launch = length(trim(lookup(each.value, \"map_customer_owned_ip_on_launch\", \"\"), \" \")) > 0 ? lookup(each.value, \"map_customer_owned_ip_on_launch\", \"\") == \"true\" ? true : null : null\n")
            .append("    map_public_ip_on_launch = length(trim(lookup(each.value, \"map_public_ip_on_launch\", \"\"), \" \")) > 0 ? lookup(each.value, \"map_public_ip_on_launch\", \"\") == \"true\" ? true : false : null\n")
            .append("    outpost_arn = length(trim(lookup(each.value, \"outpost_arn\", \"\"), \" \")) > 0 ? lookup(each.value, \"outpost_arn\", \"\") : null\n")
            .append("    private_dns_hostname_type_on_launch = length(trim(lookup(each.value, \"private_dns_hostname_type_on_launch\", \"\"), \" \")) > 0 ? lookup(each.value, \"private_dns_hostname_type_on_launch\", \"\") : null\n")
            .append("    region = length(trim(lookup(each.value, \"region\", \"\"), \" \")) > 0 ? lookup(each.value, \"region\", \"\") : null\n")

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
