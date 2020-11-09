package Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class PodcastCell extends ListCell<Podcast>{

    private Model model = Main.model;

    @FXML
    private AnchorPane container;

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
            loader.setLocation(getClass().getResource(model.PODCAST_CELL_PATH));
            loader.setController(this);
//            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        super.updateItem(podcast, empty);

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

            System.out.println(getClass().getName()+" "+podcast.hasNotes());
            System.out.println(getClass().getName()+" "+podcast.isPlaying());

            if(podcast.hasNotes()){
                hasNotes.setVisible(true);
            } else {
                hasNotes.setVisible(false);
            }
            if(podcast.isPlaying()){
                isPlaying.setVisible(true);
            } else {
                isPlaying.setVisible(false);
            }

            setGraphic(container);
        }

//        setContentDisplay(ContentDisplay.CENTER);
    }
}
