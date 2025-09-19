module br.com.javafx.educalink {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens br.com.javafx.educalink to javafx.fxml;
    opens br.com.javafx.educalink.login to javafx.fxml;
    opens br.com.javafx.educalink.areaalu to javafx.fxml;
    opens br.com.javafx.educalink.areaprof to javafx.fxml, com.google.gson;

    exports br.com.javafx.educalink;
    exports br.com.javafx.educalink.login;
    exports br.com.javafx.educalink.areaalu;
    exports br.com.javafx.educalink.areaprof;
}
