package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * @author jwadams18
 * NoteCast! - PodcastPlayer
 * CS*350 Human Computer Interaction
 */
public class Main extends Application {

    //Used to connect secondary files together using Model-View-Controller (MVC) practices
    public static final Model model = new Model();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("NoteCast!");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/musicnote.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
