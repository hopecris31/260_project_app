package edu.union.csc260.sprint.controller;

import java.io.File;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for the Root Layout.
 *
 * Invariants:
 * - sprintOverviewController, backlogApp, and user should not be null and should be set using the appropriate setter methods.
 *
 * @author Claudia Porto
 *
 */
public class RootLayoutController {
    private BacklogItemsApp backlogApp;
    private User user;

    private boolean editWindowShown = false;

    /**
     * Sets the reference to the main application.
     *
     * @param backlogApp The main application instance.
     */
    public void setMainApp(BacklogItemsApp backlogApp) {
        this.backlogApp = backlogApp;
    }

    /**
     * Creates an empty list of backlog items.
     */
    @FXML
    private void handleNew() {
        backlogApp.createEmpty();
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        //HOPE
        File projectBacklogFile = backlogApp.getProjectBacklogFilePath();
        if (projectBacklogFile != null) {
            backlogApp.saveProjectBacklogDataToFile(projectBacklogFile);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void showAboutWindow() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Scrum User App");
        alert.setHeaderText("About");
        //HOPE
        String userText = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n";

        String projectText = "Author: Claudia Porto, Hope Crisafi, and Yuxing Liu";
        projectText += "\nClass: CSC-260";
        projectText += "\nTerm: Spring 2023";

        String aboutText = userText + "\n\n" + projectText;

        alert.setContentText(aboutText);
        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Shows the backlog overview.
     */
    @FXML
    public void showBacklogOverview() {
        backlogApp.showBacklogOverview();
    }

    /**
     * Shows the sprint overview.
     */
    @FXML
    public void showSprintOverview(){
        backlogApp.showSprintOverview();
    }

    /**
     * Shows the group selection.
     */
    @FXML
    public void showGroupSelection() {
        int groupId = user.getGroup();
        backlogApp.showGroupSelection(groupId);
    }
}
