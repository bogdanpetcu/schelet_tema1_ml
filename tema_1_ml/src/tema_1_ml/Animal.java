package tema_1_ml;

public class Animal {
	
	private int 	raza_perceptie;
	private int 	raza_coliziune;
	private int 	viteza;
	private int		row;
	private int		column;
	private boolean	alive;
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getRaza_perceptie() {
		return raza_perceptie;
	}
	
	public void setRaza_perceptie(int raza_perceptie) {
		this.raza_perceptie = raza_perceptie;
	}
	
	public int getRaza_coliziune() {
		return raza_coliziune;
	}
	
	public void setRaza_coliziune(int raza_coliziune) {
		this.raza_coliziune = raza_coliziune;
	}
	
	public int getViteza() {
		return viteza;
	}
	
	public void setViteza(int viteza) {
		this.viteza = viteza;
	}

}