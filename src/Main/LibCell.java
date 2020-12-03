package Main;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.io.IOException;
/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
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

        //Sets the action listeners
        queueBtn.setOnAction(this::queuePodcast);
        removeBtn.setOnAction(this::removeAction);
        viewNotes.setOnAction(this::viewNotes);
        cm = new ContextMenu();
    }

    /**
     * Updates the controls in the cell that is displayed in the library scene
     * @param podcast podcast that is being displayed by this cell
     * @param empty
     */
    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        boolean wasEmpty = isEmpty();
        super.updateItem(podcast, empty);

        //Stores the podcast so it can be used later
        this.podcast = podcast;

        //Will be used to monitor settings to display icons etc.
        final ChangeListener<Boolean> changeListener =(observableValue, oldValue, newValue) -> {
            if(model.DEBUG)
            System.out.println("["+getClass().getName()+"] The observableValue has " + "changed: oldValue = " + oldValue + ", newValue = " + newValue);
        };
        //if empty || null hides the libcell components
        if (empty || podcast == null) {
            podCastTitle.setVisible(false);
            podcastDuration.setVisible(false);
            podcastPubDate.setVisible(false);
            podcastDuration.setVisible(false);
            HBox temp = new HBox();
            temp.setPrefHeight(55.0);
            setGraphic(temp);
        } else {

            //Checks if the file is downloaded to the directory
            if(!this.podcast.isDownloaded()){
                queueBtn.setText("Download & Queue");
                queueBtn.setWrapText(true);
            }

            //Fills in the information of the podcast into the cell
            podCastTitle.setVisible(true);
            podCastTitle.setText(podcast.getTitle());
            podcastCover.setImage(new Image(podcast.getImgPath()));
            podcastPubDate.setVisible(true);
            podcastPubDate.setText("Published: "+podcast.getPubDate());
            podcastDuration.setVisible(true);
            podcastDuration.setText(podcast.getDuration());

            //Adds tooltip to remove button
            Tooltip.install(removeBtn, new Tooltip("Click for more options"));

            //If this cell was just added, assign listeners
            //Only done once to prevent wasted resources
            if(wasEmpty != empty){
                this.podcast.hasNotesProperty().addListener(changeListener);
                this.podcast.isQueuedProperty().addListener(changeListener);
                queueBtn.disableProperty().bind(this.podcast.isQueuedProperty());
                viewNotes.disableProperty().bind(this.podcast.hasNotesProperty());
            }

            /*

            Following if statements adds tooltips based on properties
            of the podcast being displayed

             */
            if(podcast.hasNotes()){
                Tooltip.install(viewNotes, new Tooltip("View the notes for this podcast"));
            }

            if(!podcast.isQueued()){
                Tooltip.install(queueBtn, new Tooltip("Add this podcast to your queue!"));
            }

            setGraphic(container);
        }


    }

    /**
     * Will enqueue the podcast from Library
     * @param event
     */
    @FXML
    void queuePodcast(ActionEvent event) {
        ObservableList<Podcast> queueList = model.getQueueList();

        //If the MP3 was deleted, redownloads
        if(!this.podcast.isDownloaded()){
            this.podcast.download();
        }

        //If the podcast isn't in the queue then add
        if(!queueList.contains(podcast)){
            queueList.add(podcast);
            podcast.setQueued(true);
            model.getMainWindow().selectPodcast(podcast);
        }
    }

    /**
     * Based on the current properties of the podcast will display a context menu with viable options
     * @param event
     */
    @FXML
    void removeAction(ActionEvent event) {

        cm.setAutoHide(true);

        //Creates menuItems and adds listeners
        MenuItem removeQueue = new MenuItem("From queue");
        removeQueue.setOnAction(this::removeFromQueue);
        MenuItem removeNotes = new MenuItem("Notes");
        removeNotes.setOnAction(this::deleteNotes);
        MenuItem deletePod = new MenuItem("Delete podcast");
        deletePod.setOnAction(this::deletePodcast);
        MenuItem deleteMP3 = new MenuItem("Delete MP3 file");
        deleteMP3.setOnAction(this::deletePodcastMP3);

        //Removes all previous options from contextMenu
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

    /**
     * Opens new window to view the notes of the Libcell's podcast
     * @param event
     */
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
            stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/musicnote.png")));
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
        podcast.togglePlaying();
        cm.hide();
    }

    /**
     * Prompts the user with a confirm dialog before deleting notes
     * @param event
     */
    void deleteNotes(ActionEvent event){
        cm.hide();
        Alert alert = new Alert(Alert.AlertType.NONE, "Delete notes for "+this.podcast.getTitle()+"?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES){
            if(model.DEBUG)
                System.err.println("[LibCell] Deleting "+this.podcast.getTitle()+"\'s notes");

            this.podcast.setNotes(Main.model.DEFAULT_NOTES);
        } else {
            alert.hide();
        }

    }

    /**
     * Prompts user before deleting the podcast associated with the libcell
     * @param event
     */
    void deletePodcast(ActionEvent event){
        //removes context menu that was displaed
        cm.hide();
        //Creates and shows a pop-up that will prompt user to decide what happens to podcast
        Alert alert = new Alert(Alert.AlertType.NONE, "Delete "+this.podcast.getTitle()+"?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        //CASE: YES, deletes podcast
        if(alert.getResult() == ButtonType.YES){
            if(model.DEBUG)
                System.err.println("[LibCell] Deleting "+this.podcast.getTitle());

            Main.model.deletePodcast(this.podcast);
            //CASE: NO, hides alert
        } else {
            alert.hide();
        }
    }

    /**
     * Prompts user to confirm deletion the podcast's mp3 file
     * @param event
     */
    void deletePodcastMP3(ActionEvent event){
        //removes the context menu that was displayed
        cm.hide();
        //Creates and shows a pop-up box that will prompt the user to decide what happens to the mp3 file
        Alert alert = new Alert(Alert.AlertType.NONE, "Delete the mp3 file for "+this.podcast.getTitle()+"?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        //CASE: YES, removes the podcast from queue since file is deleted and updates status
        if(alert.getResult() == ButtonType.YES){
            if(model.DEBUG)
                System.err.println("[LibCell] Deleting "+this.podcast.getTitle()+"\'s mp3 file");

            this.podcast.deleteMP3();
            this.podcast.setQueued(false);
            model.getQueueList().remove(this.podcast);

            if(this.podcast.isQueued()){
                model.getQueueList().remove(this.podcast);
                model.getMainWindow().setSelection();
            }
            //CASE: NO, hides alert
        } else {
            alert.hide();
        }
    }

}

