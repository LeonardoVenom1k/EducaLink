module br.com.javafx.educalink {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.javafx.educalink to javafx.fxml;
    opens br.com.javafx.educalink.login to javafx.fxml;

    exports br.com.javafx.educalink;
    exports br.com.javafx.educalink.login;
}
