package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.ProjectBacklog;
import edu.union.csc260.sprint.model.Task;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Backlog Overview Controller class
 * This class is responsible for handling the user interface logic for the backlog overview.
 * It manages the display and interaction with project backlogs and tasks.
 *
 * Invariants:
 * - The projectBacklogTable should not be null after the FXML file has been loaded.
 * - The taskTable should not be null after the FXML file has been loaded.
 * - The mainApp should not be null and should be set by calling the setMainApp method.
 * - The taskOverviewController should not be null and should be set by calling the setMainApp method.
 * - The handleViewTask, handleAddTask, handleEditTask, handleDeleteTask, handleDeleteBacklogItem,
 *   handleNewBacklogItem, and handleEditBacklogItem methods should be called when the corresponding buttons are clicked.
 * - The showBacklogDetails method should display the details of the selected backlog.
 * - The setSelectedBacklog method should set the selected backlog and refresh the task table.
 * - The setButtonVisibility method should hide or show buttons based on the provided boolean value.
 *
 * @author Hope Crisafi and Claudia Porto
 */

public class BacklogOverviewController {
    @FXML
    private Label titleLabel;

    @FXML
    private Label goalLabel;
    @FXML
    private Button viewTaskButton;
    @FXML
    private ButtonBar backlogButtonBar;
    @FXML
    private ButtonBar taskButtonBar;

    @FXML
    private TableView<ProjectBacklog> projectBacklogTable;
    @FXML
    private TableColumn<ProjectBacklog, String> projectBacklogColumn;

    private TaskOverviewController taskOverviewController;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> taskTableColumn;

    private ProjectBacklog selectedBacklog;

    private BacklogItemsApp mainApp;

    private boolean okClicked = false;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public BacklogOverviewController() {
    }

    /**
     * Initializes the backlog overview controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        projectBacklogColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        taskTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        projectBacklogTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    setSelectedBacklog(newValue);
                    showBacklogDetails(newValue);
                }
        );

        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        taskTableColumn.setMaxWidth(Double.MAX_VALUE);
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp BacklogItemsApp instance
     */
    public void setMainApp(BacklogItemsApp mainApp) {
        this.mainApp = mainApp;

        projectBacklogTable.setItems(mainApp.getBacklogData());

        taskOverviewController = new TaskOverviewController();
        taskOverviewController.setMainApp(mainApp);
    }

    /**
     * Fills all text fields to show details about the backlog.
     * If the specified backlog is null, all text fields are cleared.
     *
     * @param backlog The project backlog or null.
     */
    private void showBacklogDetails(ProjectBacklog backlog) {
        if (backlog != null) {
            titleLabel.setText(backlog.getTitle());
            goalLabel.setText(backlog.getBacklogGoal());
        } else {
            titleLabel.setText("");
            goalLabel.setText("");
        }
    }

    /**
     * Sets the selected backlog and updates the task table accordingly.
     *
     * @param backlog The selected project backlog.
     */
    private void setSelectedBacklog(ProjectBacklog backlog) {
        this.selectedBacklog = backlog;

        this.taskTable.setItems(selectedBacklog.getTasks());

        this.projectBacklogTable.refresh();
        this.taskTable.refresh();
    }

    /**
     * Handles the view task action when the user clicks the view task button.
     */
    @FXML
    private void handleViewTask(){
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        taskOverviewController.viewTask(selectedTask);
    }

    /**
     * Handles the create new backlog item action when the user clicks the create new backlog item button.
     */
    @FXML
    private void handleNewBacklogItem() {
        ProjectBacklog tempBacklog = new ProjectBacklog();
        boolean okClicked = mainApp.showBacklogEditDialog(tempBacklog);
        if (okClicked) {
            mainApp.getBacklogData().add(tempBacklog);
        }
    }

    /**
     * Handles the edit backlog item action when the user clicks the edit backlog item button.
     */
    @FXML
    private void handleEditBacklogItem() {
        ProjectBacklog selectedBacklogItem = projectBacklogTable.getSelectionModel().getSelectedItem();
        if (selectedBacklogItem != null) {
            boolean okClicked = mainApp.showBacklogEditDialog(selectedBacklogItem);
            if (okClicked) {
                showBacklogDetails(selectedBacklogItem);
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Backlog Item Selected");
            alert.setContentText("Please select a backlog item in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Handles the delete backlog item action when the user clicks the delete backlog item button.
     */
    @FXML
    private void handleDeleteBacklogItem() {
        int selectedIndex = projectBacklogTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            ProjectBacklog selectedBacklogItem = projectBacklogTable.getItems().get(selectedIndex);
            projectBacklogTable.getItems().remove(selectedIndex);
            mainApp.getBacklogData().remove(selectedBacklogItem);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Backlog Item Selected");
            alert.setContentText("Please select a Backlog Item in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Handles the add task action when the user clicks the add task button.
     */
    @FXML
    private void handleAddTask() {
        taskOverviewController.handleNewTask(selectedBacklog);

        taskTable.setItems(selectedBacklog.getTasks());
        taskTable.refresh();
    }

    /**
     * Handles the edit task action when the user clicks the edit task button.
     */
    @FXML
    private void handleEditTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskOverviewController.handleEditTask(selectedTask);
        }
    }

    /**
     * Handles the delete task action when the user clicks the delete task button.
     */
    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskOverviewController.handleDeleteTask();
        }
    }

    /**
     * Sets the visibility of buttons in the button bars to false, except for viewTaskButton.
     *
     * @param visible boolean indicating if buttons should be set to visible or not.
     */
    public void setButtonVisibility(boolean visible) {
        backlogButtonBar.getButtons().forEach(button -> button.setVisible(visible));
        taskButtonBar.getButtons().forEach(button -> button.setVisible(visible));
        viewTaskButton.setVisible(true);
    }
}