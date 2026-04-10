package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_internet_gateway")
public class TofuResourceInternetGateway implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_internet_gateway { type = map(map(any)) }\n\n")
            .append("resource aws_internet_gateway product {\n")
            .append("    for_each = var.aws_internet_gateway\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    vpc_id = length(trim(lookup(each.value, \"vpc_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"vpc_id\", \"\") : null\n")

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
