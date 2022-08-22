package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static services.Utils.h;
import static services.Utils.h4;

public class PointerData {

    private int value;
    //private int offset;
    List<Integer> offsets = new ArrayList<>();
    private String[] data;
    private int offsetData;
    private PointerData oldPointer;
    private String[] menuData;
    private int offsetMenuData;

    public void addOffset(int offset) {
        offsets.add(offset);
    }

    public void addAll(List<Integer> offsets) {
        this.offsets.addAll(offsets);
    }

    public boolean containsOffset(int offset) {
        return offsets.contains(offset);
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void setValue(int value) {
        if (this.value>0) System.out.println(this.value+"  ->  "+value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }

    public void setOffsetData(int offsetData) {

        this.offsetData = offsetData;
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOldPointer(PointerData oldPointer) {
        this.oldPointer = oldPointer;
    }

    public PointerData getOldPointer() {
        return oldPointer;
    }

    public void setMenuData(String[] menuData) {
        this.menuData = menuData;
    }

    public String[] getMenuData() {
        return menuData;
    }

    public void setOffsetMenuData(int offsetMenuData) {
        this.offsetMenuData = offsetMenuData;
    }

    public int getOffsetMenuData() {
        return offsetMenuData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointerData that = (PointerData) o;
        return offsetData == that.offsetData;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offsetData);
    }

    @Override
    public String toString() {
        String s = "";
        for (String a:data) s = s+a+" ";
        String t = "";
        if (menuData!=null)
            for (String a:menuData) t = t+a+" ";

        return "Pointer{" + "offsets=" + Arrays.toString(offsets.stream().map(integer -> h(integer)).collect(Collectors.toList()).toArray()) + ", value=" + h4(value) + ", offsetdata="+h(offsetData)+ ", offsetMenudata="+Integer.toHexString(offsetMenuData)+", menudata=" + t + ", data=" + s + '}';
    }


    public int getDataLength() {
        int length = 0;
        for (String s : data) {
            if (s.length()==2) length++;
            if (s.length()==4) length+=2;
        }
        return length;
    }
}
