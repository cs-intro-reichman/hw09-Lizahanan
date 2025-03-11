/** Represents an iterator over a list of CharData objects. */
public class ListIterator {

    // Current position in the list (cursor)
    Node current;

    /** Constructs a list iterator, starting at the given node. */
    public ListIterator(Node node) {
        // Sets the cursor of this iterator to the given node
        current = node;
    }

    /** Checks if this iterator has more nodes to process */
    public boolean hasNext() {
        return (current != null);
    }
  
    /** Returns the CharData object of the current element in this iteration,
     *  and advances the cursor to the next element.
     *  Should be called only if hasNext() is true. */
    public CharData next() {
        CharData cp = current.cp;
        current = current.next;
        return cp;
    }

    //my defined method to get the charData at a given index
    public CharData getCharData(int index) {
        // Loop until the desired index
        for (int i = 0; i < index; i++) {
            if (current == null) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
            }
            current = current.next;
        }
        
        // Final check if current is null before accessing its data
        if (current == null) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        
        return current.cp;
    }
    
    }
