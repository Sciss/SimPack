/*
 * $Id: ITreeAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api;

import java.util.Set;

import simpack.exception.InvalidElementException;

/**
 * Accessor for tree structures. This interface defines methods for basic tree
 * operations.
 * 
 * @author Tobias Sager
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public interface ITreeAccessor extends IAccessor {

	/**
	 * Returns the root node of the tree.
	 * 
	 * @return root node of tree
	 */
	public ITreeNode getRoot();

	/**
	 * Returns the most recent common ancestor (least common subsumer) of two
	 * nodes of the tree. Throws an InvalidElementException if either
	 * <code>node1</code> or <code>node2</code> is invalid.
	 * 
	 * @param node1
	 *            first node
	 * @param node2
	 *            second node
	 * 
	 * @return the most recent common ancestor of <code>node1</code> and
	 *         <code>node2</code>
	 * 
	 * @throws InvalidElementException
	 */
	public ITreeNode getMostRecentCommonAncestor(ITreeNode node1,
			ITreeNode node2) throws InvalidElementException;

	/**
	 * Return the length of the longest directed path in the graph
	 * 
	 * @return length of longest directed path
	 */
	public double getMaximumDirectedPathLength();

	/**
	 * Returns the set of descendants of a given tree node. This includes its
	 * children as well as descendants deeper down in the tree hierarchy. The
	 * ordering of the descendants is in preorder. Throws an
	 * InvalidElementException if <code>node</code> is invalid, i.e., does not
	 * occur in the tree for instance.
	 * 
	 * @param node
	 *            tree node to find its descendants
	 * 
	 * @return set of descendants of the given tree node
	 * 
	 * @throws InvalidElementException
	 */
	public Set<ITreeNode> getDescendants(ITreeNode node)
			throws InvalidElementException;

	/**
	 * Returns the set of ancestors of a given tree node. This includes its
	 * parents as well as ancestors of <code>node</code> higher up in the tree
	 * hierarchy.
	 * 
	 * @param node
	 *            tree node to find its ancestors
	 * 
	 * @return set of ancestors of the given tree node
	 * 
	 * @throws InvalidElementException
	 */
	public Set<ITreeNode> getAncestors(ITreeNode node)
			throws InvalidElementException;

	/**
	 * Returns the set of children of a given tree node. Throws an
	 * InvalidElementException if <code>node</code> is invalid, i.e., does not
	 * occur in the tree for instance.
	 * 
	 * @param node
	 *            tree node to find its children
	 * 
	 * @return set of children of the given tree node
	 * 
	 * @throws InvalidElementException
	 */
	public Set<ITreeNode> getChildren(ITreeNode node)
			throws InvalidElementException;

	/**
	 * Returns the set of parents of a given tree node. Throws an
	 * InvalidElementException if <code>node</code> is invalid, i.e., does not
	 * occur in the tree for instance.
	 * 
	 * @param node
	 *            tree node to find its parents
	 * 
	 * @return set of parents of the given tree node
	 * 
	 * @throws InvalidElementException
	 */
	public Set<ITreeNode> getParents(ITreeNode node)
			throws InvalidElementException;

	/**
	 * Returns true if <code>node</code> is contained in the tree hierarchy
	 * this accessor provides access to.
	 * 
	 * @return true, if <code>node</code> is in tree hierarchy, false
	 *         otherwise
	 */
	public boolean contains(ITreeNode node);

	/**
	 * Returns the size of the tree, i.e., the total number of nodes in this
	 * tree.
	 * 
	 * @return size (== number of nodes) of the tree
	 */
	public int size();

	/**
	 * Return the length of the shortest path connecting <code>nodeA</code>
	 * and <code>nodeB</code>.
	 * 
	 * @param nodeA
	 *            the first node
	 * @param nodeB
	 *            the second node
	 * 
	 * @return length of the shortest path
	 */
	public double getShortestPath(ITreeNode nodeA, ITreeNode nodeB);
}