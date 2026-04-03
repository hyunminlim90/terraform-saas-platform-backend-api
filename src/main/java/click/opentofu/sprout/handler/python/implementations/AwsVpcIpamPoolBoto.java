package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_vpc_ipam_pool")
public class AwsVpcIpamPoolBoto implements PythonHandler {
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
            .append("import logging\n")
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
            .append("aws_vpc_ipam_pool_details = {'outputs': {'aws_vpc_ipam_pool': {'value': {}}}}\n")
            .append("vpc_ipam_pool_token = None\n")
            .append("while True:\n")
            .append("    try: vpc_ipam_pool_response = ec2.describe_ipam_pools(NextToken=vpc_ipam_pool_token) if vpc_ipam_pool_token else ec2.describe_ipam_pools()\n")
            .append("    except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): vpc_ipam_pool_response = {}\n")
            .append("    for vpc_ipam_pool in vpc_ipam_pool_response['IpamPools'] if 'IpamPools' in vpc_ipam_pool_response else []:\n")
            .append("        if (vpc_ipam_pool['State'] if 'State' in vpc_ipam_pool else '') != 'create-complete': continue\n")
            .append("        vpc_ipam_pool_arn = vpc_ipam_pool.get('IpamPoolArn', '')\n")
            .append("        vpc_ipam_pool_id = vpc_ipam_pool.get('IpamPoolId', '')\n")
            .append("        address_family = vpc_ipam_pool.get('AddressFamily', '')\n")
            .append("        aws_service = vpc_ipam_pool.get('AwsService', '')\n")
            .append("        description = vpc_ipam_pool.get('Description', '')\n")
            .append("        ipam_scope_id = vpc_ipam_pool.get('IpamScopeArn', '').split('/')[-1]\n")
            .append("        locale = vpc_ipam_pool.get('Locale', '')\n")
            .append("        public_ip_source = vpc_ipam_pool.get('PublicIpSource', '')\n")
            .append("        source_ipam_pool_id = vpc_ipam_pool.get('SourceIpamPoolId', '')\n")
            .append("        allocation_default_netmask_length = vpc_ipam_pool.get('AllocationDefaultNetmaskLength', '')\n")
            .append("        allocation_max_netmask_length = vpc_ipam_pool.get('AllocationMaxNetmaskLength', '')\n")
            .append("        allocation_min_netmask_length = vpc_ipam_pool.get('AllocationMinNetmaskLength', '')\n")
            .append("        auto_import = vpc_ipam_pool.get('AutoImport', '')\n")
            .append("        cascade = ''\n")
            .append("        publicly_advertisable = vpc_ipam_pool.get('PubliclyAdvertisable', '')\n")
            .append("        allocation_resource_tags = { allocation['Key']: allocation['Value'] for allocation in (vpc_ipam_pool['AllocationResourceTags'] if 'AllocationResourceTags' in vpc_ipam_pool else []) if 'Key' in allocation and 'Value' in allocation }\n")
            .append("        tags = { tag['Key']: tag['Value'] for tag in (vpc_ipam_pool['Tags'] if 'Tags' in vpc_ipam_pool else []) if 'Key' in tag and 'Value' in tag }\n")
            .append("        vpc_ipam_pool_details = {\n")
            .append("            'account_id': account_id,\n")
            .append("            'region': '" + region + "',\n")
            .append("            'vpc_ipam_pool_arn': vpc_ipam_pool_arn,\n")
            .append("            'vpc_ipam_pool_id': vpc_ipam_pool_id,\n")
            .append("            'address_family': address_family,\n")
            .append("            'aws_service': aws_service,\n")
            .append("            'description': description,\n")
            .append("            'ipam_scope_id': ipam_scope_id,\n")
            .append("            'locale': locale,\n")
            .append("            'public_ip_source': public_ip_source,\n")
            .append("            'source_ipam_pool_id': source_ipam_pool_id,\n")
            .append("            'allocation_default_netmask_length': allocation_default_netmask_length,\n")
            .append("            'allocation_max_netmask_length': allocation_max_netmask_length,\n")
            .append("            'allocation_min_netmask_length': allocation_min_netmask_length,\n")
            .append("            'auto_import': auto_import,\n")
            .append("            'cascade': cascade,\n")
            .append("            'publicly_advertisable': publicly_advertisable,\n")
            .append("            'allocation_resource_tags': allocation_resource_tags,\n")
            .append("            'tags': tags\n")
            .append("        }\n")
            .append("        aws_vpc_ipam_pool_details['outputs']['aws_vpc_ipam_pool']['value'][vpc_ipam_pool_id] = vpc_ipam_pool_details\n")
            .append("    vpc_ipam_pool_token = vpc_ipam_pool_response.get('NextToken')\n")
            .append("    if not vpc_ipam_pool_token: break\n")

            /** End of Substitution */

            .append("with open(r'" + ROOT_PATH + "/" + authEmailId + "/" + uuid + "/" + region + "/boto3/aws_vpc_ipam_pool.json', 'w', encoding='utf-8') as f:\n")
            .append("    json.dump(aws_vpc_ipam_pool_details, f, indent=4, default=str, ensure_ascii=False)\n");
        return definedPythonFile.toString();
    }
}
