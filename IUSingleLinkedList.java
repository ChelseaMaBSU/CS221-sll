import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node-based implementation of IndexedUnsortedList
 * 
 * @author Chelsea Ma
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * initialize empty list
     */
    public IUSingleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);
        head = newNode;
        if (tail == null) { // isEmpty is actually risky here if head == null was the test
            tail = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T elemet) {
        Node<T> newNode = new Node<T>(element);
        if (tail != null) { // or !isEmpty
            tail.setNextNode(newNode);
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {

    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> firstNode = head; // T retVal
        head = head.getNextNode();
        if (head == null) { // just removed the only node form the list
            tail = null; // so tail is also null
        }
        size--;
        modCount--;
        return firstNode.getElement(); // or return retVal
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();
        if (size > 0) {
            Node<T> currentNode = head;
            for (int i = 0; i < size - 1; i++) { // iterates n-2 times
                currentNode = currentNode.getNextNode();
            }
            tail = currentNode;
            currentNode.setNextNode(null);
        } else {
            head = tail = null;
        }

        size--;
        modCount--;
        return retVal;
    }

    @Override
    public T remove(T element) { // MV

        if (isEmpty()) { // check if empty
            throw new NoSuchElementException();
        }

        T retVal;

        // must check head node before general case search
        if (head.getElement().equals(element)) {
            retVal = head.getElement();
            head = head.getNextNode();
        } else {
            Node<T> currentNode = head; // need node in front of the node containing element
            // order of the following condition is important - don't rearrange it
            while (currentNode != null && currentNode != tail
                    && currentNode.getNextNode().getElement().equals(element)) {
                currentNode = currentNode.getNextNode();
            }
            // what if I never found it?
            if (currentNode == tail) {
                throw new NoSuchElementException();
            }

            retVal = currentNode.getNextNode().getElement();
            if (currentNode == tail) {
                tail = currentNode;
            }
            // general case - middle of a long list is most common location
            currentNode.setNextNode(currentNode.getNextNode().getNextNode());

            // //was it the last node?
            // if (currentNode.getNextNode() == null) {
            // tail = currentNode;
            // }
        }

        // if (size > 0) { //remove
        // Node<T> currentNode = head;
        // for (int i = 0; i < size-1; i++) {
        // if (currentNode.getElement().equals(element)) {
        // T retVal = currentNode.getElement();

        // }
        // currentNode = currentNode.getNextNode();
        // }
        // }

        // if (tail.getElement().equals(element)) {
        // throw new NoSuchElementException();
        // }

        // tail = currentNode;
        // size--;
        // return retVal;
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // head == null; tail == null; size() == 0;
    }

    private class SLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private boolean canRemove;

        /**  */
        public SLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            canRemove = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T retVal = nextNode.getElement();
            nextNode = nextNode.getNextNode();
            canRemove = true;
            return retVal;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;

            if (head.getNextNode() == nextNode) {
                head = head.getNextNode();
                if (head == null) { //or size == 1, 
                    tail = null;
                }
            } else {
                // general case - find "previousPreviousNode"
                Node<T> prevPrevNode = head;
                while (prevPrevNode.getNextNode().getNextNode() != nextNode) {
                    prevPrevNode = prevPrevNode.getNextNode();
                }
                prevPrevNode.setNextNode(nextNode);
                // if (prevPrevNode.getNextNode() == null) {
                // }
                if (nextNode == null) {
                    tail = prevPrevNode;
                }
            }
            size--;
            modCount--;
            iterModCount++;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new SLLIterator();
    }

    @Override
    public int indexOf(T element) {
        int index = 0;
        Node<T> currentNode = head;
        while (currentNode != null && !element.equals(currentNode.getElement())) { // either I found it or I ran out of nodes
            currentNode = currentNode.getNextNode();
            index++;
        }
        // if (index >= size()) { //went too far or currentNode == null;
        if (currentNode == null) { // didn't find it
            index = -1;
        }
        return index;
    }

    @Override
    public T first() {
        if (isEmpty()) { // size == 0; or head == null;
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNextNode();
        }
        return currentNode.getElement();
    }

    @Override
    public void set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
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

}