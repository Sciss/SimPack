/*
 * $Id: NodeComparator.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.tree.comparator;

import java.io.Serializable;
import java.util.Comparator;

import simpack.api.ITreeNodeComparator;
import simpack.util.tree.NodePriority;
import simpack.util.tree.TreeNodePriorityTuple;

/**
 * Comparator for TreeNodePriorityTuple objects.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 * @see Comparator
 * @see TreeNodePriorityTuple
 */
public class NodeComparator implements
		ITreeNodeComparator<TreeNodePriorityTuple>, Serializable {

	private static final long serialVersionUID = 5478893108654929519L;

	/**
	 * Compares two TreeNodePriorityTuples.
	 * 
	 * @param tuple1
	 * @param tuple2
	 * @return -1, 0 or 1 depending on if n1 is smaller, equal or greater than
	 *         n2
	 * @throws ClassCastException,
	 *             NullPointerException
	 */
	public int compare(TreeNodePriorityTuple tuple1,
			TreeNodePriorityTuple tuple2) throws ClassCastException,
			NullPointerException {
		if (tuple1 == null || tuple2 == null)
			throw new NullPointerException();

		NodePriority n1 = tuple1.getPriority();
		NodePriority n2 = tuple2.getPriority();
		if (n1 == null || n2 == null)
			throw new NullPointerException();

		return NodePriority.compare((NodePriority) n1, (NodePriority) n2);
	}

}
