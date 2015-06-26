/*
 * $Id: BottomUpMaximumSubtree.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;

import simpack.api.ITreeAccessor;
import simpack.api.ITreeNodeComparator;
import simpack.api.ITreeNode;
import simpack.api.impl.AbstractTreeSimilarityMeasure;
import simpack.exception.InvalidElementException;
import simpack.util.tree.EquivalenceClassCalculator;
import simpack.util.tree.NodePriority;
import simpack.util.tree.TreeNode;
import simpack.util.tree.TreeNodePriorityTuple;
import simpack.util.tree.TreeUtil;
import simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator;
import simpack.util.tree.comparator.NodeComparator;
import simpack.util.tree.visitor.BuildTreeVisitor;

/**
 * This implements an subtree calculation for trees. The trees are rooted. The
 * algorithm is taken from Gabriel Valientes book "Algorithms on trees and
 * graphs" (Springer) and described in chapter 4.3.4 "Bottom-up unordered
 * maximum common subtree isomorphism".
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class BottomUpMaximumSubtree extends AbstractTreeSimilarityMeasure {

	/**
	 * Contains the first tree
	 */
	private ITreeNode tree1;

	/**
	 * The first trees preordered list of nodes
	 */
	private List<ITreeNode> list1 = null;

	/**
	 * Queue sorting the nodes on NodePriority
	 */
	private PriorityQueue<TreeNodePriorityTuple> queue1 = new PriorityQueue<TreeNodePriorityTuple>(
			3, (Comparator<TreeNodePriorityTuple>) new NodeComparator());

	/**
	 * Contains the size of each node
	 */
	private LinkedHashMap<ITreeNode, Integer> size1 = new LinkedHashMap<ITreeNode, Integer>();

	/**
	 * Contains all the nodes of tree1 which are roots of a subtree
	 */
	private ArrayList<ITreeNode> subtreeRootNodesTree1 = new ArrayList<ITreeNode>();

	/**
	 * Contains the second tree
	 */
	private ITreeNode tree2;

	/**
	 * The second trees preordered list of nodes
	 */
	private List<ITreeNode> list2 = null;

	/**
	 * Defines if given trees are treated as ordered
	 */
	private boolean ordered;

	/**
	 * Defines if given trees are treated as labeled
	 */
	private boolean labeled;

	/**
	 * Queue sorting the nodes on NodePriority
	 */
	private PriorityQueue<TreeNodePriorityTuple> queue2 = new PriorityQueue<TreeNodePriorityTuple>(
			3, (Comparator<TreeNodePriorityTuple>) new NodeComparator());

	/**
	 * Contains the size of each node
	 */
	private LinkedHashMap<ITreeNode, Integer> size2 = new LinkedHashMap<ITreeNode, Integer>();

	/**
	 * Contains all the nodes of tree2 which are roots of a subtree
	 */
	private ArrayList<ITreeNode> subtreeRootNodesTree2 = new ArrayList<ITreeNode>();

	/**
	 * Contains the equivalence class codes for both trees
	 */
	private EquivalenceClassCalculator equivalenceClass = null;

	/**
	 * Constructor.
	 * <p>
	 * Construct new subtree calculator. Trees are unordered and unlabeled. Use
	 * the given <code>ITreeAccessor<code> to get the tree information.
	 * 
	 * <p>Use an AlwaysTrueTreeComparaotr as comparator.</p>
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @throws NullPointerException
	 */
	public BottomUpMaximumSubtree(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2) throws NullPointerException,
			InvalidElementException {
		this(treeAccessor1, treeAccessor2, new AlwaysTrueTreeNodeComparator());
	}

	/**
	 * Constructor.
	 * <p>
	 * Construct new subtree calculator. Trees are unordered and unlabeled. Use
	 * the given <code>ITreeAccessor<code> to get the tree information.
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator use the given comparator for comparison
	 * @throws NullPointerException
	 */
	public BottomUpMaximumSubtree(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator)
			throws NullPointerException, InvalidElementException {
		this(treeAccessor1, treeAccessor2, comparator, false);
	}

	/**
	 * Constructor.
	 * <p>
	 * Construct new subtree calculator. Trees are unlabeled. Use the given
	 * <code>ITreeAccessor<code> to get the tree information.
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator use the given comparator for comparison
	 * @param ordered treat trees as ordered if true
	 * @throws NullPointerException
	 * @throws InvalidElementException
	 */
	public BottomUpMaximumSubtree(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator, boolean ordered)
			throws NullPointerException, InvalidElementException {
		this(treeAccessor1, treeAccessor2, comparator, ordered, false);
	}

	/**
	 * Constructor.
	 * <p>
	 * Construct new subtree calculator. Use the given
	 * <code>ITreeAccessor<code> to get the tree information.
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator use the given comparator for comparison
	 * @param ordered treat trees as ordered if true
	 * @param labeled treat trees as labeled if true
	 * @throws NullPointerException
	 */
	public BottomUpMaximumSubtree(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator, boolean ordered,
			boolean labeled) throws NullPointerException,
			InvalidElementException {

		super(comparator);
		// verify data
		if (treeAccessor1 == null || treeAccessor2 == null
				|| treeAccessor1.getRoot() == null
				|| treeAccessor2.getRoot() == null)
			throw new NullPointerException("Invalid accessors passed!");

		this.ordered = ordered;
		this.labeled = labeled;

		this.tree1 = treeAccessor1.getRoot();
		this.tree2 = treeAccessor2.getRoot();

		list1 = TreeUtil.enumerationToList(tree1.postorderEnumeration());
		list2 = TreeUtil.enumerationToList(tree2.postorderEnumeration());

		if (list1 == null || list2 == null)
			throw new NullPointerException(
					"Postorder lists were not initialized.");
	}

	public boolean calculate() {
		setCalculated(false);

		// calculate equivalence classes
		try {
			equivalenceClass = new EquivalenceClassCalculator(tree1, list1,
					tree2, list2, this.ordered, this.labeled);
			if (equivalenceClass.calculate() && equivalenceClass.isCalculated()) {
				queue1 = calculateQueue(list1, equivalenceClass
						.getEquivalenceClassesTree1(), size1);
				queue2 = calculateQueue(list2, equivalenceClass
						.getEquivalenceClassesTree2(), size2);
			} else {
				return false;
			}
		} catch (Exception e1) {
			return false;
		}

		ITreeNode v = findMaxCommonSubtree();
		if (v == null) {
			// return empty tree
			return false;
		}
		setCalculated(true);

		// prepare preorder list of subtree
		List<ITreeNode> subtreeList = null;
		try {
			subtreeList = TreeUtil.enumerationToList(v.preorderEnumeration());
		} catch (InvalidElementException e) {
			return false;
		}
		if (subtreeList == null) {
			return false;
		}

		Map<TreeNode, TreeNode> mapped = null;
		try {
			mapped = mapTrees(getSubtreeRootNodesTree1().get(0),
					getSubtreeRootNodesTree2().get(0));
		} catch (NullPointerException e) {
			return false;
		} catch (InvalidElementException e) {
			return false;
		}
		if (mapped == null)
			return false;

		BuildTreeVisitor visitor = new BuildTreeVisitor(mapped,
				getSubtreeRootNodesTree1().get(0));
		ITreeNode resultTree = visitor.getTree();

		similarity = TreeUtil.getSimilarity1to1(getTree1(), getTree2(),
				resultTree);
		return true;
	}

	/**
	 * Finds the maximum common subtree
	 * 
	 * @return null if no subtree was found, the root node of tree1 subtree
	 *         otherwise
	 */
	private ITreeNode findMaxCommonSubtree() {
		ArrayList<TreeNodePriorityTuple> queue1original = copyQueue(queue1);
		ArrayList<TreeNodePriorityTuple> queue2original = copyQueue(queue2);
		// find largest common subtree
		TreeNodePriorityTuple v = null;
		TreeNodePriorityTuple w = null;
		while (!queue1.isEmpty() && !queue2.isEmpty()) {
			v = queue1.peek();
			w = queue2.peek();
			if (v.getPriority().getCode().equals(w.getPriority().getCode())) {
				subtreeRootNodesTree1.add((ITreeNode) v.getNode());
				subtreeRootNodesTree2.add((ITreeNode) w.getNode());
				subtreeRootNodesTree1.addAll(findSame(v, queue1original));
				subtreeRootNodesTree2.addAll(findSame(w, queue2original));
				break;
			}
			if (NodePriority.compare(v.getPriority(), w.getPriority()) == -1) {
				queue1.remove(v);
			} else {
				queue2.remove(w);
			}
		}
		if (v == null || w == null) {
			// returned tree will be empty
			return null;
		}

		return (ITreeNode) v.getNode();
	}

	/**
	 * Find in a queue set of TreeNodePriorityTuple the matching
	 * 
	 * @param tuple
	 *            the tuple to look for
	 * @param queue
	 *            search in this queue
	 * @return an ArrayList of matching nodes, might be empty
	 */
	private ArrayList<ITreeNode> findSame(TreeNodePriorityTuple tuple,
			ArrayList<TreeNodePriorityTuple> queue) {
		ArrayList<ITreeNode> ret = new ArrayList<ITreeNode>();
		Iterator<TreeNodePriorityTuple> iter = queue.iterator();
		while (iter.hasNext()) {
			TreeNodePriorityTuple a = iter.next();
			if ((!a.equals(tuple))
					&& (a.getPriority().getCode().equals(tuple.getPriority()
							.getCode())))
				ret.add((ITreeNode) a.getNode());
		}
		return ret;
	}

	/**
	 * Copies a PriorityQueue to an ArrayList
	 * 
	 * @param queue
	 * @return the copied queue
	 */
	private ArrayList<TreeNodePriorityTuple> copyQueue(
			PriorityQueue<TreeNodePriorityTuple> queue) {
		ArrayList<TreeNodePriorityTuple> ret = new ArrayList<TreeNodePriorityTuple>();
		Iterator<TreeNodePriorityTuple> iter = queue.iterator();
		while (iter.hasNext()) {
			TreeNodePriorityTuple a = iter.next();
			ret.add(a);
		}
		return ret;
	}

	/**
	 * Fills queue for given list.
	 * 
	 * @param list
	 * @param code
	 * @param size
	 */
	private PriorityQueue<TreeNodePriorityTuple> calculateQueue(
			List<ITreeNode> list, LinkedHashMap<ITreeNode, Integer> code,
			LinkedHashMap<ITreeNode, Integer> size)
			throws InvalidElementException {
		PriorityQueue<TreeNodePriorityTuple> queue = new PriorityQueue<TreeNodePriorityTuple>(
				3, (Comparator<TreeNodePriorityTuple>) new NodeComparator());
		// fill queue for tree
		ListIterator<ITreeNode> liter = list.listIterator();
		while (liter.hasNext()) {
			ITreeNode node = liter.next();
			size.put(node, new Integer(1));
			if (!node.isLeaf()) {
				// loop over children and add those sizes to this node's size
				for (Enumeration enumer = node.children(); enumer
						.hasMoreElements();) {
					Object o = enumer.nextElement();
					if (o instanceof ITreeNode) {
						size
								.put(node, size.get(node)
										+ size.get((ITreeNode) o));
					} else
						throw new InvalidElementException(
								"Unexpected child type in Tree while calculating child size.");
				}
			}
			TreeNodePriorityTuple tp = new TreeNodePriorityTuple(node,
					new NodePriority(size.get(node), code.get(node)));
			queue.add(tp);
		}
		return queue;
	}

	/**
	 * Get mapping of two trees. Ordered / unordered sensitive
	 * 
	 * @param root1
	 *            mapping from tree root; must be a node in tree1
	 * @param root2
	 *            mapping to tree root; must be a node in tree2
	 * @return the mapping of tree with root1 to tree with root2, null if an
	 *         error ocurred
	 * @throws InvalidElementException
	 */
	public HashMap<TreeNode, TreeNode> mapTrees(ITreeNode root1, ITreeNode root2)
			throws InvalidElementException {
		if (ordered)
			return mapOrderedTrees(root1, root2);
		else
			return mapUnorderedTrees(root1, root2);
	}

	/**
	 * Maps all elements of given unordered subtrees
	 * 
	 * @param root1
	 *            mapping from tree root; must be a node in tree1
	 * @param root2
	 *            mapping to tree root; must be a node in tree2
	 * @return the mapping of tree with root1 to tree with root2, null if an
	 *         error ocurred
	 */
	private HashMap<TreeNode, TreeNode> mapUnorderedTrees(ITreeNode root1,
			ITreeNode root2) throws InvalidElementException {
		HashMap<TreeNode, TreeNode> mappedSubtree = new HashMap<TreeNode, TreeNode>();

		if (root1 == null || root2 == null)
			return null;

		// check if root1 is from tree1 and root2 from tree2
		if (!root1.getRoot().equals(tree1.getRoot())
				|| !root2.getRoot().equals(tree2.getRoot()))
			return null;

		// check if trees equal: root1.equivClass=root2.equivClass
		if (!equivalenceClass.getEquivalenceClassesTree1().get(root1).equals(
				equivalenceClass.getEquivalenceClassesTree2().get(root2)))
			return null;

		// prepare preorder list of subtree
		List<ITreeNode> subtreeList = null;
		subtreeList = TreeUtil.enumerationToList(root1.preorderEnumeration());
		if (subtreeList == null)
			throw new InvalidElementException();

		// map trees
		mappedSubtree.put((TreeNode) root1, (TreeNode) root2);
		HashMap<ITreeNode, Boolean> mapped = new HashMap<ITreeNode, Boolean>();
		ListIterator<ITreeNode> subtreeIterator = subtreeList.listIterator(1);
		while (subtreeIterator.hasNext()) {
			ITreeNode nodeV = subtreeIterator.next();
			for (Enumeration e = mappedSubtree.get(nodeV.getParent())
					.children(); e.hasMoreElements();) {
				Object o = e.nextElement();
				if (o instanceof ITreeNode) {
					ITreeNode nodeW = (ITreeNode) o;
					if (equivalenceClass.getEquivalenceClassesTree1()
							.get(nodeV).equals(
									equivalenceClass
											.getEquivalenceClassesTree2().get(
													nodeW))
							&& (!mapped.containsKey(nodeW) || !mapped
									.get(nodeW))) {
						mappedSubtree.put((TreeNode) nodeV, (TreeNode) nodeW);
						mapped.put(nodeW, Boolean.valueOf(true));
						break;
					}
				} else
					throw new InvalidElementException();
			}
		}

		return mappedSubtree;
	}

	/**
	 * Contains the mapping of the ordered trees
	 */
	private HashMap<TreeNode, TreeNode> orderedMapping;

	/**
	 * Maps all elements of given ordered subtrees
	 * 
	 * @param root1
	 *            mapping from tree root; must be a node in tree1
	 * @param root2
	 *            mapping to tree root; must be a node in tree2
	 * @return the mapping of tree with root1 to tree with root2, null if an
	 *         error ocurred
	 */
	private HashMap<TreeNode, TreeNode> mapOrderedTrees(ITreeNode root1,
			ITreeNode root2) {
		orderedMapping = new HashMap<TreeNode, TreeNode>();
		if (mapOrderedSubtrees(root1, root2)) {
			return orderedMapping;
		}
		return null;
	}

	private boolean mapOrderedSubtrees(ITreeNode root1, ITreeNode root2) {
		if (root1 == null || root2 == null)
			return false;

		// check if root1 is from tree1 and root2 from tree2
		if (!root1.getRoot().equals(tree1.getRoot())
				|| !root2.getRoot().equals(tree2.getRoot()))
			return false;

		// check if trees equal: root1.equivClass=root2.equivClass
		if (!equivalenceClass.getEquivalenceClassesTree1().get(root1).equals(
				equivalenceClass.getEquivalenceClassesTree2().get(root2)))
			return false;

		if (comparator.compare(root1, root2) != 0)
			return false;
		orderedMapping.put((TreeNode) root1, (TreeNode) root2);
		if (root1.getChildCount() > root2.getChildCount()) {
			return false;
		}
		if (!root1.isLeaf()) {
			ITreeNode v1 = root1.getFirstChild();
			ITreeNode v2 = root2.getFirstChild();

			if (!mapOrderedSubtrees((ITreeNode) v1, (ITreeNode) v2)) {
				return false;
			}
			for (int i = 2; i <= root1.getChildCount(); i++) {
				v1 = root1.getChildAfter(v1);
				v2 = root2.getChildAfter(v2);
				if (!mapOrderedSubtrees((ITreeNode) v1, (ITreeNode) v2)) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * @return Returns the equivalenceClass list of tree1
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public LinkedHashMap<ITreeNode, Integer> getEquivalenceClassTree1()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return equivalenceClass.getEquivalenceClassesTree1();
	}

	/**
	 * @return Returns the equivalenceClass list of tree2
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public LinkedHashMap<ITreeNode, Integer> getEquivalenceClassTree2()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return equivalenceClass.getEquivalenceClassesTree2();
	}

	/**
	 * @return Returns the size list of tree1
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public LinkedHashMap<ITreeNode, Integer> getSizeTree1()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return size1;
	}

	/**
	 * @return Returns the size list of tree2
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public LinkedHashMap<ITreeNode, Integer> getSizeTree2()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return size2;
	}

	/**
	 * @return Returns the subtreeRootNodesTree1.
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public ArrayList<ITreeNode> getSubtreeRootNodesTree1()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return subtreeRootNodesTree1;
	}

	/**
	 * @return Returns the subtreeRootNodesTree2.
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public ArrayList<ITreeNode> getSubtreeRootNodesTree2()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return subtreeRootNodesTree2;
	}

	/**
	 * @return Returns the tree1.
	 */
	public ITreeNode getTree1() {
		return tree1;
	}

	/**
	 * @return Returns the tree2.
	 */
	public ITreeNode getTree2() {
		return tree2;
	}
}
