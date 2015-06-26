/*
 * $Id: LOLTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.sequence;

import junit.framework.TestCase;

import org.junit.Test;

import simpack.accessor.string.StringAccessor;
import simpack.measure.sequence.LOL;
import simpack.tokenizer.SplittedStringTokenizer;
import simpack.util.conversion.WorstCaseDistanceConversion;

/**
 * @author Christoph Kiefer
 * @version $Id: LOLTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class LOLTest extends TestCase {

	private StringAccessor sa1 = new StringAccessor(
			"this language levenshtein", new SplittedStringTokenizer("\\s+"));

	private StringAccessor sa2 = new StringAccessor(
			"this languages shteinleven", new SplittedStringTokenizer("\\s+"));

	private StringAccessor sa3 = new StringAccessor("MATES",
			new SplittedStringTokenizer("\\s+"));

	private StringAccessor sa4 = new StringAccessor(
			"Multiagent System Technologies: Third German Conference, MATES 2005, Koblenz, Germany, September 11-13, 2005. Proceedings",
			new SplittedStringTokenizer("\\s+"));

	 @Test
	public void testSimilarity() {
		LOL<String> levenshteinMeasure;
		levenshteinMeasure = new LOL<String>(sa1, sa1, 0.3d);
		assertEquals(levenshteinMeasure.getSimilarity(), new Double(1));
	}

	@Test
	public void testSimilarity2() {
		LOL<String> levenshteinMeasure = new LOL<String>(sa1, sa2, 0.3d);
		assertEquals(levenshteinMeasure.getSimilarity(), new Double(
				(3d - 1d) / 3d));
	}

	@Test
	public void testSimilarity3() {
		LOL<String> levenshteinMeasure = new LOL<String>(
				new StringAccessor(""), sa2);
		assertEquals(levenshteinMeasure.getSimilarity(), new Double(0d));
	}

	@Test
	public void testSimilarity4() {
		LOL<String> levenshteinMeasure = new LOL<String>(sa1,
				new StringAccessor(""));
		assertEquals(levenshteinMeasure.getSimilarity(), new Double(0d));
	}

	@Test
	public void testSimilarity5() {
		LOL<String> levenshteinMeasure = new LOL<String>(
				new StringAccessor(""), new StringAccessor(""));
		assertEquals(levenshteinMeasure.getSimilarity(), new Double(0d));
	}

	@Test
	public void testSimilarity6() {
		try {
			LOL<String> levenshteinMeasure = new LOL<String>(null,
					new StringAccessor(""));
			fail("Should throw NullPointerException");
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
	}

	@Test
	public void testSimilarity7() {
		LOL<String> levenshteinMeasure = new LOL<String>(sa3, sa4);
		assertEquals(levenshteinMeasure.getSimilarity(), (14d - 13d) / 14d);
	}

	@Test
	public void testSimilarity8() {
		StringAccessor sa1 = new StringAccessor("provid materi",
				new SplittedStringTokenizer("\\s+"));
		StringAccessor sa2 = new StringAccessor("custom servic",
				new SplittedStringTokenizer("\\s+"));

		LOL<String> levenshteinMeasure = new LOL<String>(sa1, sa2,
				new WorstCaseDistanceConversion(), 0.7);
		assertEquals(levenshteinMeasure.getSimilarity(), 0d);
	}
}