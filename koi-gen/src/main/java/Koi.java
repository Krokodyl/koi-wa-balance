import entities.*;
import services.*;
import services.text.Translator;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Koi {

    static byte[] data;
    static Config config;
    static Translator translator = new Translator();

    public static void main(String[] args) {
        config = JsonLoader.loadConfig();
        data = DataReader.loadData(config.getRomInput());
        List<GameCharacter> japanese = DataReader.loadCharacters("/table-japanese.txt");
        List<GameCharacter> latin = DataReader.loadCharacters("/table-latin.txt");
        translator.loadTranslationFile("translations/table2.txt");
        List<PointerTable> pointerTables = JsonLoader.loadTables();
        List<BufferedImage> alphabetImages = DataReader.readAlphabetMainImages("uppercase/uppercase", Constants.COUNT_UPPERCASE);
        SpriteWriter spriteWriter = new SpriteWriter();
        ImageReader imageReader = new ImageReader();
        
        //data = fillData(data);
        //Map<String, String> mapCodeJapanese = japanese.stream().collect(Collectors.toMap(GameCharacter::getCode, GameCharacter::getValue));
        

        
        for (PointerTable table : pointerTables) {
            DataReader.loadPointerTable(table, data);
        }
        
        DataReader.readPointerTables(pointerTables, translator, latin);

        
        for (PointerTable table : pointerTables) {
            //DataReader.printTable(table, japanese);
            data = DataWriter.writeEnglish(table, data);
        }
        
        spriteWriter.writeLatinCharacterSprites(alphabetImages, data);

        /*DataWriter.fillInputCharacters(Integer.parseInt("20c07",16), Integer.parseInt("40",16), Integer.parseInt("59",16), data, 20);
        DataWriter.fillSameInputCharacter(Integer.parseInt("20c22",16), Integer.parseInt("ff",16), 4, data, 14);
        DataWriter.fillInputCharacters(Integer.parseInt("20c26",16), Integer.parseInt("a0",16), Integer.parseInt("b9",16), data, 10);
        DataWriter.fillSameInputCharacter(Integer.parseInt("20c41",16), Integer.parseInt("ff",16), 4, data, 4);
        DataWriter.fillInputCharacters(Integer.parseInt("20c46",16), Integer.parseInt("ba",16), Integer.parseInt("c3",16), data, 20);
        DataWriter.fillInputCharacters(Integer.parseInt("20c50",16), Integer.parseInt("c4",16), Integer.parseInt("cd",16), data, 10);
*/
        
        
        /*try {
            imageReader.generateSpriteTitleScreen();
            imageReader.generateTileMapTitleScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        DataWriter.writeCodePatches(JsonLoader.loadCodePatches(), data, false);

        DataWriter.saveData(config.getRomOutput(), data);
    }

    private static void printEnglishCodes(String[] strings, Map<String, String> mapEnglishCode) {
        System.out.println();
        for (String string : strings) {
            System.out.println(string);
            String[] englishCodes = translator.getEnglishCodes(string, mapEnglishCode);
            for (String s:englishCodes) System.out.print(s+" ");
            System.out.println();
        }

    }

    private static void lookForCharacters(byte[] data, List<GameCharacter> characters) {
        List<String> exclude = Arrays.asList(
                "01f0", "02f0");
        Map<String, String> mapCodeValue = characters.stream()
                .collect(Collectors.toMap(GameCharacter::getCode, GameCharacter::getValue));
        List<String> ref = characters.stream().map(
                japaneseCharacter -> japaneseCharacter.getCode()).collect(Collectors.toList());
        List<String> codes = new ArrayList<>();
        String code = "";
        for (int k=0;k<data.length;k++) {
            byte b = data[k];
            String hexString = Integer.toHexString(b & 0xFF);
            if (hexString.length()==1) hexString = "0"+hexString;
            if (hexString.equals("01") || hexString.equals("02")) {
                k++;
                byte b2 = data[k];
                String hexString2 = Integer.toHexString(b2 & 0xFF);
                if (hexString2.length()==1) hexString2 = "0"+hexString2;
                hexString += hexString2;
            }
            code = hexString;
            if ((k > Integer.parseInt("214e7",16))) {
                //System.out.println();
            }
            if (ref.contains(code) && !exclude.contains(code)) {
                codes.add(code);
            } else {
                if (codes.size()>5) {
                    System.out.println();
                    System.out.print(Integer.toHexString(k)+"  :  ");
                    for (String s:codes) System.out.print(mapCodeValue.get(s));
                }
                codes.clear();
            }
        }
    }

    private static byte[] fillData(byte[] data) {
        byte[] res = new byte[data.length + Integer.parseInt("10000",16)];
        for (int k=0;k<data.length;k++) {
            res[k] = data[k];
        }
        for (int k=data.length;k<res.length;k++) {
            res[k] = 0;
        }
        /*for (int k=0;k<19000;k++) {
            res[Integer.parseInt("100980",16)+k] = data[Integer.parseInt("20980",16)+k];
        }*/
        return res;
    }

    private static void getCode(String jap, List<GameCharacter> chars) {
        for (char c:jap.toCharArray()) {
            for (GameCharacter ch:chars) {
                if (ch.getValue().equals(""+c)) System.out.print(ch.getCode()+" ");
            }
        }
    }

    private static void printOcr(String s) {
        String[] split = s.split(" ");
        for (int row=0;row<9;row++) {
            for (int k = split.length - 1; k >= 0; k--) {
                System.out.print(split[k].charAt(row));
            }
            System.out.println();
        }
    }

    private static void printRef() {
        InputStream stream = Koi.class.getResourceAsStream("ref.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String[] values = new String[9];
        String[] keys = new String[9];
        try {
            String line = reader.readLine();
            boolean val = true;
            int count = 0;
            int table = 1;
            while (line!=null) {
                if (val) values[count]=line.trim();
                else keys[count]=line.trim();
                count++;
                if (count==9 || (table==8 && count==7)) {
                    if (!val) {
                        int length = 9;
                        if (table==8) length = 7;
                        for (int k=0;k<length;k++) {
                            //System.out.println(keys[k]+"="+values[k]);
                            printKeyValue(keys[k],values[k]);
                        }
                        //System.out.println();
                        table++;
                        val = true;
                        count = 0;
                    }
                    else {
                        val = false;
                        count = 0;
                    }
                }
                line = reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static int bank = 0;
    static String prefix = "";

    private static void printKeyValue(String key, String value) {

        String[] split = key.split(" ");
        for (int k=0;k<split.length;k++) {
            if (split[k].equals("00")) {
                bank++;
                if (bank==1) prefix="01";
                if (bank==2) prefix="02";
            }
            System.out.println(prefix+split[k]+"="+value.charAt(k));
        }
    }
}
