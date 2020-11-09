package Main;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private Model m = Main.model;
    private boolean isPlaying;

    /**
     * Used to store data for listView that represents the queue
     */
    private final ObservableList<Podcast> queueList = FXCollections.observableArrayList(Podcast.extractor);
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
    private Label nowPlayingLabel;

    @FXML
    private Button addBtn;

    @FXML
    private Label noteTitle;

    @FXML
    private TextArea noteArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Loads the fxml files for sample scene
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        m.setMainLoader(fxmlLoader);

        //Sets the "factory" that will make each new listView cell, set selection to single
        queueView.setCellFactory(new PodcastCellFactory());
        queueView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Fills will sample data from Model, will be replaced when back-end loading is finished
//        Model.fillSampleData(queueList);
        //Sorts list based on comparator defined below
        SortedList<Podcast> sortedList = new SortedList<>(m.getQueueList());

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
                    System.out.println("Selected item: "+newValue+" current progress "+newValue.getProgress());
                    selectedPodcast = newValue;

                    //TODO set properties here
                    noteTitle.setText(selectedPodcast.getTitle()+" notes");
                    //TODO need to make listener to auto-save the text for the notes
                    noteArea.setText(selectedPodcast.getNoteArea().getText());
                    Image img = new Image(selectedPodcast.getImgPath());
                    podcastCover.setImage(img);
                    podcastCover.setPreserveRatio(true);
                    podcastCover.setSmooth(true);
                    podcastCover.setFitHeight(165);
                    podcastCover.setFitWidth(200);
                }));
        //Set default selection to first
        queueView.getSelectionModel().selectFirst();
    }

    public void addBtn(ActionEvent event) {
        Parent root;
        try {
            //Loads pop-up window using addWindow.fxml
//            root = FXMLLoader.load(new File(m.ADD_WINDOW_PATH).toURL());
            root = FXMLLoader.load(getClass().getResource(m.ADD_WINDOW_PATH));
            Stage stage = new Stage();
            stage.setTitle("Add new feed");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            // Hide this current window (if this is what you want)
//            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //Connected to the model to get the controller of the add window to get the string entered as the "rssfeed"
        String rssFeed = m.getAddWindow().getRssFeed();
        queueView.getSelectionModel().selectFirst();
        System.out.println("The link entered in the secondary window was: "+rssFeed);

        //Will be null if addWindow is closed or cancelled
        //TODO Is this needed?
        if(!rssFeed.isBlank()){
            buildPodcast(rssFeed, m.getAddWindow().getPodcastCount());
        }

    }

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
            // Hide this current window (if this is what you want)
//            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("This is the library button");
    }

    public void toggleBtn(ActionEvent event){
        //TODO find why icons aren't loading
        //TODO make sure when a podcast is playing it is stopped before playing another one
        Image img = null;

        if(isPlaying){
            img = new Image("images/playIcon.png");
        } else {
            img = new Image("images/pause.png");
        }

        isPlaying = !isPlaying;
        toggleBtnIcon.setImage(img);

        System.out.println("This is the toggle play/pause btn. isPlaying: "+isPlaying);
    }

    public void backBtn(ActionEvent event){
        System.out.println("This is the back btn");
    }

    public Podcast buildPodcast(String urlEntry, int numToLoad){


        return null;
    }


}
