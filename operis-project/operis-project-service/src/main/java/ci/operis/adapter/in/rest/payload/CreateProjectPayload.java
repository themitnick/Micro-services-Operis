package ci.operis.adapter.in.rest.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateProjectPayload (
        @Schema(description = "Project name", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String name,
        @Schema(description = "Project description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String description
){}
