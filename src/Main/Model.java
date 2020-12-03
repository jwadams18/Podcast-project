package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Model {

    public final String ADD_WINDOW_PATH = "resources/fxml/addWindow.fxml";
    public final String LIB_WINDOW_PATH = "resources/fxml/library.fxml";
    public final String POPUP_WINDOW_PATH = "resources/fxml/popup.fxml";
    public final String PODCAST_CELL_PATH = "resources/fxml/PodcastCell.fxml";
    public final String LIBRARY_VIEWCELL_PATH = "resources/fxml/libListViewCell.fxml";
    public final String NOTES_VIEW_PATH = "resources/fxml/notesView.fxml";

    public final String DEFAULT_NOTES = "Write your first notes here!";
    public final String PODCAST_DELETE = "PODCAST DELETE";
    public final String PODCAST_DELETE_MP3 = "DELETE MP3";
    public final String PODCAST_DELETE_NOTES = "DELETE NOTES";

    public final boolean DEBUG = true;


//    private FXMLLoader mainLoader, secondaryLoader;
    private Controller mainWindow;
    private addWindowController addWindow;
    private LibraryController libController;
    private NotesViewController notesViewController;
    private PopupController popupWindow;

    private HashMap<String, NodeList> mostRecentRSSData = new HashMap<>();
    private String mostRecentPodcast;

    private ObservableList<Podcast> podcastList = FXCollections.observableArrayList(Podcast.extractor);
    private ObservableList<Podcast> queueList = FXCollections.observableArrayList(Podcast.extractor);

    public boolean loadData(String urlEntry) {

        boolean validLink = true;

        NodeList titleList = null;
        NodeList authorList = null;
        NodeList epiList = null;
        NodeList imgList = null;
        NodeList enclosureList = null;
        NodeList durationList = null;
        NodeList pubDateList = null;

        try {
            URL url = new URL(urlEntry);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(DEBUG)
            System.out.println("[Model] "+conn.getResponseCode());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            final InputStream input = url.openStream();

            Document document = builder.parse(input);
            document.getDocumentElement().normalize();

            //Gets all the data from rss feed
            titleList = document.getElementsByTagName("title");
            authorList = document.getElementsByTagName("itunes:author");
            imgList = document.getElementsByTagName("itunes:image");
            enclosureList = document.getElementsByTagName("enclosure");
            durationList = document.getElementsByTagName("itunes:duration");
            pubDateList = document.getElementsByTagName("pubDate");

            //Stores all data in a hashmap
            mostRecentPodcast = titleList.item(0).getTextContent();
            mostRecentRSSData.put("title", titleList);
            mostRecentRSSData.put("author", authorList);
            mostRecentRSSData.put("image", imgList);
            mostRecentRSSData.put("enclosure", enclosureList);
            mostRecentRSSData.put("duration", durationList);
            mostRecentRSSData.put("pubDate", pubDateList);

            input.close();

        } catch (ParserConfigurationException | IOException e) {
            System.err.println("[Model] PCE/IO Exception");
            validLink = false;
        } catch (SAXException e) {
            System.err.println("[Model] SAX Exception");
            validLink = false;
        }

        return validLink;
    }

    /**
     * Brings window to front if other windows have covered the main scene
     */
    public void setOnTop(Button object) {
        Stage s = (Stage) object.getScene().getWindow();
        s.toFront();
    }

    public void deletePodcast(Podcast podcast){
        this.queueList.remove(podcast);
        this.podcastList.remove(podcast);
    }

    public Controller getMainWindow() { return mainWindow; }

    public void setMainWindow(Controller mainWindow) { this.mainWindow = mainWindow; }

    public addWindowController getAddWindow() {
        return addWindow;
    }

    public void setAddWindow(addWindowController addWindow) {
        this.addWindow = addWindow;
    }

    public LibraryController getLibController() {
        return this.libController;
    }

    public void setLibController(LibraryController libController) {
        this.libController = libController;
    }

    public void setMostRecentNotes(NotesViewController nvc){
        this.notesViewController = nvc;
    }

    public NotesViewController getNotesViewController() { return this.notesViewController; }

    public void setPopupWindow(PopupController pu) { this.popupWindow = pu; }

    public PopupController getPopupWindow() { return this.popupWindow; }

    public HashMap<String, NodeList> getMostRecentRSSData() {
        return mostRecentRSSData;
    }

    public String getMostRecentPodcast() {
        return mostRecentPodcast;
    }

    public ObservableList<Podcast> getPodcastList() {
        return podcastList;
    }

    public ObservableList<Podcast> getQueueList() {
        return queueList;
    }

}
