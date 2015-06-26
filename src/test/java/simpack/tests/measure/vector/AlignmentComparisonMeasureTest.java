/*
 * $Id: AlignmentComparisonMeasureTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.vector;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import simpack.measure.vector.AlignmentComparison;

/**
 * @author Christoph Kiefer
 * @version $Id: AlignmentComparisonMeasureTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class AlignmentComparisonMeasureTest extends TestCase {

	private Map<Object, Object> v1 = new HashMap<Object, Object>();

	private Map<Object, Object> v2 = new HashMap<Object, Object>();

	private Map<Object, Object> v3 = new HashMap<Object, Object>();

	public void setUp() {
		v1.put("name", "christoph");
		v1.put("age", 27);
		v1.put("sex", "male");

		v2.put("name", "avi");
		v2.put("type", new Object());
		v2.put("sex", "male");

		v3.put("name", "christopher");
		v3.put("type", new Object());
		v3.put("sex", "male");
	}

	public void testAlignment() {
		AlignmentComparison alignment = new AlignmentComparison(v1, v2,
				"Levenshtein", 0.75d);

		assertNotNull(alignment);
		assertTrue(alignment.calculate());
		assertTrue(alignment.isCalculated());
		assertEquals(alignment.getSimilarity(), new Double(0.7931034482758622));
	}

	public void testAlignment2() {
		AlignmentComparison alignment = new AlignmentComparison(v1, v3,
				"Levenshtein", 0.75d);

		assertNotNull(alignment);
		assertTrue(alignment.calculate());
		assertTrue(alignment.isCalculated());
		assertEquals(alignment.getSimilarity(), new Double(0.9090909090909091));
	}
}