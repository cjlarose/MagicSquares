import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SumPermutationsList {

	private final MagicSquares magic_squares;
	private final List<int[]> data;
	private final Map<Integer, List<int[]>> set_map;
	private final int[] factorial_map;

	public SumPermutationsList(MagicSquares magicSquares) {
		magic_squares = magicSquares;
		this.data = get_sum_combinations();
		this.set_map = to_map(this.data);
		this.factorial_map = generate_factorial_map();
	}

	private int[] generate_factorial_map() {
		int[] map = new int[this.magic_squares.order];
		int k = 1;
		for (int n = 1; n < this.magic_squares.order; n++) {
			k *= n;
			map[n] = k;	
		}
		return map;
	}

	private Map<Integer, List<int[]>> to_map(List<int[]> arrs) {
		Map<Integer, List<int[]>> r = new HashMap<Integer, List<int[]>>();
		for (int[] arr : arrs) {
			int key = 0;
			List<Integer> arr_list = new ArrayList<Integer>();
			for (int i : arr)
				arr_list.add(i);
			for (int i = magic_squares.max; i > 0; i--) {
				if (arr_list.contains(i))
					key += 1;
				key <<= 1;
			}

			if (!r.containsKey(key))
				r.put(key, new ArrayList<int[]>());
			r.get(key).add(arr);

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
		return query(init, new HashSet<Integer>());
	}

	private static int index_of(int n, int set) {
		int set_copy = set >> 1;
		int k = 0;
		for (int i = 0; i < n - 1; i++) {
			if ((set_copy & 1) == 1)
				k++;
			set_copy >>= 1;
		}
		return k;
	}

	public List<int[]> query(int[] init, Set<Integer> exclusion_set) {
		ArrayList<int[]> r = new ArrayList<int[]>();
		
		int exclusion_bit_set = 0;
		for (int i: exclusion_set)
			exclusion_bit_set |= 1 << i;
		
		int inclusion_bit_set = 0;
		for (int i: init)
			inclusion_bit_set |= 1 << i;

		for (int key : this.set_map.keySet()) {
			/*
			 * pick the keys such that the key and exclusion set are disjoint,
			 * and that the key is a subset of the inclusion set
			 */
			if ((key | inclusion_bit_set) == key && (key & exclusion_bit_set) == 0) {
				List<int[]> check = this.set_map.get(key);

				int new_key = key;
				
				int start_index = 0;
				for (int i = 0; i < init.length; i++) {
					start_index += this.factorial_map[this.magic_squares.order - 1 - i]
							* index_of(init[i], new_key);
					new_key ^= 1 << init[i];
				}
				
				int end_index = start_index + this.factorial_map[this.magic_squares.order - init.length];
				r.addAll(check.subList(start_index, end_index));


			}
		}

		return r;
	}
}