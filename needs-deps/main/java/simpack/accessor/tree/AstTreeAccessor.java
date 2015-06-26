/*
 * $Id: AstTreeAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.tree;

import org.eclipse.jdt.core.dom.ASTNode;

import simpack.api.ITreeNode;
import simpack.api.impl.AbstractTreeAccessor;
import simpack.util.tree.TreeNode;
import simpack.util.tree.visitor.ast.ASTFullTransformer;

/**
 * This is a tree accessor in the style of {@link simpack.api.ITreeAccessor}
 * returning a tree parsed from a {@link org.eclipse.jdt.core.dom.ASTNode}.
 * 
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class AstTreeAccessor extends AbstractTreeAccessor {

	private ASTNode astRoot;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            root node of this AST tree
	 */
	public AstTreeAccessor(ASTNode root) {
		this.astRoot = root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.accessor.tree.ITreeAccessor#getRoot()
	 */
	public ITreeNode getRoot() {
		// if tree has not yet been built, build it, return old tree otherwise
		if (tree == null) {
			tree = new TreeNode(null);
			ASTFullTransformer transformer = new ASTFullTransformer(tree);
			astRoot.accept(transformer);

			// remove first, empty node
			ITreeNode newRoot = (TreeNode) tree.children().nextElement();
			newRoot.removeFromParent();
			tree = newRoot;
		}
		return tree;
	}

	public double getMaximumDirectedPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getShortestPath(ITreeNode nodeA, ITreeNode nodeB) {
		// TODO Auto-generated method stub
		return 0;
	}
}
