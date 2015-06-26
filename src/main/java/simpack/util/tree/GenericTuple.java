/*
 * $Id: GenericTuple.java 757 2008-04-17 17:42:53Z kiefer $
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
 * This class implementes a tuple, i.e., a pair of objects. One object occurs on
 * the left and the other object on the right.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class GenericTuple {

	private Object left;

	private Object right;

	/**
	 * Constructor.
	 * <p>
	 * Takes the left and the right object of this object tuple.
	 * 
	 * @param left
	 *            the left object
	 * @param right
	 *            the right object
	 */
	public GenericTuple(Object left, Object right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Compares the instance to a given object
	 * 
	 * @param t
	 *            the tuple to be compared to this tuple
	 * @return true if the left and right objects match, false otherwise
	 */
	public boolean equals(GenericTuple t) {
		if (t == null)
			return false;
		return left.equals(t.getLeft()) && right.equals(t.getRight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getLeft().toString() + ":" + getRight().toString();
	}

	/**
	 * Return the left object.
	 * 
	 * @return left object.
	 */
	public Object getLeft() {
		return left;
	}

	/**
	 * Return the right object.
	 * 
	 * @return right object
	 */
	public Object getRight() {
		return right;
	}
}
