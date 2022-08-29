package services;

import entities.CodePatch;
import entities.Constants;
import entities.PointerData;
import entities.PointerTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static services.Utils.x;

public class DataWriter {

    public static void saveData(String romOutput, byte[] data) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(romOutput));
            stream.write(data);
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void fillInputCharacters(int offset, int firstChar, int lastChar, byte[] data, int nextZero) {
        int count = 0;
        for (int k=firstChar;k<=lastChar;k++) {
            data[offset+count] = (byte)k;
            count++;
            if (count%nextZero==0) {
                data[offset+count] = 0;
                count++;
                nextZero += 20;
            }
        }
    }

    public static void fillSameInputCharacter(int offset, int charValue, int charCount, byte[] data, int nextZero) {
        int count = 0;
        for (int k=0;k<=charCount;k++) {
            data[offset++] = (byte)charValue;
            count++;
            if (count%nextZero==0) {
                data[offset++] = 0;
                count++;
                nextZero += 20;
            }
        }
    }

    public static byte[] writeCodePatches(List<CodePatch> patchList, byte[] data, boolean debug) {
        for (CodePatch cp:patchList) {
            if (cp.isDebug()==debug)
                cp.writePatch(data);
        }
        return data;
    }

    public static byte[] writeEnglish(PointerTable table, byte[] data) {
        for (PointerData p : table.getDataJap()) {
            //System.out.println(p);
            //System.out.println(p.getNewPointer());
        }

        for (PointerData p : table.getDataEng()) {
            //System.out.println(p);
            int inputValues = 0;
            for (Integer offset : p.getOffsets()) {
                if (Constants.INPUT_VALUES_OFFSETS.contains(offset)) inputValues = 60;
                if (Constants.INPUT_VALUES_OFFSETS_KANJI.contains(offset)) inputValues = 120;
                data[offset] = (byte) ((p.getValue() % 256) & 0xFF);
                data[offset + 1] = (byte) (p.getValue() / 256);
            }
            int offsetData = p.getOffsetData();
            if (inputValues>0) {
                int inputOffsetData = offsetData + x("30000");
                String codes = Arrays.stream(p.getData()).collect(Collectors.joining(" "));
                codes = codes.replaceAll("ff ff","ff");
                codes = codes.replaceAll("00","").trim();
                while (codes.length()+1<inputValues) {
                    codes+=" ff";
                }
                for (String s : codes.split(" ")) {
                    if (s.length()==4) {
                        int a = Integer.parseInt(s.substring(0, 2), 16);
                        int b = Integer.parseInt(s.substring(2, 4), 16);
                        data[inputOffsetData] = (byte) a;
                        data[inputOffsetData + 1] = (byte) b;
                        inputOffsetData += 2;
                    }
                    if (s.length()==2) {
                        int a = Integer.parseInt(s.substring(0, 2), 16);
                        data[inputOffsetData] = (byte) a;
                        inputOffsetData += 1;
                    }
                }
                System.out.printf("#%s# %d\n",codes,codes.length());
            }
            for (String s : p.getData()) {
                if (s.length()==4) {
                    int a = Integer.parseInt(s.substring(0, 2), 16);
                    int b = Integer.parseInt(s.substring(2, 4), 16);
                    data[offsetData] = (byte) a;
                    data[offsetData + 1] = (byte) b;
                    offsetData += 2;
                }
                if (s.length()==2) {
                    int a = Integer.parseInt(s.substring(0, 2), 16);
                    data[offsetData] = (byte) a;
                    offsetData += 1;
                }
            }
        }
        return data;
    }

}
