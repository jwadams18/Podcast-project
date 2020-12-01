package Main;

import Main.Main;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.tools.Tool;
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
    private Model model = Main.model;

    public LibCell() {
        loadFXML();
    }

    public void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(model.LIBRARY_VIEWCELL_PATH));
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
        boolean wasEmpty = isEmpty();
        super.updateItem(podcast, empty);

        this.podcast = podcast;

        final ChangeListener<Boolean> changeListener =(observableValue, oldValue, newValue) -> {
            System.out.println("["+getClass().getName()+"] The observableValue has " + "changed: oldValue = " + oldValue + ", newValue = " + newValue);
        };

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
            podcastPubDate.setText("Published: "+podcast.getPubDate());
            podcastDuration.setVisible(true);
            podcastDuration.setText(podcast.getDuration());

            Tooltip.install(removeBtn, new Tooltip("Click for more options"));

            if(wasEmpty != empty){
                this.podcast.hasNotesProperty().addListener(changeListener);
                this.podcast.isQueuedProperty().addListener(changeListener);
                queueBtn.disableProperty().bind(this.podcast.isQueuedProperty());
                viewNotes.disableProperty().bind(this.podcast.hasNotesProperty());
            }

            if(podcast.hasNotes()){
                Tooltip.install(viewNotes, new Tooltip("View the notes for this podcast"));
            }

            if(!podcast.isQueued()){
                Tooltip.install(queueBtn, new Tooltip("Add this podcast to your queue!"));
            }

            setGraphic(container);
        }


    }

    @FXML
    void queuePodcast(ActionEvent event) {
        //TODO hover green
        ObservableList<Podcast> queueList = model.getQueueList();

        if(!queueList.contains(podcast)){
            queueList.add(podcast);
            podcast.setQueued(true);
            model.getMainWindow().selectPodcast(podcast);
        }
    }

    @FXML
    void removeAction(ActionEvent event) {

        cm.setAutoHide(true);

        MenuItem removeQueue = new MenuItem("From queue");
        removeQueue.setOnAction(this::removeFromQueue);
        MenuItem removeNotes = new MenuItem("Notes");
        removeNotes.setOnAction(this::deleteNotes);
        MenuItem deletePod = new MenuItem("Delete podcast");
        deletePod.setOnAction(this::deletePodcast);
        MenuItem deleteMP3 = new MenuItem("Delete MP3 file");
        deleteMP3.setOnAction(this::deletePodcastMP3);

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
        cm.getItems().addAll(deleteMP3, deletePod);

        if(!cm.isShowing()) {
            removeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> cm.show(removeBtn, mouseEvent.getScreenX()
                    , mouseEvent.getScreenY()));
        }
    }


    @FXML
    void viewNotes(ActionEvent event) {
        Parent root;
        try {
            //Loads library scene using library.fxml
            root = FXMLLoader.load(getClass().getResource(model.NOTES_VIEW_PATH));
            model.getNotesViewController().setPodcast(this.podcast);
            Stage stage = new Stage();
            stage.setTitle(this.podcast.getTitle()+" notes");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Removes the podcast from the queueList
     * @param event
     */
    void removeFromQueue(ActionEvent event){
        model.getQueueList().remove(podcast);
        podcast.setQueued(false);
        cm.hide();
    }

    void deleteNotes(ActionEvent event){
        model.getPopupWindow().show(model.PODCAST_DELETE_NOTES, this.podcast);
        cm.hide();
    }

    void deletePodcast(ActionEvent event){
        model.getPopupWindow().show(model.PODCAST_DELETE, this.podcast);
        cm.hide();
    }

    void deletePodcastMP3(ActionEvent event){
        model.getPopupWindow().show(model.PODCAST_DELETE_MP3, this.podcast);
        cm.hide();
    }

}

