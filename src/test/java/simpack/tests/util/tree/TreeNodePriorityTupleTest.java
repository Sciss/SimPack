/*
 * $Id: TreeNodePriorityTupleTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.util.tree;

import junit.framework.TestCase;
import simpack.api.ITreeNode;
import simpack.util.tree.NodePriority;
import simpack.util.tree.TreeNode;
import simpack.util.tree.TreeNodePriorityTuple;

/**
 * @author Christoph Kiefer
 * @version $Id: TreeNodePriorityTupleTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TreeNodePriorityTupleTest extends TestCase {

	private ITreeNode node;

	private NodePriority priority1, priority2, priority3;

	private TreeNodePriorityTuple tp1, tp2, tp3;

	protected void setUp() throws Exception {
		super.setUp();
		node = new TreeNode();
		priority1 = new NodePriority(new Integer(7), new Integer(13));
		priority2 = new NodePriority(new Integer(7), new Integer(13));
		priority3 = new NodePriority(new Integer(8), new Integer(13));
		tp1 = new TreeNodePriorityTuple(node, priority1);
		tp2 = new TreeNodePriorityTuple(node, priority2);
		tp3 = new TreeNodePriorityTuple(node, priority3);
	}

	public void testTreeNodePriorityTuple() {
		// NULL
		assertNotNull(tp1);
		assertNotNull(tp1.getNode());
		assertNotNull(tp1.getPriority());

		// EQUALS input
		assertSame(tp1.getNode(), node);
		assertSame(tp1.getPriority(), priority1);
	}

	public void trestTreeNodeEquals() {
		// NULL
		assertNotNull(tp1);
		assertNotNull(tp1.getNode());
		assertNotNull(tp1.getPriority());
		assertNotNull(tp2);
		assertNotNull(tp2.getNode());
		assertNotNull(tp2.getPriority());
		assertNotNull(tp3);
		assertNotNull(tp3.getNode());
		assertNotNull(tp3.getPriority());

		assertTrue(tp1.equals(tp2));
		assertTrue(tp2.equals(tp1));
		assertFalse(tp1.equals(tp3));
		assertFalse(tp3.equals(tp2));
	}
}
