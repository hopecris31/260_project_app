package edu.union.csc260.sprint;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;
import java.util.Formatter;
import java.util.Scanner;
import java.util.List;

import edu.union.csc260.sprint.controller.*;
import edu.union.csc260.sprint.model.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Project Backlog Scrum Web Application
 *
 * This class represents the main application for managing project backlog items. It provides functionality
 * for user login, displaying different views based on user roles, loading and saving data to a SQL database, and
 * managing the main stage and root layout.
 *
 * Invariants:
 * - The class must extend the Application class from the JavaFX framework.
 * - The class must have a public constructor with no parameters.
 * - The class must override the start() method from the Application class.
 * - The class must have access to the necessary controllers and models for managing project backlog items.
 *
 * Usage:
 * 1. Create an instance of the BacklogItemsApp class.
 * 2. Call the start() method to launch the application.
 * 3. Implement the necessary controllers and models for managing project backlog items.
 *
 * @author Hope Crisafi and Claudia Porto
 */
public class BacklogItemsApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private TaskOverviewController taskOverviewController;
    private SprintOverviewController sprintOverviewController;

    /**
     * The data as an observable list of Backlogs, Sprints, Tasks, Users(Students), and Groups.
     */
    private ObservableList<ProjectBacklog> backlogData = FXCollections.observableArrayList();
    private ObservableList<Sprint> sprintData = FXCollections.observableArrayList();
    private ObservableList<Task> taskData = FXCollections.observableArrayList();
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<Group> groupData = FXCollections.observableArrayList();


    /**
     * Returns the main stage of the application.
     *
     * @return The main stage of the application.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Creates an empty project backlog and adds it to the backlog data list.
     */
    public void createEmpty() {
        ProjectBacklog emptyBacklog = new ProjectBacklog();
        emptyBacklog.setTitle("");
        emptyBacklog.setBacklogGoal("");

        getBacklogData().add(emptyBacklog);
    }

    /**
     * Starts the application by setting up the primary stage and showing the login window.
     *
     * @param primaryStage The primary stage of the application.
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login to Backlog Items App");

        showLoginWindow();
    }

    /**
     * Shows the login window to the user and opens associated Scene with user selection
     */
    public void showLoginWindow() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        Label roleLabel = new Label("Select Role:");
        RadioButton studentRadioButton = new RadioButton("Student");
        RadioButton professorRadioButton = new RadioButton("Professor");

        ToggleGroup roleToggleGroup = new ToggleGroup();
        studentRadioButton.setToggleGroup(roleToggleGroup);
        professorRadioButton.setToggleGroup(roleToggleGroup);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            boolean isStudent = studentRadioButton.isSelected();
            boolean isProfessor = professorRadioButton.isSelected();

            if (!isStudent && !isProfessor) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No role selected");
                alert.setContentText("Please select either Student or Professor.");
                alert.showAndWait();
            } else if (firstName.isEmpty() || lastName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing information");
                alert.setContentText("Please enter both First and Last Name.");
                alert.showAndWait();
            } else {
                primaryStage.hide();

                if (isStudent) {
                    this.primaryStage.setTitle("Backlog Items App: Student");
                    if(!SQLDatabase.checkUserExists(firstName, lastName, 1)) {
                        User student = new User(firstName, lastName, true);
                    }
                    studentRootLayout();
                } else {
                    this.primaryStage.setTitle("Backlog Items App: Professor");
                    if(!SQLDatabase.checkUserExists(firstName, lastName, 1)) {
                        User professor = new User(firstName, lastName, false);
                    }
                    professorRootLayout();
                }
            }
        });

        grid.add(firstNameLabel, 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(lastNameLabel, 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(studentRadioButton, 1, 2);
        grid.add(professorRadioButton, 1, 3);
        grid.add(loginButton, 1, 4);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initializes the root layout for a student user.
     * Loads the RootLayout.fxml file and sets it as the main layout.
     * Also loads project backlog data from a file if available.
     */
    public void studentRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            RootLayoutController controller = loader.getController();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            controller.setMainApp(this);

            File file = getProjectBacklogFilePath();
            if (file != null) {
                loadProjectBacklogDataFromFile(file);
            }

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout for a professor user.
     * Loads the ProfRootLayout.fxml file and sets it as the main layout.
     * Also loads project backlog data from a file if available.
     */
    public void professorRootLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/ProfRootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            ProfessorRootLayoutController controller = loader.getController();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            controller.setMainApp(this);

            File file = getProjectBacklogFilePath();
            if (file != null) {
                loadProjectBacklogDataFromFile(file);
            }

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout for viewing purposes.
     * Loads the ViewRootLayout.fxml file and sets it as the main layout.
     * Also loads project backlog data from a file if available.
     */
    public void viewRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/ViewRootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            ViewRootLayoutController controller = loader.getController();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            controller.setMainApp(this);

            File file = getProjectBacklogFilePath();
            if (file != null) {
                loadProjectBacklogDataFromFile(file);
            }

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads BacklogItem data from the specified file. The current projectBacklog data will
     * be replaced.
     *
     * @param file the file containing the project backlog data
     */
    public void loadProjectBacklogDataFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                ProjectBacklog p = new ProjectBacklog(parts[0], parts[1], null);
                backlogData.add(p);
            }
            scanner.close();
            setBacklogFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Returns the user file preference, i.e., the file that was last opened.
     * The preference is read from the OS-specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return the file representing the project backlog file path, or null if not found
     */
    public File getProjectBacklogFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(BacklogItemsApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS-specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setBacklogFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(BacklogItemsApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            primaryStage.setTitle("BacklogItemsApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            primaryStage.setTitle("BacklogItemsApp");
        }
    }

    /**
     * Saves the current ProjectBacklog data to the specified file.
     *
     * @param file the file to save the project backlog data to
     */
    public void saveProjectBacklogDataToFile(File file) {
        try {
            Formatter formatter = new Formatter(file);
            for (ProjectBacklog p : backlogData) {
                List<String> allFields = List.of(p.getTitle(), p.getBacklogGoal());
                String s = String.join(",", allFields);
                formatter.format(s + "\n");
            }
            formatter.close();
            setBacklogFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Shows the backlog overview inside the root layout.
     * Loads the BacklogOverview.fxml file and sets it as the center of the root layout.
     * Also initializes the BacklogOverviewController and TaskOverviewController.
     */
    public void showBacklogOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/BacklogOverview.fxml"));
            AnchorPane backlogOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(backlogOverview);

            BacklogOverviewController controller = loader.getController();
            controller.setMainApp(this);

            taskOverviewController = new TaskOverviewController();
            taskOverviewController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the backlog overview inside the root layout with modified button visibility and an additional "Add Comment" button.
     * Loads the BacklogOverview.fxml file and sets it as the center of the root layout.
     * Also initializes the BacklogOverviewController and TaskOverviewController.
     */
    public void showViewBacklogOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/BacklogOverview.fxml"));
            AnchorPane backlogOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(backlogOverview);

            BacklogOverviewController controller = loader.getController();
            controller.setMainApp(this);

            controller.setButtonVisibility(false);

            taskOverviewController = new TaskOverviewController();
            taskOverviewController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified backlog item. If the user
     * clicks OK, the changes are saved into the provided backlog object and true
     * is returned. (BACKLOG EDIT)
     *
     * @param backlogItem the task object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showBacklogEditDialog(ProjectBacklog backlogItem) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/BacklogEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Backlog Item");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BacklogEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProjectBacklog(backlogItem);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows the sprint overview inside the root layout.
     * Loads the SprintOverview.fxml file and sets it as the center of the root layout.
     * Also initializes the SprintOverviewController.
     */
    public void showSprintOverview(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/SprintOverview.fxml"));
            AnchorPane sprintOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(sprintOverview);

            SprintOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the sprint overview inside the root layout with modified button visibility (view version).
     * Loads the SprintOverview.fxml file and sets it as the center of the root layout.
     * Also initializes the SprintOverviewController.
     */
    public void showViewSprintOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/SprintOverview.fxml"));
            AnchorPane sprintOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(sprintOverview);

            SprintOverviewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setButtonVisibility(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified sprint. If the user
     * clicks OK, the changes are saved into the provided sprint object and true
     * is returned.
     *
     * @param sprint the sprint object to be edited
     * @return true if the user clicked OK, false otherwise
     */
    public boolean showSprintEditDialog(Sprint sprint) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/SprintEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Sprint");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SprintEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSprint(sprint);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a dialog to edit details for the specified task. If the user
     * clicks OK, the changes are saved into the provided task object and true
     * is returned.
     *
     * @param task the task object to be edited
     * @return true if the user clicked OK, false otherwise
     */
    public boolean showTaskEditDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/TaskEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Backlog Item");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TaskEditController controller = loader.getController();
            controller.setTaskStage(dialogStage);
            controller.setTask(task);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows the student overview inside the root layout.
     * Loads the UserOverview.fxml file and sets it as the center of the root layout.
     * Also initializes the UserOverviewController.
     */
    public void showStudentOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/UserOverview.fxml"));
            AnchorPane userOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(userOverview);

            UserOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified student. If the user
     * clicks OK, the changes are saved into the provided student object and true
     * is returned.
     *
     * @param student the student object to be edited
     * @return true if the user clicked OK, false otherwise
     */
    public boolean showUserEditDialog(User student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/UserEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UserEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUser(student);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows the group overview inside the root layout.
     * Loads the GroupOverview.fxml file and sets it as the center of the root layout.
     * Also initializes the GroupOverviewController.
     */
    public void showGroupOverview(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/GroupOverview.fxml"));
            AnchorPane groupOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(groupOverview);

            GroupOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified group. If the user
     * clicks OK, the changes are saved into the provided group object and true
     * is returned.
     *
     * @param group the group object to be edited
     * @return true if the user clicked OK, false otherwise
     */
    public boolean showGroupEditDialog(Group group) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BacklogItemsApp.class.getResource("/view/GroupEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Group");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            GroupEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGroup(group);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows a group selection dialog to choose a student from the group.
     * If a student is selected, it handles the selected student.
     * The selected student triggers the viewRootLayout() method.
     *
     * @param groupId The ID of the group to retrieve group members from.
     */
    public void showGroupSelection(int groupId) {
        ObservableList<User> groupMembers = SQLDatabase.getGroupMembersQuery(groupId);

        Alert groupSelectionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        groupSelectionDialog.setTitle("Group Selection");
        groupSelectionDialog.setHeaderText("Select a student");

        ChoiceBox<User> studentChoiceBox = new ChoiceBox<>();
        studentChoiceBox.setItems(groupMembers);

        groupSelectionDialog.getDialogPane().setContent(studentChoiceBox);

        Optional<ButtonType> result = groupSelectionDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            User selectedStudent = studentChoiceBox.getValue();
            if (selectedStudent != null) {
                handleSelectedStudent(selectedStudent);
            }
        }
    }

    /**
     * Shows a group selection dialog to choose a student from the group.
     * If a student is selected, it handles the selected student.
     * The selected student triggers the viewRootLayout() method.
     */
    private void handleSelectedStudent(User selectedStudent) {
        if (selectedStudent != null) {
            this.primaryStage.setTitle("Backlog Items App: Student View");
            viewRootLayout();
        }
    }

    /**
     * Returns the observable list of task data.
     *
     * @return the observable list of task data
     */
    public ObservableList<Task> getTaskData() {
        return taskData;
    }

    /**
     * Returns the observable list of user data.
     *
     * @return the observable list of user data
     */
    public ObservableList<User> getUserData() {
        return userData;
    }

    /**
     * Returns the observable list of group data.
     *
     * @return the observable list of group data
     */
    public ObservableList<Group> getGroupData() {
        return groupData;
    }

    /**
     * Returns the observable list of backlog data.
     *
     * @return the observable list of backlog data
     */
    public ObservableList<ProjectBacklog> getBacklogData() {
        return backlogData;
    }

    /**
     * Returns the observable list of sprint data.
     *
     * @return the observable list of sprint data
     */
    public ObservableList<Sprint> getSprintData() {
        return sprintData;
    }
}
