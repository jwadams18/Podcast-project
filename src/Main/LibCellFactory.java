package Main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
public class LibCellFactory implements Callback<ListView<Podcast>, ListCell<Podcast>> {
    /**
     * This is used to create the Library cells that will be displayed in a ListView in the Library scene
     * @param param
     * @return
     */
    @Override
    public ListCell<Podcast> call(ListView<Podcast> param) {
        return new LibCell();
    }
}
