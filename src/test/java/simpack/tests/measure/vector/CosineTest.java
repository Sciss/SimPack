/*
 * $Id: CosineTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.vector.Cosine;
import simpack.util.Vector;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: CosineTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class CosineTest extends TestCase {

	private Vector<Double> v1 = new Vector<Double>();

	private Vector<Double> v2 = new Vector<Double>();

	private Vector<Double> v3 = new Vector<Double>();

	public void setUp() {
		v1.add(1d);
		v1.add(2d);
		v1.add(3d);

		v2.add(2d);
		v2.add(5d);
		v2.add(6d);

		v3.add(1d);
		v3.add(2d);
	}

	public void testCosine() {
		Cosine cosine = null;
		try {
			cosine = new Cosine(v1, v2);

			assertNotNull(cosine);
			assertTrue(cosine.calculate());
			assertTrue(cosine.isCalculated());

			double sumXY = 0d;
			sumXY = v1.getDotProduct(v2);

			double n1 = v1.getNorm(2d);
			double n2 = v2.getNorm(2d);

			assertEquals(cosine.getSimilarity(), sumXY / (n1 * n2));

		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}
	}

	public void testInvalidVectorSizeException() {
		try {
			Cosine cosine = new Cosine(v1, v3);
			fail("Should throw an InvalidVectorSizeException");
		} catch (InvalidVectorSizeException e) {
			assertTrue(true);
		}
	}
}
