/*
 * $Id: JaccardSimililarityTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import junit.framework.TestCase;
import simmetrics.tokenisers.TokeniserQGram3;
import simmetrics.tokenisers.TokeniserWhitespace;
import simpack.measure.external.simmetrics.JaccardSimilarity;

/**
 * @author Christoph Kiefer
 * @version $Id: JaccardSimililarityTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JaccardSimililarityTest extends TestCase {

	public void testCalculateSimilarity() {
		JaccardSimilarity jaccard = new JaccardSimilarity("test", "test");
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		assertEquals(jaccard.getSimilarity(), new Double(1));

		jaccard = new JaccardSimilarity("test", "best");
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		assertEquals(jaccard.getSimilarity(), new Double(0));
	}

	public void testCalculateSimilarityWithParameters() {
		JaccardSimilarity jaccard = new JaccardSimilarity("test", "test",
				new TokeniserWhitespace());
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		assertEquals(jaccard.getSimilarity(), new Double(1));

		jaccard = new JaccardSimilarity("test", "test", new TokeniserQGram3());
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		assertEquals(jaccard.getSimilarity(), new Double(1));

		jaccard = new JaccardSimilarity("test west", "test best",
				new TokeniserQGram3());
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		// assertEquals(jaccard.getSimilarity(), new Double(1));
	}

	public void testCalculationSimilarityWithParameters2() {
		JaccardSimilarity jaccard = new JaccardSimilarity(
				"http://lsdis.cs.uga.edu/proj/semdis/testbed/#Company http://lsdis.cs.uga.edu/proj/semdis/testbed/#Thing http://lsdis.cs.uga.edu/proj/semdis/testbed/#Financial_Institute",
				"http://lsdis.cs.uga.edu/proj/semdis/testbed/#Company http://lsdis.cs.uga.edu/proj/semdis/testbed/#Thing http://lsdis.cs.uga.edu/proj/semdis/testbed/#Software");
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		assertEquals(jaccard.getSimilarity(), new Double(1d / 2d));
	}
}
