package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_internet_gateway")
public class AwsInternetGatewayBoto implements PythonHandler {
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
            .append("aws_internet_gateway_details = {'outputs': {'aws_internet_gateway': {'value': {}}}}\n")
            .append("internet_gateway_token = None\n")
            .append("while True:\n")
            .append("    try: internet_gateway_response = ec2.describe_internet_gateways(NextToken=internet_gateway_token) if internet_gateway_token else ec2.describe_internet_gateways()\n")
            .append("    except (ClientError,ParamValidationError,EndpointConnectionError,NoCredentialsError): internet_gateway_response = {}\n")
            .append("    for internet_gateway in internet_gateway_response['InternetGateways'] if 'InternetGateways' in internet_gateway_response else []:\n")
            .append("        internet_gateway_arn = ''\n")
            .append("        internet_gateway_id = internet_gateway.get('InternetGatewayId', '')\n")
            .append("        vpc_id = next((item.get('VpcId') for item in internet_gateway.get('Attachments', [])), '')\n")
            .append("        tags = { tag['Key']: tag['Value'] for tag in (internet_gateway['Tags'] if 'Tags' in internet_gateway else []) if 'Key' in tag and 'Value' in tag }\n")
            .append("        internet_gateway_details = {\n")
            .append("            'account_id': account_id,\n")
            .append("            'region': '" + region + "',\n")
            .append("            'internet_gateway_arn': internet_gateway_arn,\n")
            .append("            'internet_gateway_id': internet_gateway_id,\n")
            .append("            'vpc_id': vpc_id,\n")
            .append("            'tags': tags\n")
            .append("        }\n")
            .append("        aws_internet_gateway_details['outputs']['aws_internet_gateway']['value'][internet_gateway_id] = internet_gateway_details\n")
            .append("    internet_gateway_token = internet_gateway_response.get('NextToken')\n")
            .append("    if not internet_gateway_token: break\n")

            /** End of Substitution */

            .append("with open(r'" + ROOT_PATH + "/" + authEmailId + "/" + uuid + "/" + region + "/boto3/aws_internet_gateway.json', 'w', encoding='utf-8') as f:\n")
            .append("    json.dump(aws_internet_gateway_details, f, indent=4, default=str, ensure_ascii=False)\n");
        return definedPythonFile.toString();
    }
}
