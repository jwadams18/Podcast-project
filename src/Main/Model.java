package Main;
import javafx.collections.ObservableList;
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

    public final String ADD_WINDOW = "src/Main/resources/fxml/addWindow.fxml";
    public final String LIB_WINDOW = "src/Main/resources/fxml/library.fxml";


    private FXMLLoader mainLoader, secondaryLoader;
    private addWindowController addWindow;

    private HashMap<String, NodeList> mostRecentRSSData = new HashMap<>();
    private String mostRecentPodcast;

    public Model(){

    }

    public static void fillSampleData(ObservableList<Podcast> backingList) {
        backingList.add(new Podcast("Name 1", "Author 1", new JTextArea("Welcome to the new season!"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 2", "Author 2",  new JTextArea("Episode one was much better!"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 3", "Author 3",  new JTextArea("When is the new season coming out?"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 4", "Author 4",  new JTextArea("LAST EPISODE!"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 5", "Author 5", new JTextArea("SEASON 2!!"), "resources/playIcon.png"));
    }

    public boolean loadData(String urlEntry){

        boolean validLink = true;

        NodeList titleList = null;
        NodeList authorList = null;
        NodeList epiList = null;
        NodeList imgList = null;
        NodeList enclosureList = null;

        try {
            URL url = new URL(urlEntry);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println(conn.getResponseCode());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            final InputStream input = url.openStream();

            Document document = builder.parse(input);
            document.getDocumentElement().normalize();

            Node startingPoint = document.getFirstChild();
//            System.out.println(startingPoint.getTextContent());
//            while(startingPoint.getNextSibling() != null){
//                System.out.println(startingPoint.getNodeName());
//                startingPoint.getNextSibling();
//            }
            NodeList test = document.getElementsByTagName("item");
            System.out.println(test.getLength());
//            for(int i = 0; i<test.getLength(); i++){
//                System.out.println(test.item(i).getChildNodes().item(0).getTextContent());
//            }

            titleList = document.getElementsByTagName("title");
            authorList = document.getElementsByTagName("itunes:author");
            imgList = document.getElementsByTagName("itunes:image");
            enclosureList = document.getElementsByTagName("enclosure");

            mostRecentPodcast = titleList.item(0).getTextContent();
            mostRecentRSSData.put("title", titleList);
            mostRecentRSSData.put("author", authorList);
            mostRecentRSSData.put("image", imgList);
            mostRecentRSSData.put("enclosures", enclosureList);


            input.close();

        } catch (ParserConfigurationException | IOException e) {
            validLink = false;
            e.printStackTrace();
        } catch (SAXException e) {
            validLink = false;
            System.err.println("Bad link");
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

    public void setMainController(FXMLLoader fxmlCtr){
        this.mainLoader = fxmlCtr;
    }

    public FXMLLoader getMainController(){
        return this.mainLoader;
    }

    public void setAddWindowController(FXMLLoader fxmlCntr){
        this.secondaryLoader = fxmlCntr;
    }

    public FXMLLoader getAddWindowController(){
        return secondaryLoader;
    }

    public void setSecondaryController(addWindowController tst){
        this.addWindow = tst;
    }

    public addWindowController getSecondaryController(){
        return this.addWindow;
    }

    public HashMap<String, NodeList> getMostRecentRSSData() {
        return mostRecentRSSData;
    }

    public void setMostRecentRSSData(HashMap<String, NodeList> mostRecentRSSData) {
        this.mostRecentRSSData = mostRecentRSSData;
    }

    public String getMostRecentPodcast() {
        return mostRecentPodcast;
    }

    public void setMostRecentPodcast(String mostRecentPodcast) {
        this.mostRecentPodcast = mostRecentPodcast;
    }
}
