/*
 * $Id: SmithWatermanTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.external.simmetrics;

import junit.framework.TestCase;
import simmetrics.similaritymetrics.costfunctions.SubCost01;
import simmetrics.similaritymetrics.costfunctions.SubCost1_Minus2;
import simpack.measure.external.simmetrics.SmithWaterman;

/**
 * @author Christoph Kiefer
 * @version $Id: SmithWatermanTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class SmithWatermanTest extends TestCase {

	public void testCalculateSimilarity() {
		SmithWaterman sw = new SmithWaterman("test", "test");
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(1));

		sw = new SmithWaterman("test", "best");
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(0.75));
	}

	public void testCalculateSimilarityWithParameters() {
		SmithWaterman sw = new SmithWaterman("test", "test", new SubCost01());
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(0.75));

		sw = new SmithWaterman("test", "west", new SubCost01());
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(0.75));

		sw = new SmithWaterman("test", "test", new SubCost1_Minus2());
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(1));

		sw = new SmithWaterman("test", "west", new SubCost1_Minus2());
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(0.75));

		sw = new SmithWaterman("test", "west", 1f);
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(0.75));

		sw = new SmithWaterman("test", "west", 1f, new SubCost01());
		assertNotNull(sw);
		assertTrue(sw.calculate());
		assertTrue(sw.isCalculated());
		assertEquals(sw.getSimilarity(), new Double(0.75));
	}
}
