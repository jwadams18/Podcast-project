package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    private Model m = Main.model;

    @FXML
    private Button backBtn;

    @FXML
    private Button addBtn;

    @FXML
    private ListView<Podcast> libraryView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        libraryView.setCellFactory(new LibCellFactory());
        libraryView.setItems(null);
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
