import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

public class MagicSquares {
	
	int num_threads = 16;
	int order;
	int max;
	int magic_constant;
	long start_time;
	boolean print_squares = true;
	boolean eliminate_dupes = true;
	ArrayList<MagicSquares.SquareMatrix> magic_squares = new ArrayList<MagicSquares.SquareMatrix>();
	
	public MagicSquares(int order) {
		this.order = order;
		this.max = this.order*this.order;
		this.magic_constant = (this.order*this.order*this.order + this.order) / 2;
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			
			int order = Integer.parseInt(args[0]);
			MagicSquares obj = new MagicSquares(order);
			
			System.out.println("Finding all magic matricies of order " + order);
			
			//obj.init();
			obj.init_sum_combinations();
			
			long end_time = System.currentTimeMillis();
	        long runtime = end_time - obj.start_time;
	        double runtime_seconds = (double) runtime / (double) 1000;
	        
	        System.out.println("Found "+obj.magic_squares.size()+" magic squares in "+String.format("%f", runtime_seconds)+" seconds");
			
        } else {
            System.out.println("Usage: java MagicSquares <order>");
        }
	}
	
	public void init() {
		
		this.start_time = System.currentTimeMillis();
		
        long i = 0;
        long end_i = MagicSquares.factorial(this.max);
        
        long chunk_size = MagicSquares.factorial(this.max) / this.num_threads;
        
        ArrayList<Thread> threads = new ArrayList<Thread>();
        
        for (int j = 0; j < this.num_threads; j++) {
        	long a = i;
        	long b = Math.min(i + chunk_size, end_i);
        	Thread t = new Thread(this.new MatrixThread(a,b));
        	threads.add(t);
        	t.start();
        	if (b >= end_i)
        		break;
        	i += chunk_size + 1;
        }
        
        for (int j = 0; j < threads.size(); j++) {
        	try {
				threads.get(j).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
	}
	
	public class MatrixThread extends Thread {
		long a;
		long b;
		public MatrixThread(long a, long b) {
			this.a = a;
			this.b = b;
			//System.out.println("I've got perms " + a +" thru " + b + "!");
		}
		public void run() {
			for (long i = this.a; i < this.b; i++) {
				int[] current_permutation = get_permutation(i);
				MagicSquares.SquareMatrix m = new SquareMatrix(current_permutation);
	        	if (m.is_magic()) {
	        		magic_squares.add(m);
	        		if (print_squares)
	        			thread_message(m, a, b, i);
	        	}
			}
		}
	}
	
	public void thread_message(MagicSquares.SquareMatrix m, long a, long b, long i) {
		long time = System.currentTimeMillis();
		String name = Thread.currentThread().getName();
		System.out.println((time-start_time) + "ms: Magic Matrix "+magic_squares.size()+" found at "+i+" ("+(i-a)+" of "+(b-a)+" on "+name+"):");
		System.out.println(m.toString());
	}
	
	public class SquareMatrix {
		int[] data;
		int[][] equivalence_class;
		
		public SquareMatrix(int[] data) {
			this.data = data;
		}
		
		public SquareMatrix(int[][] data_2d) {
			this.data = new int[max];
			for (int m = 0; m < order; m++) 
				for (int n = 0; n < order; n++)
					this.data[order*m+n] = data_2d[m][n];
		}
		
		public SquareMatrix(Integer[] input) {
			for (int i = 0; i < input.length; i++) {
				this.data[i] = (int) input[i];
			}
		}
		
		public boolean is_magic() {
			
			for (int m = 0; m < order; m++) {
				int row_sum = 0;
				for (int n = 0; n < order; n++)
					row_sum += this.data[order * m +n];
				if (row_sum != magic_constant)
					return false;
			}
			
			for (int n = 0; n < order; n++) {
				int col_sum = 0;
				for (int m = 0; m < order; m++)
					col_sum += this.data[order * m + n];
				if (col_sum != magic_constant)
					return false;
			}
			
			int left_diagonal_sum = 0;
			for (int i = 0; i < order; i++)
				left_diagonal_sum += this.data[order*i+i];
			if (left_diagonal_sum != magic_constant)
				return false;
			
			int right_diagonal_sum = 0;
			for (int i = 0; i < order; i++)
				right_diagonal_sum += this.data[order*i+order-i-1];
			if (right_diagonal_sum != magic_constant)
				return false;
			
			return true;
		}
		
		public String toString() {
			// old_data[m][n] = new_data[order*m + n]
			String max_term = max + "";
			int max_term_size = max_term.length();
			
			String result = "";
			
			String border = "+";
			String border_between = "|";
			for (int i = 0; i < order; i++) {
				border += "--" + MagicSquares.str_repeat("-", max_term_size);
				border_between += "--" + MagicSquares.str_repeat("-", max_term_size);
				if (i != order - 1 ) {
					border += "-";
					border_between += "+";
				}
			}
			border += "+\n";
			border_between += "|\n";
			
			result += border;
			
			for (int m = 0; m < order; m++) {
				result += "|";
				for (int n = 0; n < order; n++) {
					int padding_right = max_term_size - (data[order*m+n] + "").length();
					result += " " + data[order*m+n] + MagicSquares.str_repeat(" ", padding_right)+ " |";
				}
				result += "\n";
				if (m != order - 1)
					result += border_between;
			}
			result += border;
			return result;
		}
		
		public void rotate_right() {
			int[][] new_data = new int[order][order];
			for (int i = 0; i < this.data.length; i++) {
				int m = i / order;
				int n = i % order;
				new_data[n][order-m-1] = this.data[i];
			}
			for (int m = 0; m < order; m++) 
				for (int n = 0; n < order; n++)
					this.data[order*m + n] = new_data[m][n];
		}
		
		public void transpose() {
			int[][] new_data = new int[order][order];
			for (int i = 0; i < this.data.length; i++) {
				int m = i / order;
				int n = i % order;
				new_data[n][m] = this.data[i];
			}
			for (int m = 0; m < order; m++) 
				for (int n = 0; n < order; n++)
					this.data[order*m + n] = new_data[m][n];
		}
		
		public int[][] get_equivalence_class() {
			MagicSquares.SquareMatrix matrix = new MagicSquares.SquareMatrix(this.data);
			int[][] r = new int[8][max];
			for (int i = 0; i < 8; i = i + 2) {
				
				for (int m = 0; m < order; m++) 
					for (int n = 0; n < order; n++)
						r[i][order*m + n] = matrix.data[order*m+n];
				
				matrix.transpose();
				
				for (int m = 0; m < order; m++) 
					for (int n = 0; n < order; n++)
						r[i+1][order*m + n] = matrix.data[order*m+n];
				
				matrix.transpose();
				matrix.rotate_right();
				
			}
			return r;
		}
		
		public boolean equals(MagicSquares.SquareMatrix comp_matrix) {
			//int[][] equivalence_class = this.get_equivalence_class();
			if (this.equivalence_class == null)
				this.equivalence_class = this.get_equivalence_class();
			for (int i = 0; i < equivalence_class.length; i++)
				if (Arrays.equals(comp_matrix.data, equivalence_class[i]))
					return true;
			return false;
		}
	}
	
	private static String str_repeat(String str, int repeat) {
		String result = "";
		for (int i = 0; i < repeat; i++)
			result += str;
		return result;
	}
	
	public static long factorial(int n) {
		long ret = 1;
        for (int i = 1; i <= n; ++i) ret *= i;
        return ret;
    }
	
	public int[] get_permutation(long i) {
		ArrayList<Integer> p = new ArrayList<Integer>();
		for (int k = 1; k < max+1; k++)
			p.add(k);
		
		int[] r = new int[max];
		for (int j = 0; j<max; j++) {
			long g = MagicSquares.factorial(p.size() - 1);
			int k = (int)(i/g);
			r[j] = p.get(k);
			p.remove(k);
			i = i % g;
		}
		return r;
	}
	
	public void test_num_threads() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		MagicSquares obj = new MagicSquares(3);
		for (int i = 1; i <= 128; i++) {
			obj.print_squares = false;
			obj.num_threads = i;
			obj.init();
			long end_time = System.currentTimeMillis();
			int runtime = (int) (end_time - obj.start_time);
			System.out.println("Threads: " + i + ", Time: " + runtime + "ms");
			map.put(i, runtime);
		}
		
		Integer min = Collections.min(map.values());
		ArrayList<Map.Entry<Integer,Integer>> min_entries = new ArrayList<Map.Entry<Integer,Integer>>();
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet())
		    if (min == entry.getValue())
		        min_entries.add(entry);

		System.out.println("Fastest time was "+min+"ms and occured when there were ");
		for (int i = 0; i < min_entries.size(); i++) {
			System.out.println(min_entries.get(i).getKey() + " threads");
		}
	}
	
	public void init_sum_combinations_dumb() {
		ArrayList<int[]> sum_combinations = get_sum_combinations();
		
		long end_game = (long) Math.pow(sum_combinations.size(), order);
		
		for (long i = 0; i < end_game; i++) {
			
			int[] indicies = new int[order];
			
			long j = i;
			int k = 0;
			while (k < order) {
				int power = order - k - 1;
				long divisor = (long) Math.pow(sum_combinations.size(), power);
				indicies[k] = (int) (j / divisor);
				j = (long) (j % divisor);
				k++;
			}
			
			ArrayList<Integer> matrix_data = new ArrayList<Integer>();
			
			boolean disjoint = true;
			for (int m = 0; m < order; m++) {
				int[] row = sum_combinations.get(indicies[m]);
				if (!matrix_data.isEmpty()) {
					for (int a: row) {
						if (matrix_data.contains(a)) {
							disjoint = false;
							break;
						}
					}
				}
				if (disjoint) {
					for (int a: row) matrix_data.add(a);
				} else {
					break;
				}
			}
			if (disjoint) {
				int d = 0;
				int[] flat_matrix_data = new int[max];
				Iterator<Integer> matrix_iterator = matrix_data.iterator();
				while (matrix_iterator.hasNext()) {
					flat_matrix_data[d] = matrix_iterator.next();
					d++;
				}
				SquareMatrix matrix = this.new SquareMatrix(flat_matrix_data);
				
				if (matrix.is_magic())
					System.out.println(matrix.toString());
			}
			
		}
	}
	
	public class MatrixBuilder {
		
		int[][] data = new int[order][order];
		Set<Integer> members_set = new HashSet<Integer>();
		Stack<int[][]> history = new Stack<int[][]>();
		public MatrixBuilder() {
			this.save();
		}
		public boolean set_row(int n, int[] values) {
			int[][] keys = new int[values.length][2];
			for (int i = 0; i < values.length; i++)
				keys[i] = new int[] {n,i};
			return set_cell_contents(keys, values);
		}
		public boolean set_col(int m, int[] values) {
			int[][] keys = new int[values.length][2];
			for (int i = 0; i < values.length; i++)
				keys[i] = new int[] {i,m};
			return set_cell_contents(keys, values);
		}
		public boolean set_left_diagonal(int[] values) {
			int[][] keys = new int[values.length][2];
			for (int i = 0; i < values.length; i++)
				keys[i] = new int[] {i,i,};
			return set_cell_contents(keys, values);
		}
		public boolean set_right_diagonal(int[] values) {
			int[][] keys = new int[values.length][2];
			for (int i = 0; i < values.length; i++)
				keys[i] = new int[] {order-1-i, i};
			return set_cell_contents(keys, values);
		}
		public boolean is_cell_empty(int m, int n) {
			return this.data[m][n] == 0;
		}
		private void save() {
			int[][] data_copy = new int[order][order];
			for (int i = 0; i < data_copy.length; i++) {
				data_copy[i] = Arrays.copyOf(this.data[i], order);
			}
			this.history.push(data_copy);
		}
		public void undo() {
			this.data = this.history.pop();
			this.members_set = new HashSet<Integer>();
			for (int m = 0; m < order; m++)
				for (int n = 0; n < order; n++)
					if (this.data[m][n] != 0)
						this.members_set.add(this.data[m][n]);
		}
		private boolean set_cell_contents(int[][] keys, int[] values) {
			boolean valid = true;
			for (int i = 0; i < keys.length; i++) {
				int m = keys[i][0];
				int n = keys[i][1];
				if (!is_cell_empty(m,n) && values[i] != data[m][n]) {
					valid = false;
					break;
				}
				if (is_cell_empty(m,n) && this.contains(values[i])) {
					valid = false;
					break;
				}
			}
			
			if (valid) {
				this.save();
				for (int i = 0; i < keys.length; i++) {
					int m = keys[i][0];
					int n = keys[i][1];
					data[m][n] = values[i];
					members_set.add(values[i]);
				}
				return true;
			} else {
				return false;
			}
		}
		public boolean contains(int i) {
			return this.members_set.contains(i);
		}
		public int[][] get_row_indicies(int m) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int i = 0; i < order; i++) 
				if (!is_cell_empty(m,i))
					map.put(i, data[m][i]);
			
			int[][] r = new int[map.size()][2];
			Iterator<Entry<Integer, Integer>> map_iterator = map.entrySet().iterator();
			for (int i = 0; i < map.size(); i++) {
				Entry<Integer, Integer> entry = map_iterator.next();
				r[i] = new int[] {entry.getKey().intValue(), entry.getValue().intValue()};
			}
			return r;
		}
		public SquareMatrix to_matrix() {
			return new SquareMatrix(this.data);
		}
	}
	
	public class SumCombinationThread extends Thread {
		
		SumPermutationsList sum_permutations_list;
		ArrayList<int[]> sub_list;
		int sub_list_size;
		
		public SumCombinationThread(SumPermutationsList sum_permutations_list, int i) {
			this.sum_permutations_list = sum_permutations_list;
			this.sub_list = sum_permutations_list.get_subset_begins_with(i);
			this.sub_list_size = sub_list.size();
		}
		public void run() {
			MatrixBuilder matrix_builder = new MatrixBuilder();
			for (int j = 0; j < sub_list_size; j++) {
				
				int[] row = sub_list.get(j);
				matrix_builder.set_row(0, row);
				
				for (int k = 0; k < sub_list_size; k++) {
						
					int[] col = sub_list.get(k);
					
					if (matrix_builder.set_col(0, col)) {
					
						for (int m = 0; m < sub_list_size; m++) {
							
							int[] left_diagonal = sub_list.get(m);
							
							if (matrix_builder.set_left_diagonal(left_diagonal)) {
								
								int[][] indicies = new int[][] {new int[] {0,row[order-1]}, new int[] {order-1,left_diagonal[order-1]}};
								ArrayList<int[]> right_col_possibilities = sum_permutations_list.get_subset_by_values(indicies);
								
								for (int t = 0; t < right_col_possibilities.size(); t++) {
									int[] right_col = right_col_possibilities.get(t);
									
									if (matrix_builder.set_col(order-1, right_col)) {
										
										indicies = new int[][] {new int[] {0,col[order-1]}, new int[] {order-1,left_diagonal[order-1]}};
										ArrayList<int[]> bottom_row_possibilities = sum_permutations_list.get_subset_by_values(indicies);
										
										for (int u = 0; u < bottom_row_possibilities.size(); u++) {
											int[] bottom_row = bottom_row_possibilities.get(u);
											
											if (matrix_builder.set_row(order-1, bottom_row)) {
												
												indicies = new int[][] {new int[] {0,col[order-1]}, new int[] {order-1,row[order-1]}};
												ArrayList<int[]> right_diagonal_possibilities = sum_permutations_list.get_subset_by_values(indicies);
												
												for (int v = 0; v < right_diagonal_possibilities.size(); v++) {
													int[] right_diagonal = right_diagonal_possibilities.get(v);
													
													if (matrix_builder.set_right_diagonal(right_diagonal)) {
														
														// begin
														Map<Integer,ArrayList<int[]>> row_possibilities = new HashMap<Integer, ArrayList<int[]>>();
														
														for (int n = 1; n < order-1; n++) {
															
															indicies = matrix_builder.get_row_indicies(n);
															ArrayList<int[]> possible_rows = sum_permutations_list.get_subset_by_values(indicies);
															row_possibilities.put(n, possible_rows);
															
														}
														
														ArrayList<int[][]> row_permutations = get_row_permutations(row_possibilities);
														for (int n = 0; n < row_permutations.size(); n++) {
															
															int[][] p = row_permutations.get(n);
															
															boolean success = true;
															int q = 0;
															while (q < p.length) {
																if (!matrix_builder.set_row(q+1, p[q])) {
																	success = false;
																	break;
																}
																q++;
															}
															
															if (success) {
																SquareMatrix matrix = matrix_builder.to_matrix();
																if (matrix.is_magic()) {
																	if (eliminate_dupes) {
																		boolean is_unique = true;
																		for (int r = 0; r < magic_squares.size(); r++) {
																			if (matrix.equals(magic_squares.get(r)))
																				is_unique = false;
																		}
																		if (is_unique) {
																			magic_squares.add(matrix);
															        		if (print_squares) {
															        			long time = System.currentTimeMillis();
															        			System.out.println("["+(time-start_time)+"]: Magic Square #" + magic_squares.size());
															        			System.out.println(matrix_builder.to_matrix().toString());
															        		}
																		}
																	} else {
																		magic_squares.add(matrix);
														        		if (print_squares) {
														        			System.out.println("Magic Square #" + magic_squares.size());
														        			System.out.println(matrix_builder.to_matrix().toString());
														        		}
																	}
																}
															}
															
															for (int s = 0; s < q; s++)
																matrix_builder.undo();
															
														}
														//end
														
														// undo right diagonal
														matrix_builder.undo();
													}
												}
												
												// undo bottom row
												matrix_builder.undo();
											}
										}
										// right col undo
										matrix_builder.undo();
									}
								}
								//left diagonal undo
								matrix_builder.undo();
							}
						}
						// left col undo
						matrix_builder.undo();
					}
				}
				// top row undo
				matrix_builder.undo();
			}
		}
	}
	
	public void init_sum_combinations() {
		this.start_time = System.currentTimeMillis();
		SumPermutationsList sum_permutations_list = this.new SumPermutationsList();
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 1; i <= max; i++) {
			Thread t = new Thread(this.new SumCombinationThread(sum_permutations_list, i));
	    	threads.add(t);
	    	t.start();
		}
		
		for (int i = 0; i < threads.size(); i++) {
	    	try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}
	
	/*
	 * http://stackoverflow.com/questions/659117/whats-a-good-way-to-structure-variable-nested-loops
	 */
	public ArrayList<int[][]> get_row_permutations(Map<Integer,ArrayList<int[]>> row_possibilities) {
		ArrayList<ArrayList<int[]>> items = new ArrayList<ArrayList<int[]>>();
		Iterator<Entry<Integer, ArrayList<int[]>>> entry_set_iterator = row_possibilities.entrySet().iterator();
		for (int i = 0; i < row_possibilities.size(); i++) {
			items.add(entry_set_iterator.next().getValue());
		}
		
		ArrayList<int[][]> r = new ArrayList<int[][]>();
		
		int possible_combinations = 1;
		for (int i = 0; i < items.size(); i++)
			possible_combinations *= items.get(i).size();
		
		for (int i = 0; i < possible_combinations; i++) {
			int index = i;
			int[][] this_combination = new int[items.size()][order];
			for (int j = 0; j < items.size(); j++) {
				ArrayList<int[]> item_list = items.get(j);
				int item_from_this_list = index % item_list.size();
				this_combination[j] = item_list.get(item_from_this_list);
				index /= item_list.size();
			}
			r.add(this_combination);
		}
		
		return r;
	}
	
	public class SumPermutationsList {
		public ArrayList<int[]> data;
		HashMap<Integer, ArrayList<int[]>> index_by_initial_element = new HashMap<Integer, ArrayList<int[]>>();
		
		public SumPermutationsList() {
			this.data = get_sum_combinations();
			for (int i = 1; i <= max; i++) {
				this.index_by_initial_element.put(i, new ArrayList<int[]>());
			}
			for (int i = 0; i < this.data.size(); i++) {
				int[] c = this.data.get(i);
				this.index_by_initial_element.get(c[0]).add(c);
			}
		}
		
		public ArrayList<int[]> get_subset_begins_with(int i) {
			return this.index_by_initial_element.get(i);
		}
		
		public ArrayList<int[]> get_subset_by_values(int[][] values) {
			ArrayList<int[]> r = new ArrayList<int[]>();
			for (int i = 0; i < data.size(); i++) {
				int[] p = data.get(i);
				boolean valid = true;
				for (int j = 0; j < values.length; j++) {
					int k = values[j][0];
					int v = values[j][1];
					if (p[k] != v) {
						valid = false;
						break;
					}
				}
				if (valid)
					r.add(p);
			}
			return r;
		}
		
		public boolean arr_exclusive(int[] arr1, int[] arr2) {
			for (int i = 1; i < arr1.length; i++)
				for (int j = 1; j < arr2.length; j++)
					if (arr1[i] == arr2[j])
						return false;
			return true;
		}
		
		public boolean arr_exclusive(int[] arr1, int[][] arrs) {
			for (int i = 0; i < arrs.length; i++)
				if (!arr_exclusive(arr1, arrs[i]))
					return false;
			return true;
		}
		
		public int[][] indicies_to_permutation_arr(int[] indicies) {
			int[][] r = new int[indicies.length-1][order];
			ArrayList<int[]> sub_list = index_by_initial_element.get(indicies[0]);
			for (int i = 0; i < r.length; i++) {
				r[i] = sub_list.get(indicies[i+1]);
			}
			return r;
		}
		
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