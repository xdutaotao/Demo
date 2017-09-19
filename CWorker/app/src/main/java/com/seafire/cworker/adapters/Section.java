package com.seafire.cworker.adapters;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Section {

    private final String name;
    private final int size;

    public boolean isExpanded;

    public Section(String name, int size) {
        this.name = name;
        this.size = size;
        isExpanded = false;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
}
