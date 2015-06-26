/*
 * $Id: BuildTreeVisitor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.tree.visitor;

import java.util.Map;
import java.util.Stack;

import simpack.api.ITreeNode;
import simpack.util.tree.TreeNode;

/**
 * This visitor receives a mapping of corresponding nodes between two trees. If
 * tree nodes are {@link simpack.api.ITreeNode ITreeNodes} and the mapped trees
 * are valid trees, the visitor will return the two mapped trees, generated from
 * the given {@link java.util.Map Map} and using the given root node.
 * 
 * @see simpack.measure.tree.BottomUpMaximumSubtree
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class BuildTreeVisitor extends TreeVisitor {

	private Map<TreeNode, TreeNode> mapped;

	private ITreeNode tree;

	private ITreeNode mappedTree;

	private Stack<ITreeNode> currentParent = new Stack<ITreeNode>();

	private Stack<ITreeNode> currentParentMapped = new Stack<ITreeNode>();

	/**
	 * Constructor.
	 * <p>
	 * Visitor to visit all tree nodes in the tree mapping <code>mapped</code>.
	 * 
	 * @param mapped
	 *            mapping of tree nodes of two trees
	 * @param root
	 */
	public BuildTreeVisitor(Map<TreeNode, TreeNode> mapped, ITreeNode root) {
		super(root);
		this.mapped = mapped;
		accept(root);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.util.tree.visitor.TreeVisitor#visit(simpack.api.ITreeNode)
	 */
	public boolean visit(ITreeNode node) {
		if (!mapped.containsKey(node))
			return false;

		ITreeNode clone = (ITreeNode) node.clone();
		// root node
		if (node.equals(root)) {
			tree = clone;
			mappedTree = mapped.get(node);

			currentParent.push(clone);
			currentParentMapped.push(mappedTree);
		}
		// non-root node, add to currentParent(Mapped)
		else {
			currentParent.peek().add(clone);
			currentParent.push(clone);
			currentParentMapped.peek().add(mapped.get(node));
			currentParentMapped.push(mapped.get(node));
		}

		// visit nodes children
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.util.tree.visitor.TreeVisitor#postVisit(simpack.api.ITreeNode)
	 */
	public void postVisit(ITreeNode node) {
		currentParent.pop();
		currentParentMapped.pop();
	}

	/**
	 * Returns the tree which is constructed after having visited the mapping of
	 * tree nodes of two trees.
	 * 
	 * @return constructed tree
	 */
	public ITreeNode getTree() {
		return tree;
	}

	/**
	 * Returns the mapped tree.
	 * 
	 * @return mapped tree
	 */
	public ITreeNode getMappedTree() {
		return mappedTree;
	}
}
