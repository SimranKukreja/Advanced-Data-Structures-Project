//Data Structure to store the ride details in a MinHeap
class HeapNode {
	int rideNumber;
	int rideCost;
	int tripDuration;
	int idx;
	TreeNode rbt;// pointer to the Red Black Tree Node

	// constructor to initialize ride details in the MinHeap
	public HeapNode(int rideNumber, int rideCost, int tripDuration) {
		this.rideNumber = rideNumber;
		this.rideCost = rideCost;
		this.tripDuration = tripDuration;
		this.rbt = null;
	}
}