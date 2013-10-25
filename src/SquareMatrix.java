import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public final class SquareMatrix {
	/**
	 * 
	 */
	private final MagicSquares magic_squares;
	final int[] data;
	final int[] equivalent_data;
	
	public SquareMatrix(MagicSquares magicSquares, int[] data) {
		magic_squares = magicSquares;
		this.data = data;
		this.equivalent_data = this.get_equivalence_class().get(0);
	}
	
	public SquareMatrix(MagicSquares magicSquares, int[][] data_2d) {
		magic_squares = magicSquares;
		this.data = new int[magic_squares.max];
		for (int m = 0; m < magic_squares.order; m++) 
			for (int n = 0; n < magic_squares.order; n++)
				this.data[magic_squares.order*m+n] = data_2d[m][n];
		this.equivalent_data = this.get_equivalence_class().get(0);
	}
	
	public boolean is_magic_lazy() {
		int right_diagonal_sum = 0;
		for (int i = 0; i < magic_squares.order; i++)
			right_diagonal_sum += this.data[magic_squares.order*i+magic_squares.order-i-1];
		if (right_diagonal_sum != magic_squares.magic_constant)
			return false;
		
		return true;
	}
	
	public boolean is_magic() {
		
		for (int m = 0; m < magic_squares.order; m++) {
			int row_sum = 0;
			for (int n = 0; n < magic_squares.order; n++)
				row_sum += this.data[magic_squares.order * m +n];
			if (row_sum != magic_squares.magic_constant)
				return false;
		}
		
		for (int n = 0; n < magic_squares.order; n++) {
			int col_sum = 0;
			for (int m = 0; m < magic_squares.order; m++)
				col_sum += this.data[magic_squares.order * m + n];
			if (col_sum != magic_squares.magic_constant)
				return false;
		}
		
		int left_diagonal_sum = 0;
		for (int i = 0; i < magic_squares.order; i++)
			left_diagonal_sum += this.data[magic_squares.order*i+i];
		if (left_diagonal_sum != magic_squares.magic_constant)
			return false;
		
		int right_diagonal_sum = 0;
		for (int i = 0; i < magic_squares.order; i++)
			right_diagonal_sum += this.data[magic_squares.order*i+magic_squares.order-i-1];
		if (right_diagonal_sum != magic_squares.magic_constant)
			return false;
		
		return true;
	}
	
	public String toString() {
		// old_data[m][n] = new_data[order*m + n]
		String max_term = magic_squares.max + "";
		int max_term_size = max_term.length();
		
		String result = "";
		
		String border = "+";
		String border_between = "|";
		for (int i = 0; i < magic_squares.order; i++) {
			border += "--" + MagicSquares.str_repeat("-", max_term_size);
			border_between += "--" + MagicSquares.str_repeat("-", max_term_size);
			if (i != magic_squares.order - 1 ) {
				border += "-";
				border_between += "+";
			}
		}
		border += "+\n";
		border_between += "|\n";
		
		result += border;
		
		for (int m = 0; m < magic_squares.order; m++) {
			result += "|";
			for (int n = 0; n < magic_squares.order; n++) {
				int padding_right = max_term_size - (data[magic_squares.order*m+n] + "").length();
				result += " " + data[magic_squares.order*m+n] + MagicSquares.str_repeat(" ", padding_right)+ " |";
			}
			result += "\n";
			if (m != magic_squares.order - 1)
				result += border_between;
		}
		result += border;
		return result;
	}
	
	public void rotate_right(int[] data2) {
		int[] new_data = new int[magic_squares.max];
		for (int i = 0; i < data2.length; i++) {
			int m = i / magic_squares.order;
			int n = i % magic_squares.order;
			new_data[magic_squares.order*n+magic_squares.order-m-1] = data2[i];
		}
		for (int i = 0; i < magic_squares.max; i++) 
			data2[i] = new_data[i];
	}
	
	public void transpose(int[] data2) {
		int[] new_data = new int[magic_squares.max];
		for (int i = 0; i < data2.length; i++) {
			int m = i / magic_squares.order;
			int n = i % magic_squares.order;
			new_data[magic_squares.order*n+m] = data2[i];
		}
		for (int i = 0; i < magic_squares.max; i++) 
			data2[i] = new_data[i];
	}
	
	public ArrayList<int[]> get_equivalence_class() {
		ArrayList<int[]> r = new ArrayList<int[]>();
		
		int[] data_copy = this.data.clone();
		for (int i = 0; i < 4; i++) {
			r.add(data_copy.clone());
			rotate_right(data_copy);
		}
		transpose(data_copy);
		for (int i = 0; i < 4; i++) {
			r.add(data_copy.clone());
			rotate_right(data_copy);
		}
		
		Collections.sort(r, magic_squares.int_arr_comparator);
		return r;
	}
	
	@Override
	public boolean equals(Object other) {
		return this.hashCode() == ((SquareMatrix) other).hashCode() && 
			Arrays.equals(this.equivalent_data, ((SquareMatrix) other).equivalent_data);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(this.equivalent_data);
	}

}