package Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PodcastCell extends ListCell<Podcast>{

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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PodcastCell.fxml"));
            loader.setController(this);
//            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Podcastcell.fxml loaded");
    }

    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        super.updateItem(podcast, empty);

//        isPlaying.setVisible(false);
//        hasNotes.setVisible(false);

        if(empty || podcast == null) {
            podcastTitle.setVisible(false);
            podcastProgress.setVisible(false);
            setText(null);
            HBox temp = new HBox();
            temp.setPrefHeight(34.0);
            setGraphic(temp);
        } else {
            podcastTitle.setVisible(true);
            podcastTitle.setText(podcast.getTitle());
            podcastProgress.setProgress(podcast.getProgress());
            podcastProgress.setVisible(true);

            if(podcast.hasNotes()){
                System.out.println("Is this running?");
                hasNotes.setVisible(true);
            }
            if(podcast.isPlaying()){
                isPlaying.setVisible(true);
            }

            setGraphic(container);
        }

//        setContentDisplay(ContentDisplay.CENTER);
    }
}
