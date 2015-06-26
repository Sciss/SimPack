/*
 * $Id: OptimizedMatrix2D.java 757 2008-04-17 17:42:53Z kiefer $
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

/**
 * The class <code>OptimizedMatrix2D</code> is a ever-growing two dimensional
 * matrix. It is optimized for sparse matrices, thus matrices that have many
 * zero values. This implementation tries to find a compromise between size and
 * quickness. For more information relatet to performance see
 * {@link OptimizedMatrix1D}.
 * <p>
 * This matrix consists of several {@link OptimizedMatrix1D}. An
 * <code>OptimizedMatrix1D</code> can either be a column or a row of this
 * matrix. These 1D-matrices are stored in two arrays, one for the columns, one
 * for the rows. Actually this doubles the size of this 2D-matrix, and inserting
 * values needs double the time. The reason for the chosen implementation lies
 * in the need of efficiently retrieving either a whole column or a row.
 * 
 * @see OptimizedMatrix1D
 * 
 * @author Silvan Hollenstein
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */

public class OptimizedMatrix2D implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * rows is holding all the rows of this matrix.
	 */
	private final OptimizedMatrix1D[] rows;

	/**
	 * cols is holding all the columns of this matrix.
	 */
	private final OptimizedMatrix1D[] cols;

	/**
	 * The number of rows in this matrix.
	 */
	private final int countRows;

	/**
	 * The number of columns in this matrix.
	 */
	private final int countCols;

	public OptimizedMatrix2D(int rows, int cols) {
		this.rows = new OptimizedMatrix1D[rows];
		this.cols = new OptimizedMatrix1D[cols];

		OptimizedMatrix1D matrix1D;
		for (int i = 0; i < rows; i++) {
			matrix1D = new OptimizedMatrix1D(cols);
			this.rows[i] = matrix1D;
		}
		for (int j = 0; j < cols; j++) {
			matrix1D = new OptimizedMatrix1D(rows);
			this.cols[j] = matrix1D;
		}
		countRows = rows;
		countCols = cols;

	}

	/**
	 * Sets a value into this matrix at the specified row and column. No check
	 * is done if the position is out of bounds.
	 * 
	 * @param row
	 * @param column
	 * @param value
	 */
	public void setQuick(int row, int column, double value) {
		OptimizedMatrix1D matrixCol = cols[column];
		matrixCol.setQuick(row, value);

		OptimizedMatrix1D matrixRow = rows[row];
		matrixRow.setQuick(column, value);
	}

	/**
	 * Get the value at the specified position. No check is done if the position
	 * is out of bounds.
	 * 
	 * @param row
	 * @param column
	 * @return matrix value
	 */
	public double getQuick(int row, int column) {
		// OptimizedMatrix1D matrixCol = cols[column];
		// return matrixCol.getQuick(row);
		OptimizedMatrix1D matrixRow = rows[row];
		return matrixRow.getQuick(column);
	}

	/**
	 * The number of rows in this matrix.
	 * 
	 * @return number of rows
	 */
	public int rows() {
		return countRows;
	}

	/**
	 * The number of columns in this matrix.
	 * 
	 * @return number of columns
	 */
	public int columns() {
		return countCols;
	}

	/**
	 * Get the column at the specified position.
	 * 
	 * @param column
	 * @return an OptimizedMatrix1D
	 */
	public OptimizedMatrix1D viewColumn(int column) {
		return cols[column];
	}

	/**
	 * Get the row at the specified position.
	 * 
	 * @param row
	 * @return an OptimizedMatrix1D
	 */
	public OptimizedMatrix1D viewRow(int row) {
		return rows[row];
	}

	/**
	 * Trims this matrix to its real size.
	 */
	public void trim() {
		for (int i = 0; i < countRows; i++) {
			rows[i].trim();
		}
		for (int i = 0; i < countCols; i++) {
			cols[i].trim();
		}
	}

}
