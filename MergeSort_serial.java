

public class MergeSort_serial {

	private int[] datalist;
	private int[] helperList;

	public MergeSort_serial(int[] values) {

		datalist = values;
		helperList = new int[datalist.length];
		
		// calling the sorting method to sort the array
		sort(0, datalist.length - 1);

		// Outputting the sorted result 
		/*for (int i = 0; i < datalist.length; i++) {
			System.out.println(datalist[i]);
		}*/
	}

	public void sort(int left, int right) {
		// getting the middle value to split the list 
		int mid = left + (right - left) / 2;

		if (left < right) {
			// keep dividing the left hand side until there are only 1 value
			// each chunk (recursive call)
			sort(left, mid);

			// keep dividing the right hand side until there are only 1 value
			// each chunk (recursive call)
			sort(mid + 1, right);

			// sorting by merging the value back together
			merge(left, mid, right);
		}
	}

	// merging the values back up
	private void merge(int left, int mid, int right) {
		// copy the array over to the helper array which use to merge/sort the
		// values
		for (int i = left; i <= right; i++) {
			helperList[i] = datalist[i];
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
			if (helperList[l] <= helperList[m]) {
				datalist[lo++] = helperList[l++];
			} else {
				datalist[lo++] = helperList[m++];
			}
		}

		// copying the rest of the value into the original list
		while (l <= mid) {
			datalist[lo++] = helperList[l++];
		}

	}

}
