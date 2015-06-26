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
package simpack.tests.measure.external.simmetrics;

import simpack.measure.external.simmetrics.Levenshtein;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: LevenshteinTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class LevenshteinTest extends TestCase {

	public void testCalculateSimilarity() {
		Levenshtein levenshtein = new Levenshtein("test", "test");
		assertNotNull(levenshtein);
		assertTrue(levenshtein.calculate());
		assertTrue(levenshtein.isCalculated());
		assertEquals(levenshtein.getSimilarity(), new Double(1));

		levenshtein = new Levenshtein("test", "west");
		assertNotNull(levenshtein);
		assertTrue(levenshtein.calculate());
		assertTrue(levenshtein.isCalculated());
		assertEquals(levenshtein.getSimilarity(), new Double(0.75));
	}
}
