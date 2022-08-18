package enums;

import java.awt.*;

public enum GameColor {

    TRANSPARENT(new Color(100,0,0,0), Color.red.getRGB()),
    WHITE(Color.white, Color.white.getRGB()),
    BLACK(Color.black, Color.black.getRGB()),
    GREY(Color.darkGray, Color.darkGray.getRGB());

    private Color color;

    private int fileColor;

    GameColor(Color color, int fileColor) {
        this.color = color;
        this.fileColor = fileColor;
    }

    public Color getColor() {
        return color;
    }

    public static GameColor getGameColor(int fileColor) {
        for (GameColor value : values()) {
            if (value.fileColor == fileColor) return value;
        }
        return null;
    }

    /*public static GameColor getGameColor(EditorColor editorColor, CharSide side) {
        switch (side) {
            case ONE: switch (editorColor) {
                case BLACK:
                case DARK_BROWN:
                case LIGHT_BROWN:
                case SAND:
                    return TRANSPARENT;
                case RED:
                case DARK_BLUE:
                case YELLOW:
                case SKY_BLUE:
                    return BLACK;
                case DEEP_BLUE:
                case LIME_GREEN:
                case LIGHT_BLUE:
                case LIGHT_PINK:
                    return GREY;
                case GREY:
                case SALMON:
                case DARK_PINK:
                case SOFT_GREEN:
                    return WHITE;
            }
            case TWO: switch (editorColor) {
                case BLACK:
                case RED:
                case DEEP_BLUE:
                case GREY:
                    return TRANSPARENT;
                case DARK_BROWN:
                case DARK_BLUE:
                case LIME_GREEN:
                case SALMON:
                    return BLACK;
                case LIGHT_BROWN:
                case YELLOW:
                case LIGHT_BLUE:
                case DARK_PINK:
                    return GREY;
                case SAND:
                case SKY_BLUE:
                case LIGHT_PINK:
                case SOFT_GREEN:
                    return WHITE;
            }
        }
        return null;
    }*/
}
