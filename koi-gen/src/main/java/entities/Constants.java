package entities;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static List<String> FIRST_BYTE_KANJI = Arrays.asList("01", "02");

    public static String END_OF_LINE_CHARACTER_HEXA = "00";

    public static int COUNT_UPPERCASE = 26;
    public static int OFFSET_UPPERCASE_MAINSPRITES = Integer.parseInt("A8600",16);
    public static int OFFSET_UPPERCASE_SIDESPRITES = Integer.parseInt("ACB00",16);

    public static int COUNT_LOWERCASE = 63;
    public static int OFFSET_LOWERCASE_MAINSPRITES = Integer.parseInt("A8600",16) + Integer.parseInt("900",16);
    public static int OFFSET_LOWERCASE_SIDESPRITES = Integer.parseInt("ACB00",16) + Integer.parseInt("480",16);

    public static int COUNT_DIGITS = 11;
    public static int OFFSET_DIGITS_MAINSPRITES = OFFSET_UPPERCASE_MAINSPRITES + Integer.parseInt("4B0",16);
    public static int OFFSET_DIGITS_SIDESPRITES = OFFSET_UPPERCASE_SIDESPRITES + Integer.parseInt("4B0",16);


    public static int OFFSET_STATUS_MAINSPRITES = Integer.parseInt("40380",16);

    public static int COUNT_NUMBERS = 0;
    public static int OFFSET_NUMBERS_MAINSPRITES = Integer.parseInt("A8600",16) + Integer.parseInt("390",16);
    public static int OFFSET_NUMBERS_SIDESPRITES = Integer.parseInt("ACB00",16) + Integer.parseInt("480",16);

    public static int OFFSET_DOUBLE_MAINSPRITES = Integer.parseInt("A9800",16);
    public static int OFFSET_DOUBLE_SIDESPRITES = Integer.parseInt("AD400",16);

    public static List<Integer> SKIPPED_CHARACTER_CODE = Arrays.asList(Integer.parseInt("60",16), Integer.parseInt("68",16));

    public static String TRANSLATION_KEY_VALUE_SEPARATOR = "=";
    public static String TRANSLATION_KEY_JPN = "JPN";
    public static String TRANSLATION_KEY_ENG = "ENG";
    public static String TRANSLATION_KEY_OFFSETDATA = "OFFSETDATA";
    public static String TRANSLATION_KEY_DATA = "DATA";
    public static String TRANSLATION_KEY_OFFSET = "OFFSET";
    public static String TRANSLATION_KEY_MENUDATA = "MENUDATA";
    public static String TRANSLATION_KEY_VALUE = "VALUE";
    public static String TRANSLATION_KEY_OPTIONS = "OPTIONS";
    public static String TRANSLATION_KEY_OPTIONS_KEEP_VALUE = "KEEP_VALUE";

}
