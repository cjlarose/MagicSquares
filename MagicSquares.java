import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MagicSquares {
	
	int num_threads = 16;
	int order;
	int max;
	int magic_constant;
	long start_time;
	boolean print_squares = true;
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
			
			if (args.length > 1 && args[1].equals("threads")) {
				obj.test_num_threads();
			} else {
			
				System.out.println("Finding all magic matricies of order " + order);
				
				obj.init();
				
				long end_time = System.currentTimeMillis();
		        long runtime = end_time - obj.start_time;
		        double runtime_seconds = (double) runtime / (double) 1000;
		        
		        System.out.println("Found "+obj.magic_squares.size()+" magic squares in "+String.format("%f", runtime_seconds)+" seconds");
	        
			}
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
	
	public void init_sum_combinations_basic() {
		ArrayList<int[]> sum_combinations = get_sum_combinations();
		for (int i = 0; i < sum_combinations.size(); i++) {
			int[][] rows = new int[order][order];
			rows[0] = sum_combinations.get(i);
			for (int j = 0; j < sum_combinations.size(); j++) {
				rows[1] = sum_combinations.get(j);
				for (int k = 0; k < sum_combinations.size(); k++) {
					rows[2] = sum_combinations.get(k);
					int[] matrix_data = new int[max];
					for (int m = 0; m < order; m++)
						for (int n = 0; n < order; n++)
							matrix_data[order*m + n] = rows[m][n];
					SquareMatrix matrix = this.new SquareMatrix(matrix_data);
					if (matrix.is_magic()) {
						System.out.println(matrix.toString());
					}
				}
			}
		}
	}
	
	public void init_sum_combinations() {
		ArrayList<int[]> sum_combinations = get_sum_combinations();
		HashMap<Integer, ArrayList<int[]>> sum_combinations_by_initial_element = new HashMap<Integer, ArrayList<int[]>>();
		
		for (int i = 0; i < sum_combinations.size(); i++) {
			int[] c = sum_combinations.get(i);
			ArrayList<int[]> sub_list = sum_combinations_by_initial_element.get(c[0]);
			if (sub_list == null) {
				sum_combinations_by_initial_element.put(c[0], new ArrayList<int[]>());
				sub_list = sum_combinations_by_initial_element.get(c[0]);
			}
			sub_list.add(c);
		}
		
		for (int i = 0; i < sum_combinations.size(); i++) {
			
			int[][] rows = new int[order][order];
			rows[0] = sum_combinations.get(i);
			// r will be the first row.
			// get possible columns
			// TODO: Save possible columns to memory instead of regenerating for every comb
			ArrayList<int[]> cols = new ArrayList<int[]>();
			for (int j = i+1; j < sum_combinations.size(); j++) {
				int[] c = sum_combinations.get(j);
				if (c[0] == rows[0][0])
					cols.add(c);
				else 
					break;
			}
			
			for (int k = 0; k < cols.size(); k++) {
				// TODO: test if col and row are disjoint (excluding the first element in each)
				int[] col = cols.get(k);
				for (int m = 1; m < col.length; m++) {
					
				}
			}
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