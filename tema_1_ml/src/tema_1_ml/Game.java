package tema_1_ml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
	
	private static int[][] board;
	private static int boardSize;
	private static int predator_count;
	private static int trap_count;
	private static Pradator[] predators;
	private static Prada prada;
	private static Capcana[] traps;
	
	public static ArrayList<Integer> get_possible_actions() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int row = prada.getRow(), column = prada.getColumn();
		
		result.add(Constants.STAY);
		
		//Assuming two animals can't be on the same cell at the same time
		if(column - 1 >= 0)
			result.add(Constants.MOVE_LEFT);
		
		if(column + 1 < boardSize)
			result.add(Constants.MOVE_RIGHT);
		
		if(row - 1 >= 0)
			result.add(Constants.MOVE_UP);
		
		if(row + 1 < boardSize)
			result.add(Constants.MOVE_DOWN);
		
		return result;
	}
	
	//Makes a move and updates the state of the board
	public static void make_move(int move) {
		System.out.println("Prada incearca sa se miste");
		int left, right, up, down;
		int row = prada.getRow(), column = prada.getColumn(), pRow, pColumn;
		boolean trap_area;

		switch(move) {
		case Constants.STAY:
			System.out.println("Prada ramane pe loc");
			break;
		case Constants.MOVE_DOWN:
			System.out.println("Prada merge in jos");
			row+=1;
			break;
		case Constants.MOVE_UP:
			System.out.println("Prada merge in sus");
			row-=1;
			break;
		case Constants.MOVE_LEFT:
			System.out.println("Prada merge la stanga");
			column-=1;
			break;
		case Constants.MOVE_RIGHT:
			System.out.println("Prada merge la dreapta");
			column+=1;
			break;
		}
		
		if(row < 0 || row >= boardSize || column < 0 || column >= boardSize) {
			System.out.println("Unable to make move");
			return;
		}
		
		board[prada.getRow()][prada.getColumn()]=Constants.EMPTY_CELL_TYPE;
		
		//Verificare daca prada intra in raza de actiune a mai mult de un pradator
		ArrayList<Integer> attackers = new ArrayList<Integer>();
		
		for (int i=0; i < predator_count; i++) {
			if(!predators[i].isAlive())
				continue;
			
			up 		= predators[i].getRow()    - predators[i].getRaza_coliziune();
			down 	= predators[i].getRow()    + predators[i].getRaza_coliziune();
			right 	= predators[i].getColumn() + predators[i].getRaza_coliziune();
			left 	= predators[i].getColumn() - predators[i].getRaza_coliziune();
			
			if(column >= left && column <= right && row >= up && row <= down) {
				attackers.add(i);
			}
		}
		
		/* Daca prada nu a intrat in raza de actiune a mai mult de un pradator
		 * verificam daca prin mutare a eliminat din pradatori
		 */
		if(attackers.size() < 2) {
			up 		= row    - prada.getRaza_coliziune();
			down 	= row    + prada.getRaza_coliziune();
			right 	= column + prada.getRaza_coliziune();
			left 	= column - prada.getRaza_coliziune();
			
			for (int i=0; i < predator_count; i++) {
				if(!predators[i].isAlive())
					continue;
				
				pRow = predators[i].getRow();
				pColumn = predators[i].getColumn();
				
				if(pColumn >= left && pColumn <= right && pRow >= up && pRow <= down) {
					predators[i].setAlive(false);
					board[predators[i].getRow()][predators[i].getColumn()] = Constants.EMPTY_CELL_TYPE;
					System.out.println("Prada a atacat pradatorul " + i);
				}
			}
			
		} else {
			System.out.println("Prada a intrat in raza de actiune a cel putin 2 pradatori. Prada moare.");
			prada.setAlive(false);
			prada.setRow(row);
			prada.setColumn(column);
			return;
		}
		
		board[row][column] = Constants.PRADA_TYPE;
		prada.setRow(row);
		prada.setColumn(column);
		
		//MISCARE PRADATORI
		for (int i=0; i < predator_count; i++) {
			if(!predators[i].isAlive())
				continue;
			
			//TODO: implement movement logic for predators;
			
			//Dummy version
			
			
			Random x = new Random();
			boolean cont = true;
			
			while(cont) {
				int movement = x.nextInt(5) + 4;
				
				row = predators[i].getRow();
				column = predators[i].getColumn();
				
				switch(movement) {
				case Constants.STAY:
					break;
				case Constants.MOVE_DOWN:
					row += 1;
					break;
				case Constants.MOVE_UP:
					row -= 1;
					break;
				case Constants.MOVE_LEFT:
					column -= 1;
					break;
				case Constants.MOVE_RIGHT:
					column += 1;
					break;
				}
				
				if(row >= 0 && row < boardSize && column >= 0 && column < boardSize && board[row][column] != Constants.PRADATOR_TYPE) {
					cont = false;
				}
			}
			
			board[predators[i].getRow()][predators[i].getColumn()] = Constants.EMPTY_CELL_TYPE;
			
			trap_area = false;
			for (int j=0; j < trap_count; j++) {
				if(!traps[j].isActive())
					continue;
				
				up 		= traps[j].getRow()    - traps[j].getRaza_influenta();
				down 	= traps[j].getRow()    + traps[j].getRaza_influenta();
				right 	= traps[j].getColumn() + traps[j].getRaza_influenta();
				left 	= traps[j].getColumn() - traps[j].getRaza_influenta();
				
				if(column >= left && column <= right && row >= up && row <= down) { 
					trap_area = true;
					traps[j].setActive(false);
					board[traps[j].getRow()][traps[j].getColumn()] = Constants.EMPTY_CELL_TYPE;
					break;
				}
			}
			
			if(!trap_area) {
				board[row][column] = Constants.PRADATOR_TYPE;
			
				predators[i].setRow(row);
				predators[i].setColumn(column);
			} else {
				System.out.println("Pradatorul " + i + " a calcat in capcana");
				predators[i].setAlive(false);
			}
		}
	}
	
	public static void print_board() {
		
		//System.out.println("X - Spatiu liber\n" + "C - capcana\n" + "P - prada\n" + "B - pradator");
		String toPrint = " ";
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				switch(board[i][j]) {
					case Constants.EMPTY_CELL_TYPE: {
						toPrint = "X";
						break;
					}
					case Constants.PRADA_TYPE: {
						toPrint = "P";
						break;
					}
					case Constants.PRADATOR_TYPE: {
						toPrint = "B";
						break;
					}
					case Constants.CAPCANA_TYPE: {
						toPrint = "C";
						break;
					}
				}
				System.out.print(toPrint + " ");
			}
			System.out.println();
		}
	}
	
	public static void initialize_board(String file) throws FileNotFoundException {
		Random rand = new Random();
		int row, column;
		Scanner in = new Scanner(new File(file));

		boardSize 		= in.nextInt();
		predator_count 	= in.nextInt();
		trap_count 		= in.nextInt();
		
		predators 	= new Pradator[predator_count];	
		traps 		= new Capcana[trap_count];
		prada 		= new Prada();
		
		if(predator_count < 2) {
			System.out.println("Too few predators");
			return;
		}
		
		board = new int[boardSize][boardSize];
		
		for(int i=0; i < boardSize; i++) {
			for(int j=0; j < boardSize; j++) {
				board[i][j] = Constants.EMPTY_CELL_TYPE;
			}
		}

		row = rand.nextInt(boardSize);
		column = rand.nextInt(boardSize);
		
		board[row][column] = Constants.PRADA_TYPE;

		prada.setRaza_coliziune(in.nextInt());
		prada.setRaza_perceptie(in.nextInt());
		
		prada.setViteza(1);
		prada.setRow(row);
		prada.setColumn(column);
		prada.setAlive(true);
		
		for(int i=0; i < predator_count; i++) {
			row 	= rand.nextInt(boardSize);
			column 	= rand.nextInt(boardSize);
			
			while(board[row][column] != Constants.EMPTY_CELL_TYPE) {
				row 	= rand.nextInt(boardSize);
				column 	= rand.nextInt(boardSize);				
			}
			
			board[row][column] = Constants.PRADATOR_TYPE;
			
			predators[i] = new Pradator();
			
			predators[i].setRaza_coliziune(in.nextInt());
			predators[i].setRaza_perceptie(in.nextInt());
			predators[i].setViteza(1);
			predators[i].setRow(row);
			predators[i].setColumn(column);
			predators[i].setAlive(true);
		}
		
		for(int i=0; i < trap_count; i++) {
			row = rand.nextInt(boardSize);
			column = rand.nextInt(boardSize);
			
			while(board[row][column] != Constants.EMPTY_CELL_TYPE) {
				row = rand.nextInt(boardSize);
				column = rand.nextInt(boardSize);				
			}
			
			board[row][column] = Constants.CAPCANA_TYPE;
			
			traps[i] = new Capcana();
			
			traps[i].setRaza_influenta(in.nextInt());
			traps[i].setRow(row);
			traps[i].setColumn(column);
			traps[i].setActive(true);
		}
		
		in.close();
		
	}
	
	public static int game_state() {
		int row, column, left, right, up, down, possibleAttackers = 0, alivePredators=0;
		
		if(!prada.isAlive()) {
			return Constants.PREDATORS_WIN;
		}
		
		for (int i=0; i < predator_count; i++) {
			if(predators[i].isAlive()) {
				alivePredators++;
			}
		}
		
		if (alivePredators < 2) {
			System.out.println("A mai ramas un singur pradator. Pradatorii au pierdut!");
			return Constants.PREDATORS_LOSE;
		}
		
		row 	= prada.getRow();
		column 	= prada.getColumn();
		
		for (int i=0; i < predator_count; i++) {
			if(!predators[i].isAlive())
				continue;
			
			up 		= predators[i].getRow()    - predators[i].getRaza_coliziune();
			down 	= predators[i].getRow()    + predators[i].getRaza_coliziune();
			right 	= predators[i].getColumn() + predators[i].getRaza_coliziune();
			left 	= predators[i].getColumn() - predators[i].getRaza_coliziune();
			
			if(column >= left && column <= right && row >= up && row <= down) 
				possibleAttackers++;
			
			if(possibleAttackers > 1) {
				System.out.println("Prada a fost incoltita!");
				return Constants.PREDATORS_WIN;
			}
		}
		
		for (int i=0; i < trap_count; i++) {
			if(!traps[i].isActive())
				continue;
			
			up 		= traps[i].getRow()    - traps[i].getRaza_influenta();
			down 	= traps[i].getRow()    + traps[i].getRaza_influenta();
			right 	= traps[i].getColumn() + traps[i].getRaza_influenta();
			left 	= traps[i].getColumn() - traps[i].getRaza_influenta();
			
			if(column >= left && column <= right && row >= up && row <= down) { 
				System.out.println("Prada a calcat in capcana!");
				return Constants.PREDATORS_WIN;
			}
		}
			
		return Constants.NEUTRAL_STATE;
	}

	public static void main(String[] args) throws FileNotFoundException {
		String filePath = "/home/bogdanpetcu/tema1ml/input.txt";
		Random x = new Random();
		ArrayList<Integer> possibleActions;
		
		initialize_board(filePath);
		
		print_board();
		
		while(game_state() == Constants.NEUTRAL_STATE) {
			possibleActions = get_possible_actions();
			make_move(possibleActions.get(x.nextInt(possibleActions.size())));
			print_board();
		}
		

		
	}

}
