package controller.treatment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import repository.BirdTreatmentRepository;
import repository.BirdsRepository;
import repository.TreatmentRepository;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import constants.MyValues;
import domains.Bird;
import domains.BirdTreatment;
import domains.Treatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class BirdTreatmentViewController implements Initializable {	
	
	@FXML
	private Label LabelAlert,LbTitle;
	@FXML
	private TextField TfSearchSingle;
	@FXML
	private ComboBox<String> CbType;
	@FXML
	private ComboBox<Treatment> CbTreatment;
	@FXML
	private AnchorPane AcUnico, AcMultiplo, AcTodos;
	@FXML
	private ListView<Bird> LvAvailable,LvAssigned;
	@FXML
	private Button btnAssign, btnDeAssign;
	
	private ObservableList<Bird> assignedBirds,availableBirds;
	BirdsRepository birdsRepository = new BirdsRepository();
	TreatmentRepository treatmentRepository = new TreatmentRepository();
	BirdTreatmentRepository birdTreatmentRepository = new BirdTreatmentRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Treatment> treatments = FXCollections.observableArrayList();
		CbType.setItems(MyValues.TREATMENT_TYPE);
		CbType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(MyValues.UNICO)) {
				AcUnico.setVisible(true);
				AcMultiplo.setVisible(false);
				AcTodos.setVisible(false);
			} else if (newValue.equals(MyValues.MULTIPLOS)) {
				AcUnico.setVisible(false);
				AcMultiplo.setVisible(true);
				AcTodos.setVisible(false);
			} else if (newValue.equals(MyValues.TODOS)) {
				AcUnico.setVisible(false);
				AcMultiplo.setVisible(false);
				AcTodos.setVisible(true);
			}
		});
		try {
			treatments = treatmentRepository.getAllTreatments();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CbTreatment.setItems(treatments);
		CbTreatment.setConverter(new StringConverter<Treatment>() {
			 public String toString(Treatment t) {
				 return t.getName();
			 }
			 public Treatment fromString(String s) {
				 return CbTreatment.getItems().stream().filter(t -> t.getName().equals(s)).findFirst().orElse(null);
			 }
		});
		LvAvailable.setCellFactory(new Callback<ListView<Bird>, ListCell<Bird>>() {
	        @Override
	        public ListCell<Bird> call(ListView<Bird> param) {
	            return new ListCell<Bird>() {
	                @Override
	                protected void updateItem(Bird bird, boolean empty) {
	                    super.updateItem(bird, empty);
	                    if (bird != null) {
	                        setText(bird.getBand());
	                    } else {
	                        setText(null);
	                    }
	                }
	            };
	        }
	    });
		LvAssigned.setCellFactory(new Callback<ListView<Bird>, ListCell<Bird>>() {
	        @Override
	        public ListCell<Bird> call(ListView<Bird> param) {
	            return new ListCell<Bird>() {
	                @Override
	                protected void updateItem(Bird bird, boolean empty) {
	                    super.updateItem(bird, empty);
	                    if (bird != null) {
	                        setText(bird.getBand());
	                    } else {
	                        setText(null);
	                    }
	                }
	            };
	        }
	    });
		availableBirds = FXCollections.observableArrayList(birdsRepository.getAllBirdsOwned());
		assignedBirds = FXCollections.observableArrayList();
		LvAvailable.setItems(availableBirds);
		LvAssigned.setItems(assignedBirds);
	}
	
	@FXML
	public void btnAssign(ActionEvent event) {
		Bird selectedBird = LvAvailable.getSelectionModel().getSelectedItem();
		btnDeAssign.setStyle(null);
		if (selectedBird!=null) {
	        assignedBirds.add(selectedBird);
	        availableBirds.remove(selectedBird);
	        btnAssign.setStyle(null);
		} else {
			btnAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Um passaro disponivel tem de ser selecionado");
		}
	}
	
	@FXML
	public void btnDeAssign(ActionEvent event) {
		Bird selectedBird = LvAssigned.getSelectionModel().getSelectedItem();
		btnAssign.setStyle(null);
		if (selectedBird!=null) {
			assignedBirds.remove(selectedBird);
			availableBirds.add(selectedBird);
			btnDeAssign.setStyle(null);
		}else {
			btnDeAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Um passaro escolhido tem de ser selecionado");
		}
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		Integer countBirds=0;
		if (validator()) {
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
			Date today = calendar.getTime();
			Treatment t = treatmentRepository.getTreatmentById(CbTreatment.getValue().getId());
			if (CbType.getValue().equals(MyValues.UNICO)) {
				BirdTreatment bt = new BirdTreatment();
				Bird b =birdsRepository.getBirdWhereString("Band", TfSearchSingle.getText());
				bt.setBird(b);
				bt.setTreatment(t);
				bt.setStart(today);
				calendar.add(Calendar.DAY_OF_MONTH, bt.getTreatment().getDurationDays());
				bt.setFinish(calendar.getTime());
				birdTreatmentRepository.insert(bt);
				countBirds++;
			}else if (CbType.getValue().equals(MyValues.MULTIPLOS)) {
				for (Bird b : assignedBirds) {
					BirdTreatment bt = new BirdTreatment();
					bt.setBird(b);
					bt.setTreatment(t);
					bt.setStart(today);
					calendar.add(Calendar.DAY_OF_MONTH, bt.getTreatment().getDurationDays());
					bt.setFinish(calendar.getTime());
					birdTreatmentRepository.insert(bt);
					countBirds++;
				}
			}else if (CbType.getValue().equals(MyValues.TODOS)) {
				for (Bird b : birdsRepository.getAllBirds()) {
					BirdTreatment bt = new BirdTreatment();
					bt.setBird(b);
					bt.setTreatment(t);
					bt.setStart(today);
					calendar.add(Calendar.DAY_OF_MONTH, bt.getTreatment().getDurationDays());
					bt.setFinish(calendar.getTime());
					birdTreatmentRepository.insert(bt);
					countBirds++;
				}
			}
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Tratamento iniciado!");
			treatmentRepository.updateTreatmentStats(t, t.getTimesAplied()+1, t.getBirdsTreated()+countBirds);
			clearAllFields();
		}
	}
	
	
	
	public boolean validator() throws SQLException {
		boolean validate= false;
		clearAllErros();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (CbType.getValue()==null) {
			CbType.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Quantidade de passaros tem de ser escolhido");
			validate=false;
		}else {
			CbType.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate && CbType.getValue().equals(MyValues.UNICO)) {
			if (TfSearchSingle.getText().isEmpty()) {
				TfSearchSingle.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha tem de ser escolhida");
				validate=false;
			}else if (birdsRepository.getBirdWhereString("Band", TfSearchSingle.getText())==null) {
				TfSearchSingle.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha de passaro nao existe");
				validate=false;
			}
			else {
				TfSearchSingle.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate && CbType.getValue().equals(MyValues.MULTIPLOS)) {
			if (assignedBirds.size()==0) {
				LvAssigned.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Pelo menos um passaro tem de ser escolhido");
				validate=false;
			}else {
				LvAssigned.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (CbTreatment.getValue()==null) {
				CbTreatment.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tratamento tem de ser escolhido");
				validate=false;
			}else {
				CbTreatment.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		return validate;
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.close(); 
	}

	public void clearAllErros() {
		CbType.setStyle(null);
		CbTreatment.setStyle(null);
		LvAssigned.setStyle(null);
		TfSearchSingle.setStyle(null);
		btnAssign.setStyle(null);
		btnDeAssign.setStyle(null);
	}
	
	public void clearAllFields() {
		CbType.setValue(null);
		CbTreatment.setValue(null);
		TfSearchSingle.setStyle(null);
		Integer assignedSize = assignedBirds.size();
		for (int i = 0; i<assignedSize;i++) {
			availableBirds.add(assignedBirds.get(0));
			assignedBirds.remove(0);
		}
		clearAllErros();
	}

	
}
