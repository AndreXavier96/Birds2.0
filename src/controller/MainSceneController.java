package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.DataBaseOperationsRepository;

import java.io.File;
import java.nio.file.Paths;

import constants.MyValues;
import javafx.event.ActionEvent;
import javafx.scene.Node;



public class MainSceneController {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	private DataBaseOperationsRepository dataBaseOperationsRepository = new DataBaseOperationsRepository();

	@FXML
	public void btnSearchBird(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/birds/ViewSingleBird.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchFederation(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/federation/ViewSingleFederation.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void btnAddFederation(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/federation/AddFederationView.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllFederations(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/federation/ViewAllFederations.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddClub(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/clubs/AddClubView.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllClubs(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/clubs/ViewAllClubs.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBreeder(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/breeder/AddBreederView.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBreeders(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/breeder/ViewAllBreeders.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBird(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/birds/AddBirdView.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBirds(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/birds/ViewAllBirds.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddSpecies(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/species/AddSpeciesView.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void btnCreateDB(ActionEvent event) {
		dataBaseOperationsRepository.CreateDataBase(MyValues.DBNAME);
//		dataBaseOperationsRepository.alterTableAddColumn("BIRDS", "Obs VARCHAR(500)");
	}
	
	@FXML
	private void btnDropDB(ActionEvent event) {
		System.out.println(System.getProperty("user.dir"));
		try {
			System.out.println("Trying to delete DB...");
			File f =new File("./Database");
			deleteDirectory(f);
//			dataBaseOperationsRepository.alterTableDropColumn("MUTATIONS", "Observations");
			System.out.println("DB deleted!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	@FXML
	public void btnViewAllSpecies(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/species/ViewAllSpecies.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllMutations(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/mutations/ViewAllMutations.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void BtnViewCages(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/cages/ViewAllCages.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddMutation(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/mutations/AddMutationView.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddCage(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/cages/AddCageView.fxml").toUri().toURL());
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
