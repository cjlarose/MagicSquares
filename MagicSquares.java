import java.util.Arrays;

public class MagicSquares {
	
	int order;
	int max;
	int magic_constant;
	
	public static void main(String[] args) {
		if (args.length > 0) {
			
			MagicSquares obj = new MagicSquares();
			
			long start_time = System.currentTimeMillis();
			
			obj.order = Integer.parseInt(args[0]);
			obj.max = obj.order*obj.order;
			obj.magic_constant = (obj.order*obj.order*obj.order + obj.order) / 2;
			
			System.out.println("Finding all magic matricies of order " + obj.order);
			
			int[] begin = new int[obj.max];
			int[] end = new int[obj.max];
            for (int i=0; i<obj.max; i++) {
                begin[i] = i + 1;
                end[i] = obj.max - i;
            }
            
            int[] current_permutation = begin;
            int count_magic = 0;
            
            
            while (true) {
            	MagicSquares.SquareMatrix m = obj.new SquareMatrix(current_permutation);
            	if (m.is_magic()) {
            		count_magic++;
            		System.out.println(m.toString());
            	}
            	if (Arrays.equals(current_permutation, end))
            		break;
            	MagicSquares.nextPermutation(current_permutation);
            }
            
			long end_time = System.currentTimeMillis();
            long runtime = end_time - start_time;
            //System.out.println(runtime);
            double runtime_seconds = (double) runtime / (double)1000;
            System.out.println("Found "+count_magic+" magic squares in "+String.format("%f", runtime_seconds)+" seconds");
            //System.out.println("Found "+count_magic+" magic squares");
           
        } else {
            System.out.println("Usage: java MagicSquares <order>");
        }
	}
	
	public class SquareMatrix {
		
		int[][] data;
		
		public SquareMatrix(int[] single_dimentional_data) {
			this.data = new int[order][order];
			for (int i = 0; i < single_dimentional_data.length; i++) {
				int n = i % order;
				int m = i/order;
				this.data[m][n] = single_dimentional_data[i];
			}
		}

		private int[] get_col(int n) {
			int[] result = new int[order];
			for (int m = 0; m < order; m++)
				result[m] = this.data[m][n];
			return result;
		}

		private int[] get_left_diagonal() {
			int[] result = new int[order];
			for (int i = 0; i < order; i++)
				result[i] = this.data[i][i];
			return result;
		}
		

		private int[] get_right_diagonal() {
			int[] result = new int[order];
			for (int i = 0; i < order; i++)
				result[i] = this.data[i][order - 1 - i];
			return result;
		}
		

		public boolean is_magic() {
			for (int m = 0; m < order; m++) {
				if (array_sum(this.data[m]) != magic_constant)
					return false;
			}
			for (int n = 0; n < order; n++) {
				if (array_sum(this.get_col(n)) != magic_constant)
					return false;
			}
			if (array_sum(this.get_left_diagonal()) != magic_constant)
				return false;
			if (array_sum(this.get_right_diagonal()) != magic_constant)
				return false;
			return true;
		}
		

		public String toString() {
			String max_term = order * order + "";
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
					int padding_right = max_term_size - (data[m][n] + "").length();
					result += " " + data[m][n] + MagicSquares.str_repeat(" ", padding_right)+ " |";
				}
				result += "\n";
				if (m != order - 1)
					result += border_between;
			}
			result += border;
			return result;
		}

		
	}
	
	private static int array_sum(int[] arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		return sum;
	}
	
	private static String str_repeat(String str, int repeat) {
		String result = "";
		for (int i = 0; i < repeat; i++) {
			result += str;
		}
		return result;
	}

	public static void nextPermutation(int[] digits) {
		
		int n = digits.length - 1; 
		
		int j = n - 1;
		while (digits[j] > digits[j+1]) {
			j--;
		}
		
		int k = n;
		while (digits[j] > digits[k])
			k--;
		
		int temp = digits[j];
		digits[j] = digits[k];
		digits[k] = temp;
		
		int r = n;
		int s = j + 1;
		
		while (r > s) {
			temp = digits[r];
			digits[r] = digits[s];
			digits[s] = temp;
			r--;
			s++;
		}
		
	}

}