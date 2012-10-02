import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SumPermutationsList {

	private final MagicSquares magic_squares;
	private final List<int[]> data;
	private final List<Integer> sets;
	private Map<Integer, int[]> index;

	public SumPermutationsList(MagicSquares magicSquares) {
		magic_squares = magicSquares;
		this.data = get_sum_combinations();
		this.sets = to_sets(this.data);
		this.index = Collections.synchronizedMap(new HashMap<Integer, int[]>());
	}
	
	private List<Integer> to_sets(List<int[]> arrs) {
		List<Integer> r = new ArrayList<Integer>(arrs.size());
		for (int[] arr: arrs) {
			int set = 0;
			List<Integer> arr_list = new ArrayList<Integer>();
			for (int i: arr)
				arr_list.add(i);
			for (int i = magic_squares.max; i > 0; i--) {
				if (arr_list.contains(i))
					set += 1;
				set <<= 1;
			}
			r.add(set);
		}
		return r;
	}

	public List<int[]> get_all_data() {
		return this.data;
	}

	public boolean arr_begins_with(int[] arr, int[] init) {
		for (int i = 0; i < init.length; i++) {
			if (init[i] != arr[i])
				return false;
		}
		return true;
	}

	public List<int[]> get_sum_combinations() {

		List<Integer> elements = new ArrayList<Integer>();
		for (int i = 0; i < magic_squares.max; i++) {
			elements.add(i + 1);
		}

		List<int[]> r = get_sum_combinations_recursive(elements,
				magic_squares.order, magic_squares.magic_constant);

		return r;
	}

	public List<int[]> get_sum_combinations_recursive(List<Integer> elements,
			int length, int sum) {
		List<int[]> r = new ArrayList<int[]>();

		if (length == 1) {
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).equals(sum)) {
					int[] base_answer = new int[1];
					base_answer[0] = elements.get(i);
					r.add(base_answer);
					return r;
				}
			}
			return null;
		}

		for (int i = 0; i < elements.size(); i++) {
			int e = elements.get(i);
			ArrayList<Integer> sub_sequence = new ArrayList<Integer>();
			for (Integer j : elements) {
				if (j != e)
					sub_sequence.add(j);
			}

			List<int[]> sub_combinations = get_sum_combinations_recursive(
					sub_sequence, length - 1, sum - e);
			if (sub_combinations != null) {
				for (int j = 0; j < sub_combinations.size(); j++) {
					int[] r2 = new int[length];
					r2[0] = e;
					int[] arr = sub_combinations.get(j);
					for (int k = 0; k < arr.length; k++) {
						r2[k + 1] = arr[k];
					}
					r.add(r2);
				}
			}
		}
		return r;
	}

	public List<int[]> query(int[] init) {
		return query(init, 0);
	}

	public List<int[]> query(int[] init, int exclusion_bit_set) {
		ArrayList<int[]> r = new ArrayList<int[]>();
		int[] index_result = this.index.get(Arrays.hashCode(init));
		
		if (index_result != null) {
			if (index_result[0] >= 0) {
				//List<int[]> sub_list = this.data.subList(index_result[0], index_result[1] + 1);
				for (int i = index_result[0]; i < index_result[1] + 1; i++)
					if ((this.sets.get(i) & exclusion_bit_set) == 0)
						r.add(this.data.get(i));
			}
		} else {
			
	
			int found_index = Collections.binarySearch(this.data, init,
					new Comparator<int[]>() {
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
			
			int begin_index = found_index;
			int end_index = found_index;
	
			if (i >= 0) {
				
				while (i >= 0) {
					if (arr_begins_with(this.data.get(i), init)) {
						if ((this.sets.get(i) & exclusion_bit_set) == 0)
							r.add(this.data.get(i));
						begin_index = i;
					} else {
						break;
					}
					i--;
				}
	
				i = found_index + 1;
				while (i < this.data.size()) {
					if (arr_begins_with(this.data.get(i), init)) {
						if ((this.sets.get(i) & exclusion_bit_set) == 0)
							r.add(this.data.get(i));
						end_index = i;
					} else {
						break;
					}
					i++;
				}
			}
			
			this.index.put(Arrays.hashCode(init), new int[] {begin_index, end_index});
	
		}
		return r;
	}

}