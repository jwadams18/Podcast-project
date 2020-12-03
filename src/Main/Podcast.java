package Main;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;
import javafx.util.Callback;
import javafx.util.Duration;
import org.w3c.dom.Node;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Podcast {

    private final StringProperty author = new SimpleStringProperty(this, "author", "");
    private final StringProperty title = new SimpleStringProperty(this, "title", "");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(this, "isPlaying", false);
    private final BooleanProperty hasNotes = new SimpleBooleanProperty(this, "hasNotes", false);
    private final BooleanProperty isQueued = new SimpleBooleanProperty(this, "isQueued", false);
    private File mediaFile = null;
    private Duration progress, maxProgress;
    private ProgressBar cellProgressBar;
    private String imgPath, enclosurePath, duration, pubDate, notesStr;

    public Podcast(String Title, String Author, String imgPath, Node enclosure, String duration, String pubDate){

        this.title.set(Title);
        this.author.set(Author);
        this.notesStr = null;
        this.duration = duration;
        this.pubDate = pubDate;

        this.imgPath = imgPath;
        this.progress = new Duration(0);
        this.isPlaying.set(false);
        this.hasNotes.set(false);

        this.enclosurePath = enclosure.getAttributes().getNamedItem("url").getTextContent();
        download();
    }

    public void download() {

        if(Main.model.DEBUG)
        System.out.println("[Podcast] Starting download");

        //Creates new file using the title of the podcast, in the Podcast directory
        this.mediaFile = new File("Podcast/"+ getTitleStringForm()+".mp3");
        //Creates directory if needed
        if(!new File("Podcast/").exists()){
            new File("Podcast/").mkdir();
        }
        if(this.mediaFile.exists()){
            System.err.println("[Podcast] Podcast already downloaded! Skipping...");
            return;
        }

        try{
            //Uses enclosure url from rss to establish connect and attempt to download
            URL url = new URL(this.enclosurePath);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            //Creates readers and input streams to download the file
            System.err.println("response code: " +http.getResponseCode()+" "+ getTitleStringForm());
            //Follows redirects
            HttpURLConnection.setFollowRedirects(true);

            double fileSize = (double) http.getContentLengthLong();
            BufferedInputStream input = new BufferedInputStream(http.getInputStream());
            FileOutputStream fileOut = new FileOutputStream(this.mediaFile);
            BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut, 1024);
            byte[] bufferData = new byte[1024];

            double downloaded = 0.0;
            int read = 0;
            double percentDownloaded = 0.0;

            //Will return -1 when end of input stream reached
            while((read = input.read(bufferData, 0, 1024)) >= 0){

                bufferOut.write(bufferData, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded*100)/fileSize;
                String percent = String.format("%.4f %s", percentDownloaded,"%");
                System.out.println("Downloaded "+percent+" of "+this.title.get());

            }

            bufferOut.close();
            input.close();
            fileOut.close();

            if(Main.model.DEBUG)
            System.out.println("Download complete: "+this.mediaFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void deleteMP3(){
        this.mediaFile.delete();
    }

    public String getTitleStringForm(){
        return this.title.get().replace(':', '_').replace(' ', '_');

    }

    @Override
    public String toString(){
        return this.title.get();
    }

    public String getNotes(){
        return this.notesStr;
    }

    public void setNotes(String notes){
        this.notesStr = notes;
    }

    public void setProgress(Duration progress) {
        this.progress = progress;
    }

    public void setCellProgressBar(ProgressBar pb){
        this.cellProgressBar = pb;
    }

    public void setMaxProgress(Duration maxProgress){
        this.maxProgress = maxProgress;
    }

    public void setNotesStatus(boolean value) { this.hasNotesProperty().set(value); }

    public Duration getMaxProgress(){
        return this.maxProgress;
    }

    public ProgressBar getCellProgressBar(){ return this.cellProgressBar;}

    public String getAuthor() {
        return author.get();
    }

    public String getTitle() {
        return title.get();
    }

    public Duration getProgress() {
        return this.progress;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getDuration() { return this.duration;}

    public String getPubDate() { return this.pubDate; }

    public boolean isPlaying() {
        return this.isPlaying.get();
    }

    public boolean isQueued(){
        return this.isQueued.get();
    }

    public void setQueued(boolean value){
        this.isQueued.set(value);
    }

    public void togglePlaying() {
        isPlaying.set(!isPlaying.get());
    }

    public boolean hasNotes(){
        //TODO fix this
        if(notesStr != null)
        return !notesStr.equalsIgnoreCase("Write your first note!")
                && !notesStr.isBlank();

        return false;
    }

    public boolean isDownloaded(){
        return this.mediaFile.exists();
    }

    public StringProperty titleProperty(){
        return title;
    }

    public BooleanProperty isPlayingProperty(){
        return isPlaying;
    }

    public BooleanProperty hasNotesProperty(){
        return hasNotes;
    }

    public BooleanProperty isQueuedProperty(){
        return isQueued;
    }

    public static Callback<Podcast, Observable[]> extractor = p -> new Observable[]
            {p.titleProperty(), p.isPlayingProperty(), p.hasNotesProperty()};

    public String dump(){
        return this.title.get()+" "+this.author.get()+" "+this.imgPath+" "+this.enclosurePath;
    }
}
