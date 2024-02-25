package edu.union.csc260.sprint.controller;

import java.io.File;
import java.util.Optional;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.SQLDatabase;
import edu.union.csc260.sprint.model.Group;
import edu.union.csc260.sprint.model.User;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller class for the View Root Layout.
 *
 * Invariants:
 * - The `backlogApp` reference is set by the main application.
 * - The `user` reference is set by the main application.
 * - The `editWindowShown` flag is initially set to false.
 * @author Claudia Porto
 *
 */
public class ViewRootLayoutController {

    private BacklogItemsApp backlogApp;
    private User user;

    private boolean editWindowShown = false;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param backlogApp
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scrum User App");
        alert.setHeaderText("About");

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
     * Shows the view backlog overview.
     */
    @FXML
    public void showViewBacklogOverview() {
        backlogApp.showViewBacklogOverview();
    }

    /**
     * Shows the view sprint overview.
     */
    @FXML
    public void showViewSprintOverview(){
        backlogApp.showViewSprintOverview();
    }
}

