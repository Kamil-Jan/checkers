package com.checkers.checker;

import com.checkers.board.Board;
import com.checkers.board.BoardPosition;
import com.checkers.moves.Move;

import java.util.function.Function;

public class Checker {
    public enum Side {
        BLACK, WHITE
    }

    private final Side side;
    private char symbol;
    private boolean king;
    private BoardPosition position;

    public Checker(Side side) {
        this.side = side;
        king = false;
        setSymbol();
    }

    public Checker(Side side, BoardPosition pos) {
        this.side = side;
        position = pos;
        king = false;
        setSymbol();
    }

    public BoardPosition getPosition() {
        return position;
    }

    public void setPosition(BoardPosition pos) {
        position = pos;
    }

    public Side getSide() {
        return side;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isKing() {
        return king;
    }

    public void turnIntoKing() {
        king = true;
        setSymbol();
    }

    public boolean isOnKingRow() {
        if (side == Side.BLACK) {
            return (position.getBoardRow() == 1);
        } else {
            return (position.getBoardRow() == 8);
        }
    }

    public boolean canMove(Board board) {
        if (position == null)
            return false;

        int row = position.getBoardRow();
        int col = position.getBoardCol();

        Function<Move, Boolean> moveFilter = Move::isValidMove;
        if (!king) {
            if (isFilteredNormalMove(row, col, row - 1, col - 1, board, moveFilter)) return true;
            if (isFilteredNormalMove(row, col, row - 1, col + 1, board, moveFilter)) return true;
            if (isFilteredNormalMove(row, col, row + 1, col - 1, board, moveFilter)) return true;
            return isFilteredNormalMove(row, col, row + 1, col + 1, board, moveFilter);
        } else {
            Function<Integer, Integer> add = (Integer x) -> x + 1;
            Function<Integer, Integer> subtract = (Integer x) -> x - 1;

            if (isFilteredKingMove(row, col, add, add, board, moveFilter)) return true;
            if (isFilteredKingMove(row, col, add, subtract, board, moveFilter)) return true;
            if (isFilteredKingMove(row, col, subtract, add, board, moveFilter)) return true;
            return isFilteredKingMove(row, col, subtract, subtract, board, moveFilter);
        }
    }

    public boolean canCapture(Board board) {
        if (position == null) return false;

        int row = position.getBoardRow();
        int col = position.getBoardCol();

        if (!king) {
            Function<Move, Boolean> captureFilter =
                (Move move) -> (move.isValidNormalCapture() && move.isValidMove());

            if (isFilteredNormalMove(row, col, row - 2, col - 2, board, captureFilter)) return true;
            if (isFilteredNormalMove(row, col, row - 2, col + 2, board, captureFilter)) return true;
            if (isFilteredNormalMove(row, col, row + 2, col - 2, board, captureFilter)) return true;
            return isFilteredNormalMove(row, col, row + 2, col + 2, board, captureFilter);
        } else {
            Function<Move, Boolean> captureFilter =
                (Move move) -> (move.isValidKingCapture() && move.isValidMove());

            Function<Integer, Integer> add = (Integer x) -> x + 1;
            Function<Integer, Integer> subtract = (Integer x) -> x - 1;

            if (isFilteredKingMove(row, col, add, add, board, captureFilter)) return true;
            if (isFilteredKingMove(row, col, add, subtract, board, captureFilter)) return true;
            if (isFilteredKingMove(row, col, subtract, add, board, captureFilter)) return true;
            return isFilteredKingMove(row, col, subtract, subtract, board, captureFilter);
        }
    }

    private boolean isFilteredNormalMove(int startRow, int startCol, int endRow, int endCol,
                                         Board board, Function<Move, Boolean> filter) {
        if (BoardPosition.isValidPosition(startRow, startCol) &&
            BoardPosition.isValidPosition(endRow, endCol)) {

            BoardPosition startBPos = new BoardPosition(startRow, startCol);
            BoardPosition endBPos = new BoardPosition(endRow, endCol);
            Move move = new Move(startBPos, endBPos, board);
            return filter.apply(move);
        }
        return false;
    }

    private boolean isFilteredKingMove(int startRow, int startCol,
                                      Function<Integer, Integer> rowOperation,
                                      Function<Integer, Integer> colOperation,
                                      Board board, Function<Move, Boolean> filter) {
        int curRow = rowOperation.apply(startRow);
        int curCol = colOperation.apply(startCol);
        while (BoardPosition.isValidPosition(curRow, curCol)) {
            if (isFilteredKingMove(startRow, startCol, curRow, curCol, board, filter)) return true;
            curRow = rowOperation.apply(curRow);
            curCol = colOperation.apply(curCol);
        }
        return false;
    }

    private boolean isFilteredKingMove(int startRow, int startCol, int endRow, int endCol,
                                      Board board, Function<Move, Boolean> filter) {
        if (BoardPosition.isValidPosition(startRow, startCol) &&
            BoardPosition.isValidPosition(endRow, endCol)) {

            BoardPosition startBPos = new BoardPosition(startRow, startCol);
            BoardPosition endBPos = new BoardPosition(endRow, endCol);
            Move move = new Move(startBPos, endBPos, board);
            return filter.apply(move);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(
            "Side=%s; position=%s, isKing=%b; symbol=%s",
            side, position, king, symbol
        );
    }

    private void setSymbol() {
        if (side == Side.WHITE) {
            if (king)
                symbol = '⛃';
            else
                symbol = '⛂';
        } else {
            if (king)
                symbol = '⛁';
            else
                symbol = '⛀';
        }
    }
}

