package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_vpc")
public class AwsVpcBoto implements PythonHandler {
    @Override
    public String buildAwsPythonFile(String ROOT_PATH, String authEmailId, String uuid, String region, String awsAccessKey, String awsSecretAccessKey, String awsSessionToken) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("import boto3\n")
            .append("import botocore.exceptions\n")
            .append("from botocore.exceptions import ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError\n")
            .append("import json\n")
            .append("from datetime import datetime\n")
            .append("import base64\n")
            .append("import sys\n")
            .append("from collections import defaultdict\n")
            .append("aws_access_key = '" + awsAccessKey + "'\n")
            .append("aws_secret_key = '" + awsSecretAccessKey + "'\n")
            .append("aws_session_token = '" + awsSessionToken + "'\n")
            .append("session = boto3.Session(\n")
            .append("    region_name='" + region + "',\n")
            .append("    aws_access_key_id=aws_access_key,\n")
            .append("    aws_secret_access_key=aws_secret_key,\n")
            .append("    aws_session_token=aws_session_token\n")
            .append(")\n")

            /** Start of Substitution */

            .append("ec2 = session.client('ec2')\n")
            .append("sts = session.client('sts')\n")
            .append("try: \n")
            .append("    caller_identity = sts.get_caller_identity()\n")
            .append("    account_id = caller_identity['Account'] if 'Account' in caller_identity else ''\n")
            .append("except ClientError as e:\n")
            .append("    if e.response['Error']['Code'] == 'ExpiredToken':\n")
            .append("        print('ERROR: AWS temporary credentials expired')\n")
            .append("        sys.exit(1)\n")
            .append("    else:\n")
            .append("        account_id = ''\n")
            .append("except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): account_id = ''\n")
            .append("aws_vpc_details = {'outputs': {'aws_vpc': {'value': {}}}}\n")
            .append("vpc_token = None\n")
            .append("while True:\n")
            .append("    try: vpc_response = ec2.describe_vpcs(NextToken=vpc_token,Filters=[{'Name':'state','Values':['available']}]) if vpc_token else ec2.describe_vpcs(Filters=[{'Name':'state','Values':['available']}])\n")
            .append("    except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): vpc_response = {}\n")
            .append("    for vpc in vpc_response['Vpcs'] if 'Vpcs' in vpc_response else []:\n")
            .append("        if (vpc['State'] if 'State' in vpc else '') != 'available': continue\n")
            .append("        vpc_id = vpc['VpcId'] if 'VpcId' in vpc else ''\n")
            .append("        try: enable_dns_support_response = ec2.describe_vpc_attribute(VpcId=vpc_id,Attribute='enableDnsSupport')\n")
            .append("        except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): enable_dns_support_response = {}\n")
            .append("        try: enable_dns_hostnames_response = ec2.describe_vpc_attribute(VpcId=vpc_id,Attribute='enableDnsHostnames')\n")
            .append("        except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): enable_dns_hostnames_response = {}\n")
            .append("        try: enable_network_address_usage_metrics_response = ec2.describe_vpc_attribute(VpcId=vpc_id,Attribute='enableNetworkAddressUsageMetrics')\n")
            .append("        except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): enable_network_address_usage_metrics_response = {}\n")
            .append("        assign_generated_ipv6_cidr_block = False\n")
            .append("        cidr_block = vpc['CidrBlock'] if 'CidrBlock' in vpc else ''\n")
            .append("        enable_dns_hostnames = enable_dns_hostnames_response.get('EnableDnsHostnames', {}).get('Value', False)\n")
            .append("        enable_dns_support = enable_dns_support_response.get('EnableDnsSupport', {}).get('Value', False)\n")
            .append("        enable_network_address_usage_metrics = enable_network_address_usage_metrics_response.get('EnableNetworkAddressUsageMetrics', {}).get('Value', False)\n")
            .append("        instance_tenancy = vpc['InstanceTenancy'] if 'InstanceTenancy' in vpc else ''\n")
            .append("        ipv4_ipam_pool_id = ''\n")
            .append("        ipv4_netmask_length = ''\n")
            .append("        ipv6_cidr_block = next(iter(vpc['Ipv6CidrBlockAssociationSet'] if 'Ipv6CidrBlockAssociationSet' in vpc else []), {}).get('Ipv6CidrBlock', '')\n")
            .append("        ipv6_cidr_block_network_border_group = next(iter(vpc['Ipv6CidrBlockAssociationSet'] if 'Ipv6CidrBlockAssociationSet' in vpc else []), {}).get('NetworkBorderGroup', '')\n")
            .append("        ipv6_ipam_pool_id = ''\n")
            .append("        ipv6_netmask_length = ''\n")
            .append("        tags = { tag['Key']: tag['Value'] for tag in (vpc['Tags'] if 'Tags' in vpc else []) if 'Key' in tag and 'Value' in tag }\n")
            .append("        vpc_details = {\n")
            .append("            'account_id': account_id,\n")
            .append("            'region': '" + region + "',\n")
            .append("            'vpc_id': vpc_id,\n")
            .append("            'assign_generated_ipv6_cidr_block': assign_generated_ipv6_cidr_block,\n")
            .append("            'cidr_block': cidr_block,\n")
            .append("            'enable_dns_hostnames': enable_dns_hostnames,\n")
            .append("            'enable_dns_support': enable_dns_support,\n")
            .append("            'enable_network_address_usage_metrics': enable_network_address_usage_metrics,\n")
            .append("            'instance_tenancy': instance_tenancy,\n")
            .append("            'ipv4_ipam_pool_id': ipv4_ipam_pool_id,\n")
            .append("            'ipv4_netmask_length': ipv4_netmask_length,\n")
            .append("            'ipv6_cidr_block': ipv6_cidr_block,\n")
            .append("            'ipv6_cidr_block_network_border_group': ipv6_cidr_block_network_border_group,\n")
            .append("            'ipv6_ipam_pool_id': ipv6_ipam_pool_id,\n")
            .append("            'ipv6_netmask_length': ipv6_netmask_length,\n")
            .append("            'tags': tags\n")
            .append("        }\n")
            .append("        aws_vpc_details['outputs']['aws_vpc']['value'][vpc_id] = vpc_details\n")
            .append("    vpc_token = vpc_response.get('NextToken')\n")
            .append("    if not vpc_token: break\n")

            /** End of Substitution */

            .append("with open(r'" + ROOT_PATH + "/" + authEmailId + "/" + uuid + "/" + region + "/boto3/aws_vpc.json', 'w', encoding='utf-8') as f:\n")
            .append("    json.dump(aws_vpc_details, f, indent=4, default=str, ensure_ascii=False)\n");
        return definedPythonFile.toString();
    }
}
