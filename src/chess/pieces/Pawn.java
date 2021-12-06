package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Colour;

public class Pawn extends ChessPiece{
	
	private ChessMatch chessMatch;

	public Pawn(Board board, Colour colour, ChessMatch chessMatch) {
		super(board, colour);
		this.chessMatch = chessMatch;
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
			
			// special move en passant white
			if(position.getRow() == 3) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() - 1][left.getColumn()] = true;
				}
				
				Position right = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					mat[right.getRow() - 1][right.getColumn()] = true;
				}
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
			
			// special move en passant black
			if(position.getRow() == 4) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() + 1][left.getColumn()] = true;
				}
				
				Position right = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					mat[right.getRow() + 1][right.getColumn()] = true;
				}
			}
		}
		
		return mat;
	}
	
	@Override
	public String toString(){
		return "P";
	}

}
