/*
 * $Id: IGraphNode.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.TreeSet;

/**
 * This interface defines the methods of a graph node.
 * 
 * @author Christoph Kiefer
 * @author Daniel Baggenstos
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public interface IGraphNode {

	/**
	 * Returns a textual description (label) of the node.
	 * 
	 * @return node label
	 */
	public String getLabel();

	/**
	 * Return the user-specified object of this graph node.
	 * 
	 * @return user-specified object
	 */
	public Object getUserObject();

	/**
	 * Explicitly set the user-specified object.
	 * 
	 * @param userObject
	 */
	public void setUserObject(Object userObject);

	/**
	 * Returns all the predecessors of the node.
	 * 
	 * @return the predecessors of the node
	 */
	public TreeSet<IGraphNode> getPredecessorSet();

	/**
	 * Returns all the successors of the node.
	 * 
	 * @return the successors of the node
	 */
	public TreeSet<IGraphNode> getSuccessorSet();

	/**
	 * Returns all the adjacent nodes (predecessors and successors) of the node.
	 * 
	 * @return set of adjacent graph nodes
	 */
	public TreeSet<IGraphNode> getAdjacentSet();

	/**
	 * Adds a predecessor to the node.
	 * 
	 * @param node
	 *            the predecessor
	 * 
	 */
	public void addPredecessor(IGraphNode node);

	/**
	 * Adds a successor to the node.
	 * 
	 * @param node
	 *            the successor
	 * 
	 */
	public void addSuccessor(IGraphNode node);

	/**
	 * Overrides method in java.lang.Object.
	 * 
	 * @return converts stored object into a string
	 */
	public String toString();

	/**
	 * Assigns the hasGroup flag to a node.
	 */
	public void setHasGroup();

	/**
	 * Checks whether the node has a group node assigned to it.
	 * 
	 * @return true, if node has a group node assigned to it, false, else
	 */
	public boolean getHasGroup();

	/**
	 * Returns all the single nodes of a group node (a not empty TreeSet in
	 * subclass group node).
	 * 
	 * @return all the single nodes contained in this group node
	 */
	public TreeSet<IGraphNode> getGroupNodes();

	/**
	 * Checks whether the node is a group node.
	 * 
	 * @return true, if node is a group node, false, else
	 */
	public boolean getIsGroup();

	/**
	 * Returns the number incoming edges (the number of predecessors) of the
	 * node.
	 * 
	 * @return the number incoming edges
	 */
	public Integer getInDegree();

	/**
	 * Returns the number outgoing edges (the number of successors) of the node.
	 * 
	 * @return the number outgoing edges
	 */
	public Integer getOutDegree();

	/**
	 * Returns the size of a group node.
	 * 
	 * @return the number of single nodes contained in a group node
	 */
	public int getGroupSize();
}