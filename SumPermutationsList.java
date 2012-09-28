import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class SumPermutationsList {
	/**
	 * 
	 */
	private final MagicSquares magic_squares;
	public ArrayList<int[]> data;
	
	public SumPermutationsList(MagicSquares magicSquares) {
		magic_squares = magicSquares;
		this.data = magic_squares.get_sum_combinations();
	}
	
	public ArrayList<int[]> get_all_data() {
		return this.data;
	}
	
	public boolean arr_begins_with(int[] arr, int[] init) {
		for (int i = 0; i < init.length; i++) {
			if (init[i] != arr[i])
				return false;
		}
		return true;
	}
	
	public boolean arr_disjoint(int[] arr, Set<Integer> exclusion_set) {
		for (int i = 0; i < arr.length; i++) {
			if (exclusion_set.contains(arr[i]))
				return false;
		}
		return true;
	}
	
	public ArrayList<int[]> query(int[] init) {
		return query(init, new HashSet<Integer>());
	}
	
	public ArrayList<int[]> query(int[] init, Set<Integer> exclusion_set) {
		ArrayList<int[]> r = new ArrayList<int[]>();
		
		int found_index = Collections.binarySearch(this.data, init, new Comparator<int[]>() {
			public int compare(int[] arr, int[] init) {
				for (int i = 0; i < init.length; i++) {
					if (arr[i] != init[i]) {
						return (arr[i] > init[i] ? 1 : -1);
					}
				}
				return 0;
			}
		});
		
		int i = found_index;
		
		if (i >= 0) {
			while (i >= 0) {
				if (arr_begins_with(this.data.get(i), init)) {
					if (arr_disjoint(this.data.get(i), exclusion_set))
						r.add(this.data.get(i));
				} else {
					break;
				}
				i--;
			}
			
			i = found_index + 1;
			while (i < this.data.size()) {
				if (arr_begins_with(this.data.get(i), init)) {
					if (arr_disjoint(this.data.get(i), exclusion_set))
						r.add(this.data.get(i));
				} else {
					break;
				}
				i++;
			}
		}
		
		return r;
	}
}