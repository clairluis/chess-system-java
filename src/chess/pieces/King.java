package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Colour;

public class King extends ChessPiece{
	
	private ChessMatch chessMatch;

	public King(Board board, Colour colour, ChessMatch chessMatch) {
		super(board, colour);
		this.chessMatch = chessMatch;
	}
	
	private boolean canMove(Position position) {
		ChessPiece piece = (ChessPiece) getBoard().piece(position);
		
		return piece == null || piece.getColour() != getColour();
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece piece = (ChessPiece) getBoard().piece(position);
		
		return piece != null && piece instanceof Rook && piece.getColour() == getColour() && piece.getMoveCount() == 0;
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position pos = new Position(0,0);
		
		//above
		pos.setValues(position.getRow() - 1, position.getColumn());
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//left
		pos.setValues(position.getRow(), position.getColumn() - 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//right
		pos.setValues(position.getRow(), position.getColumn() + 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//below
		pos.setValues(position.getRow() + 1, position.getColumn());
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//nw
		pos.setValues(position.getRow() - 1, position.getColumn() - 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//ne
		pos.setValues(position.getRow() - 1, position.getColumn() + 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//sw
		pos.setValues(position.getRow() + 1, position.getColumn() - 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//se
		pos.setValues(position.getRow() + 1, position.getColumn() + 1);
		
		if(getBoard().positionExists(pos) && canMove(pos)) {
			mat[pos.getRow()][pos.getColumn()] = true;		
		}
		
		//castling special move
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			//castling kingside rook
			Position posRook1 = new Position(position.getRow(), position.getColumn() + 3);
			
			if(testRookCastling(posRook1)) {
				Position pos1 = new Position(position.getRow(), position.getColumn() + 1);
				Position pos2 = new Position(position.getRow(), position.getColumn() + 2);
				
				if(getBoard().piece(pos1) == null && getBoard().piece(pos2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			
			//castling queenside rook
			Position posRook2 = new Position(position.getRow(), position.getColumn() - 4);
			
			if(testRookCastling(posRook2)) {
				Position pos1 = new Position(position.getRow(), position.getColumn() - 1);
				Position pos2 = new Position(position.getRow(), position.getColumn() - 2);
				Position pos3 = new Position(position.getRow(), position.getColumn() - 3);
				
				if(getBoard().piece(pos1) == null && getBoard().piece(pos2) == null && getBoard().piece(pos3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}
		
		return mat;
	}

	@Override
	public String toString(){
		return "K";
	}
	
}
