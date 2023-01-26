package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.FXUtil;
import hr.java.vjezbe.glavna.fxutil.LocalDateConverter;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class IspitiPretragaController {
	@FXML
	private TextField nazivPredmetaUnos;
	@FXML
	private TextField imeStudentaUnos;
	@FXML
	private TextField prezimeStudentaUnos;
	@FXML
	private TextField ocjenaUnos;
	@FXML
	private DatePicker datumPisanjaUnos;
	@FXML
	private TextField vrijemePisanjaUnos;

	@FXML
	private TableView<Ispit> predmetTableView;
	@FXML
	private TableColumn<Ispit, String> nazivPredmetaColumn;
	@FXML
	private TableColumn<Ispit, String> imeStudenataColumn;
	@FXML
	private TableColumn<Ispit, String> prezimeStudenataColumn;
	@FXML
	private TableColumn<Ispit, Integer> ocjenaColumn;
	@FXML
	private TableColumn<Ispit, String> datumIVrijemePisanjaColumn;

	private List<Ispit> sviIspiti;

	public void initialize() {
		datumPisanjaUnos.setConverter(new LocalDateConverter(VjezbeApplication.DATE_FORMAT_SHORT));

		nazivPredmetaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPredmet().getNaziv()));
		imeStudenataColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudent().getIme()));
		prezimeStudenataColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudent().getPrezime()));
		ocjenaColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOcjena().getNumerickaVrijednost()).asObject());
		datumIVrijemePisanjaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatumIVrijeme().format(VjezbeApplication.DATE_TIME_FORMAT_FULL)));

		try {
			sviIspiti = VjezbeApplication.getDataSource().readIspiti();
		} catch (DataSourceException e) {
			FXUtil.showDataSourceErrorAlert(e.getMessage());
		}
		predmetTableView.setItems(FXCollections.observableList(sviIspiti));
	}

	public void pretrazi() {
		var filtrirano = sviIspiti.stream()
			.filter(i -> i.getPredmet().getNaziv().contains(nazivPredmetaUnos.getText()))
			.filter(i -> i.getStudent().getIme().contains(imeStudentaUnos.getText()))
			.filter(i -> i.getStudent().getPrezime().contains(prezimeStudentaUnos.getText()))
			.filter(i -> FXUtil.tryFilterByOcjenaInput(i.getOcjena(), ocjenaUnos.getText()))
			.filter(i -> datumPisanjaUnos.getValue() == null || i.getDatumIVrijeme().toLocalDate().equals(datumPisanjaUnos.getValue()))
			.filter(i -> FXUtil.tryFilterByTimeInput(i.getDatumIVrijeme().toLocalTime(), vrijemePisanjaUnos.getText(), VjezbeApplication.TIME_FORMAT_SHORT))
			.toList();

		predmetTableView.setItems(FXCollections.observableList(filtrirano));
	}
}
