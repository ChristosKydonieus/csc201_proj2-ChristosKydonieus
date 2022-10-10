public class DataCount <T>{
    private T data;
    private int count;

    public DataCount(T data, int count){
        this.data = data;
        this.count = count;
    }

    /** This method is used to get the data
     *
     * @return T data
     */
    public T getData(){
        return data;
    }

    /** This method is used to get the count
     *
     * @return count
     */
    public int getCount(){
        return count;
    }

    /** This method is used to set the data
     */
    public void setData(T newData){
        data = newData;
    }

    /** This method is used to set the count
     */
    public void setCount(int newCount){
        count = newCount;
    }

    /** This method is increase the count by one.
     */
    public void increment(){
        count++;
    }

    /** This overides the to string method to return the data and the count between brackets
     *
     * @return string representation of the data
     */
    @Override
    public String toString(){
        return "[" + data + ": " + count + "]";
    }
}
