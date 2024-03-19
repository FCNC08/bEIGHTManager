module bEIGHTManager {
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
