/*
 * $Id: AbstractGraphAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.util.graph.comparator.NamedGraphNodeComparator;

/**
 * @author Daniel Baggenstos
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractGraphAccessor implements IGraphAccessor {

	private static Logger logger = LogManager
			.getLogger(AbstractGraphAccessor.class);

	/**
	 * This will contain the complete node set of the graph
	 */
	protected TreeSet<IGraphNode> nodeSet = new TreeSet<IGraphNode>(
			new NamedGraphNodeComparator());

	/**
	 * The first root (a node without incoming but only outgoing edges) found in
	 * the graph.
	 */
	protected IGraphNode root;

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#size()
	 */
	public int size() {
		return nodeSet.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getNodeSet()
	 */
	public TreeSet<IGraphNode> getNodeSet() {
		return nodeSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getNode(java.lang.String)
	 */
	public IGraphNode getNode(String label) {
		for (IGraphNode node : nodeSet) {
			if (node.getLabel().equals(label)) {
				return node;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#addNode(simpack.api.IGraphNode)
	 */
	public void addNode(IGraphNode node) {
		nodeSet.add(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#contains(simpack.api.IGraphNode)
	 */
	public boolean contains(IGraphNode node) {
		for (IGraphNode n : nodeSet) {
			if (n.equals(node)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getRoot()
	 */
	public IGraphNode getRoot() {
		if (root == null) {
			for (IGraphNode node : nodeSet) {
				if (node.getPredecessorSet().size() == 0)
					root = node;
			}
		}
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#setEdge(simpack.api.IGraphNode,
	 *      simpack.api.IGraphNode)
	 */
	public void setEdge(IGraphNode tmpSourceNode, IGraphNode tmpTargetNode) {
		IGraphNode sourceNode = null;
		logger.debug("Want to set an edge for " + tmpSourceNode.getLabel()
				+ " -> " + tmpTargetNode.getLabel());
		if (contains(tmpSourceNode)) {
			sourceNode = getNode(tmpSourceNode.getLabel());
			logger.debug("Node contained " + sourceNode.toString());
		} else {
			sourceNode = tmpSourceNode;
			logger.debug("Adding node " + sourceNode.toString());
			addNode(sourceNode);
		}
		IGraphNode targetNode = null;
		if (contains(tmpTargetNode)) {
			targetNode = getNode(tmpTargetNode.getLabel());
			logger.debug("Node contained " + targetNode.toString());
		} else {
			targetNode = tmpTargetNode;
			logger.debug("Adding node " + targetNode.toString());
			addNode(targetNode);
		}
		logger.debug("Adding " + sourceNode.toString() + " -> "
				+ targetNode.toString());
		sourceNode.addSuccessor(targetNode);
		targetNode.addPredecessor(sourceNode);
	}
}
