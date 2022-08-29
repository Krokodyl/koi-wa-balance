package entities;

import java.util.Arrays;
import java.util.List;

import static services.Utils.x;

public class Constants {

    public static List<String> FIRST_BYTE_KANJI = Arrays.asList("01", "02");

    public static String END_OF_LINE_CHARACTER_HEXA = "00";

    public static int COUNT_UPPERCASE = 26;
    public static int OFFSET_UPPERCASE_MAINSPRITES = Integer.parseInt("A8600",16);
    public static int OFFSET_UPPERCASE_SIDESPRITES = Integer.parseInt("ACB00",16);

    public static int COUNT_LOWERCASE = 66;
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

    public static List<Integer> INPUT_VALUES_OFFSETS = Arrays.asList(
            x("2020C"),
            x("2020E"),
            x("20210"),
            x("20212"),
            x("20214"),
            x("20202"),
            x("20204"),
            x("20206"),
            x("20208"),
            x("2020A")
    );
    public static List<Integer> INPUT_VALUES_OFFSETS_KANJI = Arrays.asList(
            x("202A0"),
            x("202A2"),
            x("202A4"),
            x("202A6"),
            x("202A8"),

            x("202AA"),
            x("202AC"),
            x("202AE"),
            x("202B0"),
            x("202B2"),

            x("202B4"),
            x("202B6"),
            x("202B8"),
            x("202BA"),
            x("202BC"),
            
            x("202BE"),
            x("202C0"),
            x("202C2"),
            x("202C4"),
            x("202C6"),

            x("202C8"),
            x("202CA"),
            x("202CC"),
            x("202CE"),
            x("202D0"),

            x("202D2"),
            x("202D4"),
            x("202D6"),
            x("202D8")
    );

}
