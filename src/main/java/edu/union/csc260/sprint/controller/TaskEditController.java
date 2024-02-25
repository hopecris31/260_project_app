package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller class for a Sprint Edit.
 * This class is responsible for handling the user interface logic for editing a task.
 *
 * Invariants:
 * - nameField and taskField should not be null after the FXML file has been loaded.
 * - DialogStage should not be null and should be set by calling the setDialogStage method.
 * - Task should not be null and should be set by calling the setTask method.
 * - OkClicked should be false initially and should be set to true when the user clicks the OK button.
 * - The isInputValid method should return true if the input in the nameField and taskField is valid, and false if there are any validation errors.
 * - If the input validation fails, an error message should be displayed in an Alert dialog.
 *
 * @author Claudia Porto
 */
public class TaskEditController {
    @FXML
    private CheckBox completeBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField taskField;
    @FXML
    private TextArea commentsArea;

    private Stage taskDialogStage;
    private Task task;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
    * Sets the dialog stage of this task.
    *
    * @param taskDialogStage the dialog stage
    */
    public void setTaskStage(Stage taskDialogStage) {
       this.taskDialogStage = taskDialogStage;
    }

    /**
     * Sets the task to be edited in the task dialog stage.
     * 
     * @param task the task to be edited
     */
    public void setTask(Task task){
       this.task = task;

       completeBox.setSelected(task.isCompleted());
       completeBox.setDisable(true);
       nameField.setText(task.getName());
       taskField.setText(task.getTask());
       commentsArea.setText(task.getComments());
    }

    /**
    * Returns true if the user clicked Save, false otherwise.
    *
    * @return true iff OK button is clicked
    */
    public boolean isOkClicked(){
       return okClicked;
    }


    /**
    * Called when the user clicks the save button.
    */
    @FXML
    private void handleSaveButton() {
       if (isInputValid()) {
           task.setCompleted(completeBox.isSelected()); 
           task.setName(nameField.getText());
           task.setTask(taskField.getText());
           task.setComments(commentsArea.getText());
           okClicked = true;
           taskDialogStage.close();
       }
    }


    /**
    * Called when the user clicks cancel button.
    */
    @FXML
    private void handleCancel() {
        taskDialogStage.close();
    }


    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid task name!\n";
        }
        if (taskField.getText() == null || taskField.getText().length() == 0) {
            errorMessage += "No valid task goal!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(taskDialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();           
            return false;
        }
    }
}
