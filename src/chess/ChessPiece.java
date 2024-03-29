package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
	
	private Colour colour;
	private int moveCount;

	public ChessPiece(Board board, Colour colour) {
		super(board);
		this.colour = colour;
	}

	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
		
		return chessPiece != null && chessPiece.getColour() != colour;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
	
	public Colour getColour() {
		return colour;
	}
	
	public int getMoveCount() {
		return moveCount;
	}

}
