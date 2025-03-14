package ci.operis.core.port.out;

import ci.operis.core.domain.Project;

public interface ProjectRepository {
    void createProject(Project project);
}
