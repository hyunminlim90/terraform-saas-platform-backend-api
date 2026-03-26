package click.opentofu.sprout.controller.sts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.sts.AwsCloudRequest;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingleDownload;
import click.opentofu.sprout.util.FunctionUtils;
import click.opentofu.sprout.util.GeneralUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/request")
@RequiredArgsConstructor
public class DownloadTerraformRequest {

    private final GeneralUtils generalUtils;
    private final FunctionUtils functionUtils;

    private final Map<String, AsyncServiceSingleDownload> asyncServiceSingleDownloadMap;

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/downloads")
    public CompletableFuture<ResponseEntity<InputStreamResource>> asyncDownloadVpc (
        @RequestBody AwsCloudRequest awsCloudRequest,
        HttpServletRequest request
    ) {
        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        CompletableFuture<InputStreamResource> completableFuture = asyncServiceSingleDownload(awsCloudRequest, null, "terraformDownload");
        return functionUtils.asyncDownloadResponse(completableFuture, "terraform-module-download.zip");        
    }

    private <T> CompletableFuture<InputStreamResource> asyncServiceSingleDownload (T dto, SseEmitter emitter, String serviceName) {
        AsyncServiceSingleDownload service = asyncServiceSingleDownloadMap.get(serviceName);
        generalUtils.beanExists(service, generalUtils.capitalizeFirst(serviceName));
        CompletableFuture<InputStreamResource> completableFuture = service.mainWorkerAsync(dto, emitter);
        return completableFuture;
    }
}
