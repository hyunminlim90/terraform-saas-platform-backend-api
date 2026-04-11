package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_route_table_association")
public class TofuResourceRouteTableAssociation implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_route_table_association { type = map(map(any)) }\n\n")
            .append("resource aws_route_table_association product {\n")
            .append("    for_each = var.aws_route_table_association\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    gateway_id     = length(trim(lookup(each.value, \"gateway_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"gateway_id\", \"\") : null\n")
            .append("    route_table_id = length(trim(lookup(each.value, \"route_table_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"route_table_id\", \"\") : null\n")
            .append("    subnet_id      = length(trim(lookup(each.value, \"subnet_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"subnet_id\", \"\") : null\n")

            /** End of Reusable Terraform Module Code Substitution */

            .append("    lifecycle {\n")
            .append("        ignore_changes = [\n")
            .append("        ]\n")
            .append("    }\n")
            .append("}\n");
        return definedMainFile.toString();
    }
}
