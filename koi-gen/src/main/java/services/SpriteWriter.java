package services;

import entities.Constants;
import enums.CharLocation;
import enums.CharSide;
import enums.EditorColor;
import enums.GameColor;
import images.MainSprite;
import images.PixelLine;
import images.SideSprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteWriter {

    static int code = Integer.parseInt("40",16);

    public void writeMainSprites(int offset, List<BufferedImage> images, byte[] data) {
        code = Integer.parseInt("40",16);
        count = 0;
        List<MainSprite> sprites = new ArrayList<>();
        MainSprite mainSprite = new MainSprite(offset);
        for (BufferedImage image : images) {
            System.out.println("Sprite "+Integer.toHexString(code++)+" "+Integer.toHexString(offset));
            if (mainSprite.isFull()) {
                sprites.add(mainSprite);
                offset += Integer.parseInt("20", 16);
                /*while (Constants.SKIPPED_CHARACTER_CODE.contains(code)) {
                    offset += Integer.parseInt("20", 16);
                    code++;
                }*/
                System.out.println(count+" "+Integer.toHexString(offset));
                count++;
                mainSprite = new MainSprite(offset);
            }
            CharLocation nextLocation = mainSprite.getNextLocation();
            CharSide nextSide = mainSprite.getNextSide();
            mainSprite.putImage(image, nextLocation, nextSide);
        }
        if (!sprites.contains(mainSprite)) {
            sprites.add(mainSprite);
            System.out.println("Sprite "+Integer.toHexString(code++)+" "+Integer.toHexString(offset));
        }
        for (MainSprite sprite : sprites) {
            BufferedImage imageSide2 = sprite.getImage(CharSide.TWO);
            BufferedImage imageSide1 = sprite.getImage(CharSide.ONE);

            writeImagesAtOffset(imageSide1, imageSide2, sprite.getOffset(), data);
        }

    }

    static int count = 1;

    public void writeSideSprites(int offset, List<BufferedImage> images, byte[] data) {
        code = Integer.parseInt("40",16);
        count = 0;
        List<SideSprite> sprites = new ArrayList<>();
        SideSprite sprite = new SideSprite(offset);
        for (BufferedImage image : images) {
            if (sprite.isFull()) {
                sprites.add(sprite);
                offset += Integer.parseInt("20", 16);
                /*while (Constants.SKIPPED_CHARACTER_CODE.contains(code)) {
                    offset += Integer.parseInt("20", 16);
                    code++;
                }*/
                count++;
                sprite = new SideSprite(offset);
            }
            CharLocation nextLocation = sprite.getNextLocation();
            CharSide nextSide = sprite.getNextSide();
            sprite.putImage(image, nextLocation, nextSide);
        }
        if (!sprites.contains(sprite)) sprites.add(sprite);
        for (SideSprite sideSprite : sprites) {
            BufferedImage imageSide2 = sideSprite.getImage(CharSide.TWO);
            BufferedImage imageSide1 = sideSprite.getImage(CharSide.ONE);

            writeImagesAtOffset(imageSide1, imageSide2, sideSprite.getOffset(), data);
        }

    }

    public static byte[] writeImagesAtOffset(BufferedImage imageSide1, BufferedImage imageSide2, int offset, byte[] data) {
        
        PixelLine[] lines = new PixelLine[8];
        for (int y = 0; y < imageSide1.getHeight(); y++) {
            PixelLine pixelLine = new PixelLine();
            for (int x = 0; x < imageSide1.getWidth(); x++) {
                int b1 = imageSide1.getRGB(x, y);
                int b2 = imageSide2.getRGB(x, y);
                GameColor color1 = GameColor.getGameColor(b1);
                GameColor color2 = GameColor.getGameColor(b2);

                EditorColor editorColor = EditorColor.getEditorColor(color1, color2);

                pixelLine.setPixelColor(x, editorColor);
            }
            lines[y] = pixelLine;
        }
        int cursor = offset;
        for (PixelLine pixelLine:lines) {
            data[cursor] = pixelLine.getByte(0);
            data[cursor+1] = pixelLine.getByte(1);
            data[cursor+16] = pixelLine.getByte(2);
            data[cursor+17] = pixelLine.getByte(3);
            cursor = cursor + 2;
        }
        return data;
    }

    public void writeLatinCharacterSprites(List<BufferedImage> alphabetImages, byte[] data) {
        System.out.println("Main Upper");
        writeMainSprites(Constants.OFFSET_UPPERCASE_MAINSPRITES, alphabetImages, data);
        List<BufferedImage> readAlphabetSideImages = DataReader.readAlphabetSideImages("uppercase/uppercase", Constants.COUNT_UPPERCASE);
        System.out.println("Side Upper");
        writeMainSprites(Constants.OFFSET_UPPERCASE_SIDESPRITES, readAlphabetSideImages, data);

        alphabetImages = DataReader.readAlphabetMainImages("lowercase/lowercase", Constants.COUNT_LOWERCASE);
        System.out.println("Main Lower");
        writeMainSprites(Constants.OFFSET_LOWERCASE_MAINSPRITES, alphabetImages, data);
        readAlphabetSideImages = DataReader.readAlphabetSideImages("lowercase/lowercase", Constants.COUNT_LOWERCASE);
        System.out.println("Side Lower");
        writeMainSprites(Constants.OFFSET_LOWERCASE_SIDESPRITES, readAlphabetSideImages, data);

        alphabetImages = DataReader.readAlphabetMainImages("digits/lowercase", Constants.COUNT_DIGITS);
        System.out.println("Main Digits");
        writeMainSprites(Constants.OFFSET_DIGITS_MAINSPRITES, alphabetImages, data);
        //readAlphabetSideImages = DataReader.readAlphabetSideImages("digits/lowercase", Constants.COUNT_DIGITS);
        System.out.println("Side Digits");
        //writeMainSprites(Constants.OFFSET_DIGITS_SIDESPRITES, readAlphabetSideImages, data);

        /*alphabetImages = DataReader.readAlphabetMainImages("status/status", 1);
        System.out.println("Main Digits");
        writeMainSprites(Constants.OFFSET_STATUS_MAINSPRITES, alphabetImages, data);
        //readAlphabetSideImages = DataReader.readAlphabetSideImages("digits/lowercase", Constants.COUNT_DIGITS);
        System.out.println("Side Digits");*/

    }
}
