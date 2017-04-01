

public class Submarine extends Ship {
    public static final ShipType TYPE = ShipType.SUBMARINE;
    public static final int LENGTH = 3;
    public static final int MAX_ALLOWED = 1;
    public static final String NAME = "Submarine";

    public Submarine(Coordinate start, Coordinate end, Board board) {
        // Call the parent constructor and set the length to 5.
        super(start, end, board);
    }
    
    @Override
    public final int getLength() {
        return LENGTH;
    }
    
    @Override
    public int maxAllowed() {
        return MAX_ALLOWED;
    }
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public ShipType getType() {
        return TYPE;
    }
}