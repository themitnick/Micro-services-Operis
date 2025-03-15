package ci.operis.adapter.in.rest;

import ci.operis.adapter.in.rest.dto.ProjectDto;
import ci.operis.adapter.in.rest.payload.CreateProjectPayload;
import ci.operis.core.domain.CreateProjectCommand;
import ci.operis.core.domain.Project;
import ci.operis.core.port.in.CreateProjectUseCase;
import ci.operis.infrastructure.jwt.JWTConnectedUserResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final JWTConnectedUserResolver jwtConnectedUserResolver;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody @Valid CreateProjectPayload payload,
            @RequestHeader("Authorization") String token
            ) {

        String userConnected = jwtConnectedUserResolver.extractConnectedUserEmail(token);
        CreateProjectCommand command = new CreateProjectCommand(
                payload.name(),
                payload.description(),
                userConnected
        );
        Project project = createProjectUseCase.createProject(command);
        return new ResponseEntity<>(new ProjectDto(project.getId(), project.getName(), project.getDescription(), project.getOwner()), HttpStatus.CREATED);
    }
}
