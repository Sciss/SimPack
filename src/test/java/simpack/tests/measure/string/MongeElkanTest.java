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
package simpack.tests.measure.string;

import junit.framework.TestCase;
import simpack.accessor.string.StringAccessor;
import simpack.measure.string.MongeElkan;

/**
 * @author Christoph Kiefer
 * @version $Id: MongeElkanTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class MongeElkanTest extends TestCase {

	public void testMongeElkan() {
		String str1 = "FoodService";
		String str2 = "GroceryFoodService";

		StringAccessor sa1 = new StringAccessor(str1.toLowerCase());
		StringAccessor sa2 = new StringAccessor(str2.toLowerCase());

		MongeElkan me = new MongeElkan(sa1, sa2);

		assertNotNull(me);
		assertEquals(me.getSimilarity(), 0.6111111111111112);
	}
}