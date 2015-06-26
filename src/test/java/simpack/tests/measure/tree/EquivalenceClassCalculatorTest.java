/*
 * $Id: EquivalenceClassCalculatorTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.tree;

import java.util.LinkedHashMap;
import java.util.List;

import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;
import simpack.util.tree.EquivalenceClassCalculator;
import simpack.util.tree.TreeUtil;

/**
 * @author Christoph Kiefer
 * @version $Id: EquivalenceClassCalculatorTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class EquivalenceClassCalculatorTest extends DefaultTreeTestCase {

	private List<ITreeNode> list1, list2;

	private EquivalenceClassCalculator cec;

	protected void setUp() throws Exception {
		super.setUp();

		list1 = TreeUtil.enumerationToList(tree1.postorderEnumeration());
		list2 = TreeUtil.enumerationToList(tree2.postorderEnumeration());

		cec = new EquivalenceClassCalculator(tree1, list1, tree2, list2);
		assertTrue(cec.calculate());
	}

	/*
	 * Test method for
	 * 'ch.toe.tree.CalculateEquivalenceClass.calculateEquivalenceClasses()'
	 */
	public void testCalculateEquivalenceClasses() {
		try {
			new EquivalenceClassCalculator(null, list1, tree2, list2);
			fail("Should throw NPE");
		} catch (InvalidElementException e) {
			fail();
		} catch (NullPointerException npe) {
			// success
		}
		try {
			new EquivalenceClassCalculator(tree1, null, tree2, list2);
			fail("Should throw NPE");
		} catch (NullPointerException npe) {
		} catch (InvalidElementException e) {
			fail();
		}
		try {
			new EquivalenceClassCalculator(tree1, list1, null, list2);
			fail("Should throw NPE");
		} catch (NullPointerException npe) {
		} catch (InvalidElementException e) {
			fail();
		}
		try {
			new EquivalenceClassCalculator(tree1, list1, tree2, null);
			fail("Should throw NPE");
		} catch (NullPointerException npe) {
		} catch (InvalidElementException e) {
			fail();
		}
	}

	public void testReuse() {
		EquivalenceClassCalculator calc = null;
		try {
			calc = new EquivalenceClassCalculator(tree1, list1, tree2, list2);
		} catch (NullPointerException e) {
			fail();
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(calc);
		assertTrue(calc.calculate());
		assertTrue(calc.isCalculated());

		LinkedHashMap<ITreeNode, Integer> code1A = calc
				.getEquivalenceClassesTree1();
		assertNotNull(code1A);
		LinkedHashMap<ITreeNode, Integer> code2A = calc
				.getEquivalenceClassesTree2();
		assertNotNull(code2A);
		LinkedHashMap<ITreeNode, Integer> code1B = calc
				.getEquivalenceClassesTree1();
		assertNotNull(code1B);
		LinkedHashMap<ITreeNode, Integer> code2B = calc
				.getEquivalenceClassesTree2();
		assertNotNull(code2B);

		assertSame(code1A, code1B);
		assertSame(code2A, code2B);
	}

	/*
	 * Test method for
	 * 'ch.toe.tree.CalculateEquivalenceClass.getEquivalenceClassesTree1()'
	 */
	public void testGetEquivalenceClassesTree1() {
		assertTrue(cec.isCalculated());

		LinkedHashMap<ITreeNode, Integer> code = cec
				.getEquivalenceClassesTree1();
		assertNotNull(code);

		assertEquals(code.get(t1n1).intValue(), 1);
		assertEquals(code.get(t1n2).intValue(), 1);
		assertEquals(code.get(t1n3).intValue(), 1);
		assertEquals(code.get(t1n4).intValue(), 2);
		assertEquals(code.get(t1n5).intValue(), 3);
		assertEquals(code.get(t1n6).intValue(), 4);
		assertEquals(code.get(t1n7).intValue(), 1);
		assertEquals(code.get(t1n8).intValue(), 5);
		assertEquals(code.get(t1n9).intValue(), 1);
		assertEquals(code.get(t1n10).intValue(), 1);
		assertEquals(code.get(t1n11).intValue(), 2);
		assertEquals(code.get(t1n12).intValue(), 3);
		assertEquals(code.get(t1n13).intValue(), 1);
		assertEquals(code.get(t1n14).intValue(), 4);
		assertEquals(code.get(t1n15).intValue(), 6);
	}

	/*
	 * Test method for
	 * 'ch.toe.tree.CalculateEquivalenceClass.getEquivalenceClassesTree2()'
	 */
	public void testGetEquivalenceClassesTree2() {
		assertTrue(cec.isCalculated());

		LinkedHashMap<ITreeNode, Integer> code = cec
				.getEquivalenceClassesTree2();
		assertNotNull(code);

		assertEquals(code.get(t2n1).intValue(), 1);
		assertEquals(code.get(t2n2).intValue(), 1);
		assertEquals(code.get(t2n3).intValue(), 7);
		assertEquals(code.get(t2n4).intValue(), 8);
		assertEquals(code.get(t2n5).intValue(), 1);
		assertEquals(code.get(t2n6).intValue(), 1);
		assertEquals(code.get(t2n7).intValue(), 1);
		assertEquals(code.get(t2n8).intValue(), 2);
		assertEquals(code.get(t2n9).intValue(), 3);
		assertEquals(code.get(t2n10).intValue(), 1);
		assertEquals(code.get(t2n11).intValue(), 4);
		assertEquals(code.get(t2n12).intValue(), 5);
		assertEquals(code.get(t2n13).intValue(), 1);
		assertEquals(code.get(t2n14).intValue(), 1);
		assertEquals(code.get(t2n15).intValue(), 1);
		assertEquals(code.get(t2n16).intValue(), 7);
		assertEquals(code.get(t2n17).intValue(), 9);
		assertEquals(code.get(t2n18).intValue(), 10);
	}
}
