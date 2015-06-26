/*
 * $Id: WhiteSpaceStringSequenceLevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.tokenizer.SplittedStringTokenizer;
import simpack.util.conversion.WorstCaseDistanceConversion;

/**
 * @author Christoph Kiefer
 * @version $Id: WhiteSpaceStringSequenceLevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class WhiteSpaceStringSequenceLevenshteinTest extends TestCase {

	private String profileDesc1 = new String(
			"It is the most used service for acknowledgement of the hotel in a city of a country.");

	private String profileDesc2 = new String(
			"This service returns accomodation information, hotel, restaurant etc in the city of the country.");

	private String profileDesc3 = new String(
			"This service reserve hotel in a city of the country during the given duration.");

	private StringAccessor csa1 = new StringAccessor(profileDesc1,
			new SplittedStringTokenizer("\\s+"));

	private StringAccessor csa2 = new StringAccessor(profileDesc2,
			new SplittedStringTokenizer("\\s+"));

	private StringAccessor csa3 = new StringAccessor(profileDesc3,
			new SplittedStringTokenizer("\\s+"));

	public void testSimilarity() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(csa1,
				csa1);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(1));
	}

	public void testSimilarity2() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(csa1,
				csa2);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				(96d - 13d) / 96d));
	}

	public void testSimilarity3() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(csa1,
				csa3);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				(84d - 15d) / 84d));
	}

	public void testSimilarity4() {
		Levenshtein<String> levensteinMeasure = new Levenshtein<String>(csa2,
				csa3);
		assertEquals(levensteinMeasure.getSimilarity(), new Double(
				(96d - 11d) / 96d));
	}

	public void testWeightBalanceException() {
		try {
			new Levenshtein<String>(csa1, csa2, 1.0, 1.0, 2.5, 0.0,
					new WorstCaseDistanceConversion());
			fail("Should raise a WeightBalanceException");
		} catch (WeightBalanceException e) {
			assertTrue(true);
		}
		try {
			new Levenshtein<String>(csa1, csa2, 1.0, 1.0, 0.0, 2.5,
					new WorstCaseDistanceConversion());
			fail("Should raise a WeightBalanceException");
		} catch (WeightBalanceException e) {
			assertTrue(true);
		}
		try {
			new Levenshtein<String>(csa1, csa2, 1.0, 1.0, 2.5, 2.5,
					new WorstCaseDistanceConversion());
			fail("Should raise a WeightBalanceException");
		} catch (WeightBalanceException e) {
			assertTrue(true);
		}
	}
}