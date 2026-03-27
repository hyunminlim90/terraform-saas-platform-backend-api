package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_vpc_ipam_pool")
public class TofuResourceVpcIpamPool implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_vpc_ipam_pool { type = map(map(any)) }\n\n")
            .append("resource aws_vpc_ipam_pool product {\n")
            .append("    for_each = var.aws_vpc_ipam_pool\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    address_family                    = length(trim(lookup(each.value, \"address_family\", \"\"), \" \")) > 0 ? lookup(each.value, \"address_family\", \"\") : null\n")
            .append("    aws_service                       = length(trim(lookup(each.value, \"aws_service\", \"\"), \" \")) > 0 ? lookup(each.value, \"aws_service\", \"\") : null\n")
            .append("    description                       = length(trim(lookup(each.value, \"description\", \"\"), \" \")) > 0 ? lookup(each.value, \"description\", \"\") : null\n")
            .append("    ipam_scope_id                     = length(trim(lookup(each.value, \"ipam_scope_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"ipam_scope_id\", \"\") : null\n")
            .append("    locale                            = length(trim(lookup(each.value, \"locale\", \"\"), \" \")) > 0 ? lookup(each.value, \"locale\", \"\") : null\n")
            .append("    public_ip_source                  = length(trim(lookup(each.value, \"public_ip_source\", \"\"), \" \")) > 0 ? lookup(each.value, \"public_ip_source\", \"\") : null\n")
            .append("    region                            = length(trim(lookup(each.value, \"region\", \"\"), \" \")) > 0 ? lookup(each.value, \"region\", \"\") : null\n")
            .append("    source_ipam_pool_id               = length(trim(lookup(each.value, \"source_ipam_pool_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"source_ipam_pool_id\", \"\") : null\n")
            .append("    allocation_default_netmask_length = length(trim(lookup(each.value, \"allocation_default_netmask_length\", \"\"), \" \")) > 0 ? try(tonumber(lookup(each.value, \"allocation_default_netmask_length\", \"\")), null) : null\n")
            .append("    allocation_max_netmask_length     = length(trim(lookup(each.value, \"allocation_max_netmask_length\", \"\"), \" \")) > 0 ? try(tonumber(lookup(each.value, \"allocation_max_netmask_length\", \"\")), null) : null\n")
            .append("    allocation_min_netmask_length     = length(trim(lookup(each.value, \"allocation_min_netmask_length\", \"\"), \" \")) > 0 ? try(tonumber(lookup(each.value, \"allocation_min_netmask_length\", \"\")), null) : null\n")
            .append("    auto_import                       = length(trim(lookup(each.value, \"auto_import\", \"\"), \" \")) > 0 ? lookup(each.value, \"auto_import\", \"\") == \"true\" ? true : false : null\n")
            .append("    cascade                           = length(trim(lookup(each.value, \"cascade\", \"\"), \" \")) > 0 ? lookup(each.value, \"cascade\", \"\") == \"true\" ? true : false : null\n")
            .append("    publicly_advertisable             = length(trim(lookup(each.value, \"publicly_advertisable\", \"\"), \" \")) > 0 ? lookup(each.value, \"publicly_advertisable\", \"\") == \"true\" ? true : false : null\n")
            .append("    allocation_resource_tags          = length(trim(lookup(each.value, \"allocation_resource_tags\", \"\"), \" \")) > 0 ? merge({ for index, allocation in split(\"{{__,__}}\", lookup(each.value, \"allocation_resource_tags\", \"\")) : element(split(\"{{__:__}}\", allocation), 0) => element(split(\"{{__:__}}\", allocation), 1) }) : null\n")
        
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
