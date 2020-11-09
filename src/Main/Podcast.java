package Main;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import org.w3c.dom.Node;

import javax.swing.*;

public class Podcast {

    private final StringProperty author = new SimpleStringProperty(this, "author", "");
    private final StringProperty title = new SimpleStringProperty(this, "title", "");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(this, "isPlaying", false);
    private final BooleanProperty hasNotes = new SimpleBooleanProperty(this, "hasNotes", false);
    private int  progress, maxProgress;
    private JTextArea noteArea;
    private String imgPath, enclosurePath, duration, pubDate;

    public Podcast(){

        this.progress = 0;
        this.isPlaying.set(false);
        this.hasNotes.set(false);

    }

    public Podcast(String Title, String Author, String imgPath, Node enclosure, String duration, String pubDate){

        this.title.set(Title);
        this.author.set(Author);
        this.noteArea = new JTextArea();
        this.noteArea.setText("Write your first note here!");
        this.duration = duration;
        this.pubDate = pubDate;

        this.imgPath = imgPath;
        this.progress = 0;
        this.isPlaying.set(false);
        this.hasNotes.set(false);

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

    public String getTitle() {
        return title.get();
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

    public String getImgPath() {
        return imgPath;
    }

    public String getDuration() { return this.duration;}

    public String getPubDate() { return this.pubDate; }

    public boolean isPlaying() {
        return this.isPlaying.get();
    }

    public void togglePlaying() {
        boolean temp = isPlaying.get();
        isPlaying.set(temp);
    }

    public boolean hasNotes(){
        return !noteArea.getText().equalsIgnoreCase("Write your first note here!");
    }

    public static Callback<Podcast, Observable[]> extractor = p -> new Observable[]
            {p.title, p.isPlaying, p.hasNotes};

    public String dump(){
        return this.title.get()+" "+this.author.get()+" "+this.imgPath+" "+this.enclosurePath+" "+this.maxProgress;
    }
}
