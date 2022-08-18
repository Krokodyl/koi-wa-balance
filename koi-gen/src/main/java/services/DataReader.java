package services;

import entities.*;
import images.Sprite;
import services.text.Translator;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataReader {

    public static void readPointerTables(List<PointerTable> pointerTables, Translator translator, List<GameCharacter> latin) {
        Map<String, String> mapEnglishCode = latin.stream()
                .collect(Collectors.toMap(GameCharacter::getValue, GameCharacter::getCode));
        for (PointerTable table : pointerTables) {
            int newDataStart = table.getNewDataStart();
            int newDataShift = table.getNewDataShift();
            int offset = newDataStart;
            Map<Integer, PointerData> mapValuePointer = new HashMap<>();
            for (PointerData pointer:table.getDataJap()) {
                System.out.println("JPN: "+pointer);
            }
            for (PointerData pointer:table.getDataJap()) {
                if (mapValuePointer.containsKey(pointer.getValue())) {
                    PointerData existingPointer = mapValuePointer.get(pointer.getValue());
                    String english = translator.getEnglish(pointer.getValue());
                    if (english!=null) {
                    /*while (english.length()<pointer.getDataLength()) {
                        english = "0"+english;
                    }*/
                        //String printedEnglish = translator.getPrintedEnglish(pointer.getValue());
                        String[] englishCodes = translator.getEnglishCodes(Utils.splitInPairs(english.toLowerCase()), mapEnglishCode);
                        PointerData englishPointer = new PointerData();
                        englishPointer.setData(englishCodes);
                        //englishPointer.setData(pointer.getData());
                        englishPointer.setOffsetData(existingPointer.getOffsetData());
                        englishPointer.setOffset(pointer.getOffset());
                        englishPointer.setValue(existingPointer.getValue());
                        table.addPointerDataEng(englishPointer);
                    }
                }
                else {
                    String english = translator.getEnglish(pointer.getValue());
                    if (english!=null) {
                    /*while (english.length()<pointer.getDataLength()) {
                        english = "0"+english;
                    }*/
                        String[] englishCodes = translator.getEnglishCodes(english, mapEnglishCode);
                        //String[] englishCodes = translator.getEnglishCodes(Utils.splitInPairs(english.toLowerCase()), mapEnglishCode);
                        Translation translation = translator.getTranslation(pointer.getValue());
                        PointerData englishPointer = new PointerData();
                        englishPointer.setData(englishCodes);
                        englishPointer.setOffset(pointer.getOffset());
                        /*if (translation.hasOption(Constants.TRANSLATION_KEY_OPTIONS_KEEP_VALUE)) {
                            englishPointer.setOffsetData(pointer.getOffsetData());
                            englishPointer.setValue(pointer.getValue());
                        }
                        else {*/
                        englishPointer.setOffsetData(offset);
                        englishPointer.setValue(offset - newDataShift);
                        //}
                        table.addPointerDataEng(englishPointer);
                        mapValuePointer.put(pointer.getValue(), englishPointer);
                        offset += englishPointer.getDataLength();
                        //pointer.setNewPointer(englishPointer);
                        System.out.println("Old "+pointer);
                        System.out.println("New "+englishPointer);
                    }
                }
            }
        }
    }
    
    public static Sprite readSprite(byte[] data, int offset) {
        byte[] spriteData = new byte[32];
        for (int k=0;k<32;k++) {
            spriteData[k] = data[offset+k];
        }
        return new Sprite(spriteData);
    }

    public static PointerTable loadPointerTable(PointerTable table, byte[] data) {
        for (PointerRange range : table.getRanges()) {
            for (int i = range.getStart(); i < range.getEnd(); i = i + 2) {
                PointerData p = new PointerData();
                int a = (data[i] & 0xFF);
                int b = (data[i + 1] & 0xFF);
                int c = b * 256 + a;
                p.setValue(c);
                p.setOffset(i);
                String[] readData = readPointerData(c + range.getShift(), data);
                p.setData(readData);
                p.setOffsetData(c + range.getShift());
                table.addPointerDataJap(p);
            }
        }
        return table;
    }

    private static String[] readPointerData(int offset, byte[] data) {
        boolean end = false;
        List<String> res = new ArrayList<String>();
        int i = offset;
        while (!end) {
            int a = (data[i] & 0xFF);
            String s = Integer.toHexString(a);
            if (Integer.toHexString(offset).equals("20cec")) {
                System.out.println();
            }
            s = Utils.padLeft(s, '0', 2);
            if (Constants.FIRST_BYTE_KANJI.contains(s)) {
                int b = (data[i + 1] & 0xFF);
                s = s + Utils.padLeft(Integer.toHexString(b), '0', 2);
                i = i + 2;
            }
            else {
                i = i + 1;
            }
            if (s.equals(Constants.END_OF_LINE_CHARACTER_HEXA)) {
                end = true;
            }
            res.add(s);
        }
        return res.toArray(new String[0]);
    }

    public static List<GameCharacter> loadCharacters(String name) {
        List<GameCharacter> characters = new ArrayList<>();
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(DataReader.class.getResourceAsStream(name),"UTF-8"));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                characters.add(new GameCharacter(key,value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        characters.add(new GameCharacter("ff"," "));
        return characters;
    }

    public static byte[] loadData(String romInput) {
        try {
            return Files.readAllBytes(new File(romInput).toPath());
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<BufferedImage> readAlphabetMainImages(String collectionName, int collectionCount) {
        List<BufferedImage> images = new ArrayList<>();
        for (int i=1;i<=collectionCount;i++) {
            String file = "images/"+collectionName+i+".png";
            images.add(Utils.loadSubImage(file,0,0, 8,4));
            images.add(Utils.loadSubImage(file,0,4, 8,4));
            images.add(Utils.loadSubImage(file,0,8, 8,4));
        }
        return images;
    }

    public static List<BufferedImage> readAlphabetSideImages(String collectionName, int collectionCount) {
        List<BufferedImage> images = new ArrayList<>();
        for (int i=1;i<=collectionCount;i=i+2) {
            String file = "images/"+collectionName+i+".png";
            String file2 = "images/"+collectionName+(i+1)+".png";

            images.add(Utils.concatImagesSide(Utils.loadSubImage(file,8,0, 4,4), Utils.loadSubImage(file2,8,0, 4,4)));
            images.add(Utils.concatImagesSide(Utils.loadSubImage(file,8,4, 4,4), Utils.loadSubImage(file2,8,4, 4,4)));
            images.add(Utils.concatImagesSide(Utils.loadSubImage(file,8,8, 4,4), Utils.loadSubImage(file2,8,8, 4,4)));


        }
        return images;
    }

    public static void printUnknownCharactersTable(PointerTable table, List<GameCharacter> japanese) {
        Map<String, String> mapCodeValue = japanese.stream()
                .collect(Collectors.toMap(GameCharacter::getCode, GameCharacter::getValue));
        List<String> unknownChars = new ArrayList<>();
        for (PointerData pointerData : table.getDataJap()) {
            for (String s : pointerData.getData()) {
                String japaneseChar = mapCodeValue.get(s);
                if (japaneseChar==null) {
                    japaneseChar = "{"+s+"}";
                    if (!unknownChars.contains(japaneseChar)) {
                        unknownChars.add(japaneseChar);
                    }
                }
            }
        }
        for (String s : unknownChars) {
            System.out.println("Unknown : "+s);
        }

    }

    public static void printTable(PointerTable table, List<GameCharacter> japanese) {
        Map<String, String> mapCodeValue = japanese.stream()
                .collect(Collectors.toMap(GameCharacter::getCode, GameCharacter::getValue));
        List<Integer> uniquePointer = new ArrayList<>();
        for (PointerData pointerData : table.getDataJap()) {
            boolean first = false;
            if (!uniquePointer.contains(pointerData.getValue())) {
                uniquePointer.add(pointerData.getValue());
                first = true;
            }

            String data = "";
            for (String s : pointerData.getData()) {
                data += s+" ";
            }
            String jap = "";
            for (String s : pointerData.getData()) {
                String japaneseChar = mapCodeValue.get(s);
                if (japaneseChar==null) japaneseChar = "{"+s+"}";
                jap += japaneseChar;
            }

            if (first) {
                String value = Utils.padLeft(Integer.toHexString(pointerData.getValue()), '0', 4);
                System.out.println("OFFSET="+Integer.toHexString(pointerData.getOffset()));
                System.out.println("VALUE="+ value);
                System.out.println("OFFSETDATA="+Integer.toHexString(pointerData.getOffsetData()));
                System.out.println("DATA="+data);
                System.out.println("JAP="+jap);
                System.out.println("ENG="+ value + "{EL}");
                System.out.println();
            }
        }

    }

    public static List<String> getListEnglishPairs() {
        List<String> res = new ArrayList<>();
        String name = "english-pairs-frequencies.txt";
        Map<Character, int[]> freq = new HashMap<>();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        try {
            String line = br.readLine();
            line = br.readLine();
            while (line != null) {
                String[] split = line.split(" ");
                char c = line.charAt(0);
                for (int k = 1; k < split.length; k++) {
                    if (!split[k].equals("0")) {
                        //System.out.println(c + ""+ (char)(k-1+'a'));
                        res.add(c + ""+ (char)(k-1+'a'));
                    }
                }
                line = br.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }



}
