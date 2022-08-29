package services.text;

import entities.Constants;
import entities.Translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Translator {

    // pointerValue, English
    Map<Integer, Translation> translationMap = new HashMap<>();

    boolean replaceMissing = false;
    
    public Collection<Translation> getTranslations() {
        return translationMap.values();
    }

    public void loadTranslationFile(String name) {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Translation t = new Translation();
        while (line != null) {
            if (line.contains(Constants.TRANSLATION_KEY_VALUE_SEPARATOR)) {
                String[] split = line.split(Constants.TRANSLATION_KEY_VALUE_SEPARATOR);
                if (split.length>0) {
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSETDATA)) {
                        t.setOffsetData(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSET)) {
                        t.setOffset(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_VALUE)) {
                        t.setValue(split[1]);
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_ENG)) {
                        if (split.length>1) {
                            t.setTranslation(split[1]);
                        } else {
                            t.setTranslation("");
                        }
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_JPN)) {
                        if (split.length>1) {
                            t.setJapanese(split[1]);
                        }
                    }
                }
            } else {
                if (t.getTranslation() != null && !t.getTranslation().trim().isEmpty()) {
                    translationMap.put(Integer.parseInt(t.getValue(),16), t);
                }
                else {
                    System.out.println("MISSING TRANSLATIONS : "+Integer.toHexString(t.getOffset()));
                }
                t = new Translation();
            }
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Translation getTranslation(int value) {
        return translationMap.get(value);
    }

    public String getEnglish(int value) {
        Translation translation = translationMap.get(value);
        if (translation==null && replaceMissing) {
            System.out.println("Missing Translation "+Integer.toHexString(value)+"{EL}");
            return Integer.toHexString(value)+"{EL}";
        }
        if (translation!=null) return translation.getTranslation();
        return null;
    }

    public String[] getEnglishCodes(String english, Map<String, String> mapCodeEnglish) {
        List<String> res = new ArrayList<>();
        String specialString = "";
        boolean special = false;
        for (char c:english.toCharArray()) {
            String val = c + "";
            if (c=='{') {
                special = true;
            }
            if (special) {
                specialString += c;
            }
            if (c=='}') {
                special = false;
                val = specialString;
                specialString = "";
            }
            if (!special) {
                String code = mapCodeEnglish.get(val);
                if (code == null) {
                    if (val.startsWith("{")) {
                        val = val.replace("{","");
                        val = val.replace("}","");
                        res.add(val);
                    }
                    else res.add("{" + val + "}");
                } else {
                    res.add(code);
                }
            }
        }
        return res.toArray(new String[0]);
    }

    public String[] getEnglishCodes(List<String> splitEnglish, Map<String, String> mapEnglishCode) {
        List<String> res = new ArrayList<>();
        String specialString = "";
        boolean special = false;

        for (String english : splitEnglish) {
            String code = mapEnglishCode.get(english);
            if (code==null) {
                code = "cd";
                if (english.startsWith("{") && english.endsWith("}")) {
                    code = english.substring(1, english.length()-1);
                }
            }
            res.add(code);
        }
        return res.toArray(new String[0]);
    }

    public List<String> getPrintedEnglish(int value) {
        Translation translation = translationMap.get(value);
        if (translation!=null) return getPrintedEnglish(translation);
        return null;
    }

    private List<String> getPrintedEnglish(Translation translation) {
        List<String> res = new ArrayList<>();
        String text = translation.getTranslation();
        String[] split = text.split("\\{NL}");
        boolean skip = false;
        for (String s:split) {
            String segment = "";
            for (char c : s.toCharArray()) {
                if (c == '{') {
                    skip = true;
                } else if (c == '}') {
                    skip = false;
                } else {
                    if (!skip) segment += c;
                }
            }
            res.add(segment);
        }
        return res;
    }

    public List<String> getAllPrintedEnglish() {
        List<String> res = new ArrayList<>();
        for (Translation value : translationMap.values()) {
            if (value.getTranslation()!=null && !value.getTranslation().isEmpty()) {
                List<String> printedEnglish = getPrintedEnglish(value);
                for (String english : printedEnglish) {
                    if (!res.contains(english))
                        res.add(english);
                }
            }
        }
        return res;
    }
}
