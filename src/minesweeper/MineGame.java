package minesweeper;

import java.util.ArrayList;

public class MineGame {

	private Field field;
	@SuppressWarnings("unused")
	private int flagcount;
	@SuppressWarnings("unused")
	private final Difficulty difficulty;
	private static ArrayList<Tile> uncovered = new ArrayList<Tile>();

	/*
	 * Bin mir noch nicht sicher, ob und wenn ja, wie ich eine Schwierigkeit
	 * USERDEFINED umsetze, da ja irgendwo eine Eingabe geschehen muss
	 * 
	 * Vermutlich mit zwei Konstruktoren. Einmal public MineGame(Difficulty d)
	 * und einmal public MineGame(int width, int heigth, int minecount)
	 */
	public MineGame(Difficulty d) {
		this.difficulty = d;
		if (d == Difficulty.EASY) {
			this.field = new Field(9, 9, 10);
			this.flagcount = 10;
		} else if (d == Difficulty.MEDIUM) {
			this.field = new Field(16, 16, 40);
			this.flagcount = 40;
		} else {
			this.field = new Field(30, 16, 99);
			this.flagcount = 99;
		}
	}

	public MineGame(int width, int height, int mines) {
		this.difficulty = Difficulty.USERDEFINED;
		this.field = new Field(width, height, mines);
		this.flagcount = mines;
	}

	public void mark(int x, int y) {
		if (x < 0 || y < 0 || x >= field.getWidth() || y >= field.getWidth()) {
			throw new IllegalArgumentException();
		}
		if (field.getTile(x, y).flagged()) {
			field.getTile(x, y).setFlag(false);
			flagcount++;
		} else {
			field.getTile(x, y).setFlag(true);
			flagcount--;
		}
	}

	public void initialUncover(int x, int y) {
		if (x < 0 || y < 0 || x >= field.getWidth() || y >= field.getWidth()) {
			throw new IllegalArgumentException();
		}
		if (field.getTile(x, y) != null && field.getTile(x, y).mined()) {
			//System.out.println("Leider verloren!");
			return;
		}
		if (field.getTile(x, y).flagged()) {
			return;
		}
		if (field.getTile(x, y).getMinesNearby() != 0) {
			uncovered.add(field.getTile(x, y));
			field.getTile(x, y).setUncoverStatus();
		} else {
			uncover(x, y);
		}
	}

	
	
	/*Das muss ich noch kommentieren!
	 * 
	 */
	private void uncover(int x, int y) {
		if (x < 0 || y < 0 || x >= field.getWidth() || y >= field.getWidth()) {
			throw new IllegalArgumentException();
		}
		if (!field.getTile(x, y).getUncoverStatus()) {
			uncovered.add(field.getTile(x, y));
			field.getTile(x, y).setUncoverStatus();
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((x + j) < 0 || (y + i) < 0 || (x + j) >= field.getWidth() || (y + i) >= field.getHeight()) {
					continue;
				} else {
					if (field.getTile(x + j, y + i).getMinesNearby() == 0
							&& !field.getTile(x + j, y + i).getUncoverStatus()
							&& !field.getTile(x + j, y + i).flagged()) {
						uncover((x + j), (y + i));
					} else if (field.getTile(x + j, y + i).flagged() || field.getTile(x + j, y + i).mined()) {
						continue;
					} else if (i == 0 && j == 0) {
						continue;
					} else if (field.getTile(x + j, y + i).getUncoverStatus()) {
						continue;
					} else if (field.getTile(x + j, y + i).getMinesNearby() != 0
							&& !field.getTile(x + j, y + i).getUncoverStatus()) {
						uncovered.add(field.getTile(x + j, y + i));
						field.getTile(x + j, y + i).setUncoverStatus();

					}
				}
			}
		}

	}
	
	
	public Field getField(){
		return this.field;
	}
	
	public String printGame(){
		String ausgabe = "das Spiel existiert";
		return ausgabe;
	}

	public static void main(String[] args) {
		MineGame testgame = new MineGame(4, 4, 1);

		Field testfeld = testgame.field;

		for (int i = 0; i < testfeld.getWidth(); i++) {
			for (int j = 0; j < testfeld.getHeight(); j++) {
				testfeld.getTile(i, j).printTile();
			}
			System.out.println();
		}

		testgame.mark(0, 1);
		testgame.mark(1, 0);
		testgame.initialUncover(0, 0);

		System.out.println("Aufgedeckte Tiles:");
		for (Tile t : uncovered) {
			t.printTile();
		}
		System.out.println("\nAnzahl aufgedeckte Tiles: " + uncovered.size());

		testgame.mark(0, 1);
		testgame.mark(1, 0);
		testgame.initialUncover(0, 1);

		System.out.println("Aufgedeckte Tiles:");
		for (Tile t : uncovered) {
			t.printTile();
		}
		System.out.println("\nAnzahl aufgedeckte Tiles: " + uncovered.size());

	}

}
