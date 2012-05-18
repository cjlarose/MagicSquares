import static org.junit.Assert.*;

import org.junit.Test;


public class MagicSquaresTest {

	@Test
	public void testNextPermutation() {
		int[] digits = {3,6,2,5,4,1};
		int[] new_digits = {3,6,4,1,2,5};
		MagicSquares.nextPermutation(digits);
		assertArrayEquals(new_digits, new_digits);
	}
	
	@Test
	public void testNextPermutation2() {
		int[] digits = {1,2,3};
		int[] new_digits = {1,3,2};
		MagicSquares.nextPermutation(digits);
		assertArrayEquals(new_digits, new_digits);
	}
	
	@Test
	public void testMainOrder1() {
		MagicSquares.main(new String[] {"1"});
	}
	
	@Test
	public void testMainOrder2() {
		MagicSquares.main(new String[] {"2"});
	}
	
	@Test
	public void testMainOrder3() {
		MagicSquares.main(new String[] {"3"});
	}
	
	/*
	@Test
	public void testMainOrder4() {
		MagicSquares.main(new String[] {"4"});
	}
	*/

}
