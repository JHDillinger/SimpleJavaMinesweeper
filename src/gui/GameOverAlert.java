package gui;

import java.io.File;

import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverAlert {

	public static void display() {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("GameOver!");
		window.setMinWidth(250);

		Label label = new Label();
		label.setText("Sie haben leider verloren!");

		Button closeButton = new Button("Neues Spiel");
		closeButton.setOnAction(e -> window.close());

		File file = new File("src/explosion.jpg");
	/*	File file = new File(
				"C:/Users/Armin/Studium/Informatik/Semester 5/eclipse_workspaceSemester5/Minesweeper/explosion.jpg");*/

		Image img = new Image(file.toURI().toString());
		ImageView iv = new ImageView(img);

		HBox buttonslayout = new HBox(10);
		buttonslayout.getChildren().addAll(closeButton /*, newGameButton*/);
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
