package Main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LibCellFactory implements Callback<ListView<Podcast>, ListCell<Podcast>> {
    @Override
    public ListCell<Podcast> call(ListView<Podcast> param) {
        return new LibCell();
    }
}
