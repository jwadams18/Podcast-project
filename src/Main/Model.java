package Main;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;

import java.io.IOException;
import java.util.stream.Stream;

public class Model {

    private RssReader reader;

    public Model(){
        reader = new RssReader();
    }

    public Stream<Item> read(String url) throws IOException {
        return reader.read(url);
    }


}
