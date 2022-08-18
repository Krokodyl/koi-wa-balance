package services.lz;

import services.DataWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static services.Utils.h;
import static services.Utils.x;

public class LzDecompressor {

    ByteArrayOutputStream decompressedData = new ByteArrayOutputStream();
    
    final static int REPEAT_BIT = 0x01;
    final static int WRITE_BIT = 0x00;
    
    public void decompressData(byte[] input, int start) {
        List<Command> commands = buildCommands(input, start);
        try {
            processCommands(commands);
            DataWriter.saveData("src/main/resources/gen/"+h(start)+".data", decompressedData.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Command> buildCommands(byte[] input, int start) {
        return buildCommands(input, start, 10);
    }
    
    public List<Command> buildCommands(byte[] input, int start, int commandCount) {
        List<Command> commands = new ArrayList<>();
        int offset = start;
        HeaderCommand headerCommand = new HeaderCommand(input[offset++],input[offset++]);
        int offsetEnd = offset+headerCommand.getLength();
        boolean end = false;
        int count = 0;
        while (!end) {
            FlagCommand flagCommand = new FlagCommand(input[offset++]);
            System.out.println(flagCommand);
            count = 0;
            while (count<8 && count<commandCount) {
                int bit = flagCommand.getBit(count++);
                if (bit==WRITE_BIT) {
                    WriteCommand writeCommand = new WriteCommand(input[offset++]);
                    System.out.println(writeCommand);
                    commands.add(writeCommand);
                }
                else {
                    RepeatCommand repeatCommand = buildRepeatCommand(input[offset++], input[offset++]);
                    System.out.println(repeatCommand);
                    commands.add(repeatCommand);
                }
            }
            if (offset>=offsetEnd || count==commandCount) end = true;
            //end = true;
        }
        if (offset<input.length) {
            int endCommandCount = input[offset++];
            if (endCommandCount > 0) {
                commands.addAll(buildCommands(input, offset, endCommandCount));
            }
        }
        System.out.println(h(offsetEnd));
        System.out.println(h(offset));
        return commands;
    }
    
    public void processCommands(List<Command> commands) throws IOException {
        for (Command command : commands) {
            if (command instanceof WriteCommand) {
                WriteCommand writeCommand = (WriteCommand) command;
                decompressedData.write(writeCommand.getBytes());
            }
            if (command instanceof RepeatCommand) {
                RepeatCommand repeatCommand = (RepeatCommand) command;
                int shift = repeatCommand.getShift();
                int length = repeatCommand.getLength();
                byte[] output = decompressedData.toByteArray();
                int repeatStart = output.length-shift;
                if (repeatStart<0) repeatStart=0;
                int repeatIndex = repeatStart;
                while (length>0) {
                    byte data = output[repeatIndex++];
                    if (repeatIndex>output.length-1) repeatIndex=repeatStart;
                    decompressedData.write(data);
                    length--;
                }
            }
        }
    }
    
    public RepeatCommand buildRepeatCommand(byte a,byte b) {
        int length = ((b & 0xFF) >>> 4) + 3;
        int shift = ((b & 0x0F) * x("100")) + (a & 0xFF);
        RepeatCommand repeatCommand = new RepeatCommand(shift, length);
        return repeatCommand;
    }

    public byte[] getDecompressedData() {
        return decompressedData.toByteArray();
    }
}
