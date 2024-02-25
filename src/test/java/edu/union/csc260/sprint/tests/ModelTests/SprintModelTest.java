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


public class SprintModelTest{
   private Sprint sprint;

   @Test
   public void testSprintProperties(){
       String title = "Sprint 1";
       sprint.setTitle(title);
       assertEquals(title, sprint.getTitle());


       String goal = "Finish this";
       sprint.setGoal(goal);
       assertEquals(goal, sprint.getGoal());


       LocalDate estFinishDate = LocalDate.now().plusWeeks(2);
       sprint.setEstFinishDate(estFinishDate);
       assertEquals(estFinishDate, sprint.getEstFinishDate());


       List<Task> tasks = new ArrayList<>();
       tasks.add(new Task("Task 3", "something..", "no comments"));
       assertEquals(tasks, sprint.getTasks());

       sprint.setCompleted(true);
       String reflection = "Test Reflection";
       sprint.setReflection(reflection);
       assertEquals(reflection, sprint.getReflection().getText());
   }


   @Test
   public void testSetCompleted() {
       sprint.setCompleted(true);
       assertTrue(sprint.isCompleted());
   }
}
