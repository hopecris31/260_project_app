package edu.union.csc260.sprint.controller;

import edu.union.csc260.sprint.BacklogItemsApp;
import edu.union.csc260.sprint.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * ProfessorRootLayoutController Class
 *
 * Controller class for the Professor side Root Layout.
 *
 * Invariants:
 * - groupOverviewController, backlogApp, and user should not be null and should be set using the setter methods.
 *
 * @author Yuxing Liu and Claudia Porto
 *
 */
public class ProfessorRootLayoutController {
    private BacklogItemsApp backlogApp;
    private User user;

    /**
     * Sets the reference to the main application.
     *
     * @param backlogItemsApp The main application instance.
     */
    public void setMainApp(BacklogItemsApp backlogItemsApp){
        this.backlogApp = backlogItemsApp;
    }

    /**
     * Creates an empty list of backlog items.
     */
    @FXML
    private void handleNew() {
        backlogApp.createEmpty();
    }

    /**
     * Saves the file to the SQL
     */
    @FXML
    private void handleSave() {       
    }


    /**
     * Opens an about dialog.
     */
    @FXML
    private void showAboutWindow() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Scrum User App");
        alert.setHeaderText("About");
        //HOPE QUERY THIS SHI
        String userText = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n";

        String projectText = "Author: Claudia Porto, Hope Crisafi, Dina Saef, and Yuxing Liu";
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
     * Shows the group overview.
     */
    @FXML
    public void showGroupOverview() {
        backlogApp.showGroupOverview();
    }

    /**
     * shows the student overview
     */
    @FXML
    public void showStudentOverview(){
        backlogApp.showStudentOverview();
    }
}
