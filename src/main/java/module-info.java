module lk.ijse.gdse71.test01simplechatapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.gdse71.test01simplechatapplication to javafx.fxml;
    exports lk.ijse.gdse71.test01simplechatapplication;
}