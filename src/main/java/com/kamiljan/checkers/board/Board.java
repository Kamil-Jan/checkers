package com.kamiljan.checkers.board;

import com.kamiljan.checkers.checker.Checker;
import com.kamiljan.checkers.moves.Move;

import java.util.Collection;
import java.util.HashMap;

public class Board {
    private final HashMap<BoardPosition, Checker> board
        = new HashMap<>();
    private int whiteSideCheckerCount;
    private int blackSideCheckerCount;
    private Checker.Side winner;

    public void initBoard() {
        whiteSideCheckerCount = 12;
        for (int pos = 1; pos <= 12; pos++) {
            BoardPosition BPos = new BoardPosition(pos);
            board.put(BPos, new Checker(Checker.Side.BLACK, BPos));
        }

        blackSideCheckerCount = 12;
        for (int pos = 21; pos <= 32; pos++) {
            BoardPosition BPos = new BoardPosition(pos);
            board.put(BPos, new Checker(Checker.Side.WHITE, BPos));
        }
    }

    public Checker getChecker(int pos) throws IllegalArgumentException {
        return getChecker(new BoardPosition(pos));
    }

    public Checker getChecker(String pos) throws IllegalArgumentException {
        return getChecker(new BoardPosition(pos));
    }

    public Checker getChecker(int row, int col) throws IllegalArgumentException {
        return getChecker(new BoardPosition(row, col));
    }

    public Checker getChecker(BoardPosition bPos) throws IllegalArgumentException {
        if (!isOccupied(bPos))
            throw new IllegalArgumentException("Position is not occupied: " + bPos);
        return board.get(bPos);
    }

    public Collection<Checker> getCheckers() {
        return board.values();
    }

    public int getWhiteSideCheckerCount() {
        return whiteSideCheckerCount;
    }

    public int getBlackSideCheckerCount() {
        return blackSideCheckerCount;
    }

    private void printRowNames() {
        System.out.print("  ");
        char[] rows = "abcdefgh".toCharArray();
        for (char ch : rows) {
            System.out.print(" " + ch + " ");
        }
        System.out.println();
    }

    public void display() {
        String verLine = "⏐";
        String horLine = "⎯";
        String emptyField = "__";

        printRowNames();
        System.out.println(horLine.repeat(27));
        for (int pos = 1; pos <= 32; pos++) {
            BoardPosition bPos = new BoardPosition(pos);
            int row = bPos.getBoardRow();

            if (pos % 4 == 1)
                System.out.print(row + verLine);

            if (row % 2 == 0) {
                if (isOccupied(bPos))
                    System.out.print(emptyField + verLine +  getChecker(bPos).getSymbol() + " " + verLine);
                else
                    System.out.print(emptyField + verLine + emptyField + verLine);
            } else {
                if (isOccupied(bPos))
                    System.out.print(getChecker(bPos).getSymbol() + " " + verLine + emptyField + verLine);
                else
                    System.out.print(emptyField + verLine + emptyField + verLine);
            }

            if (pos % 4 == 0) {
                System.out.println(row);
            }
        }

        System.out.println(horLine.repeat(27));
        printRowNames();
    }

    public void moveChecker(int start, int end) throws IllegalArgumentException {
        BoardPosition bPosStart = new BoardPosition(start);
        BoardPosition bPosEnd = new BoardPosition(end);
        moveChecker(bPosStart, bPosEnd);
    }

    public void moveChecker(String start, String end) throws IllegalArgumentException {
        BoardPosition bPosStart = new BoardPosition(start);
        BoardPosition bPosEnd = new BoardPosition(end);
        moveChecker(bPosStart, bPosEnd);
    }

    public void moveChecker(Move move) throws IllegalArgumentException {
        BoardPosition bPosStart = move.getStartPosition();
        BoardPosition bPosEnd = move.getEndPosition();
        moveChecker(bPosStart, bPosEnd);
    }

    public void moveChecker(BoardPosition start, BoardPosition end) throws IllegalArgumentException {
        if (!isOccupied(start))
            throw new IllegalArgumentException("Start position is not occupied: " + start);

        Checker movingChecker = board.get(start);
        movingChecker.setPosition(end);
        board.remove(start);
        board.put(end, movingChecker);
    }

    public void putChecker(int pos, Checker.Side side) {
        putChecker(new BoardPosition(pos), side);
    }

    public void putChecker(int row, int col, Checker.Side side) {
        putChecker(new BoardPosition(row, col), side);
    }

    public void putChecker(String pos, Checker.Side side) {
        putChecker(new BoardPosition(pos), side);
    }

    public void putChecker(BoardPosition BPos, Checker.Side side) {
        board.put(BPos, new Checker(side, BPos));
        if (side == Checker.Side.BLACK)
            blackSideCheckerCount++;
        else
            whiteSideCheckerCount++;
    }

    public void removeChecker(int pos) throws IllegalArgumentException {
        removeChecker(new BoardPosition(pos));
    }

    public void removeChecker(int row, int col) throws IllegalArgumentException {
        removeChecker(new BoardPosition(row, col));
    }

    public void removeChecker(String pos) throws IllegalArgumentException {
        removeChecker(new BoardPosition(pos));
    }

    public void removeChecker(BoardPosition bPos) throws IllegalArgumentException {
        if (!isOccupied(bPos))
            throw new IllegalArgumentException("Position is not occupied: " + bPos);

        Checker checker = board.get(bPos);
        if (checker.getSide().equals(Checker.Side.BLACK))
            blackSideCheckerCount--;
        else
            whiteSideCheckerCount--;

        board.remove(bPos);
    }

    public boolean isOccupied(int pos) {
        return isOccupied(new BoardPosition(pos));
    }

    public boolean isOccupied(String pos) {
        return isOccupied(new BoardPosition(pos));
    }

    public boolean isOccupied(int row, int col) {
        return isOccupied(new BoardPosition(row, col));
    }

    public boolean isOccupied(BoardPosition bPos) {
        return board.containsKey(bPos);
    }

    public Checker.Side getWinner() {
        return winner;
    }

    public boolean isWin() {
        return (isWhiteWin() || isBlackWin());
    }

    public boolean isWhiteWin() {
        boolean isWinner = (blackSideCheckerCount == 0 || cantSideMove(Checker.Side.BLACK));
        if (isWinner) winner = Checker.Side.WHITE;
        return isWinner;
    }

    public boolean isBlackWin() {
        boolean isWinner = (whiteSideCheckerCount == 0 || cantSideMove(Checker.Side.WHITE));
        if (isWinner) winner = Checker.Side.BLACK;
        return isWinner;
    }

    public boolean cantSideMove(Checker.Side side) {
        for (Checker checker : getCheckers()) {
            if (checker.getSide() == side && checker.canMove(this)) {
                return false;
            }
        }
        return true;
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}

