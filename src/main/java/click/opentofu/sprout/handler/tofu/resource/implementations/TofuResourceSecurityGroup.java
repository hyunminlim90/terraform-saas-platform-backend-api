package click.opentofu.sprout.handler.tofu.resource.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;

@Component("tofu_resource_security_group")
public class TofuResourceSecurityGroup implements ResourceHandler {
    @Override
    public String buildTofuResourceMainFile(String region, String account) {
        StringBuilder definedMainFile = new StringBuilder();
        definedMainFile
            .append("variable aws_security_group { type = map(map(any)) }\n\n")
            .append("resource aws_security_group product {\n")
            .append("    for_each = var.aws_security_group\n\n")

            /** Start of Reusable Terraform Module Code Substitution */

            .append("    description            = length(trim(lookup(each.value, \"description\", \"\"), \" \")) > 0 ? lookup(each.value, \"description\", \"\") : null\n")
            .append("    name                   = length(trim(lookup(each.value, \"security_group_name\", \"\"), \" \")) > 0 ? lookup(each.value, \"security_group_name\", \"\") : null\n")
            .append("    name_prefix            = length(trim(lookup(each.value, \"name_prefix\", \"\"), \" \")) > 0 ? lookup(each.value, \"name_prefix\", \"\") : null\n")
            .append("    region                 = length(trim(lookup(each.value, \"region\", \"\"), \" \")) > 0 ? lookup(each.value, \"region\", \"\") : null\n")
            .append("    vpc_id                 = length(trim(lookup(each.value, \"vpc_id\", \"\"), \" \")) > 0 ? lookup(each.value, \"vpc_id\", \"\") : null\n")
            .append("    revoke_rules_on_delete = length(trim(lookup(each.value, \"revoke_rules_on_delete\", \"\"), \" \")) > 0 ? lookup(each.value, \"revoke_rules_on_delete\", \"\") == \"true\" ? true : false : null\n")
            .append("    dynamic \"ingress\" {\n")
            .append("        for_each = zipmap(range(length(compact(split(\"{{__,__}}\", lookup(each.value, \"ingress_protocol\", \"\"))))), compact(split(\"{{__,__}}\", lookup(each.value, \"ingress_protocol\", \"\"))))\n")
            .append("        content {\n")
            .append("            protocol         = ingress.value\n")
            .append("            description      = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_description\", \"\")), ingress.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_description\", \"\")), ingress.key), \"\") : null\n")
            .append("            from_port        = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_from_port\", \"\")), ingress.key), \"\"), \" \")) > 0 ? try(tonumber(try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_from_port\", \"\")), ingress.key), \"\")), null) : null\n")
            .append("            to_port          = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_to_port\", \"\")), ingress.key), \"\"), \" \")) > 0 ? try(tonumber(try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_to_port\", \"\")), ingress.key), \"\")), null) : null\n")
            .append("            self             = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_self\", \"\")), ingress.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"ingress_self\", \"\")), ingress.key), \"\") == \"true\" ? true : false : null\n")
            .append("            cidr_blocks      = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_cidr_blocks\", \"\")), ingress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_cidr_blocks\", \"\")), ingress.key))), \"\") : null\n")
            .append("            ipv6_cidr_blocks = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_ipv6_cidr_blocks\", \"\")), ingress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_ipv6_cidr_blocks\", \"\")), ingress.key))), \"\") : null\n")
            .append("            prefix_list_ids  = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_prefix_list_ids\", \"\")), ingress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_prefix_list_ids\", \"\")), ingress.key))), \"\") : null\n")
            .append("            security_groups  = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_security_groups\", \"\")), ingress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"ingress_security_groups\", \"\")), ingress.key))), \"\") : null\n")
            .append("        }\n")
            .append("    }\n")
            .append("    dynamic \"egress\" {\n")
            .append("        for_each = zipmap(range(length(compact(split(\"{{__,__}}\", lookup(each.value, \"egress_protocol\", \"\"))))), compact(split(\"{{__,__}}\", lookup(each.value, \"egress_protocol\", \"\"))))\n")
            .append("        content {\n")
            .append("            protocol         = egress.value\n")
            .append("            description      = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_description\", \"\")), egress.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_description\", \"\")), egress.key), \"\") : null\n")
            .append("            from_port        = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_from_port\", \"\")), egress.key), \"\"), \" \")) > 0 ? try(tonumber(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_from_port\", \"\")), egress.key), \"\")), null) : null\n")
            .append("            to_port          = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_to_port\", \"\")), egress.key), \"\"), \" \")) > 0 ? try(tonumber(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_to_port\", \"\")), egress.key), \"\")), null) : null\n")
            .append("            self             = length(trim(try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_self\", \"\")), egress.key), \"\"), \" \")) > 0 ? try(element(split(\"{{__,__}}\", lookup(each.value, \"egress_self\", \"\")), egress.key), \"\") == \"true\" ? true : false : null\n")
            .append("            cidr_blocks      = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_cidr_blocks\", \"\")), egress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_cidr_blocks\", \"\")), egress.key))), \"\") : null\n")
            .append("            ipv6_cidr_blocks = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_ipv6_cidr_blocks\", \"\")), egress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_ipv6_cidr_blocks\", \"\")), egress.key))), \"\") : null\n")
            .append("            prefix_list_ids  = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_prefix_list_ids\", \"\")), egress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_prefix_list_ids\", \"\")), egress.key))), \"\") : null\n")
            .append("            security_groups  = length(try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_security_groups\", \"\")), egress.key))), \"\")) > 0 ? try(compact(split(\"{{__,__}}\", element(split(\"{{__/__}}\", lookup(each.value, \"egress_security_groups\", \"\")), egress.key))), \"\") : null\n")
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
