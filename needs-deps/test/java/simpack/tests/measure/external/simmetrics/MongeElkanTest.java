/*
 * $Id: MongeElkanTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import simmetrics.similaritymetrics.SmithWatermanGotoh;
import simmetrics.tokenisers.TokeniserQGram3;
import simmetrics.tokenisers.TokeniserWhitespace;
import simpack.measure.external.simmetrics.MongeElkan;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: MongeElkanTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class MongeElkanTest extends TestCase {

	public void testCalculateSimilarity() {
		MongeElkan me = new MongeElkan("test", "test");
		assertNotNull(me);
		assertTrue(me.calculate());
		assertTrue(me.isCalculated());
		assertEquals(me.getSimilarity(), new Double(1));

		me = new MongeElkan("test", "best");
		assertNotNull(me);
		assertTrue(me.calculate());
		assertTrue(me.isCalculated());
		assertEquals(me.getSimilarity(), new Double(0.75));
	}

	public void testCalculateSimilarityWithParameters() {
		MongeElkan me = new MongeElkan("test", "test",
				new TokeniserWhitespace());
		assertNotNull(me);
		assertTrue(me.calculate());
		assertTrue(me.isCalculated());
		assertEquals(me.getSimilarity(), new Double(1));

		me = new MongeElkan("test", "test", new TokeniserQGram3());
		assertNotNull(me);
		assertTrue(me.calculate());
		assertTrue(me.isCalculated());
		assertEquals(me.getSimilarity(), new Double(1));

		me = new MongeElkan("test west", "test best", new TokeniserQGram3());
		assertNotNull(me);
		assertTrue(me.calculate());
		assertTrue(me.isCalculated());
		//assertEquals(me.getSimilarity(), new Double(0.5));
		
		me = new MongeElkan("test", "test",
				new TokeniserWhitespace(),
				new SmithWatermanGotoh());
		assertNotNull(me);
		assertTrue(me.calculate());
		assertTrue(me.isCalculated());
		assertEquals(me.getSimilarity(), new Double(1));
	}
}
