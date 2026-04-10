package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_route_table")
public class TofuResourceRouteTable implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_route_table { type = map(map(any)) }\n\n")
            .append("resource aws_route_table product {\n")
            .append("    for_each = var.aws_route_table\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    vpc_id           = length(trim(lookup(each.value, \"vpc_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"vpc_id\", \"\") : null\n")
            .append("    propagating_vgws = length(trim(lookup(each.value, \"propagating_vgws\", \"\"), \" \")) > 0 ? compact(split(\"{{__,__}}\", lookup(each.value, \"propagating_vgws\", \"\"))) : null\n")
            .append("    dynamic \"route\" {\n")
            .append("        for_each = zipmap(range(length(compact(split(\"{{__,__}}\", lookup(each.value, \"cidr_block\", \"\"))))), compact(split(\"{{__,__}}\", lookup(each.value, \"cidr_block\", \"\"))))\n")
            .append("        content {\n")
            .append("            cidr_block                 = route.value\n")
            .append("            ipv6_cidr_block            = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"ipv6_cidr_block\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"ipv6_cidr_block\", \"\")), route.key), \"\") : null\n")
            .append("            destination_prefix_list_id = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"destination_prefix_list_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"destination_prefix_list_id\", \"\")), route.key), \"\") : null\n")
            .append("            carrier_gateway_id         = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"carrier_gateway_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"carrier_gateway_id\", \"\")), route.key), \"\") : null\n")
            .append("            core_network_arn           = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"core_network_arn\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"core_network_arn\", \"\")), route.key), \"\") : null\n")
            .append("            egress_only_gateway_id     = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_only_gateway_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_only_gateway_id\", \"\")), route.key), \"\") : null\n")
            .append("            gateway_id                 = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"gateway_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"gateway_id\", \"\")), route.key), \"\") : null\n")
            .append("            local_gateway_id           = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"local_gateway_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"local_gateway_id\", \"\")), route.key), \"\") : null\n")
            .append("            nat_gateway_id             = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"nat_gateway_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"nat_gateway_id\", \"\")), route.key), \"\") : null\n")
            .append("            network_interface_id       = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"network_interface_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"network_interface_id\", \"\")), route.key), \"\") : null\n")
            .append("            transit_gateway_id         = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"transit_gateway_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"transit_gateway_id\", \"\")), route.key), \"\") : null\n")
            .append("            vpc_endpoint_id            = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"vpc_endpoint_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"vpc_endpoint_id\", \"\")), route.key), \"\") : null\n")
            .append("            vpc_peering_connection_id  = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"vpc_peering_connection_id\", \"\")), route.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"vpc_peering_connection_id\", \"\")), route.key), \"\") : null\n")
            .append("        }\n")
            .append("    }\n")

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
