import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import acsse.csc2a.gui.ShipPane;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            //Root Node
            ShipPane RootNode = new ShipPane();

            //Putting the pane in scene
            Scene scene = new Scene(RootNode);

            //Setting up stage
            primaryStage.setTitle("MWSCB Messages");
            primaryStage.setWidth(600);
            primaryStage.setHeight(600);

            // Adding scene to stage
            primaryStage.setScene(scene);

            // Displaying stage
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}