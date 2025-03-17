package ci.operis.adapter.in.rest;

import ci.operis.adapter.in.rest.dto.ProjectDto;
import ci.operis.adapter.in.rest.error.ApiError;
import ci.operis.adapter.in.rest.payload.CreateProjectPayload;
import ci.operis.core.domain.CreateProjectCommand;
import ci.operis.core.domain.Project;
import ci.operis.core.port.in.CreateProjectUseCase;
import ci.operis.infrastructure.jwt.JWTConnectedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/projects")
@Tag(name = "Project API", description = "API to manage projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final JWTConnectedUserResolver jwtConnectedUserResolver;

    @Operation(summary = "Create a new project", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Project created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody @Valid CreateProjectPayload payload,
            @Parameter(description = "JWT token", required = true, hidden = true)
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
