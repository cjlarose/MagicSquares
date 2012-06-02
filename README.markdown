Magic Squares
=============

Finds magic squares ([Wikipedia](http://en.wikipedia.org/wiki/Magic_square)) of an arbitrary order n. 

It first generates a list of every possible permutation of n integers that sum to the magic constant, which represents all possible rows, columns, and diagonals.  For any one of those permutations, it attempts to construct a matrix whose first row is that permutation and whose first column is a another permutation that begins with the same entry and contains no other duplicate entries.  Then, the algorithm attempts to find sum permutations to fill in the remaining rows and columns.

On my laptop with a 2.7GHz Intel Core i7 processor, the program finds all magic squares of order n in the following runtimes:

<table>
<tr><th>Order</th><th>Mean Runtime</th><th>Std. Dev.</th><th>Distinct Magic Squares</th></tr>
<tr><td>1</td><td>.2ms</td><td>.7ms</td><td>1</td></tr>
<tr><td>2</td><td>.3ms</td><td>.5ms</td><td>0</td></tr>
<tr><td>3</td><td>6.5ms</td><td>2ms</td><td>1</td></tr>
<tr><td>4</td><td>3.5seconds</td><td>.3seconds</td><td>880</td></tr>
</table>

You can generate statistics of the program on your own machine with the generateRuntimeStats() test method.

It's probably not a great idea to try to run this for n >= 5.  It'll work, it just might take a few millennia.  That's my disclaimer. 

Usage: 

```
javac MagicSquares.java
java MagicSquares 3
```
