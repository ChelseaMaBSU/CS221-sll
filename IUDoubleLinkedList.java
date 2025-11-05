import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node-based implementation of IndexedUnsortedlist.
 * Supports a fully-functional ListIterator in addition to basic iterator.
 * 
 * @author Chelsea Ma
 * 
 */

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
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
        // TODO Auto-generated method stub

    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) { // check index is in bounds
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node<T>(element);
        // if (index == 0) {
        // addToFront(element);
        // newNode.setNextNode(head);
        // if (head != null) {
        // head.setPrevNode(newNode);
        // } else {
        // tail = newNode;
        // }
        // head = newNode;
        // } else if (index == size) {
        // tail.setNextNode(newNode);
        // newNode.setPrevNode(newNode);
        // tail = newNode;
        // size++;
        // modCount++;
        // } else {
        // Node<T> currNode = head;

        // for (int i = 0; i < index-1; i++) {
        // currNode = currNode.getNextNode();
        // newNode.setNextNode(currNode.getNextNode());
        // currNode.setNextNode(newNode);
        // newNode.setPrevNode(currNode);

        // size++;
        // modCount++;
        // }
        // }
        if (isEmpty()) {
            head = tail = newNode;
        } else if (index == 0) {
            newNode.setNextNode(head);
            head.setPrevNode(newNode);
            head = newNode;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNextNode();
            }
            newNode.setPrevNode(current);
            newNode.setNextNode(current.getNextNode());
            current.setNextNode(newNode);
            if (newNode.getNextNode() != null) { // if index != size or current != tail
                newNode.getNextNode().setPrevNode(newNode);
            } else {
                tail = newNode;
            }
        }
        size++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);
        if (tail == null) { // adding to empty list
            tail = newNode;
        } else {
            head.setPrevNode(newNode);
        }
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        // TODO Auto-generated method stub

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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public ListIterator listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T remove(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T removeFirst() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T removeLast() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void set(int index, T element) {
        // TODO Auto-generated method stub

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

}
