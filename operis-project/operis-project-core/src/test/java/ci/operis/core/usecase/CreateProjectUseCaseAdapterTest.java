package ci.operis.core.usecase;

import ci.operis.core.domain.CreateProjectCommand;
import ci.operis.core.domain.Project;
import ci.operis.core.port.out.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateProjectUseCaseAdapterTest {
    @InjectMocks
    private CreateProjectUseCaseAdapter createProjectUseCaseAdapter;
    @Mock
    private ProjectRepository projectRepository;

    @Test
    void createProjectShouldSuccessfullyGivenValidParams() {
        //Given
        CreateProjectCommand command = new CreateProjectCommand(
                "project 1", "description of project 1", "owner@gmail.com"
        );

        //When
        createProjectUseCaseAdapter.createProject(command);

        //Then
        ArgumentCaptor<Project> projectArgumentCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository, times(1)).createProject(projectArgumentCaptor.capture());
        Project projectCaptured = projectArgumentCaptor.getValue();
        assertNotNull(projectCaptured.getId());
        assertEquals("project 1", projectCaptured.getName());
        assertEquals("owner@gmail.com", projectCaptured.getOwner());
        assertTrue(projectCaptured.getMembers().contains("owner@gmail.com"));
    }

    @ParameterizedTest
    @CsvSource({
            "' ', owner@gmail.com, Project name cannot be null or empty",
            "'', owner@gmail.com, Project name cannot be null or empty",
            ", owner@gmail.com, Project name cannot be null or empty",
            "project 1, '  ', Project owner cannot be null or empty",
            "project 1, '', Project owner cannot be null or empty",
            "project 1, , Project owner cannot be null or empty",
    })
    void createProjectShouldThrowExceptionGivenInvalidParams(String name, String owner, String expectedMessage) {
        //Given
        CreateProjectCommand command = new CreateProjectCommand(
                name, "description of project 1", owner
        );

        //When
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> createProjectUseCaseAdapter.createProject(command));

        //Then
        assertEquals(expectedMessage, actualException.getMessage());
        verify(projectRepository, times(0)).createProject(null);
    }

}