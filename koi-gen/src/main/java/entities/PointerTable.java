package entities;

import java.util.ArrayList;
import java.util.List;

public class PointerTable {

    private int id;
    List<PointerRange> ranges = new ArrayList<>();
    List<PointerData> dataJap = new ArrayList<>();
    List<PointerData> dataEng = new ArrayList<>();
    private int newDataStart;
    private int newDataShift;

    public PointerTable(int id) {
        this.id = id;
    }

    public void addPointerRange(PointerRange range) {
        ranges.add(range);
    }

    public void addPointerDataJap(PointerData data) {
        this.dataJap.add(data);
    }

    public void addPointerDataEng(PointerData data) {
        this.dataEng.add(data);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewDataStart() {
        return newDataStart;
    }

    public void setNewDataStart(int newDataStart) {
        this.newDataStart = newDataStart;
    }

    public int getNewDataShift() {
        return newDataShift;
    }

    public void setNewDataShift(int newDataShift) {
        this.newDataShift = newDataShift;
    }

    public List<PointerRange> getRanges() {
        return ranges;
    }

    public List<PointerData> getDataJap() {
        return dataJap;
    }

    public List<PointerData> getDataEng() {
        return dataEng;
    }
}
