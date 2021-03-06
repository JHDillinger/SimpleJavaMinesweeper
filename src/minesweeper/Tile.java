package minesweeper;

public class Tile {

	/*
	 * Nicht sicher, ob die Klasse Tile ein bisschen zu umfangreich ist, wenn
	 * die Flagge auch noch mitgespeichert wird. Vielleicht sollte das
	 * unabhängig von der Kachel in MineGame geschehen
	 */
	private final boolean mine;
	private boolean flag = false;
	private boolean uncovered = false;
	private boolean finished = false;
		
	private int minesnearby;
	
	private final int xCoord;
	private final int yCoord;

	
	public Tile(int x, int y, boolean mine) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException();
		} else {
			this.xCoord = x;
			this.yCoord = y;
		}
		this.mine = mine;
	}

	public boolean mined() {
		return this.mine;
	}

	public void setFlag(boolean b) {
		this.flag = b;
	}

	public boolean flagged() {
		return this.flag;
	}
	
	public boolean getUncoverStatus(){
		return this.uncovered;
	}
	
	public void setUncoverStatus(){
		this.uncovered = true;
	}	

	public int getMinesNearby() {
		return this.minesnearby;
	}

	public void setMinesNearby(int x) {
		this.minesnearby = x;
	}

	public void incrementMinesNearby() {
		this.minesnearby++;
	}

	public int getX() {
		return xCoord;
	}

	public int getY() {
		return yCoord;
	}

	
	public boolean getFinished(){
		return finished;
	}
	public void setFinished(){
		finished = true;
	}
	public void printTile() {
		if (this.mine) {
			System.out.print(" X ");
		}
		else if(this.minesnearby ==0){
			System.out.print(" - ");
		}
		else if(this.minesnearby != 0 && !this.mine){
			System.out.print(" " + this.minesnearby + " ");
		}
	}

	public static void main(String[] args) {
		
	}

}
