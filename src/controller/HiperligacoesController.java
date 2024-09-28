package controller;

import java.io.IOException;
import java.sql.SQLException;

import controller.bird.ViewSingleBirdController;
import controller.breeder.ViewSingleBreederController;
import controller.cage.ViewSingleCageController;
import controller.couples.ViewSingleCouplesController;
import controller.egg.ViewSingleEggController;
import controller.federation.ViewSingleFederationController;
import controller.mutation.ViewSingleMutationController;
import controller.species.ViewSingleSpecieController;
import domains.Egg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HiperligacoesController {

	public void openViewSingleCouple(Scene currentScene,String band) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/ViewSingleCouple.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleCouplesController controller = loader.getController();
	    	controller.search(band);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openViewSingleBird(Scene currentScene,String band) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleBirdController controller = loader.getController();
	    	controller.search(band);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void openViewFederation(Scene currentScene,String name) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/ViewSingleFederation.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleFederationController controller = loader.getController();
	    	controller.search(name);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openViewSpecie(Scene currentScene,String name) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/ViewSingleSpecie.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleSpecieController controller = loader.getController();
	    	controller.search(name);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openViewBreeder(Scene currentScene,String name) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/ViewSingleBreeder.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleBreederController controller = loader.getController();
	    	controller.searchName(name);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openViewMutation(Scene currentScene,String name) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/ViewSingleMutation.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleMutationController controller = loader.getController();
	    	controller.search(name);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openViewCage(Scene currentScene,String name) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/ViewSingleCage.fxml"));
	    	Parent root = loader.load();
	    	ViewSingleCageController controller = loader.getController();
	    	controller.search(name);
	    	Stage currentStage =(Stage) currentScene.getWindow();
	    	currentScene.setRoot(root);
	    	currentStage.sizeToScene();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openViewEggFromSingleBrood(Scene currentScene, Egg egg) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/egg/ViewSingleEgg.fxml"));
	        Parent root = loader.load();
	        ViewSingleEggController controller = loader.getController();
	        controller.startValues(egg);
	        Stage newStage = new Stage();
	        newStage.setScene(new Scene(root));
	        newStage.initModality(Modality.APPLICATION_MODAL);
	        newStage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
