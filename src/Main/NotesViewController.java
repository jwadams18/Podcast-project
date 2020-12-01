package Main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets the controller to this file
        model.setMostRecentNotes(this);

        edited = false;

        isEditable = new SimpleBooleanProperty(this.notesArea.isEditable());
    }

    @FXML
    void setEditable(MouseEvent event){
        edited = true;
        this.notesArea.setEditable(!this.notesArea.isEditable());
    }

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

    public void setPodcast(Podcast podcast){
        this.podcast = podcast;
        notesTitle.setText(this.podcast.getTitle()+ " notes");
        notesArea.setEditable(false);
        notesArea.setText(this.podcast.getNotes());
    }
}
