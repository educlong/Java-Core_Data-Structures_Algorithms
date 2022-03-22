package a04; /**
 * @date March 07, 2022
 * @author DUC LONG NGUYEN (Paul)
 * */
/**
 * Generic Linked List class that always keeps the elements in order 
 * @author mark.yendt
 */
public class SortedLinkedList<T extends Comparable>
{
  /**
   * The Node class stores a list element and a reference to the next node.
   */
  private final class Node<T extends Comparable>
  {
    T value;
    Node next;

    /**
     * Constructor.
     * @param val The element to store in the node.
     * @param n The reference to the successor node.
     */
    Node(T val, Node n)
    {
      value = val;
      next = n;
    }

    /**
     * Constructor.
     *
     * @param val The element to store in the node.
     */
    Node(T val)
    {
      // Call the other (sister) constructor.
      this(val, null);
    }
  }
  private Node first;  // list head

  /**
   * Constructor.
   */
  public SortedLinkedList()
  {
    first = null;
  }

  /**
   * The isEmpty method checks to see if the list is empty.
   *
   * @return true if list is empty, false otherwise.
   */
  public boolean isEmpty()
  {
    return first == null;
  }

  /**
   * The size method returns the length of the list.
   * @return The number of elements in the list.
   */
  public int size()
  {
    int count = 0;
    Node p = first;
    while (p != null) {
      // There is an element at p
      count++;
      p = p.next;
    }
    return count;
  }

  /**
   * The add method adds an element to the list in sorted order.
   * @param element The element to add to the list in sorted order.
   */
  public void add(T element)
  {
    Node pNode = new Node(element);
    if (this.first==null) this.first = pNode;
    else {
      Node pInsert = this.first;
      Node pPrevious = null;
      while (pInsert != null) {
        if (pNode.value.compareTo(this.first.value)<0){
          pNode.next=this.first;
          this.first = pNode;
          pPrevious = null;
          pInsert = this.first;
          break;
        }
        if (pPrevious!=null && pPrevious.value.compareTo(pNode.value)<0 && pNode.value.compareTo(pInsert.value)<0) break;
        pPrevious = pInsert;
        pInsert = pInsert.next;
      }
      if (pPrevious!=null){
        pNode.next = pInsert;
        pPrevious.next = pNode;
      }
    }
  }

  /**
   * The remove method removes an element.
   * @param element The element to remove.
   * @return true if the remove succeeded, false otherwise.
   */
  public boolean remove(T element)
  {
    Node pDel = this.first;
    if (pDel==null) return false;
    else {
      Node pPrevious = null;
      while (pDel!=null){
        if (pDel.value.compareTo(element)==0) break;
        pPrevious = pDel;
        pDel = pDel.next;
      }
      if (pDel==null) return false;
      else {
        if (pDel==this.first) this.first = this.first.next;
        else pPrevious.next = pDel.next;
        pDel.next=null;
        pDel=null;
      }
    }
    return true;
  }

  /**
   * The toString method computes the string representation of the list.
   * @return The string form of the list.
   */
  public String toString()
  {
    String listOfItems = "";
    Node p = this.first;
    if (p == null) return "List is empty";
    while (p!=null){
      listOfItems += p.value + ", ";
      p = p.next;
    }
    return listOfItems.substring(0,listOfItems.trim().lastIndexOf(","))+".";
  }

  /**
   * The add method adds an element at the beginning of the list.
   * @param element The element to add to the list
   */
  private void addFirst(T element)
  {
    Node pNode = new Node(element);
    if (this.first!=null) pNode.next=this.first;
    this.first = pNode;
  }


  /**
   * The addLast method adds an element at the end of the list.
   * @param element The element to add to the list
   */
  private void addLast(T element)
  {
    Node pNode = new Node(element);
    if (this.first==null) this.first = pNode;
    else{
      Node p = this.first;
      while (p.next!=null) p=p.next;
      p.next=pNode;
    }
  }

  /**
   * The add method adds an element at a position of the list.
   * @param element The element to add to the list
   * @param pos the position of the element in a list.
   */
  private void addMid(T element, int pos)
  {
    if(pos<=0) addFirst(element);
    else if(pos>=this.size()-1) add(element);
    else{
      Node pNode = new Node(element);
      Node pInsert = this.first;
      Node pPrevious = null;
      int count = 0;
      while (pInsert!=null){
        if (count==pos) break;
        pPrevious=pInsert;
        pInsert=pInsert.next;
        count++;
      }
      pNode.next = pInsert;
      pPrevious.next = pNode;
    }
  }

  /**
   * The remove method removes the first element in a list.
   */
  private void removeFirst(){
    Node pDel = this.first;
    if (pDel!=null){
      this.first = this.first.next;
      pDel.next=null;
      pDel=null;
    }
  }

  /**
   * The remove method removes the last element in a list.
   */
  private void removeLast(){
    if (this.first==null) return;
    else{
      Node p = this.first;
      Node pPrevious = null;
      while (p.next!=null){
        pPrevious = p;
        p=p.next;
      }
      pPrevious.next=null;
      p=null;
    }
  }

  /**
   * The remove method removes an element based on the position of the element in a list
   * @param pos the position of the element in a list.
   */
  protected void removeMid(int pos)
  {
    if(pos<=0) removeFirst();
    else if(pos>=this.size()-1) removeLast();
    else{
      Node pDel = this.first;
      Node pPrevious = this.first;
      int count = 0;
      while (pDel!=null){
        if (count==pos) break;
        pPrevious=pDel;
        pDel=pDel.next;
        count++;
      }
      pPrevious.next = pDel.next;
      pDel.next=null;
      pDel=null;
    }
  }

  /**
   * The search method searches an element in a list.
   * @param element The element to search.
   * @return the position of this element in a list
   */
  private int search(T element){
    Node p = this.first;
    int count = -1;
    boolean isSuccess = false;
    while (p!=null){
      count++;
      if (p.value.compareTo(element)==0) {
        isSuccess = true;
        break;
      }
      p = p.next;
    }
    return isSuccess ? count : -1;
  }

  /**
   * The edit method edits an element in a list.
   * @param pos the position of the element to edit in a list.
   * @param element The new element are edited in the position in a list
   */
  private void edit(int pos, T element){
    if(pos<=0){
      removeFirst();
      addFirst(element);
    }
    else if(pos>=this.size()-1){
      removeLast();
      add(element);
    }
    else {
      removeMid(pos);
      addMid(element, pos);
    }
  }

  /**
   * The sort method sorts the list.
   */
 /* public void sort(){
    for(Node pTemp1 = this.first; pTemp1!=null; pTemp1 = pTemp1.next)
      for (Node pTemp2 = pTemp1.next; pTemp2!=null; pTemp2 = pTemp2.next)
        if (pTemp1.value.compareTo(pTemp2.value) > 0){
          T temp = (T) pTemp1.value;
          pTemp1.value = pTemp2.value;
          pTemp2.value = temp;
        }
  }*/
}