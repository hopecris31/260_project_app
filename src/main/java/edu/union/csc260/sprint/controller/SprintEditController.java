package edu.union.csc260.sprint.controller;

import java.time.LocalDate;

import edu.union.csc260.sprint.model.Sprint;
import edu.union.csc260.sprint.model.Task;

import edu.union.csc260.sprint.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller class for a Sprint Edit.
 * This class is responsible for handling the user interface logic for editing a sprint.
 *
 * Invariants:
 * - TitleField, GoalField, and EstFinishDatePicker should not be null after the FXML file has been loaded.
 * - DialogStage should not be null and should be set by calling the setDialogStage method.
 * - Sprint should not be null and should be set by calling the setSprint method.
 * - OkClicked should be false initially and should be set to true when the user clicks the OK button.
 * - The isInputValid method should return true if the input in the titleField, goalField, and estFinishDate is valid, and false if there are any validation errors.
 * - If the input validation fails, an error message should be displayed in an Alert dialog.
 *
 * @author Claudia Porto
 */
public class SprintEditController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField goalField;
    @FXML
    private DatePicker estFinishDatePicker;
    @FXML
    private TextArea reflectionArea;
    @FXML
    private ListView<Task> tasksListView;

    private Stage dialogStage;
    private Sprint sprint;
    private boolean okClicked = false;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        tasksListView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (task == null || empty) {
                    setStyle("");
                } else {
                    setText(task.getName());
                }
            }
        });
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the sprint to be edited in the Sprint.
     *
     * @param sprint the sprint to be edited
     */
    public void setSprint(Sprint sprint) {
        this.sprint = sprint;

        titleField.setText(sprint.getTitle());
        goalField.setText(sprint.getGoal());

        estFinishDatePicker.setValue(sprint.getEstFinishDate());
        estFinishDatePicker.setPromptText("dd.mm.yyyy");

        if(sprint.isCompleted()) {
            reflectionArea.setText(sprint.getReflection().getText());
        }

        tasksListView.getItems().clear();
        tasksListView.getItems().addAll(sprint.getTasks());

        tasksListView.getSelectionModel().selectAll();

        sprint.getTasks().addAll(tasksListView.getSelectionModel().getSelectedItems());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return true if the user clicked OK, false otherwise
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks the OK button.
     * It validates the input and updates the sprint with the edited details.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            sprint.setTitle(titleField.getText());
            sprint.setGoal(goalField.getText());
            LocalDate localDate = estFinishDatePicker.getValue();
            sprint.setEstFinishDate(localDate);

            if (sprint.isCompleted()) {
                sprint.setReflection(reflectionArea.getText());
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks the cancel button.
     * It closes the dialog stage without saving any changes.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the input fields.
     * It checks if the estimated finish date is a valid date.
     * If the input is invalid, it displays an error alert with the corresponding error message.
     *
     * @return true if the input is valid, false otherwise
     */
    private boolean isInputValid() {
        String errorMessage = "";

        LocalDate estFinishDate = estFinishDatePicker.getValue();
        if (estFinishDate == null) {
            errorMessage += ("No valid finish date!\n");
        } else if (!DateUtil.validDate(DateUtil.format(estFinishDate))) {
            errorMessage += ("No valid finish date. Use the format dd.mm.yyyy!\n");
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showErrorAlert("Please correct invalid fields:", errorMessage);
            return false;
        }
    }


    /**
     * Shows an error alert with the given header text and content text.
     *
     * @param headerText the header text for the error message
     * @param contentText the content text for the error message
     */
    public static void showErrorAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
