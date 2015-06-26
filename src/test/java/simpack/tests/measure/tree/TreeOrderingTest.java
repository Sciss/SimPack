/*
 * $Id: TreeOrderingTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.util.tree.TreeNode;

/**
 * @author Christoph Kiefer
 * @version $Id: TreeOrderingTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TreeOrderingTest extends TestCase {

	private ITreeNode tree1;

	private ITreeNode t1n1, t1n2, t1n3, t1n4, t1n5, t1n6, t1n7;

	private ArrayList<TreeNode> preorderTree1, wrongPreorderTree1;

	private ArrayList<TreeNode> postorderTree1, wrongPostorderTree1;

	protected void setUp() throws Exception {
		super.setUp();
		tree1 = generateSampleT1();
		generatePreorderSequenceT1();
		generateWrongPreorderSequenceT1();
		generatePostorderSequenceT1();
		generateWrongPostorderSequenceT1();
	}

	public void testPreorderSampleTreeT1() {
		assertNotNull(tree1);
		assertNotNull(preorderTree1);
		List<Object> order1 = new SimpleTreeAccessor(tree1)
				.getPreorderSequence();

		assertEquals(preorderTree1.size(), order1.size());

		int pos = 0;
		for (TreeNode node1 : preorderTree1) {
			TreeNode node2 = (TreeNode) order1.get(pos++);
			assertEquals(node1.getUserObject(), node2.getUserObject());
		}
	}

	public void testWrongPreorderSampleTreeT1() {
		assertNotNull(tree1);
		assertNotNull(wrongPreorderTree1);
		List<Object> order1 = new SimpleTreeAccessor(tree1)
				.getPreorderSequence();

		assertEquals(wrongPreorderTree1.size(), order1.size());

		int pos = 0;
		for (TreeNode node1 : wrongPreorderTree1) {
			TreeNode node2 = (TreeNode) order1.get(pos);
			if (pos < 4) {
				assertEquals(node1.getUserObject(), node2.getUserObject());
			} else {
				assertNotSame(node1.getUserObject().toString(), node2
						.getUserObject().toString());
			}
			pos++;
		}
	}

	public void testPostorderSampleTreeT1() {
		assertNotNull(tree1);
		assertNotNull(postorderTree1);
		List<Object> order1 = new SimpleTreeAccessor(tree1)
				.getPostorderSequence();

		assertEquals(order1.size(), postorderTree1.size());

		int pos = 0;
		for (TreeNode node1 : postorderTree1) {
			TreeNode node2 = (TreeNode) order1.get(pos++);
			assertEquals(node1.getUserObject(), node2.getUserObject());
		}
	}

	public void testWrongPostorderSampleTreeT1() {
		assertNotNull(tree1);
		assertNotNull(wrongPostorderTree1);
		List<Object> order1 = new SimpleTreeAccessor(tree1)
				.getPostorderSequence();

		assertEquals(wrongPostorderTree1.size(), order1.size());

		int pos = 0;
		for (TreeNode node1 : wrongPostorderTree1) {
			TreeNode node2 = (TreeNode) order1.get(pos);
			if (pos < 4) {
				assertEquals(node1.getUserObject(), node2.getUserObject());
			} else {
				assertNotSame(node1.getUserObject().toString(), node2
						.getUserObject().toString());
			}
			pos++;
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
		t1n6 = new TreeNode(new String("t1n6"));
		t1n7 = new TreeNode(new String("t1n7"));

		// right
		t1n4.add(t1n5);
		t1n4.add(t1n6);
		t1n3.add(t1n4);
		t1n3.add(t1n7);

		// root
		t1n1.add(t1n2);
		t1n1.add(t1n3);

		return t1n1;
	}

	private void generatePreorderSequenceT1() {
		preorderTree1 = new ArrayList<TreeNode>();
		preorderTree1.add(new TreeNode(new String("t1n1")));
		preorderTree1.add(new TreeNode(new String("t1n2")));
		preorderTree1.add(new TreeNode(new String("t1n3")));
		preorderTree1.add(new TreeNode(new String("t1n4")));
		preorderTree1.add(new TreeNode(new String("t1n5")));
		preorderTree1.add(new TreeNode(new String("t1n6")));
		preorderTree1.add(new TreeNode(new String("t1n7")));
	}

	private void generateWrongPreorderSequenceT1() {
		wrongPreorderTree1 = new ArrayList<TreeNode>();
		wrongPreorderTree1.add(new TreeNode(new String("t1n1")));
		wrongPreorderTree1.add(new TreeNode(new String("t1n2")));
		wrongPreorderTree1.add(new TreeNode(new String("t1n3")));
		wrongPreorderTree1.add(new TreeNode(new String("t1n4")));
		wrongPreorderTree1.add(new TreeNode(new String("t1n5")));
		wrongPreorderTree1.add(new TreeNode(new String("t1n7")));
		wrongPreorderTree1.add(new TreeNode(new String("t1n6")));
	}

	private void generatePostorderSequenceT1() {
		postorderTree1 = new ArrayList<TreeNode>();
		postorderTree1.add(new TreeNode(new String("t1n2")));
		postorderTree1.add(new TreeNode(new String("t1n5")));
		postorderTree1.add(new TreeNode(new String("t1n6")));
		postorderTree1.add(new TreeNode(new String("t1n4")));
		postorderTree1.add(new TreeNode(new String("t1n7")));
		postorderTree1.add(new TreeNode(new String("t1n3")));
		postorderTree1.add(new TreeNode(new String("t1n1")));
	}

	private void generateWrongPostorderSequenceT1() {
		wrongPostorderTree1 = new ArrayList<TreeNode>();
		wrongPostorderTree1.add(new TreeNode(new String("t1n2")));
		wrongPostorderTree1.add(new TreeNode(new String("t1n5")));
		wrongPostorderTree1.add(new TreeNode(new String("t1n6")));
		wrongPostorderTree1.add(new TreeNode(new String("t1n4")));
		wrongPostorderTree1.add(new TreeNode(new String("t1n7")));
		wrongPostorderTree1.add(new TreeNode(new String("t1n1")));
		wrongPostorderTree1.add(new TreeNode(new String("t1n3")));
	}
}