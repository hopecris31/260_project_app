package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.Group;
import edu.union.csc260.sprint.model.Sprint;
import edu.union.csc260.sprint.model.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for the User Overview.
 * This class controls the interaction between the user interface and the underlying data model for the User Overview.
 * It handles displaying and editing sprint details.
 * The class is designed to work with the corresponding FXML file (UserOverview.FXML) that defines the user interface layout.
 *
 * Invariants:
 * - The `mainApp` variable should be set to a valid `BacklogItemsApp` instance before calling any other methods.
 * - The `studentTable` should be populated with valid `User` objects from the `mainApp`'s user data.
 *
 * @author Claudia Porto
 */
public class UserOverviewController {
    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;
    @FXML
    private Label groupLabel;

    @FXML
    private TableView<User> studentTable;
    @FXML
    private TableColumn<User, String> studentColumn;

    private User selectedStudent;

    private BacklogItemsApp mainApp;

    private boolean okClicked = false;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public UserOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        studentColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            String fullName = user.getFirstName() + " " + user.getLastName();
            return new SimpleStringProperty(fullName);
        });

        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    setSelectedStudent(newValue);
                    showUserDetails(newValue);
                }
        );
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(BacklogItemsApp mainApp) {
        this.mainApp = mainApp;

        studentTable.setItems(mainApp.getUserData());
    }

    /**
     * Fills all text fields to show details about the Student.
     * If the specified sprint is null, all text fields are cleared.
     *
     * @param student the person or null
     */
    private void showUserDetails(User student) {
        if (student != null) {
            firstNameLabel.setText(student.getFirstName());
            lastNameLabel.setText(student.getLastName());
            groupLabel.setText(String.valueOf(student.getGroup()));
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            groupLabel.setText("");
        }
    }

    /**
     * Sets the selected user in the controller and refreshes the associated student table.
     *
     * @param user The selected user.
     */
    private void setSelectedStudent(User user) {
        this.selectedStudent = user;

        this.studentTable.refresh();
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to add
     * details for a new student.
     */
    @FXML
    public void handleNewStudent() {
        User tempStudent = new User();
        boolean okClicked = mainApp.showUserEditDialog(tempStudent);
        if (okClicked) {
            mainApp.getUserData().add(tempStudent);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected student.
     */
    @FXML
    public void handleEditStudent() {
        User selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = mainApp.showUserEditDialog(selectedStudent);
            if (okClicked) {
                showUserDetails(selectedStudent);
            }
        } else {
            showNoSelectionAlert();
        }
    }

    /**
     * Called when the user clicks on the delete button. Deletes a student.
     */
    @FXML
    public void handleDeleteStudent() {
        User selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
            studentTable.getItems().remove(selectedIndex);
            mainApp.getUserData().remove(selectedStudent);
        } else {
            showNoSelectionAlert();
        }
    }

    /**
     * Shows an alert when no student is selected.
     */
    private void showNoSelectionAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Student Selected");
        alert.setContentText("Please select a Student in the table.");
        alert.showAndWait();
    }
}
