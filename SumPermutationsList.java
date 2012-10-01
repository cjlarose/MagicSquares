import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SumPermutationsList {

	private final MagicSquares magic_squares;
	private final List<int[]> data;
	private final Map<Integer, List<int[]>> set_map;
	//private Map<Integer, int[]> index;

	public SumPermutationsList(MagicSquares magicSquares) {
		magic_squares = magicSquares;
		this.data = get_sum_combinations();
		this.set_map = to_map(this.data);
		//this.index = Collections.synchronizedMap(new HashMap<Integer, int[]>());
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

	private int sequence_to_bit_mask(Collection<Integer> exclusion_set) {
		int set = 0;
		for (int i = magic_squares.max; i > 0; i--) {
			if (exclusion_set.contains(i))
				set += 1;
			set <<= 1;
		}
		return set;
	}

	private int sequence_to_bit_mask(int[] seq) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i : seq)
			list.add(i);
		return sequence_to_bit_mask(list);
	}

	public List<int[]> query(int[] init, Set<Integer> exclusion_set) {
		ArrayList<int[]> r = new ArrayList<int[]>();
		
		int exclusion_bit_set = sequence_to_bit_mask(exclusion_set);
		int inclusion_bit_set = sequence_to_bit_mask(init);
		
		for (int key: this.set_map.keySet()) {
			/*
			 *  pick the keys such that the key and exclusion set are disjoint,
			 *  and that the key is a subset of the inclusion set 
			 */
			if ((key & exclusion_bit_set) == 0 && (key | inclusion_bit_set) == key)  {
				//viable_keys.add(key);
				List<int[]> check = this.set_map.get(key);
				for (int[] i: check)
					if (arr_begins_with(i, init))
						r.add(i);
			}
		}
		
		return r;
	}
}