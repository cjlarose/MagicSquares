import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class MagicSquaresTest {
	
	@Test
	public void testFactorial3() {
		int n = 3;
		assertEquals(MagicSquares.factorial(n), 6);
	}
	
	@Test
	public void testFactorial4() {
		int n = 4;
		assertEquals((int) MagicSquares.factorial(n),24);
	}
	
	@Test
	public void testFactorial16() {
		int n = 16;
		assertEquals(MagicSquares.factorial(n), 20922789888000L);
	}
	
	@Test
	public void testGetPermutation() {
		MagicSquares obj = new MagicSquares(2);
		int[] result0 = obj.get_permutation(0L);
		assertArrayEquals(result0, new int[] {1,2,3,4});
		
		int[] result2 = obj.get_permutation(5L);
		assertArrayEquals(result2, new int[] {1,4,3,2});
		
		int[] result23 = obj.get_permutation(23L);
		assertArrayEquals(result23, new int[] {4,3,2,1});
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
	
	@Test
	public void testMatrixRotation() {
		MagicSquares obj = new MagicSquares(4);
		MagicSquares.SquareMatrix m = obj.new SquareMatrix(new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
		m.rotate_right();
		assertArrayEquals(m.data, new int[] {13,9,5,1,14,10,6,2,15,11,7,3,16,12,8,4});
		m.rotate_right();
		assertArrayEquals(m.data, new int[] {16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1});
		m.rotate_right();
		assertArrayEquals(m.data, new int[] {4,8,12,16,3,7,11,15,2,6,10,14,1,5,9,13});
		m.rotate_right();
		assertArrayEquals(m.data, new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
	}
	
	@Test
	public void testMatrixTransposition() {
		MagicSquares obj = new MagicSquares(4);
		MagicSquares.SquareMatrix m = obj.new SquareMatrix(new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
		m.transpose();
		assertArrayEquals(m.data, new int[] {1,5,9,13,2,6,10,14,3,7,11,15,4,8,12,16});
		m.transpose();
		assertArrayEquals(m.data, new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
	}

	@Test
	public void testMatrixEquals() {
		MagicSquares obj = new MagicSquares(4);
		MagicSquares.SquareMatrix m1 = obj.new SquareMatrix(new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
		int[][] e = new int[][] {
				{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16},
				{13,9,5,1,14,10,6,2,15,11,7,3,16,12,8,4},
				{16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1},
				{4,8,12,16,3,7,11,15,2,6,10,14,1,5,9,13},
				{1,5,9,13,2,6,10,14,3,7,11,15,4,8,12,16},
				{13,14,15,16,9,10,11,12,5,6,7,8,1,2,3,4},
				{16,12,8,4,15,11,7,3,14,10,6,2,13,9,5,1},
				{4,3,2,1,8,7,6,5,12,11,10,9,16,15,14,13}
		};
		for (int i = 0; i < e.length; i++) {
			MagicSquares.SquareMatrix m2 = obj.new SquareMatrix(e[i]);
			assertTrue(m1.equals(m2));
		}
	}
	
	/*@Test
	public void testNumThreads() {
		MagicSquares obj = new MagicSquares();
		obj.testNumThreads();
	}
	
	@Test
	public void testMainOrder3Threads() {
		MagicSquares.main(new String[] {"3", "threads"});
	}*/

}
