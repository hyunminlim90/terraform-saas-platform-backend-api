package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_subnet")
public class AwsSubnetBoto implements PythonHandler {
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
            .append("aws_subnet_details = {'outputs': {'aws_subnet': {'value': {}}}}\n")
            .append("subnet_token = None\n")
            .append("while True:\n")
            .append("    try: subnet_response = ec2.describe_subnets(NextToken=subnet_token,Filters=[{'Name':'state','Values':['available']}]) if subnet_token else ec2.describe_subnets(Filters=[{'Name':'state','Values':['available']}])\n")
            .append("    except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): subnet_response = {}\n")
            .append("    for subnet in subnet_response['Subnets'] if 'Subnets' in subnet_response else []:\n")
            .append("        if (subnet['State'] if 'State' in subnet else '') != 'available': continue\n")
            .append("        subnet_id = subnet['SubnetId'] if 'SubnetId' in subnet else ''\n")
            .append("        vpc_id = subnet['VpcId'] if 'VpcId' in subnet else ''\n")
            .append("        assign_ipv6_address_on_creation = subnet['AssignIpv6AddressOnCreation'] if 'AssignIpv6AddressOnCreation' in subnet else ''\n")
            .append("        availability_zone = subnet['AvailabilityZone'] if 'AvailabilityZone' in subnet else ''\n")
            // .append("        availability_zone_id = subnet['AvailabilityZoneId'] if 'AvailabilityZoneId' in subnet else ''\n")
            .append("        availability_zone_id = subnet['AvailabilityZoneId'] if not availability_zone and 'AvailabilityZoneId' in subnet else ''\n")
            .append("        cidr_block = subnet['CidrBlock'] if 'CidrBlock' in subnet else ''\n")
            .append("        customer_owned_ipv4_pool = subnet['CustomerOwnedIpv4Pool'] if 'CustomerOwnedIpv4Pool' in subnet else ''\n")
            .append("        enable_dns64 = subnet['EnableDns64'] if 'EnableDns64' in subnet else ''\n")
            .append("        enable_lni_at_device_index = subnet['EnableLniAtDeviceIndex'] if 'EnableLniAtDeviceIndex' in subnet else ''\n")
            .append("        enable_resource_name_dns_a_record_on_launch = subnet['PrivateDnsNameOptionsOnLaunch']['EnableResourceNameDnsARecord'] if 'PrivateDnsNameOptionsOnLaunch' in subnet and 'EnableResourceNameDnsARecord' in subnet['PrivateDnsNameOptionsOnLaunch'] else ''\n")
            .append("        enable_resource_name_dns_aaaa_record_on_launch = subnet['PrivateDnsNameOptionsOnLaunch']['EnableResourceNameDnsAAAARecord'] if 'PrivateDnsNameOptionsOnLaunch' in subnet and 'EnableResourceNameDnsAAAARecord' in subnet['PrivateDnsNameOptionsOnLaunch'] else ''\n")
            .append("        ipv6_cidr_block = next(iter(subnet['Ipv6CidrBlockAssociationSet'] if 'Ipv6CidrBlockAssociationSet' in subnet else []), {}).get('Ipv6CidrBlock', '')\n")
            .append("        ipv6_native = subnet['Ipv6Native'] if 'Ipv6Native' in subnet else ''\n")
            .append("        map_customer_owned_ip_on_launch = subnet['MapCustomerOwnedIpOnLaunch'] if 'MapCustomerOwnedIpOnLaunch' in subnet else ''\n")
            .append("        map_public_ip_on_launch = subnet['MapPublicIpOnLaunch'] if 'MapPublicIpOnLaunch' in subnet else ''\n")
            .append("        outpost_arn = subnet['OutpostArn'] if 'OutpostArn' in subnet else ''\n")
            .append("        private_dns_hostname_type_on_launch = subnet['PrivateDnsNameOptionsOnLaunch']['HostnameType'] if 'PrivateDnsNameOptionsOnLaunch' in subnet and 'HostnameType' in subnet['PrivateDnsNameOptionsOnLaunch'] else ''\n")
            .append("        tags = { tag['Key']: tag['Value'] for tag in (subnet['Tags'] if 'Tags' in subnet else []) if 'Key' in tag and 'Value' in tag }\n")
            .append("        subnet_details = {\n")
            .append("            'account_id': account_id,\n")
            .append("            'region': '" + region + "',\n")
            .append("            'subnet_id': subnet_id,\n")
            .append("            'vpc_id': vpc_id,\n")
            .append("            'assign_ipv6_address_on_creation': assign_ipv6_address_on_creation,\n")
            .append("            'availability_zone': availability_zone,\n")
            .append("            'availability_zone_id': availability_zone_id,\n")
            .append("            'cidr_block': cidr_block,\n")
            .append("            'customer_owned_ipv4_pool': customer_owned_ipv4_pool,\n")
            .append("            'enable_dns64': enable_dns64,\n")
            .append("            'enable_lni_at_device_index': enable_lni_at_device_index,\n")
            .append("            'enable_resource_name_dns_a_record_on_launch': enable_resource_name_dns_a_record_on_launch,\n")
            .append("            'enable_resource_name_dns_aaaa_record_on_launch': enable_resource_name_dns_aaaa_record_on_launch,\n")
            .append("            'ipv6_cidr_block': ipv6_cidr_block,\n")
            .append("            'ipv6_native': ipv6_native,\n")
            .append("            'map_customer_owned_ip_on_launch': map_customer_owned_ip_on_launch,\n")
            .append("            'map_public_ip_on_launch': map_public_ip_on_launch,\n")
            .append("            'outpost_arn': outpost_arn,\n")
            .append("            'private_dns_hostname_type_on_launch': private_dns_hostname_type_on_launch,\n")
            .append("            'tags': tags\n")
            .append("        }\n")
            .append("        aws_subnet_details['outputs']['aws_subnet']['value'][subnet_id] = subnet_details\n")
            .append("    subnet_token = subnet_response.get('NextToken')\n")
            .append("    if not subnet_token: break\n")

            /** End of Substitution */

            .append("with open(r'" + ROOT_PATH + "/" + authEmailId + "/" + uuid + "/" + region + "/boto3/aws_subnet.json', 'w', encoding='utf-8') as f:\n")
            .append("    json.dump(aws_subnet_details, f, indent=4, default=str, ensure_ascii=False)\n");
        return definedPythonFile.toString();
    }
}
