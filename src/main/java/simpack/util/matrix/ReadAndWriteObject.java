/*
 * $Id: ReadAndWriteObject.java 757 2008-04-17 17:42:53Z kiefer $
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ReadAndWriteObject {

	static Logger logger = LogManager.getLogger(ReadAndWriteObject.class);

	private long before, after;

	/**
	 * Creates a new instance of ReadAndWriteMatrix
	 */
	public ReadAndWriteObject() {
	}

	/**
	 * Reads an object from a given file. The file is supposed to be gzipped.
	 * This function first unpacks the file (with <tt>gunzip</tt>) before it
	 * reads the file. At the end, the file is packed again (with <tt>gzip</tt>).
	 * 
	 * @param filename
	 *            the name of the file in which the object is saved
	 * @return the object read from the file
	 */
	public Object read(String filename) {
		logger.debug("Reading " + filename);
		before = System.currentTimeMillis();

		Object tmp = null;
		try {
			FileInputStream istream = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(istream);

			tmp = ois.readObject();
			ois.close();
			istream.close();

			after = System.currentTimeMillis();
			logger.debug(filename + " read in " + (after - before) / 1000
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
	 * Writes an object in a given file. The file will then be packed (with
	 * <tt>gzip</tt>)
	 * 
	 * @param obj
	 *            the object that has to be written in the file
	 * @param filename
	 *            the name of the file in which the object should be saved.
	 */
	public void write(Object obj, String filename) {
		logger.debug("Writing into " + filename);
		before = System.currentTimeMillis();

		try {
			FileOutputStream ostream = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(ostream);

			oos.writeObject(obj);
			oos.flush();
			ostream.close();

		} catch (IOException e) {
			System.out.println("Error while trying to write in " + filename);
			System.out.println(e.getMessage());
		}

		after = System.currentTimeMillis();
		logger.debug("Object written in " + filename + " in "
				+ (after - before) / 1000 + " seconds.\n");
	}
}