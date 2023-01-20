package application;
	
import java.io.File;
import java.nio.file.Paths;
import constants.MyValues;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import repository.DataBaseOperationsRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	private DataBaseOperationsRepository databaseOperations = new DataBaseOperationsRepository();
	
	@Override
	public void start(Stage primaryStage) {
		File f = new File("Database/",MyValues.DBNAME+".mv.db");
		
		// if database dont existe create ir and all tables
		//else create only the tables, sql validate if table exists
		if(!f.exists()) 
			databaseOperations.CreateDataBase(MyValues.DBNAME);
		else
			databaseOperations.CreateAllTables();
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		//show main scene
		try {
			System.out.println(System.getProperty("user.dir")+"/resources/images/icon.png");
			Parent root = FXMLLoader.load(Paths.get("resources/views/MainScene.fxml").toUri().toURL());
			Scene scene = new Scene(root);
			primaryStage.setTitle("Bird Application");
			primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
