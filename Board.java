import java.util.Hashtable;
import java.util.Collection;

public class Board {
    private Hashtable<BoardPosition, Checker> board
        = new Hashtable<>();
    private int whiteSideCheckerCount;
    private int blackSideCheckerCount;
    private Checker nullChecker = new Checker(Checker.Side.WHITE);
    private Checker.Side winner;

    public void initBoard() {
        whiteSideCheckerCount = 12;
        for (int pos = 1; pos <= 12; pos++) {
            BoardPosition BPos = new BoardPosition(pos);
            board.put(BPos, new Checker(Checker.Side.BLACK, BPos));
        }

        blackSideCheckerCount = 12;
        for (int pos = 21; pos <= 32; pos++) {
            BoardPosition BPos = new BoardPosition(pos);
            board.put(BPos, new Checker(Checker.Side.WHITE, BPos));
        }
    }

    public Checker getChecker(int pos) {
        return getChecker(new BoardPosition(pos));
    }

    public Checker getChecker(String pos) {
        return getChecker(new BoardPosition(pos));
    }

    public Checker getChecker(BoardPosition bPos) {
        return board.get(bPos);
    }

    public Checker getChecker(int row, int col) {
        return board.get(new BoardPosition(row, col));
    }

    public Collection<Checker> getCheckers() {
        return board.values();
    }

    public int getWhiteSideCheckerCount() {
        return whiteSideCheckerCount;
    }

    public int getBlackSideCheckerCount() {
        return blackSideCheckerCount;
    }

    public void display() {
        System.out.print("  ");
        for (int i = 1; i <= 8; i++) {
            System.out.print(" " + (char)(i + 'a' - 1) + " ");
        }
        System.out.println();
        System.out.println("---".repeat(9));

        int row = 1;
        for (int pos = 1; pos <= 32; pos++) {
            if (pos % 4 == 1)
                System.out.print(BoardPosition.convertIntPosToString(pos).charAt(1) + "|");

            BoardPosition bPos = new BoardPosition(pos);
            if (row % 2 != 0) {
                if (board.containsKey(bPos) && !board.get(bPos).equals(nullChecker))
                    System.out.print(" . " + " " +  board.get(bPos).getSymbol() + " ");
                else
                    System.out.print(" . " + " . ");
            } else {
                if (board.containsKey(bPos) && !board.get(bPos).equals(nullChecker))
                    System.out.print(" " +  board.get(bPos).getSymbol() + " " + " . ");
                else
                    System.out.print(" . " + " . ");
            }

            if (pos % 4 == 0) {
                System.out.println("|" + BoardPosition.convertIntPosToString(pos).charAt(1));
                row++;
            }
        }

        System.out.println("---".repeat(9));
        System.out.print("  ");
        for (int i = 1; i <= 8; i++) {
            System.out.print(" " + (char)(i + 'a' - 1) + " ");
        }
        System.out.println();
    }

    public void move(int start, int end) {
        BoardPosition bPosStart = new BoardPosition(start);
        BoardPosition bPosEnd = new BoardPosition(end);
        move(bPosStart, bPosEnd);
    }

    public void move(String start, String end) {
        BoardPosition bPosStart = new BoardPosition(start);
        BoardPosition bPosEnd = new BoardPosition(end);
        move(bPosStart, bPosEnd);
    }

    public void move(Move move) {
        BoardPosition bPosStart = move.getStartPosition();
        BoardPosition bPosEnd = move.getEndPosition();
        move(bPosStart, bPosEnd);
    }

    public void move(BoardPosition start, BoardPosition end) {
        Checker movingChecker = board.get(start);
        movingChecker.setPosition(end);

        board.put(start, nullChecker);
        board.put(end, movingChecker);
    }

    public void putChecker(int pos, Checker.Side side) {
        putChecker(new BoardPosition(pos), side);
    }

    public void putChecker(int row, int col, Checker.Side side) {
        putChecker(new BoardPosition(row, col), side);
    }

    public void putChecker(String pos, Checker.Side side) {
        putChecker(new BoardPosition(pos), side);
        if (side == Checker.Side.BLACK)
            blackSideCheckerCount++;
        else
            whiteSideCheckerCount++;
    }

    public void putChecker(BoardPosition BPos, Checker.Side side) {
        board.put(BPos, new Checker(side, BPos));
    }

    public void removeChecker(int pos) {
        removeChecker(new BoardPosition(pos));
    }

    public void removeChecker(int row, int col) {
        removeChecker(new BoardPosition(row, col));
    }

    public void removeChecker(String pos) {
        removeChecker(new BoardPosition(pos));
    }

    public void removeChecker(BoardPosition bPos) {
        if (!isOccupied(bPos)) return;

        Checker checker = board.get(bPos);
        if (checker.getSide().equals(Checker.Side.BLACK))
            blackSideCheckerCount--;
        else
            whiteSideCheckerCount--;

        board.put(bPos, nullChecker);
    }

    public boolean isOccupied(int pos) {
        return isOccupied(new BoardPosition(pos));
    }

    public boolean isOccupied(String pos) {
        return isOccupied(new BoardPosition(pos));
    }

    public boolean isOccupied(int row, int col) {
        return isOccupied(new BoardPosition(row, col));
    }

    public boolean isOccupied(BoardPosition bPos) {
        if (board.containsKey(bPos) && !board.get(bPos).equals(nullChecker))
            return true;
        return false;
    }

    public Checker.Side getWinner() {
        return winner;
    }

    public boolean isWin() {
        return (isWhiteWin() || isBlackWin());
    }

    public boolean isWhiteWin() {
        boolean isWinner = (blackSideCheckerCount == 0 || !canSideMove(Checker.Side.BLACK));
        if (isWinner) winner = Checker.Side.WHITE;
        return isWinner;
    }

    public boolean isBlackWin() {
        boolean isWinner = (whiteSideCheckerCount == 0 || !canSideMove(Checker.Side.WHITE));
        if (isWinner) winner = Checker.Side.BLACK;
        return isWinner;
    }

    public boolean canSideMove(Checker.Side side) {
        for (Checker checker : getCheckers()) {
            if (checker.getSide() == side && checker.canMove(this)) {
                return true;
            }
        }
        return false;
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.initBoard();
        board.move("c3", "d4");
        board.removeChecker("b2");
        board.removeChecker("b8");
        board.display();
        System.out.println(board.isOccupied(18));
        System.out.println(board.isOccupied("d4"));
        System.out.println(board.isOccupied(new BoardPosition(18)));
    }
}

