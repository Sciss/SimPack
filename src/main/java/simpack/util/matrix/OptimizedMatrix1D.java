/*
 * $Id: OptimizedMatrix1D.java 757 2008-04-17 17:42:53Z kiefer $
 *
 * Copyright (C) 2004-2008 by the Dynamic and Distributed Information Systems
 * Group at the University of Zurich, Switzerland
 *
 * All inquiries regarding the copyrights of this project should be addressed
 * to:
 *
 * Prof. Abraham Bernstein, PhD
 * Dynamic and Distributed Information Systems Group
 * Department of Informatics
 * University of Zurich
 * Binzmuehlestrasse 14
 * CH-8050 Zurich, Switzerland
 *
 * SimPack is licensed under the GNU Lesser General Public License
 *
 * Details can be found at http://www.gnu.org/licenses/lgpl.html or at
 * http://www.ifi.uzh.ch/ddis/simpack.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA
 *
 */
package simpack.util.matrix;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The OptimizedMatrix1D is a ever-growing one dimensional matrix. It is
 * optimized for sparse matrices, thus matrices that have many zero values. This
 * implementation tries to find a compromise between size and quickness.
 * <p>
 * The size is at most 1.5 times the real size. Real size would be all the
 * non-zero values. This class is designed for quick iteration through the
 * non-zero values, so that the methods zSum, sumOfSquares and zDotProduct can
 * be computed in O(n) where n is the number of non-zeros.
 * <p>
 * The method getQuick usually needs approximately O(log n) time, setQuick about
 * O(log n + n/2). But as this matrix is optimized for the use with StringTFIDF,
 * setQuick can have O(1), although this happens only in one special case: In
 * StringTFIDF you normally iterate over the rows and insert then some value, e.g.
 * setQuick(0, 0.6), setQuick(1, 0.75), setQuick(2, 0.2), etc. With every
 * setQuick the position where you insert a value is higher than before. That's
 * why we can check at the beginning of the method setQuick, if the inserted
 * position is higher than any else before, and then just put it at the end of
 * the array.
 * <p>
 * Use trim() before you save the matrix to disk, the arrays will be trimmed to
 * its real size.
 * 
 * @author Silvan Hollenstein
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */

public class OptimizedMatrix1D implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The size of this matrix.
	 */
	private final int size;

	/**
	 * This array holds the different positions of the values in this matrix. It
	 * will always be ordered together with <code>values</code>
	 */
	private int[] positions;

	/**
	 * This array holds the different values corresponding to the positions in
	 * <code>positions</code>. It will always be ordered together with
	 * <code>positions</code>
	 */
	private double[] values;

	/**
	 * This value is one higher than the highest possible position. At the
	 * beginning the array <code>positions</code> will be filled with this
	 * value.
	 */
	private final int upperDelimiter;

	/**
	 * realSize is the number of non-zero elements. In other words: the number
	 * of elements in <code>positions</code> that are smaller than
	 * upperDelimiter.
	 */
	private int realSize = 0;

	/**
	 * Constructor.
	 * <p>
	 * Constructs 1-dimensional matrix of size <code>size</code>.
	 * 
	 * @param size
	 *            of matrix
	 */
	public OptimizedMatrix1D(int size) {
		this.size = size;
		upperDelimiter = size;
		positions = new int[4];
		values = new double[4];
		Arrays.fill(positions, upperDelimiter);
	}

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param pos
	 * @param val
	 * @param sizeMatrix
	 * @param sizeNonZeros
	 */
	public OptimizedMatrix1D(int[] pos, double[] val, int sizeMatrix,
			int sizeNonZeros) {
		positions = pos;
		values = val;
		size = sizeMatrix;
		upperDelimiter = sizeMatrix;
		realSize = sizeNonZeros;
	}

	/**
	 * @return the size of this matrix
	 */
	public int size() {
		return size;
	}

	/**
	 * Copies the values in <code>positions</code> and <code>values</code>
	 * one to the right, starting from <code>fromIndex</code>
	 * 
	 * @param fromIndex
	 */
	private void moveOneBack(int fromIndex) {
		System.arraycopy(positions, fromIndex, positions, fromIndex + 1,
				realSize - fromIndex);
		System.arraycopy(values, fromIndex, values, fromIndex + 1, realSize
				- fromIndex);
	}

	/**
	 * Ensures that the size of the arrays is at least of the specified value
	 * given by <code>minCapacity</code>.
	 * 
	 * @param minCapacity
	 */
	private void ensureCapacity(int minCapacity) {
		if (minCapacity > positions.length) {
			int[] temp = new int[(positions.length * 3) / 2 + 1];
			double[] tempDouble = new double[(positions.length * 3) / 2 + 1];
			Arrays.fill(temp, upperDelimiter);
			System.arraycopy(positions, 0, temp, 0, positions.length);
			System.arraycopy(values, 0, tempDouble, 0, values.length);
			positions = temp;
			values = tempDouble;
		}
	}

	/**
	 * Sets a value into the 1D-matrix at the specified position. No check is
	 * done if the position is out of bounds.
	 * 
	 * @param i
	 *            the position
	 * @param val
	 *            the value
	 */
	public void setQuick(int i, double val) {
		ensureCapacity(realSize + 1);
		// this is a shortcut, useful when you know, the inserted value will
		// be at the highest position of all the values already inserted
		if (realSize > 0) {
			if (i > positions[realSize - 1]) {
				positions[realSize] = i;
				values[realSize] = val;
				realSize++;
				return;
			}
		}
		insertValueAtPos(i, val);
	}

	/**
	 * This method makes a binary search to get the index in
	 * <code>positions</code> where the new value has to be inserted. If it is
	 * inserted between other values, they must be moved back by one. This
	 * implementation is relying on the precondition that the array
	 * <code>positions</code> is sorted, but ensures that the array will be
	 * sorted again.
	 * 
	 * @param i
	 * @param val
	 */
	private void insertValueAtPos(int i, double val) {
		int insertPos = -1;
		int low = 0;
		int high = realSize - 1;
		int mid = 0; // must be set to zero (when inserting the first value
		// to
		// matrix, it won't enter the while-loop

		while (low <= high) {
			mid = (low + high) >> 1;
			int midVal = positions[mid];

			if (midVal < i)
				low = mid + 1;
			else if (midVal > i)
				high = mid - 1;
			else {
				// return mid; // position found, replace value
				values[mid] = val;
				return;
			}
		}
		// position not yet in matrix, insert
		if (i < positions[mid]) // insert at position mid
			insertPos = mid;
		else
			insertPos = mid + 1; // i is bigger than middle value, take one
		// pos to the right

		if (positions[insertPos] != upperDelimiter)
			moveOneBack(insertPos);
		positions[insertPos] = i;
		values[insertPos] = val;
		realSize++;
	}

	/**
	 * Get the value at the specified position. No check is done if the position
	 * is out of bounds.
	 * 
	 * @param i
	 * @return the value at position i
	 */
	public double getQuick(int i) {
		int index = Arrays.binarySearch(positions, i);
		if (index < 0) {
			return 0.0;
		} else
			return values[index];
	}

	/**
	 * Get a view of a part of this matrix starting at <code>from</code> and
	 * with the size <code>length</code> The returned matrix will be a new
	 * instance.
	 * 
	 * @param from
	 * @param length
	 * @return a new instance of <code>OptimizedMatrix1D</code>
	 */
	public OptimizedMatrix1D viewPart(int from, int length) {
		int end = from + length - 1;
		if (end > size - 1)
			end = size - 1;
		if (from < 0)
			from = 0;
		length = end - from + 1;

		int countSelection = 0;
		for (int i = 0; i < realSize; i++) {
			if (positions[i] >= from && positions[i] <= end) {
				countSelection++;
			}
		}

		int[] tempPos = new int[countSelection];
		double[] tempVal = new double[countSelection];
		int j = 0;
		for (int i = 0; i < realSize; i++) {
			if (positions[i] >= from && positions[i] <= end) {
				tempPos[j] = positions[i];
				tempVal[j] = values[i];
				j++;
			}
		}
		return new OptimizedMatrix1D(tempPos, tempVal, this.size,
				countSelection);

	}

	/**
	 * Computes the sum of all values in this matrix.
	 * 
	 * @return sum
	 */
	public double zSum() {
		double sum = 0;
		for (int i = 0; i < realSize; i++) {
			sum += values[i];
		}
		return sum;
	}

	/**
	 * Computes the sum of the squares of all the values in this matrix.
	 * 
	 * @return sum matrix square values
	 */
	public double sumOfSquares() {
		double sum = 0;
		for (int i = 0; i < realSize; i++) {
			sum += values[i] * values[i];
		}
		return sum;
	}

	/**
	 * Computes the dot product of this matrix with another.
	 * 
	 * @param other
	 * @return dot product of two matrices
	 */
	public double zDotProduct(OptimizedMatrix1D other) {
		double product = 0;
		int j = 0;

		if (other.realSize > 0) {
			for (int i = 0; i < this.realSize; i++) {

				while (j < other.realSize - 1
						&& (other.positions[j] < positions[i])) {
					j++;
				}
				if (positions[i] == other.positions[j]) {
					product += values[i] * other.values[j];
				}
			}
		}
		return product;
	}

	/**
	 * Returns a sorted array with all the positions in this matrix that have
	 * non-zero values.
	 * 
	 * @return list of non-zero matrix positions
	 */
	public int[] getNonZeroPositions() {
		trim();
		return positions;
	}

	/**
	 * @return an array containing the values corresponding to the positions
	 *         specified in <code>getNonZeroPositions</code>. In other words:
	 *         positions[i] <==> values[i]
	 */
	public double[] getNonZeroValues() {
		trim();
		return values;
	}

	/**
	 * Trim the arrays to their real size.
	 */
	public void trim() {
		if (realSize < positions.length) {
			int[] tempPos = new int[realSize];
			double[] tempVal = new double[realSize];
			System.arraycopy(positions, 0, tempPos, 0, realSize);
			System.arraycopy(values, 0, tempVal, 0, realSize);
			positions = tempPos;
			values = tempVal;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < realSize; i++) {
			buffer.append(positions[i] + ", " + values[i] + "\n");
		}
		return buffer.toString();
	}

}