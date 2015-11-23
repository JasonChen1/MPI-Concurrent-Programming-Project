
public class ShellSort_serial {

	public ShellSort_serial(int[] values) {
		sort(values);

		// output the result
		
	/*	  for (int i = 0; i < values.length; i++) {
		 System.out.println(values[i]); }*/
		 
	}

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
