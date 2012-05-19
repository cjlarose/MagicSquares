import java.util.ArrayList;

public class MagicSquares {
	
	static final int NUM_THREADS = 64;
	int order;
	int max;
	int magic_constant;
	long start_time;
	ArrayList<MagicSquares.SquareMatrix> magic_squares = new ArrayList<MagicSquares.SquareMatrix>();
	
	public static void main(String[] args) {
		if (args.length > 0) {
			
			MagicSquares obj = new MagicSquares();
			
			obj.start_time = System.currentTimeMillis();
			
			obj.order = Integer.parseInt(args[0]);
			obj.max = obj.order*obj.order;
			obj.magic_constant = (obj.order*obj.order*obj.order + obj.order) / 2;
			
			System.out.println("Finding all magic matricies of order " + obj.order);
			
            long i = 0;
            long end_i = MagicSquares.factorial(obj.max);
            
            long chunk_size = MagicSquares.factorial(obj.max) / NUM_THREADS;
            
            ArrayList<Thread> threads = new ArrayList<Thread>();
            
            for (int j = 0; j < NUM_THREADS; j++) {
            	long a = i;
            	long b = Math.min(i + chunk_size, end_i);
            	Thread t = new Thread(obj.new MatrixThread(a,b));
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
            
			long end_time = System.currentTimeMillis();
            long runtime = end_time - obj.start_time;
            double runtime_seconds = (double) runtime / (double) 1000;
            System.out.println("Found "+obj.magic_squares.size()+" magic squares in "+String.format("%f", runtime_seconds)+" seconds");
           
        } else {
            System.out.println("Usage: java MagicSquares <order>");
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
	        		thread_message(m, a, b, i);
	        	}
			}
		}
	}
	
	public void thread_message(MagicSquares.SquareMatrix m, long a, long b, long i) {
		long time = System.currentTimeMillis();
		String name = Thread.currentThread().getName();
		System.out.println((time-start_time) + "ms: Magic Matrix "+magic_squares.size()+" found at "+i+" ("+(i-a)+" of "+(b-a+1)+" on "+name+"):");
		System.out.println(m.toString());
	}
	
	public class SquareMatrix {
		int[] data;
		
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

}