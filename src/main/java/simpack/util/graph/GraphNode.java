/*
 * $Id: GraphNode.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.graph;

import java.io.Serializable;
import java.util.TreeSet;

import simpack.api.IGraphNode;
import simpack.util.graph.comparator.NamedGraphNodeComparator;

/**
 * @author Christoph Kiefer
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class GraphNode implements IGraphNode, Serializable,
		Comparable<GraphNode> {

	private static final long serialVersionUID = -7149340901405017775L;

	/**
	 * This will contain the object stored in a node
	 */
	protected Object userObject;

	/**
	 * This will contain all the predecessors of the node
	 */
	protected TreeSet<IGraphNode> predecessorSet = new TreeSet<IGraphNode>(
			new NamedGraphNodeComparator());

	/**
	 * This will contain all the successors of the node
	 */
	protected TreeSet<IGraphNode> successorSet = new TreeSet<IGraphNode>(
			new NamedGraphNodeComparator());

	/**
	 * This will contain all the adjacent nodes (predecessors and successors) of
	 * the node
	 */
	protected TreeSet<IGraphNode> adjacentSet = new TreeSet<IGraphNode>(
			new NamedGraphNodeComparator());

	/**
	 * Determines whether it is a group node.
	 */
	protected boolean isGroup = false;

	/**
	 * The number of incoming edges.
	 */
	protected int inDegree = 0;

	/**
	 * The number of outgoing edges.
	 */
	protected int outDegree = 0;

	/**
	 * Determines whether a node has a group of nodes assigned to it
	 */
	private boolean hasGroup = false;

	/**
	 * Constructor.
	 * <p>
	 */
	public GraphNode() {

	}

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given userObject.
	 * 
	 * @param userObject
	 *            the wrapped object of the graph node
	 */
	public GraphNode(Object userObject) {
		this.userObject = userObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#setHasGroup()
	 */
	public void setHasGroup() {
		hasGroup = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getHasGroup()
	 */
	public boolean getHasGroup() {
		return hasGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getIsGroup()
	 */
	public boolean getIsGroup() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getGroupNodes()
	 */
	public TreeSet<IGraphNode> getGroupNodes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getUserObject()
	 */
	public Object getUserObject() {
		return userObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#setUserObject(java.lang.Object)
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getPredecessorSet()
	 */
	public TreeSet<IGraphNode> getPredecessorSet() {
		return predecessorSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getSuccessorSet()
	 */
	public TreeSet<IGraphNode> getSuccessorSet() {
		return successorSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getAdjacentSet()
	 */
	public TreeSet<IGraphNode> getAdjacentSet() {
		return adjacentSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#addPredecessor(simpack.api.IGraphNode)
	 */
	public void addPredecessor(IGraphNode node) {
		if (predecessorSet.add(node)) {
			adjacentSet.add(node);
			inDegree++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#addSuccessor(simpack.api.IGraphNode)
	 */
	public void addSuccessor(IGraphNode node) {
		if (successorSet.add(node)) {
            adjacentSet.add(node);
            outDegree++;
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getInDegree()
	 */
	public Integer getInDegree() {
		return inDegree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getOutDegree()
	 */
	public Integer getOutDegree() {
		return outDegree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getLabel()
	 */
	public String getLabel() {
		return toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getGroupSize()
	 */
	public int getGroupSize() {
		return 0;
	}

	/**
	 * Overrides method in java.lang.Object.
	 * 
	 * @return converts stored object into a string
	 */
	public String toString() {
		return userObject.toString();
	}

	/**
	 * Returns true if a certain node equals this node.
	 * This uses the <code>equals()</code>-method of the wrapped user objects.
	 *
	 * @param node
	 *            graph node
	 * 
	 * @return true if <code>node</code> and this node are equal
	 */
	public boolean equals(Object node) {
		GraphNode n = (GraphNode) node;
		return n.userObject.equals(this.userObject);
	}

	public int compareTo(GraphNode o) {
		return this.getUserObject().toString().compareTo(
				o.getUserObject().toString());
	}
}