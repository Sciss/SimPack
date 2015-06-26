/*
 * $Id: BuildTreeVisitorTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Map;

import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.measure.tree.BottomUpMaximumSubtree;
import simpack.util.tree.TreeNode;
import simpack.util.tree.TreeUtil;
import simpack.util.tree.visitor.BuildTreeVisitor;

/**
 * @author Christoph Kiefer
 * @version $Id: BuildTreeVisitorTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class BuildTreeVisitorTest extends DefaultTreeTestCase {

	private BottomUpMaximumSubtree cstree1;

	private Map<TreeNode, TreeNode> map;

	private BuildTreeVisitor visitor;

	protected void setUp() throws Exception {
		super.setUp();

		cstree1 = new BottomUpMaximumSubtree(new SimpleTreeAccessor(tree1),
				new SimpleTreeAccessor(tree2), null, true, true);
		assertTrue(cstree1.calculate());
		assertTrue(cstree1.isCalculated());
		map = cstree1.mapTrees(cstree1.getSubtreeRootNodesTree1().get(0),
				cstree1.getSubtreeRootNodesTree2().get(0));
		visitor = new BuildTreeVisitor(map, cstree1.getSubtreeRootNodesTree1()
				.get(0));
	}

	public void testTreeBuilder() {
		assertNotNull(cstree1);
		assertTrue(cstree1.isCalculated());

		assertNotNull(map);

		assertNotNull(visitor);

		ITreeNode mapped = visitor.getMappedTree();
		assertSame(mapped, t2n11);
		assertEquals(TreeUtil.calculateSize(mapped), 6);
	}
}
