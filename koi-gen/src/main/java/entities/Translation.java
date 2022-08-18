package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Translation {

    private int offset = 0;
    private String value = "";
    private int offsetData = 0;
    private String translation = "";
    private List<String> options = new ArrayList<>();

    public Translation() {
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOffsetData(int offsetData) {
        this.offsetData = offsetData;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void addOption(String s) {
        options.add(s);
    }

    public void addOptions(String[] s) {
        Collections.addAll(options, s);
    }

    public boolean hasOption(String s) {
        return options.contains(s);
    }
}
