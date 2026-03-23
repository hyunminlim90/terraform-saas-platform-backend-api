package click.opentofu.sprout.handler.entity.implementations.sts;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.handler.entity.entity.sts.entity.StsEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.StsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component("aws_sts_builder")
@Slf4j
public class StsBuilder implements EntityHandler {

    private final StsRepository stsRepository;

    @Override
    public void buildEntityAndSaveForSts(JsonNode parameters, TemporaryCredential TemporaryCredential) {
        StsEntity stsEntity = StsEntity.builder()
            .accountId(parameters.path("account_id").asText() + "," + TemporaryCredential.getUuid())
            .alias(parameters.path("account_alias").asText())
            .roleName(parameters.path("role_name").asText())
            .authEmailId(TemporaryCredential.getAuthEmailId())
            .awsAccessKey(TemporaryCredential.getAwsAccessKey())
            .awsSecretAccessKey(TemporaryCredential.getAwsSecretAccessKey())
            .awsSessionToken(TemporaryCredential.getAwsSessionToken())
            .build();
            stsRepository.save(stsEntity);
    }
}
