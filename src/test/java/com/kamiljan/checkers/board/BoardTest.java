package com.kamiljan.checkers.board;

import com.kamiljan.checkers.checker.Checker;
import com.kamiljan.checkers.moves.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void testCheckerGetters() {
        Board board = new Board();
        board.initBoard();

        for (int i = 1; i <= 12; i++) {
            BoardPosition BPos = new BoardPosition(i);
            assertNotNull(board.getChecker(BPos.getIntPosition()));
            assertNotNull(board.getChecker(BPos.getStringPosition()));
            assertNotNull(board.getChecker(
                    BPos.getBoardRow(),
                    BPos.getBoardCol()
                    )
            );
            assertNotNull(board.getChecker(BPos));
        }

        for (int i = 13; i <= 20; i++) {
            BoardPosition BPos = new BoardPosition(i);
            assertThrows(IllegalArgumentException.class, () -> board.getChecker(BPos.getIntPosition()));
            assertThrows(IllegalArgumentException.class, () -> board.getChecker(BPos.getStringPosition()));
            assertThrows(IllegalArgumentException.class, () -> board.getChecker(
                    BPos.getBoardRow(),
                    BPos.getBoardCol()
                    )
            );
            assertThrows(IllegalArgumentException.class, () -> board.getChecker(BPos));
        }

        for (int i = 21; i <= 32; i++) {
            BoardPosition BPos = new BoardPosition(i);
            assertNotNull(board.getChecker(BPos.getIntPosition()));
            assertNotNull(board.getChecker(BPos.getStringPosition()));
            assertNotNull(board.getChecker(
                    BPos.getBoardRow(),
                    BPos.getBoardCol()
                    )
            );
            assertNotNull(board.getChecker(BPos));
        }
    }

    @Test
    public void testIsOccupied() {
        Board board = new Board();
        board.initBoard();

        BoardPosition BPos;
        for (int i = 1; i <= 12; i++) {
            BPos = new BoardPosition(i);
            assertTrue(board.isOccupied(BPos.getIntPosition()));
            assertTrue(board.isOccupied(BPos.getStringPosition()));
            assertTrue(board.isOccupied(
                    BPos.getBoardRow(),
                    BPos.getBoardCol()
                    )
            );
            assertTrue(board.isOccupied(BPos));
        }

        for (int i = 13; i <= 20; i++) {
            BPos = new BoardPosition(i);
            assertFalse(board.isOccupied(BPos.getIntPosition()));
            assertFalse(board.isOccupied(BPos.getStringPosition()));
            assertFalse(board.isOccupied(
                    BPos.getBoardRow(),
                    BPos.getBoardCol()
                    )
            );
            assertFalse(board.isOccupied(BPos));
        }

        for (int i = 21; i <= 32; i++) {
            BPos = new BoardPosition(i);
            assertTrue(board.isOccupied(BPos.getIntPosition()));
            assertTrue(board.isOccupied(BPos.getStringPosition()));
            assertTrue(board.isOccupied(
                    BPos.getBoardRow(),
                    BPos.getBoardCol()
                    )
            );
            assertTrue(board.isOccupied(BPos));
        }
    }

    @Test
    public void testCheckerMoves() {
        Board board = new Board();
        board.initBoard();

        BoardPosition startPos = new BoardPosition("g3");
        BoardPosition endPos = new BoardPosition("h4");
        board.moveChecker(
                startPos.getIntPosition(),
                endPos.getIntPosition()
        );
        assertTrue(board.isOccupied(endPos));
        assertFalse(board.isOccupied(startPos));

        startPos = new BoardPosition("h4");
        endPos = new BoardPosition("g3");
        board.moveChecker(
                startPos.getStringPosition(),
                endPos.getStringPosition()
        );
        assertTrue(board.isOccupied(endPos));
        assertFalse(board.isOccupied(startPos));

        startPos = new BoardPosition("g3");
        endPos = new BoardPosition("h4");
        board.moveChecker(startPos, endPos);
        assertTrue(board.isOccupied(endPos));
        assertFalse(board.isOccupied(startPos));

        startPos = new BoardPosition("h4");
        endPos = new BoardPosition("g5");
        Move move = new Move(startPos, endPos, board);
        board.moveChecker(move);
        assertTrue(board.isOccupied(endPos));
        assertFalse(board.isOccupied(startPos));
    }

    @Test
    public void testWin() {
        Board board = new Board();
        board.putChecker("g1", Checker.Side.WHITE);
        board.putChecker("e1", Checker.Side.WHITE);
        assertTrue(board.isWin());
        assertTrue(board.isWhiteWin());

        board.putChecker("h8", Checker.Side.BLACK);
        board.putChecker("g7", Checker.Side.WHITE);
        board.putChecker("f6", Checker.Side.WHITE);
        assertTrue(board.isWin());
        assertTrue(board.isWhiteWin());
    }
}
