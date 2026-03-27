package click.opentofu.sprout.controller.draft.duplicate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.FunctionUtils;
import click.opentofu.sprout.util.GeneralUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/request")
@RequiredArgsConstructor
public class DuplicateDraftVpcIpamPool {

    private final GeneralUtils generalUtils;
    private final FunctionUtils functionUtils;

    private final Map<String, AsyncServiceSingle> asyncServiceSingleMap;
    
    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/duplicate-draft/aws_vpc_ipam_pool")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> existsDraftVersion (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        resourceDto.setModuleName("aws_vpc_ipam_pool");

        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "existsDraftVersion"); },
            () -> { return duplicateDraftVersions(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> duplicateDraftVersions (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "duplicateDraftVersions"); },
            () -> { return CompletableFuture.completedFuture(ResponseEntity.ok(Map.of("empty_task", "null"))); }
        );
    }

    private <T> CompletableFuture<Object> asyncServiceSingle (T dto, SseEmitter emitter, String serviceName) {
        AsyncServiceSingle service = asyncServiceSingleMap.get(serviceName);
        generalUtils.beanExists(service, generalUtils.capitalizeFirst(serviceName));
        CompletableFuture<Object> completableFuture = service.mainWorkerAsync(dto, emitter);
        return completableFuture;
    }
}
