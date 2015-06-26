/*
 * $Id: PearsonTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.vector;

import simpack.exception.InvalidVectorSizeException;
import simpack.measure.vector.ZPearson;
import simpack.measure.vector.StdPearson;
import simpack.util.Vector;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: PearsonTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class PearsonTest extends TestCase {

	private Vector<Double> v1 = new Vector<Double>();

	private Vector<Double> v2 = new Vector<Double>();

	private Vector<Double> v3 = new Vector<Double>();

	private Vector<Double> v4 = new Vector<Double>();

	private Vector<Double> v5 = new Vector<Double>();

	public void setUp() {
		v1.add(1d);
		v1.add(2d);
		v1.add(3d);

		v2.add(2d);
		v2.add(5d);
		v2.add(8d);

		v3.add(1d);
		v3.add(2d);

		v4.add(1d);
		v4.add(1d);
		v4.add(1d);

		v5.add(1d);
		v5.add(0d);
		v5.add(1d);
	}

	/**
	 * uses z-scores
	 */
	public void testZPearson() {
		ZPearson pearson = null;
		try {
			pearson = new ZPearson(v1, v2);
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}

		assertNotNull(pearson);
		assertTrue(pearson.calculate());
		assertTrue(pearson.isCalculated());

		double sumXY = 0;
		try {
			sumXY = v1.getDotProduct(v2);
		} catch (InvalidVectorSizeException e1) {
			e1.printStackTrace();
		}
		double sumXsumY = v1.getNorm(1d) * v2.getNorm(1d);
		double sumX2 = Math.pow(v1.getNorm(2d), 2d);
		double sumX22 = Math.pow(v1.getNorm(1d), 2d);
		double sumY2 = Math.pow(v2.getNorm(2d), 2d);
		double sumY22 = Math.pow(v2.getNorm(1d), 2d);
		double N = v1.size();

		assertEquals(pearson.getSimilarity(), new Double(
				(sumXY - (sumXsumY / N)))
				/ Math.sqrt((sumX2 - (sumX22 / N)) * (sumY2 - (sumY22 / N))));
	}

	/**
	 * standard formula
	 */
	public void testStdPearson() {
		StdPearson pearson = null;
		try {
			pearson = new StdPearson(v1, v2);
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}

		assertNotNull(pearson);
		assertTrue(pearson.calculate());
		assertTrue(pearson.isCalculated());

		// v1 mean = 2
		// v2 mean = 5

		double dot = (1d - 2d) * (2d - 5d) + (2d - 2d) * (5d - 5d) + (3d - 2d)
				* (8d - 5d);

		double norm1 = Math.sqrt((1d - 2d) * (1d - 2d) + (2d - 2d) * (2d - 2d)
				+ (3d - 2d) * (3d - 2d));

		double norm2 = Math.sqrt((2d - 5d) * (2d - 5d) + (5d - 5d) * (5d - 5d)
				+ (8d - 5d) * (8d - 5d));

		assertEquals(pearson.getSimilarity(), new Double(dot / (norm1 * norm2)));
	}

	public void testStdPearson2() {
		StdPearson pearson = null;
		try {
			pearson = new StdPearson(v4, v5);
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}

		assertNotNull(pearson);
		assertTrue(pearson.calculate());
		assertTrue(pearson.isCalculated());

		// v4 mean = 1
		// v5 mean = 2/3
		double m = 2d / 3d;

		double dot = (1d - 1d) * (1d - m) + (1d - 1d) * (0d - m) + (1d - 1d)
				* (1d - m);

		double norm1 = Math.sqrt((1d - 1d) * (1d - 1d) + (1d - 1d) * (1d - 1d)
				+ (1d - 1d) * (1d - 1d));

		double norm2 = Math.sqrt((1d - m) * (1d - m) + (0d - m) * (0d - m)
				+ (1d - m) * (1d - m));

		assertEquals(pearson.getSimilarity(), new Double(0.5d));
	}

	public void testZPearsonVsStdPerson() {
		ZPearson pearson1 = null;
		StdPearson pearson2 = null;
		try {
			pearson1 = new ZPearson(v1, v2);
			pearson2 = new StdPearson(v1, v2);
		} catch (InvalidVectorSizeException ex) {
			ex.printStackTrace();
		}

		assertNotNull(pearson1);
		assertTrue(pearson1.calculate());
		assertTrue(pearson1.isCalculated());

		assertNotNull(pearson2);
		assertTrue(pearson2.calculate());
		assertTrue(pearson2.isCalculated());

		System.out.println(pearson1.getSimilarity());
		System.out.println(pearson2.getSimilarity());

		assertEquals(pearson1.getSimilarity(), pearson2.getSimilarity());
	}

	public void testInvalidVectorSizeException() {
		ZPearson pearson = null;
		try {
			pearson = new ZPearson(v1, v3);
			fail("Should throw an InvalidVectorSizeException");
		} catch (InvalidVectorSizeException e) {
			assertTrue(true);
		}
	}
}
