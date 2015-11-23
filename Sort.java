import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import mpi.MPI;
import mpi.MPIException;

public class Sort {

	private static ArrayList<Integer> data = new ArrayList<Integer>();//temporary array to store the load file
	private static Scanner scan = null;
	private static int myrank;//the rank of the process
	private static int size;//the number of processes
	private static StopWatch time = new StopWatch();//timer
	private static int[] values;// the data list

	public Sort() {

	}

	// loading file by scanning the input file and store the values into an
	// integer array
	public static void loading(String file) throws IOException {
		File files = new File(file);

		try {
			scan = new Scanner(files);// initializing new scanner

			while (scan.hasNextLine()) {
				// adding each integer into the array
				data.add(scan.nextInt());
				// check the next line of the file
				scan.nextLine();
			}
			scan.close();
		} catch (Exception e) {
			System.out.print("Loading file fail");
		}

	}

	/** change array to a list */
	public static void arrayToList(ArrayList<Integer> array) {
		//make sure array is not empty before adding to list
		if (!array.isEmpty()) {
			values = new int[array.size()];
			// copy the array data over to the list
			for (int i = 0; i < values.length; i++) {
				values[i] = data.get(i);
			}

			// if the length of the list is equal to 0 o r 1 then its no point
			// running the sort method because its already sorted
			if (values.length <= 1) {
				return;
			}
		}
	}

	/** get the rank of the process */
	public static int getMyrank() {
		return myrank;
	}

	/** get the number of process */
	public static int getSize() {
		return size;
	}

	public static void main(String[] args) throws IOException, MPIException {
		//testing code on local machine
		//loading("rand.dups.100000.txt");
		//arrayToList(data);
		// new Insertion_serial(values);
		// new Selection_serial(values);
		// new MergeSort_serial(values);
		// new ShellSort_serial(values);

		// the next 4 lines of code are from hello world example
		/*===================*/
		String hostName = null;
		MPI.Init(args);
		myrank = MPI.COMM_WORLD.getRank();// get rank of processors
		size = MPI.COMM_WORLD.getSize();// get number of processors
		/*===================*/
		
		String sortType = null;
		
		// first input should be the file second should be algorithm make
		// sure they are not null
		if (args[0] != null && args[1] != null) {
			// load data into array
			loading(args[0]);
			arrayToList(data);
			
			// make sure the array is not empty before sorting the
			// data(double check)
			if (values.length != 0) {
				//start timing 
				time.start();
				// if more than 1 process running then run it on parallel
				if (size > 1) {
					if (args[1].equalsIgnoreCase("insertion")) {
						sortType = "Insertion Sort Parallel";
						new Insertion_parallel(values);
					} else if (args[1].equalsIgnoreCase("shell")) {
						sortType = "shellSort Parallel";
						new ShellSort_parallel(values);
					} else if (args[1].equalsIgnoreCase("merge")) {
						sortType = "MergeSort Parallel";
						new MergeSort_parallel(values);
					} else {
						System.out.println("The three sorting algorithm are: insertion,shell and merge");
					}
				}
				// if there are only 1 process running then run it on serial
				else if (size == 1) {
					if (args[1].equalsIgnoreCase("insertion")) {
						sortType = "Insertion Sort Serial";
						new Insertion_serial(values);
					} else if (args[1].equalsIgnoreCase("shell")) {
						sortType = "shellSort Serial";
						new ShellSort_serial(values);
					} else if (args[1].equalsIgnoreCase("merge")) {
						sortType = "MergeSort Serial";
						new MergeSort_serial(values);
					} else {
						System.out.println("The three sorting algorithm are: insertion,shell and merge");
					}
				}
			}
			//stop timing
			time.stop();
		} else {
			System.out.println("Argument 1 should be the file, and Argument "
					+ "2 should be the sort type i.e(insertion,selection and merge)");
		}

		// the next 7 lines of code i got it from hello world example
		/*===================*/
		try {
			Process hostname = Runtime.getRuntime().exec("hostname");
			BufferedReader input = new BufferedReader(new InputStreamReader(hostname.getInputStream()));
			hostName = input.readLine();
		} catch (Exception e) {
		}

		MPI.Finalize();
		/*===================*/

		System.out.println( sortType + " Sorting time from rank: " + myrank +" of "+size+"\tTime: " + time.getElapsedTime()
				+ " milliseconds ("+ hostName +")\n");
	}

}
