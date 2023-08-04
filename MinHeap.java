//Min-heap implementation to perform insert, delete min, and delete a specific key from the heap
public class MinHeap {
	public HeapNode[] Heap;
	public int index;
	private int size;

	// constructor to initialize the Min Heap to max size 2000
	public MinHeap(int size) {
		this.size = size;
		this.index = 0;
		Heap = new HeapNode[size];
	}

	// returns index of the parent node in the heap array
	private int parent(int i) {
		return (i - 1) / 2;
	}

	// returns index of the left child node in the heap array
	private int leftChild(int i) {
		return (i * 2) + 1;
	}

	// returns index of the right child node in the heap array
	private int rightChild(int i) {
		return (i * 2) + 2;
	}

	// returns true if the given node at index i is a leaf node
	private boolean isLeaf(int i) {
		if (leftChild(i) >= index) {// change2
			return true;
		}
		return false;
	}

	// inserts ride into the MinHeap
	public HeapNode insert(HeapNode element) {
		if (index >= size) {
			return null;
		}
		Heap[index] = element;
		int current = index;
		Heap[index].idx = index;
		// compares ride costs and min cost rides are put at top of heap
		while (Heap[current].rideCost < Heap[parent(current)].rideCost
				|| (current != parent(current) && Heap[current].rideCost == Heap[parent(current)].rideCost)) {
			// compares trip durations for same cost rides and puts lesser duration rides on
			// top of heap
			if (Heap[current].rideCost == Heap[parent(current)].rideCost
					&& Heap[current].tripDuration < Heap[parent(current)].tripDuration) {
				swap(current, parent(current));
				current = parent(current);
			} else if (Heap[current].rideCost < Heap[parent(current)].rideCost) {
				swap(current, parent(current));
				current = parent(current);
			} else {
				break;
			}
		}
		index++;
		return Heap[current];
	}

	// removes and returns the min element from the heap
	public HeapNode remove() {
		if (index == 0)
			return null;

		// root is the minimum since it is a min heap
		HeapNode popped = Heap[0]; // 2

		if (index != 1) {
			Heap[0] = Heap[--index];// 1
			minHeapify(0);
		} else {
			index--;
			Heap[0] = null;
		}
		return popped == null ? null : popped;
	}

	// deletes a ride at a given index from the min heap
	public HeapNode deleteKey(int i) {
		HeapNode delNode = Heap[i];
		decreaseKey(i, new HeapNode(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE));
		remove();
		return delNode;
	}

	// bubbles up the deleted ride to the top of the min heap for remove min
	// operation
	private void decreaseKey(int i, HeapNode new_val) {
		Heap[i] = new_val;
		while (i != 0 && ((Heap[parent(i)].rideCost > Heap[i].rideCost)
				|| (i != parent(i) && (Heap[parent(i)].rideCost == Heap[i].rideCost)
						&& (Heap[parent(i)].tripDuration > Heap[i].tripDuration)))) {
			{
				swap(i, parent(i));
				i = parent(i);
			}
		}
	}

	// performs min-heap heapify operation at index i
	private void minHeapify(int i) {

		// for a non-leaf node with any child having a lesser/equal ride cost
		if (!isLeaf(i)) {

			if (Heap[i] != null && (Heap[leftChild(i)] != null && Heap[i].rideCost >= Heap[leftChild(i)].rideCost)
					|| (Heap[rightChild(i)] != null && Heap[i].rideCost >= Heap[rightChild(i)].rideCost)) {

				// swapping the minimum of left and right child value with the parent
				if (Heap[leftChild(i)].rideCost < Heap[rightChild(i)].rideCost) {
					// comparing trip durations of rides having same cost
					if ((Heap[leftChild(i)].rideCost == Heap[i].rideCost)
							&& (Heap[leftChild(i)].tripDuration < Heap[i].tripDuration)) {
						swap(i, leftChild(i));
						minHeapify(leftChild(i));
					} else if (Heap[leftChild(i)].rideCost < Heap[i].rideCost) {
						swap(i, leftChild(i));
						minHeapify(leftChild(i));
					}
				} else {
					// comparing trip durations of rides having same cost
					if (Heap[rightChild(i)].rideCost == Heap[leftChild(i)].rideCost) {
						if (Heap[rightChild(i)].tripDuration < Heap[leftChild(i)].tripDuration) {
							swap(i, rightChild(i));
							minHeapify(rightChild(i));
						} else {
							swap(i, leftChild(i));
							minHeapify(leftChild(i));
						}
					} else if ((Heap[rightChild(i)].rideCost == Heap[i].rideCost)
							&& (Heap[rightChild(i)].tripDuration < Heap[i].tripDuration)) {
						swap(i, rightChild(i));
						minHeapify(rightChild(i));
					} else if (Heap[rightChild(i)].rideCost < Heap[i].rideCost) {
						swap(i, rightChild(i));
						minHeapify(rightChild(i));
					}
				}
			}
		}
	}

	// swapping 2 nodes in the min heap
	private void swap(int x, int y) {
		HeapNode tmp;
		tmp = Heap[x];
		Heap[x] = Heap[y];
		Heap[y] = tmp;

		Heap[x].idx = x;
		Heap[y].idx = y;

	}

}
