import mpi.MPI;
import mpi.MPIException;

public class ShellSort_parallel {

	public ShellSort_parallel(int[] values) throws MPIException {

		// MPI implementation below
		/** ====================================================== **/
		// first divide the list by the number of processes into equal size of
		// chunks for each process
		int chunk = values.length / Sort.getSize();
		// set the size of the list for each process
		int[] tempList = new int[chunk];

		// sends chunks of an array to different processes.
		/* ================================= */
		// got it from
		// http://mpitutorial.com/tutorials/mpi-scatter-gather-and-allgather/
		MPI.COMM_WORLD.scatter(values, chunk, MPI.INT, tempList, chunk, MPI.INT, 0);
		/* ================================= */

		// sort each chunk on each process using shellSort
		sort(tempList);

		// create a list to add all the chunks back together
		int[] sortedList = null;

		/*
		 * only initialize the list when all the other chunk has been sorted
		 * which mean rank should be 0
		 */
		if (Sort.getMyrank() == 0) {
			sortedList = new int[values.length];
		}

		// after each chunk has been sorted add them back to one
		/* ================================= */
		// got it from
		// http://mpitutorial.com/tutorials/mpi-scatter-gather-and-allgather/
		MPI.COMM_WORLD.gather(tempList, chunk, MPI.INT, sortedList, chunk, MPI.INT, 0);
		/* ================================= */

		// when all other process has finish sorting do one final sort
		if (Sort.getMyrank() == 0) {
			// use insertion sort to sort the final array
			finalSort(sortedList);

			// output the result
			/*
			 * for (int i = 0; i < sortedList.length; i++) { System.out.println(
			 * "original list: " + values[i] + "\tsortedList:==== " +
			 * sortedList[i]); }
			 */
		}
		/** ====================================================== **/

	}

	/** insertion sort implementation for the final sort */
	public void finalSort(int[] sortData) {
		for (int i = 1; i < sortData.length; i++) {
			int temp = sortData[i];
			int j = i;
			// while the preview value is smaller keep on checking
			while (j > 0 && sortData[j - 1] > temp) {
				sortData[j] = sortData[j - 1];
				j--;
			}
			// if the previews value are bigger than the current value swap them
			sortData[j] = temp;
		}
	}

	/**
	 * The method starts by sorting pairs of elements far apart from each other,
	 * then progressively reducing the gap between elements to be compared.
	 * Starting with far apart elements can move some out-of-place elements into
	 * position faster than a simple nearest neighbor exchange.
	 */
	public void sort(int[] toSortList) {

		for (int gap = toSortList.length / 2; gap > 0; gap /= 2) {
	        // do the insertion sort
	        for (int i = gap; i < toSortList.length; i++) {
	            int val = toSortList[i];
	            int j;
	            /* each time go through the loop will check if the value in the
				 * starting index is bigger or equal to the current index if so
				 * set the current value as the starting value*/
	            for (j = i; j >= gap && toSortList[j - gap]>val; j -= gap) {
	            	//setting the current value as
					// the starting value
	            	toSortList[j] = toSortList[j - gap];
	            }
	         // swap the value
	            toSortList[j] = val;
	        }
	    }
		
	}

}
