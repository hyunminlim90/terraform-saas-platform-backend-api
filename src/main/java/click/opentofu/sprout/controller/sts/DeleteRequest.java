package click.opentofu.sprout.controller.sts;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
        try {
            Object objectRoles = request.getAttribute("roles");
            Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
            List<String> roles = generalUtils.castToListOfString(objectRoles);
            // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
            String accountId = accountRequest.getAccountId();
            String email = generalUtils.castToString(objectEmail);
            generalUtils.isAuthorizedForWrite(roles, email);
            deleteService.deleteAwsAccount(accountId, email);
            return ResponseEntity.ok().body(Collections.emptyMap());    
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorType", e.getClass().getSimpleName());
            errorResponse.put("result", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
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
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);
        deleteService.deleteAwsWorkspace(email);
        return ResponseEntity.ok().body(Collections.emptyMap());
    }
}
