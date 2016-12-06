package org.twinone.util;

/**
 * Created by Android on 4/25/2016.
 */
public class ListViewAudio {

    String name = null;
    boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ListViewAudio(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

}
