package bussines_layer.employees_module;

import java.util.ArrayList;

public class LazyList<T> extends ArrayList<T> {
    private boolean read;
    public LazyList()
    {
        super();
        read=false;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
