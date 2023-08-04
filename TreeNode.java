//Data Structure to store the ride details in a Red Black Tree
public class TreeNode {
    public int rideNumber;
    public int rideCost;
    public int tripDuration;
    public TreeNode right;
    public TreeNode left;
    public TreeNode parent;
    public int color;
    HeapNode node;// pointer to the MinHeap Node

    // constructor to initialize ride details in the Red Black tree
    public TreeNode(int rideNumber, int rideCost, int tripDuration, HeapNode node) {
        this.left = null;
        this.right = null;
        this.parent = null;
        this.rideNumber = rideNumber;
        this.rideCost = rideCost;
        this.tripDuration = tripDuration;
        this.color = 1;
        this.node = node;
    }
}
