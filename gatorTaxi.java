import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

//Main class to run the program
public class gatorTaxi {
	public static void main(String[] args) throws IOException {

		// setting a flag 'terminate' to terminate code when duplicate ride is found
		boolean terminate = false;
		List<String> wholeInput = new ArrayList<>();
		String input;

		// instantiating the MinHeap and Red Black Tree
		MinHeap minHeap = new MinHeap(2000);// setting max size as 2000
		RedBlackTree rbtObj = new RedBlackTree();

		// setting the path of the output file
		Path opFileName = Path.of("output_file.txt");
		BufferedWriter writer = Files.newBufferedWriter(Paths.get("output_file.txt"));
		writer.write("");
		writer.flush();

		// reading input from the input file
		try {
			// File file = new File("input2.txt");
			File file = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null)
				wholeInput.add(st);
			br.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
		// processing the input
		for (String s : wholeInput) {
			input = s;
			String[] arr = input.split("\\(");
			input = arr[0];
			String[] inputParam = new String[3];
			String temp = arr[1].substring(0, arr[1].length() - 1);
			if (input.equals("Print")) {
				if (temp.contains(",")) {
					inputParam = temp.split(",");
					input += "2";
				} else {

					inputParam[0] = temp;
				}
			} else if (input.equals("Insert")) {
				inputParam = temp.split(",");
			} else if (input.equals("UpdateTrip")) {
				inputParam = temp.split(",");
			}

			switch (input) {
				case "Print":
					// searching the tree for the rideNumber in the input
					TreeNode ride = rbtObj.search(Integer.parseInt(inputParam[0]));
					// if no such ride is found, print(0,0,0)
					if (ride == null) {
						Files.writeString(opFileName, "(0,0,0)\n", StandardOpenOption.APPEND);
					} else {// printing the ride details of the found ride
						String output = "(" + ride.rideNumber + "," + ride.rideCost + "," + ride.tripDuration + ")\n";
						Files.writeString(opFileName, output, StandardOpenOption.APPEND);
					}
					break;
				case "Print2":
					// printing the ride details of the rides found in the given range
					rbtObj.findNodesInRange(rbtObj.getRoot(), Integer.parseInt(inputParam[0]),
							Integer.parseInt(inputParam[1]));
					// if no rides are found in the given range
					if (rbtObj.flag) {
						Files.writeString(opFileName, "(0,0,0)\n", StandardOpenOption.APPEND);
					} else { // using flag to not print any extra ','
						Files.writeString(opFileName, "\n", StandardOpenOption.APPEND);
						rbtObj.flag = true;
					}
					break;
				case "Insert":
					ride = rbtObj.search(Integer.parseInt(inputParam[0]));
					// if ride already exists for the given ride number
					if (ride != null) {
						Files.writeString(opFileName, "Duplicate RideNumber", StandardOpenOption.APPEND);
						terminate = true;
						break;
					} else {// inserting ride into the MinHeap and Red Black Tree
						HeapNode insertedNode = minHeap.insert(new HeapNode(Integer.parseInt(inputParam[0]),
								Integer.parseInt(inputParam[1]), Integer.parseInt(inputParam[2])));
						insertedNode.rbt = rbtObj.insert(insertedNode.rideNumber, insertedNode.rideCost,
								insertedNode.tripDuration, insertedNode);
					}
					break;
				case "GetNextRide":
					HeapNode currRide = minHeap.remove();
					// if the min heap is empty
					if (currRide == null) {
						Files.writeString(opFileName, "No active ride requests\n", StandardOpenOption.APPEND);
					} else {// returning the ride at the root of MinHeap
						if (currRide.rbt != null)
							rbtObj.deleteNode(currRide.rbt.rideNumber);
						String output = "(" + currRide.rideNumber + "," + currRide.rideCost + ","
								+ currRide.tripDuration
								+ ")\n";
						Files.writeString(opFileName, output, StandardOpenOption.APPEND);
					}

					break;
				case "CancelRide":
					TreeNode nodeDel = rbtObj.search(Integer.parseInt(temp));
					// canceling the found ride by deleting from MinHeap and Red Black Tree
					if (nodeDel != null) {
						HeapNode delNode = minHeap.deleteKey(nodeDel.node.idx);
						rbtObj.deleteNode(delNode.rbt.rideNumber);
					}

					break;

				case "UpdateTrip":
					int rideNo = Integer.parseInt(inputParam[0]);
					int new_duration = Integer.parseInt(inputParam[1]);

					// searching the red black tree using ride number
					ride = rbtObj.search(rideNo);

					// if no such ride found, move to next input
					if (ride == null)
						break;
					// case 1 of update trip scenario
					if (new_duration <= ride.tripDuration) {
						// delete the existing ride
						HeapNode delNode4 = minHeap.deleteKey(ride.node.idx);
						rbtObj.deleteNode(delNode4.rbt.rideNumber);

						// insert new ride with updated ride cost
						HeapNode insertedNode = minHeap.insert(new HeapNode(rideNo, delNode4.rideCost, new_duration));
						insertedNode.rbt = rbtObj.insert(insertedNode.rideNumber, insertedNode.rideCost,
								insertedNode.tripDuration, insertedNode);
						// case 2 of update trip scenario
					} else if ((ride.tripDuration < new_duration) && (new_duration <= (2 * ride.tripDuration))) {

						int newCost = ride.rideCost + 10;

						// delete the existing ride
						HeapNode delNode2 = minHeap.deleteKey(ride.node.idx);
						rbtObj.deleteNode(delNode2.rbt.rideNumber);

						// insert new ride with updated ride cost
						HeapNode insertedNode = minHeap.insert(new HeapNode(rideNo, newCost, new_duration));
						insertedNode.rbt = rbtObj.insert(insertedNode.rideNumber, insertedNode.rideCost,
								insertedNode.tripDuration, insertedNode);
						// case 3 of update trip scenario
					} else if (new_duration > (2 * ride.tripDuration)) {

						// delete the existing ride from min heap and red black tree
						HeapNode delNode3 = minHeap.deleteKey(ride.node.idx);
						rbtObj.deleteNode(delNode3.rbt.rideNumber);
					}
					break;

			}
			// terminating the program in case of inserting a duplicate ride
			if (terminate)
				break;
		}

	}
}