/*
 * $Id: IGraphAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
import java.util.TreeSet;

import simpack.exception.InvalidElementException;

/**
 * Accessor for graph structures. This defines a method set for basic graph
 * operations.
 * 
 * @author Christoph Kiefer
 * @author Daniel Baggenstos
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public interface IGraphAccessor extends IAccessor {

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
	 * 
	 * @throws InvalidElementException
	 */
	public double getShortestPath(IGraphNode nodeA, IGraphNode nodeB)
			throws InvalidElementException;

	/**
	 * Returns the length of the longest directed path in the graph.
	 * 
	 * @return length of longest directed path
	 */
	public double getMaximumDirectedPathLength();

	/**
	 * Checks for the existence of <code>node</code> in the graph.
	 * 
	 * @param node
	 *            node to be checked for existence in this graph
	 * @return true, if <code>node</code> occurs in the graph, false otherwise
	 */
	public boolean contains(IGraphNode node);

	/**
	 * Returns the size of the graph structure, i.e., the number of nodes in the
	 * graph this accessor provides access to.
	 * 
	 * @return number of nodes in the graph
	 */
	public int size();

	/**
	 * Realizes the storage and assignments of nodes in the accessor.
	 * 
	 * @param source
	 *            source node of an edge
	 * 
	 * @param target
	 *            target node of an edge
	 */
	public void setEdge(IGraphNode source, IGraphNode target);

	/**
	 * Returns all nodes of the graph from which this accessor provides access
	 * to.
	 * 
	 * @return all nodes in the graph
	 */
	public TreeSet<IGraphNode> getNodeSet();

	/**
	 * Returns the wanted node if available.
	 * 
	 * @param label
	 *            the label of the wanted node
	 * 
	 * @return the graph node with the given label, if contained in the graph,
	 *         null otherwise
	 */
	public IGraphNode getNode(String label);

	/**
	 * Adds a new node to the graph.
	 * 
	 * @param node
	 *            graph node
	 */
	public void addNode(IGraphNode node);

	/**
	 * Returns the first node found in the graph having no predecessors (root)
	 * but only successors.
	 * 
	 * @return graph node having only outgoing edges
	 */
	public IGraphNode getRoot();

	/**
	 * Returns the set of graph nodes that have an outgoing (directed) edge to
	 * <code>node</code>. In a tree, the predecessors of a node are its
	 * parents.
	 * 
	 * @param node
	 *            graph node
	 * 
	 * @param direct
	 *            If true, only answer the directly adjacent nodes in the
	 *            predecessor relation: i.e. only the nodes that have an
	 *            outgoing edge going into <code>node</code>
	 * 
	 * @return set of nodes
	 * 
	 * @throws InvalidElementException
	 */
	public Set<IGraphNode> getPredecessors(IGraphNode node, boolean direct)
			throws InvalidElementException;

	/**
	 * Returns the set of graph nodes that have an incoming (directed) edge from
	 * <code>node</code>. In a tree, the successors of a node are its
	 * children.
	 * 
	 * @param node
	 *            graph node
	 * 
	 * @param direct
	 *            If true, only answer the directly adjacent nodes in the
	 *            successor relation: i.e. only the nodes that have an incoming
	 *            edge from <code>node</code>
	 * 
	 * @return set of graph nodes
	 * 
	 * @throws InvalidElementException
	 */
	public Set<IGraphNode> getSuccessors(IGraphNode node, boolean direct)
			throws InvalidElementException;

	/**
	 * Returns the node in the graph which (1) connects <code>nodeA</code> and
	 * <code>nodeB</code> (i.e., from which there exists a directed path to
	 * <code>nodeA</code> and <code>nodeB</code>) and (2) whose sum of path
	 * lengths to <code>nodeA</code> and <code>nodeB</code> is minimal.
	 * 
	 * @param nodeA
	 *            graph node
	 * 
	 * @param nodeB
	 *            another graph node
	 * 
	 * @return set of graph nodes
	 * 
	 * @throws InvalidElementException
	 */
	public IGraphNode getMostRecentCommonAncestor(IGraphNode nodeA,
			IGraphNode nodeB) throws InvalidElementException;
	
	/**
	 * @return
	 */
	public double getMaxDepth();
}
