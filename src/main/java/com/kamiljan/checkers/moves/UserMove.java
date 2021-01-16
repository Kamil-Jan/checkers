package com.kamiljan.checkers.moves;

import com.kamiljan.checkers.board.Board;
import com.kamiljan.checkers.board.BoardPosition;
import com.kamiljan.checkers.checker.Checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserMove {

    public static Move getUserMove(Board board, Checker.Side turn) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        BoardPosition startPos;
        BoardPosition endPos;
        Move move;
        while (true) {
            startPos = getPosition(br, "Enter checker position: ");
            if (startPos == null)
                continue;

            if (!board.isOccupied(startPos)) {
                System.out.println("Invalid checker position\n");
                continue;
            }

            if (board.getChecker(startPos).getSide() != turn) {
                System.out.println(turn + "'s move, not " + board.getChecker(startPos).getSide() + "'s\n");
                continue;
            }

            endPos = getPosition(br, "Enter position to move to: ");
            if (endPos == null)
                continue;

            move = new Move(startPos, endPos, board);
            if (!move.isValidMove()) {
                System.out.println("Invalid move\n");
                continue;
            }

            boolean checkersCanCapture = areCheckersCanCapture(board, turn);
            if (checkersCanCapture && !(move.isValidNormalCapture() || move.isValidKingCapture())) {
                System.out.println(turn + " must capture\n");
                continue;
            }
            break;
        }

        return move;
    }

    public static void processMove(Board board, Move move) {
        board.moveChecker(move);
        for (Checker capturedChecker : move.getCapturedCheckers()) {
            board.removeChecker(capturedChecker.getPosition());
        }

        Checker movingChecker = board.getChecker(move.getEndPosition());
        if (movingChecker.isOnKingRow()) {
            movingChecker.turnIntoKing();
        }
    }

    private static BoardPosition getPosition(BufferedReader br, String text) {
        String stringPos;
        System.out.print(text);
        try {
            stringPos = br.readLine();
        } catch (IOException e) {
            return null;
        }

        if (stringPos.equals("cancel")) return null;

        if (BoardPosition.isValidPosition(stringPos))
            return new BoardPosition(stringPos);

        System.out.println("Invalid checker position\n");
        return null;
    }

    private static boolean areCheckersCanCapture(Board board, Checker.Side turn) {
        for (Checker checker : board.getCheckers()) {
            if (checker.getSide() == turn && checker.canCapture(board)) {
                return true;
            }
        }
        return false;
    }

}
