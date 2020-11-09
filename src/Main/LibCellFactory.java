package Main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LibCellFactory implements Callback<ListView<Podcast>, ListCell<Podcast>> {
    private int counter = 0;
    @Override
    public ListCell<Podcast> call(ListView<Podcast> param) {
        System.out.println("Is this called?");
        if(param.getItems().size() == 0){
            System.out.println("Empty!");
            return new AddBtnCell();
        }
        return new LibCell();
    }
}
