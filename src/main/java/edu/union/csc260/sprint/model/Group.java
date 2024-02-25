package edu.union.csc260.sprint.model;

import java.util.List;

import edu.union.csc260.sprint.SQLDatabase;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Group.
 *
 * Invariants:
 * - The `groupNumber` property represents the number of the group.
 * - The `groupName` property represents the name of the group.
 * - The `groupMembers` list contains the members of the group.
 * - The `groupID` represents the ID of the group.
 *
 * Author: Yuxing Liu and Claudia Porto
 */
public class Group {
   private final IntegerProperty groupNumber;
   private final StringProperty groupName;
   private ObservableList<User> groupMembers;

   private int groupId;

    /**
    * Default constructor.
    */
   public Group(){
       this(null,null);
   }

   /**
    * Constructor with some initial data!
    *
    * @param groupName    the name of the group
    * @param groupMembers the list of group members
    */
   public Group(String groupName, List<User> groupMembers){
       this.groupNumber = new SimpleIntegerProperty(0);
       this.groupName = new SimpleStringProperty(groupName);
       this.groupMembers = groupMembers != null ? FXCollections.observableArrayList(groupMembers) : FXCollections.observableArrayList();
   }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * Gets the group number.
     *
     * @return the group number
     */
    public int getGroupNumber(){
        return SQLDatabase.getGroupNumberQuery(groupId);
    }

    /**
     * Sets the group number.
     *
     * @param groupNumber the group number
     */
    public void setGroupNumber(int groupNumber){
       SQLDatabase.setGroupNumberQuery(groupId, groupNumber);
    }

    /**
     * Gets the group name.
     *
     * @return the group name
     */
   public String getGroupName(){
       return SQLDatabase.getGroupNameQuery(groupId);
   }

    /**
     * Gets the group name property.
     *
     * @return the group name property
     */
    public StringProperty groupNameProperty(){
        return SQLDatabase.getGroupNamePropertyQuery(groupId);
    }

    /**
     * Sets the group name.
     *
     * @param groupName the group name
     */
   public void setGroupName(String groupName){
       SQLDatabase.setGroupNameQuery(groupId, groupName);
   }

    /**
     * Gets the group members.
     *
     * @return the group members
     */
   public ObservableList<User> getGroupMembers(){
       return SQLDatabase.getGroupMembersQuery(groupId);
   }
}
