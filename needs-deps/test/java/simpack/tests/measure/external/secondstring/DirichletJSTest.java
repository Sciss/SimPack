/*
 * $Id: DirichletJSTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import junit.framework.TestCase;
import simpack.accessor.string.StringAccessor;
import simpack.measure.external.secondstring.DirichletJS;

import com.wcohen.ss.tokens.NGramTokenizer;
import com.wcohen.ss.tokens.SimpleTokenizer;

public class DirichletJSTest extends TestCase {

	public void testCalculationSimilarity() {
		DirichletJS dirichletJS = new DirichletJS(new StringAccessor("test"),
				new StringAccessor("test"));
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), Double.NaN);

		dirichletJS = new DirichletJS(new StringAccessor("test"),
				new StringAccessor("west"));
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), new Double(0));
	}

	public void testEmptyStrings() {
		DirichletJS dirichletJS = new DirichletJS(new StringAccessor(""),
				new StringAccessor("test"));
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), new Double(0));

		dirichletJS = new DirichletJS(new StringAccessor("test"),
				new StringAccessor(""));
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), new Double(0));

		dirichletJS = new DirichletJS(new StringAccessor(""),
				new StringAccessor(""));
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), new Double(0));
	}

	public void testCalculationSimilarityWithParameters() {
		DirichletJS dirichletJS = new DirichletJS(new StringAccessor("test"),
				new StringAccessor("test"), SimpleTokenizer.DEFAULT_TOKENIZER,
				1.0);
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), Double.NaN);

		dirichletJS = new DirichletJS(new StringAccessor("test"),
				new StringAccessor("test"), NGramTokenizer.DEFAULT_TOKENIZER,
				1.0);
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), Double.NaN);

		new DirichletJS(new StringAccessor("test"), new StringAccessor(
				"test best"), NGramTokenizer.DEFAULT_TOKENIZER, 1.0);
		assertNotNull(dirichletJS);
		assertTrue(dirichletJS.calculate());
		assertTrue(dirichletJS.isCalculated());
		assertEquals(dirichletJS.getSimilarity(), Double.NaN);
	}
}
