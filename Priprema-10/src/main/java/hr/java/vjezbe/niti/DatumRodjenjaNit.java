package hr.java.vjezbe.niti;

import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.stream.Collectors;

public class DatumRodjenjaNit implements Runnable {
	@Override
	public void run() {
		var dataSource = VjezbeApplication.getDataSource();
		List<Student> imajuRodjendan;

		try {
			imajuRodjendan = dataSource.readStudentiKojiImajuRodjendan();
		} catch (DataSourceException e) {
			e.printStackTrace();
			return;
		}
		
		if (imajuRodjendan.size() == 0) {
			return;
		}
		
		var alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Rođendan studenata");
		alert.setHeaderText("Čestitajte rođendan sljedećim studentima:");
		alert.setContentText(imajuRodjendan.stream()
			.map(s -> s.getIme() + " " + s.getPrezime())
			.collect(Collectors.joining("\n"))
		);
		
		alert.show();
	}
}
