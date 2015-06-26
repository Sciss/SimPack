/*
 * $Id: JaroSecondstringTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.measure.external.secondstring.Jaro;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: JaroSecondstringTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JaroSecondstringTest extends TestCase {

	public void testCalculationSimilarity() {
		Jaro jaro = new Jaro("test", "test");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(1));
		
		jaro = new Jaro("testt", "westt");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0.8666666666666667));
		
		jaro = new Jaro("testt", "nada");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));
	}
	
	public void testCalculationSimilarity2() {
		Jaro jaro = new Jaro("Individual", "Person");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0d));
	}
	
	public void testCalculationEmptyStrings() {
		Jaro jaro = new Jaro("", "test");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));
		
		jaro = new Jaro("test", "");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));
		
		jaro = new Jaro("", "");
		assertNotNull(jaro);
		assertTrue(jaro.calculate());
		assertTrue(jaro.isCalculated());
		assertEquals(jaro.getSimilarity(), new Double(0));
	}
}
