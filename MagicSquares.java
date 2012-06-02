import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class MagicSquares {
	
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
			
			obj.init_magic_tree();
			
			long end_time = System.currentTimeMillis();
	        long runtime = end_time - obj.start_time;
	        double runtime_seconds = (double) runtime / (double) 1000;
	        
	        System.out.println("Found "+obj.magic_squares.size()+" magic squares in "+String.format("%f", runtime_seconds)+" seconds");
			
        } else {
            System.out.println("Usage: java MagicSquares <order>");
        }
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
			for (int i = 1; i < order; i++) 
				if (!is_cell_empty(m,i))
					map.put(i, data[m][i]);
			
			int[][] r = new int[map.size()][2];
			Iterator<Entry<Integer, Integer>> map_iterator = map.entrySet().iterator();
			for (int i = 0; i < map.size(); i++) {
				Entry<Integer, Integer> entry = map_iterator.next();
				r[i] = new int[] {entry.getKey().intValue(), entry.getValue().intValue()};
			}
			return r;
			//return map;
		}
		public int get_cell_contents(int m, int n) {
			return this.data[m][n];
		}
		public SquareMatrix to_matrix() {
			return new SquareMatrix(this.data);
		}
	}
	
	public class MagicTree {
		MagicTreeNode root = new MagicTreeNode();
		SumPermutationsList sum_permutations_list;
		
		public MagicTree(SumPermutationsList sum_permutations_list) {
			this.sum_permutations_list = sum_permutations_list;
			for (int[] p: sum_permutations_list.get_all_data()) {
				root.add_child(p);
			}
		}
		
		public void build_tree() {
			for (MagicTreeNode node: this.root.children) {
				node.build();
			}
		}
		
		public class MagicTreeNode {
			public int[] data;
			public ArrayList<MagicTreeNode> children = new ArrayList<MagicTreeNode>();
			public boolean is_row;
			public int index;
			public MagicTreeNode parent;
			
			public MagicTreeNode() {};
			
			public MagicTreeNode(int[] data, boolean is_row, int index, MagicTreeNode parent) {
				this.data = data;
				this.is_row = is_row;
				this.index = index;
				this.parent = parent;
			}
			
			public MagicTreeNode add_child(int[] data) {
				boolean child_is_row = this.data == null ? true : !this.is_row;
				int child_index = this.data == null ? 0 : (this.is_row ? this.index : this.index+1);
				MagicTreeNode child = new MagicTreeNode(data, child_is_row, child_index, this);
				this.children.add(child);
				return child;
			}
			
			public int[] get_row(int m) {
				int[] r = new int[this.index+1];
				MagicTreeNode current_node = this;
				while (current_node.data != null) {
					if (current_node.is_row != true) {
						r[current_node.index] = current_node.data[m];
					}
					current_node = current_node.parent;
				}
				return r;
			}
			
			public int[] get_column(int n) {
				int[] r = new int[this.index+1];
				MagicTreeNode current_node = this;
				while (current_node.data != null) {
					if (current_node.is_row == true)
						r[current_node.index] = current_node.data[n];
					current_node = current_node.parent;
				}
				return r;
			}
		
			public Set<Integer> get_elements() {
				Set<Integer> r = new HashSet<Integer>();
				MagicTreeNode current_node = this;
				while (current_node.data != null) {
					for (int e: current_node.data) {
						r.add(e);
					}
					current_node = current_node.parent;
				}
				return r;
			}
			
			public SquareMatrix to_matrix() {
				MatrixBuilder matrix_builder = new MatrixBuilder();
				MagicTreeNode current_node = this;
				while (current_node.data != null) {
					if (current_node.is_row) {
						matrix_builder.set_row(current_node.index, current_node.data);
					}
					current_node = current_node.parent;
				}
				return matrix_builder.to_matrix();
			}
			
			public void build() {
				Set<Integer> forbidden_elements = this.get_elements();
				if (this.is_row) {
					if (this.index == order-1) {
						// this is a potentially magic square
						SquareMatrix matrix = this.to_matrix();
						if (matrix.is_magic())
							handle_magic_matrix(matrix);
					} else {
						// all my children (who are columns) inherit my index
						// 1. get list of forbidden elements
						// 2. establish which elements i already know have to be true about my children
						// 3. get list of possible rows that begin with every element I know about my children &&
						//    that do not contain the forbidden elements
						
						// i know exactly this.index+1 elements of my child column
						int[] child_begin = this.get_column(this.index);
	
						for (int i: child_begin) {
							forbidden_elements.remove(i);
						}
						
						ArrayList<int[]> child_possibilities = sum_permutations_list.query(child_begin, forbidden_elements);
						
						for (int i = 0; i < child_possibilities.size(); i++) {
							MagicTreeNode child = this.add_child(child_possibilities.get(i));
							child.build();
						}
					}
				} else {
					int[] child_begin = this.get_row(this.index+1);
					for (int i: child_begin) {
						forbidden_elements.remove(i);
					}
					ArrayList<int[]> child_possibilities = sum_permutations_list.query(child_begin, forbidden_elements);
					for (int i = 0; i < child_possibilities.size(); i++) {
						MagicTreeNode child = this.add_child(child_possibilities.get(i));
						child.build();
					}
				}
			}
			
		}
	}
	public void init_magic_tree() {
		this.start_time = System.currentTimeMillis();
		SumPermutationsList sum_permutations_list = this.new SumPermutationsList();
		
		MagicTree magic_tree = this.new MagicTree(sum_permutations_list);
		
		magic_tree.build_tree();
	}
	
	public void handle_magic_matrix(SquareMatrix matrix) {
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
        			System.out.println(matrix.toString());
        		}
			}
		} else {
			magic_squares.add(matrix);
    		if (print_squares) {
    			System.out.println("Magic Square #" + magic_squares.size());
    			System.out.println(matrix.toString());
    		}
		}
	}

	public class SumPermutationsList {
		public ArrayList<int[]> data;
		
		public SumPermutationsList() {
			this.data = get_sum_combinations();
		}
		
		public ArrayList<int[]> get_all_data() {
			return this.data;
		}
		
		public ArrayList<int[]> query(int[] init) {
			ArrayList<int[]> r = new ArrayList<int[]>();
			for (int i = 0; i < this.data.size(); i++) {
				int[] element = this.data.get(i);
				boolean matches = true;
				for (int j = 0; j < init.length; j++) {
					if (element[j] != init[j]) {
						matches = false;
						break;
					}
				}
				if (matches)
					r.add(element);
			}
			return r;
		}
		public ArrayList<int[]> query(int[] init, Set<Integer> exclusion_set) {
			ArrayList<int[]> r = this.query(init);
			Iterator iter_r = r.iterator();
			while (iter_r.hasNext()) {
				int[] element = (int[]) iter_r.next();
				boolean remove_it = false;
				for (int i = 0; i < element.length; i++) {
					if (exclusion_set.contains(element[i])) {
						remove_it = true;
						break;
					}
				}
				if (remove_it)
					iter_r.remove();
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