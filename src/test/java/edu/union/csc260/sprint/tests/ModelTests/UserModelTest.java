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

public class UserModelTest {
    private User user;
    private String firstName;
    private String lastName;
    private boolean type;
    private int group;

    @Before
    public void setUp() {
        firstName = "Yx";
        lastName = "L";
        type = true;
        group = 1;

        user = new User(firstName, lastName, type);
        user.setGroup(group);
    }

    @Test
    public void testUserProperties() {
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(type, user.getType());
        assertEquals(group, user.getGroup());

        String newFirstName = "Kristina";
        user.setFirstName(newFirstName);
        assertEquals(newFirstName, user.getFirstName());

        String newLastName = "S";
        user.setLastName(newLastName);
        assertEquals(newLastName, user.getLastName());

        boolean newType = false;
        user.setType(newType);
        assertEquals(newType, user.getType());

        int newGroup = 2;
        user.setGroup(newGroup);
        assertEquals(newGroup, user.getGroup());
    }
}
