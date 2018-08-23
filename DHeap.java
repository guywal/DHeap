/**
 * D-Heap
 */


public class DHeap
{
	
    public int size, max_size, d;
    public DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) {
    		this.size = array1.length;
    		for (int i = 0; i < array1.length; i++) { //reset array and insert array1 items to it
			array[i] = array1[i];
			array[i].setPos(i);
    		}
    		int parentIndex = parent(size - 1, this.d);
    		int counter = 0;// comparison operations counter
    		for (int j = parentIndex; j > -1; j--) { //iterates over all non leaf nodes
    			counter += this.HeapifyDown(this.array[j]); //add # of operations for each HeapifyDown
    		}
        return counter; 
    }

    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap() //for each node with parent check that parent.key <= child.key
    {
    		for (int i = size-1; i > 0; i--) {
        		int parentIndex = parent(i, this.d);
        		if (this.array[i].getKey() < this.array[parentIndex].getKey()) {
        			return false;
	    		}
		}
        return true;
    }


 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) { return (i-1)/d;} 
    public static int child (int i, int k, int d) { return (i*d) + k;} 

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) //insert item to the right place in the heap
    {        
	    	array[size] = item; //set item to be the last
	    	item.setPos(size++); //update item.pos
	    	return HeapifyUp(item);
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min()
    {
     	return Delete(array[0]);
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min()
    {
    	return array[0];
    }
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta)
    {
    	item.setKey(item.getKey() - delta); //decrease the key
		return this.HeapifyUp(item); //put the item on the right place
    }
	
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item)
    {
	    	int pos = item.getPos();
	    	this.swap(item, array[size-1]);//swap item with the last item
	    	size--;
	    	return HeapifyDown(array[pos]) + HeapifyUp(array[pos]); 	//put the item in the right place by calling HeapifyUp and HeapifyDown, only 1 will do anything
    }
	
	/**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		if (array1.length == 0) {//return 0 if the array is empty
			return 0;
		}
		DHeap heap = new DHeap(d, array1.length);
		for (int i = 0; i < array1.length; i++) { //puts all the items in the heap
			DHeap_Item item = new DHeap_Item(Integer.toString(array1[i]), array1[i]);
			heap.array[i] = item;
		}
		int counter = 0; //operations counter
		counter += heap.arrayToHeap(heap.array); //turn array into a legal heap
		int tempSize = heap.size;
		for (int j = 0; j < tempSize; j++) {//get the current min value and delete it from the heap until no items left
			array1[j] = heap.Get_Min().getKey(); //add the current min to the next cell in array1
			counter += heap.Delete_Min();
		}
		return counter; //return # of comparisons
	}
	
	private int HeapifyUp(DHeap_Item item) { // returns number of comparisons
		int parentIndex = parent(item.getPos(), this.d);
		if (item.getKey() < this.array[parentIndex].getKey()) {//check if we need to swap
			this.swap(item, this.array[parentIndex]);
			return 1 + this.HeapifyUp(item);
		}
		return 1;
	}
	
	private int HeapifyDown(DHeap_Item item) { //returns number of comparisons
		int minIndex = child(item.getPos(), 1, this.d);
		if (minIndex > size - 1) { //item has no children
			return 0; 								 
		}
		int minKey = this.array[minIndex].getKey(); //first child  
		int counter = 0;								
		for (int i = minIndex + 1; i <= Math.min(child(item.getPos(), this.d, this.d), this.size - 1); i++) { //iterate over all children of item
			DHeap_Item tempChild = this.array[i];   
			counter++;	//add to number of comparisons						  
			if (tempChild.getKey() < minKey) { // find the min child
				minKey = tempChild.getKey();	 
				minIndex = i;					
			}
		}
		DHeap_Item minChild = this.array[minIndex]; 
		if (minChild != null) { 
			if (minChild.getKey() < item.getKey()) { //if minChild has smaller key than item
				this.swap(item, minChild);
				return counter + this.HeapifyDown(item);
			}
		}
		return counter; //return # of comparisons
	}


	private void swap(DHeap_Item item, DHeap_Item minChild) { //swaps positions in array and updates item.pos, minChild.pos
		int tempIndex = minChild.getPos();
		this.array[item.getPos()] = minChild;
		this.array[tempIndex] = item;
		minChild.setPos(item.getPos());
		item.setPos(tempIndex);
		
	}

	
}
