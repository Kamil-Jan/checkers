package com.checkers.moves;

import com.checkers.board.Board;
import com.checkers.board.BoardPosition;
import com.checkers.checker.Checker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Move {
    private final BoardPosition startPos;
    private final BoardPosition endPos;
    private final Board board;
    private boolean validMove;
    private boolean validNormalMove;
    private boolean validKingMove;
    private boolean validNormalCapture;
    private boolean validKingCapture;

    private final Checker movingChecker;
    private final List<Checker> capturedCheckers = new ArrayList<>();

    public Move(int startPos, int endPos, Board board) {
        this.startPos = new BoardPosition(startPos);
        this.endPos = new BoardPosition(endPos);
        this.board = board;
        this.movingChecker = board.getChecker(this.startPos);
        setValidBooleans();
    }

    public Move(String startPos, String endPos, Board board) {
        this.startPos = new BoardPosition(startPos);
        this.endPos = new BoardPosition(endPos);
        this.board = board;
        this.movingChecker = board.getChecker(this.startPos);
        setValidBooleans();
    }

    public Move(BoardPosition startPos,
                BoardPosition endPos,
                Board board) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.board = board;
        this.movingChecker = board.getChecker(this.startPos);
        setValidBooleans();
    }

    public boolean isValidMove() {
        return validMove;
    }

    public boolean isValidNormalMove() {
        return validNormalMove;
    }

    public boolean isValidKingMove() {
        return validKingMove;
    }

    public boolean isValidNormalCapture() {
        return validNormalCapture;
    }

    public boolean isValidKingCapture() {
        return validKingCapture;
    }

    public List<Checker> getCapturedCheckers() {
        return capturedCheckers;
    }

    public BoardPosition getStartPosition() {
        return startPos;
    }

    public BoardPosition getEndPosition() {
        return endPos;
    }

    private void setValidBooleans() {
        this.validNormalMove = isValidNormalMoveUtil();
        this.validKingMove = isValidKingMoveUtil();
        this.validNormalCapture = isValidNormalCaptureUtil();
        this.validKingCapture = isValidKingCaptureUtil();
        this.validMove = isValidMoveUtil();
    }

    private boolean isValidMoveUtil() {
        if (!movingChecker.isKing())
            return (isValidNormalCapture() || isValidNormalMove());
        else {
            return (isValidKingCapture() || isValidKingMove());
        }
    }

    private boolean isValidNormalMoveUtil() {
        if (!board.isOccupied(startPos))
            return false;

        if (board.isOccupied(endPos))
            return false;

        if (movingChecker.isKing())
            return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        if (movingChecker.getSide() == Checker.Side.BLACK)
            return endRow == startRow - 1 && Math.abs(endCol - startCol) == 1;
        else
            return endRow == startRow + 1 && Math.abs(endCol - startCol) == 1;
    }

    private boolean isValidKingMoveUtil() {
        if (!board.isOccupied(startPos))
            return false;

        if (board.isOccupied(endPos))
            return false;

        if (!movingChecker.isKing())
            return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        if (rowDiff == colDiff) {
            int curRow = startRow;
            int curCol = startCol;

            Function<Integer, Integer> add = (Integer x) -> x + 1;
            Function<Integer, Integer> subtract = (Integer x) -> x - 1;

            Function<Integer, Integer> rowOperation;
            Function<Integer, Integer> colOperation;
            if (curRow < endRow)
                rowOperation = add;
            else
                rowOperation = subtract;

            if (curCol < endCol)
                colOperation = add;
            else
                colOperation = subtract;

            curRow = rowOperation.apply(curRow);
            curCol = colOperation.apply(curCol);

            while (curRow != endRow && curCol != endRow) {
                if (board.isOccupied(curRow, curCol)) {
                    if (board.isOccupied(curRow + 1, curCol + 1)) return false;
                }
                curRow = rowOperation.apply(curRow);
                curCol = colOperation.apply(curCol);
            }
            return true;
        }
        return false;
    }

    private boolean isValidNormalCaptureUtil() {
        if (!board.isOccupied(startPos))
            return false;

        if (board.isOccupied(endPos))
            return false;

        if (movingChecker.isKing())
            return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        if (rowDiff == 2 && colDiff == 2) {
            int rowCaptured = (startRow + endRow) / 2;
            int colCaptured = (startCol + endCol) / 2;
            if (board.isOccupied(rowCaptured, colCaptured) &&
                board.getChecker(rowCaptured, colCaptured).getSide() !=
                board.getChecker(startRow, startCol).getSide()) {

                if (!capturedCheckers.contains(board.getChecker(rowCaptured, colCaptured)))
                    capturedCheckers.add(board.getChecker(rowCaptured, colCaptured));
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isValidKingCaptureUtil() {
        if (!board.isOccupied(startPos))
            return false;

        if (board.isOccupied(endPos))
            return false;

        if (!movingChecker.isKing())
            return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        if (rowDiff != colDiff)
            return false;

        int curRow = startRow;
        int curCol = startCol;

        Function<Integer, Integer> add = (Integer x) -> x + 1;
        Function<Integer, Integer> subtract = (Integer x) -> x - 1;

        Function<Integer, Integer> rowOperation;
        Function<Integer, Integer> colOperation;
        Function<Integer, Boolean> rowLoopStop;
        Function<Integer, Boolean> colLoopStop;
        if (curRow < endRow) {
            rowOperation = add;
            rowLoopStop = (Integer x) -> x > endRow ;
        } else {
            rowOperation = subtract;
            rowLoopStop = (Integer x) -> x < endRow ;
        }

        if (curCol < endCol) {
            colOperation = add;
            colLoopStop = (Integer x) -> x > endCol ;
        }
        else {
            colOperation = subtract;
            colLoopStop = (Integer x) -> x < endCol ;
        }

        curRow = rowOperation.apply(curRow);
        curCol = colOperation.apply(curCol);

        while (!colLoopStop.apply(curCol) && !rowLoopStop.apply(curRow)) {
            if (board.isOccupied(curRow, curCol)) {
                if (board.isOccupied(rowOperation.apply(curRow), colOperation.apply(curCol))) return false;

                if (board.getChecker(curRow, curCol).getSide() != movingChecker.getSide() &&
                    !capturedCheckers.contains(board.getChecker(curRow, curCol))) {

                    capturedCheckers.add(board.getChecker(curRow, curCol));
                }
            }
            curRow = rowOperation.apply(curRow);
            curCol = colOperation.apply(curCol);
        }

        return (capturedCheckers.size() != 0);
    }

    @Override
    public String toString() {
        return String.format(
            "Move from %s to %s; isValidMove? %s; capturedCheckers=%s",
            startPos, endPos, validMove, capturedCheckers
        );
    }

}
