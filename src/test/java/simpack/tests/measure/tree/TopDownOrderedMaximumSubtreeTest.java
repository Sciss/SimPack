/*
 * $Id: TopDownOrderedMaximumSubtreeTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Enumeration;
import java.util.HashMap;

import junit.framework.TestCase;
import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;
import simpack.measure.tree.TopDownOrderedMaximumSubtree;
import simpack.util.tree.TreeNode;
import simpack.util.tree.comparator.NamedTreeNodeComparator;

/**
 * @author Christoph Kiefer
 * @version $Id: TopDownOrderedMaximumSubtreeTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TopDownOrderedMaximumSubtreeTest extends TestCase {

	private ITreeNode tree1, tree2;

	private ITreeNode t1n1, t1n2, t1n3, t1n4, t1n5, t1n6, t1n7, t1n8, t1n9,
			t1n10, t1n11, t1n12;

	private ITreeNode t2n1, t2n2, t2n3, t2n4, t2n5, t2n6, t2n7, t2n8, t2n9,
			t2n10, t2n11, t2n12, t2n13, t2n14, t2n15, t2n16, t2n17, t2n18;

	private TopDownOrderedMaximumSubtree cstree1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		tree1 = generateSampleT1();
		tree2 = generateSampleT2();
		cstree1 = new TopDownOrderedMaximumSubtree(
				new SimpleTreeAccessor(tree1), new SimpleTreeAccessor(tree2));
		assertTrue(cstree1.calculate());
		assertTrue(cstree1.isCalculated());
	}

	public void testCalculation() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		assertNotNull(cstree1);

		HashMap<ITreeNode, ITreeNode> map = cstree1.getMappedTrees();
		assertNotNull(map);

		assertSame(map.get(t1n12), t2n18);
		assertSame(map.get(t1n6), t2n4);
		assertSame(map.get(t1n5), t2n1);
		assertSame(map.get(t1n11), t2n12);
		assertSame(map.get(t1n9), t2n5);
		assertSame(map.get(t1n10), t2n11);

		NamedTreeNodeComparator comp = new NamedTreeNodeComparator();

		ITreeNode matched1 = cstree1.getMatchedTree1();
		Enumeration e = matched1.postorderEnumeration();
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t1n5), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t1n6), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t1n9), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t1n10), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t1n11), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t1n12), 0);

		ITreeNode matched2 = cstree1.getMatchedTree2();
		e = matched2.postorderEnumeration();
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t2n1), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t2n4), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t2n5), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t2n11), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t2n12), 0);
		assertEquals(comp.compare((ITreeNode) e.nextElement(), t2n18), 0);
	}

	public void testOwnComparator() {
		NamedTreeNodeComparator comp = new NamedTreeNodeComparator();

		TopDownOrderedMaximumSubtree calc = null;
		try {
			calc = new TopDownOrderedMaximumSubtree(new SimpleTreeAccessor(
					tree1), new SimpleTreeAccessor(tree2), comp);
		} catch (NullPointerException e) {
			fail();
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(calc);
		assertTrue(calc.calculate());
		assertTrue(calc.isCalculated());

		checkSameCalc(calc);
	}

	public void testRecalc() {
		TopDownOrderedMaximumSubtree calc = null;
		try {
			calc = new TopDownOrderedMaximumSubtree(new SimpleTreeAccessor(
					tree1), new SimpleTreeAccessor(tree2));
		} catch (NullPointerException e) {
			fail();
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(calc);
		assertTrue(calc.calculate());
		assertTrue(calc.isCalculated());

		checkSameCalc(calc);
	}

	/**
	 * Assert that cstree1 matches the given calculation
	 * 
	 * @param calc
	 */
	private void checkSameCalc(TopDownOrderedMaximumSubtree calc) {
		assertEquals(cstree1.getMappedTrees(), calc.getMappedTrees());

		Enumeration enum1 = cstree1.getMatchedTree1().postorderEnumeration();
		Enumeration enum2 = calc.getMatchedTree1().postorderEnumeration();
		assertTrue(checkSameEnum(enum1, enum2));

		enum1 = cstree1.getMatchedTree2().postorderEnumeration();
		enum2 = calc.getMatchedTree2().postorderEnumeration();
		assertTrue(checkSameEnum(enum1, enum2));
	}

	/**
	 * Assert and check that the two given Enumerations contain the same objects
	 * in the same order
	 * 
	 * @param enum1
	 * @param enum2
	 * @return true if Enumerations are equal
	 */
	private boolean checkSameEnum(Enumeration enum1, Enumeration enum2) {
		NamedTreeNodeComparator comp = new NamedTreeNodeComparator();
		while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
			assertEquals(comp.compare((ITreeNode) enum1.nextElement(),
					(ITreeNode) enum2.nextElement()), 0);
		}
		assertFalse(enum1.hasMoreElements());
		assertFalse(enum2.hasMoreElements());
		return true;
	}

	/**
	 * Constructs the sample tree T1 on page 207 (Fig. 4.13) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T1
	 */
	private ITreeNode generateSampleT1() {
		t1n1 = new TreeNode(new String("t1n1"));
		t1n2 = new TreeNode(new String("t1n2"));
		t1n3 = new TreeNode(new String("t1n3"));
		t1n4 = new TreeNode(new String("t1n4"));
		t1n5 = new TreeNode(new String("C"));
		t1n6 = new TreeNode(new String("B"));
		t1n7 = new TreeNode(new String("t1n7"));
		t1n8 = new TreeNode(new String("t1n8"));
		t1n9 = new TreeNode(new String("E"));
		t1n10 = new TreeNode(new String("F"));
		t1n11 = new TreeNode(new String("D"));
		t1n12 = new TreeNode(new String("A"));

		// left
		t1n4.add(t1n2);
		t1n4.add(t1n3);
		t1n5.add(t1n1);
		t1n5.add(t1n4);
		t1n6.add(t1n5);

		// right
		t1n9.add(t1n7);
		t1n9.add(t1n8);
		t1n11.add(t1n9);
		t1n11.add(t1n10);

		// root
		t1n12.add(t1n6);
		t1n12.add(t1n11);

		return t1n12;
	}

	/**
	 * Constructs the sample tree T2 on page 207 (Fig. 4.13) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T2
	 */
	private ITreeNode generateSampleT2() {
		t2n1 = new TreeNode(new String("C"));
		t2n2 = new TreeNode(new String("t2n2"));
		t2n3 = new TreeNode(new String("t2n3"));
		t2n4 = new TreeNode(new String("B"));
		t2n5 = new TreeNode(new String("E"));
		t2n6 = new TreeNode(new String("t2n6"));
		t2n7 = new TreeNode(new String("t2n7"));
		t2n8 = new TreeNode(new String("t2n8"));
		t2n9 = new TreeNode(new String("t2n9"));
		t2n10 = new TreeNode(new String("t2n10"));
		t2n11 = new TreeNode(new String("F"));
		t2n12 = new TreeNode(new String("D"));
		t2n13 = new TreeNode(new String("t2n13"));
		t2n14 = new TreeNode(new String("t2n14"));
		t2n15 = new TreeNode(new String("t2n15"));
		t2n16 = new TreeNode(new String("t2n16"));
		t2n17 = new TreeNode(new String("t2n17"));
		t2n18 = new TreeNode(new String("A"));

		// left
		t2n3.add(t2n2);
		t2n4.add(t2n1);
		t2n4.add(t2n3);

		// middle
		t2n8.add(t2n6);
		t2n8.add(t2n7);
		t2n9.add(t2n8);
		t2n11.add(t2n9);
		t2n11.add(t2n10);
		t2n12.add(t2n5);
		t2n12.add(t2n11);

		// right
		t2n16.add(t2n15);
		t2n17.add(t2n13);
		t2n17.add(t2n14);
		t2n17.add(t2n16);

		// root
		t2n18.add(t2n4);
		t2n18.add(t2n12);
		t2n18.add(t2n17);

		return t2n18;
	}
}
