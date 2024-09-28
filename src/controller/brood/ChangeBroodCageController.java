package controller.brood;

import javafx.fxml.FXML;
import java.sql.SQLException;
import constants.MyValues;
import domains.Brood;
import domains.Cage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import repository.BroodRepository;
import repository.CageRepository;

public class ChangeBroodCageController {
	@FXML
	private Label LabelError,LbFather,LbMother,LbCurrentCage;
	@FXML
	private ComboBox<Cage> CbCage;
	@FXML
	private ViewSingleBroodController viewSingleBroodController;
	
	private Brood brood;

	CageRepository cageRepository = new CageRepository();
	BroodRepository broodRepository = new BroodRepository();
	
	public void startValues(Brood brood) throws SQLException {
		this.brood = brood;
		LbFather.setText(brood.getFather().getBand());
		LbMother.setText(brood.getMother().getBand());
		LbCurrentCage.setText(brood.getCage().getCode());
		ObservableList<Cage> cages = cageRepository.getAllCages();
		for(int i=0;i<cages.size();i++)
			if (cages.get(i).getCode().equals(brood.getCage().getCode()))
				cages.remove(i);
		CbCage.setItems(cages);
	}
	
	public void setViewSingleBroodController(ViewSingleBroodController controller) {
		this.viewSingleBroodController = controller;
	}
	
	@FXML
	public void btnChange(ActionEvent event) throws SQLException {
		if (validate()) {
			broodRepository.updateBroodCage(brood.getId(), CbCage.getValue().getId());
		    Stage stage = (Stage) CbCage.getScene().getWindow();
		    stage.close();
		    viewSingleBroodController.setSuccess(MyValues.CHANGE_CAGE_SUCCESS, broodRepository.getBroodById(brood.getId()));
		}
	}
	
	public boolean validate() {
		boolean validate = false;
		if (CbCage.getValue()==null) {
			CbCage.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nova Gaiola tem de ser escolhida");
			validate=false;
		}else {
			CbCage.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		return validate;
	}
	
	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) CbCage.getScene().getWindow();
	    stage.close();
	}
}
