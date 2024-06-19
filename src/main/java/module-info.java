module com.kensoftph.javafxcarousel {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kensoftph.javafxcarousel to javafx.fxml;
    exports com.kensoftph.javafxcarousel;
}