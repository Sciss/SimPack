/*
 * $Id: GraphVertexTuple.java 757 2008-04-17 17:42:53Z kiefer $
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
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class GraphVertexTuple extends GenericTuple {

	private TreeNodeTuple treeNodeTuple;

	/**
	 * Constructor.
	 * <p>
	 * Takes two Integer objects which represent the objects of this tuple.
	 * 
	 * @param left
	 *            left Integer object
	 * @param right
	 *            right Integer object
	 */
	public GraphVertexTuple(Integer left, Integer right) {
		super(left, right);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two integer values which represent the objects of this tuple.
	 * 
	 * @param left
	 *            left integer object
	 * @param right
	 *            right integer object
	 */
	public GraphVertexTuple(int left, int right) {
		super(new Integer(left), new Integer(right));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getLeft().toString() + ":" + getRight().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.util.tree.GenericTuple#getLeft()
	 */
	public Integer getLeft() {
		return (Integer) super.getLeft();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.util.tree.GenericTuple#getRight()
	 */
	public Integer getRight() {
		return (Integer) super.getRight();
	}

	/**
	 * Return this object tuple.
	 * 
	 * @return this tuple.
	 */
	public TreeNodeTuple getTreeNodeTuple() {
		return treeNodeTuple;
	}

	/**
	 * Sets this object tuple to <code>treeNodeTuple</code>.
	 * 
	 * @param treeNodeTuple
	 *            the treeNodeTuple to be set
	 */
	public void setTreeNodeTuple(TreeNodeTuple treeNodeTuple) {
		this.treeNodeTuple = treeNodeTuple;
	}
}
