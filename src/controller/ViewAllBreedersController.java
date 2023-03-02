package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.ResourceBundle;

import domains.Breeder;
import domains.Club;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.BreederRepository;
import repository.FederationRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllBreedersController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Breeder> tableID;
	@FXML
	private TableColumn<Breeder,String> colName, colEmail, colPostalCode, colLocale, colDistrict, colAddress, colType, colClube,colStam;
	@FXML 
	private TableColumn<Breeder, Integer> colCC,colNIF,colCellphone,colCites;

	private FederationRepository federationRepository = new FederationRepository();	
	
	@FXML
	public void btnBack(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/MainScene.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BreederRepository breederRepository = new BreederRepository();
		ObservableList<Breeder> breeders = breederRepository.getAllBreeders();
		colCC.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("CC"));
		colName.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Name"));
		colNIF.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("Nif"));
		colCellphone.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("Cellphone"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Email"));
		colPostalCode.setCellValueFactory(new PropertyValueFactory<Breeder,String>("PostalCode"));
		colLocale.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Locale"));
		colDistrict.setCellValueFactory(new PropertyValueFactory<Breeder,String>("District"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Address"));
		colType.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Type"));
		colClube.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Breeder, String>, ObservableValue<String>>() {
		        @Override
		        public ObservableValue<String> call(TableColumn.CellDataFeatures<Breeder, String> param) {
		            StringBuilder sb = new StringBuilder();
		            for (Club c : param.getValue().getClub()) {
		            	sb.append("[");
		            	sb.append(c.getFederation().getAcronym());
		            	sb.append("]");
		                sb.append(c.getAcronym());
		                sb.append(", ");
		            }
		            if (sb.length() > 2) {
		                sb.delete(sb.length() - 2, sb.length());
		            }
		            return new SimpleStringProperty(sb.toString());
		        }
		    });
		colStam.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Breeder, String>, ObservableValue<String>>() {
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Breeder, String> param) {
		        Breeder breeder = param.getValue();
		        Map<Integer, String> stam = breeder.getStam();
		        StringBuilder sb = new StringBuilder();
		        for (Map.Entry<Integer, String> entry : stam.entrySet()) {
		            Integer federationId = entry.getKey();
		            String federationStam = entry.getValue();
		            String federationName = federationRepository.getFederationWhereInt("id", federationId).getAcronym();
		            sb.append("[").append(federationName).append("]").append(federationStam).append(", ");
		        }
		        if (sb.length() > 2) {
		            sb.delete(sb.length() - 2, sb.length());
		        }
		        return new SimpleStringProperty(sb.toString());
		    }
		});

		tableID.setItems(breeders);
	}
	
}
