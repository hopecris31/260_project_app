package edu.union.csc260.sprint.model;

import java.time.LocalDate;

import edu.union.csc260.sprint.SQLDatabase;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

/**
 * Model class for a Sprint.
 *
 * Invariants:
 * - The `title` property represents the title of the sprint.
 * - The `goal` property represents the goal of the sprint.
 * - The `estFinishDate` property represents the estimated finish date of the sprint.
 * - The `tasks` list contains the tasks associated with the sprint.
 * - The `reflection` represents the reflection text area for the sprint.
 * - The `completed` property indicates whether the sprint is completed or not.
 * - The `completedTasksCount` property represents the number of completed tasks in the sprint.
 * - The `currentSprint` property indicates whether the sprint is the current sprint or not.
 * - The `progress` property represents the progress of the sprint.
 * - The `comments` list contains the comments associated with the sprint.
 * - The `projectBacklog` represents the project backlog associated with the sprint.
 *
 *
 * Author: Claudia Porto
 */
public class Sprint {
	private final StringProperty title;
	private final StringProperty goal;
	private final ObjectProperty<LocalDate> estFinishDate;
	private final ObservableList<Task> tasks;
	private final TextArea reflection;
	private final BooleanProperty completed;
	private final IntegerProperty completedTasksCount;
	private final BooleanProperty currentSprint;
	private final DoubleProperty progress;
	private final ObservableList<String> comments;
	
	private ProjectBacklog projectBacklog;
	private int sprintId;

	/**
	 * Default constructor.
	 */
	public Sprint() {
		this(null);
	}

	/**
	 * Constructor with some initial data.
	 *
	 * @param backlog the project backlog associated with the sprint
	 */
	public Sprint(ProjectBacklog backlog) {
		this.projectBacklog = backlog;

		this.title = new SimpleStringProperty(backlog.getTitle());
		this.goal = new SimpleStringProperty(backlog.getBacklogGoal());
		this.tasks = FXCollections.observableArrayList(backlog.getTasks());
		this.reflection = new TextArea();
		this.reflection.setText("");
		this.estFinishDate = new SimpleObjectProperty<>(LocalDate.now().plusWeeks(1));
		this.completed = new SimpleBooleanProperty(false);
		this.completedTasksCount = new SimpleIntegerProperty(0);
		this.currentSprint = new SimpleBooleanProperty(false);
		this.progress = new SimpleDoubleProperty(0.0);
		this.comments = FXCollections.observableArrayList();
	}

	/**
	 * Gets the title of the sprint.
	 *
	 * @return the title of the sprint
	 */
	public String getTitle() {
		return SQLDatabase.getTitleQuery(sprintId);
	}

	/**
	 * Sets the title of the sprint.
	 *
	 * @param title the title of the sprint
	 */
	public void setTitle(String title) {
		SQLDatabase.setTitleQuery(title, sprintId);
	}

	/**
	 * Gets the title property of the sprint.
	 *
	 * @return the title property of the sprint
	 */
	public StringProperty titleProperty() {
		return SQLDatabase.titlePropertyQuery(sprintId);
	}

	/**
	 * Gets the goal of the sprint.
	 *
	 * @return the goal of the sprint
	 */
	public String getGoal() {
		return SQLDatabase.getGoalQuery(sprintId);
	}

	/**
	 * Sets the goal of the sprint.
	 *
	 * @param goal the goal of the sprint
	 */
	public void setGoal(String goal) {
		SQLDatabase.setGoalQuery(goal, sprintId);
	}

	/**
	 * Gets the estimated finish date of the sprint.
	 *
	 * @return the estimated finish date of the sprint
	 */
	public LocalDate getEstFinishDate() {
		return SQLDatabase.getEstFinishDateQuery(sprintId);
	}

	/**
	 * Sets the estimated finish date of the sprint.
	 *
	 * @param estFinishDate the estimated finish date of the sprint
	 */
	public void setEstFinishDate(LocalDate estFinishDate) {
		SQLDatabase.setEstFinishDateQuery(estFinishDate, sprintId);
	}

	/**
	 * Gets the tasks associated with the sprint.
	 *
	 * @return the tasks associated with the sprint
	 */
	public ObservableList<Task> getTasks() {
		return tasks;
	}

	/**
	 * Gets the reflection text area for the sprint.
	 *
	 * @return the reflection text area for the sprint
	 */
	public TextArea getReflection() {
		return SQLDatabase.getReflectionQuery(sprintId);
	}

	/**
	 * Sets the reflection for the sprint.
	 *
	 * @param reflection the reflection text to set
	 * @throws IllegalStateException if the sprint is incomplete
	 */
	public void setReflection(String reflection) {
		if (isCompleted()) {
			SQLDatabase.setReflection(reflection, sprintId);
		} else {
			throw new IllegalStateException("Cannot set reflection for an incomplete sprint.");
		}
	}

	/**
	 * Checks if the sprint is completed.
	 *
	 * @return true if the sprint is completed, false otherwise
	 */
	public boolean isCompleted() {
		return SQLDatabase.isSprintCompletedQuery(sprintId);
	}

	/**
	 * Sets the completion status of the sprint.
	 *
	 * @param value the completion status to set
	 */
	public void setCompleted(boolean value) {
		SQLDatabase.setCompletedQuery(value, sprintId);
	}

	/**
	 * Checks if the sprint is the current sprint.
	 *
	 * @return true if the sprint is the current sprint, false otherwise
	 */
	public boolean isCurrentSprint() {
		return SQLDatabase.isCurrentSprintQuery(sprintId);
	}

	/**
	 * Sets the current sprint status of the sprint.
	 *
	 * @param currentSprint the current sprint status to set
	 */
	public void setCurrentSprint(boolean currentSprint) {
		SQLDatabase.setCurrentSprintQuery(sprintId, currentSprint);
	}

	/**
	 * Gets the progress of the sprint.
	 *
	 * @return the progress of the sprint
	 */
	public double getProgress() {
		return SQLDatabase.getProgressQuery(sprintId);
	}

	/**
	 * Sets the progress of the sprint.
	 *
	 * @param progress the progress of the sprint
	 */
	public void setProgress(double progress) {
		int totalTasks = tasks.size();
		int completedTasks = (int) tasks.stream().filter(Task::isCompleted).count();

		if (totalTasks > 0) {
			progress = (double) completedTasks / totalTasks;
			SQLDatabase.setProgressQuery(sprintId, progress);

			if (progress == 1.0) {
				setCompleted(true);
			}
		} else {
			this.progress.set(0.0);
			setCompleted(false);
		}
	}

	/**
	 * Adds a comment to the sprint.
	 *
	 * @param comment the comment to add
	 */
	public void addComment(String comment) {
		comments.add(comment);
	}
}
