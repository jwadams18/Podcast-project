package Main;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import org.w3c.dom.Node;

import javax.swing.*;
import java.util.Random;

public class Podcast {

    private final StringProperty author = new SimpleStringProperty(this, "author", "");
    private final StringProperty title = new SimpleStringProperty(this, "title", "");
    private int  progress, maxProgress;
    private JTextArea noteArea;
    private String imgPath, enclosurePath;
    private boolean isPlaying, hasNotes;

    public Podcast(){

        this.progress = 0;
        this.isPlaying = false;
        this.hasNotes = false;

    }

    public Podcast(String Title, String Author, String imgPath, Node enclosure){

        this.title.set(Title);
        this.author.set(Author);
        this.noteArea = new JTextArea();
        this.noteArea.setText("Write your first note here!");

        this.imgPath = imgPath;
        this.progress = 50;
        this.isPlaying = false;
        this.hasNotes = false;

        this.enclosurePath = enclosure.getAttributes().getNamedItem("url").getTextContent();
        this.maxProgress = Integer.parseInt(enclosure.getAttributes().getNamedItem("length").getTextContent());

    }


    @Override
    public String toString(){
        return this.title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public JTextArea getNoteArea() {
        return noteArea;
    }

    public void setNoteArea(JTextArea noteArea) {
        this.noteArea = noteArea;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean hasNotes(){
        return noteArea.getText().isBlank();
    }

    public static Callback<Podcast, Observable[]> extractor = p -> new Observable[]
            {p.title};

    public String dump(){
        return this.title.get()+" "+this.author.get()+" "+this.imgPath+" "+this.enclosurePath+" "+this.maxProgress;
    }
}
