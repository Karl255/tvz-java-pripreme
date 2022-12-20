package hr.java.vjezbe.glavna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class VjezbeApplication extends Application {
	private static Stage appStage;
	
	@Override
	public void start(Stage stage) throws IOException {
		appStage = stage;
		
		FXMLLoader fxmlLoader = new FXMLLoader(VjezbeApplication.class.getResource("pocetna-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Vježba");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
	
	public static void showWindow(URL resource) {
		try {
			var window = (Parent) FXMLLoader.load(resource);
			appStage.setScene(new Scene(window));
			appStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
