package Main;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private Model m = Main.model;
    private boolean isPlaying;
    private ChangeListener<Podcast> podcastChangeListener;
    private Podcast selectedPodcast;

    @FXML
    //List of podcast
    private ListView<Podcast> queueView;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView rewindBtnIcon;

    @FXML
    private Button toggleBtn;

    @FXML
    private ImageView toggleBtnIcon;

    @FXML
    private Button forwardBtn;

    @FXML
    private ImageView forwardBtnIcon;

    @FXML
    private ImageView podcastCover;

    @FXML
    private Button libBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Label noteTitle;

    @FXML
    private TextArea noteArea;

    @FXML
    private AnchorPane notesCover;

    @FXML
    private SplitPane splitPane;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Loads the fxml files for sample scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        m.setMainWindow(this);

        //Pre-loads library so the library controller can be used before viewing the library
        loadExternalScenes();
        //Sets the "factory" that will make each new listView cell, set selection to single
        queueView.setCellFactory(new PodcastCellFactory());
        queueView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Sorts list based on comparator defined below
        SortedList<Podcast> sortedList = new SortedList<>(m.getQueueList());

        //Defines how to sort podcast
        sortedList.setComparator((Podcast1, Podcast2) -> {
            int result = Podcast1.getAuthor().compareToIgnoreCase(Podcast2.getAuthor());

            if(result == 0){
                result = Podcast1.getTitle().compareToIgnoreCase(Podcast2.getTitle());
            }
            return result;
        });
        //Data is put into listView
        queueView.setItems(sortedList);

        //Listener that will update info (notes, title, podcast cover)
        queueView.getSelectionModel().selectedItemProperty().addListener(
                podcastChangeListener = ((observableValue, oldValues, newValue) -> {
                    System.out.println(observableValue.getValue());
                    //Need to stop playing if one is playing
                    if(oldValues != null && oldValues.isPlaying()){
                        oldValues.togglePlaying();
                    }

                    //Re-enable buttons if the list was previously empty
                    if(newValue != null){

                        if(oldValues != null){
                            System.err.println("Saving notes!");
                            oldValues.setNotes(noteArea.getText());
                        }

                        //Toggles what is visible/disabled when podcast is selected
                        notesCover.setVisible(false);
                        toggleBtn.setDisable(false);
                        forwardBtn.setDisable(false);
                        backBtn.setDisable(false);

                        System.out.println("Selected item: "+newValue+" current progress "+newValue.getProgress());
                        selectedPodcast = newValue;
                        noteTitle.setVisible(true);
                        noteTitle.setText(selectedPodcast.getTitle()+" notes");
                        //TODO need to make listener to auto-save the text for the notes
                        noteArea.setWrapText(true);
                        noteArea.setPrefRowCount(25);
                        if(selectedPodcast.getNotes() != null){
                            noteArea.setText(selectedPodcast.getNotes());
                        } else {
                            noteArea.setText("Write your first note!");
                        }
                        Image img = new Image(selectedPodcast.getImgPath());
                        podcastCover.setImage(img);
                        podcastCover.setPreserveRatio(true);
                        podcastCover.setSmooth(true);
                        podcastCover.setFitHeight(145);
                        podcastCover.setFitWidth(200);
                        podcastCover.setVisible(true);
                    } else {
                        noteTitle.setVisible(false);
                        noteArea.setText("No podcast selected!");
                        podcastCover.setVisible(false);
                        notesCover.setVisible(true);

                    }
                }));
        //Set default selection to first
        queueView.getSelectionModel().selectFirst();

        //If list is empty then buttons will disable to prevent weird cases
        if(queueView.getSelectionModel().getSelectedItem() == null){
            toggleBtn.setDisable(true);
            forwardBtn.setDisable(true);
            backBtn.setDisable(true);
        } else {
            toggleBtn.setDisable(false);
            forwardBtn.setDisable(false);
            backBtn.setDisable(false);
        }
    }

    public void addBtn(ActionEvent event) {
        Parent root;
        try {
            //Loads pop-up window using addWindow.fxml
            root = FXMLLoader.load(getClass().getResource(m.ADD_WINDOW_PATH));
            Stage stage = new Stage();
            stage.setTitle("Add new feed");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //Connected to the model to get the controller of the add window to get the string entered as the "rssfeed"
        String rssFeed = m.getAddWindow().getRssFeed();
        queueView.getSelectionModel().selectFirst();
        System.out.println("[Controller] The link entered in the secondary window was: "+rssFeed);
    }

    /**
     * Opens the library scene on click of library button
     * @param event
     */
    public void libBtn(ActionEvent event){
        Parent root;
        try {
            //Loads library scene using library.fxml
            root = FXMLLoader.load(getClass().getResource(m.LIB_WINDOW_PATH));
            Stage stage = new Stage();
            stage.setTitle("My Library");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Toggles the playing status of the podcast
     * @param event
     */
    public void toggleBtn(ActionEvent event){
        Image img = null;

        //Handles null case
        if(selectedPodcast == null){
            toggleBtn.setDisable(true);
            return;
        } else {
            toggleBtn.setDisable(false);
        }

        //Displays correct icon based on isPlaying status
        if(isPlaying){
            img = new Image(getClass().getResource("resources/playIcon.png").toExternalForm());
        } else {
            img = new Image(getClass().getResource("resources/pause.png").toExternalForm());
        }

        //Updates the listView to display the playing icon
        queueView.getSelectionModel().selectedItemProperty().removeListener(podcastChangeListener);
        selectedPodcast.togglePlaying();
        isPlaying = !isPlaying;
        toggleBtnIcon.setImage(img);
        queueView.getSelectionModel().selectedItemProperty().addListener(podcastChangeListener);

        System.out.println("This is the toggle play/pause btn. isPlaying: "+isPlaying);
    }

    public void backBtn(ActionEvent event){
        System.out.println("This is the back btn");
    }

    /**
     * Used to load scenes other than the main queue scene (sample.fxml)
     */
    public void loadExternalScenes(){
        try {
            FXMLLoader.load(getClass().getResource(m.LIB_WINDOW_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used when a podcast is added in an external scene
     */
    public void setSelection(){
        queueView.getSelectionModel().selectFirst();
    }

    /**
     * Brings the queue scene to the front
     */
    public void setOnTop(){
        Stage s = (Stage) backBtn.getScene().getWindow();
        s.toFront();
    }


}
