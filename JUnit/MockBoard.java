
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;


public class MockBoard implements Serializable {
    public static final int BOARD_DIM = 10;
    // We could track a Ship-Bool pair but it is just as easy to have
    // two arrays. The Ships array will keep track of the ships on the
    // game board. The moves array will be initialized to false and will
    // change to true when that move is made.
    //private ArrayList < ArrayList < Ship > > theShips;
    private MockShip [][] theShips;
    private boolean [][] moves;
    // Keep a list of all ships on this board for quick searching.
    LinkedList<MockShip> shipList;
    private String name;

    public MockBoard (String _name) {
        theShips = new MockShip[BOARD_DIM][BOARD_DIM];
        moves = new boolean[BOARD_DIM][BOARD_DIM];
        shipList = new LinkedList<MockShip>();
        name = _name;
    }

    public String getName() {
        return name;
    }

    public void addShip(MockShip ship) {
        if (!canShipFit(ship)) {
            throw new IllegalArgumentException("This board already has the maximum amount of: " + ship.getName());
        }
        for (Coordinate coord : ship.getCoordinates()){
            theShips[coord.getCol()][coord.getRow()] = ship;
            //Not consistent!!!
        }
        shipList.add(ship);
    }

    public MockShip makeMove(Coordinate move) {
        moves[move.getCol()][move.getRow()] = true;
        MockShip ship = theShips[move.getCol()][move.getRow()];
        if(ship != null) {
            ship.registerHit();
        }
        return ship;
    }

    public boolean canShipFit(MockShip ship) {
        int shipCount = 0;
        for (MockShip s : shipList) {
            if (s.getType() == ship.getType()) {
                shipCount++;
            }
        }
        if (shipCount >= ship.maxAllowed()) {
            return false;
        } else {
            return true;
        }
    }

    public List<MockShip> getShipList() {
        return shipList;
    }
    public MockShip[][] getShipLoc(){
        return theShips;
    }

    public boolean areAllShipsSunk() {
        for (MockShip s:shipList) {
            if (!s.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean showShips) {
        StringBuilder sb = new StringBuilder();
        // Build an intermediate representation of the board as a character array
        char [][] boardRepresentation = new char[BOARD_DIM+1][BOARD_DIM+1];
        boardRepresentation[0][0] = '+';
        for (int row = 1; row < BOARD_DIM+1; row++) {
            // The first column will be filled with the row labels
            boardRepresentation[row][0] = Integer.toString(row).charAt(0);
        }
        for (int col = 1; col < BOARD_DIM+1; col++) {
            boardRepresentation[0][col] = Coordinate.reverseColumnLookup(col-1);
        }
        for (int row = 0; row < BOARD_DIM; row++) {
            for (int col = 0; col < BOARD_DIM; col++) {
                if (moves[col][row]) {
                    if (theShips[col][row] != null) {
                        //The indexing should be correct for all arrays,
                        //but need to decide on columns or rows first
                        boardRepresentation[row+1][col+1] = 'X';
                    } else {
                        boardRepresentation[row+1][col+1] = 'O';
                    }
                }
                if (showShips && theShips[col][row] != null) {
                    boardRepresentation[row+1][col+1] = 'S';
                }
                else{
                    boardRepresentation[row+1][col+1] = '.';
                }
            }
        }
        for (char [] row : boardRepresentation) {
            sb.append(row);
            sb.append('\n');
        }
        return sb.toString();
    }
}