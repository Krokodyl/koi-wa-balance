package entities;

import java.util.ArrayList;
import java.util.List;

public class Translation {

    private int offset = 0;
    private String value = "";
    private int offsetData = 0;
    private String translation = "";
    private String japanese = "";

    public Translation() {
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOffsetData(int offsetData) {
        this.offsetData = offsetData;
    }

    public int getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public static List<String> test = new ArrayList<>();

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }
}
