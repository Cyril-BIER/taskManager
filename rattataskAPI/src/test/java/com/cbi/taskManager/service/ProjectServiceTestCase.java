package com.cbi.taskManager.service;

import com.cbi.taskManager.dto.CreateProjectDTO;
import com.cbi.taskManager.dto.CreateTaskDTO;
import com.cbi.taskManager.model.Project;
import com.cbi.taskManager.model.Task;
import com.cbi.taskManager.model.User;
import com.cbi.taskManager.repository.ProjectRepository;
import com.cbi.taskManager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTestCase {
    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ProjectService projectService;

    @Test
    public void postOneProjectTest(){
        CreateProjectDTO createProjectDTO = new CreateProjectDTO("My project");
        projectService.createProject(createProjectDTO);
        verify(projectRepository).save(new Project("My project"));
    }

    @Test
    public void getZeroProjectTest(){
        when(projectRepository.findAllById(List.of(1L))).thenReturn(List.of());
        List<ProjectDTO> actual = projectService.getProjects(List.of(1L));
        assertEquals(List.of(),actual);
    }

    @Test
    public void getOneProjectTest(){
        ProjectDTO expected = new ProjectDTO(null,"Project 1", List.of());
        when(projectRepository.findAllById(List.of(1L))).thenReturn(List.of(
                new Project("Project 1")
        ));
        List<ProjectDTO> actual = projectService.getProjects(List.of(1L));
        assertEquals(List.of(expected),actual);
    }

    @Test
    public void getManyProjectTest(){
        List<ProjectDTO> expected = List.of(
                new ProjectDTO(null,"Project 1", List.of()),
                new ProjectDTO(null,"Project 2", List.of())
        );
        when(projectRepository.findAllById(List.of(1L,2L)))
                .thenReturn(List.of(
                        new Project("Project 1"),
                        new Project("Project 2")));
        List<ProjectDTO> actual = projectService.getProjects(List.of(1L,2L));
        assertEquals(expected,actual);
    }

    @Test
    public void getAllProjectsTest(){
        projectService.getProjects(List.of());
        verify(projectRepository).findAll();
    }

    @Test
    public void add1TaskTest(){
        List<User> users = List.of( new User(1L, "mail","name", "lastname", "password"));
        List<Task> tasks = List.of(
                new Task("Nom tâche", "Description", users)
        );
        Project expected = new Project("Project");
        expected.addTasks(tasks);
        List<CreateTaskDTO> dtos =List.of( new CreateTaskDTO("Nom tâche","Description",List.of(1L)));

        when(userRepository.findAllById(Set.of(1L))).thenReturn(users);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project("Project")));
        projectService.addTasks(1L,dtos);
        verify(projectRepository).save(expected);
    }

    @Test
    public void addManyTasksTest(){
        User user1 = new User(1L, "mail","name", "lastname", "password");
        User user2 = new User(2L, "mail2","name", "lastname", "password");
        User user3 = new User(3L, "mail3","name", "lastname", "password");

        List<Task> tasks = List.of(
                new Task("Tâche1", "Description", List.of(user1,user2,user3)),
                new Task("Tâche2", "Description", List.of(user2)),
                new Task("Tâche3", "Description", List.of(user1,user3))
        );
        Project expected = new Project("Project");
        expected.addTasks(tasks);

        List<CreateTaskDTO> dtos =List.of(
                new CreateTaskDTO("Tâche1","Description",List.of(1L,2L,3L)),
                new CreateTaskDTO("Tâche2","Description",List.of(2L)),
                new CreateTaskDTO("Tâche3","Description",List.of(1L,3L))

        );

        when(userRepository.findAllById(Set.of(1L,2L,3L))).thenReturn(List.of(user1,user2,user3));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project("Project")));
        projectService.addTasks(1L,dtos);
        verify(projectRepository).save(expected);
    }

    @Test
    public void delete1ProjectTest(){
        projectService.delete(List.of(1L));
        verify(projectRepository).deleteAllById(List.of(1L));
    }

    @Test
    public void deleteManyProjectsTest(){
        projectService.delete(List.of(1L, 3L, 5L));
        verify(projectRepository).deleteAllById(List.of(1L, 3L, 5L));
    }
}
