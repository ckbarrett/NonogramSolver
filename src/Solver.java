/*
 * Created by: Christian Barrett
 * Date: 11/25/2020
 * Project: Nonogram solver
 */

import javafx.util.Pair;

public class Solver {
	
	 // Squirrel
	private static final int[][] exampleRows1 = 
		{{3},{4,2,1},{3,1,1,1},{3,1,2,1},{2,1,2},
		 {4,2,2},{2,2,2},{1,3,1},{2,2,1},{4,3},
		 {1,1,2,2},{1,1,1},{1,1,1},{5,2},{3,2,3}};
	private static final int[][] exampleCols1 = 
		{{2},{3},{1,1},{1,1,2,1},{2,2,1,1},
		 {2,6,2},{1,2,2},{1,3,2},{3,2,5},{2,1,1},
		 {1,1},{4,2,1},{2,2,2,2},{1,1,2,5},{2,5}};
	
	// Tea pot
	private static final int[][] exampleRows2 = 
		{{1},{3},{1,2},{7},{5},
		 {2,2},{1,2,4},{2,5,2},{3,8,1},{2,8,1},
		 {2,2,3,1},{2,2,3,1},{3,2,3,1},{6,3,2},{5,3,1},
		 {4,3,1},{3,4},{3,4},{9},{7}};
	private static final int[][] exampleCols2 = 
		{{2},{8},{7},{4},{6},
		 {1,2,12},{18},{1,2,3,2},{2,2,4,2},{4,14},
		 {18},{1,2,12},{2,3},{1,3},{7}};
	
	public static void main(String[] args) {
		Pair<int[][], int[][]> requirements = readNonogram(exampleRows2, exampleCols2);
		int[][] rows = requirements.getKey();
		int[][] cols = requirements.getValue();
		boolean[][] puzzle = new boolean[rows.length][cols.length];
		if(!solveNonogram(puzzle, rows, cols, 0,0)) System.out.println("Not solvable");
		else printNonogram(puzzle);
	}
	private static Pair<int[][], int[][]> readNonogram(int[][] rows, int[][] cols){
		int rowSum = 0;
		for(int i = 0; i < rows.length; i++) {
			for(int j = 0; j < rows[i].length; j++) {
				rowSum += rows[i][j];
			}
		}
		int colSum = 0;
		for(int i = 0; i < cols.length; i++) {
			for(int j = 0; j < cols[i].length; j++) {
				colSum += cols[i][j];
			}
		}
		if(rowSum != colSum) throw new InvalidPuzzleException("Invalid puzzle");
		else return new Pair<int[][], int[][]>(rows, cols);
	}
	private static boolean solveNonogram(boolean[][] puzzle, int[][] rows, int[][] cols, int row, int col) {
		if(row == puzzle.length) return true;
		else {
			puzzle[row][col] = true;
			if(verifyNonogram(puzzle, rows, cols, row, col)) {
				int tmpcol = col + 1;
				int tmprow = row + (tmpcol / puzzle[0].length);
				tmpcol = tmpcol % puzzle[0].length;
				if(solveNonogram(puzzle, rows, cols, tmprow, tmpcol)) {
					return true;
				}
			}
			puzzle[row][col] = false;
			if(verifyNonogram(puzzle, rows, cols, row, col)) {
				int tmpcol = col + 1;
				int tmprow = row + (tmpcol / puzzle[0].length);
				tmpcol = tmpcol % puzzle[0].length;
				if(solveNonogram(puzzle, rows, cols, tmprow, tmpcol)) {
					return true;
				}
			}
			return false;
		}
	}
	private static boolean verifyNonogram(boolean[][] puzzle, int[][] rows, int[][] cols, int row, int col) {	
		if(!verifyRow(puzzle[row], rows[row], col)) return false;
		boolean[] tmpRow = new boolean[rows.length];
		for(int i = 0; i < rows.length; i++) {
			tmpRow[i] = puzzle[i][col];
		}
		if(!verifyRow(tmpRow,cols[col], row)) return false;
		return true;
	}
	private static boolean verifyRow(boolean[] row, int[] rowRequirement, int col) {
		int k = 0;
		int consecutive = 0;
		boolean last = false;
		for(int i = 0; i <= col; i++) {
			if(row[i]) {
				consecutive++;
				if(!last) {
					if(k >= rowRequirement.length) {
						return false;
					}
				}
				last = true;
			}
			else {
				if(last) {
					if(rowRequirement[k] != consecutive) {
						return false;
					}
					consecutive = 0;
					k++;
				}
				last = false;
			}
		}
		if(col == (row.length - 1)) {
			if(last) {
				return k == (rowRequirement.length - 1) && consecutive == rowRequirement[k];
			}
			else {
				return k == rowRequirement.length;
			}
		}
		else {
			if(last) {
				return consecutive <= rowRequirement[k];
			}
		}
		return true;
	}
	private static void printNonogram(boolean[][] puzzle) {
		for(int row = 0; row < puzzle.length; row++) {
			for(int col = 0; col < puzzle[row].length; col++) {
				if(puzzle[row][col]) System.out.print("* ");
				else System.out.print("  ");
			}
			System.out.println();
		}
	}
}
