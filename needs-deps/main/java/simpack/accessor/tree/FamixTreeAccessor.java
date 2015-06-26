/*
 * $Id: FamixTreeAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.api.ITreeNode;
import simpack.api.impl.AbstractTreeAccessor;
import simpack.util.tree.visitor.FamixTreeBuildVisitor;
import ch.toe.famix.FAMIXInstance;

/**
 * This is a tree accessor in the style of {@link simpack.api.ITreeAccessor}
 * returning a tree parsed from a {@link ch.toe.famix.FAMIXInstance}.
 * 
 * @author Tobias Sager
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 * @see ch.toe.famix.FAMIXInstance
 */
public class FamixTreeAccessor extends AbstractTreeAccessor {

	/**
	 * Saves the instance to parse
	 */
	private FAMIXInstance famixInstance = null;

	/**
	 * Constructor.
	 * <p>
	 * Instantiates a new accessor for a <code>FAMIXInstance</code> object.
	 * 
	 * @param famix
	 *            root node of this FAMIX tree
	 * @see FAMIXInstance
	 */
	public FamixTreeAccessor(FAMIXInstance famix) {
		famixInstance = famix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.accessor.tree.ITreeAccessor#getRoot()
	 */
	public ITreeNode getRoot() {
		// if tree has not yet been built, build it, return old tree otherwise
		if (tree == null) {
			FamixTreeBuildVisitor visitor = new FamixTreeBuildVisitor();
			famixInstance.accept(visitor);
			tree = visitor.getRoot();
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
