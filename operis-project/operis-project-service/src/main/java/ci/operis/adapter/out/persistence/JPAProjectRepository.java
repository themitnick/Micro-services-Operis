package ci.operis.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAProjectRepository extends JpaRepository<ProjectEntity, String> {
}
