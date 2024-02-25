package edu.union.csc260.sprint.tests.ModelTests;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import edu.union.csc260.sprint.model.*;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.*;
public class TaskModelTest{
   @Test
   public void testTaskProperties(){
       Task task = new Task("Task 1", "Something", "Some comments");
       assertEquals("Task 1", task.getName());
       assertEquals("Something", task.getTask());
       assertEquals("Some comments", task.getComments());

       task.setName("Updated Task 1");
       task.setTask("Something else");
       task.setComments("Updated comments");
       assertEquals("Updated Task 1", task.getName());
       assertEquals("Something else", task.getTask());
       assertEquals("Updated comments", task.getComments());
   }


   @Test
   public void testTaskCompletion() {
       Task task = new Task();
       assertFalse(task.isCompleted());
       task.setCompleted(true);
       assertTrue(task.isCompleted());
   }
}


