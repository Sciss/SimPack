/*
 * $Id: JaroTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.string;

import simpack.accessor.string.StringAccessor;
import simpack.measure.string.Jaro;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: JaroTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JaroTest extends TestCase {

	public void testCalculationSimilarity() {
		Jaro jaro = new Jaro(new StringAccessor("Individual"),
				new StringAccessor("IndividualPerson"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0.875d));
	}

	public void testCalculationSimilarity2() {
		Jaro jaro = new Jaro(new StringAccessor("info"), new StringAccessor(
				"hasID"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(-1d));
	}

	public void testCalculationSimilarity3() {
		Jaro jaro = new Jaro(new StringAccessor("Individual"),
				new StringAccessor("Address"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(-1d));
	}

	public void testCalculationSimilarity4() {
		Jaro jaro = new Jaro(new StringAccessor("test"), new StringAccessor(
				"test"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(1));

		jaro = new Jaro(new StringAccessor("testt"),
				new StringAccessor("westt"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0.8666666666666667));

		jaro = new Jaro(new StringAccessor("testt"), new StringAccessor("nada"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));
	}

	public void testCalculationEmptyStrings() {
		Jaro jaro = new Jaro(new StringAccessor(""), new StringAccessor("test"));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));

		jaro = new Jaro(new StringAccessor("test"), new StringAccessor(""));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));

		jaro = new Jaro(new StringAccessor(""), new StringAccessor(""));
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));
	}
}
