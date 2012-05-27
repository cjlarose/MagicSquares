Magic Squares
=============

Finds magic squares ([Wikipedia](http://en.wikipedia.org/wiki/Magic_square)) of an arbitrary order n. The program includes several different ways of approaching the problem, each with different runtimes and output redundancies.

The first method, init(), is the most basic of the approaches.  It loops through every possible permutation of the intergers [1..n^2] in lexiographic order, constructs an n x n matrix with the permutation filling in the matrix left-to-right, top-to-bottom, then testing if the square is magic by adding each row, column, and diagonal and comparing each sum to the magic constant.

The second method, init_sum_combinations_dumb(), takes a slightly more foreward-thinking process. It first generates a list of every possible permutation of n integers that sum to the magic constant, which represents all possible rows, columns, and diagonals. It then constructs all possible permutations of those permutations and interprets them as rows of an n x n matrix, and finally checks if the square is magic.

The final method is not yet implemented, but should start the same way as the previous method, by generating the list of possible sum permutations. For any one of those permutations, it attempts to construct a matrix whose first row is that permutation and whose first column is a another permutation that begins with the same entry and contains no other duplicate entries.  Then, for each remaining row, the algorithm attempts to find sum permutations to fill in the remaining rows and columns.

Usage:
	javac MagicSquares.java
	java MagicSquares 3
