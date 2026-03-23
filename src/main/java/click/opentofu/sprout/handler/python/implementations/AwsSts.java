package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;
import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_sts")
public class AwsSts implements PythonHandler {
    @Override
    public String buildAwsPythonFileForSts (String ROOT_PATH, String uuid, String authEmailId, String awsAccessKey, String awsSecretAccessKey, String awsSessionToken) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("import boto3\n")
            .append("import botocore.exceptions\n")
            .append("import json\n")
            .append("aws_access_key = '" + awsAccessKey + "'\n")
            .append("aws_secret_key = '" + awsSecretAccessKey + "'\n")
            .append("aws_session_token = '" + awsSessionToken + "'\n")
            .append("session = boto3.Session(\n")
            .append("    aws_access_key_id=aws_access_key,\n")
            .append("    aws_secret_access_key=aws_secret_key,\n")
            .append("    aws_session_token=aws_session_token\n")
            .append(")\n")
            .append("sts = session.client('sts')\n")
            .append("iam = session.client('iam')\n")
            .append("try: account_id = sts.get_caller_identity()['Account']\n")
            .append("except botocore.exceptions.ClientError as error:\n")
            .append("    if error.response['Error']['Code'] == 'ExpiredToken': account_id = 'ExpiredToken'\n")
            .append("    elif error.response['Error']['Code'] == 'InvalidClientTokenId': account_id = 'InvalidClientTokenId'\n")
            .append("    else: account_id = 'UnknownException'\n")
            .append("try: role_name = sts.get_caller_identity()['Arn'].split('/')[-2]\n")
            .append("except botocore.exceptions.ClientError as error:\n")
            .append("    if error.response['Error']['Code'] == 'ExpiredToken': role_name = 'ExpiredToken'\n")
            .append("    elif error.response['Error']['Code'] == 'InvalidClientTokenId': role_name = 'InvalidClientTokenId'\n")
            .append("    else: role_name = 'UnknownException'\n")
            .append("try: account_alias = iam.list_account_aliases()\n")
            .append("except botocore.exceptions.ClientError as error:\n")
            .append("    if error.response['Error']['Code'] == 'ExpiredToken': account_alias = 'ExpiredToken'\n")
            .append("    elif error.response['Error']['Code'] == 'InvalidClientTokenId': account_alias = 'InvalidClientTokenId'\n")
            .append("    else: account_alias = 'AliasNotSet'\n")
            .append("aws_sts_details = {'outputs': {'aws_sts': {'value': {}}}}\n")
            .append("sts_details = {\n")
            .append("    'account_id': account_id,\n")
            .append("    'account_alias': account_alias['AccountAliases'][0] if isinstance(account_alias, dict) and 'AccountAliases' in account_alias and account_alias['AccountAliases'] else 'AliasNotSet',\n")
            .append("    'role_name': role_name\n")
            .append("}\n")
            .append("aws_sts_details['outputs']['aws_sts']['value'][account_id] = sts_details\n")
            .append("with open(r'" + ROOT_PATH + "/" + "sts/" + authEmailId + "/" + uuid + "/boto3/aws_sts.json', 'w') as f:\n")
            .append("    json.dump(aws_sts_details, f, indent=4)\n");
        return definedPythonFile.toString();
    }
}
