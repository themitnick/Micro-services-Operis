package ci.operis.adapter.in.rest.payload;

import javax.validation.constraints.NotBlank;

public record CreateProjectPayload (
        @NotBlank
        String name,
        String description
){}
