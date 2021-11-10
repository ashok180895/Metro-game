package comp1110.ass2.gui;

import comp1110.ass2.Player;
import comp1110.ass2.Station;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static comp1110.ass2.Metro.*;
import static comp1110.ass2.Metro.findAllTileRotations;

public class Game extends Application {
    // By Ashok Nara and Emma Liu

    /* board layout */
    private static final int SQUARE_SIZE = 70;
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;
    private static final int BOARD_LOCATION_X = 34;
    private static final int BOARD_LOCATION_Y = 34;
    private static final double CONTROL_LAYOUT_X = 10.0 * SQUARE_SIZE + BOARD_LOCATION_X;
    public static final int MENU_WIDTH = 400;
    public static final int MENU_HEIGHT = 650;
    public static final String PLACE_SOUND= "hollow.mp3";
    public static final String TILE_SOUND= "per.mp3";
    public static final String END_MESSAGE="firework.mp3";
    private static final String URI_BASE = "assets/";
    public static final int END_STAGE_WIDTH = 512;
    public static final int END_STAGE_HEIGHT = 384;

    // Game nodes
    private final Group root = new Group();
    private final Group backgroundGroup = new Group();
    private final Group initialiseBoard = new Group();
    private final Group stationMarkers = new Group();
    private final VBox controlLayout = new VBox();
    private final VBox scoreInfo = new VBox();
    private final GridPane scoreBoard = new GridPane();
    private final VBox playerControls = new VBox();
    private final HBox currentPlayerHBox = new HBox();
    private final Group boardTiles = new Group();
    private final Group tilesInHand = new Group();

    // Game variables.
    String placementSequence; // Sequences of tiles with types adjusted to match orientation
    String placementSequenceNoRotation; // Sequence of tiles to be used to draw from deck and display on board
    // Array of rotations of each tile in placementSequenceNoRotation. Used to display tiles on board.
    List<Integer> rotationArrayList;
    int numberOfPlayers; // Total number of players, including computers
    int numberOfComps; // Number of computer opponents
    int currentPlayerIndex; // Index of current player
    boolean[] variationsBoolean = new boolean[3]; // Indicate which game variations are active
    String[] currentHands; // Hands of all players
    boolean[][] validPlaces = new boolean[8][8]; // Indicates validity of placement for a DraggableTile
    PlaceTile[] placeTiles = new PlaceTile[64]; // Tiles representing where placements can happen
    PlaceTile highlightedPlaceTile; // Tile closest to Draggable Tile
    List<Player> playerArrayList; // Players of current game
    List<ColourName> colourOptions; // Colour options
    private static List<Color> playerColours; // Players' chosen colours

    // Background
    Rectangle playerControlBackground;
    Rectangle scoreBackground;

    /**
     * By Emma Liu
     *
     * Graphical representation of the track tiles.
     */
    static class BoardTile extends ImageView {
        // BoardTile fields: the type of tile, the x location and the y location
        String tile;
        int locationX;
        int locationY;

        /* Construct new BoardTile from string with first 4 characters determining type of tile and last 2
        characters determining position on board */
        BoardTile(String placement, int rotations) {
            // Check whether tile is well formed
            if (!isPiecePlacementWellFormed(placement)) {
                throw new IllegalArgumentException("Bad placement: \"" + placement + "\"");
            }
            // Assign fields
            this.tile = placement.substring(0,4);
            this.locationX = Integer.parseInt(placement.substring(5,6));
            this.locationY = Integer.parseInt(placement.substring(4,5));

            // Find correct image to represent tile and set size.
            setImage(new Image(Viewer.class.getResource(URI_BASE + tile + ".jpg").toString()));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);

            Rotate rotate = new Rotate();
            rotate.setAngle(90 * rotations);
            rotate.setPivotX(this.getLayoutX()+SQUARE_SIZE/2.0);
            rotate.setPivotY(this.getLayoutY()+SQUARE_SIZE/2.0);
            this.getTransforms().add(rotate);

            // Adjust image position to match specified tile position.
            setLayoutX(locationX * SQUARE_SIZE);
            setLayoutY(locationY * SQUARE_SIZE);

        }
    }


    /**
     * By Ashok Nara
     * Edited by Emma Liu
     *
     *Draggable random tile to place on the board.
     */
    class DraggableTile extends ImageView {
        String tileType;
        String tileTypeRotated;
        boolean draggable;
        // coordinates of the tile.
        private double mouseX;
        private double mouseY;
        // coordinates of the tile.
        private final double startX;
        private final double startY;
        int numbRotations;
        Rotate rotate;

        public DraggableTile(String tileType, boolean draggable, double x, double y){
            this.tileType = tileType;
            this.tileTypeRotated = tileType;
            this.draggable = draggable;
            this.startX = x;
            this.startY = y;
            this.numbRotations = 0;

            // Set up rotations for DraggableTile
            this.rotate = new Rotate();
            rotate.setAngle(90);
            rotate.setPivotX(SQUARE_SIZE/2.0);
            rotate.setPivotY(SQUARE_SIZE/2.0);

            // Set image, size and location on screen
            this.setImage(new Image(Game.class.getResource(URI_BASE + tileType + ".jpg").toString()));
            this.setFitHeight(SQUARE_SIZE);
            this.setFitWidth(SQUARE_SIZE);
            this.setLayoutX(x);
            this.setLayoutY(y);

            // Adjust brightness of tile depending on whether it is draggable or not.
            if (!draggable) {
                ColorAdjust dim = new ColorAdjust();
                dim.setBrightness(-0.65);
                this.setEffect(dim);
            }

            // Rotate tile by double-clicking
            setOnMouseClicked(mouseClick->{
                if (mouseClick.getClickCount() == 2 && variationsBoolean[0]) {
                    numbRotations = (numbRotations + 1) % 4;
                    this.getTransforms().add(rotate);
                    tileTypeRotated = rotateTile(tileTypeRotated);
                }
            });

            //setting the coordinates of mouse and bring tile out.
            setOnMousePressed(mousePress->{
                if (draggable) {
                    mouseX=mousePress.getSceneX();
                    mouseY=mousePress.getSceneY();
                    validPlaces = playableSpaces(placementSequence,this.tileTypeRotated);
                }
                mousePress.consume();
            });

            //changing the position of tile i.e, the tile is being dragged.
            setOnMouseDragged(mouseDrag->{
                if (draggable) {
                    toFront();
                    //setting the latest position from previous position with difference of coordinates.
                    double xDist=mouseDrag.getSceneX()-mouseX;
                    double yDist=mouseDrag.getSceneY()-mouseY;

                    //update tile's position
                    setLayoutX(getLayoutX()+xDist);
                    setLayoutY(getLayoutY()+yDist);

                    // Highlight placement tile closest to draggable tile
                    highlightNearestPlaceTile(mouseDrag.getSceneX(), mouseDrag.getSceneY());

                    // update mouse coordinates.
                    mouseX=mouseDrag.getSceneX();
                    mouseY=mouseDrag.getSceneY();

                    mouseDrag.consume(); //after successful event, the mouseDrag is closed.
                }
            });

            // check the tile be placed in right position according to the rules.
            //if the tile is in outside of board and valid position is pressed by mouse. the tile will be placed there.
            //failing case , the tile will be placed back in the playable window.
            setOnMouseReleased(mouseRelease->{
                if (draggable) {
                    toFront();
                    // getting the location of mouse for placing the tile.
                    int[] tempLocation = locationOnBoard(mouseRelease.getSceneX(), mouseRelease.getSceneY());
                    highlightNearestPlaceTile(startX, startY);
                    String loc = Integer.toString(tempLocation[0]) + (tempLocation[1]);
                    if (tempLocation[0] == -1) {
                        setLayoutX(startX);
                        setLayoutY(startY);
                    } else if (isPlacementSequenceValid(placementSequence +tileTypeRotated+loc)) {
                        implementMove(tileTypeRotated+loc,tileType+loc,numbRotations);
                    } else {
                        setLayoutX(startX);
                        setLayoutY(startY);
                    }
                }
                mouseRelease.consume();
            });
        }
    }



    /**
     * By Emma Liu
     *
     * Graphical representation of stations
     */
    static class StationTile extends ImageView {
        int stationNumber;

        // Construct station tile, according to station numbers.
        // Note that central station tiles have been allocated numbers 33 to 36.
        StationTile(int number) {

            // Ensure that station number with range of station numbers.
            if (number > 36 || number < 1) {
                throw new IllegalArgumentException("Bad Station Number: \"" + number + "\"");
            }
            this.stationNumber = number;

            // Find correct image to represent the station tiles.
            if (stationNumber < 33) {
                // Setting image for stations on the edges of the board
                setImage(new Image(Viewer.class.getResource(URI_BASE + "station" + stationNumber + ".jpg").toString()));
            } else {
                // Setting image for central stations
                setImage(new Image(Viewer.class.getResource(URI_BASE + "centre_station.jpg").toString()));
            }

            // Set station image size
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);

            // Set location and orientation of station on the board
            if (stationNumber < 9) {
                // For stations on upper horizontal edge of the board
                setRotate(180);
                setLayoutX(630 - (stationNumber * 70));
            } else if (stationNumber < 17) {
                // For stations on left edge of the board
                setRotate(90);
                setLayoutY((stationNumber - 8) * 70);
            } else if (stationNumber < 25) {
                // For stations on lower horizontal edge of the board
                setLayoutX((stationNumber - 16) * 70);
                setLayoutY(630);
            } else if (stationNumber < 33) {
                // For stations on right edge of the board
                setRotate(270);
                setLayoutX(630);
                setLayoutY(630 - ((stationNumber - 24) * 70));
            } else {
                // For central stations
                switch (stationNumber) {
                    case 33:
                        setLayoutX(350);
                        setLayoutY(280);
                        break;
                    case 34:
                        setRotate(90);
                        setLayoutX(350);
                        setLayoutY(350);
                        break;
                    case 35:
                        setRotate(180);
                        setLayoutX(280);
                        setLayoutY(350);
                        break;
                    case 36:
                        setRotate(270);
                        setLayoutX(280);
                        setLayoutY(280);
                        break;
                }
            }
        }
    }

    /**
     * By Emma Liu
     *
     * Coloured Rectangles to indicate allocation of stations to players.
     */
    static class StationMarker extends Rectangle {
        public StationMarker(int stationNumber, Color colour) {
            this.setWidth(0.68 * SQUARE_SIZE);
            this.setHeight(0.18 * SQUARE_SIZE);
            this.setFill(colour);

            if (stationNumber < 9) {
                // For stations on upper horizontal edge of the board
                this.setLayoutX(9.16 * SQUARE_SIZE - (stationNumber * SQUARE_SIZE));
                this.setLayoutY(0.02 * SQUARE_SIZE);
            } else if (stationNumber < 17) {
                // For stations on left edge of the board
                setRotate(90);
                this.setLayoutX(-0.23 * SQUARE_SIZE);
                this.setLayoutY((stationNumber - 7.6) * SQUARE_SIZE);
            } else if (stationNumber < 25) {
                // For stations on lower horizontal edge of the board
                this.setLayoutX((stationNumber - 15.84) * SQUARE_SIZE);
                this.setLayoutY(9.81 * SQUARE_SIZE);
            } else if (stationNumber < 33) {
                // For stations on right edge of the board
                this.setRotate(90);
                this.setLayoutX(9.56 * SQUARE_SIZE);
                this.setLayoutY(9.43 * SQUARE_SIZE - ((stationNumber - 24) * SQUARE_SIZE));
            }
        }
    }

    /**
     * By Emma Liu
     *
     * Tiles that mark the placements allowed by the DraggableTiles. Tiles change colour according to whether a
     * placement is valid or not.
     */
    static class PlaceTile extends Rectangle {
        public static final int STROKE_WIDTH = 4;
        private final int placeNumber;

        // Create PlaceTile according to placeNumber
        public PlaceTile(int placeNumber) {
            this.placeNumber = placeNumber;
            this.setHeight(SQUARE_SIZE-STROKE_WIDTH);
            this.setWidth(SQUARE_SIZE-STROKE_WIDTH);
            this.setFill(Color.BLACK);
            this.setOpacity(0.85);
            this.setStroke(Color.BLACK);
            this.setStrokeWidth(STROKE_WIDTH);
            this.setLayoutX((this.placeNumber % 8 + 1) * SQUARE_SIZE + STROKE_WIDTH/2.0);
            this.setLayoutY(((float) (this.placeNumber / 8) + 1) * SQUARE_SIZE + STROKE_WIDTH/2.0);
        }

        int number() {
            return placeNumber;
        }
    }

    enum ColourName {
        Red(Color.RED), Blue(Color.BLUE),
        Green(Color.GREEN), Coral(Color.CORAL),
        Violet(Color.BLUEVIOLET), Turquoise(Color.LIGHTSEAGREEN);

        private final Color colour;

        ColourName( Color colour) {
            this.colour = colour;
        }

        Color getColour() {
            return this.colour;
        }
    }

    /**
     * By Emma
     *
     * Creates menu to select number of players, player name, player colour,
     * no. of computer players and game variations.
     */
    private void createMenu() {
        StackPane menuStackPane = new StackPane();
        Stage menu = new Stage();
        menu.setTitle("Game Options");
        menu.setScene(new Scene(menuStackPane, MENU_WIDTH, MENU_HEIGHT));

        // Title of menu
        Label menuLabel = new Label("Game Options");
        menuLabel.setFont(Font.font(null, FontWeight.BOLD,20));

        // Initialise temporary arrays for game. Temporary arrays are used so that if the New Game was pressed by
        // accident in the middle of the game, the current game settings are not lost.
        List<Player> playerArrayListTemp = new ArrayList<>();
        List<Color> playerColoursTemp = new ArrayList<>();

        // Set background image
        ImageView backgroundImage = new ImageView();
        backgroundImage.setImage(new Image(Game.class.getResource(URI_BASE + "grayscale-trains.jpg").toString()));
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitHeight(MENU_HEIGHT);

        // Initialises a Rectangle that is part of the menu screen background.
        Rectangle menuBackground = new Rectangle(MENU_WIDTH-60, MENU_HEIGHT-60);
        menuBackground.setFill(Color.LIGHTGREY);
        menuBackground.setOpacity(0.75);
        menuBackground.setLayoutX(30);
        menuBackground.setLayoutY(30);

        // Create player name label and textField
        Label playerNameLabel = new Label("Player Name: ");
        playerNameLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        TextField playerName = new TextField();
        playerName.setPromptText("Enter your preferred name.");
        playerName.setPrefColumnCount(10);

        // Create player colour label
        Label playerColourLabel = new Label("Colour: ");
        playerColourLabel.setFont(Font.font(null, FontWeight.BOLD,12));

        // Create player colour comboBox
        colourOptions = new ArrayList<>();
        colourOptions.addAll(Arrays.asList(
                ColourName.Red,
                ColourName.Blue,
                ColourName.Green,
                ColourName.Coral,
                ColourName.Violet,
                ColourName.Turquoise
        ));
        ObservableList<ColourName> colourList = FXCollections.observableArrayList(colourOptions);
        ComboBox<ColourName> colorComboBox = new ComboBox<>();
        colorComboBox.setPrefWidth(MENU_WIDTH/2.0);
        colorComboBox.getItems().addAll(colourList);

        // Set default to the first available colour
        colorComboBox.setValue(colourOptions.get(0));

        // Allow comboBox to display colours
        colorComboBox.setCellFactory(lv -> new ListCell<>(){
            private final Rectangle rectangle;
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                rectangle = new Rectangle(MENU_WIDTH/2.0 - 38, 10);
            }
            @Override
            protected void updateItem(ColourName item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    rectangle.setFill(item.getColour());
                    setGraphic(rectangle);
                }
            }

        });

        // Create number of CPUs label and comboBox
        Label numbCompsLabel = new Label("No. of CPUs:");
        numbCompsLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        ArrayList<String> numbCompArray = new ArrayList<>(
                Arrays.asList("0 CPUs", "1 CPU", "2 CPUs", "3 CPUs", "4 CPUs", "5 CPUs", "6 CPUs"));
        ObservableList<String> numbCompList = FXCollections.observableArrayList(numbCompArray);
        ComboBox<String> numbCompCB = new ComboBox<>(numbCompList);
        numbCompCB.setPrefWidth(MENU_WIDTH/2.0);

        // Set default to 1 computer
        numberOfComps = 1;
        numbCompCB.setValue(numbCompList.get(1));

        // Create variations label and checkboxes
        Label variationsLabel = new Label("Variations");
        variationsLabel.setFont(Font.font(null, FontWeight.BOLD,12));

        CheckBox tileRotationCB = new CheckBox("Tile rotations are permitted");
        tileRotationCB.setIndeterminate(false);

        CheckBox playableHandCB = new CheckBox("All tiles in hand are playable");
        playableHandCB.setIndeterminate(false);

        CheckBox fourTilesCB = new CheckBox("Maximum of four tiles in hand");
        fourTilesCB.setIndeterminate(false);

        // Create button to add player
        Button addPlayer = new Button("Add Player");
        addPlayer.setPrefWidth(MENU_WIDTH/4.0);

        // Initialise feedback label
        Label feedbackLabel = new Label();

        // When Add Player button is added, create new non-computer Player using the supplied name and colour, unless:
        //      - name is greater than 16 characters
        //      - name or colour have not been chosen
        EventHandler<ActionEvent> addPlayerAction =
                e -> {
                    if (playerName.getText().length() > 16) { // check length of name
                        feedbackLabel.setText("Player name contains too many characters.");
                    } else if (playerName.getText() != null && !playerName.getText().isEmpty() &&
                            colorComboBox.getValue()!= null) {

                        // Add player and colour to temporary arrays
                        ColourName chosenColour = colorComboBox.getValue();
                        playerArrayListTemp.add(new Player(playerName.getText(),'P',chosenColour.getColour()));
                        playerColoursTemp.add(chosenColour.getColour());

                        // Adjust number of computer opponents if number of players and computers exceed 6 players
                        int allowableCompIndex = 7 - playerArrayListTemp.size();
                        if (playerArrayListTemp.size() + numberOfComps > 6) {
                            numberOfComps = 6 - playerArrayListTemp.size();
                            numbCompCB.setValue(numbCompList.get(numberOfComps));
                        }

                        // Adjust maximum number of computer opponents in comboBox depending on number of players
                        if (allowableCompIndex>numbCompList.size()) {
                            numbCompList.addAll(numbCompArray.subList(numbCompList.size(), allowableCompIndex));
                        } else {
                            numbCompList.remove(allowableCompIndex, numbCompList.size());
                        }

                        // Update feedback label acknowledge player has been added to the game
                        feedbackLabel.setText(playerName.getText() + " has been added to the game.");

                        // Reset player options for new player
                        playerName.clear();
                        colorComboBox.setValue(null);

                        // If 6 players, automatically start the game.
                        if (playerArrayListTemp.size()==6) {
                            // update playerArrayList and playerColours
                            playerArrayList = new ArrayList<>(playerArrayListTemp);
                            playerColours = new ArrayList<>(playerColoursTemp);

                            // Set game variations
                            variationsBoolean[0] = tileRotationCB.isSelected();
                            variationsBoolean[1] = playableHandCB.isSelected();
                            variationsBoolean[2] = fourTilesCB.isSelected();

                            // close menu
                            menu.close();

                            // Generate computer players and add to playerArrayList
                            generatePlayerArray();
                        } else {
                            // Remove chosen colour from colour comboBox
                            colourOptions.remove(chosenColour);
                            colorComboBox.setValue(colourOptions.get(0));
                            colorComboBox.getItems().remove(chosenColour);
                        }
                    } else if (playerName.getText() == null || playerName.getText().isEmpty()) {
                        // Check if name is null and update feedback label
                        feedbackLabel.setText("You have not submitted a name.");
                    } else {
                        // Update feedback label: Colour has not been chosen
                        feedbackLabel.setText("You have not chosen a colour.");
                    }
                };

        // Set when add player button pressed
        addPlayer.setOnAction(addPlayerAction);

        // Sets number of computers according to number of computer opponent comboBox
        EventHandler<ActionEvent> chooseNumbComps =
                e -> {
                    String numbCompObj = numbCompCB.getValue();
                    numberOfComps = Integer.parseInt(numbCompObj.substring(0, 1));
                };

        // Set when number of computer opponent comboBox is updated
        numbCompCB.setOnAction(chooseNumbComps);

        // Create start button
        Button startButton = new Button("Start");
        startButton.setPrefWidth(MENU_WIDTH/4.0);

        // When start button pressed, copy
        startButton.setOnAction(e -> {
            if (playerArrayListTemp.size() + numberOfComps > 1) {
                // update playerArrayList and playerColours
                playerArrayList = new ArrayList<>(playerArrayListTemp);
                playerColours = new ArrayList<>(playerColoursTemp);

                // Set game variations
                variationsBoolean[0] = tileRotationCB.isSelected();
                variationsBoolean[1] = playableHandCB.isSelected();
                variationsBoolean[2] = fourTilesCB.isSelected();

                // close menu
                menu.close();

                // Generate computer players and add to playerArrayList
                generatePlayerArray();
            } else {
                // If not enough players, update feedback label
                feedbackLabel.setText("You do not have enough players to start.");
            }
        });

        // Set column constraints for menuGrid
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(MENU_WIDTH/5.0);
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(MENU_WIDTH/2.0);

        // Create menuGrid and add children
        GridPane menuGrid = new GridPane();
        menuGrid.getColumnConstraints().addAll(col1,col2);
        menuGrid.setVgap(20);
        menuGrid.setHgap(20);
        menuGrid.add(playerNameLabel,0,0);
        menuGrid.add(playerName, 1,0);
        menuGrid.add(playerColourLabel,0,1);
        menuGrid.add(colorComboBox,1,1);
        menuGrid.add(addPlayer,1,2);
        menuGrid.add(numbCompsLabel,0,4);
        menuGrid.add(numbCompCB,1,4);
        menuGrid.add(variationsLabel,1,6);
        menuGrid.add(tileRotationCB,1,7);
        menuGrid.add(playableHandCB,1,8);
        menuGrid.add(fourTilesCB,1,9);
        menuGrid.add(startButton,1,11);
        GridPane.setHalignment(addPlayer, HPos.RIGHT);
        GridPane.setHalignment(startButton, HPos.RIGHT);

        // Create menuVBox and add children
        VBox menuVBox = new VBox();
        menuVBox.setPadding(new Insets(MENU_WIDTH/5.0,MENU_WIDTH/10.0,MENU_WIDTH/5.0,MENU_WIDTH/10.0));
        menuVBox.setSpacing(40);
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.getChildren().addAll(menuLabel,menuGrid,feedbackLabel);
        menuStackPane.getChildren().addAll(backgroundImage,menuBackground,menuVBox);
        StackPane.setAlignment(backgroundImage,Pos.CENTER);
        StackPane.setAlignment(menuVBox,Pos.CENTER);

        // Show menu
        menu.show();
    }

    /**
     * By Emma Liu
     *
     * Sets background image and sets stations on board
     */
    private void setBoard() {
        initialiseBoard.getChildren().clear();

        // Set place tiles on board.
        for (int i = 0; i < 27; i++) {
            PlaceTile placeTile = new PlaceTile(i);
            placeTiles[i] = placeTile;
            initialiseBoard.getChildren().add(placeTile);
        }
        for (int i = 29; i < 35; i++) {
            PlaceTile placeTile = new PlaceTile(i);
            placeTiles[i] = placeTile;
            initialiseBoard.getChildren().add(placeTile);
        }
        for (int i = 37; i < 64; i++) {
            PlaceTile placeTile = new PlaceTile(i);
            placeTiles[i] = placeTile;
            initialiseBoard.getChildren().add(placeTile);
        }

        // Set station tiles on board.
        for (int i = 1; i < 37; i++) {
            initialiseBoard.getChildren().add(new StationTile(i));
        }
    }

    /**
     * By Emma Liu
     *
     * Generates array of players and computer opponents for game.
     */
    void generatePlayerArray() {
        // Add computer opponents to player array
        for (int i = 0; i < numberOfComps; i++) {
            playerArrayList.add(new Player("CPU " + (i + 1), 'C', colourOptions.get(i).getColour()));
            playerColours.add(colourOptions.get(i).getColour());
        }

        // Set number of players
        numberOfPlayers = playerArrayList.size();

        // Find corresponding secondary colours to player colours
        List<Color> secondaryColours = new ArrayList<>();
        for (Player player : playerArrayList) {
            switch (player.getColour().toString()) {
                case "0xff0000ff":
                    secondaryColours.add(Color.LIGHTPINK);
                    break;
                case "0x0000ffff":
                    secondaryColours.add(Color.LIGHTBLUE);
                    break;
                case "0x008000ff":
                    secondaryColours.add(Color.LIGHTGREEN);
                    break;
                case "0xff7f50ff":
                    secondaryColours.add(Color.LIGHTSALMON);
                    break;
                case "0x8a2be2ff":
                    secondaryColours.add(Color.THISTLE);
                    break;
                case "0x20b2aaff":
                    secondaryColours.add(Color.LIGHTCYAN);
                    break;
            }
        }

        // Add secondary colours to playerColours
        playerColours.addAll(secondaryColours);

        // Initialise new game
        newGame();
    }

    /**
     *  By Ashok Nara
     *
     * @param tone is type of sound effect
     * Plays different sound based different situation in the game.
     */
    void sound(String tone){
        Media media = new Media(Game.class.getResource(URI_BASE+tone).toString());
        MediaPlayer player = new MediaPlayer(media);
        player.play();
    }

    /**
     * Modified by Ashok Nara and Emma Liu
     *
     * Create a basic text field for input , play button and list for number of players.
     */
    private void makeControls() {
        // Create new game button
        Button startNewGame = new Button("New Game");

        // When selected, go to createMenu()
        startNewGame.setOnAction(e -> createMenu());

        // Create gameControls and add children
        HBox gameControls = new HBox();
        gameControls.getChildren().add(startNewGame);

        // Add controls to controlLayout
        scoreInfo.setSpacing(10);
        playerControls.setSpacing(20);
        controlLayout.getChildren().addAll(gameControls, scoreInfo, playerControls);
    }

    /**
     * By Emma Liu
     *
     * Sets up the game window to begin a new game according to the number of players, by clearing the previous
     * game's information and initialising game variables.
     */
    void newGame() {

        // Clear previous game.
        scoreInfo.getChildren().clear();
        playerControls.getChildren().clear();
        boardTiles.getChildren().clear();
        tilesInHand.getChildren().clear();
        scoreBoard.getChildren().clear();
        currentPlayerHBox.getChildren().clear();
        initialiseBoard.getChildren().remove(stationMarkers);
        backgroundGroup.getChildren().removeAll(scoreBackground, playerControlBackground);

        // Initialises game variables.
        this.placementSequence = "";
        this.placementSequenceNoRotation = "";
        this.currentPlayerIndex = 0;
        this.rotationArrayList = new ArrayList<>();
        this.currentHands = new String[this.numberOfPlayers];
        Arrays.fill(this.currentHands, "");

        // Initialises a Rectangle that is part of the ending screen.
        scoreBackground = new Rectangle(VIEWER_WIDTH-(CONTROL_LAYOUT_X+48),
                0.38 * SQUARE_SIZE * numberOfPlayers + 45.4);
        scoreBackground.setFill(Color.LIGHTGREY);
        scoreBackground.setOpacity(0.75);
        scoreBackground.setLayoutX(CONTROL_LAYOUT_X+32);
        scoreBackground.setLayoutY(BOARD_LOCATION_Y+1.75*SQUARE_SIZE);
        updateScoreBoard();

        // Sets up the part of the background of the game.
        playerControlBackground = new Rectangle(VIEWER_WIDTH-(CONTROL_LAYOUT_X+48),
                215 + ((variationsBoolean[0]) ? 15 : 0)+ ((variationsBoolean[2]) ? (SQUARE_SIZE + 25) : 0));
        playerControlBackground.setFill(playerColours.get(numberOfPlayers + currentPlayerIndex));
        playerControlBackground.setOpacity(0.75);
        playerControlBackground.setLayoutX(CONTROL_LAYOUT_X+32);
        playerControlBackground.setLayoutY(BOARD_LOCATION_Y + 1.75 * SQUARE_SIZE +
                0.38 * SQUARE_SIZE * numberOfPlayers + 58.4);

        // Add the background Rectangles and text to root.
        backgroundGroup.getChildren().addAll(scoreBackground,playerControlBackground);

        // Colour code stations according to the number of players
        assignStationColours();

        // Display information of the current score.
        Text score = new Text("Score");
        score.setFont(Font.font(null, FontWeight.BOLD,16));

        // Add button to add another tile into the current player's hand.
        Button drawNewTileButton = new Button("Draw new tile");
        drawNewTileButton.setOnAction(e -> drawNewTile());

        // Add children to root.
        scoreBoard.setVgap(10);
        scoreBoard.setHgap(10);
        scoreInfo.getChildren().addAll(score,scoreBoard);
        playerControls.getChildren().addAll(currentPlayerHBox, drawNewTileButton);

        // Give instructions for rotation if rotate variation chosen
        if (variationsBoolean[0]) {
            Label rotateInstruction = new Label("             double-click to rotate tile");
            rotateInstruction.setFont(Font.font(null, FontWeight.LIGHT, FontPosture.ITALIC,11));
            playerControls.getChildren().add(rotateInstruction);
        }

        // Check first player
        checkPlayerTurn();
    }

    /**
     * By Emma Liu
     *
     * Checks whether the next player is a person or computer and runs the appropriate methods.
     */
    void checkPlayerTurn() {
        // Generate String containing all the tiles that are currently held by players.
        StringBuilder allHands = new StringBuilder();
        for (int i = 0; i < numberOfPlayers; i++) {
            allHands.append(currentHands[i]);
        }

        // Find number remaining tiles in deck
        int remainingTiles = 60 - placementSequence.length()/6 - allHands.length()/4;
        if (playerArrayList.get(currentPlayerIndex).getType() == 'P' && (!currentHands[currentPlayerIndex].equals("")
                || remainingTiles > 0)) {
            // If player's turn and tiles remaining in either deck or hand, allocate turn to player
            playerTurnSetUp();
        } else if (playerArrayList.get(currentPlayerIndex).getType() == 'C' && (!currentHands[currentPlayerIndex].equals("")
                || remainingTiles > 0)) {
            // If computer's turn and tiles remaining in either deck or hand, allocate turn to computer
            compPlay();
        } else {
            // If no tiles in deck or hand, skip to next turn
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
            checkPlayerTurn();
        }
    }

    /**
     * By Emma Liu
     *
     * Prepares the layout so a player can play their turn.
     */
    void playerTurnSetUp() {
        // Adjust background to match player colour.
        playerControlBackground.setFill(playerColours.get(numberOfPlayers + currentPlayerIndex));

        // Display tiles in player's current hand
        displayHand();

        // Display name of player
        Text currentPlayerText = new Text("Current Player: " + playerArrayList.get(currentPlayerIndex).getName());
        currentPlayerText.setFont(Font.font(null, FontWeight.BOLD,16));

        // Add to currentPlayerHBox
        currentPlayerHBox.getChildren().clear();
        currentPlayerHBox.getChildren().add(currentPlayerText);
    }

    /**
     * By Emma Liu
     *
     * Places DraggableTiles on the screen, representing the current player's hand.
     * Only one of the DraggableTiles can be placed on the board, as according to the Metro rules.
     */
    private void displayHand() {
        // Clear previous player's hand.
        tilesInHand.getChildren().clear();

        // Go through String containing the current player's hand and creates new DraggableTiles accordingly,
        // adding the DraggableTile to the tilesInHand node.
        String currentPlayerHand = currentHands[currentPlayerIndex];
        int numbTilesInHand = currentPlayerHand.length()/4;
        for (int i = 0; i < numbTilesInHand; i++) {
            // Set position of tiles
            int col = (numbTilesInHand - 1 - i) % 2;
            int row = (numbTilesInHand - 1 - i) / 2;
            double x = (10.0 * SQUARE_SIZE) + BOARD_LOCATION_X + 70 + (SQUARE_SIZE + 25.0) * col;
            double y = controlLayout.getHeight() - ((variationsBoolean[0]) ? 20 : 0) + (SQUARE_SIZE + 25.0) * row;

            // Create new DraggableTile and add to tilesInHand
            DraggableTile tile = new DraggableTile(currentPlayerHand.substring(i * 4, (i + 1) * 4),
                    i == numbTilesInHand - 1 || variationsBoolean[1], x, y);
            tilesInHand.getChildren().add(tile);
        }
    }

    /**
     * By Emma Liu
     *
     * If the current player's hand contains less than two tiles, this method will draw a random tile
     * and add it to the current player's hand.
     */
    private void drawNewTile() {
        // Check that player is eligible to draw a new tile.
        if (currentHands[currentPlayerIndex].length()/4 < ((variationsBoolean[2]) ? 4 : 2)) {
            // Generate String containing all the tiles that are currently held by players.
            StringBuilder allHands = new StringBuilder();
            for (int i = 0; i < numberOfPlayers; i++) {
                allHands.append(currentHands[i]);
            }

            // Draw random tile and add it to hand.
            currentHands[currentPlayerIndex] = currentHands[currentPlayerIndex] +
                    drawFromDeck(placementSequenceNoRotation, allHands.toString());
            sound(TILE_SOUND);
            // Display all tiles in hand
            displayHand();
        }
    }

    /**
     * By Ashok Nara
     * (Modified by Emma Liu)
     *
     * Draws a new tile and adds it to the current computer opponent's hand.
     *
     * @return the computer's hand.
     */
    public String drawForComp(){
        // Create a string containing all players' hands
        StringBuilder allHands = new StringBuilder();
        for (int i = 0; i < this.numberOfPlayers; i++) {
            allHands.append(currentHands[i]);
        }

        if (placementSequence.length()/6 + allHands.length()/4 == 60) {
            // If no more tiles in deck, return hand unchanged
            return currentHands[currentPlayerIndex];
        } else if (!variationsBoolean[1]) {
            // If only first tile playable, draw only one tile and return hand. This means that the computer
            // draws a tile and plays it immediately, not holding any tiles when it is not its turn.
            String newTile = drawFromDeck(placementSequenceNoRotation, allHands.toString());
            currentHands[currentPlayerIndex] = newTile;
            return newTile;
        } else {
            // Draws and adds maximum allowed tiles to current hand.
            String currentHand = currentHands[currentPlayerIndex];
            StringBuilder currentHandSB = new StringBuilder(currentHand);
            int numbTilesInHand = currentHand.length() / 4;
            int tilesToDraw = ((variationsBoolean[2]) ? 4 : 2) - numbTilesInHand; // Checks if maximum is 2 or 4
            for (int i = 0; i < tilesToDraw; i++) {
                String newTile = drawFromDeck(placementSequenceNoRotation, allHands.toString());
                currentHandSB.append(newTile);
                allHands.append(newTile);
            }
            currentHands[currentPlayerIndex] = currentHandSB.toString();
            return currentHandSB.toString();
        }
    }

    /**
     * By Ashok Nara
     * Game variation capability added by Emma Liu
     */
    void compPlay(){
        // Draw new tile(s) for computer, depending on maximum playable tiles allowed
        String pe = drawForComp();

        // Generates tile options depending on variations chosen
        if (variationsBoolean[0]) {
            // if tile rotations allowed, find all orientations of each tile in hand
            String withRotations = findAllTileRotations(pe);

            // generates "best" move from tile options
            String compTile = generateMoveFromHand(placementSequence,withRotations,numberOfPlayers);

            // Find index of played option
            String compTileType = compTile.substring(0,4);
            int ind = 0;
            for (int i = 0; i < withRotations.length() / 4; i++) {
                if (compTileType.equals(withRotations.substring(i * 4, (i + 1) * 4))) {
                    ind = i;
                    break;
                }
            }

            // Find number of rotations
            int numbRotations = ind % 4;

            // Find original tile (tile without rotation)
            String originalPiece = withRotations.substring((ind - numbRotations) * 4, (ind + 1 - numbRotations) * 4);

            // Update placementSequence, placementSequenceNoRotation and rotationArrayList with computer's move
            placementSequence = placementSequence + compTile;
            placementSequenceNoRotation = placementSequenceNoRotation + originalPiece + compTile.substring(4);
            rotationArrayList.add(numbRotations);

            // Remove original tile from hand
            removeTileFromHand(originalPiece);
        } else {
            // Generate move from tiles in hand
            String compTile = generateMoveFromHand(placementSequence, pe, numberOfPlayers);

            // Update placementSequence, placementSequenceNoRotation and rotationArrayList with computer's move
            placementSequence = placementSequence + compTile;
            placementSequenceNoRotation = placementSequenceNoRotation + compTile;
            rotationArrayList.add(0);

            // Remove tile from hand
            removeTileFromHand(compTile.substring(0,4));
        }

        // Update scores and board
        updateScoreBoard();

        makePlacement();

        // Check if end of game, otherwise assign next player as the current player and check player
        if (endOfGame(placementSequence)) {
            endGameMessage();
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
            checkPlayerTurn();
        }
    }

    /**
     * By Emma Liu
     *
     * Implements changes in game when a new piece placed on the board.
     * @param piecePlacement The new piece placement to be added to the placementSequence.
     */
    void implementMove(String piecePlacement, String pieceNoRotation, int rotations) {
        // Update placementSequence, placementSequenceNoRotation and rotationArrayList with player's move
        placementSequence = placementSequence + piecePlacement;
        placementSequenceNoRotation = placementSequenceNoRotation + pieceNoRotation;
        rotationArrayList.add(rotations);

        // Remove tile from hand
        removeTileFromHand(pieceNoRotation.substring(0,4));

        // Update scores and board
        makePlacement();
        updateScoreBoard();

        // Check whether the game is complete.
        if (endOfGame(placementSequence)) {
            endGameMessage();
        } else {
            // Assign next player as the current player and check player
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
            checkPlayerTurn();
        }
    }

    /**
     * By Emma Liu
     *
     * Remove's played piece from the current player's hand.
     * @param pieceNoRotation the original played piece (no rotations)
     */
    void removeTileFromHand(String pieceNoRotation) {
        // Create temporary hand
        StringBuilder tempHand = new StringBuilder();

        // Go through each tile in hand. If it does not match the played piece, add to temporary hand.
        // If it matches the played piece, skip the tile, copy the remaining pieces and break the loop.
        for (int i = 0; i < currentHands[currentPlayerIndex].length() / 4; i++) {
            String tile = currentHands[currentPlayerIndex].substring(i * 4,(i + 1) * 4);
            if (tile.equals(pieceNoRotation)) {
                tempHand.append(currentHands[currentPlayerIndex].substring((i + 1) * 4));
                break;
            } else {
                tempHand.append(tile);
            }
        }

        // Update current hand.
        currentHands[currentPlayerIndex] = tempHand.toString();
    }

    /**
     * By Emma Liu
     *
     * Updates display of scores.
     */
    void updateScoreBoard() {
        // Clear previous scores
        scoreBoard.getChildren().clear();

        // Get score array
        int[] score = getScore(placementSequence,numberOfPlayers);

        // Generate text and add to scoreBoard
        for (int i = 0; i < this.numberOfPlayers; i++) {
            Text playerName = new Text(playerArrayList.get(i).getName() + ": ");
            playerName.setFont(Font.font(null, FontWeight.BOLD,11));
            scoreBoard.add(playerName,0,i);
            scoreBoard.add(new Text(Integer.toString(score[i])),1,i);
        }
    }

    /**
     * By Emma Liu
     *
     * Finds coordinates of tile position on board from the coordinates in the game window.
     * @param x the x-coordinate on the game window.
     * @param y the y-coordinate on the game window.
     * @return If the game coordinates are on the board, this method returns the location of the tile position
     * on the board as a int[]. Otherwise it returns the [-1,-1].
     */
    int[] locationOnBoard(double x, double y) {
        // Boundaries of board
        double boardLeft = BOARD_LOCATION_X + SQUARE_SIZE;
        double boardRight = BOARD_LOCATION_X + SQUARE_SIZE * 9;
        double boardTop = BOARD_LOCATION_Y + SQUARE_SIZE;
        double boardBottom = BOARD_LOCATION_Y + SQUARE_SIZE * 9;

        // Check whether location within boundaries of board
        if (x <= boardLeft || x >= boardRight || y <= boardTop || y >= boardBottom) {
            return new int[]{-1,-1};
        } else {
            // If within boundaries, give integer location of placement tile
            int a = (int) Math.floor((y - boardTop) / SQUARE_SIZE);
            int b = (int) Math.floor((x - boardLeft) / SQUARE_SIZE);
            return new int[]{a,b};
        }
    }

    /**
     * By Emma Liu
     *
     * Finds PlaceTile closest to given location and highlights it either green or red depending
     * if it is a valid placement for the current DraggableTile.
     * @param x the x-coordinate on the game window.
     * @param y the y-coordinate on the game window.
     */
    private void highlightNearestPlaceTile(double x, double y) {
        // Turn previous highlighted tile black
        if (highlightedPlaceTile != null) {
            highlightedPlaceTile.setFill(Color.BLACK);
            highlightedPlaceTile.setStroke(Color.BLACK);
            highlightedPlaceTile.setOpacity(0.85);
        }

        // Find closest tile
        int[] location = locationOnBoard(x,y);
        String stringLoc = Integer.toString(location[0]) + (location[1]);

        // Location on top of placement tile, highlight placement tile green if it is a valid placement,
        // or red if it is not
        if (location[0] != -1 && !stringLoc.equals("33") && !stringLoc.equals("34")
                && !stringLoc.equals("43") && !stringLoc.equals("44")) {
            highlightedPlaceTile = placeTiles[location[0] * 8 + location[1]];
            if (validPlaces[highlightedPlaceTile.number() / 8][highlightedPlaceTile.number() % 8]) {
                highlightedPlaceTile.setFill(Color.GREEN);
                highlightedPlaceTile.setStroke(Color.DARKGREEN);
            } else {
                highlightedPlaceTile.setFill(Color.RED);
                highlightedPlaceTile.setStroke(Color.DARKRED);
            }
            highlightedPlaceTile.setOpacity(0.7);
        } else {
            highlightedPlaceTile = null;
        }
    }

    /**
     * By Emma Liu
     *
     * Draw a placement in the window, removing any previously drawn one
     */
    private void makePlacement() {
        // Clear tiles from board
        boardTiles.getChildren().clear();

        // For each tile in placement sequence, make new BoardTile instance and add to boardTiles.
        for (int i = 0; i < placementSequenceNoRotation.length() / 6; i++) {
            boardTiles.getChildren().add(new BoardTile(placementSequenceNoRotation.substring(i * 6, i * 6 + 6),
                    rotationArrayList.get(i)));
        }
        sound(PLACE_SOUND);
    }

    /**
     * By Emma Liu
     *
     * Add coloured Rectangles on top of StationTiles according to the number of players,
     * so that the players can identify which station is theirs.
     */
    private void assignStationColours() {
        Station[][] playerStations = Station.assignStations(numberOfPlayers);

        for (int i = 0; i < numberOfPlayers; i++) {
            for (Station s: playerStations[i]) {
                Color currentColour = playerColours.get(i);
                stationMarkers.getChildren().add(new StationMarker(s.stationNumber, currentColour));
            }
        }
        if (numberOfPlayers == 3 || numberOfPlayers == 5 || numberOfPlayers == 6) {
            for (Station s: playerStations[numberOfPlayers]) {
                Color currentColour = Color.WHITE;
                stationMarkers.getChildren().add(new StationMarker(s.stationNumber, currentColour));
            }
        }
        initialiseBoard.getChildren().add(stationMarkers);
    }

    /**
     * By Emma Liu
     *
     * Create the message that appears once a game is completed, stating the winning player(s) and the final score.
     */
    private void endGameMessage() {
        sound(END_MESSAGE);
        // Set up stage
        StackPane endMessage = new StackPane();
        Stage endGameStage = new Stage();
        endGameStage.setTitle("Congratulations Message");
        endGameStage.setScene(new Scene(endMessage, END_STAGE_WIDTH, END_STAGE_HEIGHT));

        // Initialise nodes
        HBox winnersHBox = new HBox();
        VBox endGameText = new VBox();

        // Place background image
        ImageView backgroundImage = new ImageView();
        backgroundImage.setImage(new Image(Game.class.getResource(URI_BASE + "grayscale-trains.jpg").toString()));
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitHeight(END_STAGE_HEIGHT);

        // Set background rectangle
        Rectangle endGameRectangle = new Rectangle(END_STAGE_WIDTH-60,END_STAGE_HEIGHT-60);
        endGameRectangle.setOpacity(0.75);

        // Find final score
        int[] score = getScore(placementSequence,numberOfPlayers);

        // Find the winning players.
        int maxScore = 0;
        LinkedList<Integer> winningPlayers = new LinkedList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            if (score[i] > maxScore) {
                winningPlayers.clear();
                winningPlayers.add(i);
                maxScore = score[i];
            } else if (score[i] == maxScore) {
                winningPlayers.add(i);
            }
        }

        // Set up drop shadow effect for text
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        // Create text
        Text congratulations = new Text("Congratulations,");
        congratulations.setFont(Font.font(null, FontWeight.BOLD,32));
        congratulations.setEffect(ds);
        Text youHaveWon = new Text();
        youHaveWon.setFont(Font.font(null, FontWeight.BOLD,26));
        youHaveWon.setEffect(ds);

        // Generate winning players text and add it to endGameText node
        if (winningPlayers.size() == 0) {
            // If there are no winners...? Set text.
            Text noWinners = new Text("...on finishing. There are no winners \n(for some reason...)");
            noWinners.setFont(Font.font(null, FontWeight.BOLD,20));
            noWinners.setEffect(ds);
            noWinners.setTextAlignment(TextAlignment.CENTER);
            winnersHBox.getChildren().add(noWinners);
            winnersHBox.setAlignment(Pos.CENTER);

            // Set background rectangle to Color.LIGHTGREY
            endGameRectangle.setFill(Color.LIGHTGREY);
        } else if (winningPlayers.size() == 1) {
            // If there is one winner
            Text oneWinner = new Text(playerArrayList.get(winningPlayers.get(0)).getName() + "!");
            oneWinner.setFont(Font.font(null, FontWeight.BOLD,26));
            oneWinner.setEffect(ds);
            winnersHBox.getChildren().add(oneWinner);
            winnersHBox.setAlignment(Pos.CENTER);
            youHaveWon.setText("You have won!");

            // Set message box to winning player's colour.
            endGameRectangle.setFill(playerColours.get(numberOfPlayers + winningPlayers.get(0)));
        } else {
            // If there are more than one winner.
            StringBuilder winnersSB = new StringBuilder(playerArrayList.get(winningPlayers.get(0)).getName());
            // Add second to second last winner to StringBuilder
            for (int i = 1; i < winningPlayers.size()-1; i++) {
                if (winningPlayers.size()>4 && i == 3) {
                    winnersSB.append(",\n").append(playerArrayList.get(winningPlayers.get(i)).getName());
                } else {
                    winnersSB.append(", ").append(playerArrayList.get(winningPlayers.get(i)).getName());
                }
            }
            // Add last winner to StringBuilder
            winnersSB.append(" and ").append(playerArrayList.get(winningPlayers.get(winningPlayers.size() - 1))
                    .getName()).append("!");

            // Create text
            Text multiWinners = new Text(winnersSB.toString());
            multiWinners.setTextAlignment(TextAlignment.CENTER);
            multiWinners.setFont(Font.font(null, FontWeight.BOLD,26));
            multiWinners.setEffect(ds);
            winnersHBox.getChildren().add(multiWinners);
            winnersHBox.setAlignment(Pos.CENTER);

            // Adjust youHaveWon text according to if there are two or more than two players.
            if (winningPlayers.size()==2) {
                youHaveWon.setText("You have both have won!");
            } else {
                youHaveWon.setText("You all have won!");
            }

            // Set background rectangle to Color.LIGHTGREY
            endGameRectangle.setFill(Color.LIGHTGREY);
        }
        endGameText.getChildren().addAll(congratulations,winnersHBox,youHaveWon);

        // Generate score information
        Text finalScoreText = new Text("Final Score");
        finalScoreText.setFont(Font.font(null,FontWeight.BOLD, 16));
        finalScoreText.setUnderline(true);
        StringBuilder scoresSB = new StringBuilder();
        for (int i = 0; i < this.numberOfPlayers; i++) {
            scoresSB.append("  ").append(playerArrayList.get(i).getName()).append(": ").append(score[i]).append("  ");
            if (numberOfPlayers > 4 && i == 2) {
                scoresSB.append("\n");
            }
        }
        Text playerScore = new Text(scoresSB.toString());
        playerScore.setFont(Font.font(null,FontWeight.BOLD, 14));
        playerScore.setTextAlignment(TextAlignment.CENTER);

        // Add scores to endGameText
        endGameText.getChildren().addAll(finalScoreText,playerScore);
        endGameText.setSpacing(15);
        endGameText.setPadding(new Insets(20,40,20,40));
        endGameText.setAlignment(Pos.CENTER);

        // Add endGameText and background to endMessage
        endMessage.getChildren().addAll(backgroundImage,endGameRectangle,endGameText);
        StackPane.setAlignment(endGameRectangle,Pos.CENTER);

        // Show stage
        endGameStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Metro Game");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        // Set background image
        ImageView backgroundImage = new ImageView();
        backgroundImage.setImage(new Image(Game.class.getResource(URI_BASE + "cityscape-background.png").toString()));
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitHeight(VIEWER_HEIGHT);
        backgroundGroup.getChildren().add(backgroundImage);

        // Set node layout
        initialiseBoard.setLayoutX(BOARD_LOCATION_X);
        initialiseBoard.setLayoutY(BOARD_LOCATION_Y);
        controlLayout.setLayoutX(CONTROL_LAYOUT_X);
        controlLayout.setLayoutY(BOARD_LOCATION_Y);
        controlLayout.setPadding(new Insets(70, 55, 70, 55));
        controlLayout.setSpacing(40);
        boardTiles.setLayoutX(BOARD_LOCATION_X + SQUARE_SIZE);
        boardTiles.setLayoutY(BOARD_LOCATION_Y + SQUARE_SIZE);

        // Add nodes to root
        root.getChildren().addAll(backgroundGroup,initialiseBoard,boardTiles,controlLayout,tilesInHand);

        // Make controls and set board
        makeControls();
        setBoard();

        // Set scene and show stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}