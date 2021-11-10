

import java.util.ArrayList;
import java.util.List;

public class Board {
    // Creates an 8 by 8 String array to represent the Metro board.
    // The array holds the tile type of the tiles on the board and the String "Station" for the central stations


    String[][] tiles;

    /**
     * Creates an empty Board.
     */
    public Board() {
        this.tiles = new String[8][8];
        this.tiles[3][3] = "Station";
        this.tiles[3][4] = "Station";
        this.tiles[4][3] = "Station";
        this.tiles[4][4] = "Station";
    }

    /**
     * Creates a Board, containing tiles according to the placementSequence.
     * @param placementSequence a String of tile placements.
     */
    public Board(String placementSequence) {
        this.tiles = new String[8][8];
        this.tiles[3][3] = "Station";
        this.tiles[3][4] = "Station";
        this.tiles[4][3] = "Station";
        this.tiles[4][4] = "Station";

        Tile currentTile;

        for (int i = 0; i < placementSequence.length() / 6; i++) {
            currentTile = new Tile(placementSequence,i);
            this.tiles[currentTile.location.row][currentTile.location.column] = currentTile.type;
        }
    }

    /**
     * Adds a tile to the Board
     * @param tile to be added to the Board.
     */
    public void updateBoard(Tile tile) {
        if (this.tiles[tile.location.row][tile.location.column] != null) {
            throw new IllegalArgumentException("Tile location already occupied.");
        }
        this.tiles[tile.location.row][tile.location.column] = tile.type;
    }

    /**
     * Finds the Tile located at a certain Location from the Board
     * @param location the Location of the Tile to be retrieved.
     * @return The Tile at the given Location on the Board.
     */
    public Tile tileFromLocation(Location location) {
        if (this.tiles[location.row][location.column] == null) {
            return null;
        } else {
            return new Tile(this.tiles[location.row][location.column], location);
        }
    }

    /**
     * Finds the Tile located at a certain coordinates on the Board
     * @param row The row that the tile is located.
     * @param column The column that the tile is located.
     * @return The Tile at the given coordinates on the Board.
     */
    public Tile tileFromCoordinates(int row, int column) {
        if (this.tiles[row][column] == null) {
            return null;
        } else {
            return new Tile(this.tiles[row][column], new Location(row,column));
        }
    }

    /**
     * Finds the unoccupied Locations on the Board
     * @return An ArrayList of Locations containing a the unoccupied Locations on the Board.
     */
    public List<Location> findEmptyLocations() {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.tiles[i][j] == null) {
                    locations.add(new Location(i,j));
                }
            }
        }
        return locations;
    }

}
