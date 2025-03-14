package ci.operis.adapter.out.persistence;

import ci.operis.core.domain.Project;
import ci.operis.core.port.out.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class PostgreSQLProjectRepositoryAdapter implements ProjectRepository {

    private final JPAProjectRepository jpaProjectRepository;
    @Override
    public void createProject(Project project) {

        ProjectEntity projectEntity = new ProjectEntity();
        jpaProjectRepository.save(projectEntity);

    }
}