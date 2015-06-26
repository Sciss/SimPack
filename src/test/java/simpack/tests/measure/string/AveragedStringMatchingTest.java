/*
 * $Id: AveragedStringMatchingTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import junit.framework.TestCase;
import simpack.accessor.string.StringAccessor;
import simpack.api.ISequenceAccessor;
import simpack.exception.InvalidSimilarityMeasureNameException;
import simpack.measure.string.AveragedStringMatching;
import simpack.tokenizer.SplittedStringTokenizer;

/**
 * @author Christoph Kiefer
 * @version $Id: AveragedStringMatchingTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class AveragedStringMatchingTest extends TestCase {

	private ISequenceAccessor<String> accessor1 = new StringAccessor(
			"Programming Language", new SplittedStringTokenizer(" "));

	private ISequenceAccessor<String> accessor2 = new StringAccessor(
			"Semantics of Programming Languages", new SplittedStringTokenizer(
					" "));

	public void testCalculateSimilarityLevenshteinSimPack() {
		AveragedStringMatching avgLevenshtein = null;
		try {
			avgLevenshtein = new AveragedStringMatching(accessor1, accessor2);
		} catch (InvalidSimilarityMeasureNameException e) {
			e.printStackTrace();
		}
		assertNotNull(avgLevenshtein);
		assertTrue(avgLevenshtein.calculate());
		assertTrue(avgLevenshtein.isCalculated());

		assertEquals(avgLevenshtein.getSimilarity(), new Double(
				(1d + (1d - (1d / 9d))) / 2d));
	}
}
