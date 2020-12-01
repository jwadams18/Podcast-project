package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PopupController implements Initializable {

    private String popupType;
    private Podcast podcast;
    private Model model = Main.model;

    @FXML
    private Label text;

    @FXML
    private Button leftBtn;

    @FXML
    private Button rightBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main.model.setPopupWindow(this);
    }

    @FXML
    void delete(ActionEvent event) {

        if(this.popupType.equalsIgnoreCase(model.PODCAST_DELETE))
            model.deletePodcast(this.podcast);

        if(this.popupType.equalsIgnoreCase(model.PODCAST_DELETE_MP3))
            podcast.deleteMP3();

        if(this.popupType.equalsIgnoreCase(model.PODCAST_DELETE_NOTES))
            podcast.setNotes(model.DEFAULT_NOTES);

        Stage s = (Stage) leftBtn.getScene().getWindow();
        s.close();
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage s = (Stage) leftBtn.getScene().getWindow();
        s.close();
    }

    public void show(String type, Podcast podcast){
        System.out.println("[PC:62] Made it into show");
        this.popupType = type;
        this.podcast = podcast;
        System.out.println("[PC:65] Set to: "+this.podcast+" "+this.popupType);
        Parent root;
        try {
            //Loads pop-up window using popup.fxml
            root = FXMLLoader.load(getClass().getResource(model.POPUP_WINDOW_PATH));
            Stage stage = new Stage();
            stage.setTitle("Confirm deletion");

            System.out.println(model.PODCAST_DELETE);
            System.out.println(model.PODCAST_DELETE_MP3);
            System.out.println(model.PODCAST_DELETE_NOTES);

            if(type.equalsIgnoreCase(model.PODCAST_DELETE)){
                text.setText("Are you sure you want to delete "+podcast.getTitle()+" from your library?");
            } else if(this.popupType.equalsIgnoreCase(model.PODCAST_DELETE_MP3)) {
                text.setText("Remove "+podcast.getTitle()+".mp3 from your library?");
            } else if(this.popupType.equalsIgnoreCase(model.PODCAST_DELETE_NOTES)) {
                text.setText("Delete notes for "+podcast.getTitle()+"?");
            } else {
                text.setText("Fuck");
            }



            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
