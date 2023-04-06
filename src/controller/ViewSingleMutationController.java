package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.MutationsRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import domains.Mutation;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ViewSingleMutationController {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TextField TfSearch;
	@FXML
	private Label LbTitle, LabelAlert,LbSpecie, LbName, LbVar1,LbVar2, LbVar3, LbObs;

	private MutationsRepository mutationsRepository = new MutationsRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();


	@FXML
	public void btnEdit(ActionEvent event) throws SQLException, IOException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/AddMutationView.fxml"));
			Parent root = loader.load();
			Mutation m = mutationsRepository.getMutationWhereString("Name", TfSearch.getText());
			AddMutationViewController addMutationViewController =  loader.getController();
			addMutationViewController.startValuesEdit(m);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_EDIT_MUTATION + LbName.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			Mutation updatedMutation = mutationsRepository.getMutationsById(m.getId());
			search(updatedMutation.getName());
		}
	}

	@FXML
	public void btnDelete(ActionEvent event) throws SQLException, IOException {
		if (validator() && validatorSearch()) {
			Mutation m = mutationsRepository.getMutationWhereString("Name", TfSearch.getText());
			if (validatorDelete(m)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
				Parent root = loader.load();
				ConfirmationController confirmationController = loader.getController();
				confirmationController.getLbText()
						.setText("Tem a certeza que quer apagar a Mutacao: '" + LbName.getText() + "'?");
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_DELETE_MUTATION + LbName.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				if (confirmationController.isConfirmed()) {
					mutationsRepository.deleteMutation(m);
					clearAllFields();
				}
			}
		}
	}

	public void search(String name) throws SQLException {
		clearAllFields();
		TfSearch.setText(name);
		Mutation m = mutationsRepository.getMutationWhereString("Name", TfSearch.getText());
		updateAllInfo(m);
	}
	
	@FXML
	public void btnSearchForName(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Mutation m = mutationsRepository.getMutationWhereString("Name", TfSearch.getText());
			updateAllInfo(m);
		}
	}

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
	
	public boolean validatorDelete(Mutation mutation) throws SQLException {
		boolean validate=false;
		if (birdsRepository.getBirdWhereInt("MutationsId", mutation.getId()) != null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Mutacao nao pode ser apagada porque tem passaros associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}

	public boolean validatorSearch(){
		boolean validate = false;
		if (LbName.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Mutacao tem de ser procurada antes de editar/apagar.");
			validate=false;
		} else if (!TfSearch.getText().equalsIgnoreCase(LbName.getText())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("Mutacao tem de ser procurada antes de editar/apagar.");
	        validate = false;
	    }else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		if (TfSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido");
			validate=false;
		}else if (mutationsRepository.getMutationWhereString("Name", TfSearch.getText())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao existe");
			validate=false;
		}else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public void updateAllInfo(Mutation m) {
		LbTitle.setText(m.getName());
		LbSpecie.setText(m.getSpecie().getCommonName()+"("+m.getSpecie().getScientificName()+")");
		LbName.setText(m.getName());
		LbVar1.setText(m.getVar1());
		LbVar2.setText(m.getVar2());
		LbVar3.setText(m.getVar3());
		LbObs.setText(m.getObs());
	}
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Mutação XXXX");
		LbSpecie.setText(null);
		LbName.setText(null);
		LbVar1.setText(null);
		LbVar2.setText(null);
		LbVar3.setText(null);
		LbObs.setText(null);
	}
}
