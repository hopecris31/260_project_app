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

public class GroupModelTest {
    private Group group;
    private String groupName;
    private List<User> groupMembers;

    @Before
    public void setUp() {
        groupName = "Test Group";
        groupMembers = new ArrayList<>();
        groupMembers.add(new User("Yx","L",true));
        groupMembers.add(new User("Zy","L",true));

        group = new Group(groupName, groupMembers);
    }

    @Test
    public void testGroupProperties() {
        assertNotNull(group.groupNameProperty());

        assertEquals(groupName, group.getGroupName());
        assertEquals(groupMembers, group.getGroupMembers());

        String newGroupName = "New Group";
        group.setGroupName(newGroupName);
        assertEquals(newGroupName, group.getGroupName());

    }
}
