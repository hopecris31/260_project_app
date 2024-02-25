package edu.union.csc260.sprint.tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import edu.union.csc260.sprint.controller.SprintEditController;
import edu.union.csc260.sprint.controller.TaskEditController;
import edu.union.csc260.sprint.model.ProjectBacklog;
import edu.union.csc260.sprint.model.Sprint;
import edu.union.csc260.sprint.model.Task;

import java.util.ArrayList;
import java.util.List;

public class ControllerTest {
    @Test
    public void testTaskEdit(){
        Task polkaTask = new Task("paint polka dots", "draw dots around smiley face",
                "use a sponge brush for dots");

        assertFalse(polkaTask.isCompleted());
        assertEquals("paint polka dots", polkaTask.getName());
        assertEquals("draw dots around smiley face", polkaTask.getTask());
        assertEquals("use a sponge brush for dots", polkaTask.getComments());

        polkaTask.setCompleted(true);
        assertTrue(polkaTask.isCompleted());

        polkaTask.setCompleted(true);
        assertTrue(polkaTask.isCompleted());

        TaskEditController testingTask = new TaskEditController();
        assertFalse(testingTask.isOkClicked());
    }

    @Test
    public void testBacklogEdit(){
        List<Task> taskList2 = new ArrayList<>();
        Task polkaTask = new Task("paint polka dots", "draw dots around smiley face",
                "use a sponge brush for dots");
        Task stripesTask = new Task ("lines in dots", "add design to the polka dots",
                "draw 3-4 diagonal lines in each dot");
        taskList2.add(polkaTask);
        taskList2.add(stripesTask);

        ProjectBacklog addDesignsToPainting = new ProjectBacklog("drawing designs",
                "add polka dots & stripes", taskList2);

        addDesignsToPainting.setTitle("for the painting");
        assertEquals("for the painting", addDesignsToPainting.getTitle());

        addDesignsToPainting.setBacklogGoal("to create a painting");
        assertEquals("to create a painting", addDesignsToPainting.getBacklogGoal());

        Task chevronTask =  new Task("paint chevron pattern", "draw zigzags in blank space",
                "use pink paint");
        List<Task> newList = new ArrayList<>();
        newList.add(chevronTask);
        assertEquals(newList, addDesignsToPainting.getTasks());
    }

    @Test
    public void testSprintEdit(){
        List<Task> taskList2 = new ArrayList<>();
        Task polkaTask = new Task("paint polka dots", "draw dots around smiley face",
                "use a sponge brush for dots");
        Task stripesTask = new Task ("lines in dots", "add design to the polka dots",
                "draw 3-4 diagonal lines in each dot");
        taskList2.add(polkaTask);
        taskList2.add(stripesTask);

        ProjectBacklog addDesignsToPainting = new ProjectBacklog("drawing designs",
                "add polka dots & stripes", taskList2);

        Sprint paintingSprint = new Sprint(addDesignsToPainting);

        paintingSprint.setTitle("let's draw");
        assertEquals("let's draw", paintingSprint.getTitle());

        paintingSprint.setGoal("adding patterns");
        assertEquals("adding patterns", paintingSprint.getGoal());

        paintingSprint.setReflection("this painting looks good");
        assertEquals("this painting looks good", paintingSprint.getReflection());

        Task chevronTask =  new Task("paint chevron pattern", "draw zigzags in blank space",
                "use pink paint");
        List<Task> newList = new ArrayList<>();
        newList.add(chevronTask);
        assertEquals(newList, paintingSprint.getTasks());

        SprintEditController testingSprint = new SprintEditController();

        assertFalse(testingSprint.isOkClicked());
    }
}
