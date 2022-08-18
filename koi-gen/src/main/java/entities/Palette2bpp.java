package entities;

import services.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static entities.FontColor.*;

public class Palette2bpp extends Palette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>();
    
    public Palette2bpp(String file) throws IOException {
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(file)));
        int gap = image.getWidth()/4;
        //System.out.println("Loading Palette "+file);
        for (int index = 0;index<4;index++) {
            int color = image.getRGB(index * gap, 0);
            FontColor fc = null;
            switch (index) {
                case 0:fc = MAP_2BPP_COLOR_01;break;
                case 1:{fc = MAP_2BPP_COLOR_02;break;}
                case 2:{fc = MAP_2BPP_COLOR_03;break;}
                case 3:{fc = MAP_2BPP_COLOR_04;break;}
            }
            String colorAsHex = Utils.getColorAsHex(color);
            //System.out.println(colorAsHex +"\t"+fc.name());
            if (!mapGameColors.containsKey(colorAsHex))
            mapGameColors.put(colorAsHex.toLowerCase(), fc);
        }
    }

    public FontColor getFontColor(int i) {
        if (i==-16777216) return BLACK;
        else if (i==-1) return WHITE;
        else if (i==-16777126) return DARK_BLUE;
        else return DARK_GREY;
    }

    @Override
    public FontColor getFontColor(String hexa) {
        return mapGameColors.get(hexa);
    }

}
