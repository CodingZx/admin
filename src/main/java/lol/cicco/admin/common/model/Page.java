package lol.cicco.admin.common.model;

import com.github.pagehelper.PageRowBounds;

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

    public int getSize() {
        return size <= 0 ? DEFAULT_SIZE : size;
    }

    public int getStart() {
        if (start < 0) {
            start = 0;
        }
        return start;
    }

    public PageRowBounds getBounds() {
        return new PageRowBounds(getStart(), getSize());
    }
}
