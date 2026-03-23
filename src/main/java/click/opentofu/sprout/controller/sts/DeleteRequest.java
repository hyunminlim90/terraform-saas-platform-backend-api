package click.opentofu.sprout.controller.sts;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.opentofu.sprout.dto.sts.AccountRequest;
import click.opentofu.sprout.service.implementations.DeleteService;
import click.opentofu.sprout.util.GeneralUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/request")
@RequiredArgsConstructor
public class DeleteRequest {

    private final GeneralUtils generalUtils;
    private final DeleteService deleteService;
    
    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-account/delete")
    public ResponseEntity<Map<String, Object>> requestDelete (
        @RequestBody AccountRequest accountRequest,
        HttpServletRequest request
    ) {
        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String accountId = accountRequest.getAccountId();
        generalUtils.isAuthorizedForWrite(roles, authEmailId);
        deleteService.deleteAwsAccount(accountId, authEmailId);
        return ResponseEntity.ok().body(Collections.emptyMap());
    }

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/workspace/empty/discard")
    public ResponseEntity<Map<String, Object>> requestDiscard (
        HttpServletRequest request
    ) {
        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        generalUtils.isAuthorizedForWrite(roles, authEmailId);
        deleteService.deleteAwsWorkspace(authEmailId);
        return ResponseEntity.ok().body(Collections.emptyMap());
    }
}
