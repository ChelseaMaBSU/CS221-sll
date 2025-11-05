/** 
 * Linkable node for linear data structures
 * @author Chelsea Ma
 */

public class Node<T> {
    private Node<T> nextNode;
    private Node<T> prevNode;
    private T element;

    /**
     * initialize a new Node with the given element
     * @param element
     */
    public Node(T element) {
        this.element = element;
        nextNode = null;
        prevNode = null;
    }

    /**
     * initialize a Node with the given element and next Node
     * @param element
     * @param nextNode
     */
    public Node(T element, Node<T> nextNode) {
        this.element = element;
        this.nextNode = nextNode;
    }
    
    public Node<T> getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    public Node<T> getPrevNode() {
        return nextNode;
    }

    public void setPrevNode(Node<T> prevNode) {
        this.prevNode = prevNode;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return "Element: " + element.toString() + "Has next: " + (nextNode != null);
    }
    
}