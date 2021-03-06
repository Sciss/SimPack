/*
 * $Id: JelinekMercerJSTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import com.wcohen.ss.tokens.SimpleTokenizer;

import simpack.measure.external.secondstring.JelinekMercerJS;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: JelinekMercerJSTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JelinekMercerJSTest extends TestCase {

	public void testCalculationSimilarity() {
		JelinekMercerJS jelinekMercerJS = new JelinekMercerJS("test", "test");
		assertNotNull(jelinekMercerJS);
		assertTrue(jelinekMercerJS.calculate());
		assertTrue(jelinekMercerJS.isCalculated());
		assertEquals(jelinekMercerJS.getSimilarity(), Double.NaN);

		jelinekMercerJS = new JelinekMercerJS("test", "best");
		assertNotNull(jelinekMercerJS);
		assertTrue(jelinekMercerJS.calculate());
		assertTrue(jelinekMercerJS.isCalculated());
		assertEquals(jelinekMercerJS.getSimilarity(), new Double(0));
	}

	public void testCalculationSimilarityWithParameters() {
		JelinekMercerJS jelinekMercerJS = new JelinekMercerJS("test the best in west",
				"test them", SimpleTokenizer.DEFAULT_TOKENIZER, new Double(0.5));
		assertNotNull(jelinekMercerJS);
		assertTrue(jelinekMercerJS.calculate());
		assertTrue(jelinekMercerJS.isCalculated());
		assertEquals(jelinekMercerJS.getSimilarity(), Double.NaN);
	}
}
