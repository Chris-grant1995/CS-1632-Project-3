
import java.util.HashMap;
import java.io.Serializable;

public class Coordinate implements Serializable {

    public static final int MAX_DIM = 10;
    private int row, col;
    private static final String formattingRules = "Coordinate format must be <A-J>:<1-10>";
    // Support for mapping from letters to numbers...
    private static final HashMap<Character, Integer> columnMap = new HashMap<Character, Integer>() {
        {
            put('A', 0);
            put('B', 1);
            put('C', 2);
            put('D', 3);
            put('E', 4);
            put('F', 5);
            put('G', 6);
            put('H', 7);
            put('I', 8);
            put('J', 9);
        }
    };

    // Support for mapping from numbers to letters
    private static final char[] reverseColumnMap
            = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public Coordinate() {
        super();
    }

    /**
     * A simple constructor that calls setCoordinates below.
     */
    public Coordinate(String coord) throws IllegalArgumentException {
        setCoordinates(coord);
    }

    public Coordinate(int _row, int _col) {
        setRow(_row);
        setCol(_col);
    }

    /**
     * This function will parse and set the coordinates for the board. This
     * allows any parsing errors to be detected on the client side before it
     * gets to the server.
     */
    public boolean setCoordinates(String coord) throws IllegalArgumentException {

        if (coord.length() != 3 && coord.length() != 4) {
            System.out.println(formattingRules);
            return false;
        }

        if (!coord.contains(":")) {
            System.out.println(formattingRules);
            return false;
        }

        int _col = 0;
        String[] coordinates = coord.split(":");
        coordinates[0] = coordinates[0].toUpperCase();
        if (columnMap.keySet().contains(coordinates[0].charAt(0))) {
            _col = columnMap.get(coordinates[0].charAt(0));
        } else {
            System.out.println("Invalid input.");
            System.out.println(formattingRules);
            return false;
        }

        int _row = 0;
        try {
            _row = Integer.parseInt(coordinates[1]) - 1;

            if (_row < 0 || _row > 9) {
                System.out.println("Out of bounds.");
                System.out.println(formattingRules);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(formattingRules);
            return false;
        }

        // At this point we have passed all tests and we can initialize the coordinate.
        setCol(_col);
        setRow(_row);
        return true;
    }

    /**
     * Ensures each ship takes up the proper amount of spaces.   
     * @param ship Ship to check
     * @param coord1 Coordinate 1 inputted: A:1, i.e
     * @param coord2 Coordinate 2 inputted: A:4, i.e
     * @return True if valid, false if not
     */
    public boolean checkShipPlacement(String ship, String coord1, String coord2) {

        if (ship.equalsIgnoreCase("carrier")) {
            if (!checkSpaces(coord1, coord2, 5)) {
                System.out.println("Carrier must take up 5 spaces");
                return false;
            }
        } else if (ship.equalsIgnoreCase("battleship")) {
            if (!checkSpaces(coord1, coord2, 4)) {
                System.out.println("Battleship must take up 4 spaces.");
                return false;
            }
        } else if (ship.equalsIgnoreCase("cruiser") || ship.equalsIgnoreCase("submarine")) {
            if (!checkSpaces(coord1, coord2, 3)) {
                System.out.println(ship + " must take up 3 spaces.");
                return false;
            }
        } else if (ship.equalsIgnoreCase("destroyer")) {
            if (!checkSpaces(coord1, coord2, 2)) {
                System.out.println("Destroyer must take up 2 spaces.");
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method to do the checks of ships/spaces.
     * @param coord1 Coordinate 1 inputted: A:1, i.e
     * @param coord2 Coordinate 2 inputted: A:4, i.e
     * @param spaces Number of spaces coordinate should span
     * @return True if valid, false if not
     */
    private boolean checkSpaces(String coord1, String coord2, int spaces) {
        String[] coord1split = coord1.split(":");
        String[] coord2split = coord2.split(":");
        String coordLetter1 = coord1split[0].toUpperCase();
        String coordLetter2 = coord2split[0].toUpperCase();
        int coordNum1 = Integer.parseInt(coord1split[1]);
        int coordNum2 = Integer.parseInt(coord2split[1]);

        // Same column - vertical placement
        if (coord1split[0].equalsIgnoreCase(coord2split[0])) {
            if (coordNum1 > coordNum2) {
                int curSpaces = (coordNum1 - coordNum2) + 1;
                if (curSpaces != spaces) {
                    return false;
                }
            } else {
                int curSpaces = (coordNum2 - coordNum1) + 1;
                if (curSpaces != spaces) {
                    return false;
                }
            }
        } else { // Same row potentially, horizontal placement
            // Checks to make sure ships are to be placed horizontally, not diagonally
            if (coordNum1 != coordNum2) {
                System.out.println("Must place ships horizontally or vertically.");
                return false;
            }
            if (coordLetter1.charAt(0) > coordLetter2.charAt(0)) {
                int curSpaces = ((int) coordLetter1.charAt(0) - (int) coordLetter2.charAt(0)) + 1;
                if (curSpaces != spaces) {
                    return false;
                }
            } else {
                int curSpaces = ((int) coordLetter2.charAt(0) - (int) coordLetter1.charAt(0)) + 1;
                if (curSpaces != spaces) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setCol(int _col) {
        col = _col;
    }

    public void setRow(int _row) {
        row = _row;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public static int columnLookup(char colName) {
        return columnMap.get(colName);
    }

    public static char reverseColumnLookup(int col) {
        return reverseColumnMap[col];
    }

    @Override
    public String toString() {
        return reverseColumnLookup(col) + ":" + (row + 1);
    }
}
