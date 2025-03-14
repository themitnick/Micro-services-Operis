package ci.operis.core.port.in;

import ci.operis.core.domain.CreateProjectCommand;
import ci.operis.core.domain.Project;

public interface CreateProjectUseCase {
    public Project createProject(CreateProjectCommand command) ;
}
