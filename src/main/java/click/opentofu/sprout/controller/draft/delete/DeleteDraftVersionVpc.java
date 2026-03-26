package click.opentofu.sprout.controller.draft.delete;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.service.implementations.DeleteDraftVersion;
import click.opentofu.sprout.util.GeneralUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/request")
@RequiredArgsConstructor
public class DeleteDraftVersionVpc {
    
    private final GeneralUtils generalUtils;
    private final DeleteDraftVersion deleteDraftVersion;

    @CrossOrigin(
        origins = {
            "https://studio.opentofu.click"
        },
        allowCredentials = "true"
    )
    @PostMapping(path = "/aws-resources/delete-draft/aws_vpc")
    public ResponseEntity<Map<String, Object>> deleteDraftVersion (
        @RequestBody ResourceDto resourceDto,
        HttpServletRequest request
    ) {
        resourceDto.setModuleName("aws_vpc");

        Object objectRoles = request.getAttribute("roles");
        Object objectEmail = request.getAttribute("jwtAccessTokenEmail");
        List<String> roles = generalUtils.castToListOfString(objectRoles);
        // String authEmailId = generalUtils.castToString(objectEmail).split("@")[0];
        String email = generalUtils.castToString(objectEmail);
        generalUtils.isAuthorizedForWrite(roles, email);

        deleteDraftVersion.deleteResourceSaveName(resourceDto);
        return ResponseEntity.ok(Map.of("result", "Operation succeeded"));
    }
}
