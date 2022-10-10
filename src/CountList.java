import java.util.Iterator;
import java.util.LinkedList;

public class CountList <T> implements List <T>, Iterable <T> {
    protected LinkedList <DataCount <T>> list = new LinkedList <DataCount <T>>();

    /** this method returns the size of the list
     *
     * @return the size of the list
     */
    public int size () {
        return list.size ();
    }

    /** this adds a data count object to the list containing the data and the count of the data
     *
     * @param data to add to the list
     */
    public void add(T data ) {
        list.add(new DataCount <T>(data , 1));
    }

    /** this method checks if there are any items in the list
     *
     * @return true if size is zero, false if not
     */
    public boolean isEmpty () {
        return list.size () == 0;
    }

    /**
     * This method searches uses an iterator to go through the list and see if the data being searched for matches the
     * data of a node. If not, returns false. If true, increments the count and then checks if the previous node's
     * count is less than the current nodes. If so, swaps the data and count of the nodes.
     *
     * @param data to be matched to
     * @return true or false if the data is in the list
     */
    public boolean contains (T data) {
        Iterator<DataCount<T>> itr = list.iterator();
        DataCount<T> cur = null;
        DataCount<T> prev = null;
        while (itr.hasNext()){
            if (cur != null){
                prev = cur;
            }
            cur = itr.next();
            if (cur.getData().equals(data)){
                cur.increment();
                if (prev != null){
                    if (cur.getCount() > prev.getCount()){
                        T tempData = cur.getData();
                        int tempCount = cur.getCount();
                        cur.setData(prev.getData());
                        cur.setCount(prev.getCount());
                        prev.setData(tempData);
                        prev.setCount(tempCount);
                    }
                }
                return true;
            }
        }

        return false;
    }

    /**
     * this overrides the toString function to return the list in a readable order
     *
     * @return the list in a readable format
     */
    @Override
    public String toString() {
        String str = "";
        Iterator<DataCount<T>> itr = list.iterator();
        while(itr.hasNext()){
            str += itr.next().toString();
        }

        return str;
    }


    /**
     * This class designs a particular iterator object for the data count class to better handle the data
     * fields of that object
     */
    private class MLIterator implements Iterator<T> {
        private Iterator<DataCount<T>> it = list.iterator();

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public T next() {
            // return the next data item and advance nextNode
            return (it.next()).getData();
        }
    }

    /**
     * This Method returns a MLIterator iterator
     *
     * @return a MLIterator iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new MLIterator();
    }
}