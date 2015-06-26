/*
 * $Id: MappedVertexComparator.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.api.IGraphNodeComparator;
import simpack.util.graph.MappedVertex;

/**
 * Named graph node comparison class. Compares the labels (names) of two graph
 * nodes.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class MappedVertexComparator implements
		IGraphNodeComparator<MappedVertex>, Serializable {

	private static final long serialVersionUID = 6137186334656355169L;

	/**
	 * Compares two mapped vertices.
	 * 
	 * @param v1
	 *            first mapped vertex
	 * 
	 * @param v2
	 *            second mapped vertex
	 * 
	 * @return the value <code>0</code> if the argument string is equal to
	 *         this string; a value less than <code>0</code> if this string is
	 *         lexicographically less than the string argument; and a value
	 *         greater than <code>0</code> if this string is lexicographically
	 *         greater than the string argument.
	 */
	public int compare(MappedVertex v1, MappedVertex v2) {
		return v1.toString().compareTo(v2.toString());
	}
}
