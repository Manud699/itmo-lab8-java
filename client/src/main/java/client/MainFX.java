package client;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage){
        var runner = new SystemBootstrapper(getParameters()
                        .getRaw()
                        .toArray(new String[0]),primaryStage)
                        .buildRunner();
        runner.start();
    }


    public static void main(String[]argms) {
        launch(argms);
    }
}

