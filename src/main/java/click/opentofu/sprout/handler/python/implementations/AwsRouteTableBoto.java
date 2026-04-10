package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_route_table")
public class AwsRouteTableBoto implements PythonHandler {
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
            .append("aws_route_table_details = {'outputs': {'aws_route_table': {'value': {}}}}\n")
            .append("route_table_token = None\n")
            .append("while True:\n")
            .append("    try: route_table_response = ec2.describe_route_tables(NextToken=route_table_token,Filters=[{'Name':'route.state','Values':['active']}]) if route_table_token else ec2.describe_route_tables(Filters=[{'Name':'route.state','Values':['active']}])\n")
            .append("    except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): route_table_response = {}\n")
            .append("    for route_table in route_table_response['RouteTables'] if 'RouteTables' in route_table_response else []:\n")
            .append("        route_table_id = route_table.get('RouteTableId', '')\n")
            .append("        vpc_id = route_table.get('VpcId', '')\n")
            .append("        propagating_vgws = list(filter(None, (item.get('GatewayId') for item in route_table.get('PropagatingVgws', []))))\n")
            .append("        cidr_block = [ item.get('DestinationCidrBlock', '') for item in route_table.get('Routes', []) ]\n")
            .append("        ipv6_cidr_block = [ item.get('DestinationIpv6CidrBlock', '') for item in route_table.get('Routes', []) ]\n")
            .append("        destination_prefix_list_id = [ item.get('DestinationPrefixListId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        carrier_gateway_id = [ item.get('CarrierGatewayId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        core_network_arn = [ item.get('CoreNetworkArn', '') for item in route_table.get('Routes', []) ]\n")
            .append("        egress_only_gateway_id = [ item.get('EgressOnlyInternetGatewayId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        gateway_id = [ item.get('GatewayId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        local_gateway_id = [ item.get('LocalGatewayId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        nat_gateway_id = [ item.get('NatGatewayId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        network_interface_id = [ item.get('NetworkInterfaceId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        transit_gateway_id = [ item.get('TransitGatewayId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        vpc_endpoint_id = []\n")
            .append("        vpc_peering_connection_id = [ item.get('VpcPeeringConnectionId', '') for item in route_table.get('Routes', []) ]\n")
            .append("        tags = { tag['Key']: tag['Value'] for tag in (route_table['Tags'] if 'Tags' in route_table else []) if 'Key' in tag and 'Value' in tag }\n")
            .append("        route_table_details = {\n")
            .append("            'account_id': account_id,\n")
            .append("            'region': '" + region + "',\n")
            .append("            'route_table_id': route_table_id,\n")
            .append("            'vpc_id': vpc_id,\n")
            .append("            'propagating_vgws': propagating_vgws,\n")
            .append("            'cidr_block': cidr_block,\n")
            .append("            'ipv6_cidr_block': ipv6_cidr_block,\n")
            .append("            'destination_prefix_list_id': destination_prefix_list_id,\n")
            .append("            'carrier_gateway_id': carrier_gateway_id,\n")
            .append("            'core_network_arn': core_network_arn,\n")
            .append("            'egress_only_gateway_id': egress_only_gateway_id,\n")
            .append("            'gateway_id': gateway_id,\n")
            .append("            'local_gateway_id': local_gateway_id,\n")
            .append("            'nat_gateway_id': nat_gateway_id,\n")
            .append("            'network_interface_id': network_interface_id,\n")
            .append("            'transit_gateway_id': transit_gateway_id,\n")
            .append("            'vpc_endpoint_id': vpc_endpoint_id,\n")
            .append("            'vpc_peering_connection_id': vpc_peering_connection_id,\n")
            .append("            'tags': tags\n")
            .append("        }\n")
            .append("        aws_route_table_details['outputs']['aws_route_table']['value'][route_table_id] = route_table_details\n")
            .append("    route_table_token = route_table_response.get('NextToken')\n")
            .append("    if not route_table_token: break\n")

            /** End of Substitution */

            .append("with open(r'" + ROOT_PATH + "/" + authEmailId + "/" + uuid + "/" + region + "/boto3/aws_route_table.json', 'w', encoding='utf-8') as f:\n")
            .append("    json.dump(aws_route_table_details, f, indent=4, default=str, ensure_ascii=False)\n");
        return definedPythonFile.toString();
    }
}
