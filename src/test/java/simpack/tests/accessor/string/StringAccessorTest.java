/*
 * $Id: StringAccessorTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.accessor.string;

import java.util.List;

import simpack.accessor.string.StringAccessor;
import simpack.tokenizer.SplittedStringTokenizer;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: StringAccessorTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class StringAccessorTest extends TestCase {

	public void testStringAccessor() {
		StringAccessor sa = new StringAccessor("Test");
		List<String> lsa = sa.getSequence();

		assertEquals(lsa.get(0), "T");
		assertEquals(lsa.get(1), "e");
		assertEquals(lsa.get(2), "s");
		assertEquals(lsa.get(3), "t");
	}

	public void testStringAccessorWithSplittedStringAccessor() {
		StringAccessor sa = new StringAccessor("TestABaumAFeierabend",
				new SplittedStringTokenizer("A"));
		List<String> lsa = sa.getSequence();

		assertEquals(lsa.get(0), "Test");
		assertEquals(lsa.get(1), "Baum");
		assertEquals(lsa.get(2), "Feierabend");
	}

	public void testGetElementFrequencies() {
		StringAccessor sa = new StringAccessor("test");
		int[] freqsFound = sa.getElementFrequencies();
		int[] freqsExpected = { 2, 1, 1, 2 };

		for (int i = 0; i < freqsFound.length; i++) {
			assertEquals(freqsExpected[i], freqsFound[i]);
		}
	}

	public void testGetElementFrequencyString() {
		StringAccessor sa = new StringAccessor("Querbeet");
		assertEquals(3, sa.getElementFrequency("e"));
	}
}
