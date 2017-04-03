package gui;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WinAlert {
	public static void display() {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("GameOver!");
		window.setMinWidth(250);

		Label label = new Label();
		label.setText("Sie haben gewonnen!");

		Button closeButton = new Button("Neues Spiel");
		closeButton.setOnAction(e -> window.close());

		File file = new File("src/balloons.jpg");

		Image img = new Image(file.toURI().toString());
		ImageView iv = new ImageView(img);

		HBox buttonslayout = new HBox(10);
		buttonslayout.getChildren().addAll(closeButton /* , newGameButton */);
		buttonslayout.setAlignment(Pos.BOTTOM_CENTER);

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, iv, buttonslayout);
		layout.setAlignment(Pos.BOTTOM_CENTER);
		layout.setStyle("-fx-background: #FFFFFF;");
		Scene scene = new Scene(layout);

		window.setResizable(false);
		window.setScene(scene);
		window.showAndWait();

	}
}
