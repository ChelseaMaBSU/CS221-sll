
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node-based implementation of IndexedUnsortedlist. Supports a
 * fully-functional ListIterator in addition to basic iterator.
 *
 * @param <T> type of elements stored in list
 * @author Chelsea Ma
 *
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

    /** Reference to first node in list */
    private Node<T> head;
    /** Reference to last node in list */
    private Node<T> tail;
    /** Number of elements currently in list */
    private int size;
    /** Modification count for fail-test iterator */
    private int modCount;

    /**
     * Initialize a new empty list
     */
    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void add(T element) {
        addToRear(element);

    }

    @Override
    public void add(int index, T element) {
        // if (index < 0 || index > size) { // check index is in bounds
        //     throw new IndexOutOfBoundsException();
        // }
        // Node<T> newNode = new Node<T>(element);
        // if (isEmpty()) {
        //     head = tail = newNode;
        // } else if (index == 0) {
        //     newNode.setNextNode(head);
        //     head.setPrevNode(newNode);
        //     head = newNode;
        // } else {
        //     Node<T> current = head;
        //     for (int i = 0; i < index - 1; i++) {
        //         current = current.getNextNode();
        //     }
        //     newNode.setPrevNode(current);
        //     newNode.setNextNode(current.getNextNode());
        //     current.setNextNode(newNode);
        //     if (newNode.getNextNode() != null) { // if index != size or current != tail
        //         newNode.getNextNode().setPrevNode(newNode);
        //     } else {
        //         tail = newNode;
        //     }
        // }
        // size++;
        // modCount++;
        ListIterator<T> lit = listIterator(index);
        lit.add(element);
    }

    @Override
    public void addAfter(T element, T target) {
        ListIterator<T> lit = listIterator();
        boolean foundIt = false;
        while (lit.hasNext()) {
            if (lit.next().equals(target)) {
                lit.add(element);
                foundIt = true;
                break;
            } // else, nothing - just need to keep moving
        }
        if (!foundIt) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void addToFront(T element) {
        // Node<T> newNode = new Node<T>(element);
        // newNode.setNextNode(head);
        // if (tail == null) { // adding to empty list
        //     tail = newNode;
        // } else {
        //     head.setPrevNode(newNode);
        // }
        // head = newNode;
        // size++;
        // modCount++;
        ListIterator<T> lit = listIterator();
        lit.add(element);
    }

    @Override
    public void addToRear(T element) {
        //CRITICAL that indexed listIterator starts at the best end
        ListIterator<T> lit = listIterator(size);
        lit.add(element);

    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public T first() {
        if (isEmpty()) { // size == 0; or head == null;
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T get(int index) { // Single-linked impl. can be improved from n/2 to n/4 avg
        // if (index < 0 || index >= size) {
        //     throw new IndexOutOfBoundsException();
        // }
        // Node<T> currentNode = head;
        // for (int i = 0; i < index; i++) {
        //     currentNode = currentNode.getNextNode();
        // }
        // return currentNode.getElement();
        if (index == size) { //only index not caught by ListIterator's constructor
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> lit = listIterator(index);
        return lit.next();
    }

    @Override
    public int indexOf(T element) {
        // int index = 0;
        // Node<T> currentNode = head;
        // while (currentNode != null && !element.equals(currentNode.getElement())) { // either I found it or I ran out of nodes
        //     currentNode = currentNode.getNextNode();
        //     index++;
        // }
        // // if (index >= size()) { //went too far or currentNode == null;
        // if (currentNode == null) { // didn't find it
        //     index = -1;
        // }
        int index = -1;
        int currentIndex = 0;
        ListIterator<T> lit = listIterator();
        while (index < 0 && lit.hasNext()) {
            if (lit.next().equals(element)) {
                index = currentIndex;
            }
            currentIndex++;
        }
        return index;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        ListIterator<T> lit = listIterator();
        boolean found = false;
        T retVal = null;

        while (lit.hasNext() && !found) {
            retVal = lit.next();
            if (retVal.equals(element)) {
                lit.remove();
                found = true;
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }
        return retVal;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        ListIterator<T> lit = listIterator(index);
        T retVal = lit.next();
        lit.remove();
        return retVal;
    }

    @Override
    public T removeFirst() {
        ListIterator<T> lit = listIterator();
        if (!lit.hasNext()) {
            throw new NoSuchElementException();
        }
        T retVal = lit.next();
        lit.remove();
        return retVal;
    }

    @Override
    public T removeLast() {
        // if (isEmpty()) {
        //     throw new NoSuchElementException();
        // }
        // T retVal = tail.getElement();
        // // update tail/move tail
        // tail = tail.getPrevNode();
        // if (size == 1) { // or tail == null
        //     head = null; // removed only element
        // } else {
        //     tail.setNextNode(null);
        // }
        // size--;
        // modCount++;
        // return retVal;
        ListIterator<T> lit = listIterator(size);
        if (!lit.hasPrevious()) {
            throw new NoSuchElementException();
        }
        T retVal = lit.previous();
        lit.remove();
        return retVal;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        ListIterator<T> lit = listIterator(index);
        lit.next();
        lit.set(element);

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        // for (int i = 0; i < rear; i++) {
        // str.append(array[i].toString());
        // str.append(", ");
        // }
        for (T element : this) {
            str.append(element.toString());
            str.append(", ");
        }

        if (!isEmpty()) {
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");
        return str.toString();
    }

    /**
     * ListIterator for IUDOubleLinkedList
     * Supports all optional ListIterator operations
     */
    private class DLLIterator implements ListIterator<T> {

        /** Node that will be returned by next call  */
        private Node<T> nextNode;
        /** Index of the next node */
        private int nextIndex;
        /** Last node returned by next or previous */
        private Node<T> lastReturnedNode;
        /** Iterator modCount for concurrent mod detection */
        private int iterModCount;

        /**
         * Initialize iterator at the beginning of list
         */
        public DLLIterator() {
            nextNode = head;
            nextIndex = 0;
            iterModCount = modCount;
            lastReturnedNode = null;

        }

        /**
         * Initialize iterator before the specified index.
         *
         * @param startingIndex index that would be next after constructor
         */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }

            if (startingIndex > size / 2) { // back half
                if (startingIndex == size) {
                    nextNode = null;
                } else {
                    nextNode = tail;
                    for (int i = size - 1; i > startingIndex; i--) {
                        nextNode = nextNode.getPrevNode();
                    }
                }
            } else { // in first half
                nextNode = head;
                for (int i = 0; i < startingIndex; i++) {
                    nextNode = nextNode.getNextNode();
                }
            }
            nextIndex = startingIndex;
            iterModCount = modCount;
            lastReturnedNode = null;
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
            lastReturnedNode = nextNode;
            nextNode = nextNode.getNextNode();
            nextIndex++;
            return retVal;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            //nextNode.getPrevious() != null or nextIndex != 0 or nextNode != head
            return nextIndex > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (nextNode == null) {
                nextNode = tail;
            } else {
                nextNode = nextNode.getPrevNode();
            }
            lastReturnedNode = nextNode;
            nextIndex--;
            return nextNode.getElement();
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }
            //now can remove lastReturnedNode
            //update tail if removing last node
            if (lastReturnedNode == tail) {
                tail = lastReturnedNode.getPrevNode();
            } else {
                lastReturnedNode.getNextNode().setPrevNode(lastReturnedNode.getPrevNode());
            }
            //update head if removing first node
            if (lastReturnedNode == head) {
                head = lastReturnedNode.getNextNode();
            } else {
                lastReturnedNode.getPrevNode().setNextNode(lastReturnedNode.getNextNode());
            }
            //adjust iterator position
            if (lastReturnedNode == nextNode) { //last move was previous and just removed nextNode
                nextNode = nextNode.getNextNode();
            } else { //last move was next
                nextIndex--;
            }
            lastReturnedNode = null;
            size--;
            modCount++;
            iterModCount++;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }

            lastReturnedNode.setElement(e);
            modCount++;
            iterModCount++;
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            Node<T> newNode = new Node<T>(e);

            if (isEmpty()) {
                head = tail = newNode;
            } else if (nextNode == head) {
                newNode.setNextNode(head);
                head.setPrevNode(newNode);
                head = newNode;
            } else if (nextNode == null) {
                newNode.setPrevNode(tail);
                tail.setNextNode(newNode);
                tail = newNode;
            } else {
                // ✓ FIXED: Set newNode's previous to nextNode's previous
                newNode.setNextNode(nextNode);
                newNode.setPrevNode(nextNode.getPrevNode());  // Fixed line
                nextNode.getPrevNode().setNextNode(newNode);
                nextNode.setPrevNode(newNode);
            }

            nextIndex++;
            size++;
            modCount++;
            iterModCount++;
            lastReturnedNode = null;
        }
    }
}
