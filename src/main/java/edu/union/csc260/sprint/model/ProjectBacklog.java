package edu.union.csc260.sprint.model;

import java.util.List;

import edu.union.csc260.sprint.SQLDatabase;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Project Backlog.
 *
 * Invariants:
 * - The `title` property represents the title of the project backlog.
 * - The `backlogGoal` property represents the goal of the project backlog.
 * - The `tasks` list contains the tasks associated with the project backlog.
 * - The `completed` property indicates whether the project backlog is completed or not.
 *
 * @author Claudia Porto, Hope Crisafi
 */
 public class ProjectBacklog {
    private StringProperty title;
    private StringProperty backlogGoal;
    private ObservableList<Task> tasks;
    private BooleanProperty completed;

    private int backlogItemId;

    /**
     * Default constructor.
     */
    public ProjectBacklog() {
        this(null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param title       the title of the project backlog
     * @param backlogGoal the goal of the project backlog
     * @param tasks       the list of tasks associated with the project backlog
     */
    public ProjectBacklog(String title, String backlogGoal, List<Task> tasks) {
        this.title = new SimpleStringProperty(title);
        this.backlogGoal = new SimpleStringProperty(backlogGoal);
        this.tasks = tasks != null ? FXCollections.observableArrayList(tasks) : FXCollections.observableArrayList();
        this.completed = new SimpleBooleanProperty(false);
    }

    /**
     * Gets the title of the project backlog.
     *
     * @return the title of the project backlog
     */
    public String getTitle() {
        return SQLDatabase.getBacklogItemTitle(backlogItemId);
    }

    /**
     * Sets the title of the project backlog.
     *
     * @param title the title of the project backlog
     */
    public void setTitle(String title) {
        SQLDatabase.setBacklogItemTitle(backlogItemId, title);
    }

    /**
     * Gets the title property of the project backlog.
     *
     * @return the title property of the project backlog
     */
    public StringProperty titleProperty() {
        return SQLDatabase.backlogItemTitlePropertyQuery(backlogItemId);
    }

    /**
     * Gets the goal of the project backlog.
     *
     * @return the goal of the project backlog
     */
    public String getBacklogGoal() {
        return SQLDatabase.getBacklogItemGoal(backlogItemId);
    }

    /**
     * Sets the goal of the project backlog.
     *
     * @param backlogGoal the goal of the project backlog
     */
    public void setBacklogGoal(String backlogGoal) {
        SQLDatabase.setBacklogItemGoal(backlogItemId, backlogGoal);
    }

    /**
     * Gets the tasks associated with the project backlog.
     *
     * @return the tasks associated with the project backlog
     */
    public ObservableList<Task> getTasks() {
        return SQLDatabase.getTasksQuery(backlogItemId);
    }

    /**
     * Adds a task to the project backlog.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        SQLDatabase.addTask(backlogItemId, task);
    }
}
