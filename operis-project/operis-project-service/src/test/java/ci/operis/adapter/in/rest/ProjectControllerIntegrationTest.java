package ci.operis.adapter.in.rest;

import ci.operis.adapter.in.rest.dto.ProjectDto;
import ci.operis.adapter.in.rest.error.ApiError;
import ci.operis.adapter.in.rest.payload.CreateProjectPayload;
import ci.operis.adapter.out.persistence.JPAProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationTest {
    public static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjQ0MzM0ODUsInN1YiI6ImltYWQudGVzdEBnbWFpbC5jb20ifQ.3G86W0E_jhiqVKJDioofMIt4dtKzEz9W4ggfR6IM3Zo";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JPAProjectRepository jpaProjectRepository;

    @Test
    void createProjectShouldReturn201GivenValidParams() throws Exception {
        //Given
        CreateProjectPayload payload = new CreateProjectPayload("project 1", "description of project 1");

        //When
        var result = mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(payload)))
                //Then
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        var projectDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProjectDto.class);
        assertNotNull(projectDto.id());
        assertEquals("project 1", projectDto.name());
        assertEquals("description of project 1", projectDto.description());
        assertEquals("owner@gmail.com", projectDto.owner());

        //AND
        jpaProjectRepository.findById(projectDto.id()).ifPresentOrElse(
                projectEntity -> {
                    assertEquals(projectDto.id(), projectEntity.getId());
                    assertEquals(projectDto.name(), projectEntity.getName());
                    assertEquals(projectDto.description(), projectEntity.getDescription());
                    assertEquals(projectDto.owner(), projectEntity.getOwner());
                },
                () -> fail("Project not found in database")
        );

    }

    @Test
    void createProjectShouldReturnBad_RequestGivenInvalidParams() throws Exception {
        //Given
        CreateProjectPayload payload = new CreateProjectPayload(" ", "description of project 1");

        //When
        var result = mockMvc.perform(post("/api/v1/projects")
                        .header("Authorization", BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                //Then
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        //AND
        var apiError = objectMapper.readValue(result.getContentAsString(), ApiError.class);
        assertEquals("Validation failed for one or more fields", apiError.message());
        assertEquals(HttpStatus.BAD_REQUEST.name(), apiError.httpStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), apiError.statusCode());
    }



}