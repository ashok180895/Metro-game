

import java.util.Arrays;
import java.util.List;

public class Metro {
    /**


     *
     * Determine whether a piece placement is well-formed. For a piece
     * placement to be well-formed, it must:
     * - contain exactly six characters;
     * - have as its first, second, third and fourth characters letters between
     * 'a' and 'd' inclusive (tracks); and
     * - have as its fifth and sixth characters digits between 0 and 7 inclusive
     * (column and row respectively).
     *
     * @param piecePlacement A String representing the piece to be placed
     * @return True if this string is well-formed
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {


        //count for right tracks.
        int countYes=0;

        //tile with bad length.
        if(piecePlacement.length()!=6)
            return false;

            //first check for right tracks and placing position on the board.
        else {
            for(int i=0;i<piecePlacement.length();i++){

                if (i < 4) {
                    if ((piecePlacement.charAt(i)=='a')|| (piecePlacement.charAt(i)=='b')||(piecePlacement.charAt(i)=='c')||(piecePlacement.charAt(i)=='d')) {
                        countYes++;//counting the correct track

                    } else
                        return false;}//tiles having bad tracks.
                else {
                    int rowColumn = Integer.parseInt(String.valueOf(piecePlacement.charAt(i)));
                    // checking the row and column of each tile
                    if (rowColumn >= 8)
                        return false;
                }

            }

        }


        return countYes == 4;
    }

    /**


     *
     * Determine whether a placement sequence string is well-formed.
     * For a placement sequence to be well-formed, it must satisfy the
     * following criteria:
     * - It must be composed of well-formed tile placements.
     * - For any piece x, there can exist no more instances of x on the board
     * than instances of x in the deck.
     *
     * @param placement A String representing the placement of all tiles on the
     *                  board
     * @return true if this placement sequence is well-formed
     */
    public static boolean isPlacementSequenceWellFormed(String placement) {


        int numTiles = placement.length() / 6; // Number of tiles in placement sequence

        // Check each tile has 6 characters
        if (placement.length() % 6 != 0) {
            return false;
        }

        // Check if number of tiles is less than possible maximum number of tiles.
        if (numTiles > 60) {
            return false;
        }

        String[] tilePlacement = new String[numTiles]; // Array for position of tiles

        // Arrays to count number of tiles
        int[] tilesFourCopies = new int[5];
        int[] tilesThreeCopies = new int[2];
        int[] tilesTwoCopies = new int[17];

    /* For each tile in placement sequence, check whether piece placement is well formed,
       ensure that there are no multiple tiles in the same position and check that there are
       no extra tiles than what is in a deck.
     */
        for (int i = 0; i < numTiles; i++) {
            // Initialise current tile and its position
            String tile = placement.substring(i * 6, i * 6 + 4);
            String tilePos = placement.substring(i * 6 + 4, i * 6 + 6);

            if (!isPiecePlacementWellFormed(placement.substring(i * 6, i * 6 + 6))) {
                // Check whether tile well formed
                return false;
            } else if (Arrays.asList(tilePlacement).contains(tilePos)) {
                // Check whether multiple tiles in same position
                return false;
            } else {
                // Ensure that number of tiles of each type do not exceed maximum number in deck
                switch (tile) {
                    case "aacb":
                        tilesFourCopies[0] += 1;
                        break;
                    case "cbaa":
                        tilesFourCopies[1] += 1;
                        break;
                    case "acba":
                        tilesFourCopies[2] += 1;
                        break;
                    case "baac":
                        tilesFourCopies[3] += 1;
                        break;
                    case "aaaa":
                        tilesFourCopies[4] += 1;
                        break;
                    case "cbcb":
                        tilesThreeCopies[0] += 1;
                        break;
                    case "bcbc":
                        tilesThreeCopies[1] += 1;
                        break;
                    case "cccc":
                        tilesTwoCopies[0] += 1;
                        break;
                    case "bbbb":
                        tilesTwoCopies[1] += 1;
                        break;
                    case "dacc":
                        tilesTwoCopies[2] += 1;
                        break;
                    case "cdac":
                        tilesTwoCopies[3] += 1;
                        break;
                    case "ccda":
                        tilesTwoCopies[4] += 1;
                        break;
                    case "accd":
                        tilesTwoCopies[5] += 1;
                        break;
                    case "dbba":
                        tilesTwoCopies[6] += 1;
                        break;
                    case "adbb":
                        tilesTwoCopies[7] += 1;
                        break;
                    case "badb":
                        tilesTwoCopies[8] += 1;
                        break;
                    case "bbad":
                        tilesTwoCopies[9] += 1;
                        break;
                    case "ddbc":
                        tilesTwoCopies[10] += 1;
                        break;
                    case "cddb":
                        tilesTwoCopies[11] += 1;
                        break;
                    case "bcdd":
                        tilesTwoCopies[12] += 1;
                        break;
                    case "dbcd":
                        tilesTwoCopies[13] += 1;
                        break;
                    case "adad":
                        tilesTwoCopies[14] += 1;
                        break;
                    case "dada":
                        tilesTwoCopies[15] += 1;
                        break;
                    case "dddd":
                        tilesTwoCopies[16] += 1;
                        break;
                }
                // Add current tile position to array of tile positions
                tilePlacement[i] = tilePos;

            }

        }
        // Make sure number of each tile does not exceed maximum in deck
        Arrays.sort(tilesFourCopies);
        Arrays.sort(tilesThreeCopies);
        Arrays.sort(tilesTwoCopies);
        return (tilesFourCopies[4] <= 4) && (tilesThreeCopies[1] <= 3) && (tilesTwoCopies[16] <= 2);

    }



    /**

     *
     * @param n size of the array
     * @param array array to which element has to be added
     * @param x the adding element/tile
     * @return modified array with element x added to the array
     */
    public static Object[] addX(int n, Object[] array, Object x)
    {
        int i;

        // create a new array of size n+1
        Object[] newArray = new Object[n + 1];


        //copy all the elements of the array and add the element at the last position.
        for (i = 0; i < n; i++) {
            newArray[i] = array[i];
        }

        newArray[n] = x;

        return newArray;
    }
    /**
     * Task 5

     *
     * Draw a random tile from the deck.
     *
     * @param placementSequence a String representing the sequence of tiles
     *                          that have already been played
     * @param totalHands        a String representing all tiles (if any) in
     *                          all players' hands
     * @return a random tile from the deck
     */
    public static String drawFromDeck(String placementSequence, String totalHands) {


        // Number of tiles in placement sequence
        int numTiles = placementSequence.length() / 6;

        //Arrays for counting the occurrences of tiles
        int[] tilesFourCopies = new int[5];
        int[] tilesThreeCopies = new int[2];
        int[] tilesTwoCopies = new int[17];

        //Array for storing the tile which has lesser occurrences.
        Object[] remainingTile=new Object[0];

        //Arrays for tracking the tile
        Object[] tileFourCopies= {"aacb", "cbaa", "acba", "baac","aaaa"};
        Object[] tileThreeCopies={"cbcb" , "bcbc"};
        Object[] tileTwoCopies={"cccc", "bbbb", "dacc", "cdac", "ccda", "accd", "dbba", "adbb", "badb",
                "bbad", "ddbc", "cddb", "bcdd", "dbcd", "adad", "dada", "dddd"};

        int tileLength=(placementSequence.length() / 6)+(totalHands.length()/4);

        //Array for storing placement sequence tiles
        Object[] tileBoard=new Object[numTiles];

        //Array for storing tiles in player hands
        Object[] tileInHands = new Object[totalHands.length()/4];

        //Combining array of tiles during play
        Object[] result=new Object[tileLength];

        for(int i=0;i<(placementSequence.length() / 6);i++)
            tileBoard[i] = placementSequence.substring(i * 6, i * 6 + 4);

        for (int i = 0; i < totalHands.length()/4; i++)
            tileInHands[i] = totalHands.substring(i * 4, i * 4 + 4);

        // given placement sequence tiles and tiles in hands are copied to result array for counting the occurrence of each tile.
        System.arraycopy(tileBoard, 0, result, 0, tileBoard.length);
        System.arraycopy(tileInHands, 0, result, tileBoard.length, tileInHands.length);

        //counting the occurrence of each tile
        for (int i = 0; i < tileLength; i++) {
            String tile = (String) result[i];
            switch (tile) {
                case "aacb":
                    tilesFourCopies[0] += 1;
                    break;
                case "cbaa":
                    tilesFourCopies[1] += 1;
                    break;
                case "acba":
                    tilesFourCopies[2] += 1;
                    break;
                case "baac":
                    tilesFourCopies[3] += 1;
                    break;
                case "aaaa":
                    tilesFourCopies[4] += 1;
                    break;
                case "cbcb":
                    tilesThreeCopies[0] += 1;
                    break;
                case "bcbc":
                    tilesThreeCopies[1] += 1;
                    break;
                case "cccc":
                    tilesTwoCopies[0] += 1;
                    break;
                case "bbbb":
                    tilesTwoCopies[1] += 1;
                    break;
                case "dacc":
                    tilesTwoCopies[2] += 1;
                    break;
                case "cdac":
                    tilesTwoCopies[3] += 1;
                    break;
                case "ccda":
                    tilesTwoCopies[4] += 1;
                    break;
                case "accd":
                    tilesTwoCopies[5] += 1;
                    break;
                case "dbba":
                    tilesTwoCopies[6] += 1;
                    break;
                case "adbb":
                    tilesTwoCopies[7] += 1;
                    break;
                case "badb":
                    tilesTwoCopies[8] += 1;
                    break;
                case "bbad":
                    tilesTwoCopies[9] += 1;
                    break;
                case "ddbc":
                    tilesTwoCopies[10] += 1;
                    break;
                case "cddb":
                    tilesTwoCopies[11] += 1;
                    break;
                case "bcdd":
                    tilesTwoCopies[12] += 1;
                    break;
                case "dbcd":
                    tilesTwoCopies[13] += 1;
                    break;
                case "adad":
                    tilesTwoCopies[14] += 1;
                    break;
                case "dada":
                    tilesTwoCopies[15] += 1;
                    break;
                case "dddd":
                    tilesTwoCopies[16] += 1;
                    break;
            }

        }
        //checking the tile occurrences and adding them in remainingTile array
        for(int h=0;h<tilesFourCopies.length;h++){
            if(tilesFourCopies[h]<4){
                remainingTile=addX(remainingTile.length,remainingTile,tileFourCopies[h]);
            }
        }
        for(int h=0;h<tilesThreeCopies.length;h++){
            if(tilesThreeCopies[h]<3){
                remainingTile=addX(remainingTile.length,remainingTile,tileThreeCopies[h]);
            }
        }
        for(int h=0;h<tilesTwoCopies.length;h++){
            if(tilesTwoCopies[h]<2){
                remainingTile=addX(remainingTile.length,remainingTile,tileTwoCopies[h]);
            }
        }

        //draw the tile randomly  for uncompleted boards
        if(remainingTile.length>1){
            int randomNum;
            //randomNum = ThreadLocalRandom.current().nextInt(0, remainingTile.length + 1);
            randomNum= (int) (Math.random() * remainingTile.length ) ;
            return (String) remainingTile[randomNum];
        }
        //making changes to random number generation for clearing the IndexOutOfBoundsException.

        //return the result for almost completed boards
        return (String) remainingTile[0];

    }

    /**

     * Determine if a given placement sequence follows the rules of the game.
     * These rules are:
     * - No tile can overlap another tile, or any of the central stations.
     * - A tile cannot be placed next to one of the central squares unless it
     * continues or completes an existing track.
     * - If a tile is on an edge of the board, it cannot contain a track that
     * results in a station looping back to itself, UNLESS that tile could not
     * have been placed elsewhere.
     * - If a tile is on a corner of the board, it cannot contain a track that
     * links the two stations adjacent to that corner, UNLESS that tile could
     * not have been placed elsewhere.
     *
     * @param placementSequence A sequence of placements on the board.
     * @return Whether this placement string is valid.
     */

    public static boolean isPlacementSequenceValid(String placementSequence) {

        int numbTiles = placementSequence.length() / 6;

        if (numbTiles == 0) {
            return true; // If board empty, return true.
        }

        boolean[][] validSpaces = new boolean[8][8];  // Represents locations on board.
        // If true, location is a valid placement for tile

        // Initialise board.
        Board board = new Board();

        // Initialise validSpaces by setting all edge locations to true.
        for (int j = 0; j < 8; j++) {
            validSpaces[0][j] = true;
            validSpaces[7][j] = true;
            validSpaces[j][0] = true;
            validSpaces[j][7] = true;
        }


        // Loop iterates through each tile in placementSequence.
        for (int k = 0; k < numbTiles; k++) {

            Tile tile = new Tile(placementSequence, k);
            boolean[][] validSpacesCurrent = new boolean[8][8]; // Records valid placements for current tile.
            for (int i = 0; i < 8; i++) {
                // Copies results from validSpaces into validSpacesCurrent
                System.arraycopy(validSpaces[i],0,validSpacesCurrent[i],0,8);
            }

            // Adjusts validSpacesCurrent to take into account types of tracks on current tile.
            int validSpaceCount = 0;
            switch (tile.type.charAt(0)) {
                case 'b':
                    validSpacesCurrent[0][7] = false;
                    break;
                case 'c':
                    validSpacesCurrent[0][0] = false;
                    break;
                case 'd':
                    for (int i = 0; i < 8; i++) {
                        validSpacesCurrent[0][i] = false;
                    }
                    break;
            }
            switch (tile.type.charAt(1)) {
                case 'b':
                    validSpacesCurrent[7][7] = false;
                    break;
                case 'c':
                    validSpacesCurrent[0][7] = false;
                    break;
                case 'd':
                    for (int i = 0; i < 8; i++) {
                        validSpacesCurrent[i][7] = false;
                    }
                    break;
            }
            switch (tile.type.charAt(2)) {
                case 'b':
                    validSpacesCurrent[7][0] = false;
                    break;
                case 'c':
                    validSpacesCurrent[7][7] = false;
                    break;
                case 'd':
                    for (int i = 0; i < 8; i++) {
                        validSpacesCurrent[7][i] = false;
                    }
                    break;
            }
            switch (tile.type.charAt(3)) {
                case 'b':
                    validSpacesCurrent[0][0] = false;
                    break;
                case 'c':
                    validSpacesCurrent[7][0] = false;
                    break;
                case 'd':
                    for (int i = 0; i < 8; i++) {
                        validSpacesCurrent[i][0] = false;
                    }
                    break;
            }

            // Counts the number of valid placements possible for current tile.
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (validSpacesCurrent[i][j]) {
                        validSpaceCount++;
                    }
                }
            }

            int intLoc = tile.location.toIntRepresentation(); // Tile location represented as single integer.
            int[] locDistance = {-1, 1, -8, 8}; // Distance between tile location and location of tiles adjacent to tile.

            // If tile location is in a valid placement or there are no valid placements on board, update validSpaces
            // and continue loop, otherwise return false.
            if (validSpacesCurrent[tile.location.row][tile.location.column] ||
                    (validSpaceCount == 0 && validSpaces[tile.location.row][tile.location.column])) {
                validSpaces[tile.location.row][tile.location.column] = false;
                for (int d : locDistance) {
                    int n = intLoc + d;
                    int nCol = n % 8;
                    int nRow = n / 8;
                    int iCol = intLoc % 8;
                    if (n >= 0 && n < 64 && Math.abs(nCol - iCol) <= 1 && board.tileFromCoordinates(nRow, nCol) == null) {
                        validSpaces[nRow][nCol] = true;
                    }
                }
                board.updateBoard(tile);
            } else {
                return false;
            }
        }
        return true;
    }

    /**

     *
     * @param placementSequence the placement sequence of the current game
     * @param tileType the piece to be played
     * @return a 8 by 8 boolean array representing the board, showing whether a placement is valid or not.
     */
    public static boolean[][] playableSpaces(String placementSequence,String tileType) {
        int numbTiles = placementSequence.length() / 6;

        boolean[][] validSpaces = new boolean[8][8];  // Represents locations on board.
        // If true, location is a valid placement for tile

        // Initialise board.
        Board board = new Board();

        // Initialise validSpaces by setting all edge locations to true.
        for (int j = 0; j < 8; j++) {
            validSpaces[0][j] = true;
            validSpaces[7][j] = true;
            validSpaces[j][0] = true;
            validSpaces[j][7] = true;
        }

        // Loop iterates through each tile in placementSequence.
        for (int k = 0; k < numbTiles; k++) {

            Tile tile = new Tile(placementSequence, k);
            validSpaces[tile.location.row][tile.location.column] = false;

            int intLoc = tile.location.toIntRepresentation(); // Tile location represented as single integer.
            int[] locDistance = {-1, 1, -8, 8}; // Distance between tile location and location of tiles adjacent to tile.

            // If tile location is in a valid placement or there are no valid placements on board, update validSpaces
            // and continue loop, otherwise return false.
            for (int d : locDistance) {
                int n = intLoc + d;
                int nCol = n % 8;
                int nRow = n / 8;
                int iCol = intLoc % 8;
                if (n >= 0 && n < 64 && Math.abs(nCol - iCol) <= 1 && board.tileFromCoordinates(nRow, nCol) == null) {
                    validSpaces[nRow][nCol] = true;
                }
            }
            board.updateBoard(tile);
        }
        boolean[][] validSpacesCurrent = new boolean[8][8]; // Records valid placements for current tile.
        for (int i = 0; i < 8; i++) {
            // Copies results from validSpaces into validSpacesCurrent
            System.arraycopy(validSpaces[i],0,validSpacesCurrent[i],0,8);
        }

        // Adjusts validSpacesCurrent to take into account types of tracks on current tile.
        int validSpaceCount = 0;
        switch (tileType.charAt(0)) {
            case 'b':
                validSpacesCurrent[0][7] = false;
                break;
            case 'c':
                validSpacesCurrent[0][0] = false;
                break;
            case 'd':
                for (int i = 0; i < 8; i++) {
                    validSpacesCurrent[0][i] = false;
                }
                break;
        }
        switch (tileType.charAt(1)) {
            case 'b':
                validSpacesCurrent[7][7] = false;
                break;
            case 'c':
                validSpacesCurrent[0][7] = false;
                break;
            case 'd':
                for (int i = 0; i < 8; i++) {
                    validSpacesCurrent[i][7] = false;
                }
                break;
        }
        switch (tileType.charAt(2)) {
            case 'b':
                validSpacesCurrent[7][0] = false;
                break;
            case 'c':
                validSpacesCurrent[7][7] = false;
                break;
            case 'd':
                for (int i = 0; i < 8; i++) {
                    validSpacesCurrent[7][i] = false;
                }
                break;
        }
        switch (tileType.charAt(3)) {
            case 'b':
                validSpacesCurrent[0][0] = false;
                break;
            case 'c':
                validSpacesCurrent[7][0] = false;
                break;
            case 'd':
                for (int i = 0; i < 8; i++) {
                    validSpacesCurrent[i][0] = false;
                }
                break;
        }

        // Counts the number of valid placements possible for current tile.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (validSpacesCurrent[i][j]) {
                    validSpaceCount++;
                }
            }
        }

        if (validSpaceCount == 0) {
            return validSpaces;
        } else {
            return validSpacesCurrent;
        }


    }

    /**

     * Determine the current score for the game.
     *
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param numberOfPlayers   The number of players in the game
     * @return an array containing the scores for all players in the game
     */
    public static int[] getScore(String placementSequence, int numberOfPlayers) {

        // Create new Board with placementSequence
        Board board = new Board(placementSequence);

        // Initialise variables used in method
        Location location; // Holds location of current tile being traced when counting length of path
        Location prevLocation; // Holds location of the previous tile being traced when counting length of path
        int[] playerScores = new int[numberOfPlayers]; // Array to hold player scores
        int trackCount; // Holds the number of tiles in a path
        Tile currentTile; // Holds the current Tile being traced

        // Allocate Stations to players depending on the number of players
        Station[][] playerStations = Station.assignStations(numberOfPlayers);

        // For each player, trace through the paths connected to each of the player's station,
        // allocating the score to the playerScores array according to the rules of the game.
        for (int i = 0; i < numberOfPlayers; i++) { // Iterates through each player
            for (int j = 0; j < playerStations[i].length; j++) { // Iterates through each station belonging to the current player
                location = playerStations[i][j].startLocation; // Find the location of the tile connected to the station
                prevLocation = playerStations[i][j].location; // Find the location of the station
                trackCount = 0; // Initialise trackCount

                while (true) { // Continue to loop through each tile in the path
                    assert location != null;
                    if (location.row == -1 || location.column == -1 || location.row == 8 || location.column == 8) {
                        playerScores[i] += trackCount;
                        break; // If path ends at an edge station, add number of tiles to player score and break loop
                    } else if ( (location.row == 3 && location.column == 3) || (location.row == 3 && location.column == 4)
                            || (location.row == 4 && location.column == 3) || (location.row == 4 && location.column == 4)) {
                        playerScores[i] += (trackCount * 2);
                        break; // If path ends in central station add 2 * (Number of tiles in track) to player score and break loop
                    } else if (board.tileFromLocation(location) == null) {
                        break; // If no new tile in path, break loop without changing player score.
                    } else { // Continue tracing through path, adding 1 to tile count.
                        currentTile = board.tileFromLocation(location);
                        trackCount ++;
                        location = Tile.traceDirection(currentTile,prevLocation);
                        prevLocation = currentTile.location;
                    }
                }
            }
        }
        return playerScores; // Return array containing player scores
    }

    /**

     *
     * Given a placement sequence string, generate a valid next move.
     *
     * @param placementSequence a String representing the sequence of piece placements made so far in the game
     * @param piece             a four-character String representing the tile to be placed
     * @param numberOfPlayers   The number of players in the game
     * @return A valid placement of the given tile
     */
    public static String generateMove(String placementSequence, String piece, int numberOfPlayers) {

        // Number of pieces in placementSequence
        int numbPieces = placementSequence.length() / 6;
        // Determine current player by considering number of players and number of tiles on board
        int currentPlayer = numbPieces % numberOfPlayers;
        // Allocate Stations to players depending on the number of players
        Station[][] playerStations = Station.assignStations(numberOfPlayers);
        // Create new Board with placementSequence
        Board board = new Board(placementSequence);

        // Initialise variables.
        int highestTrackCount = 0;
        int highestScore = 0;
        Location newLoc = null;
        Location highestScoreLoc = null;
        Location highestTrackCountLoc = null;
        boolean newPath;
        boolean firstFreeLoc = true;

        // Loop iterates through each station belonging to the current player to find the move that:
        //      - Completes a track, giving the highest score.
        //      - If cannot complete a track, produces the longest track.
        for (int j = 0; j < playerStations[currentPlayer].length; j++) { // Iterates through each station belonging to the current player
            // Holds location of current tile being traced when counting length of path
            Location location = playerStations[currentPlayer][j].startLocation;
            // Holds location of the previous tile being traced when counting length of path
            Location prevLocation = playerStations[currentPlayer][j].location;
            int trackCount = 0; // Initialise trackCount
            newPath = false;

            while (true) { // Continue to loop through each tile in the path
                assert location != null;
                if (location.row == -1 || location.column == -1 || location.row == 8 || location.column == 8) {
                    if (newPath && (trackCount > highestScore)) {
                        highestScore = trackCount;
                        highestScoreLoc = newLoc;
                    }
                    break; // If path ends at an edge station, break loop
                } else if ( (location.row == 3 && location.column == 3) || (location.row == 3 && location.column == 4)
                        || (location.row == 4 && location.column == 3) || (location.row == 4 && location.column == 4)) {
                    if (newPath && (trackCount*2 > highestScore)) {
                        highestScore = trackCount*2;
                        highestScoreLoc = newLoc;
                    }
                    break; // If path ends in central station break loop
                } else if (board.tileFromLocation(location) == null) {
                    if (firstFreeLoc && isPlacementSequenceValid(placementSequence + piece + location.toString())) {
                        highestTrackCount = 1;
                        highestTrackCountLoc = location;
                        firstFreeLoc = false;
                    }
                    if (!newPath && isPlacementSequenceValid(placementSequence + piece + location.toString())) {
                        newPath = true;
                        newLoc = location;
                        location = Tile.traceDirection(new Tile(piece,newLoc),prevLocation);
                        prevLocation = newLoc;
                        trackCount ++;
                    } else {
                        if (newPath && (trackCount > highestTrackCount)) {
                            highestTrackCount = trackCount;
                            highestTrackCountLoc = newLoc;
                        }
                        break;
                    }
                } else { // Continue tracing through path, adding 1 to tile count.
                    Tile currentTile = board.tileFromLocation(location);
                    trackCount ++;
                    location = Tile.traceDirection(currentTile,prevLocation);
                    prevLocation = currentTile.location;
                }
            }
        }

        // Chooses the move with highest score. If no highest score, chooses move that produces the longest track.
        // If no longest track, than places piece in first available spot.
        if (highestScore != 0) {
            return piece + highestScoreLoc.toString();
        } else if (highestTrackCount != 0) {
            return piece + highestTrackCountLoc.toString();
        } else {
            List<Location> freeLocations = board.findEmptyLocations();
            for (Location freeLoc : freeLocations) {
                if (isPlacementSequenceValid(placementSequence + piece + freeLoc.toString())) {
                    return piece + freeLoc.toString();
                }
            }
            return "";
        }
    }

    /**

     *
     * Generates a more strategic piece placement for a given placement sequence and piece.
     * @param placementSequence the placement sequence of the current game.
     * @param piece the piece to be played.
     * @param numberOfPlayers the number of players in the game.
     * @return A valid piece placement that gives the most favourable outcome,
     * according to the measure used in this method.
     */
    public static String generateBetterMove(String placementSequence, String piece, int numberOfPlayers) {
        // Number of pieces in placementSequence
        int numbPieces = placementSequence.length() / 6;
        // Determine current player by considering number of players and number of tiles on board
        int currentPlayer = numbPieces % numberOfPlayers;
        // Allocate Stations to players depending on the number of players
        Station[][] playerStations = Station.assignStations(numberOfPlayers);
        // Create new Board with placementSequence
        Board board = new Board(placementSequence);
        boolean[][] validSpaces = playableSpaces(placementSequence,piece);

        float[] placementRank = new float[64];
        for (int i = 0; i < 64; i++) {
            if (validSpaces[i/8][i%8]) {
                placementRank[i] = 50;
            }
        }
        float SCORE_FACTOR = 0.25f;

        // Initialise variables.
        Location newLoc = null;
        boolean newPath;

        // Loop iterates through each station belonging to the current player to find the move that:
        //      - Completes a track, giving the highest score.
        //      - If cannot complete a track, produces the longest track.
        for (int k = 0; k < numberOfPlayers; k++) {
            for (int j = 0; j < playerStations[k].length; j++) { // Iterates through each station belonging to the current player
                // Holds location of current tile being traced when counting length of path
                Location location = playerStations[k][j].startLocation;
                // Holds location of the previous tile being traced when counting length of path
                Location prevLocation = playerStations[k][j].location;
                int trackCount = 0; // Initialise trackCount
                newPath = false;
                while (true) { // Continue to loop through each tile in the path
                    assert location != null;
                    if (location.row == -1 || location.column == -1 || location.row == 8 || location.column == 8) {
                        if (newPath) {
                            if (k == currentPlayer) {
                                placementRank[newLoc.row * 8 + newLoc.column] += trackCount * SCORE_FACTOR - 5;
                            } else {
                                placementRank[newLoc.row * 8 + newLoc.column] += 5 - trackCount  * SCORE_FACTOR;
                            }
                        }
                        break; // If path ends at an edge station, break loop
                    } else if ((location.row == 3 && location.column == 3) || (location.row == 3 && location.column == 4)
                            || (location.row == 4 && location.column == 3) || (location.row == 4 && location.column == 4)) {
                        if (newPath) {
                            if (k == currentPlayer) {
                                placementRank[newLoc.row * 8 + newLoc.column] += trackCount * 2  * SCORE_FACTOR - 5;
                            } else {
                                placementRank[newLoc.row * 8 + newLoc.column] += 5 - trackCount * 2 * SCORE_FACTOR ;
                            }
                        }
                        break; // If path ends in central station break loop
                    } else if (board.tileFromLocation(location) == null) {
                        if (!newPath && validSpaces[location.row][location.column]) {
                            newPath = true;
                            newLoc = location;
                            location = Tile.traceDirection(new Tile(piece, newLoc), prevLocation);
                            prevLocation = newLoc;
                            trackCount++;
                        } else if (newPath) {
                            if (k == currentPlayer) {
                                placementRank[newLoc.row * 8 + newLoc.column] += trackCount;
                            } else {
                                placementRank[newLoc.row * 8 + newLoc.column] += -trackCount;
                            }
                            break;
                        } else {
                            break;
                        }
                    } else { // Continue tracing through path, adding 1 to tile count.
                        Tile currentTile = board.tileFromLocation(location);
                        trackCount++;
                        location = Tile.traceDirection(currentTile, prevLocation);
                        prevLocation = currentTile.location;
                    }
                }
            }
        }

        float highestRank = 0;
        int highestRankLocation = 0;
        for (int i = 0; i < 64; i++) {
            if (placementRank[i] > highestRank) {
                highestRank = placementRank[i];
                highestRankLocation = i;
            }
        }
        if (highestRank == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (validSpaces[i][j]){
                        return piece + (i) + (j);
                    }
                }
            }
        }

        return piece + (highestRankLocation/8) + (highestRankLocation%8);

    }

    /**

     *
     * Chooses the 'best' piece placement from all tiles types in hand.
     * @param placementSequence the placement sequence of the current game.
     * @param piecesInHand the pieces in the current player's hand.
     * @param numberOfPlayers the number of players in the game.
     * @return A valid piece placement that gives the most favourable outcome, according to the measure of this method.
     */
    public static String generateMoveFromHand(String placementSequence, String piecesInHand, int numberOfPlayers) {
        // Number of pieces in placementSequence
        int numbPieces = placementSequence.length() / 6;
        // Determine current player by considering number of players and number of tiles on board
        int currentPlayer = numbPieces % numberOfPlayers;
        // Allocate Stations to players depending on the number of players
        Station[][] playerStations = Station.assignStations(numberOfPlayers);
        // Create new Board with placementSequence
        Board board = new Board(placementSequence);
        float highestRankOverall = 0;
        String bestPlacement = null;

        for (int l = 0; l < piecesInHand.length() / 4; l++) {
            String piece = piecesInHand.substring(l * 4, (l + 1) * 4);

            boolean[][] validSpaces = playableSpaces(placementSequence, piece);

            float[] placementRank = new float[64];
            for (int i = 0; i < 64; i++) {
                if (validSpaces[i / 8][i % 8]) {
                    placementRank[i] = 50;
                }
            }
            float SCORE_FACTOR = 0.25f;

            // Initialise variables.
            Location newLoc = null;
            boolean newPath;

            // Loop iterates through each station belonging to the current player to find the move that:
            //      - Completes a track, giving the highest score.
            //      - If cannot complete a track, produces the longest track.
            for (int k = 0; k < numberOfPlayers; k++) {
                for (int j = 0; j < playerStations[k].length; j++) { // Iterates through each station belonging to the current player
                    // Holds location of current tile being traced when counting length of path
                    Location location = playerStations[k][j].startLocation;
                    // Holds location of the previous tile being traced when counting length of path
                    Location prevLocation = playerStations[k][j].location;
                    int trackCount = 0; // Initialise trackCount
                    newPath = false;
                    while (true) { // Continue to loop through each tile in the path
                        assert location != null;
                        if (location.row == -1 || location.column == -1 || location.row == 8 || location.column == 8) {
                            if (newPath) {
                                if (k == currentPlayer) {
                                    placementRank[newLoc.row * 8 + newLoc.column] += trackCount * SCORE_FACTOR - 5;
                                } else {
                                    placementRank[newLoc.row * 8 + newLoc.column] += 5 - trackCount * SCORE_FACTOR;
                                }
                            }
                            break; // If path ends at an edge station, break loop
                        } else if ((location.row == 3 && location.column == 3) || (location.row == 3 && location.column == 4)
                                || (location.row == 4 && location.column == 3) || (location.row == 4 && location.column == 4)) {
                            if (newPath) {
                                if (k == currentPlayer) {
                                    placementRank[newLoc.row * 8 + newLoc.column] += trackCount * 2 * SCORE_FACTOR - 5;
                                } else {
                                    placementRank[newLoc.row * 8 + newLoc.column] += 5 - trackCount * 2 * SCORE_FACTOR;
                                }
                            }
                            break; // If path ends in central station break loop
                        } else if (board.tileFromLocation(location) == null) {
                            if (!newPath && validSpaces[location.row][location.column]) {
                                newPath = true;
                                newLoc = location;
                                location = Tile.traceDirection(new Tile(piece, newLoc), prevLocation);
                                prevLocation = newLoc;
                                trackCount++;
                            } else if (newPath) {
                                if (k == currentPlayer) {
                                    placementRank[newLoc.row * 8 + newLoc.column] += trackCount;
                                } else {
                                    placementRank[newLoc.row * 8 + newLoc.column] += -trackCount;
                                }
                                break;
                            } else {
                                break;
                            }
                        } else { // Continue tracing through path, adding 1 to tile count.
                            Tile currentTile = board.tileFromLocation(location);
                            trackCount++;
                            location = Tile.traceDirection(currentTile, prevLocation);
                            prevLocation = currentTile.location;
                        }
                    }
                }
            }

            for (int i = 0; i < 64; i++) {
                if (placementRank[i] > highestRankOverall) {
                    highestRankOverall = placementRank[i];
                    bestPlacement = piece + (i / 8) + (i % 8);
                }
            }
            if (bestPlacement == null) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (validSpaces[i][j]) {
                            bestPlacement = piece + (i) + (j);
                        }
                    }
                }
            }
        }

        return bestPlacement;

    }

    /**

     *
     * Rotates a given tileType by adjusting the String representation.
     * @param tileType the tile to be rotated
     * @return the String representation of the tile after rotating 90 degrees.
     */
    public static String rotateTile(String tileType) {
        char endChar = tileType.charAt(3);
        return endChar + tileType.substring(0,3);
    }

    /**

     *
     * For a given String of tileTypes, rotates each tileType and adds the rotations to
     * the String after the original tile.
     * @param hand the String representation of a list of tiles in hand
     * @return the String representation of a list of tiles with all orientations of each tile added.
     */
    public static String findAllTileRotations(String hand) {
        int numbTiles = hand.length() / 4;
        StringBuilder allRotations = new StringBuilder();
        for (int i = 0; i < numbTiles; i++) {
            String piece = hand.substring(i * 4, (i + 1) * 4);
            String tempPiece = piece;
            StringBuilder pieceSB = new StringBuilder(piece);
            for (int k = 0; k < 3; k++) {
                tempPiece = rotateTile(tempPiece);
                pieceSB.append(tempPiece);
            }
            allRotations.append(pieceSB);
        }
        return allRotations.toString();
    }

    /**
     *
     * Check whether the game has ended
     * @param placementSequence The placementSequence of the current game
     * @return True if all 60 tiles have been placed on the deck.
     */
    public static boolean endOfGame(String placementSequence) {
        return placementSequence.length() / 6 == 60;
    }
}

