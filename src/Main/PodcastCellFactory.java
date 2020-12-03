package Main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
public class PodcastCellFactory implements Callback<ListView<Podcast>, ListCell<Podcast>> {
    /**
     * Create PodcastCells that will be displayed in the ListView on the main window
     * @param param
     * @return
     */
    @Override
    public ListCell<Podcast> call(ListView<Podcast> param){
        return new PodcastCell();
    }
}
