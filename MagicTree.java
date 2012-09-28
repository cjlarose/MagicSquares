import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class MagicTree {
	/**
	 * 
	 */
	private final MagicSquares magic_squares;
	MagicTreeNode root = new MagicTreeNode();
	SumPermutationsList sum_permutations_list;
	
	public MagicTree(MagicSquares magicSquares, SumPermutationsList sum_permutations_list) {
		magic_squares = magicSquares;
		this.sum_permutations_list = sum_permutations_list;
		
		ArrayList<int[]> permutations_list_data = sum_permutations_list.get_all_data();
		for (int[] p: permutations_list_data)
			root.add_child(p);
	}
	
	public int[] arr_reverse(int[] arr) {
		int[] r = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			r[arr.length-1-i] = arr[i];
		}
		return r;
	}
	
	public class NodeBuilderTask extends RecursiveTask<Set<SquareMatrix>> {

		private static final long serialVersionUID = 7218910311926378380L;
		
		private List<MagicTreeNode> nodes;
		public Set<SquareMatrix> result = new HashSet<SquareMatrix>();
		
		public NodeBuilderTask(List<MagicTreeNode> nodes) {
			this.nodes = nodes;
		}
		
		@Override
		public Set<SquareMatrix> compute() {
			if (nodes.size() <= 500) {
				for (MagicTreeNode node: nodes) {
					result.addAll(node.build());
				}
			} else {
				
				int half = nodes.size() / 2;
				
				List<MagicTreeNode> upper_half = nodes.subList(0, half);
				List<MagicTreeNode> lower_half = nodes.subList(half, nodes.size());
				
				NodeBuilderTask worker1 = new NodeBuilderTask(upper_half);
				NodeBuilderTask worker2 = new NodeBuilderTask(lower_half);
				
				worker1.fork();
				result.addAll(worker2.compute());
				result.addAll(worker1.join());
			}
			System.out.println("Task found "+result.size()+" magic matrices");
			return result;
		}
	}
	
	public void build_tree() {
		int processors = Runtime.getRuntime().availableProcessors();
		
		NodeBuilderTask task = new NodeBuilderTask(root.children);
		ForkJoinPool pool = new ForkJoinPool(processors);
		pool.invoke(task);
		
		Set<SquareMatrix> magic_squares = task.result;
		System.out.println("Computed Result: " + magic_squares.size());
	}
	
	public class MagicTreeNode {
		public int[] data;
		public ArrayList<MagicTreeNode> children = new ArrayList<MagicTreeNode>();
		public int type; // 0 for root, 1 for main diagonal, 2 for row, 3 for column.
		public int index;
		public MagicTreeNode parent;
		
		public MagicTreeNode() {};
		
		public MagicTreeNode(int[] data, int type, int index, MagicTreeNode parent) {
			this.data = data;
			this.type = type;
			this.index = index;
			this.parent = parent;
		}
		
		public MagicTreeNode add_child(int[] data) {
			int child_type = 0;
			int child_index = 0;
			switch (this.type) {
				case 0: 
					child_type = 1;
					child_index = -1;
				break;
				case 1: 
					child_type = 2;
					child_index = 0;
				break;
				case 2: 
					child_type = 3;
					child_index = this.index;
				break;
				case 3: 
					child_type = 2;
					child_index = this.index+1;
				break;
				default: 
					child_type = -42;
					child_index = -42;
			}
			MagicTreeNode child = new MagicTreeNode(data, child_type, child_index, this);
			this.children.add(child);
			return child;
		}
		
		public int[] get_main_diagonal() {
			MagicTreeNode current_node = this;
			while (current_node.data != null) {
				if (current_node.type == -1)
					return current_node.data;
				current_node = current_node.parent;
			}
			return null;
		}
		
		public int[] get_row(int m) {
			int[] r = new int[this.index+2];
			MagicTreeNode current_node = this;
			while (current_node.data != null) {
				if (current_node.type == 3) 
					r[current_node.index] = current_node.data[m];
				else if (current_node.type == 1)
					r[m] = current_node.data[m];
				current_node = current_node.parent;
			}
			return r;
		}
		
		public int[] get_column(int n) {
			int[] r = new int[this.index+1];
			MagicTreeNode current_node = this;
			while (current_node.data != null) {
				if (current_node.type == 2)
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
			int[][] matrix_data = new int[MagicTree.this.magic_squares.order][MagicTree.this.magic_squares.order];
			MagicTreeNode current_node = this;
			while (current_node.data != null) {
				if (current_node.type == 2) {
					matrix_data[current_node.index] = current_node.data;
				}
				current_node = current_node.parent;
			}
			matrix_data[MagicTree.this.magic_squares.order-1] = this.get_row(MagicTree.this.magic_squares.order-1);
			return new SquareMatrix(magic_squares, matrix_data);
		}
		
		public List<SquareMatrix> build() {
			List<SquareMatrix> r = new ArrayList<SquareMatrix>();
			
			Set<Integer> forbidden_elements = this.get_elements();
			
			int[] child_begin = new int[] {};
			if (this.type == 1) {
				child_begin = new int[] {this.data[0]};
			} else if (this.type == 2) {
				child_begin = this.get_column(this.index);
			} else {
				if (MagicTree.this.magic_squares.order == 1 || this.index == MagicTree.this.magic_squares.order-2) {
					// this is a potentially magic square
					SquareMatrix matrix = this.to_matrix();
					if (matrix.is_magic_lazy()) {
						r.add(matrix);
						return r;
					}
				} else {
					child_begin = this.get_row(this.index+1);
				}
			}
			
			for (int i: child_begin) {
				forbidden_elements.remove(i);
			}
			ArrayList<int[]> child_possibilities = sum_permutations_list.query(child_begin, forbidden_elements);
			for (int[] c: child_possibilities) {
				MagicTreeNode child = this.add_child(c);
				r.addAll(child.build());
				this.children.remove(child);
			}
			
			return r;
		}
		
	}
}