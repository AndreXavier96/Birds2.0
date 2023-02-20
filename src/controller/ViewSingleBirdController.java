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
import javafx.scene.image.Image;
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
		clearAllFields();
		if (validator()) {
			Bird b = birdsRepository.getBirdByBand(TfBandSearch.getText());
			personalInfo(b);
			affiliation(b);
		}
	}
	
	public void affiliation(Bird b) {
		if(b.getFather()!=null) {
			LbBandFather.setText(b.getFather().getBand());
			if (!b.getFather().getImage().isEmpty())
				ImFather.setImage(new Image(b.getFather().getImage()));
			
			if(b.getFather().getFather()!=null) {
				LbBandGrandFatherFather.setText(b.getFather().getFather().getBand());
				if (!b.getFather().getFather().getImage().isEmpty())
					ImGrandFatherFather.setImage(new Image(b.getFather().getFather().getImage()));
			}
				
			if(b.getFather().getMother()!=null) {
				LbBandGrandFatherMother.setText(b.getFather().getMother().getBand());
				if (!b.getFather().getMother().getImage().isEmpty())
					ImGrandFatherMother.setImage(new Image(b.getFather().getMother().getImage()));
			}
				
			
		}
		if(b.getMother()!=null) {
			LbBandMother.setText(b.getMother().getBand());
			if (!b.getMother().getImage().isEmpty())
				ImMother.setImage(new Image(b.getMother().getImage()));
			
			if(b.getMother().getFather()!=null) {
				LbBandGrandMotherFather.setText(b.getMother().getFather().getBand());
				if (!b.getMother().getFather().getImage().isEmpty())
					ImGrandMotherFather.setImage(new Image(b.getMother().getFather().getImage()));
			}
				
			if(b.getMother().getMother()!=null) {
				LbBandGrandMotherMother.setText(b.getMother().getMother().getBand());
				if (!b.getMother().getMother().getImage().isEmpty())
					ImGrandMotherMother.setImage(new Image(b.getMother().getMother().getImage()));
			}
				
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
			if (!b.getImage().isEmpty()) {
				ImBird.setImage(new Image(b.getImage()));
			}
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
	
	public void clearAllFields() {
		LabelError.setText(null);
		LbTitle.setText("Bird STAM yyyy 0000");
		LbBand.setText(null);
		LbYear.setText(null);
		LbEntryDate.setText(null);
		LbEntryType.setText(null);
		LbSex.setText(null);
		LbBuyPrice.setText(null);
		ApBuyPrice.setVisible(false);
		LbSellPrice.setText(null);
		ApSellPrice.setVisible(false);
		LbSpecie.setText(null);
		LbMutation.setText(null);
		LbClassification.setText(null);
		LbState.setText(null);
		LbLastModify.setText(null);
		LbOBs.setText(null);
		LbCage.setText(null);
		LbBreeder.setText(null);
		ImBird.setImage(new Image("file:resources/images/img/default.png"));

		LbBandFather.setText(null);
		LbBandMother.setText(null);
		LbBandGrandFatherFather.setText(null);
		LbBandGrandMotherFather.setText(null);
		LbBandGrandFatherMother.setText(null);
		LbBandGrandMotherMother.setText(null);
		ImFather.setImage(new Image("file:resources/images/img/default.png"));
		ImMother.setImage(new Image("file:resources/images/img/default.png"));
		ImGrandFatherFather.setImage(new Image("file:resources/images/img/default.png"));
		ImGrandMotherFather.setImage(new Image("file:resources/images/img/default.png"));
		ImGrandFatherMother.setImage(new Image("file:resources/images/img/default.png"));
		ImGrandMotherMother.setImage(new Image("file:resources/images/img/default.png"));

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
