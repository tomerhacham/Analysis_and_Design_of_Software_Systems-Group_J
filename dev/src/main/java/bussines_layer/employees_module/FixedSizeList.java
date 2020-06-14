package bussines_layer.employees_module;


import java.util.ArrayList;
import java.util.function.Predicate;

public class FixedSizeList<T> extends ArrayList<T> {
    private int capacity;

    public FixedSizeList(int capacity) {
        super(capacity);
        this.capacity=capacity;
            }

    public T findAndRemove(Predicate<T> p)
    {
        T searched=null;
        for(T element:this)
        {
            if(p.test(element)) {
                searched = element;
                break;
            }
        }
        if(searched!=null)
            super.remove(searched);
        return searched;
    }

    @Override
    public boolean add(T o) {
       if(!isFull()) {
           super.add(o);
           return true;
       }
       return false;
    }

    @Override
    public void add(int index, T element) {
        if (!isFull())
            super.add(index, element);
    }

    public boolean isFull()
    {
        return size()>=capacity;
    }

    public int capacity() {
        return capacity;
    }
}
