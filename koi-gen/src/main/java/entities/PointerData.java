package entities;

public class PointerData {

    private int value;
    private int offset;
    private String[] data;
    private int offsetData;
    private PointerData newPointer;

    public void setValue(int value) {
        if (this.value>0) System.out.println(this.value+"  ->  "+value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
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

    @Override
    public String toString() {
        String s = "";
        if (data!=null) for (String a:data) s = s+a+" ";
        return "Pointeur{" + "offset=" + Integer.toHexString(offset) + ", valeur=" + Integer.toHexString(value) + ", offsetdata="+Integer.toHexString(offsetData)+", data=" + s + '}';
    }

    public PointerData getNewPointer() {
        return newPointer;
    }

    public void setNewPointer(PointerData newPointer) {
        this.newPointer = newPointer;
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
