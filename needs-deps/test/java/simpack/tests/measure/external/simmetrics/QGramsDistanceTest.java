/*
 * $Id: QGramsDistanceTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.external.simmetrics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;
import simpack.measure.external.simmetrics.QGramsDistance;
import simpack.tokenizer.TokeniserQGramN;

/**
 * @author Christoph Kiefer
 * @version $Id: QGramsDistanceTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class QGramsDistanceTest extends TestCase {

	public void setUp() {
		System.out.println("test");
	}
	
	public void testCalculateSimilarityQGramsN() {

		QGramsDistance qGramsDistance = new QGramsDistance("proceedings",
				"proceedings", new TokeniserQGramN(2));

		assertNotNull(qGramsDistance);
		assertTrue(qGramsDistance.calculate());
		assertTrue(qGramsDistance.isCalculated());
		assertEquals(qGramsDistance.getSimilarity(), new Double(1));
	}

	public void testCalculateSimilarityQGramsN2() {

		QGramsDistance qGramsDistance = new QGramsDistance("inproceedings",
				"proceedings", new TokeniserQGramN(2));

		assertNotNull(qGramsDistance);
		assertTrue(qGramsDistance.calculate());
		assertTrue(qGramsDistance.isCalculated());
		assertEquals(qGramsDistance.getSimilarity(), (22d - 2d) / 22d);
	}

	public void testCalculateSimilarityQGramsN3() {

		QGramsDistance qGramsDistance = new QGramsDistance("proceedings",
				"inproceedings", new TokeniserQGramN(2));

		assertNotNull(qGramsDistance);
		assertTrue(qGramsDistance.calculate());
		assertTrue(qGramsDistance.isCalculated());
		assertEquals(qGramsDistance.getSimilarity(), (22d - 2d) / 22d);
	}

	public void testCalculateSimilarityQGramsN4() {

		QGramsDistance qGramsDistance = new QGramsDistance(
				"CityLuxuryHotelInfoService", "CityCountryHotelInfoService",
				new TokeniserQGramN(2));

		getUnNormalisedSimilarity("CityLuxuryHotelInfoService",
				"CityCountryHotelInfoService");

		assertNotNull(qGramsDistance);
		assertTrue(qGramsDistance.calculate());
		assertTrue(qGramsDistance.isCalculated());
		assertEquals(qGramsDistance.getSimilarity(), (27d + 25d - 11d)
				/ (27d + 25d));
	}

	public float getUnNormalisedSimilarity(String string1, String string2) {
		final Vector<String> str1Tokens = new TokeniserQGramN(2)
				.tokenize(string1);
		final Vector<String> str2Tokens = new TokeniserQGramN(2)
				.tokenize(string2);

		final Set<String> allTokens = new HashSet<String>();
		allTokens.addAll(str1Tokens);
		allTokens.addAll(str2Tokens);

		final Iterator<String> allTokensIt = allTokens.iterator();
		int difference = 0;
		while (allTokensIt.hasNext()) {
			final String token = allTokensIt.next();
			int matchingQGrams1 = 0;
			for (String str1Token : str1Tokens) {
				if (str1Token.equals(token)) {
					matchingQGrams1++;
				}
			}
			int matchingQGrams2 = 0;
			for (String str2Token : str2Tokens) {
				if (str2Token.equals(token)) {
					matchingQGrams2++;
				}
			}

			if (matchingQGrams1 > matchingQGrams2) {
				difference += (matchingQGrams1 - matchingQGrams2);
				System.out.println("dif > 1: " + token + ", dif = "
						+ (matchingQGrams1 - matchingQGrams2));
			} else if (matchingQGrams1 < matchingQGrams2) {
				difference += (matchingQGrams2 - matchingQGrams1);
				System.out.println("dif < 1: " + token + ", dif = "
						+ (matchingQGrams2 - matchingQGrams1));
			}
		}

		System.out.println(difference);
		return difference;
	}
}
