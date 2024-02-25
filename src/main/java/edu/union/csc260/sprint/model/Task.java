package edu.union.csc260.sprint.model;

import edu.union.csc260.sprint.SQLDatabase;
import javafx.beans.property.*;

/**
 * Model class for a Task.
 *
 * Invariants:
 * - The `name` property represents the name of the task.
 * - The `task` property represents the description of the task.
 * - The `comments` property represents the comments associated with the task.
 * - The `completed` property indicates whether the task is completed or not.
 *
 * @author Claudia Porto
 */
public class Task {

  	private final StringProperty name;
  	private final StringProperty task;
  	private final StringProperty comments;
    private final BooleanProperty completed;
	private int taskId;

  	/**
  	 * Default constructor.
  	 */
  	public Task() {
		  this(null, null, null);
	}

	/**
	 * Constructor with some initial data.
	 *
	 * @param name     the name of the task
	 * @param task     the task description
	 * @param comments the comments associated with the task
	 */
  	public Task(String name, String task, String comments) {
    	this.name = new SimpleStringProperty(name);
    	this.task = new SimpleStringProperty(task);
		this.comments = new SimpleStringProperty(comments);
        this.completed = new SimpleBooleanProperty(false);
  	}

	/**
	 * Gets the name of the task.
	 *
	 * @return the name of the task
	 */
  	public String getName() {
		  return SQLDatabase.getNameQuery(taskId);
  	}

	/**
	 * Sets the name of the task.
	 *
	 * @param name the name of the task
	 */
  	public void setName(String name) {
		  SQLDatabase.setNameQuery(taskId, name);
  	}

	/**
	 * Gets the task description.
	 *
	 * @return the task description
	 */
  	public String getTask() {
		  return SQLDatabase.getTaskQuery(taskId);
  	}

	/**
	 * Sets the task description.
	 *
	 * @param task the task description
	 */
  	public void setTask(String task) {
    	SQLDatabase.setTaskQuery(taskId, task);
  	}

	/**
	 * Gets the comments associated with the task.
	 *
	 * @return the comments associated with the task
	 */
  	public String getComments() {
		  return SQLDatabase.getCommentsQuery(taskId);
  	}

	/**
	 * Sets the comments associated with the task.
	 *
	 * @param comments the comments to set
	 */
  	public void setComments(String comments) {
    	SQLDatabase.setCommentsQuery(taskId, comments);
  	}

	/**
	 * Checks if the task is completed.
	 *
	 * @return true if the task is completed, false otherwise
	 */
    public boolean isCompleted() {
        return SQLDatabase.isTaskCompletedQuery(taskId);
    }

	/**
	 * Sets the completion status of the task.
	 *
	 * @param completed the completion status to set
	 */
    public void setCompleted(boolean completed) {
        SQLDatabase.setTaskCompletedQuery(taskId, completed);
    }
}
