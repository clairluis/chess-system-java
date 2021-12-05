package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Colour;

public class Knight extends ChessPiece{

	public Knight(Board board, Colour colour) {
		super(board, colour);
	}
	
	private boolean canMove(Position position) {
		ChessPiece piece = (ChessPiece) getBoard().piece(position);
		
		return piece == null || piece.getColour() != getColour();
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position pos = new Position(0,0);
		
		pos.setValues(position.getRow() - 1, position.getColumn() - 2);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() - 2, position.getColumn() - 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() - 2, position.getColumn() + 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() - 1, position.getColumn() + 2);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() + 1, position.getColumn() + 2);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() + 2, position.getColumn() + 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() + 2, position.getColumn() - 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		pos.setValues(position.getRow() + 1, position.getColumn() - 2);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		return mat;
	}
	
	@Override
	public String toString(){
		return "N";
	}

}
