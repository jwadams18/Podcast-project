package Main;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PodcastCell extends ListCell<Podcast> implements Initializable {

    @FXML
    private HBox container;

    @FXML
    private ImageView hasNotes;

    @FXML
    private ProgressBar podcastProgress;

    @FXML
    private Label podcastTitle;

    @FXML
    private ImageView isPlaying;


    public PodcastCell(){
        loadFXML();
    }

    public void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PodcastCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Podcastcell.fxml loaded");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        super.updateItem(podcast, empty);

        if(empty || podcast == null) {
            podcastTitle.setVisible(false);
            podcastProgress.setVisible(false);
            setText(null);
            setGraphic(null);
        } else {
            podcastTitle.setVisible(true);
            podcastTitle.setText(podcast.getTitle());
            podcastProgress.setProgress(podcast.getProgress());
            podcastProgress.setVisible(true);


            if(podcast.hasNotes()){
                hasNotes.setVisible(true);
            }
            if(podcast.isPlaying()){
//                isPlaying.setVisible(true);
            }
        }
        setGraphic(container);
        setContentDisplay(ContentDisplay.CENTER);
    }
}
