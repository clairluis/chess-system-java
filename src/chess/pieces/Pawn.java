package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Colour;

public class Pawn extends ChessPiece{

	public Pawn(Board board, Colour colour) {
		super(board, colour);
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position pos = new Position(0,0);
		
		if(getColour() == Colour.WHITE) {
			//white pawn moves always one square to the front
			pos.setValues(position.getRow() - 1, position.getColumn());
			
			if(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
			//white pawn can move two squares in the first move if there is no other piece in the way
			pos.setValues(position.getRow() - 2, position.getColumn());
			
			Position pos2 = new Position(position.getRow() - 1, position.getColumn());
			
			if(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos) && getMoveCount() == 0 && getBoard().positionExists(pos2) && !getBoard().isThereAPiece(pos2)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
			//white pawn can move to the left diagonal if there is an opponent piece
			pos.setValues(position.getRow() - 1, position.getColumn() - 1);
			
			if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
			//white pawn can move to the right diagonal if there is an opponent piece
			pos.setValues(position.getRow() - 1, position.getColumn() + 1);
			
			if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
		} else {
			//black pawn moves always one square to the front
			pos.setValues(position.getRow() + 1, position.getColumn());
			
			if(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
			//black pawn can move two squares in the first move if there is no other piece in the way
			pos.setValues(position.getRow() + 2, position.getColumn());
			
			Position pos2 = new Position(position.getRow() + 1, position.getColumn());
			
			if(getBoard().positionExists(pos) && !getBoard().isThereAPiece(pos) && getMoveCount() == 0 && getBoard().positionExists(pos2) && !getBoard().isThereAPiece(pos2)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
			//black pawn can move to the left diagonal if there is an opponent piece
			pos.setValues(position.getRow() + 1, position.getColumn() - 1);
			
			if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
			
			//black pawn can move to the right diagonal if there is an opponent piece
			pos.setValues(position.getRow() + 1, position.getColumn() + 1);
			
			if(getBoard().positionExists(pos) && isThereOpponentPiece(pos)) {
				mat[pos.getRow()][pos.getColumn()] = true;
			}
		}
		
		return mat;
	}
	
	@Override
	public String toString(){
		return "P";
	}

}
