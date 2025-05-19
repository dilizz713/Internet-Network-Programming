package lk.ijse.gdse71.test01simplechatapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientInitializer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/client.fxml"))));
        stage.setTitle("Client");
        stage.show();
    }
}
