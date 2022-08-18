package images;

import enums.CharLocation;
import enums.CharSide;
import services.Utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents two 8x8 images encoded on top of each other
 */
public class SideSprite {

    int offset;
    Map<CharSide, BufferedImage> topImages;
    Map<CharSide, BufferedImage> botImages;

    public SideSprite(int offset) {
        this.offset = offset;
        topImages = new HashMap<>();
        botImages = new HashMap<>();
    }

    public int getOffset() {
        return offset;
    }

    public boolean isFull() {
        return topImages.size()==2 && botImages.size() == 1;
    }

    public CharSide getNextSide() {
        if (isFull()) return null;
        else if (topImages.containsKey(CharSide.TWO) && botImages.containsKey(CharSide.TWO)) return CharSide.ONE;
        else return CharSide.TWO;
    }

    public CharLocation getNextLocation() {
        if (isFull()) return null;
        else if (!topImages.containsKey(CharSide.TWO)) return CharLocation.TOP;
        else if (!botImages.containsKey(CharSide.TWO)) return CharLocation.BOTTOM;
        else if (!topImages.containsKey(CharSide.ONE)) return CharLocation.TOP;
        else return CharLocation.BOTTOM;
    }

    public void putImage(BufferedImage image, CharLocation location, CharSide side) {
        if (location==CharLocation.TOP) {
            topImages.put(side, image);
        }
        else {
            botImages.put(side, image);
        }
    }

    public BufferedImage getImage(CharSide side) {
        BufferedImage topImage = topImages.get(side);
        if (topImage==null) topImage = Utils.createRedImage(4,4);
        BufferedImage botImage = botImages.get(side);
        if (botImage==null) botImage = Utils.createRedImage(4,4);
        return Utils.concatImages(topImage, botImage);
    }
}
