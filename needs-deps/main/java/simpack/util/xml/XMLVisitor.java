/*
 * $Id: XMLVisitor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.xml;

import java.util.Stack;

import simpack.api.ITreeNode;
import simpack.util.tree.TreeNode;
import ch.toe.famix.Visitor;

/**
 * This class is used to build up a tree when traversing another tree in
 * preorder. By implementing the interface {@link Visitor} it provides the
 * methods {@link #visit(Object)} and {@link #endVisit(Object)}.<br>
 * The class which is doing the traversal of the other tree, must use this
 * Visitor by calling the methods defined here, each time a node is visited or
 * left.
 * <p>
 * In this class it works like that: when visit is called, we add the parameter
 * Object as a child to the current context-node (that means the node where we
 * reside at the moment), and set this child as the new context-node, thus, we
 * go one level deeper. when endVisit is called, we just go one level up, to the
 * parent of the context-node.
 * 
 * 
 * @author Silvan Hollenstein, Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class XMLVisitor implements Visitor {

	private ITreeNode root;

	private Stack<ITreeNode> stack = new Stack<ITreeNode>();

	public XMLVisitor() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.toe.famix.Visitor#visit(java.lang.Object)
	 */
	public void visit(Object o) {
		if (stack.empty()) {
			root = new TreeNode(o);
			stack.push(root);
		} else {
			TreeNode newChild = new TreeNode(o);
			ITreeNode parent = stack.peek();
			parent.add(newChild);
			stack.push(newChild);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.toe.famix.Visitor#endVisit(java.lang.Object)
	 */
	public void endVisit(Object o) {
		stack.pop();
	}

	/**
	 * @return the root of the tree (from there it can be navigated to all child
	 *         elements)
	 */
	public ITreeNode getTree() {
		return root;
	}

}
