package Generator;

public class Limit {
    private int offset;
    private int size;

    public Limit(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }
}
