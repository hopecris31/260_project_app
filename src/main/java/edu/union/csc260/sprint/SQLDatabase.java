package edu.union.csc260.sprint;

import edu.union.csc260.sprint.model.Group;
import edu.union.csc260.sprint.model.User;
import edu.union.csc260.sprint.model.Task;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLDatabase {
    private static Connection connection;
    public static void setConnection() {
        String url = "jdbc:mysql://127.0.0.1:3333/group2_db";
        String username = "group2user";
        String password = "group2_user-Un1on1795";
        System.out.println("======================");
        System.out.println("Connecting database...");
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot close the database.", e);
        }
        System.out.println("Database connection closed!");
        System.out.println("===========================");
    }

    /**
     * ProjectBacklog Queries
     */

    public static String getBacklogItemTitle(int backlogItemId) {
        String backlogItemTitle = null;
        try {
            setConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT backlogItemTitle FROM backlogItems WHERE backlogItemId = " + backlogItemId);

            if (rs.next()) {
                backlogItemTitle = rs.getString("backlogItemTitle");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally{ closeConnection(); }

        return backlogItemTitle;
    }

    public static void setBacklogItemTitle(int backlogItemId, String newTitle) {

        try {
            setConnection();
            Statement statement = connection.createStatement();
            String query = "UPDATE backlogItems SET backlogItemTitle = '" + newTitle + "' WHERE backlogItemId = " + backlogItemId;
            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static StringProperty backlogItemTitlePropertyQuery(int backlogItemId) {
        StringProperty title = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT backlogItemTitle FROM backlogItems WHERE backlogItemId = ?");

            pstmt.setInt(1, backlogItemId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                title = new SimpleStringProperty(rs.getString("backlogItemTitle"));
            } else {
                throw new RuntimeException("No such backlogItemId in backlogItems table.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }
        return title;
    }

    public static void setBacklogItemGoal(int backlogItemId, String newGoal) {
        try {
            setConnection();
            Statement statement = connection.createStatement();
            String query = "UPDATE backlogItems SET backlogItemGoal = ? WHERE backlogItemId = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, newGoal);
            pstmt.setInt(2, backlogItemId);
            pstmt.executeUpdate();

            pstmt.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static ObservableList<Task> getTasksQuery(int backlogItemId) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();

        try {
            setConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT taskName FROM tasks WHERE backlogItemId = " + backlogItemId);

            while (rs.next()) {
                String taskName = rs.getString("taskName");
                Task task = new Task(taskName, null, null);
                tasks.add(task);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return tasks;
    }




    public static String getBacklogItemGoal(int backlogItemId) {
        String backlogItemGoal = null;
        try {
            setConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT backlogItemGoal FROM backlogItems WHERE backlogItemId = " + backlogItemId);

            if (rs.next()) {
                backlogItemGoal = rs.getString("backlogItemGoal");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return backlogItemGoal;
    }

    public static void addTask(int backlogItemId, Task task) {
        try {
            setConnection();
            String query = "INSERT INTO tasks (backlogItemId, taskName, taskGoal, taskCompleted) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, backlogItemId);
            pstmt.setString(2, task.getName());
            pstmt.setString(3, task.getTask());
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem inserting into the database.", e);
        } finally {
            closeConnection();
        }
    }

    /*
     * Group queries
     */

    public static void setGroupNumberQuery(int groupId, int newGroupNumber) {

        try {
            setConnection();
            String query = "UPDATE scrumGroup SET groupNumber = ? WHERE groupId = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, newGroupNumber);
            pstmt.setInt(2, groupId);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }


    public static int getGroupNumberQuery(int groupId) {
        int groupNumber = -1;

        try {
            setConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT groupNumber FROM scrumGroup WHERE groupId = " + groupId);

            if (rs.next()) {
                groupNumber = rs.getInt("groupNumber");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return groupNumber;
    }

    /**
     * Group Queries
     */

    public int addGroupAndAssignToUser(Group group, User user) {
        int groupId = -1;
        try {
            setConnection();

            PreparedStatement groupStatement = connection.prepareStatement(
                    "INSERT INTO scrumGroup (groupName, groupNumber) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            groupStatement.setString(1, group.getGroupName());
            groupStatement.setInt(2, group.getGroupNumber());
            int affectedRows = groupStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating group failed, no rows affected.");
            }

            try (ResultSet generatedKeys = groupStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    groupId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating group failed, no ID obtained.");
                }
            }

            PreparedStatement userStatement = connection.prepareStatement(
                    "UPDATE user SET groupId = ? WHERE userId = ?");
            userStatement.setInt(1, groupId);
            userStatement.setInt(2, user.getUserId());
            userStatement.executeUpdate();

            groupStatement.close();
            userStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }

        return groupId;
    }


    public void setGroupNumber(int groupId, int newGroupNumber) {

        try {
            setConnection();
            String query = "UPDATE scrumGroup SET groupNumber = ? WHERE groupId = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, newGroupNumber);
            pstmt.setInt(2, groupId);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static String getGroupNameQuery(int groupId) {
        String groupName = null;

        try {
            setConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT groupName FROM scrumGroup WHERE groupId = " + groupId);

            if (rs.next()) {
                groupName = rs.getString("groupName");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return groupName;
    }

    public static void setGroupNameQuery(int groupId, String groupName) {

        try {
            setConnection();
            String query = "UPDATE scrumGroup SET groupName = ? WHERE groupId = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, groupName);
            pstmt.setInt(2, groupId);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static StringProperty getGroupNamePropertyQuery(int groupId) {
        StringProperty name = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT groupName FROM scrumGroup WHERE sprintId = ?");

            pstmt.setInt(1, groupId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                name = new SimpleStringProperty(rs.getString("groupName"));
            } else {
                throw new RuntimeException("No such groupId in scrumGroup table.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }
        return name;
    }

    public static ObservableList<User> getGroupMembersQuery(int groupId) {
        ObservableList<User> users = FXCollections.observableArrayList();
        setConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT firstName, lastName, userType FROM user WHERE groupId = " + groupId);
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                boolean type = rs.getBoolean("userType");
                User user = new User(firstName, lastName, type);
                users.add(user);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return users;
    }



    /**
     * Sprint Queries
     */

    public static String getTitleQuery(int sprintId) {
        String title = null;

        try {
            setConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                "SELECT b.backlogItemTitle FROM sprints s " +
                "JOIN backlogItems b ON s.sprintId = b.sprintId " +
                "WHERE s.sprintId = " + sprintId);

            if (rs.next()) {
                title = rs.getString("backlogItemTitle");
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return title;
    }


    public static void setTitleQuery(String title, int sprintId) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE backlogItems SET backlogItemTitle = ? WHERE sprintId = ?");

            pstmt.setString(1, title);
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static StringProperty titlePropertyQuery(int sprintId) {
        StringProperty title = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT backlogItemTitle FROM backlogItems WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                title = new SimpleStringProperty(rs.getString("backlogItemTitle"));
            } else {
                throw new RuntimeException("No such sprintId in backlogItems table.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }
        return title;
    }

    public static String getGoalQuery(int sprintId) {
        String goal = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT backlogItemGoal FROM backlogItems WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                goal = rs.getString("backlogItemGoal");
            } else {
                throw new RuntimeException("No such sprintId in backlogItems table.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }
        return goal;
    }


    public static void setGoalQuery(String goal, int sprintId) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE backlogItems SET backlogItemGoal = ? WHERE sprintId = ?");

            pstmt.setString(1, goal);
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static LocalDate getEstFinishDateQuery(int sprintId) {
        LocalDate estFinishDate = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT finishDate FROM sprints WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                estFinishDate = rs.getDate("finishDate").toLocalDate();
            } else {
                throw new RuntimeException("No such sprintId in sprints table.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return estFinishDate;
    }

    public static void setEstFinishDateQuery(LocalDate estFinishDate, int sprintId) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE sprints SET finishDate = ? WHERE sprintId = ?");

            pstmt.setDate(1, java.sql.Date.valueOf(estFinishDate));
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();


            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }


    public static boolean isSprintCompletedQuery(int sprintId) {
        boolean isCompleted = false;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT isCompleted FROM sprints WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isCompleted = rs.getBoolean("isCompleted");
            } else {
                throw new RuntimeException("No such sprintId in sprints table.");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return isCompleted;
    }

    public static void setCompletedQuery(boolean value, int sprintId) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE sprints SET isCompleted = ? WHERE sprintId = ?");

            pstmt.setBoolean(1, value);
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static TextArea getReflectionQuery(int sprintId) {
        TextArea reflectionTextArea = new TextArea();

        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT reflection FROM sprints WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String reflection = rs.getString("reflection");
                reflectionTextArea.setText(reflection);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return reflectionTextArea;
    }



    public static void setReflection(String reflection, int sprintId) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE sprints SET reflection = ? WHERE sprintId = ?");

            pstmt.setString(1, reflection);
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static boolean isCurrentSprintQuery(int sprintId) {
        boolean isCurrentSprint = false;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT isCurrentSprint FROM sprints WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isCurrentSprint = rs.getBoolean("isCurrentSprint");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return isCurrentSprint;
    }

    public static void setCurrentSprintQuery(int sprintId, boolean isCurrentSprint) {
        try {
            setConnection();
            PreparedStatement pstmtReset = connection.prepareStatement(
                    "UPDATE sprints SET isCurrentSprint = 0");
            pstmtReset.executeUpdate();

            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE sprints SET isCurrentSprint = ? WHERE sprintId = ?");

            pstmt.setBoolean(1, isCurrentSprint);
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();

            pstmt.close();
            pstmtReset.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static double getProgressQuery(int sprintId) {
        double progress = 0.0;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT progress FROM sprints WHERE sprintId = ?");

            pstmt.setInt(1, sprintId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                progress = rs.getDouble("progress");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return progress;
    }

    public static void setProgressQuery(int sprintId, double progress) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE sprints SET progress = ? WHERE sprintId = ?");

            pstmt.setDouble(1, progress);
            pstmt.setInt(2, sprintId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    /**
     *Task Method
     */

    public static String getNameQuery(int taskId) {
        String taskName = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT taskName FROM tasks WHERE taskId = ?");

            pstmt.setInt(1, taskId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                taskName = rs.getString("taskName");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return taskName;
    }

    public static void setNameQuery(int taskId, String name) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE tasks SET taskName = ? WHERE taskId = ?");

            pstmt.setString(1, name);
            pstmt.setInt(2, taskId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static String getTaskQuery(int taskId) {
        String taskGoal = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT taskGoal FROM tasks WHERE taskId = ?");

            pstmt.setInt(1, taskId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                taskGoal = rs.getString("taskGoal");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return taskGoal;
    }

    public static void setTaskQuery(int taskId, String task) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE tasks SET taskGoal = ? WHERE taskId = ?");

            pstmt.setString(1, task);
            pstmt.setInt(2, taskId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static String getCommentsQuery(int taskId) {
        String comments = "";
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT comments FROM tasks WHERE taskId = ?");

            pstmt.setInt(1, taskId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                comments = rs.getString("comments");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return comments;
    }

    public static void setCommentsQuery(int taskId, String comments) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE tasks SET comments = ? WHERE taskId = ?");

            pstmt.setString(1, comments);
            pstmt.setInt(2, taskId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static boolean isTaskCompletedQuery(int taskId) {
        boolean completed = false;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT taskCompleted FROM tasks WHERE taskId = ?");

            pstmt.setInt(1, taskId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                completed = rs.getBoolean("taskCompleted");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return completed;
    }

    public static void setTaskCompletedQuery(int taskId, boolean completed) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE tasks SET taskCompleted = ? WHERE taskId = ?");

            pstmt.setBoolean(1, completed);
            pstmt.setInt(2, taskId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

/**
 * User Methods
 */

    public static int addUserQuery(String firstName, String lastName, int userType, int groupId) {
        int userId = 0;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO user (firstName, lastName, userType, groupId) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, userType);
            pstmt.setInt(4, groupId);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem inserting into the database.", e);
        } finally {
            closeConnection();
        }

        return userId;
    }

    public static boolean checkUserExists(String firstName, String lastName, int type) {
        boolean exists = false;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM user WHERE firstName = ? AND lastName = ? AND userType = ?");

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, type);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return exists;
    }



    public static int getUserIdQuery(StringProperty firstName, StringProperty lastName, int type) {
        int userId = 0;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT userId FROM user WHERE firstName = ? AND lastName = ? AND userType = ?");

            pstmt.setString(1, firstName.get());
            pstmt.setString(2, lastName.get());
            pstmt.setInt(3, type);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("userId");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return userId;
    }

    public static String getFirstNameQuery(int userId) {
        String firstName = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT firstName FROM user WHERE userId = ?");

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                firstName = rs.getString("firstName");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return firstName;
    }

    public static void setFirstNameQuery(int userId, String firstName) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE user SET firstName = ? WHERE userId = ?");

            pstmt.setString(1, firstName);
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static String getLastNameQuery(int userId) {
        String lastName = null;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT lastName FROM user WHERE userId = ?");

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                lastName = rs.getString("lastName");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return lastName;
    }


    public static void setLastNameQuery(int userId, String lastName) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE user SET lastName = ? WHERE userId = ?");

            pstmt.setString(1, lastName);
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static boolean getTypeQuery(int userId) {
        boolean type = false;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT userType FROM user WHERE userId = ?");

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                type = rs.getInt("userType") == 1;
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return type;
    }

    public static void setTypeQuery(int userId, boolean type) {
        int userType = type ? 1 : 0;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE user SET userType = ? WHERE userId = ?");

            pstmt.setInt(1, userType);
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

    public static int getGroupQuery(int userId) {
        int groupId = 0;
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT groupId FROM user WHERE userId = ?");

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                groupId = rs.getInt("groupId");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem querying the database.", e);
        } finally {
            closeConnection();
        }

        return groupId;
    }

    public static void setGroupQuery(int userId, int group) {
        try {
            setConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE user SET groupId = ? WHERE userId = ?");

            pstmt.setInt(1, group);
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Problem updating the database.", e);
        } finally {
            closeConnection();
        }
    }

}
