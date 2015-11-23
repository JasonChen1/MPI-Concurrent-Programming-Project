
public class Insertion_serial {

	public Insertion_serial(int[] datalist) {		
		// pass datalist to the sort method to sort the array
		sort(datalist);
		
		/*// Outputting the sorted result and time spend
		for (int i = 0; i < datalist.length; i++) {
			System.out.println(datalist[i]);
		}*/
	
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
