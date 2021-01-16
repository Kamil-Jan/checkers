package com.kamiljan.checkers.moves;

import com.kamiljan.checkers.board.Board;
import com.kamiljan.checkers.board.BoardPosition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    private static boolean checkMove(Board board, String startPos, List<String> validEndPositions) {
        for (int pos=1; pos <= 32; pos++) {
            String endPos = BoardPosition.convertIntPosToString(pos);
            Move move = new Move(startPos, endPos, board);
            boolean containsEndPos = validEndPositions.contains(endPos);
            if (containsEndPos && !move.isValidMove())
                return false;
            else if (!containsEndPos && move.isValidMove())
                return false;
        }
        return true;
    }

    @Test
    public void testMoves() {
        Board board = new Board();
        board.initBoard();
        assertTrue(checkMove(board, "d6", List.of("e5", "c5")));
        assertTrue(checkMove(board, "c3", List.of("b4", "d4")));

        board.moveChecker("d6", "e5");
        assertTrue(checkMove(board, "e5", List.of("f4", "d4")));

        board.moveChecker("e5", "d4");
        assertTrue(checkMove(board, "d4", List.of()));
        assertTrue(checkMove(board, "c3", List.of("b4", "e5")));
        assertTrue(checkMove(board, "e3", List.of("f4", "c5")));

        UserMove.processMove(board, new Move("c3", "e5", board));
        board.getChecker("f6").turnIntoKing();
        assertTrue(checkMove(board, "f6", List.of("d4", "c3", "g5", "h4")));
        assertTrue(checkMove(board, "f2", List.of()));

        assertEquals(
                board.getChecker("e5"),
                new Move("f6", "d4", board)
                        .getCapturedCheckers().get(0)
        );
        assertEquals(
                board.getChecker("e5"),
                new Move("f6", "c3", board)
                        .getCapturedCheckers().get(0)
        );
    }
}
