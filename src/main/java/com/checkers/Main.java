package com.checkers;

import com.checkers.board.Board;
import com.checkers.checker.Checker;
import com.checkers.moves.Move;
import com.checkers.moves.UserMove;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.initBoard();

        Move userMove;
        Checker.Side turn = Checker.Side.WHITE;
        while (!board.isWin()) {
            board.display();
            System.out.println(turn + "'s move");

            userMove = UserMove.getUserMove(board, turn);
            UserMove.processMove(board, userMove);

            if (userMove.getCapturedCheckers().size() == 0 ||
            !board.getChecker(userMove.getEndPosition()).canCapture(board))
                turn = (turn == Checker.Side.WHITE)? Checker.Side.BLACK : Checker.Side.WHITE;

            Board.clearConsole();
        }
        board.display();
        System.out.println(board.getWinner() + " won!");
    }
}
