import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class RedBlackTree {
	private TreeNode root;
	private TreeNode TNULL;
	public boolean flag = true;
	private Path opFileName = Path.of("output_file.txt");

	// Search the tree for a given rideNumber
	private TreeNode searchHelper(TreeNode node, int rideNumber) {
		// return null if the search reached a null node
		if (node == TNULL)
			return null;

		// return the current node if it has the same ride number
		if (rideNumber == node.rideNumber) {
			return node;
		}
		// recursively search the left subtree if the curr node ride number is greater
		// than the passed value
		if (rideNumber < node.rideNumber) {
			return searchHelper(node.left, rideNumber);
		}
		// recursively search the right subtree
		return searchHelper(node.right, rideNumber);
	}

	// Balance the RBT after deletion of a ride, using left and right rotations
	private void deleteFixup(TreeNode x) {
		TreeNode s;
		while (x != root && x.color == 0) {
			if (x == x.parent.left) {
				s = x.parent.right;
				if (s.color == 1) {
					s.color = 0;
					x.parent.color = 1;
					leftRotate(x.parent);
					s = x.parent.right;
				}

				if (s.left.color == 0 && s.right.color == 0) {
					s.color = 1;
					x = x.parent;
				} else {
					if (s.right.color == 0) {
						s.left.color = 0;
						s.color = 1;
						rightRotate(s);
						s = x.parent.right;
					}

					s.color = x.parent.color;
					x.parent.color = 0;
					s.right.color = 0;
					leftRotate(x.parent);
					x = root;
				}
			} else {
				s = x.parent.left;
				if (s.color == 1) {
					s.color = 0;
					x.parent.color = 1;
					rightRotate(x.parent);
					s = x.parent.left;
				}

				if (s.right.color == 0 && s.right.color == 0) {
					s.color = 1;
					x = x.parent;
				} else {
					if (s.left.color == 0) {
						s.right.color = 0;
						s.color = 1;
						leftRotate(s);
						s = x.parent.left;
					}

					s.color = x.parent.color;
					x.parent.color = 0;
					s.left.color = 0;
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		x.color = 0;
	}

	// transform the red black tree to satisfy constraints
	private void rbTransform(TreeNode node1, TreeNode node2) {
		if (node1.parent == null) {
			root = node2;
		} else if (node1 == node1.parent.left) {
			node1.parent.left = node2;
		} else {
			node1.parent.right = node2;
		}
		node2.parent = node1.parent;
	}

	// delete the ride with a given rideNumber from the tree
	private void deleteHelper(TreeNode node, int rideNumber) {
		TreeNode z = TNULL;
		TreeNode x, y;
		// search the tree for the ride number that has to be deleted
		while (node != TNULL) {
			if (node.rideNumber == rideNumber) {
				z = node;
			}

			if (node.rideNumber <= rideNumber) {
				node = node.right;
			} else {
				node = node.left;
			}
		}

		// if search results in null, then node doesn't exist in the tree to delete
		if (z == TNULL) {
			System.out.println("Couldn't find key in the tree");
			return;
		}

		// logic to delete the node through transformations
		y = z;
		int yOriginalColor = y.color;
		if (z.left == TNULL) {
			x = z.right;
			rbTransform(z, z.right);
		} else if (z.right == TNULL) {
			x = z.left;
			rbTransform(z, z.left);
		} else {
			y = minimum(z.right);
			yOriginalColor = y.color;
			x = y.right;
			if (y.parent == z) {
				x.parent = y;
			} else {
				rbTransform(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}

			rbTransform(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		// perform rotations to fix the tree if the node color is black
		if (yOriginalColor == 0) {
			deleteFixup(x);
		}
	}

	// Balance the node after insertion
	private void fixInsert(TreeNode k) {
		TreeNode u;
		while (k.parent.color == 1) {
			if (k.parent == k.parent.parent.right) {
				u = k.parent.parent.left;
				if (u.color == 1) {
					u.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.left) {
						k = k.parent;
						rightRotate(k);
					}
					k.parent.color = 0;
					k.parent.parent.color = 1;
					leftRotate(k.parent.parent);
				}
			} else {
				u = k.parent.parent.right;

				if (u.color == 1) {
					u.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.right) {
						k = k.parent;
						leftRotate(k);
					}
					k.parent.color = 0;
					k.parent.parent.color = 1;
					rightRotate(k.parent.parent);
				}
			}
			if (k == root) {
				break;
			}
		}
		root.color = 0;
	}

	// constructor to initialize the Red Black Tree values
	public RedBlackTree() {
		TNULL = new TreeNode(0, 0, 0, null);
		TNULL.color = 0;
		TNULL.left = null;
		TNULL.right = null;
		root = TNULL;
	}

	// return the TreeNode found upon searching or null if no node is found for the
	// passed ridenumber
	public TreeNode search(int rideNumber) {
		return searchHelper(this.root, rideNumber);
	}

	// return the minimum node linked to a given node
	public TreeNode minimum(TreeNode node) {
		while (node.left != TNULL) {
			node = node.left;
		}
		return node;
	}

	// finds the rides in range of the given ride numbers
	public void findNodesInRange(TreeNode curr, int rideNumber1, int rideNumber2) throws IOException {
		if (curr == null)
			return;

		// first we recurse the left subtree if it has values greater than rideNumber1
		if (rideNumber1 < curr.rideNumber) {
			findNodesInRange(curr.left, rideNumber1, rideNumber2);
		}

		// prints the rides found in the given ride number range
		if (rideNumber1 <= curr.rideNumber && rideNumber2 >= curr.rideNumber) {
			if (!flag) {
				Files.writeString(opFileName, ",", StandardOpenOption.APPEND);

			}
			flag = false;
			String output = "(" + curr.rideNumber + "," + curr.rideCost + "," + curr.tripDuration + ")";
			Files.writeString(opFileName, output, StandardOpenOption.APPEND);
		}

		// recurse the right subtree
		findNodesInRange(curr.right, rideNumber1, rideNumber2);
	}

	// performs a left rotation to balance the red black tree
	public void leftRotate(TreeNode x) {
		TreeNode y = x.right;
		x.right = y.left;
		if (y.left != TNULL) {
			y.left.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null) {
			this.root = y;
		} else if (x == x.parent.left) {
			x.parent.left = y;
		} else {
			x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
	}

	// performs a right rotation to balance the red black tree
	public void rightRotate(TreeNode x) {
		TreeNode y = x.left;
		x.left = y.right;
		if (y.right != TNULL) {
			y.right.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null) {
			this.root = y;
		} else if (x == x.parent.right) {
			x.parent.right = y;
		} else {
			x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
	}

	// inserts ride into the red black tree
	public TreeNode insert(int rideNumber, int rideCost, int tripDuration, HeapNode heapNode) {
		TreeNode node = new TreeNode(rideNumber, rideCost, tripDuration, heapNode);
		node.parent = null;
		node.left = TNULL;
		node.right = TNULL;
		node.color = 1;

		TreeNode y = null;
		TreeNode x = this.root;

		while (x != TNULL) {
			y = x;
			if (node.rideNumber < x.rideNumber) {
				x = x.left;
			} else {
				x = x.right;
			}
		}

		node.parent = y;
		if (y == null) {
			root = node;
		} else if (node.rideNumber < y.rideNumber) {
			y.left = node;
		} else {
			y.right = node;
		}

		if (node.parent == null) {
			node.color = 0;
			return node;
		}

		if (node.parent.parent == null) {
			return node;
		}

		fixInsert(node);
		return node;
	}

	// returns the root to the driver function
	public TreeNode getRoot() {
		return this.root;
	}

	// call the deleteHelper to perform delete
	public void deleteNode(int rideNumber) {
		deleteHelper(this.root, rideNumber);
	}
}