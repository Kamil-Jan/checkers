package com.kamiljan.checkers.board;

import java.security.InvalidParameterException;

public class BoardPosition {
    String stringPosition;
    int intPosition;

    public BoardPosition(int pos) {
        if (!isValidPosition(pos))
            throw new InvalidParameterException("Invalid position: " + pos);

        stringPosition = convertIntPosToString(pos);
        intPosition = pos;
    }

    public BoardPosition(String pos) {
        if (!isValidPosition(pos))
            throw new InvalidParameterException("Invalid position: " + pos);

        stringPosition = pos;
        intPosition = convertStringPosToInt(pos);
    }

    public BoardPosition(int row, int col) {
        if (!isValidPosition(row, col))
            throw new InvalidParameterException("Invalid position: " + row + " " + col);

        stringPosition = convertRowColPosToString(row, col);
        intPosition = convertStringPosToInt(stringPosition);
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
        if (row % 2 == 0)
            return 2 * (intPosition - row * 4);
        return 2 * (intPosition - row * 4) - 1;
    }

    public static String convertRowColPosToString(int row, int col) {
        if ( (1 > row || row > 8) || (1 > col || col > 8) )
            return "";

        Integer[] rows = { 1, 2, 3, 4, 5, 6, 7, 8 };
        Character[] columns = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        return columns[col - 1].toString() + rows[row - 1].toString();
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
        return isValidPosition(convertStringPosToInt(pos));
    }

    public static boolean isValidPosition(int row, int col) {
        return isValidPosition(convertRowColPosToString(row, col));
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
