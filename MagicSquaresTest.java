import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	public void testMainOrder4() {
		//MagicSquares.main(new String[] {"4"});
	}
	
	@Test
	public void testNumThreads() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 1; i <= 128; i++) {
			MagicSquares obj = new MagicSquares();
			obj.NUM_THREADS = i;
			obj.init(3);
			long end_time = System.currentTimeMillis();
			int runtime = (int) (end_time - obj.start_time);
			System.out.println("Threads: " + i + ", Time: " + runtime + "ms");
			map.put(i, runtime);
		}
		
		Integer min = Collections.min(map.values());
		ArrayList<Map.Entry<Integer,Integer>> min_entries = new ArrayList<Map.Entry<Integer,Integer>>();
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet())
		    if (min == entry.getValue())
		        min_entries.add(entry);

		System.out.println("Fastest time was "+min+"ms and occured when there were ");
		for (int i = 0; i < min_entries.size(); i++) {
			System.out.println(min_entries.get(i).getKey() + " threads");
		}
	}

}
