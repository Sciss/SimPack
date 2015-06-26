/*
 * $Id: LevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.accessor.string.StringAccessor;
import simpack.exception.WeightBalanceException;
import simpack.measure.sequence.Levenshtein;
import simpack.util.conversion.CommonDistanceConversion;
import simpack.util.conversion.LogarithmicDistanceConversion;
import simpack.util.conversion.WorstCaseDistanceConversion;

/**
 * @author Christoph Kiefer
 * @version $Id: LevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class LevenshteinTest extends TestCase {

	private StringAccessor sa1 = new StringAccessor("Language");

	private StringAccessor sa2 = new StringAccessor("Languages");

	private StringAccessor sa3 = new StringAccessor("Levenshtein");

	private StringAccessor sa4 = new StringAccessor("shteinLeven");

	public void testSimilarity() {
		Levenshtein<String> levensteinMeasure;
		levensteinMeasure = new Levenshtein<String>(sa1, sa1);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(1));
	}

	public void testSimilarity2() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(sa1,
				sa2);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				1d - (1d / 9d)));
	}

	public void testSimilarity3() {
		// "" : Language
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(
				new StringAccessor(""), sa1);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				1d - (8d / 8d)));
	}

	public void testSimilarity4() {
		Levenshtein<String> levensteinMeasure;
		levensteinMeasure = new Levenshtein<String>(sa3, sa4);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				(11d - 8d) / 11d));
	}

	public void testSimilarityDifferentConversion() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(sa1,
				sa2, new WorstCaseDistanceConversion());
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				1d - (1d / 9d)));
	}

	public void testSimilarityDifferentConversion2() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(sa1,
				sa2, new CommonDistanceConversion());
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				1d / (1d + 1d)));
	}

	public void testSimilarityDifferentConversion3() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(sa1,
				sa2, new LogarithmicDistanceConversion());
		assertEquals(levensteinMeasure.getSimilarity(), new Double(Math
				.exp(-(1d * 1d))));
	}

	public void testWeightBalanceException() {
		try {
			new Levenshtein<String>(sa1, sa2, 1.0, 1.0, 2.5, 0.0,
					new WorstCaseDistanceConversion());
			fail("Should raise a WeightBalanceException");
		} catch (WeightBalanceException e) {
			assertTrue(true);
		}
		try {
			new Levenshtein<String>(sa1, sa2, 1.0, 1.0, 0.0, 2.5,
					new WorstCaseDistanceConversion());
			fail("Should raise a WeightBalanceException");
		} catch (WeightBalanceException e) {
			assertTrue(true);
		}
		try {
			new Levenshtein<String>(sa1, sa2, 1.0, 1.0, 2.5, 2.5,
					new WorstCaseDistanceConversion());
			fail("Should raise a WeightBalanceException");
		} catch (WeightBalanceException e) {
			assertTrue(true);
		}
	}
}