/*
 * $Id: Level2LevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.external.secondstring.Level2;

import com.wcohen.ss.Levenstein;
import com.wcohen.ss.tokens.SimpleTokenizer;

/**
 * @author Christoph Kiefer
 * @version $Id: Level2LevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class Level2LevenshteinTest extends TestCase {

	private String a = new String(
			"This is the best service to know about the beach for surfing.");

	private String b = new String(
			"This is a recommended service to know about the beach for the beach sports.");

	private String c = new String(
			"This is the best service to know about the destination for surfing.");

	public void testCalculateSimilarity1() {
		Level2 level2 = new Level2(a, a, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(0));
	}

	public void testCalculateSimilarity2() {
		Level2 level2 = new Level2(a, b, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(-0.6363636363636364));
	}

	public void testCalculateSimilarity3() {
		Level2 level2 = new Level2(a, c, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(-0.2727272727272727));
	}

	public void testCalculateSimilarity4() {
		Level2 level2 = new Level2(b, c, SimpleTokenizer.DEFAULT_TOKENIZER,
				new Levenstein());
		assertNotNull(level2);
		assertTrue(level2.calculate());
		assertTrue(level2.isCalculated());
		assertEquals(level2.getSimilarity(), new Double(-3d / 2d));
	}
}