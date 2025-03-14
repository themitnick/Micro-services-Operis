package ci.operis.adapter.config;

import ci.operis.core.port.in.CreateProjectUseCase;
import ci.operis.core.port.out.ProjectRepository;
import ci.operis.core.usecase.CreateProjectUseCaseAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectAdapterConfig {

    @Bean
    public CreateProjectUseCase createProjectUseCase(ProjectRepository projectRepository) {
        return new CreateProjectUseCaseAdapter(projectRepository);
    }
}
