package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Colour;

public class Rook extends ChessPiece{

	public Rook(Board board, Colour colour) {
		super(board, colour);
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		return mat;
	}
	
	@Override
	public String toString(){
		return "R";
	}
	
}
