package com.checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;

    private BoardPosition getPosition(BufferedReader br, String text) {
        String stringPos;
        while (true) {
            System.out.print(text);

            try {
                stringPos = br.readLine();
            } catch (IOException e) {
                continue;
            }

            if (stringPos.equals("cancel")) {
                return null;
            }

            if (!BoardPosition.isValidPosition(stringPos)) {
                System.out.println("Invalid position");
            } else
                break;
        }

        return new BoardPosition(stringPos);
    }

    public Move getUserMove(Checker.Side turn) {
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

            List<Checker> canCaptureCheckers = getCaptureMoves(turn);
            if (canCaptureCheckers.size() != 0 &&
                !(move.isValidNormalCapture() || move.isValidKingCapture())) {

                System.out.println(turn + " must capture");
                continue;
            }
            break;
        }

        return move;
    }

    public List<Checker> getCaptureMoves(Checker.Side turn) {
        List<Checker> canCaptureCheckers = new ArrayList<>();
        for (Checker checker : board.getCheckers()) {
            if (checker.getSide() == turn && checker.canCapture(board)) {
                canCaptureCheckers.add(checker);
            }
        }
        return canCaptureCheckers;
    }

    public void processMove(Move move) {
        board.move(move);
        for (Checker capturedChecker : move.getCapturedCheckers()) {
            board.removeChecker(capturedChecker.getPosition());
        }

        Checker movingChecker = board.getChecker(move.getEndPosition());
        if (movingChecker.isOnKingRow()) {
            movingChecker.turnIntoKing();
        }
    }

    public void run() {
        board = new Board();
        board.initBoard();

        Move userMove;
        Checker.Side turn = Checker.Side.WHITE;
        while (!board.isWin()) {
            board.display();
            System.out.println(turn + "'s move");
            userMove = getUserMove(turn);
            processMove(userMove);
            if (userMove.getCapturedCheckers().size() == 0 ||
                !board.getChecker(userMove.getEndPosition()).canCapture(board))

                turn = (turn == Checker.Side.WHITE)? Checker.Side.BLACK : Checker.Side.WHITE;
            Board.clearConsole();
        }
        board.display();
        System.out.println(board.getWinner() + " won!");
    }

}
