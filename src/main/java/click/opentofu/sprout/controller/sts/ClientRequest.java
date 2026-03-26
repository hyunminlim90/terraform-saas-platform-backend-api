package click.opentofu.sprout.controller.sts;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.FunctionUtils;
import click.opentofu.sprout.util.GeneralUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/register")
@RequiredArgsConstructor
public class ClientRequest {
    
    private final FunctionUtils functionUtils;
    private final GeneralUtils generalUtils;
    private final Map<String, AsyncServiceSingle> asyncServiceSingleMap;

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/temporary-credential")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> asyncRegisterTemporaryCredential (
        @RequestBody TemporaryCredential temporaryCredential,
        HttpServletRequest request
    ) {
        // Object objectRoles = request.getAttribute("roles");
        // List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = temporaryCredential.getAuthEmailId();
        // generalUtils.isAuthorizedForWrite(roles, authEmailId);
        return functionUtils.asyncChainForSts(
            temporaryCredential,
            () -> { return asyncServiceSingle(temporaryCredential, null, "pythonBoto"); },
            () -> { return asyncReadJsonAndSave(temporaryCredential); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> asyncReadJsonAndSave (
        TemporaryCredential temporaryCredential
    ) {
        return functionUtils.asyncChainForSts(
            temporaryCredential,
            () -> { return asyncServiceSingle(temporaryCredential, null, "jsonAndSave"); },
            () -> { return asyncDeleteStsUtils(temporaryCredential); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> asyncDeleteStsUtils (
        TemporaryCredential temporaryCredential
    ) {
        return functionUtils.asyncChainForSts(
            temporaryCredential,
            () -> { return asyncServiceSingle(temporaryCredential, null, "deleteStsUtils"); },
            () -> { return CompletableFuture.completedFuture(ResponseEntity.ok(Map.of("result_message", "Operation succeeded"))); }
        );
    }

    // private <T> CompletableFuture<String> asyncServiceMulti (T dto, SseEmitter emitter, String serviceName) {
    //     AsyncServiceMulti service = asyncServiceMultiMap.get(serviceName);
    //     generalUtils.beanExists(service, generalUtils.capitalizeFirst(serviceName));
    //     CompletableFuture<String> completableFuture = service.mainWorkerAsync(dto, emitter);
    //     return completableFuture;
    // }

    private <T> CompletableFuture<Object> asyncServiceSingle (T dto, SseEmitter emitter, String serviceName) {
        AsyncServiceSingle service = asyncServiceSingleMap.get(serviceName);
        generalUtils.beanExists(service, generalUtils.capitalizeFirst(serviceName));
        CompletableFuture<Object> completableFuture = service.mainWorkerAsync(dto, emitter);
        return completableFuture;
    }
}
