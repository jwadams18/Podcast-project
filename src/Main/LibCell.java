package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LibCell extends ListCell<Podcast> {

    @FXML
    private ImageView podcastCover;

    @FXML
    private Label podCastTitle;

    @FXML
    private Button queueBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Label podcastAuthor;

    @FXML
    private Label podcastDuration;


    public LibCell(){
        loadFXML();
    }

    public void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Main.model.LIBRARY_VIEWCELL_PATH));
            loader.setController(this);
//            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        queueBtn.setOnAction(this::queuePodcast);
        queueBtn.setOnAction(this::removeAction);
    }

    @Override
    protected void updateItem(Podcast podcast, boolean empty) {
        super.updateItem(podcast, empty);

        }

    @FXML
    void queuePodcast(ActionEvent event) {

    }

    @FXML
    void removeAction(ActionEvent event) {

    }

}

