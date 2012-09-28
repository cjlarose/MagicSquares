import java.util.Comparator;
import java.util.ArrayList;

public class MagicSquares {
	
	int order;
	int max;
	int magic_constant;
	long start_time;
	boolean print_squares = true;
	Comparator<int[]> int_arr_comparator;

	public MagicSquares(int order) {
		this.order = order;
		this.max = this.order*this.order;
		this.magic_constant = (this.order*this.order*this.order + this.order) / 2;
		this.int_arr_comparator = new Comparator<int[]>() {
			public int compare(int[] arr1, int[] arr2) {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != arr2[i]) {
						return (arr1[i] > arr2[i] ? 1 : -1);
					}
				}
				return 0;
			}
		};
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			
			int order = Integer.parseInt(args[0]);
			MagicSquares obj = new MagicSquares(order);
			
			System.out.println("Finding all magic matricies of order " + order);
			
			obj.init_magic_tree();
			
			long end_time = System.currentTimeMillis();
	        long runtime = end_time - obj.start_time;
	        double runtime_seconds = (double) runtime / (double) 1000;
	        
	        System.out.println(String.format("%f", runtime_seconds)+" seconds");
			
        } else {
            System.out.println("Usage: java MagicSquares <order>");
        }
	}
	
	static String str_repeat(String str, int repeat) {
		String result = "";
		for (int i = 0; i < repeat; i++)
			result += str;
		return result;
	}
	
	public void init_magic_tree() {
		this.start_time = System.currentTimeMillis();
		SumPermutationsList sum_permutations_list = new SumPermutationsList(this);
		
		MagicTree magic_tree = new MagicTree(this, sum_permutations_list);
		
		magic_tree.build_tree();
	}

	public ArrayList<int[]> get_sum_combinations() {
		
		ArrayList<Integer> elements = new ArrayList<Integer>();
		for (int i = 0; i < max; i++) {
			elements.add(i+1);
		}
		
		ArrayList<int[]>r = get_sum_combinations_recursive(elements, order, magic_constant);
		
		return r;
	}
	
	public ArrayList<int[]> get_sum_combinations_recursive(ArrayList<Integer> elements, int length, int sum) {
		ArrayList<int[]>r = new ArrayList<int[]>();
		
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
			for (Integer j: elements) {
				if (j != e)
					sub_sequence.add(j);
			}
			
 			ArrayList<int[]> sub_combinations = get_sum_combinations_recursive(sub_sequence, length-1, sum-e);
 			if (sub_combinations != null) {
				for (int j = 0; j < sub_combinations.size(); j++) {
					int[] r2 = new int[length];
					r2[0] = e;
					int[] arr = sub_combinations.get(j);
					for (int k = 0; k < arr.length; k++) {
						r2[k+1] = arr[k];
					}
					r.add(r2);
				}
 			}
		}
		return r;
	}

}
