package lk.ijse.gdse71.test01simplechatapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerInitializer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/server.fxml"))));
        stage.setTitle("Server");
        stage.show();
    }
}
