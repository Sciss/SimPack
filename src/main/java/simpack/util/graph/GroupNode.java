/*
 * $Id: GroupNode.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.TreeSet;

import simpack.api.IGraphNode;
import simpack.api.IGroupNode;

/**
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class GroupNode extends GraphNode implements IGroupNode {

	private static final long serialVersionUID = -2093338248200928756L;

	/**
	 * This will contain all the single nodes of the group node
	 */
	private TreeSet<IGraphNode> groupSet = new TreeSet<IGraphNode>();

	/**
	 * This will contain the number of single nodes of the group node
	 */
	private int groupSize = 0;

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given userObject
	 * 
	 * @param userObject
	 *            user object to be wrapped by this group node
	 */
	public GroupNode(Object userObject) {
		this.userObject = userObject;
		isGroup = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGroupNode#setGroupNodes(java.util.TreeSet)
	 */
	public void setGroupNodes(TreeSet<IGraphNode> groupSet) {
		this.groupSet = groupSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGroupNode#setGroupSize(int)
	 */
	public void setGroupSize(int size) {
		groupSize = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getGroupSize()
	 */
	public int getGroupSize() {
		return groupSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getIsGroup()
	 */
	public boolean getIsGroup() {
		return isGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphNode#getGroupNodes()
	 */
	public TreeSet<IGraphNode> getGroupNodes() {
		return groupSet;
	}
}