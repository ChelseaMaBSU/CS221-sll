import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array base implementation of an IndexUnsortedList arraylist.
 * 
 * @param <T> the type of elements stored in list
 * @author Chelsea Ma
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private T[] array;
    private int rear;
    private int modCount;

// Constant
    public static final int DEFAULT_CAPACITY = 10;

    /**
     * Initialized list with default capacity
     */
    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Initialized list with given initial capacity
     * 
     * @param initalCapacity size of initial array
     */
@SuppressWarnings(value = "unchecked")
public IUArrayList(int initialCapacity) {  
    array = (T[]) (new Object[initialCapacity]);
    rear = 0;
    modCount = 0;
}

/**
 * Doubles the array capacity if there is no more room to add in the original array
 * 
 */
private void expandIfNecessary() {
    if (rear == array.length) {
        array = Arrays.copyOf(array,array.length * 2);
    }
}

@Override
public void addToFront(T element) {
    expandIfNecessary();

    //shift
    for (int i = rear; i > 0; i--) {
        array[i] = array[i-1];
    }

    // add new element to front fo the array list
    array[0] = element;
    rear++;
    modCount++;
}

@Override
public void addToRear(T element) {
    expandIfNecessary();

    array[rear] = element;
    rear++;
    modCount++;
}

@Override
public void add(T element) {
    addToRear(element);
}

@Override
public void addAfter(T element, T target) {
    int index = indexOf(target);

    if (index == -1) {
        throw new NoSuchElementException();
    }

    expandIfNecessary();

    for (int i = rear; i > index; i--) {
        array[i] = array[i-1];
    }

    array[index + 1] = element;
    rear++;
    modCount++;
}

@Override
public void add(int index, T element) {
    if (index < 0 || index > rear) {
        throw new IndexOutOfBoundsException();
    }

    expandIfNecessary();

    for (int i =rear; i > index; i--) {
        array[i] = array[i-1];
    }

    array[index] = element;
    rear++;
    modCount++;
}

@Override
public T removeFirst() {
    if (isEmpty()) {
        throw new NoSuchElementException();
    }

    T firstElement = array[0];

    // shift values after removed element
    for (int i = 0; i < rear - 1; i++) {
        array[i] = array[i+1];
    }

    rear--;
    array[rear] = null; //delete duplicates in rear
    modCount++;
    return firstElement;
}

@Override
public T removeLast() {
    if (isEmpty()) {
        throw new NoSuchElementException();
    }

    rear--;
    T lastElement = array[rear];
    array[rear] = null;
    modCount++;
    return lastElement;
}

@Override
public T remove(T element) {  
    if (contains(element) == false) {
        throw new NoSuchElementException();
    }

    int index = indexOf(element);

    // shift values after the removed element
    for (int i = index; i < rear -1; i++) {
        array[i] = array[i+1];
    }

    rear--;
    array[rear] = null;
    modCount++;
    return element;
}

@Override
public T remove(int index) {
    if (index < 0 || index >= rear) {
        throw new IndexOutOfBoundsException();
    }

    // value to hold element to remove
    T removedElement = array[index];

    // shift values after removed element
    for (int i = index; i < rear - 1; i++) {
        array[i] = array[i+1];
    }

    rear--;
    array[rear] = null;
    modCount++;
    return removedElement;
}

@Override
public void set(int index, T element) {
    if (index < 0 || index >= rear) {
        throw new IndexOutOfBoundsException();
    }

    array[index] = element;
    modCount++;
}

@Override
public T get(int index) {
    if (index < 0 || index >= rear) {
        throw new IndexOutOfBoundsException();
    }

    return array[index];
}

@Override
public int indexOf(T element) {
    int index = -1;
    for (int i = 0; index < 0 && i < rear; i++) {
        if (element.equals(array[i])) {
            index = i;
        }
    }

    return index;
}

@Override
public T first() {
    if (isEmpty()) {
        throw new NoSuchElementException();
    }
    return array[0];
}

@Override
public T last() {
    if (isEmpty()) {
        throw new NoSuchElementException();
    }
    return array[rear-1];
}

@Override
public boolean contains(T target) {
    return indexOf(target) > -1;
}

@Override
public boolean isEmpty() {
    return rear == 0;
}

@Override
public int size() {
    return rear;
}

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        // for (int i = 0; i < rear; i++) {
        //     str.append(array[i].toString());
        //     str.append(", ");
        // }
        for (T element : this) {
            str.append(element.toString());
            str.append(", ");
        }

        if (!isEmpty()) {
            str.delete(str.length() -2, str.length());
        }

        str.append("]");
        return str.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new ALIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    /**
     * basic iterator for IUArrayList
     */
    private class ALIterator implements Iterator<T> {
        private boolean canRemove;
        private int nextIndex;
        private int iterModCount;


        public ALIterator() {
            nextIndex = 0;
            canRemove = false;
            iterModCount = modCount;
        }

        @Override 
        public boolean hasNext() { 
             if (iterModCount != modCount) { //something happened
                 throw new ConcurrentModificationException();
             }
            return nextIndex < rear;
        }

        @Override
        public T next() {
            if (iterModCount != modCount) { //something happened
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true; //allowed to remove next element
            // T retVal = array[nextIndex];
            nextIndex++;
            // return retVal;
            return array[nextIndex-1];
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) { //something happened
                throw new ConcurrentModificationException();
            }

            if (!canRemove) {
                throw new IllegalStateException();
            }

            canRemove = false; //won't be allowed to remove last element twice
            for (int i = nextIndex-1; i < rear-1; i++) {
                array[i] = array[i+1];
            }

            array[rear-1] = null;
            rear--;
            nextIndex--;
            modCount++; //the list got changed
            iterModCount++; //ONLY this iterator knows about it
        }

    }
}