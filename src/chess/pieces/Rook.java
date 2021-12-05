package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Colour;

public class Rook extends ChessPiece{

	public Rook(Board board, Colour colour) {
		super(board, colour);
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position pos = new Position(0,0);
		
		//above
		pos.setValues(position.getRow() - 1, position.getColumn());
		
		while(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
			pos.setRow(pos.getRow() - 1);;
		}
		
		if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
		}
		
		//left
		pos.setValues(position.getRow(), position.getColumn() - 1);
		
		while(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
			pos.setColumn(pos.getColumn() - 1);;
		}
		
		if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
		}
		
		//right
		pos.setValues(position.getRow(), position.getColumn() + 1);
		
		while(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
			pos.setColumn(pos.getColumn() + 1);;
		}
		
		if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
		}
		
		//below
		pos.setValues(position.getRow() + 1, position.getColumn());
		
		while(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
			pos.setRow(pos.getRow() + 1);;
		}
		
		if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;
		}
		
		return mat;
	}
	
	@Override
	public String toString(){
		return "R";
	}
	
}
