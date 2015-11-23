
import mpi.MPI;
import mpi.MPIException;

public class Insertion_parallel {

	public Insertion_parallel(int[] values) throws MPIException {

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

		// sort each chunk on each process
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
			// calling the sorting method to sort the array
			sort(sortedList);

			/*// ouput the result
			for (int i = 0; i < sortedList.length; i++) {
				System.out.println("original list: " + values[i] + "\tsortedList:==== " + sortedList[i]);
			}*/
		}
		/** ====================================================== **/

	}

	/**
	 * Insertion sort algorithm: go through the array start from 1, if the
	 * previews value is bigger than the current value swap it and keep checking
	 * the values before the previews value (if there are any) if the values are
	 * smaller than the current value keep swapping them until the value is
	 * sorted
	 */
	public void sort(int[] sortData) {

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

}
