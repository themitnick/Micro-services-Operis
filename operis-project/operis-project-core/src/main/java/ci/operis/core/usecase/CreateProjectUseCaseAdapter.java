package ci.operis.core.usecase;

import ci.operis.core.domain.CreateProjectCommand;
import ci.operis.core.domain.Project;
import ci.operis.core.port.in.CreateProjectUseCase;
import ci.operis.core.port.out.ProjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateProjectUseCaseAdapter implements CreateProjectUseCase {

    private final ProjectRepository projectRepository;
    @Override
    public Project createProject(CreateProjectCommand command) {

        Project projectToCreate = new Project(command.name(), command.description(), command.owner());
        projectRepository.createProject(projectToCreate);

        return projectToCreate;
    }
}
