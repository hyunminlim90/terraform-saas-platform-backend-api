package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_route_table_association")
public class AwsRouteTableAssociationBoto implements PythonHandler {
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
            .append("aws_route_table_association_details = {'outputs': {'aws_route_table_association': {'value': {}}}}\n")
            .append("route_table_association_token = None\n")
            .append("while True:\n")
            .append("    try: route_table_association_response = ec2.describe_route_tables(NextToken=route_table_association_token) if route_table_association_token else ec2.describe_route_tables()\n")
            .append("    except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): route_table_association_response = {}\n")
            .append("    for route_table_association in route_table_association_response['RouteTables'] if 'RouteTables' in route_table_association_response else []:\n")
            .append("        for association in route_table_association.get('Associations', []):\n")
            .append("            if association.get('AssociationState', {}).get('State') != 'associated': continue\n")
            .append("            if not association.get('SubnetId') and not association.get('GatewayId'): continue\n")
            .append("            route_table_association_arn = ''\n")
            .append("            route_table_association_id = association.get('RouteTableAssociationId', '')\n")
            .append("            gateway_id = association.get('GatewayId', '')\n")
            .append("            route_table_id = association.get('RouteTableId', '')\n")
            .append("            subnet_id = association.get('SubnetId', '')\n")
            .append("            route_table_association_details = {\n")
            .append("                'account_id': account_id,\n")
            .append("                'region': '" + region + "',\n")
            .append("                'route_table_association_arn': route_table_association_arn,\n")
            .append("                'route_table_association_id': route_table_association_id,\n")
            .append("                'gateway_id': gateway_id,\n")
            .append("                'route_table_id': route_table_id,\n")
            .append("                'subnet_id': subnet_id,\n")
            .append("            }\n")
            .append("            aws_route_table_association_details['outputs']['aws_route_table_association']['value'][route_table_association_id] = route_table_association_details\n")
            .append("    route_table_association_token = route_table_association_response.get('NextToken')\n")
            .append("    if not route_table_association_token: break\n")

            /** End of Substitution */

            .append("with open(r'" + ROOT_PATH + "/" + authEmailId + "/" + uuid + "/" + region + "/boto3/aws_route_table_association.json', 'w', encoding='utf-8') as f:\n")
            .append("    json.dump(aws_route_table_association_details, f, indent=4, default=str, ensure_ascii=False)\n");
        return definedPythonFile.toString();
    }
}
