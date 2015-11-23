

import mpi.MPI;
import mpi.MPIException;

public class MergeSort_parallel {

	public MergeSort_parallel(int[] datalist) throws MPIException {

		// MPI implementation below
		/** ====================================================== **/
		// first divide the list by the number of processes into equal size of chunks for each process
		int chunk = datalist.length / Sort.getSize();
		// set the size of the list for each process
		int[] tempList = new int[chunk];

		// sends chunks of an array to different processes.
		/* ================================= */
		// got it from
		// http://mpitutorial.com/tutorials/mpi-scatter-gather-and-allgather/
		MPI.COMM_WORLD.scatter(datalist, chunk, MPI.INT, tempList, chunk, MPI.INT, 0);
		/* ================================= */

		// helper list for merging
		int[] tempHelperList = new int[tempList.length];
		// sort each chunk on each process
		sort(tempHelperList, tempList, 0, tempList.length - 1);

		// create a list to add all the chunks back together
		int[] sortedList = null;

		/*
		 * only initialize the list when all the other chunk has been sorted
		 * which mean rank should be 0
		 */
		if (Sort.getMyrank() == 0) {
			sortedList = new int[datalist.length];
		}

		// after each chunk has been sorted add them back to one
		/* ================================= */
		// got it from
		// http://mpitutorial.com/tutorials/mpi-scatter-gather-and-allgather/
		MPI.COMM_WORLD.gather(tempList, chunk, MPI.INT, sortedList, chunk, MPI.INT, 0);
		/* ================================= */

		// when all other process has finish sorting do one final sort
		if (Sort.getMyrank() == 0) {
			int[] sortedHelperList = new int[datalist.length];
			// calling the sorting method to sort the array
			sort(sortedHelperList, sortedList, 0, sortedList.length - 1);
			
			//ouput the result
			/* for (int i = 0; i < sortedList.length; i++) {
			 System.out.println("original list: " + datalist[i] +
			 "\tsortedList:==== " + sortedList[i]);
			 }
			*/
		}
		/** ====================================================== **/

	}

	/**
	 * keep spiting the list into chunks till there is only 1 value each chunk
	 * then call merge method to merge them back up in sorted order
	 */
	public void sort(int[] helper, int[] toSortList, int left, int right) {
		// getting the middle value to split the list
		int mid = left + (right - left) / 2;

		if (left < right) {
			// keep dividing the left hand side until there are only 1 value
			// each chunk (recursive call)
			sort(helper, toSortList, left, mid);

			// keep dividing the right hand side until there are only 1 value
			// each chunk (recursive call)
			sort(helper, toSortList, mid + 1, right);

			// sorting by merging the value back together
			merge(helper, toSortList, left, mid, right);
		}
	}

	/**
	 * merging the values back up (same as the merge method from the serial
	 * Version the only change is adding the two list parameter instead of using
	 * the original list)
	 */
	private void merge(int[] helper, int[] toSortList, int left, int mid, int right) {
		// copy the array over to the helper array which use to merge/sort the
		// values
		for (int i = left; i <= right; i++) {
			helper[i] = toSortList[i];
		}

		int l = left;// use for the helper array
		int m = mid + 1;
		int lo = left;// use by the original array

		while (l <= mid && m <= right) {
			/*
			 * checking if the value on the left are smaller than the value in
			 * middle if so replace the value in the original list(should be the
			 * same value) otherwise swap them
			 */
			if (helper[l] <= helper[m]) {
				toSortList[lo++] = helper[l++];
			} else {
				toSortList[lo++] = helper[m++];
			}
		}

		// copying the rest of the value into the original list
		while (l <= mid) {
			toSortList[lo++] = helper[l++];
		}

	}

}
