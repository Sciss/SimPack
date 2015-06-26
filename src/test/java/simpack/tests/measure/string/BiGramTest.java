/*
 * $Id: BiGramTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.string.BiGram;

/**
 * @author Christoph Kiefer
 * @version $Id: BiGramTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class BiGramTest extends TestCase {

	public void testBiGram() {
		String str1 = "FoodService";
		String str2 = "GroceryFoodService";

		ISequenceAccessor<String> sa1 = new StringAccessor(str1.toLowerCase());
		ISequenceAccessor<String> sa2 = new StringAccessor(str2.toLowerCase());

		BiGram bigram = new BiGram(sa1, sa2);
		
		assertNotNull(bigram);
		assertEquals(bigram.getSimilarity(), (2d * 12d / 27d));
	}
	
	public void testBiGram2() {
		String str1 = "proceedings";
		String str2 = "inproceedings";

		StringAccessor sa1 = new StringAccessor(str1.toLowerCase());
		StringAccessor sa2 = new StringAccessor(str2.toLowerCase());

		BiGram bigram = new BiGram(sa1, sa2);
		
		assertNotNull(bigram);
		assertEquals(bigram.getSimilarity(), (2d * 11d / 22d));
	}
	
	public void testBiGram3() {
		String str1 = "inproceedings";
		String str2 = "inproceedings";

		StringAccessor sa1 = new StringAccessor(str1.toLowerCase());
		StringAccessor sa2 = new StringAccessor(str2.toLowerCase());

		BiGram bigram = new BiGram(sa1, sa2);
		
		assertNotNull(bigram);
		assertEquals(bigram.getSimilarity(), (2d * 14d / 24d));
	}
}
