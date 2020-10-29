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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private Model m = Main.model;
    private boolean isPlaying;

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
    private Button searchBtn;

    @FXML
    private Label nowPlayingLabel;

    @FXML
    private Button addBtn;
    //TODO need to set some default value, possibly first in list
    @FXML
    private Label noteTitle;

    @FXML
    private TextArea noteArea;


    //TODO IDEA: change search into Library where all podcast are listed

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        m.setMainController(fxmlLoader);
        /*
         * if "fx:controller" is not set in fxml
         * fxmlLoader.setController(NewWindowController);
         */

        queueView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Model.fillSampleData(queueList);
        SortedList<Podcast> sortedList = new SortedList<>(queueList);

        sortedList.setComparator((Podcast1, Podcast2) -> {
            int result = Podcast1.getAuthor().compareToIgnoreCase(Podcast2.getAuthor());

            if(result == 0){
                result = Podcast1.getTitle().compareToIgnoreCase(Podcast2.getTitle());
            }
            return result;
        });
        queueView.setItems(sortedList);

        queueView.getSelectionModel().selectedItemProperty().addListener(
                podcastChangeListener = ((observableValue, oldValues, newValue) -> {
                    System.out.println("Selected item: "+newValue);
                    selectedPodcast = newValue;

                    //TODO set properties here
                    noteTitle.setText(selectedPodcast.getTitle()+" notes");
                    //TODO need to make listener to auto-save the text for the notes
                    noteArea.setText(selectedPodcast.getNoteArea().getText());
                }));
    }

    public void foo(ActionEvent actionEvent) {
//        Stream<Item> rssFeed = null;
//        try {
//            rssFeed = m.read("http://podcasts.joerogan.net/feed");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<Item> info = rssFeed.collect(Collectors.toList());
//        for(Item itm : info){
//            System.out.println(itm.getLink());
//        }
//
//        hiLabel.setText("Loaded "+info.size()+" podcast from {link}");
    }

    public void searchBtn(ActionEvent event){
        System.out.println("This is the search btn");
    }

    public void addBtn(ActionEvent event){
        System.out.println("This is the add btn");
        Parent root;
        try {
            root = FXMLLoader.load(new File("src/Main/addWindow.fxml").toURL());
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
        String rssFeed = m.getSecondaryController().getRssFeed();
        System.out.println("The link entered in the secondary window was: "+rssFeed);
    }

    public void skipBtn(ActionEvent event){
        System.out.println("This is the skip button");
    }

    public void toggleBtn(ActionEvent event){
        //TODO find why icons aren't loading
        Image img;
        if(isPlaying){
            img = new Image("resources/playIcon.png");
        } else {
            img = new Image("resources/pause.png");
        }
        isPlaying = !isPlaying;
        toggleBtnIcon.setImage(img);

        System.out.println("This is the toggle play/pause btn. isPlaying: "+isPlaying);
    }

    public void backBtn(ActionEvent event){
        System.out.println("This is the back btn");
    }
}
