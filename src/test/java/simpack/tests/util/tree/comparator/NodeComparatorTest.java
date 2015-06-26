/*
 * $Id: NodeComparatorTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.util.tree.comparator;

import java.util.Comparator;
import java.util.TreeSet;

import junit.framework.TestCase;
import simpack.api.ITreeNode;
import simpack.util.tree.NodePriority;
import simpack.util.tree.TreeNode;
import simpack.util.tree.TreeNodePriorityTuple;
import simpack.util.tree.comparator.NodeComparator;

/**
 * @author Christoph Kiefer
 * @version $Id: NodeComparatorTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class NodeComparatorTest extends TestCase {

	private NodeComparator nc;

	private ITreeNode node;

	private TreeNodePriorityTuple t1, t2, t3, t4, t5;

	private NodePriority np1, np2, np3, np4, np5;

	private TreeSet<TreeNodePriorityTuple> queue;

	protected void setUp() throws Exception {
		super.setUp();
		nc = new NodeComparator();
		node = new TreeNode();
		np1 = new NodePriority(1, 1);
		np2 = new NodePriority(4, 8);
		np3 = new NodePriority(4, 3);
		np4 = new NodePriority(8, 5);
		np5 = new NodePriority(17, 10);
		t1 = new TreeNodePriorityTuple(node, np1);
		t2 = new TreeNodePriorityTuple(node, np2);
		t3 = new TreeNodePriorityTuple(node, np3);
		t4 = new TreeNodePriorityTuple(node, np4);
		t5 = new TreeNodePriorityTuple(node, np5);
		queue = new TreeSet<TreeNodePriorityTuple>(
				(Comparator<TreeNodePriorityTuple>) nc);
		queue.add(t1);
		queue.add(t2);
		queue.add(t3);
		queue.add(t4);
		queue.add(t5);
	}

	public void testQueueing() {
		assertTrue(queue.size() == 5);

		assertSame(queue.first(), t5);
		assertTrue(queue.remove(t5));
		assertSame(queue.first(), t4);
		assertTrue(queue.remove(t4));
		assertSame(queue.first(), t3);
		assertTrue(queue.remove(t3));
		assertSame(queue.first(), t2);
		assertTrue(queue.remove(t2));
		assertSame(queue.first(), t1);
		assertTrue(queue.remove(t1));

		assertTrue(queue.size() == 0);
	}

	/*
	 * Test method for
	 * 'ch.toe.tree.NodeComparator.compare(TreeNodePriorityTuple,
	 * TreeNodePriorityTuple)'
	 */
	public void testCompareTreeNodePriorityTupleTreeNodePriorityTuple() {
		// compare SELF
		assertTrue(nc.compare(t1, t1) == 0);
		assertTrue(nc.compare(t2, t2) == 0);
		assertTrue(nc.compare(t3, t3) == 0);
		assertTrue(nc.compare(t4, t4) == 0);
		assertTrue(nc.compare(t5, t5) == 0);

		// assure ranking
		assertTrue(nc.compare(t1, t2) == 1);
		assertTrue(nc.compare(t2, t1) == -1);

		assertTrue(nc.compare(t2, t3) == 1);
		assertTrue(nc.compare(t3, t2) == -1);

		assertTrue(nc.compare(t3, t4) == 1);
		assertTrue(nc.compare(t4, t3) == -1);

		assertTrue(nc.compare(t4, t5) == 1);
		assertTrue(nc.compare(t5, t4) == -1);
	}

	/*
	 * Test method for 'ch.toe.tree.NodeComparator.compare(NodePriority,
	 * NodePriority)'
	 */
	public void testCompareNodePriorityNodePriority() {
		// compare SELF
		assertTrue(NodePriority.compare(np1, np1) == 0);
		assertTrue(NodePriority.compare(np2, np2) == 0);
		assertTrue(NodePriority.compare(np3, np3) == 0);
		assertTrue(NodePriority.compare(np4, np4) == 0);
		assertTrue(NodePriority.compare(np5, np5) == 0);

		// assure ranking
		assertTrue(NodePriority.compare(np1, np2) == 1);
		assertTrue(NodePriority.compare(np2, np1) == -1);

		assertTrue(NodePriority.compare(np2, np3) == 1);
		assertTrue(NodePriority.compare(np3, np2) == -1);

		assertTrue(NodePriority.compare(np3, np4) == 1);
		assertTrue(NodePriority.compare(np4, np3) == -1);

		assertTrue(NodePriority.compare(np4, np5) == 1);
		assertTrue(NodePriority.compare(np5, np4) == -1);
	}

}
