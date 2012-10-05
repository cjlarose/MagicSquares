import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class MagicTreeBuilder {
	
	private final MagicSquares magic_squares;
	private List<MagicTree> trees;
	private final SumPermutationsList sum_permutations_list;
	
	public MagicTreeBuilder(MagicSquares magicSquares, SumPermutationsList sum_permutations_list) {
		this.magic_squares = magicSquares;
		this.sum_permutations_list = sum_permutations_list;

		this.trees = new ArrayList<MagicTree>();
		for (Entry<Integer, List<int[]>> e: sum_permutations_list.set_map.entrySet())
			this.trees.add(new MagicTree(e.getKey().intValue(), e.getValue()));
	}
	
	public class NodeBuilderTask extends RecursiveTask<Set<SquareMatrix>> {

		private static final long serialVersionUID = 7218910311926378380L;
		
		private List<MagicTree> nodes;
		public Set<SquareMatrix> result = new HashSet<SquareMatrix>();
		
		public NodeBuilderTask(List<MagicTree> nodes) {
			this.nodes = nodes;
		}
		
		@Override
		public Set<SquareMatrix> compute() {
			if (nodes.size() <= 22) {
				for (MagicTree node: nodes) {
					result.addAll(node.build());
				}
			} else {
				
				int half = nodes.size() / 2;
				
				List<MagicTree> upper_half = nodes.subList(0, half);
				List<MagicTree> lower_half = nodes.subList(half, nodes.size());
				
				NodeBuilderTask worker1 = new NodeBuilderTask(upper_half);
				NodeBuilderTask worker2 = new NodeBuilderTask(lower_half);
				
				worker1.fork();
				result.addAll(worker2.compute());
				result.addAll(worker1.join());
			}
			//System.out.println("Task found "+result.size()+" magic matrices");
			return result;
		}
	}
	
	public void build_tree() {
		int processors = Runtime.getRuntime().availableProcessors();
		
		NodeBuilderTask task = new NodeBuilderTask(trees);
		ForkJoinPool pool = new ForkJoinPool(processors);
		pool.invoke(task);
		
		Set<SquareMatrix> magic_squares = task.result;
		System.out.println("Computed Result: " + magic_squares.size());
	}
	
	public class MagicTree {
		int key;
		List<MagicTreeNode> children;
		
		public MagicTree(int key, List<int[]> diagonals) {
			this.key = key;
			this.children = new ArrayList<MagicTreeNode>();
			for (int[] d: diagonals)
				this.children.add(new MagicTreeNode(d));
		}
		
		public List<SquareMatrix> build() {
			List<SquareMatrix> r = new ArrayList<SquareMatrix>();
			for (MagicTreeNode n: this.children)
				r.addAll(n.build());
			return r;
		}
	}
	
	public enum ChildType {
		DIAGONAL, ROW, COLUMN
	}
	
	public class MagicTreeNode {
		public int[] data;
		public ArrayList<MagicTreeNode> children = new ArrayList<MagicTreeNode>();
		public ChildType type; // 0 for root, 1 for main diagonal, 2 for row, 3 for column.
		public int index;
		public MagicTreeNode parent;
		
		public MagicTreeNode(int[] data) {
			this.type = ChildType.DIAGONAL;
			this.data = data;
		}
		
		public MagicTreeNode(int[] data, ChildType type, int index, MagicTreeNode parent) {
			this.data = data;
			this.type = type;
			this.index = index;
			this.parent = parent;
		}
		
		public MagicTreeNode add_child(int[] data) {
			ChildType child_type;
			int child_index;
			switch (this.type) {
				case DIAGONAL: 
					child_type = ChildType.ROW;
					child_index = 0;
				break;
				case ROW: 
					child_type = ChildType.COLUMN;
					child_index = this.index;
				break;
				case COLUMN: 
					child_type = ChildType.ROW;
					child_index = this.index+1;
				break;
				default: 
					child_type = null;
					child_index = -1;
			}
			MagicTreeNode child = new MagicTreeNode(data, child_type, child_index, this);
			this.children.add(child);
			return child;
		}
		
		public int[] get_main_diagonal() {
			MagicTreeNode current_node = this;
			while (current_node.data != null) {
				if (current_node.type == ChildType.DIAGONAL)
					return current_node.data;
				current_node = current_node.parent;
			}
			return null;
		}
		
		public int[] get_row(int m) {
			int[] r = new int[this.index+2];
			MagicTreeNode current_node = this;
			while (current_node != null) {
				if (current_node.type == ChildType.COLUMN) 
					r[current_node.index] = current_node.data[m];
				else if (current_node.type == ChildType.DIAGONAL)
					r[m] = current_node.data[m];
				current_node = current_node.parent;
			}
			return r;
		}
		
		public int[] get_column(int n) {
			int[] r = new int[this.index+1];
			MagicTreeNode current_node = this;
			while (current_node != null) {
				if (current_node.type == ChildType.ROW)
					r[current_node.index] = current_node.data[n];
				current_node = current_node.parent;
			}
			return r;
		}
	
		public int get_elements() {
			int r = 0;
			MagicTreeNode current_node = this;
			while (current_node != null) {
				for (int e: current_node.data) 
					r |= 1 << e;
				current_node = current_node.parent;
			}
			return r;
		}
		
		public SquareMatrix to_matrix() {
			int[][] matrix_data = new int[MagicTreeBuilder.this.magic_squares.order][MagicTreeBuilder.this.magic_squares.order];
			MagicTreeNode current_node = this;
			while (current_node != null) {
				if (current_node.type == ChildType.ROW) {
					matrix_data[current_node.index] = current_node.data;
				}
				current_node = current_node.parent;
			}
			matrix_data[MagicTreeBuilder.this.magic_squares.order-1] = this.get_row(MagicTreeBuilder.this.magic_squares.order-1);
			return new SquareMatrix(magic_squares, matrix_data);
		}
		
		public List<SquareMatrix> build() {
			List<SquareMatrix> r = new ArrayList<SquareMatrix>();
			
			int forbidden_elements = this.get_elements();
			
			int[] child_begin = new int[] {};
			if (this.type == ChildType.DIAGONAL) {
				child_begin = new int[] {this.data[0]};
			} else if (this.type == ChildType.ROW) {
				child_begin = this.get_column(this.index);
			} else {
				if (MagicTreeBuilder.this.magic_squares.order == 1 || this.index == MagicTreeBuilder.this.magic_squares.order-2) {
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
			
			for (int i: child_begin)
				forbidden_elements ^= 1 << i;
				
			List<int[]> child_possibilities = sum_permutations_list.query(child_begin, forbidden_elements);
			for (int[] c: child_possibilities) {
				MagicTreeNode child = this.add_child(c);
				r.addAll(child.build());
				this.children.remove(child);
			}
			
			return r;
		}
		
	}
}