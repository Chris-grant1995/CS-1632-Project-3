
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public abstract class Ship implements Serializable {
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
    
    private int hitCount = 0;
    // Keep a backreference to the board that this ship is placed on
    private Board myBoard = null;
    Coordinate start, end;
    
    public Ship(Coordinate start, Coordinate end, Board board) {
        this.start = start;
        this.end = end;
        addBoard(board);
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
    
    public void addBoard(Board board) {
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
    public abstract int getLength();
    
    /**
     * Get the maximum amount of ships of this type allowed. This function is
     * only here to "force" the base class to have a
     * public static final int MAX_ALLOWED.
     * @return 
     */
    public abstract int maxAllowed();

    /**
     * Get the name of the Ship.
     */
    public abstract String getName();
    
    public abstract ShipType getType();
}