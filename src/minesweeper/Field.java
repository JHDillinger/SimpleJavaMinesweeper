package minesweeper;

import java.util.Random;

public class Field {
	private final Tile[][] minefield;
	private final int width;
	private final int height;
	private final int minecount;

	public Field(int width, int height, int mines) {
		if (width < 1 || height < 1 || mines < 1 || mines > (width * height)) {
			throw new IllegalArgumentException("Das funktioniert so nicht!");
		}

		if (width > 50 || height > 50 || mines > 100) {
			throw new IllegalArgumentException("Nicht übertreiben!");
		}
		this.width = width;
		this.height = height;
		this.minecount = mines;
		this.minefield = new Tile[height][width];

		// Initialisiere Feld mit Tiles
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.minefield[i][j] = new Tile(j, i, false);
			}
		}
		this.addRandomMines(mines);

	}

	/*
	 * AddMine bearbeitet im Endeffekt bis zu 9 Tiles. In der Mitte wird die
	 * Mine gesetzt und anschließend der Minecount der 8 Felder außenrum
	 * inkrementiert
	 * 
	 */
	public Tile addMine(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			throw new IllegalArgumentException();
		}
		if (minefield[y][x] != null && minefield[y][x].mined()) {
			throw new MineExistsException();

		}
		Tile tile = new Tile(x, y, true);
		minefield[y][x] = tile;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((x + j) < 0 || (y + i) < 0 || (x + j) >= width || (y + i) >= height) {
					continue;
				} else {
					if (minefield[y + i][x + j].getMinesNearby() == 0) {
						minefield[y + i][x + j].incrementMinesNearby();
					} else if (minefield[y + i][x + j].mined()) {
						continue;
					} else if (i == 0 && j == 0) {
						continue;
					} else {
						minefield[y + i][x + j].incrementMinesNearby();
					}
				}
			}
		}
		return tile;
	}

	public void addRandomMines(int minecount) {
		Random r = new Random();

		int i = 1;
		while (i <= minecount) {
			int x = r.nextInt(this.width);
			int y = r.nextInt(this.height);

			if (minefield[y][x] != null && minefield[y][x].mined()) {
				continue;
			} else {
				addMine(x, y);
				i++;
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			throw new IllegalArgumentException();
		} else {
			return this.minefield[y][x];
		}
	}

	public void setTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			throw new IllegalArgumentException();
		} else {
			this.minefield[y][x] = new Tile(x, y, false);
		}
	}

	public int getMinecount() {
		return minecount;
	}

	public Tile[][] getSpielfeld() {
		return this.minefield;
	}

	public static void main(String[] args) {

		Field testfeld = new Field(4, 4, 2);

		for (int i = 0; i < testfeld.width; i++) {
			for (int j = 0; j < testfeld.height; j++) {

				testfeld.minefield[i][j].printTile();
			}
			System.out.println();
		}

	}

}
