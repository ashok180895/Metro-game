

public enum Station {
    // Contains all edge stations. Fields include the Station number, the location of the Station (as a Location),
    // and the Location on the board that the Station is connected to.


    station_1(1, new Location(0, 7), new Location(-1, 7)),
    station_2(2, new Location(0, 6), new Location(-1, 6)),
    station_3(3, new Location(0, 5), new Location(-1, 5)),
    station_4(4, new Location(0, 4), new Location(-1, 4)),
    station_5(5, new Location(0, 3), new Location(-1, 3)),
    station_6(6, new Location(0, 2), new Location(-1, 2)),
    station_7(7, new Location(0, 1), new Location(-1, 1)),
    station_8(8, new Location(0, 0), new Location(-1, 0)),
    station_9(9, new Location(0, 0), new Location(0, -1)),
    station_10(10, new Location(1, 0), new Location(1, -1)),
    station_11(11, new Location(2, 0), new Location(2, -1)),
    station_12(12, new Location(3, 0), new Location(3, -1)),
    station_13(13, new Location(4, 0), new Location(4, -1)),
    station_14(14, new Location(5, 0), new Location(5, -1)),
    station_15(15, new Location(6, 0), new Location(6, -1)),
    station_16(16, new Location(7, 0), new Location(7, -1)),
    station_17(17, new Location(7, 0), new Location(8, 0)),
    station_18(18, new Location(7, 1), new Location(8, 1)),
    station_19(19, new Location(7, 2), new Location(8, 2)),
    station_20(20, new Location(7, 3), new Location(8, 3)),
    station_21(21, new Location(7, 4), new Location(8, 4)),
    station_22(22, new Location(7, 5), new Location(8, 5)),
    station_23(23, new Location(7, 6), new Location(8, 6)),
    station_24(24, new Location(7, 7), new Location(8, 7)),
    station_25(25, new Location(7, 7), new Location(7, 8)),
    station_26(26, new Location(6, 7), new Location(6, 8)),
    station_27(27, new Location(5, 7), new Location(5, 8)),
    station_28(28, new Location(4, 7), new Location(4, 8)),
    station_29(29, new Location(3, 7), new Location(3, 8)),
    station_30(30, new Location(2, 7), new Location(2, 8)),
    station_31(31, new Location(1, 7), new Location(1, 8)),
    station_32(32, new Location(0, 7), new Location(0, 8));

    public final int stationNumber; // Station number
    final Location startLocation; // Location of tile connected to station
    final Location location; // Location of station

    // Station constructor
    Station(int stationNumber, Location startLocation, Location location) {
        this.stationNumber = stationNumber;
        this.startLocation = startLocation;
        this.location = location;
    }

    /**
     * Assigns the stations to an 2D array based on the number of players
     *
     * @param totalPlayers the number of players
     * @return a 2D array listing out the stations for each player. e.g. player 1 will have stations in array[0][i].
     */
    public static Station[][] assignStations(int totalPlayers) {
        switch (totalPlayers) { // Return station array depending on the number of players
            case 2:
                return new Station[][]{{station_1, station_3, station_5, station_7, station_9, station_11, station_13,
                        station_15, station_17, station_19, station_21, station_23, station_25, station_27, station_29,
                        station_31}, {station_2, station_4, station_6, station_8, station_10, station_12, station_14,
                        station_16, station_18, station_20, station_22, station_24, station_26, station_28, station_30,
                        station_32}};
            case 3:
                return new Station[][]{{station_1, station_4, station_6, station_11, station_15, station_20, station_23,
                        station_25, station_28, station_31}, {station_2, station_7, station_9, station_12, station_14,
                        station_19, station_22, station_27, station_29, station_32},{station_3, station_5, station_8,
                        station_10, station_13, station_18, station_21, station_24, station_26, station_30},
                        {station_16, station_17}};
            case 4:
                return new Station[][]{{station_4, station_7, station_11, station_16, station_20, station_23,
                        station_27, station_32}, {station_3, station_8, station_12, station_15, station_19, station_24,
                        station_28, station_31}, {station_1, station_6, station_10, station_13, station_18, station_21,
                        station_25, station_30}, {station_2, station_5, station_9, station_14, station_17, station_22,
                        station_26, station_29}};
            case 5:
                return new Station[][]{{station_1, station_5, station_10, station_14, station_22, station_28},
                        {station_6, station_12, station_18, station_23, station_27, station_32}, {station_3, station_7,
                        station_15, station_19, station_25, station_29}, {station_2, station_9, station_13, station_21,
                        station_26, station_30}, {station_4, station_8, station_11, station_20, station_24, station_31},
                        {station_16, station_17}};
            case 6:
                return new Station[][]{{station_1, station_5, station_10, station_19, station_27}, {station_2,
                        station_11, station_18, station_25, station_29}, {station_4, station_8, station_14, station_21,
                        station_26}, {station_6, station_15, station_20, station_24, station_31}, {station_3, station_9,
                        station_13, station_23, station_30}, {station_7, station_12, station_22, station_28, station_32},
                        {station_16, station_17}};
            default:
                throw new IllegalStateException("Unexpected value: " + totalPlayers);
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
