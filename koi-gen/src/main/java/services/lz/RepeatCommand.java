package services.lz;

import services.Utils;

import static services.Utils.x;

public class RepeatCommand extends Command{
    
    int shift;
    int length;

    public RepeatCommand(int shift, int length) {
        this.shift = shift;
        this.length = length;
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[2];
        byte a = (byte) ((shift >>> 8) & 0x0F);
        a = (byte) (a | ((length-3 & 0xFF) * 0x10));
        byte b = (byte) (shift & 0xFF);
        bytes[0] = b;
        bytes[1] = a;
        return bytes;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "RepeatCommand{" +
                "shift=" + shift +
                ", length=" + length +
                ", bytes=" + Utils.bytesToHex(getBytes()) +
                '}';
    }
}
