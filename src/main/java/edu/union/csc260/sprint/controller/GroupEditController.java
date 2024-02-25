package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *  Group Edit Controller Class
 *  This class is responsible for handling the user interface logic for editing a group.
 *
 *  Invariants:
 *  - groupNumberField and groupNameField should not be null after the FXML file has been loaded.
 *  - dialogStage should not be null and should be set by calling the setDialogStage method.
 *  - group should not be null and should be set by calling the setGroup method.
 *  - okClicked should be false initially and should be set to true when the user clicks the OK button.
 *  - The isInputValid method should return true if the input in the groupNumberField and groupNameField is valid, and false if there are any validation errors.
 *  - If the input validation fails, an error message should be displayed in an Alert dialog.
 *
 *  @author Claudia Porto
 *
 */
public class GroupEditController {
    @FXML
    private TextField groupNumberField;
    @FXML
    private TextField groupNameField;
    @FXML
    private ListView<User> groupMemberListView;

    private Stage dialogStage;
    private Group group;

    private boolean okClicked = false;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        groupMemberListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                    setStyle("");
                    setText(null);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName());
                }
            }
        });
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage The stage instance.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the group to be edited.
     *
     * @param group The group to be edited.
     */
    public void setGroup(Group group) {
        this.group = group;

        groupNumberField.setText(Integer.toString(group.getGroupNumber()));
        groupNameField.setText(group.getGroupName());

        groupMemberListView.getItems().clear();
        groupMemberListView.getItems().addAll(group.getGroupMembers());

        groupMemberListView.getSelectionModel().selectAll();

        group.getGroupMembers().addAll(groupMemberListView.getSelectionModel().getSelectedItems());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return True if OK is clicked, false otherwise.
     */
    public boolean isOkClicked() {
        return okClicked;
    }


    /**
     * Called when the user clicks the OK button.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            group.setGroupNumber(Integer.parseInt(groupNumberField.getText()));
            group.setGroupName(groupNameField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks the Cancel button.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid, false otherwise
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (groupNumberField.getText() == null || groupNumberField.getText().isEmpty()) {
            errorMessage += "No valid Group Number!\n";
        } else {
            try {
                Integer.parseInt(groupNumberField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid Group Number! Must be a valid integer.\n";
            }
        }

        if (groupNameField.getText() == null || groupNameField.getText().length() == 0) {
            errorMessage += "No valid Group Name!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
