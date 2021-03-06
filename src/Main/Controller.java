package Main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
public class Controller implements Initializable {

    private Model model = Main.model;
    private boolean isPlaying;
    private ChangeListener<Podcast> podcastChangeListener;
    private Podcast selectedPodcast;

    private MediaPlayer player;

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

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider volumeSlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Loads the fxml files for sample scene, sets controller in model
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        model.setMainWindow(this);

        //Pre-loads library so the library controller can be used before viewing the library
        loadExternalScenes();
        //Sets the "factory" that will make each new listView cell, set selection to single
        queueView.setCellFactory(new PodcastCellFactory());
        queueView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Sorts list based on comparator defined below
        SortedList<Podcast> sortedList = new SortedList<>(model.getQueueList());

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

                    if(model.DEBUG)
                    System.out.println("[Controller] "+observableValue.getValue());

                    //Need to stop playing if one is playing
                    if(oldValues != null && oldValues.isPlaying()){
                        oldValues.togglePlaying();
                        isPlaying = false;
                    }

                    //Re-enable buttons if the list was previously empty
                    if(newValue != null){

                        //If there was a previously selected podcast this will run
                        if(oldValues != null){

                            if(model.DEBUG)
                            System.err.println("[Controller] Saving notes!");

                            oldValues.setNotes(noteArea.getText());
                            toggleBtnIcon.setImage(new Image(getClass().getResource("resources/playIcon.png").toExternalForm()));
                        }

                        //Toggles what is visible/disabled when podcast is selected
                        notesCover.setVisible(false);
                        toggleBtn.setDisable(false);
                        forwardBtn.setDisable(false);
                        backBtn.setDisable(false);
                        volumeSlider.setDisable(false);

                        if(model.DEBUG)
                        System.out.println("[Controller] Selected item: "+newValue+" current progress "+newValue.getProgress());

                        selectedPodcast = newValue;
                        //Sets notes title
                        noteTitle.setVisible(true);
                        noteTitle.setText(selectedPodcast.getTitle()+" notes");
                        //TODO need to make save button for notes
                        //Sets notes area
                        noteArea.setWrapText(true);
                        noteArea.setPrefRowCount(25);
                        if(selectedPodcast.getNotes() != null){
                            noteArea.setText(selectedPodcast.getNotes());
                        } else {
                            noteArea.setText("Write your first note!");
                        }

                        //Sets image cover by controls
                        Image img = new Image(selectedPodcast.getImgPath());
                        podcastCover.setImage(img);
                        podcastCover.setPreserveRatio(true);
                        podcastCover.setSmooth(true);
                        podcastCover.setFitHeight(145);
                        podcastCover.setFitWidth(200);
                        podcastCover.setVisible(true);

                        //Loads file into Media type to play
                        File f =  new File("Podcast/"+selectedPodcast.getTitleStringForm()+".mp3");
                        String path = f.getPath();
                        Media m = new Media(new File(path).toURI().toString());
                        player = new MediaPlayer(m);
                        mediaView.setMediaPlayer(player);
                        player.setVolume(1);

                        if(model.DEBUG)
                        System.out.println("[Controller] Playing from: "+player.getStartTime());

                        //Updates the max progress of the podcast if not previously set
                        if(selectedPodcast.getMaxProgress() != null){
                            selectedPodcast.setMaxProgress(m.getDuration());
                        }

                        //Used to update the progress bars
                        //TODO bind in podcast cell as well
                        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                            @Override
                            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration newDuration) {
                                double currentVal = newDuration.toMillis()/ m.getDuration().toMillis();
                                //Keeps the progress of the current player, prevents restarting everytime
                                if(selectedPodcast.getProgress().greaterThan(Duration.ZERO)){
                                    progressBar.setProgress(currentVal);
                                    selectedPodcast.getCellProgressBar().setProgress(currentVal);
                                } else {
                                    progressBar.setProgress(currentVal);
                                    selectedPodcast.getCellProgressBar().setProgress(currentVal);
                                    selectedPodcast.setProgress(newDuration);
                                }

                                if(model.DEBUG);
//                                System.err.println("[CL] "+currentVal);
                            }
                        });
                    } else {
                        //Settings if podcast is null
                        noteTitle.setVisible(false);
                        noteArea.setText("No podcast selected!");
                        podcastCover.setVisible(false);
                        notesCover.setVisible(true);

                    }
                }));
        //Set default selection to first
        queueView.getSelectionModel().selectFirst();

        //Adds listener to slider to maintain volume controls
        volumeSlider.valueProperty().addListener(event -> {
            if(selectedPodcast != null){
                player.setVolume(volumeSlider.getValue());
            }
        });
        volumeSlider.setMax(1);
        volumeSlider.setMin(0);
        volumeSlider.setValue(0.5);

        //If list is empty then buttons will disable to prevent weird cases
        if(queueView.getSelectionModel().getSelectedItem() == null){
            toggleBtn.setDisable(true);
            forwardBtn.setDisable(true);
            backBtn.setDisable(true);
            volumeSlider.setDisable(true);
        } else {
            toggleBtn.setDisable(false);
            forwardBtn.setDisable(false);
            backBtn.setDisable(false);
        }
    }

    /**
     * Displays the pop-up window that prompts the user for an RSS feed
     * @param event
     */
    public void addBtn(ActionEvent event) {
        Parent root;
        try {
            //Loads pop-up window using addWindow.fxml
            root = FXMLLoader.load(getClass().getResource(model.ADD_WINDOW_PATH));
            Stage stage = new Stage();
            stage.setTitle("Add new feed");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/musicnote.png")));
            stage.showAndWait();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //Connected to the model to get the controller of the add window to get the string entered as the "rssfeed"
        String rssFeed = model.getAddWindow().getRssFeed();
        queueView.getSelectionModel().selectFirst();

        if(model.DEBUG)
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
            root = FXMLLoader.load(getClass().getResource(model.LIB_WINDOW_PATH));
            Stage stage = new Stage();
            stage.setTitle("My Library");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/musicnote.png")));
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

        //Handles null case, which disables button, usually used when no podcast is selected
        if(selectedPodcast == null){
            toggleBtn.setDisable(true);
            return;
        } else {
            toggleBtn.setDisable(false);
        }

        //Displays correct icon based on isPlaying status
        if(isPlaying){
            img = new Image(getClass().getResource("resources/playIcon.png").toExternalForm());
            selectedPodcast.setProgress(player.getCurrentTime());
            //mediaView.getMediaPlayer().getMedia().getDuration()

            if(model.DEBUG)
            System.err.println("[Controller] Progress set to: "+player.getCurrentTime());

            player.stop();
        } else {
            //Updates icon from play icon to pause icon, and starts player where is was stopped
            img = new Image(getClass().getResource("resources/pause.png").toExternalForm());
            player.setStartTime(selectedPodcast.getProgress());
            player.play();
        }

        //Updates the listView to display the playing icon
        queueView.getSelectionModel().selectedItemProperty().removeListener(podcastChangeListener);
        selectedPodcast.togglePlaying();
        isPlaying = !isPlaying;
        toggleBtnIcon.setImage(img);
        queueView.getSelectionModel().selectedItemProperty().addListener(podcastChangeListener);

        if(model.DEBUG)
        System.out.println("[Controller] This is the toggle play/pause btn. isPlaying: "+isPlaying);
    }

    /**
     * Skips back 10 seconds for the current podcast, updates for current podcast
     * @param event
     */
    public void backBtn(ActionEvent event){

        player.seek(player.getCurrentTime().subtract(Duration.seconds(10)));
        selectedPodcast.setProgress(player.getCurrentTime());

    }

    /**
     * Skips forward 10 seconds for the current podcast, updates
     * @param event
     */
    @FXML
    public void skipBtn(ActionEvent event){
        if(model.DEBUG)
        System.out.println("[Controller]Before skip: "+player.getCurrentTime().toMillis());

        player.seek(player.getCurrentTime().add(Duration.seconds(10)));
        selectedPodcast.setProgress(player.getCurrentTime());

        if(model.DEBUG)
        System.out.println("[Controller]After skip: "+player.getCurrentTime().toMillis());
    }

    /**
     * Force saves the notes, via a button on interface, also updates property in podcast
     * potentially triggering icon display
     * @param event
     */
    @FXML
    void saveNotesAction(ActionEvent event) {
        selectedPodcast.setNotes(noteArea.getText().trim());
        selectedPodcast.setNotesStatus(true);

        if(model.DEBUG)
        System.out.println("[Controller] Notes saved!");
    }

    /**
     * Used to load scenes other than the main queue scene (sample.fxml)
     */
    public void loadExternalScenes(){
        try {
            FXMLLoader.load(getClass().getResource(model.LIB_WINDOW_PATH));
            FXMLLoader.load(getClass().getResource(model.POPUP_WINDOW_PATH));
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
        s.requestFocus();
    }

    public void selectPodcast(Podcast p){
        queueView.getSelectionModel().select(p);
    }

    /**
     * Used to update notes when changed from library-> view notes button
     */
    public void updateNotes(){
        noteArea.setText(selectedPodcast.getNotes());
    }

    public String getNotes() { return this.noteArea.getText().trim(); }

    // new
    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

}
