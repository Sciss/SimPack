/*
 * $Id: NodePriority.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.tree;

/**
 * This class stores the size and code (equivalence class) of a tree node.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class NodePriority {

	/**
	 * Size of node
	 */
	private Integer size;

	/**
	 * Equivalence class
	 */
	private Integer code;

	/**
	 * Constructor.
	 * <p>
	 * Takes the size and equivalence class code as inputs.
	 * 
	 * @param size
	 *            the size of the node
	 * @param code
	 *            the equivalence class code of the node
	 */
	public NodePriority(Integer size, Integer code) {
		if (size == null || code == null)
			throw new NullPointerException();
		this.size = size;
		this.code = code;
	}

	/**
	 * Checks for equality in priority.
	 * 
	 * @param np
	 * @return true if this instance has the same priority as the given
	 *         NodePriority
	 */
	public boolean equals(NodePriority np) {
		return NodePriority.compare(this, np) == 0;
	}

	/**
	 * Compare this instance to a NodePriority and decide which one has the
	 * higher priority. The sort order is: descending getSize(), ascending
	 * getCode()
	 * 
	 * @param np
	 *            NodePriority this NodePriority is compared to
	 * @return -1, 0 or 1 depending on if n1's priority is smaller, equal or
	 *         greater than n2's
	 * @throws ClassCastException
	 * @throws NullPointerException
	 */
	public int compare(NodePriority np) throws ClassCastException,
			NullPointerException {
		return NodePriority.compare(this, np);
	}

	/**
	 * Compares two NodePriorities and decides which one has the higher
	 * priority. The sort order is: descending getSize(), ascending getCode()
	 * 
	 * @param n1
	 *            the first NodePriority
	 * @param n2
	 *            the second NodePriority
	 * @return -1, 0 or 1 depending on if n1's priority is smaller, equal or
	 *         greater than n2's
	 * @throws ClassCastException
	 * @throws NullPointerException
	 */
	public static int compare(NodePriority n1, NodePriority n2)
			throws ClassCastException, NullPointerException {
		if (n1 == null || n2 == null)
			throw new NullPointerException();

		if (n1.getSize() > n2.getSize())
			return -1;
		else if (n1.getSize() < n2.getSize())
			return 1;
		// size equals
		else {
			if (n1.getCode().equals(n2.getCode()))
				return 0;
			else if (n1.getCode() < n2.getCode())
				return -1;
			else if (n1.getCode() > n2.getCode())
				return 1;
			// should not be reached
			else
				throw new ClassCastException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + getCode().toString() + "," + getSize().toString() + ")";
	}

	/**
	 * @return Returns the code of the tree node.
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @return Returns the size of the tree node.
	 */
	public Integer getSize() {
		return size;
	}
}
