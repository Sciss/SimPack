/*
 * $Id: AbstractTreeSimilarityMeasure.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl;

import simpack.api.ITreeNode;
import simpack.api.ITreeNodeComparator;
import simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator;

/**
 * General class for calculating tree similarity. This currently only implies
 * having a {@link simpack.api.ITreeNodeComparator ITreeNodeComparator} for
 * comparing objects while calculating.
 * 
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractTreeSimilarityMeasure extends AbstractSimilarityMeasure {

	/**
	 * This will contain the used tree comparator
	 */
	protected ITreeNodeComparator<ITreeNode> comparator;

	/**
	 * Constructor
	 * <p>
	 * The comparator is always true.
	 * 
	 * @see simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator
	 */
	public AbstractTreeSimilarityMeasure() {
		this(new AlwaysTrueTreeNodeComparator());
	}

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given comparator
	 * 
	 * @param comparator
	 */
	public AbstractTreeSimilarityMeasure(ITreeNodeComparator<ITreeNode> comparator) {
		if (comparator != null) {
			this.comparator = comparator;
		} else {
			this.comparator = new AlwaysTrueTreeNodeComparator();
		}
	}

	/**
	 * Return the tree node comparator that is used to compare nodes in the tree
	 * for equality.
	 * 
	 * @return tree node comparator
	 */
	public ITreeNodeComparator getComparator() {
		return comparator;
	}
}
