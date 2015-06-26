/*
 * $Id: TreeVisitor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.tree.visitor;

import java.util.Enumeration;

import simpack.api.ITreeNode;

/**
 * This defines the abstract class TreeVisitor which has the purpose of visiting
 * all the trees nodes and calling a visit-method for each one.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class TreeVisitor {

	/**
	 * Given root element
	 */
	protected ITreeNode root;

	/**
	 * Constructor.
	 * <p>
	 * Constructs a new TreeVisitor. The root of the tree is specified by
	 * <code>root</code>.
	 * 
	 * @param root
	 *            the root of the tree
	 */
	public TreeVisitor(ITreeNode root) {
		this.root = root;
	}

	/**
	 * Method to be overriden by children. It will be called for all nodes found
	 * in the tree.
	 * 
	 * @param node
	 *            the tree node be visited by the visitor
	 * @return true if childre are to be visited, false otherwise
	 */
	public abstract boolean visit(ITreeNode node);

	/**
	 * Method to be overriden by children. It will be called for all nodes found
	 * in tree after having visited all the node's chilren. Will not be called
	 * when visit was false.
	 * 
	 * @param node
	 *            the tree node to be post-visited by the visitor
	 */
	public abstract void postVisit(ITreeNode node);

	/**
	 * Accept node and visit all children of the node.
	 * 
	 * @param node
	 *            the tree node be visited by the visitor
	 */
	public void accept(ITreeNode node) {
		if (visit(node)) {
			Enumeration enumer = node.children();
			while (enumer.hasMoreElements()) {
				ITreeNode child = (ITreeNode) enumer.nextElement();
				accept(child);
			}
			postVisit(node);
		}
	}
}
