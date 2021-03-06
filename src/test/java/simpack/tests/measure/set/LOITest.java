/*
 * $Id: LOITest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.set;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import simpack.measure.set.LOI;

/**
 * @author Christoph Kiefer
 * @version $Id: LOITest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class LOITest {

	private Set<Object> s1 = new HashSet<Object>();

	private Set<Object> s2 = new HashSet<Object>();

	private LOI loi = null;

	@Before
	public void setUp() {
		s1.add("http://lsdis.cs.uga.edu/proj/semdis/testbed/#Company");
		s1.add("http://lsdis.cs.uga.edu/proj/semdis/testbed/#Thing");
		s1
				.add("http://lsdis.cs.uga.edu/proj/semdis/testbed/#Financial_Institute");

		s2.add("http://lsdis.cs.uga.edu/proj/semdis/testbed/#Company");
		s2.add("http://lsdis.cs.uga.edu/proj/semdis/testbed/#Thing");
		s2.add("http://lsdis.cs.uga.edu/proj/semdis/testbed/#Software");
	}

	@Test
	public final void testLOI() {
		loi = new LOI(s1, s2);
		Assert.assertNotNull(loi);
	}

	@Test
	public final void testCalculate() {
		loi = new LOI(s1, s2);
		Assert.assertTrue(loi.calculate());
		Assert.assertTrue(loi.isCalculated());
	}

	@Test
	public final void testGetSimilarity() {
		loi = new LOI(s1, s2);
		double sim = loi.getSimilarity();
		Assert.assertEquals(1d - ((4d - 2d) / 6d), sim);
	}

	@After
	public void tearDown() {
		loi = null;
	}
}
