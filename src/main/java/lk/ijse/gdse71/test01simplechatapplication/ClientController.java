package lk.ijse.gdse71.test01simplechatapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ClientController {

    @FXML
    private Button btnSend;

    @FXML
    private Button chooseFile;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane clientAnchorPane;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtField;

    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    String message = "";

    public void initialize() {
        new Thread(() -> {
            try{
                socket = new Socket("localhost", 4000);
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
                    txtArea.appendText("Server : " + message + "\n");
                }
            }catch (Exception e){

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
    void typeMsg(ActionEvent event) {

    }

}
