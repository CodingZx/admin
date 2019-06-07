package lol.cicco.admin.common.model;

public final class Page {
    private static final int DEFAULT_SIZE = 20;

    private int start;
    private int size;

    public Page(int page, int size) {
        if (page < 1) {
            page = 1;
        }
        this.size = size;
        this.start = (page - 1) * size;
    }

    public Page(int page) {
        if (page < 1) {
            page = 1;
        }
        this.size = DEFAULT_SIZE;
        this.start = (page - 1) * size;
    }

    public int getSize() {
        return size <= 0 ? DEFAULT_SIZE : size;
    }

    public int getStart() {
        if (start < 0) {
            start = 0;
        }
        return start;
    }
}
