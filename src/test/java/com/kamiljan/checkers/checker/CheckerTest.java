package com.kamiljan.checkers.checker;

import com.kamiljan.checkers.board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerTest {
    @Test
    public void testMoves() {
        Board board = new Board();
        board.putChecker("d2", Checker.Side.WHITE);
        board.putChecker("g7", Checker.Side.WHITE);
        board.putChecker("f6", Checker.Side.WHITE);
        board.putChecker("h8", Checker.Side.BLACK);
        board.putChecker("b6", Checker.Side.BLACK);

        assertTrue(board.getChecker("d2").canMove(board));
        assertTrue(board.getChecker("b6").canMove(board));
        assertTrue(board.getChecker("g7").canMove(board));
        assertTrue(board.getChecker("f6").canMove(board));

        assertFalse(board.getChecker("h8").canMove(board));
        board.getChecker("h8").turnIntoKing();
        assertFalse(board.getChecker("h8").canMove(board));
    }

    @Test
    public void testCaptures() {
        Board board = new Board();
        board.putChecker("a1", Checker.Side.WHITE);
        board.putChecker("a3", Checker.Side.WHITE);
        board.putChecker("c1", Checker.Side.WHITE);
        board.putChecker("c3", Checker.Side.WHITE);
        board.putChecker("d4", Checker.Side.BLACK);

        assertFalse(board.getChecker("a1").canCapture(board));
        assertFalse(board.getChecker("a3").canCapture(board));
        assertFalse(board.getChecker("c1").canCapture(board));

        assertTrue(board.getChecker("c3").canCapture(board));
        assertTrue(board.getChecker("d4").canCapture(board));
        board.getChecker("d4").turnIntoKing();
        assertTrue(board.getChecker("d4").canCapture(board));

        board.moveChecker("d4", "b2");
        board.removeChecker("c3");
        assertFalse(board.getChecker("b2").canCapture(board));
        assertFalse(board.getChecker("a3").canCapture(board));
        assertFalse(board.getChecker("c1").canCapture(board));
        assertTrue(board.getChecker("a1").canCapture(board));
    }
}
