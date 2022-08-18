package images;

import enums.CharSide;
import enums.EditorColor;
import enums.GameColor;

import java.awt.image.BufferedImage;

/**
 * Represents an 8x8 image in the tile editor
 */
public class Sprite {

    // 32 bytes
    byte[] data;

    public Sprite(byte[] spriteData) {
        data = spriteData;
    }

    /**
     * from 0 to 7
     */
    public PixelLine getPixelLine(int line) {
        return new PixelLine(data[0+(2*line)],data[0+(2*line)+1],data[0+(2*line)+16],data[0+(2*line)+1+16]);
    }

    /*public BufferedImage getGameImage() {
        BufferedImage image = new BufferedImage(8, 12,
                BufferedImage.TYPE_INT_ARGB);
        for (int row=0;row<4;row++) {
            PixelLine pixelLine = getPixelLine(row + 4);
            for (int col=0;col<8;col++) {
                EditorColor pixelColor = pixelLine.getPixelColor(col);
                image.setRGB(col, row, GameColor.getGameColor(pixelColor, CharSide.TWO).getColor().getRGB());
            }
        }
        for (int row=4;row<12;row++) {
            PixelLine pixelLine = getPixelLine(row-4);
            for (int col=0;col<8;col++) {
                EditorColor pixelColor = pixelLine.getPixelColor(col);
                image.setRGB(col, row, GameColor.getGameColor(pixelColor, CharSide.ONE).getColor().getRGB());
            }
        }
        return image;
    }*/
}
