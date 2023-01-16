package hr.java.vjezbe.glavna.fxutil;

import hr.java.vjezbe.entitet.Ocjena;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FXUtil {
	public static boolean tryFilterByIntegerInput(int integer, String input) {
		try {
			return integer == Integer.parseInt(input);
		} catch (RuntimeException e) {
			return true;
		}
	}
	
	public static boolean tryFilterByTimeInput(LocalTime time, String input, DateTimeFormatter format) {
		try {
			return time.equals(LocalTime.parse(input, format));
		} catch (RuntimeException e) {
			return true;
		}
	}
	
	public static boolean tryFilterByOcjenaInput(Ocjena ocjena, String input) {
		try {
			return ocjena == Ocjena.parseOcjena(input);
		} catch (RuntimeException e) {
			return true;
		}
	}
	
	public static void showDataSourceErrorAlert(String message) {
		var alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.setTitle("Error");
		alert.showAndWait();
	}
}
