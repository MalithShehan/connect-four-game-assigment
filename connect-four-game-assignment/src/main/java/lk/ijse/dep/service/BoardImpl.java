package lk.ijse.dep.service;

import lk.ijse.dep.controller.BoardController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardImpl implements Board{
    private Piece[][] pieces;
    private BoardUI boardUI;

    public Piece player;
    public int col;

    public BoardImpl(BoardUI boardUI) {

        pieces = new Piece[NUM_OF_COLS][NUM_OF_ROWS];

        this.boardUI = boardUI;

        for (int i = 0; i < NUM_OF_COLS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                pieces[i][j] = Piece.EMPTY;
            }
        }
    }

    public BoardImpl(Piece[][] pieces, BoardUI boardUI) {
        this.pieces=new Piece[6][5];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                this.pieces[i][j]=pieces[i][j];
            }
        }
        this.boardUI = boardUI;
    }

    @Override
    public BoardUI getBoardUI() {
        return boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {

        for (int i = 0; i < NUM_OF_ROWS; i++) {
            if (pieces[col][i] == Piece.EMPTY) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isLegalMove(int col) {
        int rowNo = findNextAvailableSpot(col);
        if (rowNo > -1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean exitsLegalMoves() {
        for (int i = 0; i < NUM_OF_COLS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                if (pieces[i][j] == Piece.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    public Piece getPlayer() {
        return player;
    }

    @Override
    public void updateMove(int col, Piece move) {

        this.col = col;
        this.player = move;

        for (int i = 0; i < 5; i++) {
            if (pieces[col][i] == Piece.EMPTY) {
                pieces[col][i] = move;
                break;
            }
        }

    }

    @Override
    public Winner findWinner() {

        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[0].length; j++) {
                Piece currentPiece = pieces[i][j];


                if (currentPiece != Piece.EMPTY) {

                    if(j + 3 < pieces[0].length){
                        if (currentPiece == pieces[i][j + 1]){
                            if (currentPiece == pieces[i][j + 2]){
                                if(currentPiece == pieces[i][j + 3]){
                                    return new Winner(currentPiece, i, j, i, (j+3));
                                }
                            }
                        }
                    }

                    if(i + 3 < pieces.length){
                        if (currentPiece == pieces[i + 1][j]){
                            if (currentPiece == pieces[i + 2][j]){
                                if(currentPiece == pieces[i + 3][j]){
                                    return new Winner(currentPiece, i, j, (i + 3), j);
                                }
                            }
                        }
                    }
                }
            }
        }

        return new Winner(Piece.EMPTY);
    }



    @Override
    public void updateMove(int col, int row, Piece move) {
        pieces[col][row] = move;
    }

    @Override
    public BoardImpl getBoardImpl() {
        return this;
    }
    public Piece[][] getPieces() {
        return pieces;
    }
    public boolean getStatus(){
        if (!exitsLegalMoves()){
            return false;
        }

        Winner winner=findWinner();
        if (winner.getWinningPiece() != Piece.EMPTY){

            return false;
        }
        return true;
    }
    public BoardImpl getRandomLeagalNextMove() {
        final List<BoardImpl> legalMoves = getAllLegalNextMoves();

        if (legalMoves.isEmpty()) {
            return null;
        }

        final int random= new Random().nextInt(legalMoves.size());
        return legalMoves.get(random);

    }

    public List<BoardImpl> getAllLegalNextMoves() {

        Piece nextPiece = player == Piece.BLUE?Piece.GREEN:Piece.BLUE;

        List<BoardImpl> nextMoves = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int raw=findNextAvailableSpot(i);
            if (raw!=-1){
                BoardImpl legalMove=new BoardImpl(this.pieces,this.boardUI);
                legalMove.updateMove(i,nextPiece);
                nextMoves.add(legalMove);
            }
        }
        return  nextMoves;
    }
}
