


import javafx.scene.paint.Color;

public class Player {

    private final String name;
    private final PlayerType type;
    private final Color colour;

    //assigning each player a colour to differentiate the player.
    public Player(String name, char type, Color colour) {
        this.name = name;
        this.type = PlayerType.fromChar(type);
        this.colour = colour;

    }

    public enum PlayerType {
        PLAYER('P'), COMPUTER('C');

        private final char characterRep;

        PlayerType(char characterRep) {
            this.characterRep = characterRep;
        }

        public char toChar() {
            return characterRep;
        }
        static PlayerType fromChar(char t) {
            if (t == 'P') return PLAYER;
            else if (t == 'C') return COMPUTER;
            else throw new IllegalArgumentException("Invalid tile type: " + t);
        }

    }

    public String getName() {
        return this.name;
    }

    public char getType() {
        return this.type.toChar();
    }

    public Color getColour() {
        return this.colour;
    }

}
