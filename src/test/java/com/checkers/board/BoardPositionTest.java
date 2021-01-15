package com.checkers.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardPositionTest {
    @Test
    public void testIsValidPosition() {
        char[] cols = "abcdefgh".toCharArray();
        for (int colIndex = 0; colIndex < cols.length; colIndex++) {
            for (int row = 8; row >= 1; row--) {
                String position = String.valueOf(cols[colIndex]) + row;
                if (colIndex % 2 == 0) {
                    if (row % 2 == 0)
                        assertFalse(BoardPosition.isValidPosition(position));
                    else
                        assertTrue(BoardPosition.isValidPosition(position));
                } else {
                    if (row % 2 == 0)
                        assertTrue(BoardPosition.isValidPosition(position));
                    else
                        assertFalse(BoardPosition.isValidPosition(position));
                }
            }
        }
    }

    @Test
    public void testConversions() {
        char[] cols = "abcdefgh".toCharArray();
        for (int colIndex = 0; colIndex < cols.length; colIndex++) {
            for (int row = 8; row >= 1; row--) {
                String sposition = String.valueOf(cols[colIndex]) + row;
                int col = colIndex + 1;

                BoardPosition bp1, bp2, bp3;
                if (colIndex % 2 == 0) {
                    if (row % 2 != 0) {
                        bp1 = new BoardPosition(BoardPosition.convertStringPosToInt(sposition));
                        bp2 = new BoardPosition(sposition);
                        bp3 = new BoardPosition(row, col);
                    } else continue;
                } else {
                    if (row % 2 == 0) {
                        bp1 = new BoardPosition(BoardPosition.convertStringPosToInt(sposition));
                        bp2 = new BoardPosition(sposition);
                        bp3 = new BoardPosition(row, col);
                    } else continue;
                }

                assertEquals(bp1, bp2);
                assertEquals(bp1, bp3);
                assertEquals(bp2, bp3);
            }
        }
    }
}
