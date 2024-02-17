package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.DataBaseOperationsRepository;
import java.io.File;
import constants.MyValues;
import javafx.event.ActionEvent;



public class MainSceneController {
	
	private DataBaseOperationsRepository dataBaseOperationsRepository = new DataBaseOperationsRepository();
	
	@FXML
	public void btnAddFederation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/AddFederationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllFederations(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/ViewAllFederations.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchFederation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/ViewSingleFederation.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddClub(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/AddClubView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllClubs(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/ViewAllClubs.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchClub(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/ViewSingleClub.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBreeder(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/AddBreederView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBreeders(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/ViewAllBreeders.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchBreeder(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/ViewSingleBreeder.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBird(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/AddBirdView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBirds(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewAllBirds.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchBird(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddSpecies(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/AddSpeciesView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnViewAllSpecies(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/ViewAllSpecies.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchSpecie(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/ViewSingleSpecie.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddCage(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/AddCageView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void BtnViewCages(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/ViewAllCages.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchCage(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/ViewSingleCage.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddMutation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/AddMutationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllMutations(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/ViewAllMutations.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnSearchMutation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/ViewSingleMutation.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/AddCoupleView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/ViewAllCouple.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnSearchCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/ViewSingleCouple.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddTreatment(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/AddTreatmentView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllTreatment(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/ViewAllTreatments.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnTreatBird(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/BirdTreatmentView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
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
	private void btnCreateDB(ActionEvent event) {
		dataBaseOperationsRepository.CreateDataBase(MyValues.DBNAME);
	}
	
	@FXML
	private void btnDropDB(ActionEvent event) {
		System.out.println(System.getProperty("user.dir"));
		try {
			System.out.println("Trying to delete DB...");
			File f =new File("./Database");
			deleteDirectory(f);
			System.out.println("DB deleted!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
