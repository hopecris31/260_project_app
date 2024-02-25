package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for a User Edit.
 * This class is responsible for handling the user interface logic for editing a user.
 *
 * Invariants:
 * - firstNameField and lastNameField should not be null after the FXML file has been loaded.
 * - DialogStage should not be null and should be set by calling the setDialogStage method.
 * - User should not be null and should be set by calling the setUser method.
 * - OkClicked should be false initially and should be set to true when the user clicks the OK button.
 * - The isInputValid method should return true if the input in the firstNameField and lastNameField is valid, and false if there are any validation errors.
 * - If the input validation fails, an error message should be displayed in an Alert dialog.
 *
 * @author Claudia Porto
 */
public class UserEditController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;


    private Stage dialogStage;
    private User user;

    private boolean okClicked = false;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage the dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the user to be edited in the User.
     *
     * @param user the user to be edited
     */
    public void setUser(User user) {
        this.user = user;

        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return true if okay button is clicked
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
            user.setFirstName(firstNameField.getText());
            user.setLastName(lastNameField.getText());

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
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "Enter a valid First Name!\n";
        }

        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "Enter a valid Last Name!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
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
