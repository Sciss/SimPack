/*
 * $Id: AbstractTreeAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;

import simpack.api.ITreeAccessor;
import simpack.api.ITreeNode;
import simpack.api.ITreeSequenceAccessor;
import simpack.exception.InvalidElementException;
import simpack.util.tree.TreeNode;
import simpack.util.tree.comparator.NamedTreeNodeComparator;

/**
 * This is the abstract base class for all tree accessors that allows a specific
 * tree accessor to implement basic tree operations.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractTreeAccessor implements ITreeAccessor,
		ITreeSequenceAccessor {

	/**
	 * The tree node this tree accessor provides access to.
	 */
	protected ITreeNode tree;

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#getRoot()
	 */
	public ITreeNode getRoot() {
		return tree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeSequenceAccessor#getPreorderSequence()
	 */
	public List<Object> getPreorderSequence() {
		ArrayList<Object> preorderSequenceOfTree = new ArrayList<Object>();
		preorder(getRoot(), preorderSequenceOfTree);
		return preorderSequenceOfTree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeSequenceAccessor#getPostorderSequence()
	 */
	public List<Object> getPostorderSequence() {
		ArrayList<Object> postorderSequenceOfTree = new ArrayList<Object>();
		postorder(getRoot(), postorderSequenceOfTree);
		return postorderSequenceOfTree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ISequenceAccessor#getSequence()
	 */
	public List<Object> getSequence() {
		return getPreorderSequence();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#getMostRecentCommonAncestor(simpack.api.ITreeNode,
	 *      simpack.api.ITreeNode)
	 */
	public ITreeNode getMostRecentCommonAncestor(ITreeNode node1,
			ITreeNode node2) throws InvalidElementException {

		DefaultMutableTreeNode n1 = null, n2 = null;

		n1 = getNode(node1);
		n2 = getNode(node2);

		if (n1 == null) {
			throw new InvalidElementException("Node " + node1.toString()
					+ " is invalid");
		} else if (n2 == null) {
			throw new InvalidElementException("Node " + node2.toString()
					+ " is invalid");
		}

		return (TreeNode) n1.getSharedAncestor(n2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#getDescendants(simpack.api.ITreeNode)
	 */
	public Set<ITreeNode> getDescendants(ITreeNode node)
			throws InvalidElementException {

		TreeSet<ITreeNode> descendants = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		DefaultMutableTreeNode n = getNode(node);

		if (n != null) {
			Enumeration en = n.preorderEnumeration();
			// skip first node (is the passed not itself)
			en.nextElement();
			while (en.hasMoreElements()) {
				descendants.add(new TreeNode(((DefaultMutableTreeNode) en
						.nextElement()).toString()));
			}
			return descendants;
		} else {
			throw new InvalidElementException("Node " + node.toString()
					+ " is invalid");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#getAncestors(simpack.api.ITreeNode)
	 */
	public Set<ITreeNode> getAncestors(ITreeNode node)
			throws InvalidElementException {

		TreeSet<ITreeNode> ancestors = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		DefaultMutableTreeNode n = getNode(node);

		if (n != null) {
			Enumeration en = n.pathFromAncestorEnumeration(getRoot());
			while (en.hasMoreElements()) {
				ancestors.add(new TreeNode(((DefaultMutableTreeNode) en
						.nextElement()).toString()));
			}
			// remove source node
			ancestors.remove(node);
			return ancestors;
		} else {
			throw new InvalidElementException("Node " + node.toString()
					+ " is invalid");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#getChildren(simpack.api.ITreeNode)
	 */
	public Set<ITreeNode> getChildren(ITreeNode node)
			throws InvalidElementException {
		TreeSet<ITreeNode> children = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		DefaultMutableTreeNode n = getNode(node);

		if (n != null) {
			Enumeration en = n.children();
			while (en.hasMoreElements()) {
				children.add(new TreeNode(((DefaultMutableTreeNode) en
						.nextElement()).toString()));
			}
			return children;
		} else {
			throw new InvalidElementException("Node " + node.toString()
					+ " is invalid");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#getParents(simpack.api.ITreeNode)
	 */
	public Set<ITreeNode> getParents(ITreeNode node)
			throws InvalidElementException {
		TreeSet<ITreeNode> parents = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		DefaultMutableTreeNode n = getNode(node);

		if (n != null) {
			parents.add(new TreeNode(n.getParent().toString()));
			return parents;
		} else {
			throw new InvalidElementException("Node " + node.toString()
					+ " is invalid");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#contains(simpack.api.ITreeNode)
	 */
	public boolean contains(ITreeNode node) {
		DefaultMutableTreeNode n = getNode(node);
		if (n == null) {
			return false;
		} else {
			return ((TreeNode) tree).isNodeDescendant(n);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeAccessor#size()
	 */
	public int size() {
		int size = 0;
		Enumeration en = ((TreeNode) tree).breadthFirstEnumeration();
		while (en.hasMoreElements()) {
			en.nextElement();
			size++;
		}
		return size;
	}

	/**
	 * This methods finds and return the tree node in the tree that is equal to
	 * the user-passed tree node <code>node</code>.
	 * 
	 * @param node
	 *            user-passed tree node to be found in the tree
	 * @return the node that is equal to <code>node</code>
	 */
	private final DefaultMutableTreeNode getNode(ITreeNode node) {
		Enumeration en = ((TreeNode) tree).preorderEnumeration();
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode o = (DefaultMutableTreeNode) en
					.nextElement();
			if (o.toString().equals(node.getUserObject().toString())) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Traverses node in preorder manner, i.e, adds node to
	 * preorderSequenceOfTree before processing the node's children in the same
	 * manner.
	 * 
	 * @param node
	 *            tree node to be processed in preorder manner
	 * @param preorderSequenceOfTree
	 *            data structure to store preorder tree traversal sequence
	 */
	private final void preorder(ITreeNode node,
			ArrayList<Object> preorderSequenceOfTree) {
		if (node != null) {
			preorderSequenceOfTree.add(node);
			Enumeration e = node.children();
			while (e.hasMoreElements()) {
				ITreeNode child = (ITreeNode) e.nextElement();
				preorder(child, preorderSequenceOfTree);
			}
		} else {
			return;
		}
	}

	/**
	 * Traverses node in postorder manner, i.e, processes the node's children
	 * before adding the node to postorderSequenceOfTree.
	 * 
	 * @param node
	 *            tree node to be processed in postorder manner
	 * @param postorderSequenceOfTree
	 *            data structure to store postorder tree traversal sequence
	 */
	private final void postorder(ITreeNode node,
			ArrayList<Object> postorderSequenceOfTree) {
		if (node != null) {
			Enumeration e = node.children();
			while (e.hasMoreElements()) {
				ITreeNode child = (ITreeNode) e.nextElement();
				postorder(child, postorderSequenceOfTree);
			}
			postorderSequenceOfTree.add(node);
		} else {
			return;
		}
	}
}