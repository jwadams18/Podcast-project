package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class PodcastCompletionController implements Initializable {
    private Model model = Main.model;
    private Podcast podcast;

    @FXML
    private Button libraryBtn;

    @FXML
    private Button queueBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    /*
    if this podcast is complete
    then display this popup
     */
    }
    @FXML
    public void addBackToLibrary(ActionEvent event) {
    /*
    when clicked
    add the podcast back to the library
     */
    }

    @FXML
    public void addToBottomQueue(ActionEvent event) {
    /*
    when clicked
    add podcast to bottom of queue
     */
    }

}
