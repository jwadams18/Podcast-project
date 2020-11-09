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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    private Model m = Main.model;
    private final ObservableList<Podcast> libraryList = FXCollections.observableArrayList(Podcast.extractor);
    private ChangeListener<Podcast> libraryChangeListener;
    private Podcast selectedPodcast;

    @FXML
    private Button backBtn;

    @FXML
    private Button addBtn;

    @FXML
    private ListView<Podcast> libraryView;

    @FXML
    private AnchorPane listCover;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Stores controller in model, so we can transfer data between if needed
        m.setLibController(this);

        //Sets the 'format' for listView
        libraryView.setCellFactory(new LibCellFactory());
        libraryView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Fills with sample data from model
//        Model.fillSampleData(libraryList);

        //Sorts list based on comparator defined below
        SortedList<Podcast> sortedList = new SortedList<>(libraryList);

        sortedList.setComparator((Podcast1, Podcast2) -> {
            int result = Podcast1.getAuthor().compareToIgnoreCase(Podcast2.getAuthor());

            if(result == 0){
                result = Podcast1.getTitle().compareToIgnoreCase(Podcast2.getTitle());
            }
            return result;
        });

        libraryView.setItems(sortedList);


        if(libraryView.getItems().size() == 0){
            listCover.setVisible(true);
            addBtn.setVisible(false);
        } else {
            listCover.setVisible(false);
            libraryView.getSelectionModel().selectFirst();
        }


    }

    @FXML
    public void addPodcast(ActionEvent event) {
        Parent root;
        try {
            //Loads pop-up window using addWindow.fxml
            root = FXMLLoader.load(new File(m.ADD_WINDOW).toURL());
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
    }

    @FXML
    public void backAction(ActionEvent event) {
        System.out.println("hello world");
    }

}