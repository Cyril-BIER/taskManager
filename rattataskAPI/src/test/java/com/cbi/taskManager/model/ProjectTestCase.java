package com.cbi.taskManager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTestCase {
    @Test
    public void constructorTest(){
        Project project = new Project("Project 1");
        assertEquals("Project 1", project.getName());
    }

    @Test
    public void compareTest(){
        Project project1 = new Project("My Project");
        Project project2 = new Project("My Project");

        assertEquals(project1, project2);
    }
}