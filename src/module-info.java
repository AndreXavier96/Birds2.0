module Birds {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires com.h2database;
	requires lombok;
	
	opens application to javafx.graphics, javafx.fxml;
	opens controller to javafx.fxml;
	opens controller.adoptiveParent to javafx.fxml;
	opens controller.bird to javafx.fxml;
	opens controller.breeder to javafx.fxml;
	opens controller.brood to javafx.fxml;
	opens controller.cage to javafx.fxml;
	opens controller.couples to javafx.fxml;
	opens controller.egg to javafx.fxml;
	opens controller.federation to javafx.fxml;
	opens controller.club to javafx.fxml;
	opens controller.mutation to javafx.fxml;
	opens controller.species to javafx.fxml;
	opens controller.treatment to javafx.fxml;
	opens controller.award to javafx.fxml;
	opens controller.exibithion to javafx.fxml;
	opens domains to javafx.base;
}
