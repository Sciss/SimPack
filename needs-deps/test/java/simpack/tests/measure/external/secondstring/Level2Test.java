/*
 * $Id: Level2Test.java 757 2008-04-17 17:42:53Z kiefer $
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

import com.wcohen.ss.Jaro;
import com.wcohen.ss.JaroWinkler;
import com.wcohen.ss.Levenstein;
import com.wcohen.ss.MongeElkan;
import com.wcohen.ss.SLIM;
import com.wcohen.ss.SLIMWinkler;
import com.wcohen.ss.tokens.SimpleTokenizer;

import simpack.measure.external.secondstring.Level2;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: Level2Test.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class Level2Test extends TestCase {

	private String a = new String("test");

	private String c = new String("test the best");

	private String d = new String("test them now");

	public void testCalculateSimilarity1() {
		Level2 level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(0));

		level2 = new Level2("test the best", "test them now",
				SimpleTokenizer.DEFAULT_TOKENIZER, new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(-2d / 3d));
	}

	public void testCalculateSimilarityWithDifferentParameters() {
		Level2 level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(0));

		level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER, new Jaro());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(1));

		level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER,
				new JaroWinkler());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(1));

		level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER,
				new MongeElkan());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(1));

		level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER, new SLIM());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(1));

		level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER,
				new SLIMWinkler());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(1));
	}

	public void testCalculateSimilarity2() {
		Level2 level2 = new Level2(c, d, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(-2d / 3d));

		level2 = new Level2(c, d, SimpleTokenizer.DEFAULT_TOKENIZER, new Jaro());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(11d / 12d));

		level2 = new Level2(c, d, SimpleTokenizer.DEFAULT_TOKENIZER,
				new JaroWinkler());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		// assertEquals(level2.getSimilarity(), new Double(37d / 40d));
		assertEquals(level2.getSimilarity(), new Double(0.9249999999999999));

		level2 = new Level2(c, d, SimpleTokenizer.DEFAULT_TOKENIZER,
				new MongeElkan());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(11d / 12d));

		level2 = new Level2(c, d, SimpleTokenizer.DEFAULT_TOKENIZER, new SLIM());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(11d / 12d));

		level2 = new Level2(c, d, SimpleTokenizer.DEFAULT_TOKENIZER,
				new SLIMWinkler());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(223d / 240d));
	}
}
