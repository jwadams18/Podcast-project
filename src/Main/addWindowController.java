package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
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
    private CheckBox addToQueue;

    @FXML
    private Label podcastCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(model.ADD_WINDOW_PATH));
        model.setAddWindow(this); //Used to get back to this controller if needed

        //Will be disabled until valid link provided, or the user can use cancel btn
        okBtn.setDisable(true);
        addToQueue.setVisible(false);

        //Hides all load options until a valid link is entered, and adjust height as well
        loadOptions.setVisible(false);
        addWindow.setPrefHeight(PRELOAD_HEIGHT);

    }

    /**
     * Linked to the Ok/Confirm button, will convert the data in the HashTable in model, to
     * actual podcast objects and add to library
     * @param event
     */
    @FXML
    void confirmAction(ActionEvent event){
        //Gets settings from raddioButtons and checkbox for "auto-queue"
        int numToLoad = getPodcastCount();
        boolean autoQueue = addToQueue.isSelected();
        HashMap<String, NodeList> rssData = model.getMostRecentRSSData(); //Gets all loaded data from RSS

        //Pulls all the data from Hashmap
        NodeList titleList = rssData.get("title");
        NodeList authorList = rssData.get("author");
        NodeList imgList = rssData.get("image");
        NodeList enclosureList = rssData.get("enclosure");
        NodeList durationList = rssData.get("duration");
        NodeList pubDateList = rssData.get("pubDate");

        //Debug messages
        if(model.DEBUG){
            System.out.println("[AddWindow:111] building "+numToLoad+" podcast");
            System.out.println("[AddWindow:112] Titles: "+titleList.getLength());
            System.out.println("[AddWindow:113] Authors: "+authorList.getLength());
            System.out.println("[AddWindow:114] Images: "+imgList.getLength());
            System.out.println("[AddWindow:115] Durations: "+durationList.getLength());
        }


        //Creates podcast using rss data
        for(int i = 0; i<numToLoad; i++){

            Podcast temp = new Podcast(titleList.item(i+1).getTextContent(),authorList.item(i+1).getTextContent()
                    , imgList.item(0).getAttributes().getNamedItem("href").getTextContent(),
                    enclosureList.item(i), durationList.item(i).getTextContent(), pubDateList.item(i).getTextContent());

            //if checkbox is selected will add to queuelist, sets the podcast to queued, finally selects podcast in listView
            if(autoQueue){
                model.getQueueList().add(temp);
                temp.setQueued(true);
                model.getMainWindow().setSelection();
            }
            //Prevents duplicate podcast
            if(!model.getPodcastList().contains(temp))
                model.getPodcastList().add(temp);

            //Debug message to confirm correct info
            if(model.DEBUG)
            System.out.println("[AddWindow] "+temp.dump());
        }

        //Closes window and un-covers Library since there is now a podcast in the list
        closeAction(event);
        model.getLibController().setListVisible(true);
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

        //validLink = true if no errors where thrown when "searching" for link
        if(validLink) {
            //Modifies scene for loading options, re-enables buttons that were disabled
            searchBtn.setDisable(true);
            okBtn.setDisable(false);
            searchBtn.setDefaultButton(false);
            okBtn.setDefaultButton(true);
            addWindow.getScene().getWindow().setHeight(POSTLOAD_HEIGHT);
            loadOptions.setVisible(true);
            addToQueue.setVisible(true);

            HashMap<String, NodeList> data = model.getMostRecentRSSData();
//            loadAll.setText("Load " + data.get("title").getLength() + " episode(s)");

            //Creates spinner to define the number of podcast to load
            maxNumPodcast = data.get("title").getLength();
            podcastCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxNumPodcast, maxNumPodcast / 2, 1));
            podcastCountSpinner.setEditable(true);

            //Sets the label above radio buttons to podcast linked in textbox
            podcastNameDisplay.setText(model.getMostRecentPodcast());
        } else {
            //Basic link correction
            //TODO Change to shake with red-outline if time allows
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
