/*
 * $Id: NamedGraphNodeComparator.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.graph.comparator;

import java.io.Serializable;

import simpack.api.IGraphNode;
import simpack.api.IGraphNodeComparator;

/**
 * Named graph node comparison class. Compares the labels (names) of two graph
 * nodes.
 * 
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class NamedGraphNodeComparator implements
		IGraphNodeComparator<IGraphNode>, Serializable {

	private static final long serialVersionUID = 4997348735184341449L;

	/**
	 * Compares two IGraphNodes. Throws an exception if the nodes are
	 * <code>null</code>.
	 * 
	 * @param node1
	 *            first graph node
	 * @param node2
	 *            second graph node
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 */
	public int compare(IGraphNode node1, IGraphNode node2) {
		if (node1 == null || node2 == null) {
			throw new NullPointerException();
		} else {
			Object o1 = node1.getUserObject();
			Object o2 = node2.getUserObject();
			return o1.toString().compareTo(o2.toString());
		}
	}
}
