import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;


public class MagicSquaresTest {
	
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
		MagicSquares.main(new String[] {"4"});
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
	
	
	@Test
	public void testGetSumCombintations() {
		MagicSquares obj = new MagicSquares(3);
		ArrayList<int[]> result = obj.get_sum_combinations();
	}
	
	@Test
	public void testInitBySumCombinations() {
		MagicSquares obj = new MagicSquares(3);
		obj.init_sum_combinations();
	}
	
	@Test
	public void testGetSubsetByValues() {
		MagicSquares obj = new MagicSquares(3);
		MagicSquares.SumPermutationsList sum_permutations_list = obj.new SumPermutationsList();
		ArrayList<int[]> r = sum_permutations_list.get_subset_by_values(new int[][] {new int[] {0,6}, new int[] {2,8}});
		for (int i = 0; i < r.size(); i++)
			System.out.println(Arrays.toString(r.get(i)));
	}

}
