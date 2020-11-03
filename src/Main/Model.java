package Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Model {

    private FXMLLoader mainLoader, secondaryLoader;
    private secondaryController test;

    public Model(){

    }

    public static void fillSampleData(ObservableList<Podcast> backingList) {
        backingList.add(new Podcast("Name 1", "Author 1", 1,1, new JTextArea("Welcome to the new season!"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 2", "Author 2", 1,2, new JTextArea("Episode one was much better!"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 3", "Author 3", 1,3, new JTextArea("When is the new season coming out?"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 4", "Author 4", 1,4, new JTextArea("LAST EPISODE!"), "resources/playIcon.png"));
        backingList.add(new Podcast("Name 5", "Author 5", 2,1, new JTextArea("SEASON 2!!"), "resources/playIcon.png"));
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

    public void setSecondaryController(secondaryController tst){
        this.test = tst;
    }

    public secondaryController getSecondaryController(){
        return this.test;
    }


}
