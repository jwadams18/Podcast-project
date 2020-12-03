package Main;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
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

    @FXML
    private Button removeBtn;

    private Podcast podcast;

    /**
     * This cell will be displayed in the ListView that represents the Queued podcast list
     */
    public PodcastCell(){
        loadFXML();
    }

    public void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(model.PODCAST_CELL_PATH));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maintains the data in the list cell
     * @param podcast the podcast that will supply the data to be displayed
     * @param empty
     */
    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        boolean wasEmpty = isEmpty();
        super.updateItem(podcast, empty);

        final ChangeListener<Boolean> changeListener =(observableValue, oldValue, newValue) -> {
//            System.out.println("["+getClass().getName()+" "+observableValue+" ] The observableValue has " + "changed: oldValue = " + oldValue + ", newValue = " + newValue);
        };

        //Hides controls that would have nothing to display
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
//            podcastProgress.setProgress(podcast.getProgress());
            podcastProgress.setVisible(true);

            this.podcast = podcast;
            this.podcast.setCellProgressBar(podcastProgress);

            //Watches the isPlaying/isQueued/hasNotes properties and updates the icons
            //Only adds a listener if cell was previously empty
            if(wasEmpty != empty){
                this.podcast.isPlayingProperty().addListener(changeListener);
                this.podcast.hasNotesProperty().addListener(changeListener);
                this.podcast.isQueuedProperty().addListener(changeListener);
            }

            //Toggles visibility settings based on properties of podcast
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

    }

    /**
     * ActionListener for the button in the ListView, will remove podcast from the queued list
     * @param event
     */
    @FXML
    public void removeAction(ActionEvent event){
        if(model.getQueueList().contains(podcast)){
            podcast.setNotes(model.getMainWindow().getNotes());
            podcast.setQueued(false);
            model.getQueueList().remove(podcast);
        }
    }
}
