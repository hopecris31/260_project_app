package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.ProjectBacklog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Backlog Edit Controller Class
 *
 * This class is responsible for handling the user interface logic for editing a project backlog.
 *
 * Invariants:
 * - TitleField and GoalField should not be null after the FXML file has been loaded.
 * - DialogStage should not be null and should be set by calling the setDialogStage method.
 * - ProjectBacklog should not be null and should be set by calling the setProjectBacklog method.
 * - OkClicked should be false initially and should be set to true when the user clicks the OK button.
 * - The isInputValid method should return true if the input in the titleField and goalField is valid, and false if there are any validation errors.
 * - If the input validation fails, an error message should be displayed in an Alert dialog.
 *
 * @author Hope Crisafi
 */

public class BacklogEditController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField goalField;

    private ProjectBacklog projectBacklog;
    private Stage dialogStage;

    private boolean okClicked = false;


    /**
     * Initializes the backlog edit controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage The stage of the dialog.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the backlog item to be edited.
     *
     * @param projectBacklog The backlog item to be edited.
     */
    public void setProjectBacklog(ProjectBacklog projectBacklog) {
        this.projectBacklog = projectBacklog;

        titleField.setText(projectBacklog.getTitle());
        goalField.setText(projectBacklog.getBacklogGoal());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return true iff user clicks OK
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            projectBacklog.setTitle(titleField.getText());
            projectBacklog.setBacklogGoal(goalField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true iff the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }

        if (goalField.getText() == null || goalField.getText().length() == 0) {
            errorMessage += "Enter a goal! \n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}