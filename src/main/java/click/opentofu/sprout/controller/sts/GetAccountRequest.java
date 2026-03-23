package click.opentofu.sprout.controller.sts;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.service.implementations.AccountService;
import click.opentofu.sprout.util.GeneralUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/request")
@RequiredArgsConstructor
public class GetAccountRequest {

    private final GeneralUtils generalUtils;
    private final AccountService accountService;
    
    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-accounts")
    public ResponseEntity<Map<String, Object>> requestAwsAccounts (
        @RequestBody TemporaryCredential temporaryCredential,
        HttpServletRequest request
    ) {
        Object objectRoles = request.getAttribute("roles");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        String authEmailId = temporaryCredential.getAuthEmailId();
        generalUtils.isAuthorizedForRead(roles, authEmailId);
        
        Map<String, Object> response = accountService.getAwsAccountsByEmail(authEmailId);
        return ResponseEntity.ok(response);
    }
}
