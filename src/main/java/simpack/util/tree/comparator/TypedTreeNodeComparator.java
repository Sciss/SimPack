/*
 * $Id: TypedTreeNodeComparator.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.api.ITreeNodeComparator;
import simpack.api.ITreeNode;

/**
 * Compares two tree nodes for equality. Checking on equality of user object
 * type.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class TypedTreeNodeComparator implements ITreeNodeComparator<ITreeNode> {

	/**
	 * Constructor.
	 */
	public TypedTreeNodeComparator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.toe.tree.ITreeComparator#compare(javax.swing.tree.DefaultMutableTreeNode,
	 *      javax.swing.tree.DefaultMutableTreeNode)
	 */
	public int compare(ITreeNode node1, ITreeNode node2) {
		if (node1 == null || node2 == null) {
			throw new NullPointerException();
		} else {
			if (node1.getUserObject() == null || node2.getUserObject() == null) {
				throw new NullPointerException();
			}
			if (node1.getUserObject().getClass() == node2.getUserObject()
					.getClass()) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}
