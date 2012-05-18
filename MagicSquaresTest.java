import static org.junit.Assert.*;

import org.junit.Test;


public class MagicSquaresTest {

	@Test
	public void testNextPermutation() {
		int[] digits = {3,6,2,5,4,1};
		int[] new_digits = {3,6,4,1,2,5};
		int[] result = MagicSquares.nextPermutation(digits);
		assertArrayEquals(new_digits, result);
	}
	
	@Test
	public void testNextPermutation2() {
		int[] digits = {1,2,3};
		int[] new_digits = {1,3,2};
		int[] result = MagicSquares.nextPermutation(digits);
		assertArrayEquals(new_digits, result);
	}
	
	@Test
	public void testNextPermutation3() {
		int[] digits = {9,8,7,6,5,4,3,2,1};
		int[] new_digits = {1,2,3,4,5,6,7,8,9};
		int[] result = MagicSquares.nextPermutation(digits);
		assertArrayEquals(new_digits, result);
	}
	
	@Test
	public void testMatrixGenerationOrder3() {
		int[] digits = {8,1,6,3,5,7,4,9,2}; 
		MagicSquares obj = new MagicSquares();
		MagicSquares.SquareMatrix m = obj.generateMatrix(digits);
		assertEquals(3, m.size);
		assertEquals(15, m.magicConstant);
		assertTrue(m.is_magic());
		//System.out.println(m.toString());
	}
	
	@Test
	public void testMatrixGenerationOrder4() {
		int[] digits = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		MagicSquares obj = new MagicSquares();
		MagicSquares.SquareMatrix m = obj.generateMatrix(digits);
		assertEquals(4, m.size);
		assertEquals(34, m.magicConstant);
		assertFalse(m.is_magic());
		System.out.println(m.toString());
	}
	
	@Test
	public void testMainOrder2() {
		String[] digits = {"1","2","3","4"}; 
		MagicSquares.main(digits);
	}
	
	@Test
	public void testMainOrder3() {
		String[] digits = {"8","1","6","3","5","7","4","9","2"}; 
		MagicSquares.main(digits);
	}
	
	/*@Test
	public void testMainOrder4() {
		String[] digits = new String[16];
		for (int i = 0 ; i < 16; i++) {
			digits[i] = i + "";
		}
		Hmwk6.main(digits);
	}*/

}
