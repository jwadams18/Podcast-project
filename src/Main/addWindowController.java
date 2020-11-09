package Main;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.util.HashMap;
import java.util.ResourceBundle;

public class addWindowController implements Initializable {

    private Model model = Main.model;
    //Pre-determined heights to use when displaying loadOptions or not
    private final int PRELOAD_HEIGHT = 250;
    private final int POSTLOAD_HEIGHT = 375;
    private int maxNumPodcast;
    @FXML
    private AnchorPane addWindow;

    @FXML
    private TextField rssLink;

    @FXML
    private Button okBtn;

    @FXML
    private Pane loadOptions;

    @FXML
    private RadioButton loadAll;

    @FXML
    private ToggleGroup loadOption;

    @FXML
    private RadioButton loadSome;

    @FXML
    private RadioButton loadSingle;

    @FXML
    private Pane podcastCountPane;

    @FXML
    private Slider podcastCountSlider;

    @FXML
    private Button searchBtn;

    @FXML
    private Label podcastNameDisplay;

    @FXML
    private Spinner<Integer> podcastCountSpinner;

    @FXML
    private Label podcastCount;
    //TODO make default button
    //TODO clean up design

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(model.ADD_WINDOW));
        model.setAddWindowController(fxmlLoader);
        model.setSecondaryController(this);

        //Will be disabled until valid link provided, or the user can use cancel btn
        okBtn.setDisable(true);

        //TODO Do I need this? -- USING FOR TESTING -- WILL BE DELETED
        final EventHandler<KeyEvent> keyEventEventHandler =
                keyEvent -> {
            System.out.println(rssLink.getText());
                };
        rssLink.addEventHandler(KeyEvent.KEY_RELEASED, keyEventEventHandler);


        //Hides all load options until a valid link is entered, and adjust height as well
        loadOptions.setVisible(false);
        addWindow.setPrefHeight(PRELOAD_HEIGHT);

    }

    /**
     * Linked to the Ok/Confirm button, will convert the data in the HashTable in model, to
     * actual podcast objects
     * @param event
     */
    @FXML
    void confirmAction(ActionEvent event){
        //TODO Implement
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("src/Main/resources/fxml/popup.fxml"));

        closeAction(event);
    }


    /**
     * Closes the add window
     * @param event
     */
    @FXML
    void closeAction(ActionEvent event) {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Attempts to load the URL entered in the search box, if URL is loaded then
     * changes default button to ok from search btn extends the scene to allow for loading options.
     * Populates the radio buttons to provide relevant numbers for loading the podcast
     */
    @FXML
    void loadUrl() {

        boolean validLink = model.loadData(rssLink.getText());


        if(validLink) {
            //Modifies scene for loading options
            okBtn.setDisable(false);
            searchBtn.setDefaultButton(false);
            okBtn.setDefaultButton(true);
            addWindow.getScene().getWindow().setHeight(POSTLOAD_HEIGHT);
            loadOptions.setVisible(true);

            HashMap<String, NodeList> data = model.getMostRecentRSSData();
            loadAll.setText("Load " + data.get("title").getLength() + " episode(s)");

            //Creates spinner to define the number of podcast to load
            maxNumPodcast = data.get("title").getLength();
            podcastCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxNumPodcast, maxNumPodcast / 2, 1));
            podcastCountSpinner.setEditable(true);

            //TODO remove once comfortable that spinner is working right
            podcastCountSlider.setMin(1);
            podcastCountSlider.setMax(data.get("title").getLength() - 1);
            podcastCountSlider.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                    event -> {
                        loadSome.setText("Load " + (int) podcastCountSlider.getValue() + " episode(s)");
                    });
            podcastCountSlider.addEventHandler(KeyEvent.KEY_PRESSED,
                    event -> {
                        loadSome.setText("Load " + (int) podcastCountSlider.getValue() + " episode(s)");
                    });
            podcastCountSlider.setValue(data.get("title").getLength() / 2);
            loadSome.setText("Load " + (int) podcastCountSlider.getValue() + " episode(s)");
            //END SLIDER CODE THAT WILL BE REMOVED

            //Sets the label above radio buttons to podcast linked in textbox
            podcastNameDisplay.setText(model.getMostRecentPodcast());
        } else {
            //Basic link correction
            //TODO Change to shake with red-outline if time allows for design
            if(!(rssLink.getText().startsWith("https://") || rssLink.getText().startsWith("http://"))){
                rssLink.setText("https://"+rssLink.getText());
            }
        }
    }

    /**
     * Used to connect data from this controller to main window
     * @return the text in the textfield of the add window
     */
    public String getRssFeed(){
        return rssLink.getText().trim();
    }

    /**
     * Used to connect data from this controller to main window
     * @return the selected value from the loadOptions radio buttons
     */
    public int getPodcastCount(){
        if(loadSingle.isSelected()){
            return 1;
        } else if(loadSome.isSelected()){
            return podcastCountSpinner.getValue();
        } else {
            return maxNumPodcast;
        }
    }
}