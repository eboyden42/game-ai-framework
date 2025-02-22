package org.example.ai;

public class Entry {

    private int value;
    private int depth;
    private String flag;

    public static void main(String[] args) {
        // TODO Auto-generated method

    }

    public Entry(int value, int depth, String flag) {
        setValue(value);
        setDepth(depth);
        setFlag(flag);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
