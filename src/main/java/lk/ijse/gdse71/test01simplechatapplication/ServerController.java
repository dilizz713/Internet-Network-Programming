package lk.ijse.gdse71.test01simplechatapplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class ServerController {

    @FXML
    private Button btnSend;

    @FXML
    private Button chooseFile;

    @FXML
    private Button btnEmoji;


    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane serverAnchorPAne;


    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtField;

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message = "";

    public void initialize() {
        new Thread(() -> {
            try{
                txtArea.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-font-size: 14");
                txtField.setStyle("-fx-font-family: 'Segoe UI Emoji'; -fx-font-size: 14 ");

                serverSocket = new ServerSocket(4000);
                txtArea.appendText("Server Started\n");
                socket = serverSocket.accept();
                txtArea.appendText("Client Connected\n");
                dataInputStream = new DataInputStream(socket.getInputStream());

                while(true){
                    message = dataInputStream.readUTF();
                    if(message.equals("IMAGE")){
                        int length = dataInputStream.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream.readFully(imageBytes);

                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(byteArrayInputStream);
                        imageView.setImage(image);
                    }

                    txtArea.appendText("Client : " + message + "\n");

                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void sendBtnOnAction(ActionEvent event) throws IOException {
        String text = txtField.getText();

        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(text);
        dataOutputStream.flush();

        txtArea.appendText("Me : " + text + "\n");
        txtField.clear();
    }

    @FXML
    void chooseFileBtnOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                byte[] imageBytes= Files.readAllBytes(file.toPath());

                dataOutputStream=new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("IMAGE");
                dataOutputStream.writeInt(imageBytes.length);
                dataOutputStream.write(imageBytes);
                dataOutputStream.flush();

                txtArea.appendText(file.getName()+"\n");
                txtArea.appendText(file.getAbsolutePath()+"\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void sendEmojiBtnOnActions(ActionEvent event) {
        String[] emojis = {"😊","❤️","️😍","😉","😁","😎","✌️","🤦‍♀️","️😋"};

        Stage emojiStage = new Stage();
        VBox emojiVBox = new VBox();
        emojiVBox.setSpacing(5);
        emojiVBox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-min-width: 10px; -fx-max-width: 100px");

        for (String emoji : emojis) {
            Button emojiButton = new Button(emoji);
            emojiButton.setStyle("-fx-font-size: 20;");
            emojiButton.setOnAction(e -> {
                txtField.appendText(emoji);
                emojiStage.close();
            });
            emojiVBox.getChildren().add(emojiButton);

        }
        Scene scene = new Scene(emojiVBox);
        emojiStage.setTitle("Choose Emoji");
        emojiStage.setScene(scene);
        emojiStage.show();

    }


    @FXML
    void typeMsg(ActionEvent event) {

    }

}
