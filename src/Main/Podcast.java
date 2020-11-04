package Main;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import javax.swing.*;
import java.util.Random;

public class Podcast {

    private final StringProperty author = new SimpleStringProperty(this, "author", "");
    private final StringProperty title = new SimpleStringProperty(this, "title", "");
    private int episode, season, progress;
    private JTextArea noteArea;
    private String imgPath;
    private boolean isPlaying, hasNotes;

    public Podcast(){

        this.progress = 0;
        this.isPlaying = false;
        this.hasNotes = false;

    }

    public Podcast(String Title, String Author, int season, int episode, JTextArea noteArea, String imgPath){

        this.title.set(Title);
        this.author.set(Author);
        this.season = season;
        this.episode = episode;
        this.noteArea = noteArea;
        this.imgPath = imgPath;
        this.progress = 50;
        this.isPlaying = false;
        this.hasNotes = true;

    }


    @Override
    public String toString(){
        return "[S"+this.season+":"+this.episode+"] "+this.title.get();
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

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
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
}
