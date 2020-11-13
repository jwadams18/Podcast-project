package Main;

import Main.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LibCell extends ListCell<Podcast> {

    @FXML
    private AnchorPane container;

    @FXML
    private ImageView podcastCover;

    @FXML
    private Label podCastTitle;

    @FXML
    private Button queueBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Button viewNotes;

    @FXML
    private Label podcastPubDate;

    @FXML
    private Label podcastDuration;

    private Podcast podcast;

    public LibCell() {
        loadFXML();
    }

    public void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Main.model.LIBRARY_VIEWCELL_PATH));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        queueBtn.setOnAction(this::queuePodcast);
        removeBtn.setOnAction(this::removeAction);
        viewNotes.setOnAction(this::viewNotes);
    }

    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        super.updateItem(podcast, empty);

        this.podcast = podcast;

        if (empty || podcast == null) {
            podCastTitle.setVisible(false);
            podcastDuration.setVisible(false);
            podcastPubDate.setVisible(false);
            podcastDuration.setVisible(false);
            HBox temp = new HBox();
            temp.setPrefHeight(55.0);
            setGraphic(temp);
        } else {
            podCastTitle.setVisible(true);
            podCastTitle.setText(podcast.getTitle());
            podcastCover.setImage(new Image(podcast.getImgPath()));
            podcastPubDate.setVisible(true);
            podcastPubDate.setText(podcast.getPubDate());
            podcastDuration.setVisible(true);
            podcastDuration.setText(podcast.getDuration());

            if(podcast.hasNotes()){
                viewNotes.setDisable(false);
            } else {
                //TODO make a hover message saying no notes?
                viewNotes.setDisable(true);
            }

            if(podcast.isQueued()){
                queueBtn.setDisable(true);
            }

            setGraphic(container);
        }


    }

    @FXML
    void queuePodcast(ActionEvent event) {
        //TODO if time allows set button to green on click, or check mark?
        ObservableList<Podcast> queueList = Main.model.getQueueList();
        if(!queueList.contains(podcast)){
            queueList.add(podcast);
            podcast.setQueued(true);
            queueBtn.setDisable(true);
        }
    }

    @FXML
    void removeAction(ActionEvent event) {
        System.out.println("Remove btn");
    }


    @FXML
    void viewNotes(ActionEvent event) {
        System.out.println("View notes");

    }

}

