package Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.*;
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


    private FXMLLoader mainLoader, secondaryLoader;
    private addWindowController addWindow;
    private LibraryController libController;

    private HashMap<String, NodeList> mostRecentRSSData = new HashMap<>();
    private String mostRecentPodcast;

    private ObservableList<Podcast> podcastList = FXCollections.observableArrayList(Podcast.extractor);
    private ObservableList<Podcast> queueList = FXCollections.observableArrayList(Podcast.extractor);

    public boolean loadData(String urlEntry){

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
            System.out.println(conn.getResponseCode());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            final InputStream input = url.openStream();

            Document document = builder.parse(input);
            document.getDocumentElement().normalize();

            //TODO START UNUSED CODE, SOON TO BE DELETED
//            Node startingPoint = document.getFirstChild();
//            System.out.println(startingPoint.getTextContent());
//            while(startingPoint.getNextSibling() != null){
//                System.out.println(startingPoint.getNodeName());
//                startingPoint.getNextSibling();
//            }
//            NodeList test = document.getElementsByTagName("item");
//            System.out.println(test.getLength());
//            for(int i = 0; i<test.getLength(); i++){
//                System.out.println(test.item(i).getChildNodes().item(0).getTextContent());
//            }
            //END UNUSED CODE


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
            System.err.println("PCE/IO Exception");
            validLink = false;
        } catch (SAXException e) {
            System.err.println("SAX Exception");
            validLink = false;
        }

        return validLink;
//        try{
//
//            mostRecentRSSData.put("title", titleList);
////            for(int i = 0; i<titleList.getLength();i++){
////                System.out.println(titleList.getLength()+" "+titleList.item(i).getTextContent());
////            }
//
//        } catch (NullPointerException ex){
//            System.err.println("Title not found");
//        }
//        try{
//            mostRecentRSSData.put("author", authorList);
//            System.out.println(authorList.item(0).getChildNodes().item(0).getTextContent());
//        } catch (NullPointerException ex){
//            System.err.println("Author not found");
//        }
//        try{
//            mostRecentRSSData.put("images", imgList);
//            System.out.println(imgList.item(0).getAttributes().getNamedItem("href").getTextContent());
//        } catch (NullPointerException ex){
//            System.err.println("Podcast Icon not found");
//        }
//        try{
//            mostRecentRSSData.put("enclosures", enclosureList);
//            if(enclosureList.getLength() == 0){
//                System.err.println("No mp3 files found, please use a different link!");
//            }
//            for(int i = 0; i<enclosureList.getLength();i++){
//                System.out.println(enclosureList.getLength()+" "+enclosureList.item(i).getAttributes().getNamedItem("url").getTextContent());
//            }
//        } catch (NullPointerException ex){
//            System.err.println("Podcast link not found");
//        }
//        try{
//            System.out.println(enclosureList.item(0).getAttributes().getNamedItem("length").getTextContent());
//        } catch (NullPointerException ignored){}
    }

    public FXMLLoader getMainLoader() {
        return mainLoader;
    }

    public void setMainLoader(FXMLLoader mainLoader) {
        this.mainLoader = mainLoader;
    }

    public FXMLLoader getSecondaryLoader() {
        return secondaryLoader;
    }

    public void setSecondaryLoader(FXMLLoader secondaryLoader) {
        this.secondaryLoader = secondaryLoader;
    }

    public addWindowController getAddWindow() {
        return addWindow;
    }

    public void setAddWindow(addWindowController addWindow) {
        this.addWindow = addWindow;
    }

    public LibraryController getLibController() {
        return libController;
    }

    public void setLibController(LibraryController libController) {
        this.libController = libController;
    }

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
