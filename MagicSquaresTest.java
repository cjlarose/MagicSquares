import static org.junit.Assert.*;

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
		MagicSquares obj = new MagicSquares();
		obj.max = 4;
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
	public void testNumThreads() {
		MagicSquares obj = new MagicSquares();
		obj.testNumThreads();
	}
	
	@Test
	public void testMainOrder3Threads() {
		MagicSquares.main(new String[] {"3", "threads"});
	}

}
