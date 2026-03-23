package click.opentofu.sprout.handler.python.implementations;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.python.interfaces.PythonHandler;

@Component("boto3_sts_verify")
public class AwsStsVerify implements PythonHandler {
    @Override
    public String buildAwsPythonFileForSts (String ROOT_PATH, String uuid, String authEmailId, String awsAccessKey, String awsSecretAccessKey, String awsSessionToken) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("import boto3\n")
            .append("import botocore.exceptions\n")
            .append("import sys\n")
            .append("aws_access_key = '" + awsAccessKey + "'\n")
            .append("aws_secret_key = '" + awsSecretAccessKey + "'\n")
            .append("aws_session_token = '" + awsSessionToken + "'\n")
            .append("session = boto3.Session(\n")
            .append("    aws_access_key_id=aws_access_key,\n")
            .append("    aws_secret_access_key=aws_secret_key,\n")
            .append("    aws_session_token=aws_session_token\n")
            .append(")\n")
            .append("sts = session.client('sts')\n")
            .append("try: sts.get_caller_identity()\n")
            .append("except botocore.exceptions.ClientError as error:\n")
            .append("    error_code = error.response['Error']['Code']\n")
            .append("    if error_code == 'ExpiredToken' or error_code == 'InvalidClientTokenId': sys.exit(1)\n")
            .append("sys.exit(0)\n");
        return definedPythonFile.toString();
    }
}
