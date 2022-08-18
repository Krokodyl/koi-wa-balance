package entities;

public class Config {

    String romInput;
    String romOutput;
    private String tableJapanese;

    public String getRomInput() {
        return romInput;
    }

    public void setRomInput(String romInput) {
        this.romInput = romInput;
    }

    public String getRomOutput() {
        return romOutput;
    }

    public void setRomOutput(String romOutput) {
        this.romOutput = romOutput;
    }

    public void setTableJapanese(String tableJapanese) {
        this.tableJapanese = tableJapanese;
    }

    public String getTableJapanese() {
        return tableJapanese;
    }
}
