package edu.union.csc260.sprint.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.*;
import javafx.scene.layout.GridPane;

/**
 * Controller class for the Task Overview.
 * This class controls the interaction between the user interface and the underlying data model for the Task Overview.
 * It handles displaying and editing task details. The class is designed to work with the corresponding FXML file
 * (TaskOverview.FXML) that defines the user interface layout.
 *
 * Invariants:
 * - The `mainApp` variable should be set to a valid `BacklogItemsApp` instance before calling any other methods.
 * - The `taskList` should be populated with valid `Task` objects from the `mainApp`'s task data.
 *
 * @author Claudia Porto
 *
 */
public class TaskOverviewController {
    @FXML
    private ListView<Task> taskList;

    @FXML
    private Label nameLabel;
    @FXML
    private Label taskLabel;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private CheckBox completed;

    private Task selectedTask;
    private BacklogItemsApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TaskOverviewController(){
        taskList = new ListView<>();
    }

     /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
     @FXML
     private void initialize() {
         taskList.getSelectionModel().selectedItemProperty().addListener(
                 (observable, oldValue, newValue) -> {
                     setSelectedTask(newValue);
                     showTaskDetails(newValue);
                 });

         showTaskDetails(null);
     }

    /**
     * Sets the reference to the main application.
     *
     * @param mainApp the reference to the main application
     */
    public void setMainApp(BacklogItemsApp mainApp) {
        this.mainApp = mainApp;

        taskList.setItems(mainApp.getTaskData());
    }

    /**
     * Sets the selected task in the task list.
     *
     * @param task the selected task
     */
    public void setSelectedTask(Task task) {
        this.selectedTask = task;

        this.taskList.refresh();
    }

    /**
     * Displays the details of the specified task.
     * If the task is null, clears all text fields.
     *
     * @param task the task to display details for, or null
     */
    public void showTaskDetails(Task task) {
        selectedTask = task;
        if (selectedTask != null) {
            nameLabel.setText(task.getName());
            taskLabel.setText(task.getTask());
            commentsTextArea.setText(task.getComments());
            completed.setSelected(task.isCompleted());
        } else {
            nameLabel.setText("");
            taskLabel.setText("");
            commentsTextArea.setText("");
            completed.setSelected(false);
        }
    }


    /**
     * Called when the user clicks the "New" button.
     * Opens a dialog to create a new task.
     *
     * @param selectedBacklog the selected backlog
     */
    @FXML
    public void handleNewTask(ProjectBacklog selectedBacklog) {
        Task tempTask = new Task();
        boolean saveClicked = mainApp.showTaskEditDialog(tempTask);
        if (saveClicked) {
            selectedBacklog.addTask(tempTask);
        }
    }


    /**
     * Called when the user clicks the "Edit" button.
     * Opens a dialog to edit details for the selected task.
     *
     * @param selectedTask the selected task
     */
    @FXML
    public void handleEditTask(Task selectedTask) {
        if (selectedTask != null) {
            boolean saveClicked = mainApp.showTaskEditDialog(selectedTask);
            if (saveClicked) {
                showTaskDetails(selectedTask);
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Task Selected");
            alert.setContentText("Please select a task in the list.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the "Delete" button.
     * Deletes the selected task.
     */
    @FXML
    public void handleDeleteTask() {
        int selectedIndex = taskList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Task selectedTask = taskList.getItems().get(selectedIndex);
            taskList.getItems().remove(selectedIndex);
            mainApp.getTaskData().remove(selectedTask);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Task Selected");
            alert.setContentText("Please select a task in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the "Complete" button.
     * Marks the selected task as completed.
     */
    @FXML
    public void completeTask(){
        Task selectedTask = getSelectedTask();
        if (selectedTask != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Complete Task");
            dialog.setHeaderText("Selected Task: " + selectedTask.getName());
            completed = new CheckBox("Completed");
            completed.setSelected(true);
            completed.setDisable(false);

            selectedTask.setCompleted(true);
        }
    }

    /**
     * Called when the user clicks the "View" button.
     * Displays the details of the selected task in a dialog.
     *
     * @param selectedTask the selected task
     */
    @FXML
    public void viewTask(Task selectedTask){
        if (selectedTask != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Task Details");
            dialog.setHeaderText("Selected Task: " + selectedTask.getName());

            taskLabel = new Label("Goal:");
            TextField taskTextArea = new TextField(selectedTask.getTask());
            taskTextArea.setEditable(false);

            Label commentsLabel = new Label("Comments:");
            commentsTextArea = new TextArea(selectedTask.getComments());
            commentsTextArea.setEditable(true);
            commentsTextArea.setWrapText(true);

            completed = new CheckBox("Completed");
            completed.setSelected(selectedTask.isCompleted());
            completed.setDisable(true);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20));

            gridPane.add(taskLabel, 0, 0);
            gridPane.add(taskTextArea, 1, 0);
            gridPane.add(commentsLabel, 0, 1);
            gridPane.add(commentsTextArea, 1, 1);
            gridPane.add(completed, 1, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(okButtonType);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButtonType) {
                    selectedTask.setComments(commentsTextArea.getText());
                }
                return null;
            });

            dialog.showAndWait();
        }
    }

    /**
     * Returns the selected task from the task list.
     *
     * @return the selected task
     */
    private Task getSelectedTask(){
        return taskList.getSelectionModel().getSelectedItem();
    }
}