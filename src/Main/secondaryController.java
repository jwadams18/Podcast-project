package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class secondaryController implements Initializable {

    private Model model = Main.model;
    @FXML
    private TextField rssLink;

    @FXML
    private CheckBox subscribed;

    @FXML
    private Button okBtn;
    //TODO make default button
    //TODO clean up design

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("src/Main/addWindow.fxml"));
        model.setAddWindowController(fxmlLoader);
        model.setSecondaryController(this);
    }

    @FXML
    void closeAction(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

    public String getRssFeed(){
        return rssLink.getText().trim();
    }
}
