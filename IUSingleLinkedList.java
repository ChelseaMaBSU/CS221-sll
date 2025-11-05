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
    public void addToRear(T element) {
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> currNode = head;

        //advance through nodes
        while (currNode != tail && !currNode.getElement().equals(target)) {
            currNode = currNode.getNextNode();
        }

        //element not found
        if (!currNode.getElement().equals(target)) {
            throw new NoSuchElementException();
        }

        //create new node
        Node<T> newNode = new Node<T>(element);

        newNode.setNextNode(currNode.getNextNode());
        //connect current node to new node
        currNode.setNextNode(newNode);

        //update tail if new node is added
        if (currNode == tail) {
            tail = newNode;
        }

        size++;
        modCount++;
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
        modCount++;
        return firstNode.getElement(); // or return retVal
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();
        if (size > 1) {
            Node<T> currentNode = head;
            for (int i = 0; i < size - 2; i++) { // iterates n-2 times
                currentNode = currentNode.getNextNode();
            }
            tail = currentNode;
            currentNode.setNextNode(null);
        } else {
            head = tail = null;
        }

        size--;
        modCount++;
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

            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> currentNode = head; // need node in front of the node containing element
            // order of the following condition is important - don't rearrange it
            while (currentNode != tail && !currentNode.getNextNode().getElement().equals(element)) {
                currentNode = currentNode.getNextNode();
            }
            // what if I never found it?
            if (currentNode == tail && !currentNode.getElement().equals(element)) {
                throw new NoSuchElementException();
            }

            retVal = currentNode.getNextNode().getElement();
            
            // general case - middle of a long list is most common location
            currentNode.setNextNode(currentNode.getNextNode().getNextNode());

            if (currentNode.getNextNode() == null) {
                tail = currentNode;
            }
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

        size--;
        modCount++;
        return retVal;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currentNode = head;
        for (int i = 0; i < index - 1; i++) {
            currentNode = currentNode.getNextNode();
        }

        T retVal;

        if (index == 0) {
            retVal = removeFirst();
        } else if (index == size-1) {
            retVal = removeLast();
        } else {
            
            retVal = currentNode.getNextNode().getElement();
            currentNode.setNextNode(currentNode.getNextNode().getNextNode());

            size--;
            
        }

        modCount++;
        return retVal;
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
            modCount++;
            iterModCount++;
        }
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
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currNode = head;

        for (int i = 0; i < index; i++) {
            currNode = currNode.getNextNode();
        }

        // overwrite element at the current node at provided index location
        currNode.setElement(element);
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        //index at zero
        if (index == 0) {
            addToFront(element);
        } else if (index == size) {
            addToRear(element);
        } else {
            Node<T> currNode = head;
            Node<T> newNode = new Node<T>(element);

            //advance through nodes
            for (int i = 0; i < index-1; i++) {
                currNode = currNode.getNextNode();
            }

            newNode.setNextNode(currNode.getNextNode());
            currNode.setNextNode(newNode);

            size++;
            modCount++;
        }
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
    public Iterator<T> iterator() {
        return new SLLIterator();
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