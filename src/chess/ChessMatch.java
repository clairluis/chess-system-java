package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private boolean check;
	private boolean checkMate;
	private Board board;
	private Colour currentPlayer;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	private List<Piece> piecesOnBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		this.board = new Board(8,8);
		turn = 1;
		currentPlayer = Colour.WHITE;
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j);
			}
		}
		
		return mat;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnBoard.add(piece);
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		
		validateSourcePosition(position);
		
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		
		Piece capturedPiece = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		ChessPiece movedPiece = (ChessPiece) board.piece(target);
		
		// special move promotion
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if(movedPiece.getColour() == Colour.WHITE && target.getRow() == 0 || movedPiece.getColour() == Colour.BLACK && target.getRow() == 7) {
				promoted = (ChessPiece) board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		
		// special move en passant
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece) capturedPiece;
		
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		
		if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			return promoted;
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece piece = board.removePiece(pos);
		
		piecesOnBoard.remove(piece);
		
		ChessPiece newPiece = newPiece(type, promoted.getColour());
		
		board.placePiece(newPiece, pos);
		piecesOnBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Colour colour) {
		if(type.equals("B")) return new Bishop(board, colour);
		if(type.equals("N")) return new Knight(board, colour);
		if(type.equals("Q")) return new Queen(board, colour);
		return new Rook(board, colour);
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.isThereAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		
		if(currentPlayer != ((ChessPiece) board.piece(position)).getColour()) {
			throw new ChessException("The chosen piece is not yours.");
		}
		
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position.");
		}
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece piece = (ChessPiece) board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);

		piece.increaseMoveCount();
		board.placePiece(piece, target);
		
		if(capturedPiece != null) {
			piecesOnBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		//castling kingside rook
		if(piece instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
			Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
			board.placePiece(rook, targetRook);
			rook.increaseMoveCount();
		}
		
		//castling queenside rook
		if(piece instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
			Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
			board.placePiece(rook, targetRook);
			rook.increaseMoveCount();
		}
		
		//special move en passant
		if(piece instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if(piece.getColour() == Colour.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece targetPiece = (ChessPiece) board.removePiece(target);
		
		targetPiece.decreaseMoveCount();
		board.placePiece(targetPiece, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnBoard.add(capturedPiece);
		}
		
		//castling kingside rook
		if(capturedPiece instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
			Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
			board.placePiece(rook, sourceRook);
			rook.decreaseMoveCount();
		}
		
		//castling queenside rook
		if(capturedPiece instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
			Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
			board.placePiece(rook, sourceRook);
			rook.decreaseMoveCount();
		}
		
		//special move en passant
		if(targetPiece instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece) board.removePiece(target);
				Position pawnPosition;
				if(targetPiece.getColour() == Colour.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				} else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Colour.WHITE) ? Colour.BLACK : Colour.WHITE;
	}
	
	private Colour opponent(Colour colour) {
		return (colour == Colour.WHITE) ? Colour.BLACK : Colour.WHITE;
	}
	
	private ChessPiece king(Colour colour) {
		List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColour() == colour).collect(Collectors.toList());
		
		for(Piece piece : list) {
			if(piece instanceof King) {
				return (ChessPiece) piece;
			}
		}
		
		throw new IllegalStateException("There is no " + colour + " king on the board.");
	}
	
	private boolean testCheck(Colour colour) {
		Position kingPosition = king(colour).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColour() == opponent(colour)).collect(Collectors.toList());
		
		for(Piece opponentPiece : opponentPieces) {
			boolean [][] mat = opponentPiece.possibleMoves();
			
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean testCheckMate(Colour colour) {
		if(!testCheck(colour)) {
			return false;
		}
		
		List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColour() == colour).collect(Collectors.toList());
		
		for(Piece piece : list) {
			boolean [][] mat = piece.possibleMoves();
			
			for(int i = 0; i < board.getRows(); i++) {
				for(int j = 0; j < board.getColumns(); j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece) piece).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source,target);
						boolean testCheck = testCheck(colour);
						undoMove(source, target, capturedPiece);
						
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Colour.WHITE));
		placeNewPiece('b', 1, new Knight(board, Colour.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Colour.WHITE));
		placeNewPiece('d', 1, new Queen(board, Colour.WHITE));
        placeNewPiece('e', 1, new King(board, Colour.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Colour.WHITE));
        placeNewPiece('g', 1, new Knight(board, Colour.WHITE));
        placeNewPiece('h', 1, new Rook(board, Colour.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Colour.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Colour.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Colour.BLACK));
        placeNewPiece('b', 8, new Knight(board, Colour.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Colour.BLACK));
        placeNewPiece('d', 8, new Queen(board, Colour.BLACK));
        placeNewPiece('e', 8, new King(board, Colour.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Colour.BLACK));
        placeNewPiece('g', 8, new Knight(board, Colour.BLACK));
        placeNewPiece('h', 8, new Rook(board, Colour.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Colour.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Colour.BLACK, this));
	}

	public int getTurn() {
		return turn;
	}

	public Colour getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}

}
