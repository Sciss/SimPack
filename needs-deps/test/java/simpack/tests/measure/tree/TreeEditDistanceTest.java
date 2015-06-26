/*
 * $Id: TreeEditDistanceTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import junit.framework.TestCase;
import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;
import simpack.measure.tree.TreeEditDistance;
import simpack.util.conversion.CommonDistanceConversion;
import simpack.util.conversion.LogarithmicDistanceConversion;
import simpack.util.conversion.WorstCaseDistanceConversion;
import simpack.util.tree.TreeNode;
import simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator;

/**
 * @author Christoph Kiefer
 * @version $Id: TreeEditDistanceTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TreeEditDistanceTest extends TestCase {

	private ITreeNode tree1, tree2;

	private ITreeNode t1n1, t1n2, t1n3, t1n4, t1n5;

	private ITreeNode t2n1, t2n2, t2n3, t2n4, t2n5, t2n6, t2n7;

	private TreeEditDistance calc, calc1;

	protected void setUp() throws Exception {
		super.setUp();

		tree1 = generateSampleT1();
		tree2 = generateSampleT2();

		calc = new TreeEditDistance(new SimpleTreeAccessor(tree1),
				new SimpleTreeAccessor(tree2), null, null, null, 0d);
		calc1 = new TreeEditDistance(new SimpleTreeAccessor(tree1),
				new SimpleTreeAccessor(tree1), null, null, null, 0d);
		assertTrue(calc.calculate());
		assertTrue(calc.isCalculated());
		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());
	}

	public void testSampleTree() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		assertNotNull(calc);
		assertTrue(calc.calculate());
		assertTrue(calc.isCalculated());
		assertEquals(calc.getTreeEditDistance(), 4d);
	}

	public void testZeroDistance() {
		assertTrue(calc1.isCalculated());
		assertEquals(calc1.getTreeEditDistance(), 0d);
	}

	public void testEditDistance() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		try {
			calc = new TreeEditDistance(new SimpleTreeAccessor(tree1),
					new SimpleTreeAccessor(tree2),
					new AlwaysTrueTreeNodeComparator(),
					new WorstCaseDistanceConversion());

			assertNotNull(calc);
			assertTrue(calc.calculate());
			assertTrue(calc.isCalculated());
			assertEquals(calc.getTreeEditDistance(), 4d);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}

	public void testWorstCaseDistanceConversion() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		try {
			calc = new TreeEditDistance(new SimpleTreeAccessor(tree1),
					new SimpleTreeAccessor(tree2),
					new AlwaysTrueTreeNodeComparator(),
					new WorstCaseDistanceConversion());

			assertNotNull(calc);
			assertTrue(calc.calculate());
			assertTrue(calc.isCalculated());
			assertEquals(calc.getSimilarity(), (12d - 4d) / 12d);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}

	public void testCommonDistanceConversion() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		try {
			calc = new TreeEditDistance(new SimpleTreeAccessor(tree1),
					new SimpleTreeAccessor(tree2),
					new AlwaysTrueTreeNodeComparator(),
					new CommonDistanceConversion());

			assertNotNull(calc);
			assertTrue(calc.calculate());
			assertTrue(calc.isCalculated());
			assertEquals(calc.getSimilarity(), 1d / (1d + 4d));

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}

	public void testLogarithmicDistanceConversion() {
		assertNotNull(tree1);
		assertNotNull(tree2);
		try {
			calc = new TreeEditDistance(new SimpleTreeAccessor(tree1),
					new SimpleTreeAccessor(tree2),
					new AlwaysTrueTreeNodeComparator(),
					new LogarithmicDistanceConversion());

			assertNotNull(calc);
			assertTrue(calc.calculate());
			assertTrue(calc.isCalculated());
			assertEquals(calc.getSimilarity(), Math.exp(-(4d * 4d)));

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs the sample tree T1 on page 56 (Fig. 2.1) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T1
	 */
	private ITreeNode generateSampleT1() {
		t1n1 = new TreeNode(new String("t1n1"));
		t1n2 = new TreeNode(new String("t1n2"));
		t1n3 = new TreeNode(new String("t1n3"));
		t1n4 = new TreeNode(new String("t1n4"));
		t1n5 = new TreeNode(new String("t1n5"));

		// left
		t1n2.add(t1n3);
		t1n2.add(t1n4);

		// root
		t1n1.add(t1n2);
		t1n1.add(t1n5);

		return t1n1;
	}

	/**
	 * Constructs the sample tree T1 on page 56 (Fig. 2.1) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T2
	 */
	private ITreeNode generateSampleT2() {
		t2n1 = new TreeNode(new String("t2n1"));
		t2n2 = new TreeNode(new String("t2n2"));
		t2n3 = new TreeNode(new String("t2n3"));
		t2n4 = new TreeNode(new String("t2n4"));
		t2n5 = new TreeNode(new String("t2n5"));
		t2n6 = new TreeNode(new String("t2n6"));
		t2n7 = new TreeNode(new String("t2n7"));

		// right
		t2n4.add(t2n5);
		t2n4.add(t2n6);

		t2n3.add(t2n4);
		t2n3.add(t2n7);

		// root
		t2n1.add(t2n2);
		t2n1.add(t2n3);

		return t2n1;
	}
}
