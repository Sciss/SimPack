/*
 * $Id: ReadAndWriteMatrix.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cern.colt.matrix.DoubleMatrix2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ReadAndWriteMatrix {

	static Logger logger = LogManager.getLogger(ReadAndWriteMatrix.class);

	private long before, after;

	/**
	 * Creates a new instance of ReadAndWriteMatrix
	 */
	public ReadAndWriteMatrix() {
	}

	/**
	 * Reads a matrix from a given file.
	 * 
	 * @param filename
	 *            the name of the file in which the matrix is saved
	 * @return the matrix read from the file
	 */
	public DoubleMatrix2D read(String filename) {
		logger.debug("Reading " + filename);
		before = System.currentTimeMillis();

		DoubleMatrix2D tmp = null;
		try {
			FileInputStream istream = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(istream);

			tmp = (DoubleMatrix2D) ois.readObject();
			ois.close();
			istream.close();

			after = System.currentTimeMillis();
			logger.debug("Matrix read in " + (after - before) / 1000
					+ " seconds.\n");
		} catch (IOException e) {
			System.out.println("Error while trying to read in " + filename);
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Error while trying to read in " + filename);
			System.out.println(e.getMessage());
		}

		return tmp;
	}

	/**
	 * Writes a matrix in a given file.
	 * 
	 * @param matrix
	 *            the matrix that has to be written in the file
	 * @param filename
	 *            the name of the file in which the matrix should be saved.
	 */
	public void write(DoubleMatrix2D matrix, String filename) {
		logger.debug("Writing into " + filename);
		before = System.currentTimeMillis();

		try {
			FileOutputStream ostream = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(ostream);

			oos.writeObject(matrix);
			oos.flush();
			ostream.close();
		} catch (IOException e) {
			System.out.println("Error while trying to write in " + filename);
			System.out.println(e.getMessage());
		}

		after = System.currentTimeMillis();
		logger.debug("matrix written in " + filename + " in "
				+ (after - before) / 1000 + " seconds.\n");
	}
}