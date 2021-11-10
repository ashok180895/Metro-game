

public class Location {


    int row;
    int column;

    // Location constructor.
    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**

     * Transforms the Location to a single integer, according to the following layout
     *
     *    0   1   2   3   4   5   6   7
     *    8   9  10  11  12  13  14  15
     *   16  17  18  19  20  21  22  23
     *   24  25  26  27  28  29  30  31
     *   32  33  34  35  36  37  38  39
     *   40  41  42  43  44  45  46  47
     *   48  49  50  51  52  53  54  55
     *   56  57  58  59  60  61  62  63
     *
     * @return Returns an integer representing the Location as above.
     */
    public int toIntRepresentation() {
        return this.row * 8 + this.column;
    }

    // Tile constructor from String placementSequence and int position of the tile in the placementSequence.
    public Location(String placementSequence, int position) {
        this.row = Integer.parseInt(placementSequence.substring(position * 6 + 4, position * 6 + 5));
        this.column = Integer.parseInt(placementSequence.substring(position * 6 + 5, position * 6 + 6));
    }

    @Override
    public String toString() {
        return row + String.valueOf(column);
    }

    /**
    
     * Overrides the equals method so that ,
     * @param obj The Object that the Location is being compared to.
     * @return If the Object being compared is a Location with identical row and column, returns true,
     * otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Location)) {
            return false;
        } else {
            Location loc = (Location) obj;
            return (this.row == loc.row) && (this.column == loc.column);
        }
    }
}
