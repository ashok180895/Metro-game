


public class Tile {

    final String type;
    Location location;

    // Tile constructor from String tileType and Location location.
    public Tile(String tileType, Location location) {
        this.type = tileType;
        this.location = location;
    }

    // Tile constructor from String piecePlacement.
    public Tile(String piecePlacement) {
        this.type = piecePlacement.substring(0,4);
        this.location = new Location(Integer.parseInt(piecePlacement.substring(4,5)),
                Integer.parseInt(piecePlacement.substring(5)));
    }

    // Tile constructor from String placementSequence and int position of the tile in the placementSequence.
    public Tile(String placementSequence, int position) {
         this.location = new Location(Integer.parseInt(placementSequence.substring(position * 6 + 4, position * 6 + 5)),
                 Integer.parseInt(placementSequence.substring(position * 6 + 5, position * 6 + 6)));
         this.type = placementSequence.substring(position * 6, position * 6 + 4);
    }

    /**
     * Traces path through tile from the even exit one side of the tile to the odd exit on another side according
     * to the type of track displayed on the tile.
     *
     * @param tile the tile being considered
     * @param fromLocation indicates the side that the path starts from
     * @return the Location to one side of the current tile, indicating where the path continues. Returns null if
     *         invalid fromLocation parameter
     */
    public static Location traceDirection(Tile tile, Location fromLocation) {
        // Find the distance between the x and y coordinates of the tile where the path starts and
        // the tile currently being considered and holds the distances in an array.
        int[] locDiff = new int[2];
        locDiff[0] = fromLocation.row - tile.location.row;
        locDiff[1] = fromLocation.column - tile.location.column;

        int relativePos; // Relative position of the fromLocation tile compared to the current tile.
        // If distance is further than one tile away, throw IllegalArgumentException.
        // relativePos values: 0 - above, 1 - right, 2 - below, 3 - left
        if (locDiff[0] == -1 && locDiff[1] == 0) {
            relativePos = 0;
        } else if (locDiff[0] == 0 && locDiff[1] == 1) {
            relativePos = 1;
        } else if (locDiff[0] == 1 && locDiff[1] == 0) {
            relativePos = 2;
        } else if (locDiff[0] == 0 && locDiff[1] == -1) {
            relativePos = 3;
        } else {
            return null;
        }

        // Determine relative position of new location depending on the type track that connects to the previous tile.
        // newPos values: 0 - above, 1 - right, 2 - below, 3 - left
        int newPos = 4; // Initialise newPos variable.
        char track = tile.type.charAt(relativePos); // Holds the type of track being traced in current tile.
        switch (track) {
            case 'a':
                newPos = (relativePos + 2) % 4;
                break;
            case 'b':
                newPos = (relativePos + 1) % 4;
                break;
            case 'c':
                newPos = (relativePos + 3) % 4;
                break;
            case 'd':
                newPos = relativePos;
                break;
        }

        // Return position of next tile as a location on the board, using the location of the current tile
        // and the new relative position.
        switch (newPos) {
            case 0:
                return new Location(tile.location.row - 1, tile.location.column);
            case 1:
                return new Location(tile.location.row, tile.location.column + 1);
            case 2:
                return new Location(tile.location.row + 1, tile.location.column);
            case 3:
                return new Location(tile.location.row, tile.location.column - 1);
        }

        return tile.location; // If no new position, return current tile position.
    }

    @Override
    public String toString() {
        return "Tile " + type + " at " + location;
    }
}
