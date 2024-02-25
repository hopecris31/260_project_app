package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.model.Sprint;
import edu.union.csc260.sprint.model.Task;
import edu.union.csc260.sprint.model.ProjectBacklog;
import edu.union.csc260.sprint.util.DateUtil;
import edu.union.csc260.sprint.BacklogItemsApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller class for the Sprint Overview.
 * This class controls the interaction between the user interface and the underlying data model for the Sprint Overview.
 * It handles displaying and editing sprint details, managing tasks, starting a sprint and updating the sprint progress.
 * The class is designed to work with the corresponding FXML file (SprintOverview.FXML) that defines the user interface layout.
 *
 * Invariants:
 * - The `mainApp` variable should be set to a valid `BacklogItemsApp` instance before calling any other methods.
 * - The `sprintTable` should be populated with valid `Sprint` objects from the `mainApp`'s sprint data.
 * - The `taskTable` should be populated with tasks associated with the selected sprint.
 *
 * @author Claudia Porto
 */
public class SprintOverviewController {
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> taskTableColumn;
    @FXML
    private TableView<Sprint> sprintTable;
    @FXML
    private TableColumn<Sprint, String> sprintColumn;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button startButton;
    @FXML
    private Button reflectionButton;
    @FXML
    private Button editButton;
    @FXML
    private Button newButton;
    @FXML
    private Button completeTaskButton;
    @FXML
    private Button viewReflectionButton;
    @FXML
    private ButtonBar sprintButtonBar;
    @FXML
    private ButtonBar taskButtonBar;


    @FXML
    private Label titleLabel;
    @FXML
    private Label goalLabel;
    @FXML
    private Label estFinishDateLabel;
    @FXML
    private Label reflectionLabel;


    private TaskOverviewController taskOverviewController;
    private Sprint selectedSprint;
    private BacklogItemsApp mainApp;

    /**
     * The SprintOverviewController constructor.
     * The constructor is called before the initialize() method.
     */
    public SprintOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        sprintColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        sprintTable.setRowFactory(param -> new TableRow<Sprint>() {
            @Override
            protected void updateItem(Sprint sprint, boolean empty) {
                super.updateItem(sprint, empty);
                if (sprint == null || empty) {
                    setStyle("");
                } else if (sprint.isCurrentSprint()) {
                    setStyle("-fx-background-color: #2E2D88; -fx-font-weight: bold;");
                }
            }
        });

        sprintTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    setSelectedSprint(newValue);
                    showSprintDetails(newValue);
                }
        );

        taskTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        taskTableColumn.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Sets the main application instance for the controller.
     *
     * @param mainApp The main application instance.
     */
    public void setMainApp(BacklogItemsApp mainApp) {
        this.mainApp = mainApp;
        this.sprintTable.setItems(mainApp.getSprintData());
        this.taskOverviewController = new TaskOverviewController();
        this.taskOverviewController.setMainApp(mainApp);
    }

    /**
     * Fills all text fields to show details about the selected sprint.
     * If the specified sprint is null, all text fields are cleared.
     *
     * @param sprint The selected sprint to display details for.
     */
    private void showSprintDetails(Sprint sprint) {
        if (sprint != null) {
            this.titleLabel.setText(sprint.getTitle());
            this.goalLabel.setText(sprint.getGoal());
            this.estFinishDateLabel.setText(DateUtil.format(sprint.getEstFinishDate()));
            this.progressBar.setProgress(sprint.getProgress());
            updateSprintProgress();
            if (sprint.isCompleted() && sprint.getReflection() != null) {
                viewReflectionButton.setVisible(true);
                viewReflectionButton.setOnAction(e -> showFullReflection(sprint.getReflection().getText()));
            } else {
                viewReflectionButton.setVisible(false);
            }
        } else {
            this.titleLabel.setText("");
            this.goalLabel.setText("");
            this.estFinishDateLabel.setText("");
            this.progressBar.setProgress(0.0);
            this.viewReflectionButton.setVisible(false);
        }
    }

    /**
     * Sets the selected sprint in the controller and updates the associated task table.
     *
     * @param sprint The selected sprint.
     */
    private void setSelectedSprint(Sprint sprint) {
        this.selectedSprint = sprint;

        this.taskTable.setItems(selectedSprint.getTasks());

        this.sprintTable.refresh();
        this.taskTable.refresh();

        updateStartButtonState();
        updateReflectionButtonState();
        handleSprintCompletion(selectedSprint);
        updateTaskCompleted();
    }

    /**
     * Shows the reflection text in a dialog window.
     *
     * @param reflectionText The reflection text to display.
     */
    @FXML
    private void showFullReflection(String reflectionText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sprint Reflection");
        alert.setHeaderText("Full Reflection");

        TextArea textArea = new TextArea(reflectionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);

        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
    }

    /**
     * Updates the completed state of tasks based on the selected sprint.
     */
    private void updateTaskCompleted(){
        if (mainApp.getSprintData().isEmpty()) {
            completeTaskButton.setDisable(true);
        } else {
            Sprint selectedSprint = sprintTable.getSelectionModel().getSelectedItem();
            boolean isCurrentSprint = selectedSprint != null && selectedSprint.isCurrentSprint();

            if (isCurrentSprint) {
                boolean allTasksCompleted = selectedSprint.getTasks().stream().allMatch(Task::isCompleted);
                completeTaskButton.setDisable(allTasksCompleted);
            } else {
                completeTaskButton.setDisable(true);
            }
        }
    }

    /**
     * Updates the visibility and state of the reflection button.
     */
    private void updateReflectionButtonState(){
        if (mainApp.getSprintData().isEmpty()) {
            viewReflectionButton.setVisible(false);
            reflectionButton.setVisible(false);
        } else {
            for (Sprint sprint : mainApp.getSprintData()) {
                if (sprint.isCompleted()) {
                    reflectionButton.setText("Add Reflection");
                    reflectionButton.setVisible(true);
                    reflectionButton.setDisable(false);
                    viewReflectionButton.setText("View Reflection");
                    viewReflectionButton.setVisible(true);
                    viewReflectionButton.setDisable(false);
                } else {
                    reflectionButton.setVisible(false);
                    viewReflectionButton.setVisible(false);
                }
            }
        }
    }

    /**
     * Updates the visibility and state of the start button.
     */
    private void updateStartButtonState() {
        boolean currentSprintExists = false;

        for (Sprint sprint : mainApp.getSprintData()) {
            if (sprint.isCurrentSprint()) {
                currentSprintExists = true;
                break;
            }
        }

        for (Sprint sprint : mainApp.getSprintData()) {
            if (sprint.isCompleted()) {
                startButton.setText("Sprint Completed");
                startButton.setDisable(true);
            }
        }

        if (currentSprintExists) {
            startButton.setText("Current Sprint Active");
            startButton.setDisable(true);
        } else {
            startButton.setText("Start");
            startButton.setDisable(false);
        }
    }

    /**
     * Handles the "New Sprint" button action. Displays a dialog for selecting a backlog item and creating a new sprint.
     *
     * @param event The action event.
     */
    @FXML
    private void handleNewSprint(ActionEvent event) {
        this.newButton = (Button) event.getSource();

        ChoiceBox<String> backlogChoiceBox = new ChoiceBox<>();

        ObservableList<ProjectBacklog> projectBacklogData = this.mainApp.getBacklogData();
        for (ProjectBacklog backlog : projectBacklogData) {
            backlogChoiceBox.getItems().add(backlog.getTitle());
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Select Backlog Item");
        dialog.setHeaderText("Select a backlog item to use for the new sprint:");

        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        VBox content = new VBox();
        content.getChildren().addAll(backlogChoiceBox);

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return backlogChoiceBox.getValue();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String selectedBacklogTitle = result.get();
            ProjectBacklog selectedBacklog = null;
            for (ProjectBacklog backlog : projectBacklogData) {
                if (backlog.getTitle().equals(selectedBacklogTitle)) {
                    selectedBacklog = backlog;
                    break;
                }
            }

            if (selectedBacklog != null) {
                Sprint tempSprint = new Sprint(selectedBacklog);

                boolean okClicked = this.mainApp.showSprintEditDialog(tempSprint);
                if (okClicked) {
                    this.mainApp.getSprintData().add(tempSprint);
                }
            }
        }
    }

    /**
     * Handles the "Edit Sprint" button action. Allows editing the estimated finish date of a sprint.
     *
     * @param event The action event (button click).
     */
    @FXML
    private void handleEditSprint(ActionEvent event){
        this.editButton = (Button) event.getSource();

        Sprint selectedSprint = this.sprintTable.getSelectionModel().getSelectedItem();

        if (selectedSprint == null) {
            showErrorAlert("No sprint selected", "Please select a sprint.");
            return;
        }

        if (selectedSprint.isCurrentSprint()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Sprint in progress");
            alert.setContentText("The current sprint is already in progress. Editing is not allowed.");
            ButtonType continueButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(continueButton);
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(DateUtil.format(selectedSprint.getEstFinishDate()));
        dialog.setTitle("Edit Sprint");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the new estimated start date:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newStartDate -> {
            if (DateUtil.validDate(newStartDate)) {
                LocalDate parsedDate = DateUtil.parse(newStartDate);
                selectedSprint.setEstFinishDate(parsedDate);
            } else {
                showErrorAlert("Invalid Date Format", "Please enter the date in the format: dd.mm.yyyy");
            }
        });
    }

    /**
     * Handles the "Start Sprint" button action. Starts the selected sprint.
     *
     * @param event The action event (button click).
     */
    @FXML
    private void handleStartSprint(ActionEvent event) {
        this.startButton = (Button) event.getSource();
        Sprint selectedSprint = this.sprintTable.getSelectionModel().getSelectedItem();

        if (selectedSprint == null) {
            showErrorAlert("No sprint selected", "Please select a sprint.");
            return;
        }

        if (selectedSprint.isCompleted()) {
            handleAddReflection();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Start Sprint");
            alert.setContentText("Are you sure you want to start this sprint?");

            ButtonType startButtonType = new ButtonType("Start");
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(startButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == startButtonType) {
                selectedSprint.setCurrentSprint(true);
            }
        }
        updateTaskCompleted();
        updateStartButtonState();
    }

    /**
     * Handles the "Add Reflection" button action. Opens a dialog to add a reflection to the selected completed sprint.
     * If the selected sprint is not completed, shows an error alert.
     */
    @FXML
    private void handleAddReflection() {
        Sprint selectedSprint = sprintTable.getSelectionModel().getSelectedItem();

        if (selectedSprint != null && selectedSprint.isCompleted()) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add Reflection");
            dialog.setHeaderText(null);

            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            TextArea reflectionTextArea = new TextArea();
            reflectionTextArea.setPromptText("Enter reflection");
            reflectionTextArea.setText(selectedSprint.getReflection().getText());

            dialog.getDialogPane().setContent(reflectionTextArea);

            Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
            addButtonNode.setDisable(true);
            reflectionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
                addButtonNode.setDisable(newValue.trim().isEmpty());
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButton) {
                    return reflectionTextArea.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(reflectionText -> {
                selectedSprint.setReflection(reflectionText);
                if (reflectionText.length() > 0) {
                    viewReflectionButton.setVisible(true);
                    viewReflectionButton.setOnAction(e -> showFullReflection(reflectionText));
                }
                updateReflectionButtonState();
            });
        } else {
            showErrorAlert("Sprint Not Completed", "Cannot add reflection for an incomplete sprint.");
        }
    }

    /**
     * Handles the completion of a sprint.
     * If the sprint is marked as completed, it updates the UI and enables the start button.
     * If the sprint is ready for the next sprint, it marks the sprint as completed.
     *
     * @param s The sprint to handle completion for.
     */
    @FXML
    private void handleSprintCompletion(Sprint s) {
        if (isSprintEndDateReached()) {
            s.setCurrentSprint(false);
            s.setCompleted(true);
            updateReflectionButtonState();
            startButton.setDisable(true);
            startButton.setText("Start");
        }
    }

    /**
     * Checks if the end date of the selected sprint has been reached.
     *
     * @return true if the end date has been reached, false otherwise.
     */
    private boolean isSprintEndDateReached() {
        LocalDate today = LocalDate.now();

        boolean allTasksCompleted = selectedSprint.getTasks().stream().allMatch(Task::isCompleted);
        boolean isPastFinishDate = today.isAfter(selectedSprint.getEstFinishDate());

        return allTasksCompleted || isPastFinishDate;
    }

    /**
     * Updates the progress of the selected sprint based on the completion status of its tasks.
     * Also updates the progress bar in the UI.
     */
    @FXML
    private void updateSprintProgress() {
        Sprint selectedSprint = sprintTable.getSelectionModel().getSelectedItem();
        if (selectedSprint != null) {
            ObservableList<Task> tasks = selectedSprint.getTasks();
            int completedTasks = 0;
            for (Task task : tasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                }
            }
            double progress = (double) completedTasks / (double) tasks.size();
            selectedSprint.setProgress(progress);
            progressBar.setProgress(progress);
        }
    }

    /**
     * Marks the selected task as completed and updates the UI accordingly.
     */
    @FXML
    private void completeTask(){
        updateTaskCompleted();
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask.isCompleted()){
            completeTaskButton.setDisable(true);
        }
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Confirmation");
        warningAlert.setHeaderText("Complete Task");
        warningAlert.setContentText("Are you sure you want to complete this task?\nThis action cannot be undone.");

        ButtonType completeButtonType = new ButtonType("Complete");

        warningAlert.getButtonTypes().setAll(completeButtonType);

        Optional<ButtonType> result = warningAlert.showAndWait();
        if (result.isPresent() && result.get() == completeButtonType) {
            selectedTask.setCompleted(true);
            taskOverviewController.completeTask();
            updateSprintProgress();
            if (selectedSprint.isCompleted()) {
                reflectionButton.setVisible(true);
            }
        }
    }

    /**
     * Handles the selection of a task from the task table.
     * Updates the completion status of the tasks and displays the details of the selected task.
     */
    @FXML
    private void handleTaskSelection(){
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        updateTaskCompleted();
        taskOverviewController.viewTask(selectedTask);
    }

    /**
     * Sets the visibility of buttons in the button bar.
     * @param visible true to make the buttons visible, false otherwise
     */
    public void setButtonVisibility(boolean visible) {
        taskButtonBar.getButtons().forEach(button -> button.setVisible(visible));
        sprintButtonBar.getButtons().forEach(button -> button.setVisible(visible));

        viewReflectionButton.setVisible(true);

        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setOnAction(event -> handleAddComment());
        taskButtonBar.getButtons().add(addCommentButton);
        addCommentButton.setVisible(true);
    }

    /**
     * Handles the "Add Comment" button action.
     * Opens a dialog to add a comment to the selected sprint.
     */
    private void handleAddComment() {
        if(selectedSprint != null){
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Add Comment");
            dialog.setHeaderText("Add a comment to the sprint");

            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            TextArea commentTextArea = new TextArea();
            commentTextArea.setPromptText("Enter your comment");
            dialog.getDialogPane().setContent(commentTextArea);

            commentTextArea.requestFocus();

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButton) {
                    return commentTextArea.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(comment -> {
                selectedSprint.addComment(comment);
            });
        }
    }

    /**
     * Shows an error alert dialog with the specified header and content text.
     *
     * @param headerText  The header text for the error alert.
     * @param contentText The content text for the error alert.
     */
    @FXML
    private static void showErrorAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
