package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static comp1110.ass2.Metro.isPiecePlacementWellFormed;
import static comp1110.ass2.Metro.isPlacementSequenceWellFormed;

/**
 * A very simple viewer for piece placements in the Metro game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {
    /* board layout */
    private static final int SQUARE_SIZE = 70;
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group boardTiles = new Group();
    private final Group initialiseBoard = new Group();
    private TextField textField;

    /**
     * By Emma Liu
     *
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        // FIXME Task 4: implement the simple placement viewer
        /* Check whether placement sequence is well formed. (May change to is placement sequence valid when test
        is completed.) */
        if (!isPlacementSequenceWellFormed(placement)) {
            throw new IllegalArgumentException("Bad placement sequence: \"" + placement + "\"");
        }
        // Clear tiles from board
        boardTiles.getChildren().clear();

        // For each tile in placement sequence, make new BoardTile instance and add to boardTiles.
        for (int i = 0; i < placement.length() / 6; i++) {
            if(isPlacementSequenceWellFormed(placement)) {
                boardTiles.getChildren().add(new BoardTile(placement.substring(i * 6, i * 6 + 6)));
            }
        }
    }

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
        BoardTile(String placement) {
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

            // Adjust image position to match specified tile position.
            setLayoutX(locationX * SQUARE_SIZE);
            setLayoutY(locationY * SQUARE_SIZE);

        }
    }

    /**
     * By Emma Liu
     *
     * Sets background image and sets stations on board
     */
    private void setBoard() {
        initialiseBoard.getChildren().clear();

        // Set background image for board.
        ImageView background = new ImageView();
        background.setImage(new Image(Viewer.class.getResource(URI_BASE + "tile_back_cover.jpg").toString()));
        background.setFitHeight(560);
        background.setFitWidth(560);
        background.setLayoutX(70);
        background.setLayoutY(70);
        background.toBack();
        initialiseBoard.getChildren().add(background);

        // Set station tiles on board.
        for (int i = 1; i < 37; i++) {
            initialiseBoard.getChildren().add(new StationTile(i));
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
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            makePlacement(textField.getText());
            textField.clear();
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FocusGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        // Add children to root
        initialiseBoard.setLayoutX(162);
        initialiseBoard.setLayoutY(9);
        root.getChildren().add(initialiseBoard);
        root.getChildren().add(controls);
        boardTiles.setLayoutX(162 + SQUARE_SIZE);
        boardTiles.setLayoutY(9 + SQUARE_SIZE);
        root.getChildren().add(boardTiles);

        makeControls();
        setBoard();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
