package com.checkers.moves;

import com.checkers.board.Board;
import com.checkers.board.BoardPosition;
import com.checkers.checker.Checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserMove {

    public static Move getUserMove(Board board, Checker.Side turn) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        BoardPosition startPos;
        BoardPosition endPos;
        Move move;
        while (true) {
            startPos = getPosition(br, "Enter checker position: ");
            if (startPos == null) continue;

            if (!board.isOccupied(startPos)) {
                System.out.println("Invalid checker position");
                continue;
            }

            endPos = getPosition(br, "Enter position to move to: ");
            if (endPos == null) continue;

            if (board.getChecker(startPos).getSide() != turn) {
                System.out.println(turn + "'s move, not " + board.getChecker(startPos).getSide() + "'s'");
                continue;
            }

            move = new Move(startPos, endPos, board);
            if (!move.isValidMove()) {
                System.out.println("Invalid move");
                continue;
            }

            List<Checker> canCaptureCheckers = getCaptureMoves(board, turn);
            if (canCaptureCheckers.size() != 0 &&
                !(move.isValidNormalCapture() || move.isValidKingCapture())) {

                System.out.println(turn + " must capture");
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
        while (true) {
            System.out.print(text);

            try {
                stringPos = br.readLine();
            } catch (IOException e) {
                continue;
            }

            if (stringPos.equals("cancel"))
                return null;

            if (BoardPosition.isValidPosition(stringPos))
                break;

            System.out.println("Invalid position");
        }

        return new BoardPosition(stringPos);
    }

    private static List<Checker> getCaptureMoves(Board board, Checker.Side turn) {
        List<Checker> canCaptureCheckers = new ArrayList<>();
        for (Checker checker : board.getCheckers()) {
            if (checker.getSide() == turn && checker.canCapture(board)) {
                canCaptureCheckers.add(checker);
            }
        }
        return canCaptureCheckers;
    }

}
