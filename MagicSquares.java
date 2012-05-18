/*=============================================================================
 |   Assignment:  Program #1:  Hmwk6
 |       Author:  Chris LaRose (cjlarose@email.arizona.edu)
 |       Grader:  Vicki
 |
 |       Course:  245
 |   Instructor:  L. McCann
 |     Due Date:  2012-04-06 14:00:00
 |
 |  Description:  Finds all possible magic square matrices of a given order n, 
 |                for n >= 2. A matrix is considered magic if the sum of the 
 |                elements in each row, column, and diagonal is constant. 
 |                Beginning with a given permutation of the elements in range 
 |                1..n^2, the program iterates through and wraps around all 
 |                possible permutations. Each permutation is constructed into a
 |                n by n matrix, filled from top left to bottom right, and 
 |                evaluated to determine if it is "magic". The program's output 
 |                consists of the first magic square found and a count of all 
 |                magic squares found.
 |                
 | Deficiencies:  No unsatisfied requirements. The program addresses the
 |                the problem of testing n order square matrixes, not just
 |                order three, so if anything, it's too awesome. However, 
 |                because of the inherit inefficiency of looping through 
 |                every possible permutation, the script takes forever to 
 |                complete for order >= 4, which kind of defeats the purpose.
 |                Anyways, appreciate the beauty and elegance of the general 
 |                solution.
 *===========================================================================*/
import java.util.Arrays;

public class MagicSquares {
	
	public static void main(String[] args) {
		if (args.length > 0) {
			double order = Math.sqrt(args.length);
			if (order %1 == 0) {
				int[] nums = new int[args.length];
	            for (int i=0; i<args.length; i++) {
	                nums[i] = Integer.parseInt(args[i]);
	            }
	            
	            int[] current_permutation = MagicSquares.nextPermutation(nums);
	            int count_magic = 0;
	            MagicSquares.SquareMatrix first_magic = null;
	            MagicSquares obj = new MagicSquares();
	            
	            while (true) {
	            	
	            	MagicSquares.SquareMatrix m = obj.generateMatrix(current_permutation);
	            	
	            	if (m.is_magic()) {
	            		if (count_magic == 0)
	            			first_magic = m;
	            		count_magic++;
	            		//System.out.println(m.toString());
	            	}
	            	
	            	if (Arrays.equals(current_permutation, nums))
	            		break;
	            	
	            	current_permutation = MagicSquares.nextPermutation(current_permutation);
	            }
	            
	            if (count_magic > 0) {
		            System.out.println("The first magic square following your input:");
		            System.out.println(first_magic.toString());
		            System.out.println("Total count of magic squares: " + count_magic);
	            } else {
	            	System.out.println("No magic squares found.");
	            }
			} else {
				System.out.println("Number of arguments must be a perfect square");
			}
        } else {
            System.out.println("Usage: java Hmwk6 <arguments>");
        }
	}
	
	/*+----------------------------------------------------------------------
	 ||
	 ||  Class Hmwk6.SquareMatrix
	 ||
	 ||         Author:  Chris LaRose
	 ||
	 ||        Purpose:  Constructs a square matrix and includes methods to 
	 ||                  determine if it is magic, that is, the sum of the 
	 ||                  elements in each row, column, and diagonal is constant.
	 ||
	 ||  Inherits From:  None
	 ||
	 ||     Interfaces:  None
	 ||
	 |+-----------------------------------------------------------------------
	 ||
	 ||      Constants:  size -- the number of rows n (or columns) of the square
	 ||                  matrix
	 ||                  magicConstant -- the sum of each row, column, and 
	 ||                  diagonal of a magic matrix of order n
	 ||                  data -- two-dimensional array that stores the contents
	 ||                  of each cell in the matrix
	 ||
	 |+-----------------------------------------------------------------------
	 ||
	 ||   Constructors:  The only constructor SquareMatrix(...) takes a single-
	 ||                  dimensional array of integers of length n^2, which
	 ||                  represents the contents of the matrix.
	 ||
	 ||  Class Methods:  None
	 ||
	 ||  Inst. Methods:  public boolean is_magic()
	 ||                  public String toString() 
	 ||
	 ++-----------------------------------------------------------------------*/
	public class SquareMatrix {
		
		int size;
		int magicConstant;
		int[][] data;
		
		public SquareMatrix(int[] single_dimentional_data) {
			this.size = (int) Math.sqrt(single_dimentional_data.length);
			this.data = new int[size][size];
			this.magicConstant = (size*size*size + size)/2;
			for (int i = 0; i < single_dimentional_data.length; i++) {
				int n = i % size;
				int m = i/size;
				this.data[m][n] = single_dimentional_data[i];
			}
		}
		
	    /*---------------------------------------------------------------------
	    |  Method get_col
	    |
	    |  Purpose: generates an array with the elements of a matrix's nth 
	    |      column.
	    |
	    |  Pre-condition:  0 < n <= size
	    |
	    |  Post-condition: None
	    |
	    |  Parameters:
	    |      n -- The column number (counting from the left (0) to the right
	    |          (size))
	    |
	    |  Returns:  A single dimensional array of length size containing the
	    |      elements in the nth column of the matrix
	    *-------------------------------------------------------------------*/
		private int[] get_col(int n) {
			int[] result = new int[this.size];
			for (int m = 0; m < size; m++)
				result[m] = this.data[m][n];
			return result;
		}
		
	    /*---------------------------------------------------------------------
	    |  Method get_left_diagonal
	    |
	    |  Purpose:  Produces an array with the elements of a matrix's diagonal
	    |      from top-left to the bottom-right
	    |
	    |  Pre-condition:  None
	    |
	    |  Post-condition: None
	    |
	    |  Parameters: None
	    |
	    |  Returns:  A single-dimensional array containing the contents of the
	    |      diagonal from (0,0) to (size, size)
	    *-------------------------------------------------------------------*/
		private int[] get_left_diagonal() {
			int[] result = new int[this.size];
			for (int i = 0; i < size; i++)
				result[i] = this.data[i][i];
			return result;
		}
		
	    /*---------------------------------------------------------------------
	    |  Method get_right_diagonal
	    |
	    |  Purpose:  Produces an array with the elements of a matrix's diagonal
	    |      from top-right to the bottom-left
	    |
	    |  Pre-condition:  None
	    |
	    |  Post-condition: None
	    |
	    |  Parameters: None
	    |
	    |  Returns:  A single-dimensional array containing the contents of the
	    |      diagonal from (size,0) to (0, size)
	    *-------------------------------------------------------------------*/
		private int[] get_right_diagonal() {
			int[] result = new int[this.size];
			for (int i = 0; i < size; i++)
				result[i] = this.data[i][this.size - 1 - i];
			return result;
		}
		
	    /*---------------------------------------------------------------------
	    |  Method array_sum
	    |
	    |  Purpose:  Sums the ints in a given int array
	    |
	    |  Pre-condition:  None
	    |
	    |  Post-condition: None
	    |
	    |  Parameters:
	    |      arr -- The int array to sum
	    |
	    |  Returns:  A int representing the sum of all elements in array arr
	    *-------------------------------------------------------------------*/
		private int array_sum(int[] arr) {
			int sum = 0;
			for (int i = 0; i < arr.length; i++) {
				sum += arr[i];
			}
			return sum;
		}
		
	    /*---------------------------------------------------------------------
	    |  Method is_magic
	    |
	    |  Purpose:  Determines if the matrix is magic by calculating the sums 
	    |      of each row, column, and diagonal and checking if they're each 
	    |      each to the matrix's magic constant
	    |
	    |  Pre-condition:  None
	    |
	    |  Post-condition: None
	    |
	    |  Parameters:  None
	    |
	    |  Returns:  Boolean. True if the matrix is magic. False otherwise.
	    *-------------------------------------------------------------------*/
		public boolean is_magic() {
			for (int m = 0; m < this.size; m++) {
				if (array_sum(this.data[m]) != this.magicConstant)
					return false;
			}
			for (int n = 0; n < this.size; n++) {
				if (array_sum(this.get_col(n)) != this.magicConstant)
					return false;
			}
			if (array_sum(this.get_left_diagonal()) != this.magicConstant)
				return false;
			if (array_sum(this.get_right_diagonal()) != this.magicConstant)
				return false;
			return true;
		}
		
	    /*---------------------------------------------------------------------
	    |  Method toString
	    |
	    |  Purpose:  Generates a sting representation of the matrix.
	    |
	    |  Pre-condition:  None
	    |
	    |  Post-condition: None
	    |
	    |  Parameters: None
	    |
	    |  Returns:  String representing the matrix displayed as a box.
	    *-------------------------------------------------------------------*/
		public String toString() {
			String max_term = size * size + "";
			int max_term_size = max_term.length();
			
			String result = "";
			
			String border = "+";
			String border_between = "|";
			for (int i = 0; i < this.size; i++) {
				border += "--" + this.str_repeat("-", max_term_size);
				border_between += "--" + this.str_repeat("-", max_term_size);
				if (i != this.size - 1 ) {
					border += "-";
					border_between += "+";
				}
			}
			border += "+\n";
			border_between += "|\n";
			
			result += border;
			
			for (int m = 0; m < size; m++) {
				result += "|";
				for (int n = 0; n < size; n++) {
					int padding_right = max_term_size - (data[m][n] + "").length();
					result += " " + data[m][n] + this.str_repeat(" ", padding_right)+ " |";
				}
				result += "\n";
				if (m != size - 1)
					result += border_between;
			}
			result += border;
			return result;
		}
		
	    /*---------------------------------------------------------------------
	    |  Method str_repeat
	    |
	    |  Purpose:  Generates a repeatedly-concatenated string
	    |
	    |  Pre-condition:  None
	    |
	    |  Post-condition: None
	    |
	    |  Parameters:
	    |      str -- The string to repeat
	    |      repeat -- The number of time to repeat the string
	    |
	    |  Returns:  String created by repeat concatenations of the string str
	    *-------------------------------------------------------------------*/
		private String str_repeat(String str, int repeat) {
			String result = "";
			for (int i = 0; i < repeat; i++) {
				result += str;
			}
			return result;
		}
	}
	
    /*---------------------------------------------------------------------
    |  Method generateMatix
    |
    |  Purpose:  Returns an object of type SquareMatrix given a single-
    |            dimensional array of integers
    |
    |  Pre-condition:  The number of supplied ints should be a perfect 
    |      square.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      data -- Single dimensional array of ints representing a single 
    |          permutation of ints 1..n^2 with which to generate the matrix
    |
    |  Returns:  SquareMatrix m -- the generated matrix object
    *-------------------------------------------------------------------*/
	public SquareMatrix generateMatrix(int[] data) {
		SquareMatrix m = this.new SquareMatrix(data);
		return m;
	}

    /*---------------------------------------------------------------------
    |  Method nextPermutation
    |
    |  Purpose:  Implements an algorithm to provide the lexicographically 
    |      next permutation relative to a given permutation. If the
    |      permutation is n^2, n^2-1, ..., 2, 1, the algorithm returns
    |      the ints in increasing order (wraparound implementation).
    |
    |  Pre-condition:  The permutation should contain ints 1..n^2
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      digits -- Single dimensional array of ints representing a single 
    |          permutation of ints 1..n^2
    |
    |  Returns:  Single dimensional array of ints representing the 
    |      permutation directly following the given one.
    *-------------------------------------------------------------------*/
	public static int[] nextPermutation(int[] digits) {
		int[] new_digits = new int[digits.length];
		for (int i = 0; i < digits.length; i++)
			new_digits[i] = digits[i];
		
		int n = new_digits.length - 1; 
		
		int j = n - 1;
		while (digits[j] > digits[j+1]) {
			if (j == 0) {
				for (int i = 0; i < digits.length; i++)
					new_digits[i] = digits[digits.length - 1 - i];
				return new_digits;
			}
			j--;
		}
		
		int k = n;
		while (digits[j] > digits[k])
			k--;
		
		int temp = new_digits[j];
		new_digits[j] = new_digits[k];
		new_digits[k] = temp;
		
		int r = n;
		int s = j + 1;
		
		while (r > s) {
			temp = new_digits[r];
			new_digits[r] = new_digits[s];
			new_digits[s] = temp;
			r--;
			s++;
		}
		
		return new_digits;
	}

}