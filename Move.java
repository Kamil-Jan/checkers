import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

public class Move {
    private BoardPosition startPos;
    private BoardPosition endPos;
    private Board board;
    private boolean isValid;

    private Checker movingChecker;
    private List<Checker> capturedCheckers = new ArrayList<>();

    public Move(int startPos, int endPos, Board board) {
        this.startPos = new BoardPosition(startPos);
        this.endPos = new BoardPosition(endPos);
        this.board = board;
        this.movingChecker = board.getChecker(this.startPos);
        isValid = isValidMoveUtil();
    }

    public Move(String startPos, String endPos, Board board) {
        this.startPos = new BoardPosition(startPos);
        this.endPos = new BoardPosition(endPos);
        this.board = board;
        this.movingChecker = board.getChecker(this.startPos);
        isValid = isValidMoveUtil();
    }

    public Move(BoardPosition startPos,
                BoardPosition endPos,
                Board board) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.board = board;
        this.movingChecker = board.getChecker(this.startPos);
        isValid = isValidMoveUtil();
    }

    public boolean isValidMove() {
        return isValid;
    }

    public List<Checker> getCapturedCheckers() {
        return capturedCheckers;
    }

    public BoardPosition getStartPosition() {
        return startPos;
    }

    public BoardPosition getEndPosition() {
        return endPos;
    }

    private boolean isValidMoveUtil() {
        if (!movingChecker.isKing())
            return (isValidNormalCapture() || isValidNormalMove());
        else {
            return (isValidKingCapture() || isValidKingMove());
        }
    }

    public boolean isValidNormalMove() {
        if (!board.isOccupied(startPos))
            return false;
        if (board.isOccupied(endPos))
            return false;

        if (movingChecker.isKing()) return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        if (movingChecker.getSide() == Checker.Side.BLACK) {
            if (endRow == startRow - 1 && Math.abs(endCol - startCol) == 1)
                return true;
        } else {
            if (endRow == startRow + 1 && Math.abs(endCol - startCol) == 1)
                return true;
        }
        return false;
    }

    public boolean isValidKingMove() {
        if (!board.isOccupied(startPos))
            return false;
        if (board.isOccupied(endPos))
            return false;

        if (!movingChecker.isKing()) return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        if (rowDiff == colDiff) {
            int curRow = startRow;
            int curCol = startCol;

            Function<Integer, Integer> add = (Integer x) -> x + 1;
            Function<Integer, Integer> subtract = (Integer x) -> x - 1;

            Function<Integer, Integer> rowOperation;
            Function<Integer, Integer> colOperation;
            if (curRow < endRow)
                rowOperation = add;
            else
                rowOperation = subtract;

            if (curCol < endCol)
                colOperation = add;
            else
                colOperation = subtract;

            curRow = rowOperation.apply(curRow);
            curCol = colOperation.apply(curCol);

            while (curRow != endRow && curCol != endRow) {
                if (board.isOccupied(curRow, curCol)) {
                    if (board.isOccupied(curRow + 1, curCol + 1)) return false;
                }
                curRow = rowOperation.apply(curRow);
                curCol = colOperation.apply(curCol);
            }
            return true;
        }
        return false;
    }

    public boolean isValidNormalCapture() {
        if (!board.isOccupied(startPos))
            return false;
        if (board.isOccupied(endPos))
            return false;

        if (movingChecker.isKing()) return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        if (rowDiff == 2 && colDiff == 2) {
            int rowCaptured = (startRow + endRow) / 2;
            int colCaptured = (startCol + endCol) / 2;
            if (board.isOccupied(rowCaptured, colCaptured) &&
                board.getChecker(rowCaptured, colCaptured).getSide() !=
                board.getChecker(startRow, startCol).getSide()) {

                if (!capturedCheckers.contains(board.getChecker(rowCaptured, colCaptured)))
                    capturedCheckers.add(board.getChecker(rowCaptured, colCaptured));
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isValidKingCapture() {
        if (!board.isOccupied(startPos))
            return false;
        if (board.isOccupied(endPos))
            return false;

        if (!movingChecker.isKing()) return false;

        int startRow = startPos.getBoardRow();
        int startCol = startPos.getBoardCol();
        int endRow = endPos.getBoardRow();
        int endCol = endPos.getBoardCol();

        int curRow = startRow;
        int curCol = startCol;

        Function<Integer, Integer> add = (Integer x) -> x + 1;
        Function<Integer, Integer> subtract = (Integer x) -> x - 1;

        Function<Integer, Integer> rowOperation;
        Function<Integer, Integer> colOperation;
        Function<Integer, Boolean> rowLoopStop;
        Function<Integer, Boolean> colLoopStop;
        if (curRow < endRow) {
            rowOperation = add;
            rowLoopStop = (Integer x) -> x > endRow ;
        } else {
            rowOperation = subtract;
            rowLoopStop = (Integer x) -> x < endRow ;
        }

        if (curCol < endCol) {
            colOperation = add;
            colLoopStop = (Integer x) -> x > endCol ;
        }
        else {
            colOperation = subtract;
            colLoopStop = (Integer x) -> x < endCol ;
        }

        curRow = rowOperation.apply(curRow);
        curCol = colOperation.apply(curCol);

        while (!colLoopStop.apply(curCol) && !rowLoopStop.apply(curRow)) {
            if (board.isOccupied(curRow, curCol)) {
                if (board.isOccupied(rowOperation.apply(curRow), colOperation.apply(curCol))) return false;

                if (board.getChecker(curRow, curCol).getSide() != movingChecker.getSide() &&
                    !capturedCheckers.contains(board.getChecker(curRow, curCol))) {

                    capturedCheckers.add(board.getChecker(curRow, curCol));
                }
            }
            curRow = rowOperation.apply(curRow);
            curCol = colOperation.apply(curCol);
        }

        return (capturedCheckers.size() != 0);
    }

    @Override
    public String toString() {
        String string = String.format(
            "Move from %s to %s; isValidMove? %s; capturedCheckers=%s",
            startPos, endPos, isValid, capturedCheckers
        );
        return string;
    }

    public static void main(String[] args) {
        System.out.println("NORMAL MOVES TEST\n");
        Board board = new Board();
        board.initBoard();
        board.display();

        System.out.println("Is move from 'd6' to 'e5' valid? " + new Move("d6", "e5", board).isValidMove() + "\n");
        board.move("d6", "e5");
        board.display();

        System.out.println("Is move from 'e5' to 'f4' valid? " + new Move("e5", "f4", board).isValidMove() + "\n");
        board.move("e5", "f4");
        board.display();

        System.out.println("Is move from 'f4' to 'g3' valid? " + new Move("f4", "g3", board).isValidMove());
        System.out.println("Is move from 'f4' to 'e3' valid? " + new Move("f4", "e3", board).isValidMove());
        System.out.println("Is move from 'f4' to 'g5' valid? " + new Move("f4", "g5", board).isValidMove());
        System.out.println("Is move from 'f4' to 'e5' valid? " + new Move("f4", "e5", board).isValidMove());
        System.out.println("Is move from 'f4' to 'd6' valid? " + new Move("f4", "d6", board).isValidMove());

        System.out.println("Is move from 'g3' to 'e5' valid? " + new Move("g3", "e5", board).isValidMove());
        System.out.println("Is move from 'e3' to 'g5' valid? " + new Move("e3", "g5", board).isValidMove());
        System.out.println("Is move from 'g3' to 'f4' valid? " + new Move("g3", "f4", board).isValidMove());
        System.out.println("Is move from 'e3' to 'f4' valid? " + new Move("e3", "f4", board).isValidMove());

        System.out.println(
            "Captured checker from move 'e3:g5' " +
            new Move("e3", "g5", board).getCapturedCheckers().get(0).getPosition()
        );
    }
}
