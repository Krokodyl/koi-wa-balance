package enums;

public enum EditorColor {

    BLACK(1,0),
    DARK_GREY(2,8),
    LIGHT_GREY(3,4),
    WHITE(4,12),
    DARK_BLUE(5,2),
    DARK_GREEN(6,10),
    BROWN(7,6),
    PURPLE(8,14),
    LIGHT_BLUE(9,1),
    LIGHT_GREEN(10,9),
    BEIGE(11,5),
    RED(12,13),
    TEAL(13,3),
    YELLOW(14,11),
    ORANGE(15,7),
    PINK(16,15);


    private int position;
    private int fourBitsValue;


    EditorColor(int position, int fourBitsValue) {
        this.position = position;
        this.fourBitsValue = fourBitsValue;
    }

    public int getPosition() {
        return position;
    }

    public int getFourBitsValue() {
        return fourBitsValue;
    }

    public static EditorColor getEditorColor(int fourBitsValue) {
        for (EditorColor ec:values()) {
            if (ec.fourBitsValue == fourBitsValue) return ec;
        }
        return null;
    }

    public static EditorColor getEditorColor(GameColor side1, GameColor side2) {
        switch (side1) {
            case TRANSPARENT:
                switch (side2) {
                    case TRANSPARENT:
                        return BLACK;
                    case WHITE:
                        return WHITE;
                    case BLACK:
                        return DARK_GREY;
                    case GREY:
                        return LIGHT_GREY;
                }
            case WHITE:
                switch (side2) {
                    case TRANSPARENT:
                        return TEAL;
                    case WHITE:
                        return PINK;
                    case BLACK:
                        return YELLOW;
                    case GREY:
                        return ORANGE;
                }
            case BLACK:
                switch (side2) {
                    case TRANSPARENT:
                        return DARK_BLUE;
                    case WHITE:
                        return PURPLE;
                    case BLACK:
                        return DARK_GREEN;
                    case GREY:
                        return BROWN;
                }
            case GREY:
                switch (side2) {
                    case TRANSPARENT:
                        return LIGHT_BLUE;
                    case WHITE:
                        return RED;
                    case BLACK:
                        return LIGHT_GREEN;
                    case GREY:
                        return BEIGE;
                }
        }
        return null;
    }

}
