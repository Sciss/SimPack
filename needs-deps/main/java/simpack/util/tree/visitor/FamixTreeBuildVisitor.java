/*
 * $Id: FamixTreeBuildVisitor.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Stack;

import simpack.api.ITreeNode;
import simpack.util.tree.TreeNode;

import ch.toe.famix.FAMIXInstance;
import ch.toe.famix.Visitor;
import ch.toe.famix.model.Attribute;
import ch.toe.famix.model.BehaviouralEntity;
import ch.toe.famix.model.FormalParameter;
import ch.toe.famix.model.GlobalVariable;
import ch.toe.famix.model.ImplicitVariable;
import ch.toe.famix.model.Invocation;
import ch.toe.famix.model.LocalVariable;
import ch.toe.famix.model.Model;

/**
 * Visits a <code>FAMIXInstance</code> and builds a <code>ITreeNode</code>
 * representation of it. Only considers elements that have a true
 * {@link #isTreeRelevantElement(Object) isTreeRelevantElement(Object)}.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class FamixTreeBuildVisitor implements Visitor {

	/**
	 * Stack used while tree visiting
	 */
	private Stack<ITreeNode> stack = new Stack<ITreeNode>();

	/**
	 * The root object of the tree
	 */
	private ITreeNode root = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.toe.famix.Visitor#visit(java.lang.Object)
	 */
	public void visit(Object o) {
		if (isTreeRelevantElement(o)) {
			preVisit(o);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.toe.famix.Visitor#endVisit(java.lang.Object)
	 */
	public void endVisit(Object o) {
		if (isTreeRelevantElement(o)) {
			postVisit();
		}
	}

	/**
	 * Returns the root of this FAMIX tree.
	 * 
	 * @return root of the tre.
	 */
	public ITreeNode getRoot() {
		return root;
	}

	/**
	 * Checks if the given object <code>o</code> is a wanted object in the
	 * tree graph
	 * 
	 * @param o
	 * @return true if passed object is to be visit, false otherwise
	 */
	private boolean isTreeRelevantElement(Object o) {
		if (o instanceof FAMIXInstance || o instanceof Model
				|| o instanceof ch.toe.famix.model.Package
				|| o instanceof ch.toe.famix.model.Class
				|| o instanceof GlobalVariable || o instanceof Attribute
				|| o instanceof BehaviouralEntity
				|| o instanceof FormalParameter
				|| o instanceof ImplicitVariable || o instanceof LocalVariable
				|| o instanceof Invocation) {
			return true;
		}
		return false;
	}

	/**
	 * Pre-visit the node object.
	 * 
	 * @param o
	 *            tree object to be pre-visited
	 */
	private void preVisit(Object o) {
		ITreeNode node = new TreeNode(o);
		if (root == null) {
			root = node;
		}
		stack.push(node);
	}

	/**
	 * Post-visit nodes.
	 */
	private void postVisit() {
		// child is the current visited object
		ITreeNode child = stack.pop();
		if (!stack.isEmpty()) {
			ITreeNode node = stack.pop();
			node.add(child);
			stack.push(node);
		} else {
			stack.push(child);
		}
	}
}
