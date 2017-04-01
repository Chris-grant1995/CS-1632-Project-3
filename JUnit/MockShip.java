
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public  class MockShip implements Serializable {
    public enum ShipType {
        CARRIER,
        BATTLESHIP,
        CRUISER,
        SUBMARINE,
        DESTROYER,
        NONE        //This is a special ShipType that cannot be instantiated.
    }

    /**
     * Base class should provide
     * public static final int LENGTH
     * public static final int MAX_ALLOWED
     */

    public int hitCount = 0;
    // Keep a backreference to the board that this ship is placed on
    public MockBoard myBoard = null;
    Coordinate start, end;
    MockShip.ShipType type;

    public MockShip(Coordinate start, Coordinate end, MockBoard board) {
        this.start = start;
        this.end = end;
        type = ShipType.BATTLESHIP;
        addBoard(board);
    }
    public MockShip(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }
    public void setType(ShipType t){
        type = t;
    }
    public List<Coordinate> getCoordinates() {
        LinkedList coordinates = new LinkedList<Coordinate>();
        int st;
        int en;
        if (start.getRow() == end.getRow()) {
            // This ship is oriented column wise
            st = Integer.min(start.getCol(), end.getCol());
            en = Integer.max(start.getCol(), end.getCol());
            for (int i = st; i <= en; i++) {
                coordinates.add(new Coordinate(start.getRow(),i));
            }
        } else {
            // This ship is oriented length wise
            st = Integer.min(start.getRow(), end.getRow());
            en = Integer.max(start.getRow(), end.getRow());
            for (int i = st; i <= en; i++) {
                coordinates.add(new Coordinate(i, start.getCol()));
            }
        }
        return coordinates;
    }

    public boolean isSunk() {
        return (hitCount == getLength());
    }
    public void setHitCount(int num){
        hitCount= num;
    }

    public void addBoard(MockBoard board) {
        if (myBoard == null) {
            myBoard = board;
        } else {
            throw new IllegalArgumentException("This ship is already placed on a board: " + myBoard.getName());
        }
        board.addShip(this);
    }

    public void registerHit() {
        hitCount++;
    }

    /**
     * Get the length of this ship instance.
     * @return
     */
    public int getLength(){
        return 5;
    }

    /**
     * Get the maximum amount of ships of this type allowed. This function is
     * only here to "force" the base class to have a
     * public static final int MAX_ALLOWED.
     * @return
     */
    public  int maxAllowed(){
        return 1;
    }

    /**
     * Get the name of the Ship.
     */
    public  String getName(){
        return "MockShip";
    }

    public  ShipType getType(){
        return type;
    }
}