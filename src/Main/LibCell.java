package Main;

import Main.Main;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private ContextMenu cm;

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
        cm = new ContextMenu();
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
        //TODO When removing a podcast from listview its not re-enabling queue btn in lib
        //TODO if time allows set button to green on click, or check mark?
        ObservableList<Podcast> queueList = Main.model.getQueueList();

        queueBtn.disableProperty().bind(this.podcast.isQueuedProperty());

        if(!queueList.contains(podcast)){
            queueList.add(podcast);
            podcast.setQueued(true);
            queueBtn.setDisable(true);
        }
    }

    @FXML
    void removeAction(ActionEvent event) {

        cm.setAutoHide(true);

        MenuItem removeQueue = new MenuItem("From queue");
        removeQueue.setOnAction(this::removeFromQueue);
        MenuItem removeNotes = new MenuItem("Notes");
        removeNotes.setOnAction(this::removeNotes);
        MenuItem deletePod = new MenuItem("Delete podcast");
        deletePod.setOnAction(this::deletePodcast);

        cm.getItems().clear();

        //Based on the properties of the podcast will determine which options pop-up
        if(podcast.isQueued()){
            cm.getItems().add(removeQueue);
        }
        if(podcast.hasNotes()){
            cm.getItems().add(removeNotes);
        }
        if(podcast.hasNotes() || podcast.isQueued()){
            cm.getItems().add(new SeparatorMenuItem());
        }
        cm.getItems().add(deletePod);

        if(!cm.isShowing()) {
            removeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> cm.show(removeBtn, mouseEvent.getScreenX()
                    , mouseEvent.getScreenY()));
        }
    }


    @FXML
    void viewNotes(ActionEvent event) {
        System.out.println("View notes");

    }

    /**
     * Removes the podcast from the queueList
     * @param event
     */
    void removeFromQueue(ActionEvent event){
        Main.model.getQueueList().remove(podcast);
        queueBtn.setDisable(false);
        podcast.setQueued(false);
        cm.hide();
    }

    void removeNotes(ActionEvent event){
        System.out.println("remove notes");

        cm.hide();
    }

    void deletePodcast(ActionEvent event){
        System.out.println("DELETE");

        cm.hide();
    }

}

