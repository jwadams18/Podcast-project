package Main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
public class NotesViewController implements Initializable {


    @FXML
    private TextArea notesArea;

    @FXML
    private Button editBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Label notesTitle;

    private Podcast podcast;
    private BooleanProperty isEditable;
    private Model model = Main.model;
    private boolean edited;

    /**
     * This is used to view notes form the library, intended use is when mp3 is delete but user would like to keep their
     * podcast notes and access/view/edit them
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets the controller to this file
        model.setMostRecentNotes(this);
        //
        edited = false;

        isEditable = new SimpleBooleanProperty(this.notesArea.isEditable());
    }

    /**
     * Tracks that the notes have been edited, and sets TextArea to allow edits
     * @param event
     */
    @FXML
    void setEditable(MouseEvent event){
        edited = true;
        this.notesArea.setEditable(!this.notesArea.isEditable());
    }

    /**
     * Closes the window and sets previous window on top
     * @param event
     */
    @FXML
    void closeAction(ActionEvent event){
        //Updates the notes if they editing was enabled.
        if(edited){
            this.podcast.setNotes(this.notesArea.getText());
            model.getMainWindow().updateNotes();
        }
        Stage s = (Stage) closeBtn.getScene().getWindow();
        s.close();
        model.setOnTop(closeBtn);
    }

    /**
     * This is used to load in information, such as title and notes
     * @param podcast the podcast which you want to display the notes for
     */
    public void setPodcast(Podcast podcast){
        this.podcast = podcast;
        notesTitle.setText(this.podcast.getTitle()+ " notes");
        notesArea.setEditable(false);
        notesArea.setText(this.podcast.getNotes());
    }
}
