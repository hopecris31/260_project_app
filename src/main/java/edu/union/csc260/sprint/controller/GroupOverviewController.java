package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.Group;
import edu.union.csc260.sprint.model.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import java.util.Optional;

/**
 * GroupOverviewController Class
 *
 * This class is responsible for handling the user interface logic for the group overview screen.
 *
 * Invariants:
 * - groupNameLabel, groupNumberLabel, groupsTable, groupColumn, groupMemberTable, groupMemberTableColumn, showStudentBacklogButton should not be null after the FXML file has been loaded.
 * - userOverviewController should not be null and should be set by calling the setMainApp method.
 * - selectedGroup should not be null and should be set by calling the setSelectedGroup method.
 * - mainApp should not be null and should be set by calling the setMainApp method.
 * - okClicked should be false initially and should be set to true when the user performs an action that indicates OK.
 *
 * @author Claudia Porto
 *
 */
public class GroupOverviewController {

    @FXML
    private Label groupNameLabel;
    @FXML
    private Label groupNumberLabel;

    @FXML
    private TableView<Group> groupsTable;
    @FXML
    private TableColumn<Group, String> groupColumn;
    @FXML
    private TableView<User> groupMemberTable;
    @FXML
    private TableColumn<User, String> groupMemberTableColumn;

    @FXML
    private Button showStudentBacklogButton;


    private UserOverviewController userOverviewController;
    private Group selectedGroup;
    private BacklogItemsApp mainApp;

    private boolean okClicked = false;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public GroupOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        groupColumn.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        groupMemberTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));

        groupsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    setSelectedGroup(newValue);
                    showGroupDetails(newValue);
                }
        );

        groupMemberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        groupMemberTableColumn.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Sets the main application reference.
     *
     * @param mainApp The main application instance.
     */
    public void setMainApp(BacklogItemsApp mainApp) {
        this.mainApp = mainApp;

        groupsTable.setItems(mainApp.getGroupData());

        userOverviewController = new UserOverviewController();
        userOverviewController.setMainApp(mainApp);
    }

    /**
     * Fills all text fields to show details about the group.
     * If the specified group is null, all text fields are cleared.
     *
     * @param group the group or null
     */
    private void showGroupDetails(Group group) {
        if (group != null) {
            groupNumberLabel.setText(Integer.toString(group.getGroupNumber()));
            groupNameLabel.setText(group.getGroupName());
        } else {
            groupNumberLabel.setText("");
            groupNameLabel.setText("");
        }
    }

    /**
     * Sets the selected group and updates the group member table accordingly.
     *
     * @param group The selected group.
     */
    public void setSelectedGroup(Group group) {
        selectedGroup = group;

        groupMemberTable.setItems(selectedGroup.getGroupMembers());

        groupsTable.refresh();
        groupMemberTable.refresh();
    }

    /**
     * Creates an empty group
     */
    @FXML
    private void handleNewGroup() {
        Group tempGroup = new Group();
        boolean okClicked = mainApp.showGroupEditDialog(tempGroup);
        if (okClicked) {
            mainApp.getGroupData().add(tempGroup);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected group.
     */
    @FXML
    private void handleEditGroup() {
        Group selectedGroup = groupsTable.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            boolean okClicked = mainApp.showGroupEditDialog(selectedGroup);
            if (okClicked) {
                showGroupDetails(selectedGroup);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Group Selected");
            alert.setContentText("Please select a Group in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks on the delete button. Deletes a group.
     */
    @FXML
    private void handleDeleteGroup() {
        int selectedIndex = groupsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Group selectedGroup = groupsTable.getItems().get(selectedIndex);

            groupsTable.getItems().remove(selectedIndex);

            mainApp.getBacklogData().remove(selectedGroup);

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Group Selected");
            alert.setContentText("Please select a Group in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the add student button. Opens a dialog to select
     * a student to add to the selected group.
     */
    @FXML
    private void handleAddStudent() {
        ChoiceBox<User> userChoiceBox = new ChoiceBox<>();

        ObservableList<User> availableStudents = FXCollections.observableArrayList();
        ObservableList<User> allStudents = mainApp.getUserData();

        for (User student : allStudents) {
            if (student.getGroup() == 0) {
                availableStudents.add(student);
            }
        }

        userChoiceBox.setItems(availableStudents);

        userChoiceBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user != null) {
                    return user.getFirstName() + " " + user.getLastName();
                }
                return null;
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Select Student");
        dialog.setHeaderText("Select a Student to use for the new Group:");

        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        VBox content = new VBox();
        content.getChildren().addAll(userChoiceBox);

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return userChoiceBox.getValue();
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        if (result.isPresent()) {
            User selectedStudent = result.get();

            selectedGroup.getGroupMembers().add(selectedStudent);
            selectedStudent.setGroup(selectedGroup.getGroupNumber());

            if (okClicked) {
                this.mainApp.getGroupData().add(selectedGroup);
            }
        }
    }

    /**
     * Called when the user clicks the remove student button. Removes the selected
     * student from the selected group.
     */
    @FXML
    private void handleRemoveStudent() {
        User selectedStudent = groupMemberTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            selectedGroup.getGroupMembers().remove(selectedStudent);
            selectedStudent.setGroup(0);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student in the table.");
            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the show student backlog button. Displays the project
     * backlog for the selected student.
     */
    @FXML
    private void handleShowStudentBacklog() {
        User selectedStudent = groupMemberTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            mainApp.viewRootLayout();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student in the table.");
            alert.showAndWait();
        }
    }
}
