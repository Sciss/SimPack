/*
 * $Id: TopDownOrderedMaximumSubtree.java 757 2008-04-17 17:42:53Z kiefer $
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;

import simpack.api.ITreeAccessor;
import simpack.api.ITreeNodeComparator;
import simpack.api.ITreeNode;
import simpack.api.impl.AbstractTreeSimilarityMeasure;
import simpack.exception.InvalidElementException;
import simpack.util.tree.TreeUtil;
import simpack.util.tree.comparator.TypedTreeNodeComparator;

/**
 * This implements an subtree calculation for trees. The trees are rooted and
 * ordered. The algorithm is taken from Gabriel Valientes book "Algorithms on
 * trees and graphs" (Springer) and described in chapter 4.3.1 "Top-down Maximum
 * Common Subtree Isomorphism"
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class TopDownOrderedMaximumSubtree extends AbstractTreeSimilarityMeasure {

	/**
	 * Contains the first tree
	 */
	private ITreeNode tree1;

	/**
	 * Contains the second tree
	 */
	private ITreeNode tree2;

	/**
	 * This will contain the mapped trees
	 */
	private HashMap<ITreeNode, ITreeNode> mappedTrees = new HashMap<ITreeNode, ITreeNode>();

	/**
	 * This will contain the matched subtree from tree1
	 */
	private ITreeNode matchedTree1;

	/**
	 * This will contain the matched subtree from tree2
	 */
	private ITreeNode matchedTree2;

	/**
	 * Temporary field for creating the mapping.
	 */
	private ITreeNode currentParent1;

	/**
	 * Temporary field for creating the mapping.
	 */
	private ITreeNode currentParent2;

	/**
	 * Constructor.
	 * <p>
	 * Construct new subtree calculator. TypedTreeNodeComparator is used as
	 * comparator. Use the given
	 * <code>ITreeAccessor<code> to get the tree information.
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @throws NullPointerException
	 */
	public TopDownOrderedMaximumSubtree(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2) throws NullPointerException,
			InvalidElementException {
		this(treeAccessor1, treeAccessor2, new TypedTreeNodeComparator());
	}

	/**
	 * Constructor.
	 * <p>
	 * Construct new subtree calculator. Specifiy a personal comparator. Use the
	 * given <code>ITreeAccessor<code> to get the tree information.
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator
	 * @throws NullPointerException
	 * @throws InvalidElementException
	 */
	public TopDownOrderedMaximumSubtree(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator)
			throws NullPointerException, InvalidElementException {
		super(comparator);

		// verify data
		if (treeAccessor1 == null || treeAccessor2 == null
				|| treeAccessor1.getRoot() == null
				|| treeAccessor2.getRoot() == null)
			throw new NullPointerException("Invalid accessors passed!");

		this.tree1 = treeAccessor1.getRoot();
		this.tree2 = treeAccessor2.getRoot();
	}

	public boolean calculate() {
		setCalculated(false);
		if (mapOrderedCommonSubtree((ITreeNode) tree1.getRoot(),
				(ITreeNode) tree2.getRoot())) {
			setCalculated(true);
			similarity = TreeUtil.getSimilarity1to1(getTree1(), getTree2(),
					getMatchedTree2());
			return true;
		}
		return false;
	}

	/**
	 * This method corresponds to "map_ordered_common_subtree(T1, r1, T2, r2,
	 * M)" from http://www.lsi.upc.es/~valiente/algorithm/combin.cpp
	 * 
	 * @param node1
	 * @param node2
	 * @return false if root nodes of tree
	 */
	private boolean mapOrderedCommonSubtree(ITreeNode node1, ITreeNode node2) {
		if (comparator.compare(node1, node2) != 0) {
			return false;
		} else {
			mappedTrees.put(node1, node2);
			ITreeNode clone1 = (ITreeNode) node1.clone();
			ITreeNode clone2 = (ITreeNode) node2.clone();
			if (!node1.isRoot()) {
				currentParent1.add(clone1);
				currentParent1 = clone1;
			} else {
				currentParent1 = clone1;
				matchedTree1 = currentParent1;
			}
			if (!node2.isRoot()) {
				currentParent2.add(clone2);
				currentParent2 = clone2;
			} else {
				currentParent2 = clone2;
				matchedTree2 = currentParent2;
			}

			if (!node1.isLeaf() && !node2.isLeaf()) {
				Enumeration childs1 = node1.children();
				Enumeration childs2 = node2.children();
				ArrayList<ITreeNode> list1 = new ArrayList<ITreeNode>();
				while (childs1.hasMoreElements())
					list1.add((ITreeNode) childs1.nextElement());
				ArrayList<ITreeNode> list2 = new ArrayList<ITreeNode>();
				while (childs2.hasMoreElements())
					list2.add((ITreeNode) childs2.nextElement());
				ListIterator<ITreeNode> iter1 = list1.listIterator();
				ListIterator<ITreeNode> iter2 = list2.listIterator();
				boolean res = true;
				while (res && iter1.hasNext() && iter2.hasNext()) {
					ITreeNode v = iter1.next();
					ITreeNode w = iter2.next();
					res = mapOrderedCommonSubtree(v, w);
					currentParent1 = clone1;
					currentParent2 = clone2;
				}
			}
			return true;
		}
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

	/**
	 * @return Returns the mappedTrees. Null if calculation was not successful.
	 */
	public HashMap<ITreeNode, ITreeNode> getMappedTrees() {
		if (isCalculated())
			return mappedTrees;
		return null;
	}

	/**
	 * @return Returns the matchedTree1. Null if calculation was not successful.
	 */
	public ITreeNode getMatchedTree1() {
		if (isCalculated())
			return matchedTree1;
		return null;
	}

	/**
	 * @return Returns the matchedTree2. Null if calculation was not successful.
	 */
	public ITreeNode getMatchedTree2() {
		if (isCalculated())
			return matchedTree2;
		return null;
	}
}
