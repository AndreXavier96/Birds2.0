package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.nio.file.Paths;

import constants.MyValues;
import domains.Bird;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.BirdsRepository;

public class ViewSingleBirdController  {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private Label LabelError;
	@FXML
	private Label LbTitle,LbBand, LbYear,LbEntryDate,LbEntryType;
	@FXML
	private Label LbSex,LbBuyPrice,LbSpecie,LbMutation,LbClassification;
	@FXML
	private Label LbSellPrice,LbState,LbLastModify,LbOBs,LbCage, LbBreeder;
	@FXML
	private ImageView ImBird;
	@FXML
	private TextField TfBandSearch;
	
	@FXML
	private Label LbBandFather,LbBandMother;
	@FXML
	private ImageView ImFather,ImMother;
	
	@FXML
	private Label LbBandGrandFatherFather,LbBandGrandMotherFather;
	@FXML
	private ImageView ImGrandFatherFather,ImGrandMotherFather;
	
	@FXML
	private Label LbBandGrandFatherMother,LbBandGrandMotherMother;
	@FXML
	private ImageView ImGrandFatherMother,ImGrandMotherMother;
	
	
	@FXML
	private AnchorPane ApBuyPrice, ApSellPrice;
	
	BirdsRepository birdsRepository=new BirdsRepository();
	
	@FXML
	public void btnSearchForBand(ActionEvent event) {
		if (validator()) {
			Bird b = birdsRepository.getBirdByBand(TfBandSearch.getText());
			personalInfo(b);
			affiliation(b);
		}
	}
	
	public void affiliation(Bird b) {
		if(b.getFather()!=null) {
			LbBandFather.setText(b.getFather().getBand());
			if(b.getFather().getFather()!=null)
				LbBandGrandFatherFather.setText(b.getFather().getFather().getBand());
			if(b.getFather().getMother()!=null)
				LbBandGrandFatherMother.setText(b.getFather().getMother().getBand());
		}
		if(b.getMother()!=null) {
			LbBandMother.setText(b.getMother().getBand());
			if(b.getMother().getFather()!=null)
				LbBandGrandMotherFather.setText(b.getMother().getFather().getBand());
			if(b.getMother().getMother()!=null)
				LbBandGrandMotherMother.setText(b.getMother().getMother().getBand());
		}	
	}
	
	
	public void personalInfo(Bird b) {
			LbTitle.setText("Bird "+b.getBand());
			LbBand.setText(b.getBand());
			LbSpecie.setText(b.getSpecies().getCommonName());
			LbMutation.setText(b.getMutations().getName());
			LbYear.setText(b.getYear().toString());
			LbSex.setText(b.getSex());
			LbClassification.setText("TODO");
			LbEntryDate.setText(b.getEntryDate().toString());
			LbCage.setText(b.getCage().getCode());
			LbEntryType.setText(b.getEntryType());
			LbState.setText(b.getState());
			if (LbEntryType.getText().equals(MyValues.COMPRA)) {
				LbBuyPrice.setText(b.getBuyPrice().toString()+"\u20AC");
				ApBuyPrice.setVisible(true);
			}else 
				ApBuyPrice.setVisible(false);
			if (LbState.getText().equals(MyValues.VENDIDO)) {
				LbSellPrice.setText(b.getSellPrice().toString()+"\u20AC");
				ApSellPrice.setVisible(true);
			}else
				ApSellPrice.setVisible(false);
			LbLastModify.setText("TODO");
			LbOBs.setText("TODO");
			LbBreeder.setText(b.getBreeder().getName());
	}
	
	public boolean validator() {
		boolean validate = false;
		if (TfBandSearch.getText().length()==0) {
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Anilha tem de ser preenchido");
			validate=false;
		}else if (birdsRepository.getBirdByBand(TfBandSearch.getText())==null) {
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Anilha nao existe");
			validate=false;
		}else {
			TfBandSearch.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		return validate;
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

	
}
