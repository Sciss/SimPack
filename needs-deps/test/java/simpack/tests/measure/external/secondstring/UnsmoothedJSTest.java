/*
 * $Id: UnsmoothedJSTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.external.secondstring;

import simpack.measure.external.secondstring.UnsmoothedJS;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: UnsmoothedJSTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class UnsmoothedJSTest extends TestCase {

	public void testCalculationSimilarity() {
		UnsmoothedJS jsd = new UnsmoothedJS("test", "test");
		assertNotNull(jsd);
		assertTrue(jsd.calculate());
		assertTrue(jsd.isCalculated());
		assertEquals(jsd.getSimilarity(), new Double(1d));

		jsd = new UnsmoothedJS("test", "best");
		assertNotNull(jsd);
		assertTrue(jsd.calculate());
		assertTrue(jsd.isCalculated());
		assertEquals(jsd.getSimilarity(), new Double(0d));

		jsd = new UnsmoothedJS("test the best", "test them");
		assertNotNull(jsd);
		assertTrue(jsd.calculate());
		assertTrue(jsd.isCalculated());
		assertEquals(jsd.getSimilarity(), new Double(0.40456274768944517));
	}

	public void testEmptyStrings() {
		UnsmoothedJS jsd = new UnsmoothedJS("", "test");
		assertNotNull(jsd);
		assertTrue(jsd.calculate());
		assertTrue(jsd.isCalculated());
		assertEquals(jsd.getSimilarity(), new Double(0d));

		jsd = new UnsmoothedJS("", "");
		assertNotNull(jsd);
		assertTrue(jsd.calculate());
		assertTrue(jsd.isCalculated());
		assertEquals(jsd.getSimilarity(), new Double(0d));
	}
}
