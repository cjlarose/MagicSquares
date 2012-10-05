import java.util.Comparator;

public class MagicSquares {

	int order;
	int max;
	int magic_constant;
	long start_time;
	long list_built_time;
	long end_time;
	boolean print_squares = true;
	Comparator<int[]> int_arr_comparator;

	public MagicSquares(int order) {
		this.order = order;
		this.max = this.order * this.order;
		this.magic_constant = (this.order * this.order * this.order + this.order) / 2;
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

			long list_build_time = obj.list_built_time - obj.start_time;
			long magic_time = obj.end_time - obj.list_built_time;
			long runtime = obj.end_time - obj.start_time;

			System.out.println("Time spent building list:\t" + list_build_time + " ms");
			System.out.println("Time spent building squares:\t" + magic_time + " ms");
			System.out.println("Total runtime:\t\t\t" + runtime + " ms");

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
		SumPermutationsList sum_permutations_list = new SumPermutationsList(
				this);
		this.list_built_time = System.currentTimeMillis();
		MagicTreeBuilder magic_tree = new MagicTreeBuilder(this, sum_permutations_list);
		magic_tree.build_tree();
		this.end_time = System.currentTimeMillis();
	}

}
