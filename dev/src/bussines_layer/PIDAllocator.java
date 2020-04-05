package bussines_layer;

public class PIDAllocator {
    private Integer next_id;

    public PIDAllocator() {
        this.next_id = 1 ;
    }

    public Integer getNext_id() {
        Integer ret= next_id;
        this.next_id++;
        return ret;
    }
}
