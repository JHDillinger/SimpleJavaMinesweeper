package gui;

import minesweeper.*;

import java.io.File;

import javafx.application.*;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MinesweeperGUI extends Application {
	private static MineGame game;
	private static int fieldHeight;
	private static int fieldWidth;
	private static int minecount;

	private StackPane[][] tiles;
	private int tileLength;

	private GridPane grid_main, grid_playfield;
	private Scene scene;
	private Label l;

	private int screenWidth;
	private int screenHeight;
	private int gap;

	private Stage window;

	public static void main(String[] args) {
		launch(args);
	}

	/*-
	 * Insgesamt ein wenig durcheinander, vor allem start(). Die meisten Maße,
	 * die in Zahlen angegeben bzw mithilfe von konkreten Zahlen berechnet
	 * werden, sind durch Ausprobieren entstanden.
	 * 
	 * Generell entstand vieles hier nur durch Ausprobieren und das was funktionierte 
	 * (zumindest erstmal auf den ersten Blick) wurde übernommen.
	 * 
	 * Was noch fehlt: Benutzerdefinierte Feldgröße, Zeitzähler in Sekunden, 
	 * Das Spiel schon beim OptionsWindow abbrechen zu können, ...
	 */

	@Override
	public void start(Stage stage) throws Exception {

		/*
		 * Starte Spiel mit den Optionen. Die Auswahl gibt eine Difficulty, mit
		 * derer das Spiel erstellt wird. Die Abmessungen der Stage sind in
		 * Abhängigkeit von den Maßen des MineGame.
		 * 
		 */
		Difficulty diff = optionsWindow();
		game = new MineGame(diff);
		fieldWidth = game.getField().getWidth();
		fieldHeight = game.getField().getHeight();
		minecount = game.getField().getMinecount();
		screenWidth = fieldWidth * 25;
		screenHeight = (fieldHeight * 25) + 50;

		// Um die Anzahl bereits geflaggter Felder anzuzeigen
		l = new Label("Flagcount: " + minecount);

		window = stage;
		window.setTitle("Minesweeper");

		gap = (int) (tileLength * 0.1);

		grid_main = new GridPane();
		grid_main.setAlignment(Pos.CENTER);

		grid_playfield = new GridPane();
		grid_playfield.setAlignment(Pos.CENTER);

		grid_playfield.setHgap(gap);
		grid_playfield.setVgap(gap);

		tiles = new StackPane[fieldWidth][fieldHeight];
		for (int x = 0; x < fieldWidth; x++) {
			for (int y = 0; y < fieldHeight; y++) {
				createTile(tiles, game, x, y /* , button */);
			}
		}

		grid_main.add(grid_playfield, 0, 0);
		grid_main.add(l, 0, 1);

		// Hieraus soll vielleicht noch ein Timer werden, der in Sekunden
		// hochzählt
		grid_main.add(new Label("test"), 0, 2);
		scene = new Scene(grid_main, screenWidth, screenHeight);
		scene.getStylesheets().add("resources/style.css");

		window.setResizable(false);
		window.setScene(scene);
		window.show();

	}

	/*-
	 * Erstellt die Buttons und den "Hintergrund" (also mines nearby,
	 * Minensymbol)
	 * 
	 */
	private void createTile(StackPane[][] f, MineGame M, int x, int y) {
		Tile t = M.getField().getTile(x, y);

		// Hier wird der Button gemacht;
		Button b = new Button("  ");
		b.setMaxSize(25, 25);
		b.setMinSize(25, 25);
		
		
		
		
		b.setOnMouseClicked(e -> click(b, e, t));
		
		
		
		
		
		
		

		// Hier wird der "Hintergrund" gemacht
		if (t.mined()) {
			File file = new File("src/mini-mine.gif");
			Image img = new Image(file.toURI().toString());
			ImageView iv = new ImageView(img);

			f[x][y] = new StackPane(iv, b);
		} else if (t.getMinesNearby() == 0) {
			f[x][y] = new StackPane(new Text(), b);
		} else {
			f[x][y] = new StackPane(new Text("" + t.getMinesNearby()), b);
		}

		grid_playfield.add(f[x][y], x, y);
	}

	/*
	 * Linksklick deckt die Tile auf und ruft update() auf, Rechtsklick flagged
	 * die Tile bzw entfernt die Flagge und aktualisiert die Anzahl der
	 * geflaggten Tiles
	 * 
	 */
	private void click(Button b, MouseEvent e, Tile t) {

		if (e.getButton() == MouseButton.PRIMARY && !t.flagged()) {
			game.initialUncover(t.getX(), t.getY());

			b.setDisable(true);
			t.setFinished();
			update();

			if (t.mined()) {
				GameOverAlert.display();
				window.close();
				try {
					start(window);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		if (e.getButton() == MouseButton.SECONDARY) {
			if (!t.flagged()) {
				t.setFlag(true);
				b.setText("X");
				minecount--;
				l.setText("Flagcount: " + minecount);
			} else {
				t.setFlag(false);
				b.setText(null);
				minecount++;
				l.setText("Flagcount: " + minecount);
			}
		}
	}

	/*
	 * Sorgt für das rekursive Aufdecken, falls eine leere Tile angeklickt
	 * wurde. Dazu wird ein Mausclick simuliert. Falls alle Felder aufgedeckt
	 * wurden, wird WinAlert-Fenster geöffnet
	 * 
	 * Laufzeittechnisch vermutlich katastrophal das Ganze ...
	 */
	private void update() {
		for (int x = 0; x < fieldWidth; x++) {
			for (int y = 0; y < fieldHeight; y++) {
				if (game.getField().getTile(x, y).getUncoverStatus() && !game.getField().getTile(x, y).getFinished()) {
					Button b = (Button) tiles[x][y].getChildren().get(1);

					MouseEvent e = new MouseEvent(MouseEvent.MOUSE_CLICKED, x, y, screenWidth, screenHeight,
							MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, false, false, false,
							null);
					click(b, e, game.getField().getTile(x, y));
				}
			}
		}

		int i = 0;
		for (int x = 0; x < fieldWidth; x++) {
			for (int y = 0; y < fieldHeight; y++) {
				if (game.getField().getTile(x, y).getUncoverStatus() || game.getField().getTile(x, y).mined()) {
					i++;
				}
			}
		}
		if (i == fieldWidth * fieldHeight) {
			WinAlert.display();
			window.close();
			try {
				start(window);
			} catch (Exception e2) {
				// TODO: handle exception
			}

		}
	}

	/*
	 * Ursprünglich war das als eigene OptionsWindow Klasse gedacht, allerdings
	 * war es mir im Moment zu aufwendig, herauszufinden wie ich dann die Daten
	 * der dort getätigten Auswahl übernehme und daraus in start() ein Game
	 * erstelle. Eventuell müsste ich mich damit nochmal beschäftigen, wenn ich
	 * das mit userdefined noch implementieren wollte
	 * 
	 */
	private Difficulty optionsWindow() {
		Difficulty d;

		Stage options = new Stage();
		options.initModality(Modality.APPLICATION_MODAL);
		options.setTitle("Options");

		CheckBox easy = new CheckBox("Easy");
		CheckBox medium = new CheckBox("Medium");
		CheckBox hard = new CheckBox("Hard");
		easy.setSelected(true);

		easy.setOnAction(e -> {
			medium.setSelected(false);
			hard.setSelected(false);

		});
		medium.setOnAction(e -> {
			easy.setSelected(false);
			hard.setSelected(false);
		});
		hard.setOnAction(e -> {
			easy.setSelected(false);
			medium.setSelected(false);
		});

		/*
		 * Hier fehlt noch evtl der userdefined input
		 */

		Button startButton = new Button("Start Game!");
		startButton.setOnAction(e -> options.close());
		startButton.setAlignment(Pos.BOTTOM_CENTER);

		VBox optionsLayout = new VBox(10);
		optionsLayout.getChildren().addAll(easy, medium,
				hard/* , label, heightInput, widthInput, minesInput */, startButton);

		Scene scene = new Scene(optionsLayout);

		options.setResizable(false);

		// options.setOnCloseRequest(event -> event.consume());
		options.initStyle(StageStyle.UNDECORATED);
		options.setScene(scene);
		options.showAndWait();

		if (easy.isSelected()) {
			d = Difficulty.EASY;
		} else if (medium.isSelected()) {
			d = Difficulty.MEDIUM;
		} else {
			d = Difficulty.HARD;
		}
		return d;

	}

}
