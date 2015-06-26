/*
 * $Id: TreeNode.java 757 2008-04-17 17:42:53Z kiefer $
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

import javax.swing.tree.DefaultMutableTreeNode;

import simpack.api.ITreeNode;

/**
 * This is the default implementation for interface
 * {@link simpack.api.ITreeNode ITreeNode}.
 * 
 * @author Tobias Sager
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class TreeNode extends DefaultMutableTreeNode implements ITreeNode {

	private static final long serialVersionUID = -5366774557189363700L;

	/**
	 * Constructor.
	 */
	public TreeNode() {
		super();
	}

	/**
	 * Constructor.
	 * <p>
	 * The userObject to be stored in the tree node is passed.
	 * 
	 * @param userObject
	 *            a user-specified object to be stored in the tree node
	 */
	public TreeNode(Object userObject) {
		super(userObject);
	}

	/**
	 * Constructor.
	 * <p>
	 * The userObject to be stored in the tree node is passed. Additionally, the
	 * boolean flag allowsChildren specifies if this tree node is allowed to
	 * have children.
	 * 
	 * @param userObject
	 *            a user-specified object to be stored in the tree node
	 * @param allowsChildren
	 *            if true, this tree node is allowed to have children,
	 *            otherwise, no children must be added
	 */
	public TreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#getRoot()
	 */
	public ITreeNode getRoot() {
		return (ITreeNode) super.getRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#getFirstChild()
	 */
	public ITreeNode getFirstChild() {
		return (ITreeNode) super.getFirstChild();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeNode#getChildAfter(simpack.api.ITreeNode)
	 */
	public ITreeNode getChildAfter(ITreeNode v1) {
		return (ITreeNode) super.getChildAfter(v1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ITreeNode#add(simpack.api.ITreeNode)
	 */
	public void add(ITreeNode clone) {
		super.add(clone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#getLastChild()
	 */
	public ITreeNode getLastChild() {
		return (ITreeNode) super.getLastChild();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#getPreviousSibling()
	 */
	public TreeNode getPreviousSibling() {
		return (TreeNode) super.getPreviousSibling();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (userObject != null) {
			return userObject.toString();
		} else {
			return "";
		}
	}
}
