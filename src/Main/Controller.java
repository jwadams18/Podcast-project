package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.swing.*;


public class Controller {

    private Model m;

    public Controller(){
        this.m = new Model();
    }


    @FXML
    //List of podcast
    private ListView<?> queue;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button backBtn;

    @FXML
    private Button toggleBtn;

    @FXML
    private Button forwardBtn;

    @FXML
    private ImageView podcastCover;

    @FXML
    private Button searchBtn;

    @FXML
    private Label nowPlayingBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Label noteTitle;


    public void foo(ActionEvent actionEvent) {
//        Stream<Item> rssFeed = null;
//        try {
//            rssFeed = m.read("http://podcasts.joerogan.net/feed");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<Item> info = rssFeed.collect(Collectors.toList());
//        for(Item itm : info){
//            System.out.println(itm.getLink());
//        }
//
//        hiLabel.setText("Loaded "+info.size()+" podcast from {link}");
    }

    public void searchBtn(ActionEvent event){
        System.out.println("This is the search btn");
    }

    public void addBtn(ActionEvent event){
        System.out.println("This is the add btn");
    }

    public void skipBtn(ActionEvent event){
        System.out.println("This is the skip button");
    }

    public void toggleBtn(ActionEvent event){
        System.out.println("This is the toggle play/pause btn");
    }

    public void backBtn(ActionEvent event){
        System.out.println("This is the back btn");
    }


}
