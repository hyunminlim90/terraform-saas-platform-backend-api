package click.opentofu.sprout.service.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.handler.entity.entity.sts.entity.StsEntity;
import click.opentofu.sprout.handler.entity.repository.StsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final StsRepository stsRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getAwsAccountsByEmail (String authEmailId) {
        List<StsEntity> stsEntity = stsRepository.findByAuthEmailId(authEmailId);
        log.warn("------------------------------------------");
        log.warn("stsEntity: ");
        log.warn(stsEntity.toString());
        log.warn("Requester: " + authEmailId);
        log.warn("------------------------------------------");
        Map<String, Object> response = new HashMap<>();
        if (!stsEntity.isEmpty()) {
            response.put("AwsAccountList", stsEntity);
        } else {
            response.put("isAwsAccountList", false);
            throw new RuntimeException("Temporary credentials are not registered.\nPlease register your temporary credentials.\n Requester: " + authEmailId);
        }
        return response;
    }
}
