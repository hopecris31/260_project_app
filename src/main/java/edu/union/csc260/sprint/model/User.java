package edu.union.csc260.sprint.model;

import javafx.beans.property.*;
import edu.union.csc260.sprint.SQLDatabase;

/**
 * Model class for a User.
 *
 * Invariants:
 * - The `firstName` property represents the first name of the user.
 * - The `lastName` property represents the last name of the user.
 * - The `type` property represents the type of the user.
 * - The `group` property represents the group of the user.
 * - The `sqlDatabase` instance is used to interact with the SQL database.
 *
 * @author Claudia Porto
 */
public class User {

	private final StringProperty firstName;
	private final StringProperty lastName;
	private final BooleanProperty type;
	private final IntegerProperty group;
	private final int userId;

	/**
	 * Default constructor.
	 */
	public User() {
		this(null, null, true);
	}

	/**
	 * Constructor with some initial data.
	 *
	 * @param firstName The first name of the user.
	 * @param lastName The last name of the user.
	 * @param type The type of the user.
	 */
	public User(String firstName, String lastName, boolean type) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.type = new SimpleBooleanProperty(type);
		this.group = new SimpleIntegerProperty(0);

		int typeInt = (type) ? 0 : 1;
		this.userId = SQLDatabase.addUserQuery(firstName, lastName, typeInt, this.group.get());
	}

	/**
	 * Retrieves the user ID based on the first name, last name, and type.
	 *
	 * @param firstName The first name of the user.
	 * @param lastName The last name of the user.
	 * @param type The type of the user.
	 * @return The user ID associated with the user's information.
	 */
	public int getUserId (StringProperty firstName, StringProperty lastName, int type) {
		return SQLDatabase.getUserIdQuery(firstName, lastName, type);
	}
	/**
	 * Retrieves the user ID
	 */
	public int getUserId() {
		return this.userId;
	}

	/**
	 * Gets the first name of the user.
	 *
	 * @return The first name of the user.
	 */
	public String getFirstName() {
		return SQLDatabase.getFirstNameQuery(this.userId);
	}

	/**
	 * Sets the first name of the user.
	 *
	 * @param firstName The first name to set for the user.
	 */
	public void setFirstName(String firstName) {
		SQLDatabase.setFirstNameQuery(this.userId, firstName);
	}

	/**
	 * Gets the last name of the user.
	 *
	 * @return The last name of the user.
	 */
	public String getLastName() {

		return SQLDatabase.getLastNameQuery(this.userId);
	}

	/**
	 * Sets the last name of the user.
	 *
	 * @param lastName The last name to set for the user.
	 */
	public void setLastName(String lastName) {
		SQLDatabase.setLastNameQuery(this.userId, lastName);
	}

	/**
	 * Gets the type of the user.
	 */
  	public boolean getType() {
		return SQLDatabase.getTypeQuery(userId);
	}

	/**
	 * Sets the type of the user.
	 *
	 * @param type The type to set for the user.
	 */
	public void setType(boolean type) {
		SQLDatabase.setTypeQuery(userId, type);
	}

	/**
	 * Gets the group of the user.
	 *
	 * @return The group of the user.
	 */
	public int getGroup(){
		return SQLDatabase.getGroupQuery(userId);
	}

	/**
	 * Sets the group of the user.
	 *
	 * @param group The group to set for the user.
	 */
	public void setGroup(int group){
		SQLDatabase.setGroupQuery(userId, group);
	}
}
