/*
 * $Id: BottomUpMaximumSubtreeTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import simpack.accessor.tree.FamixTreeAccessor;
import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeAccessor;
import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;
import simpack.measure.tree.BottomUpMaximumSubtree;
import simpack.tests.accessor.tree.TreeAccessorTest;
import simpack.util.tree.TreeNode;
import simpack.util.tree.TreeUtil;
import simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator;
import ch.toe.famix.FAMIXInstance;

/**
 * @author Christoph Kiefer
 * @version $Id: BottomUpMaximumSubtreeTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class BottomUpMaximumSubtreeTest extends DefaultTreeTestCase {

	private TreeNode tree3, tree4;

	private TreeNode t3n1, t3n2, t3n3;

	private TreeNode t4n1, t4n2, t4n3, t4n4, t4n5, t4n6, t4n7, t4n8, t4n9,
			t4n10, t4n11, t4n12, t4n13;

	private BottomUpMaximumSubtree cstree1, cstree2;

	protected void setUp() throws Exception {
		super.setUp();

		cstree1 = new BottomUpMaximumSubtree(new SimpleTreeAccessor(tree1),
				new SimpleTreeAccessor(tree2));
		assertTrue(cstree1.calculate());

		tree3 = generateSampleT3();
		tree4 = generateSampleT4();
		cstree2 = new BottomUpMaximumSubtree(new SimpleTreeAccessor(tree3),
				new SimpleTreeAccessor(tree4));
		assertTrue(cstree2.calculate());
	}

	public void testRecalculation() {
		assertTrue(cstree1.calculate());
		assertTrue(cstree2.calculate());
	}

	public void testMatchingSubtree1() {
		assertNotNull(tree1);
		assertNotNull(tree2);

		assertNotNull(cstree1);
		assertTrue(cstree1.isCalculated());

		assertTrue(cstree1.getSubtreeRootNodesTree1().size() == 1);
		assertTrue(cstree1.getSubtreeRootNodesTree2().size() == 1);

		assertSame(cstree1.getSubtreeRootNodesTree1().get(0), t1n8);
		assertSame(cstree1.getSubtreeRootNodesTree2().get(0), t2n12);

		HashMap<TreeNode, TreeNode> subtreeMatching1 = null;
		try {
			subtreeMatching1 = cstree1.mapTrees(cstree1
					.getSubtreeRootNodesTree1().get(0), cstree1
					.getSubtreeRootNodesTree2().get(0));
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(subtreeMatching1);

		assertNotNull(subtreeMatching1.get(t1n8));
		assertSame(subtreeMatching1.get(t1n8), t2n12);
		assertNotNull(subtreeMatching1.get(t1n6));
		assertSame(subtreeMatching1.get(t1n6), t2n11);
		assertNotNull(subtreeMatching1.get(t1n7));
		assertSame(subtreeMatching1.get(t1n7), t2n5);
		assertNotNull(subtreeMatching1.get(t1n1));
		assertSame(subtreeMatching1.get(t1n1), t2n10);
		assertNotNull(subtreeMatching1.get(t1n5));
		assertSame(subtreeMatching1.get(t1n5), t2n9);
		assertNotNull(subtreeMatching1.get(t1n4));
		assertSame(subtreeMatching1.get(t1n4), t2n8);
		assertNotNull(subtreeMatching1.get(t1n2));
		assertSame(subtreeMatching1.get(t1n2), t2n6);
		assertNotNull(subtreeMatching1.get(t1n3));
		assertSame(subtreeMatching1.get(t1n3), t2n7);
	}

	public void testMatchingSubtree2() {
		assertNotNull(tree3);
		assertNotNull(tree4);

		assertNotNull(cstree2);
		assertTrue(cstree2.isCalculated());

		assertTrue(cstree2.getSubtreeRootNodesTree1().size() == 1);
		assertTrue(cstree2.getSubtreeRootNodesTree2().size() == 3);

		assertSame(cstree2.getSubtreeRootNodesTree1().get(0), t3n3);
		assertSame(cstree2.getSubtreeRootNodesTree2().get(0), t4n3);
		assertSame(cstree2.getSubtreeRootNodesTree2().get(1), t4n10);
		assertSame(cstree2.getSubtreeRootNodesTree2().get(2), t4n7);

		HashMap<TreeNode, TreeNode> subtreeMatching2 = null;
		try {
			subtreeMatching2 = cstree2.mapTrees(cstree2
					.getSubtreeRootNodesTree1().get(0), cstree2
					.getSubtreeRootNodesTree2().get(0));
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(subtreeMatching2);

		assertNotNull(subtreeMatching2.get(t3n3));
		assertSame(subtreeMatching2.get(t3n3), t4n3);
		assertNotNull(subtreeMatching2.get(t3n1));
		assertSame(subtreeMatching2.get(t3n1), t4n1);
		assertNotNull(subtreeMatching2.get(t3n2));
		assertSame(subtreeMatching2.get(t3n2), t4n2);

		subtreeMatching2 = null;
		try {
			subtreeMatching2 = cstree2.mapTrees(cstree2
					.getSubtreeRootNodesTree1().get(0), cstree2
					.getSubtreeRootNodesTree2().get(1));
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(subtreeMatching2);

		assertNotNull(subtreeMatching2.get(t3n3));
		assertSame(subtreeMatching2.get(t3n3), t4n10);
		assertNotNull(subtreeMatching2.get(t3n1));
		assertSame(subtreeMatching2.get(t3n1), t4n8);
		assertNotNull(subtreeMatching2.get(t3n2));
		assertSame(subtreeMatching2.get(t3n2), t4n9);

		subtreeMatching2 = null;
		try {
			subtreeMatching2 = cstree2.mapTrees(cstree2
					.getSubtreeRootNodesTree1().get(0), cstree2
					.getSubtreeRootNodesTree2().get(2));
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(subtreeMatching2);

		assertNotNull(subtreeMatching2.get(t3n3));
		assertSame(subtreeMatching2.get(t3n3), t4n7);
		assertNotNull(subtreeMatching2.get(t3n1));
		assertSame(subtreeMatching2.get(t3n1), t4n5);
		assertNotNull(subtreeMatching2.get(t3n2));
		assertSame(subtreeMatching2.get(t3n2), t4n6);
	}

	public void testReuse() {
		BottomUpMaximumSubtree cstree = null;
		try {
			cstree = new BottomUpMaximumSubtree(new SimpleTreeAccessor(tree1),
					new SimpleTreeAccessor(tree2));
		} catch (NullPointerException e) {
			fail("NullPointerException");
		} catch (InvalidElementException e) {
			fail("TreeNodeTypeException");
		}
		assertNotNull(cstree);
		assertTrue(cstree.calculate());
		assertTrue(cstree.isCalculated());

		assertEquals(cstree.getSubtreeRootNodesTree1().size(), cstree1
				.getSubtreeRootNodesTree1().size());
		assertEquals(cstree.getSubtreeRootNodesTree2().size(), cstree1
				.getSubtreeRootNodesTree2().size());

		Iterator<ITreeNode> iter1 = cstree.getSubtreeRootNodesTree1()
				.iterator();
		while (iter1.hasNext()) {
			ITreeNode a = iter1.next();
			assertTrue(cstree1.getSubtreeRootNodesTree1().contains(a));

			Iterator<ITreeNode> iter2 = cstree.getSubtreeRootNodesTree2()
					.iterator();
			while (iter2.hasNext()) {
				ITreeNode b = iter2.next();
				assertTrue(cstree1.getSubtreeRootNodesTree2().contains(b));

				HashMap<TreeNode, TreeNode> map1 = null;
				try {
					map1 = cstree.mapTrees(a, b);
				} catch (InvalidElementException e) {
					fail();
				}
				assertNotNull(map1);

				HashMap<TreeNode, TreeNode> map2 = null;
				try {
					map2 = cstree1.mapTrees(a, b);
				} catch (InvalidElementException e) {
					fail();
				}
				assertNotNull(map2);

				assertEquals(map1, map2);
			}

		}
	}

	/**
	 * Double check test on p.234 (code piece 234a / 234b) in "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 */
	public void testValiente() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		HashMap<TreeNode, TreeNode> subtreeMatching1 = null;
		try {
			subtreeMatching1 = cstree1.mapTrees(cstree1
					.getSubtreeRootNodesTree1().get(0), cstree1
					.getSubtreeRootNodesTree2().get(0));
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(subtreeMatching1);

		// piece 234a
		boolean found = false;
		List<ITreeNode> list1 = null;
		try {
			list1 = TreeUtil.enumerationToList(tree1.postorderEnumeration());
		} catch (InvalidElementException e) {
			fail("TreeNodeTypeException");
		}
		assertNotNull(list1);
		List<ITreeNode> list2 = null;
		try {
			list2 = TreeUtil.enumerationToList(tree2.postorderEnumeration());
		} catch (InvalidElementException e) {
			fail("TreeNodeTypeException");
		}
		assertNotNull(list2);

		ListIterator<ITreeNode> liter1 = list1.listIterator();
		while (liter1.hasNext()) {
			ITreeNode v = liter1.next();
			assertNotNull(v);
			if (subtreeMatching1.get(v) != null) {
				ListIterator<ITreeNode> liter2 = list2.listIterator();
				while (liter2.hasNext()) {
					ITreeNode w = liter2.next();
					assertNotNull(w);
					if (!v.equals(w)
							&& subtreeMatching1.get(v).equals(
									subtreeMatching1.get(w)))
						fail("Error in subtree isomorphism matching");
				}
				liter2 = list2.listIterator();
				while (liter2.hasNext()) {
					ITreeNode w = liter2.next();
					assertNotNull(w);
					if (!v.isRoot() && !w.isRoot()) {
						assertNotNull(v.getParent());
						assertNotNull(w.getParent());
						if (subtreeMatching1.get(v.getParent()) != null
								&& subtreeMatching1.get(v.getParent()).equals(
										w.getParent())) {
							found = false;
							List<ITreeNode> children = null;
							try {
								children = TreeUtil.enumerationToList(w
										.getParent().children());
							} catch (InvalidElementException e) {
								fail("TreeNodeTypeException");
							}
							assertNotNull(children);
							ListIterator<ITreeNode> liter3 = children
									.listIterator();
							while (liter3.hasNext()) {
								ITreeNode z = liter3.next();
								if (subtreeMatching1.get(v).equals(z))
									found = true;
							}
							if (!found)
								fail("Error in subtree isomorphism matching");
						}
					}
				}
			}
		}

		// piece 234b
		liter1 = list1.listIterator();
		ITreeNode v = cstree1.getSubtreeRootNodesTree1().get(0);
		while (liter1.hasNext()) {
			ITreeNode x = liter1.next();
			ListIterator<ITreeNode> liter2 = list2.listIterator();
			while (liter2.hasNext()) {
				ITreeNode y = liter2.next();
				if (cstree1.getEquivalenceClassTree1().get(x).equals(
						cstree1.getEquivalenceClassTree2().get(y))
						&& cstree1.getSizeTree1().get(x) > cstree1
								.getSizeTree1().get(v))
					fail("Error in subtree isomorphism matching");
			}
		}
	}

	/**
	 * Tests calculation of bottom-up subtree with a <code>FAMIXAccessor</code>.
	 */
	public void testBottomUpFamixAccessor() {
		FAMIXInstance instance1 = TreeAccessorTest.generateFamixModel();
		ITreeAccessor accessor1 = new FamixTreeAccessor(instance1);
		FAMIXInstance instance2 = TreeAccessorTest.generateFamixModel();
		ITreeAccessor accessor2 = new FamixTreeAccessor(instance2);
		BottomUpMaximumSubtree calc = null;
		try {
			calc = new BottomUpMaximumSubtree(accessor1, accessor2,
					new AlwaysTrueTreeNodeComparator(), true, false);
		} catch (NullPointerException e) {
			fail();
		} catch (InvalidElementException e) {
			fail();
		}
		assertNotNull(calc);
		assertTrue(calc.calculate());
		assertTrue(calc.isCalculated());
		assertEquals(calc.getSimilarity(), new Double(1));
	}

	/**
	 * Constructs the sample tree T3
	 * 
	 * @return sample tree T3
	 */
	private TreeNode generateSampleT3() {
		t3n1 = new TreeNode(new String("t3n1"));
		t3n2 = new TreeNode(new String("t3n2"));
		t3n3 = new TreeNode(new String("t3n3"));

		// root
		t3n3.add(t3n1);
		t3n3.add(t3n2);

		return t3n3;
	}

	/**
	 * Constructs the sample tree T4
	 * 
	 * @return sample tree T4
	 */
	private TreeNode generateSampleT4() {
		t4n1 = new TreeNode(new String("t4n1"));
		t4n2 = new TreeNode(new String("t4n2"));
		t4n3 = new TreeNode(new String("t4n3"));
		t4n4 = new TreeNode(new String("t4n4"));
		t4n5 = new TreeNode(new String("t4n5"));
		t4n6 = new TreeNode(new String("t4n6"));
		t4n7 = new TreeNode(new String("t4n7"));
		t4n8 = new TreeNode(new String("t4n8"));
		t4n9 = new TreeNode(new String("t4n9"));
		t4n10 = new TreeNode(new String("t4n10"));
		t4n11 = new TreeNode(new String("t4n11"));
		t4n12 = new TreeNode(new String("t4n12"));
		t4n13 = new TreeNode(new String("t4n13"));

		// right
		t4n3.add(t4n1);
		t4n3.add(t4n2);
		t4n4.add(t4n3);

		// left
		t4n7.add(t4n5);
		t4n7.add(t4n6);
		t4n10.add(t4n8);
		t4n10.add(t4n9);
		t4n12.add(t4n7);
		t4n12.add(t4n10);
		t4n12.add(t4n11);

		// root
		t4n13.add(t4n4);
		t4n13.add(t4n12);

		return t4n13;
	}
}
