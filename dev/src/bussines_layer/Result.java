package bussines_layer;

public class Result<T> {
    //fields
    private boolean result;
    private final T data;
    private String message;

    //Constructor
    public Result(boolean result,T data ,String message) {
        this.result = result;
        this.data=data;
        this.message = message;
    }
    //region Methods
    public boolean isOK(){
        return result;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
    //endregion
}
