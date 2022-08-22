package services;

import entities.FontColor;
import entities.Palette;
import entities.Palette2bpp;
import entities.Palette4bpp;
import services.lz.LzCompressor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class ImageReader {

    BufferedImage image;

    byte[] imageData;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public ImageReader() {

    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    public void generateSpriteTitleScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/title-screen.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/title-screen.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/108000.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressedData = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressedData);
    }

    public void generateSpriteScoreScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/sprites/score.png",
                "src/main/resources/gen/sprite-uncompressed.data",
                new Palette4bpp("/palettes/score.png"),
                4
        );
        String uncomp = "src/main/resources/gen/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/118000.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressedData = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressedData);
    }

    public void generateScoreTilemap() throws IOException {
        String uncomp = "src/main/resources/tile-maps/score.data";
        String outputFile = "src/main/resources/data/120000.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressedData = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressedData);
    }

    public void generateTileMapTitleScreen() throws IOException {
        String uncomp = "src/main/resources/tile-maps/title-screen-01.data";
        String outputFile = "src/main/resources/data/110000.data";
        LzCompressor compressor = new LzCompressor();
        byte[] compressedData = compressor.compress(Files.readAllBytes(new File(uncomp).toPath()));
        DataWriter.saveData(outputFile, compressedData);
    }

    public void generateSpriteStatusScreen() throws IOException {
        String s = generateSpriteDataFromImage(
                "src/main/resources/images/status/status1.png",
                "src/main/resources/data/40380.data",
                new Palette2bpp("/palettes/status.png"),
                2
        );
        s = generateSpriteDataFromImage(
                "src/main/resources/images/status/status-down.png",
                "src/main/resources/data/40300.data",
                new Palette2bpp("/palettes/status.png"),
                2
        );
        System.out.println(s);
    }

    private static String generateSpriteDataFromImage(String image, String output, Palette palette, int bpp) throws IOException {
        System.out.println("Generating Sprite Data from image "+image);
        ImageReader fontImageReader = new ImageReader();
        String s = "";
        if (bpp==2) s = fontImageReader.loadFontImage2bpp(image, palette);
        else s = fontImageReader.loadFontImage4bpp(image, palette);
        byte[] bytes = fontImageReader.getBytes();

        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(bytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }

        return s;
    }

    public byte[] loadFontImage2bppSquelched(File file, Palette palette, FontColor fontColor) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //StringBuffer sb = new StringBuffer();
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {

        }
        int width = 0;
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int pixelX = (tileX - 1) * 8 + (x - 1);
                        int rgb = image.getRGB(pixelX, ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        FontColor pixelColor = palette.getFontColor(color);
                        int mask = pixelColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                        if (fontColor==pixelColor) {
                            if (width< pixelX) {
                                width = pixelX;
                            }
                        }
                    }
                    if (stop) break;
                    int leftByte = encodedLine >> 8;
                    int rightByte = encodedLine & 0x00FF;
                    byteArrayOutputStream.write(leftByte);
                    //outputStream.write(rightByte);
                    //String hex = Utils.toHexString(leftByte, 2);
                    //sb.append(hex.replaceAll(" ",""));
                    //System.out.print(Utils.bytesToHex(byteArrayOutputStream.toByteArray()));
                }
                if (stop) break;
                //System.out.println();
            }
            if (stop) break;
        }
        if (file.toString().contains("w8")) width = 8 + 2;
        if (file.toString().contains("w4")) width = 4 + 2;
        byte[] squelch = squelch(byteArrayOutputStream.toByteArray(), width + 2);
        //System.out.println(Utils.bytesToHex(squelch));
        return squelch;
        //return sb.toString();
    }
    
    private byte[] squelch(byte[] bytes, int width) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(width);
        stream.write(0);
        for (int k=0;k<8;k++) {
            stream.write(bytes[k+8]);
            stream.write(bytes[k]);
        }
        for (int k=0;k<7;k++) {
            stream.write(bytes[16+k+8]);
            stream.write(bytes[16+k]);
        }
        return stream.toByteArray();
    }

    public String loadFontImage2bpp(String file, Palette palette) {
        StringBuffer sb = new StringBuffer();
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        FontColor fontColor = palette.getFontColor(color);
                        int mask = fontColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    int leftByte = encodedLine >> 8;
                    int rightByte = encodedLine & 0x00FF;
                    outputStream.write(leftByte);
                    outputStream.write(rightByte);
                    String hex = Utils.toHexString(leftByte, 2)+" "+Utils.toHexString(rightByte,2);
                    sb.append(hex.replaceAll(" ",""));
                    //System.out.print(hex+" ");
                }
                if (stop) break;
                //System.out.println();
            }
            if (stop) break;
        }
        return sb.toString();
    }

    public String loadFontImage4bpp(String file, Palette palette) {
        StringBuffer sb = new StringBuffer();
        byte[] output = new byte[0];
        int indexOutput = 0;
        try {
            image = ImageIO.read(new File(file));
            output = new byte[image.getHeight()*image.getWidth()/2];
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    long encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        FontColor fontColor = palette.getFontColor(color);
                        long mask = fontColor.getLongMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    //int leftByte = encodedLine >> 8;
                    //int rightByte = encodedLine & 0x00FF;
                    long byte1 = encodedLine >> 24;
                    long byte2 = (encodedLine >> 16) & 0x00FF;
                    long byte3 = (encodedLine >> 8) & 0x00FF;
                    long byte4 = (encodedLine) & 0x00FF;

                    output[indexOutput] = (byte) ((byte1) & 0xFF);
                    output[indexOutput+1] = (byte) ((byte2) & 0xFF);
                    output[indexOutput+16] = (byte) ((byte3) & 0xFF);
                    output[indexOutput+17] = (byte) ((byte4) & 0xFF);
                    indexOutput += 2;
                }
                indexOutput += 16;
                if (stop) break;
            }
            if (stop) break;
        }
        int k = 0;
        for (byte b:output) {
            //if (k++%16==0) System.out.println();
            String s = Utils.toHexString(b) + " ";
            sb.append(s);
            outputStream.write(b);
            //System.out.print(s);
        }
        return sb.toString();
    }

    public int getWidth(File file, Palette palette, FontColor fontColor) {
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {

        }
        int width = 0;
        boolean stop = false;
        int tileX, tileY = 0, x, y;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int pixelX = (tileX - 1) * 8 + (x - 1);
                        int rgb = image.getRGB(pixelX, ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        FontColor pixelColor = palette.getFontColor(color);
                        int mask = pixelColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                        if (fontColor==pixelColor) {
                            if (width< pixelX) {
                                width = pixelX;
                            }
                        }
                    }
                    if (stop) break;
                }
                if (stop) break;
            }
            if (stop) break;
        }
        if (file.toString().contains("w8")) width = 8 + 2;
        if (file.toString().contains("w4")) width = 4 + 2;
        return width + 2;
    }

    public static void main(String[] args) {
        
        String s = "*!?:.+-()[]\\/_#$%0123456789~";
        int start = Integer.parseInt("4000",16);
        String pa = "" +
                "    {\n" +
                "      \"value\":\"%s\",\n" +
                "      \"code\":\"%s\"\n" +
                "    },\n";
        for (char c:s.toCharArray()) {
            System.out.printf(pa,c,Integer.toHexString(start));
            start += Integer.parseInt("100",16);
        }
        
        Map<String, BufferedImage> imageMap = new HashMap<>();
        Path path = null;
        try {
            path = Paths.get("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\images\\tmp");
            Files.list(path).forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {

                                BufferedImage image = ImageIO.read(file.toFile());
                                BufferedImage out = new BufferedImage(16, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                                int key = 0;
                                Color white = new Color(255, 255, 255, 255);
                                Color black = new Color(0, 0, 0, 255);
                                for (int x = 0; x < out.getWidth(); x++) {
                                    for (int y = 0; y < out.getHeight(); y++) {
                                        if (x>=8) out.setRGB(x,y,black.getRGB());
                                        else {
                                            int in = image.getRGB(x, y);
                                            if (in==white.getRGB()) out.setRGB(x,y,black.getRGB());
                                            else out.setRGB(x,y,white.getRGB());
                                        }
                                    }
                                }
                                ImageIO.write(out, "png", file.toFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLatin(byte[] data) {
        int start = Integer.parseInt("58020",16);
        final int[] offset = {start};
        Path path = null;
        try {
            path = Paths.get(ClassLoader.getSystemResource("images/latin").toURI());
            Files.list(path).sorted().forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {
                                //System.out.printf("Writing Latin char %s at %s\n",file.toFile().getName(),Integer.toHexString(offset[0]));
                                byte[] bytes = loadFontImage2bppSquelched(file.toFile(), new Palette2bpp("/palettes/palette-font.png"), FontColor.MAP_2BPP_COLOR_02);
                                for (byte b:bytes) {
                                    data[offset[0]++] = b;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            offset[0] = Integer.parseInt("58C00",16);
            path = Paths.get(ClassLoader.getSystemResource("images/syllable").toURI());
            Files.list(path).sorted().forEach(
                    file -> {
                        if (file.toFile().isFile()) {
                            try {
                                //System.out.printf("Writing Latin char %s at %s\n",file.toFile().getName(),Integer.toHexString(offset[0]));
                                byte[] bytes = loadFontImage2bppSquelched(file.toFile(), new Palette2bpp("/palettes/palette-font.png"), FontColor.MAP_2BPP_COLOR_02);
                                for (byte b:bytes) {
                                    data[offset[0]++] = b;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        //imageReader.loadFontImage2bppSquelched("src/main/resources/images/latin/char-000.png", new Palette2bpp("/palettes/palette-latin.png"), FontColor.MAP_2BPP_COLOR_02);

    }
    
    public BufferedImage getSubImage(BufferedImage image, int width) {
        return image.getSubimage(0,0,width,image.getWidth());
    }
    
    public void turnImageIntoTiles(int tileX, String s, String file, String outputTiles, String outputMap) throws IOException {
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(file)));
        ByteArrayOutputStream outputMapBytes = new ByteArrayOutputStream();
        Map<Tile, String> tiles = new HashMap<>();
        BufferedImage out = new BufferedImage(8*16, 320, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) out.getGraphics();
        int code = Integer.parseInt(s,16);
        int tileY = 0;
        for (int y=0;y<image.getHeight();y=y+8) {
            for (int x=0;x<image.getWidth();x=x+8) {
                BufferedImage subimage = image.getSubimage(x, y, 8, 8);
                String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\test\\"+tileX+"-"+tileY+".png";
                ImageIO.write(subimage, "png", new File(folder ));
                Tile tile = new Tile(subimage);
                String hexCode = Utils.toHexString(code, 4);
                if (!tiles.containsKey(tile)) {
                    /*for (Map.Entry<Tile, String> entry : tiles.entrySet()) {
                        boolean b = compareImages(entry.getKey().image, tile.image);
                        System.out.println(b);
                    }*/

                    tiles.put(tile, hexCode);
                    g.drawImage(subimage,tileX,tileY, null);
                    tileX += 8;
                    if (tileX%(8*16)==0) {
                        tileX=0;tileY+=8;
                    }
                    outputMapBytes.write(Utils.codeBytes(hexCode));
                    code += Integer.parseInt("0100",16);
                } else {
                    hexCode = tiles.get(tile);
                    outputMapBytes.write(Utils.codeBytes(hexCode));
                }
            }
        }
        try {
            DataWriter.saveData("D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\"+outputMap, outputMapBytes.toByteArray());
            String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\gen\\"+outputTiles;
            ImageIO.write(out, "png", new File(folder ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class Tile {
        
        BufferedImage image;
        int[] rgbData;

        public Tile(BufferedImage image) {
            this.image = image;
            rgbData = this.image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return Arrays.equals(rgbData, tile.rgbData);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(rgbData);
        }
    }

    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width  = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
    
    public static void extractMenus(byte[] data, int start, int end) throws IOException {
        BufferedImage sprites = ImageIO.read(Objects.requireNonNull(ImageReader.class.getResource("/sprites/move-menu.png")));
        int offset = start;
        while (offset<end) {
            if (data[offset]==Integer.parseInt("0C", 16) && data[offset+16]==Integer.parseInt("0C", 16)) {
                //System.out.println("extractMenus "+Integer.toHexString(offset));
                byte[] bytes = Arrays.copyOfRange(data, offset, offset + Integer.parseInt("20", 16));
                if (bytes[0]==Integer.parseInt("0C", 16) && bytes[16]==Integer.parseInt("0C", 16)) {
                    BufferedImage image = new BufferedImage(6*8, 16, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = (Graphics2D) image.getGraphics();
                    g.setColor(Color.BLACK);
                    g.fillRect(0,0,image.getWidth(),image.getHeight());
                    for (int tileY=0;tileY<2;tileY++) {
                        for (int tileX=0;tileX<6;tileX++) {
                            byte a = (byte) (bytes[2+(tileY*16)+tileX*2] & 0xFF);
                            byte b = (byte) (bytes[2+1+(tileY*16)+tileX*2] & 0xFF);
                            BufferedImage subImage = menuSubImage(sprites, a, b);
                            g.drawImage(subImage,tileX*8,tileY*8, null);
                        }
                    }
                    String folder = "D:\\git\\dokapon-gaiden\\dokapon-gaiden-gen\\src\\main\\resources\\images\\menus";
                    ImageIO.write(image, "png", new File(folder + "/" + offset +" - "+Integer.toHexString(offset) + ".png"));
                    
                }
                offset+=32;
            } else {
                offset++;
            }
        }
    }
    
    public static BufferedImage menuSubImage(BufferedImage sprites, byte a, byte b) {
        int y = 0x80*(b & 0xFF) + 8*((a & 0xFF)/0x10);
        int x = 8*(0x0F & (a & 0xFF));
        return sprites.getSubimage(x, y, 8, 8);
    }
}
