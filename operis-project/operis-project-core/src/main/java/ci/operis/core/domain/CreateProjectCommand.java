package ci.operis.core.domain;

public record CreateProjectCommand(String name, String description, String owner) {
}
