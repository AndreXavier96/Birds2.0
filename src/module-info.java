module Birds {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires com.h2database;
	
	opens application to javafx.graphics, javafx.fxml;
	opens controller to javafx.fxml;
	opens domains to javafx.base;
}
