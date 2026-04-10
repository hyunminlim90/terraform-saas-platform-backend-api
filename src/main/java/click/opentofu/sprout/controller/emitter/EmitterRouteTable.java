package click.opentofu.sprout.controller.emitter;

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

import click.opentofu.sprout.util.EmitterUtils;
import click.opentofu.sprout.util.FunctionUtils;
import click.opentofu.sprout.util.GeneralUtils;
import jakarta.servlet.http.HttpServletRequest;
import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/request")
@RequiredArgsConstructor
public class EmitterRouteTable {
    
    private final GeneralUtils generalUtils;
    private final FunctionUtils functionUtils;
    private final EmitterUtils emitterUtils;

    private final Map<String, AsyncServiceSingle> asyncServiceSingleMap;

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/issue-unique-id/aws_route_table")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> issueUniqueId (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "issueUniqueId"); },
            () -> { return CompletableFuture.completedFuture(ResponseEntity.ok(Map.of("empty_task", "null"))); }
        );
    }

    // @CrossOrigin(
    //     origins = {
    //         "https://studio.opentofu.click"
    //     }
    // )
    // @GetMapping("/aws-resources/stream-emitter")
    // public SseEmitter streamEmitter (
    //     @RequestParam String uniqueId
    // ) {
    //     SseEmitter sseEmitter = emitterUtils.getEmitterByUniqueId(uniqueId);
    //     emitterUtils.emitterExists(sseEmitter, uniqueId);
    //     return sseEmitter;
    // }

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/exec-tofu-plan/aws_route_table")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cleanUpDirectoryPlan (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        resourceDto.setModuleName("aws_route_table");

        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "cleanUpDirectory"); },
            () -> { return tofuResourcePlan(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuResourcePlan (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuResource"); },
            () -> { return tofuModulePlan(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuModulePlan (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuModule"); },
            () -> { return execTofuPlan(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> execTofuPlan (
        ResourceDto resourceDto
    ) {
        String uniqueId = resourceDto.getUniqueId();
        SseEmitter emitter = emitterUtils.getEmitterByUniqueId(uniqueId);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, emitter, "execTofuPlan"); },
            () -> { return CompletableFuture.completedFuture(ResponseEntity.ok(Map.of("empty_task", "null"))); }
        );
    }

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/exec-tofu-apply/aws_route_table")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cleanUpDirectoryApply (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        resourceDto.setModuleName("aws_route_table");

        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "cleanUpDirectory"); },
            () -> { return tofuResourceApply(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuResourceApply (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuResource"); },
            () -> { return tofuModuleApply(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuModuleApply (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuModule"); },
            () -> { return execTofuApply(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> execTofuApply (
        ResourceDto resourceDto
    ) {
        String uniqueId = resourceDto.getUniqueId();
        SseEmitter emitter = emitterUtils.getEmitterByUniqueId(uniqueId);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, emitter, "execTofuApply"); },
            () -> { return CompletableFuture.completedFuture(ResponseEntity.ok(Map.of("empty_task", "null"))); }
        );
    }

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/exec-tofu-plan-destroy/aws_route_table")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cleanUpDirectoryPlanDestroy (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        resourceDto.setModuleName("aws_route_table");

        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "cleanUpDirectory"); },
            () -> { return tofuResourcePlanDestroy(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuResourcePlanDestroy (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuResource"); },
            () -> { return tofuModulePlanDestroy(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuModulePlanDestroy (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuModule"); },
            () -> { return execTofuPlanDestroy(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> execTofuPlanDestroy (
        ResourceDto resourceDto
    ) {
        String uniqueId = resourceDto.getUniqueId();
        SseEmitter emitter = emitterUtils.getEmitterByUniqueId(uniqueId);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, emitter, "execTofuPlanDestroy"); },
            () -> { return CompletableFuture.completedFuture(ResponseEntity.ok(Map.of("empty_task", "null"))); }
        );
    }

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/exec-tofu-destroy/aws_route_table")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cleanUpDirectoryDestroy (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        resourceDto.setModuleName("aws_route_table");

        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "cleanUpDirectory"); },
            () -> { return tofuResourceDestroy(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuResourceDestroy (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuResource"); },
            () -> { return tofuModuleDestroy(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> tofuModuleDestroy (
        ResourceDto resourceDto
    ) {
        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, null, "tofuModule"); },
            () -> { return execTofuDestroy(resourceDto); }
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> execTofuDestroy (
        ResourceDto resourceDto
    ) {
        String uniqueId = resourceDto.getUniqueId();
        SseEmitter emitter = emitterUtils.getEmitterByUniqueId(uniqueId);

        return functionUtils.asyncChain(
            resourceDto,
            () -> { return asyncServiceSingle(resourceDto, emitter, "execTofuDestroy"); },
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
