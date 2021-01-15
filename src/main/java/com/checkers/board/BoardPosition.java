package com.checkers.board;

import java.security.InvalidParameterException;

public class BoardPosition {
    String stringPosition;
    int intPosition;

    public BoardPosition(int pos) {
        if (isValidPosition(pos)) {
            stringPosition = convertIntPosToString(pos);
            intPosition = pos;
        }
        else
            throw new InvalidParameterException("Invalid position: " + pos);
    }

    public BoardPosition(String pos) {
        if (isValidPosition(pos)) {
            stringPosition = pos;
            intPosition = convertStringPosToInt(pos);
        }
        else
            throw new InvalidParameterException("Invalid position: " + pos);
    }

    public BoardPosition(int row, int col) {
        if (isValidPosition(row, col)) {
            stringPosition = convertRowColPosToString(row, col);
            intPosition = convertStringPosToInt(stringPosition);
        }
        else
            throw new InvalidParameterException("Invalid position: " + row + " " + col);
    }

    public String getStringPosition() {
        return stringPosition;
    }

    public int getIntPosition() {
        return intPosition;
    }

    public int getBoardRow() {
        return 8 - (intPosition - 1) / 4;
    }

    public int getBoardCol() {
        int row = 8 - getBoardRow();
        int col;
        if (row % 2 == 0) {
            col = 2 * (intPosition - row * 4);
        } else {
            col = 2 * (intPosition - row * 4) - 1;
        }
        return col;
    }

    public static String convertRowColPosToString(int row, int col) {
        Integer[] rows = { 1, 2, 3, 4, 5, 6, 7, 8 };
        Character[] columns = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        try {
            return columns[col - 1].toString() + rows[row - 1].toString();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public static String convertIntPosToString(int pos) {
        Integer[] rows = { 8, 7, 6, 5, 4, 3, 2, 1 };
        Character[] columns = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

        int row = (pos - 1) / 4;
        int col;
        if (row % 2 == 0) {
            col = 2 * (pos - row * 4) - 1;
        } else {
            col = 2 * (pos - row * 4) - 2;
        }

        return columns[col].toString() + rows[row].toString();
    }

    public static int convertStringPosToInt(String pos) {
        if (pos.length() != 2) {
            return -1;
        }

        int row = 8 - ((int) pos.charAt(1) - 48);
        int col = pos.charAt(0) - 96;
        if (row % 2 == 0) {
            if (col % 2 == 0)
                return row * 4 + col / 2;
            return -1;
        } else {
            if (col % 2 == 0)
                return -1;
            return row * 4 + col / 2 + 1;
        }
    }

    public static boolean isValidPosition(int pos) {
        return pos >= 1 && pos <= 32;
    }

    public static boolean isValidPosition(String pos) {
        return isValidPosition(BoardPosition.convertStringPosToInt(pos));
    }

    public static boolean isValidPosition(int row, int col) {
        return isValidPosition(BoardPosition.convertRowColPosToString(row, col));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoardPosition) {
            BoardPosition bPos = (BoardPosition) obj;
            return hashCode() == bPos.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return stringPosition.charAt(0) * 10 + stringPosition.charAt(1) - 48;
    }

    @Override
    public String toString() {
        return stringPosition;
    }

}
