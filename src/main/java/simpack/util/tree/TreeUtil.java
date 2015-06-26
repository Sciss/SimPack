/*
 * $Id: TreeUtil.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;

/**
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class TreeUtil {

	/**
	 * Count number of nodes in tree.
	 * 
	 * @param tree
	 * @return number of tree nodes
	 */
	public static int calculateSize(ITreeNode tree) {
		int size = 1;
		Enumeration enumer = tree.children();
		while (enumer.hasMoreElements()) {
			ITreeNode child = (ITreeNode) enumer.nextElement();
			size += calculateSize(child);
		}
		return size;
	}

	/**
	 * Converts a given Enumeration of DefaultMutableTreeNode elements into a
	 * List of these elements.
	 * 
	 * @param enumeration
	 * @return the converted list, empty if no elements in input
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 */
	public static List<ITreeNode> enumerationToList(Enumeration enumeration)
			throws InvalidElementException {
		LinkedList<ITreeNode> ret = new LinkedList<ITreeNode>();
		while (enumeration.hasMoreElements()) {
			Object o = enumeration.nextElement();
			if (o instanceof ITreeNode) {
				ret.add((ITreeNode) o);
			} else
				throw new InvalidElementException(
						"Unexpected child type in Tree while converting enumeration.");
		}
		return ret;
	}

	/**
	 * Similarity measure, weighting searchObjectSize*4+matchedObjectSize*1
	 * 
	 * @param completeSearchObject
	 * @param completeMatchedObject
	 * @param matchedObject
	 * @return similarity, between 0 and 1 where 1 is 100% match
	 */
	public static Double getSimilarity4to1(ITreeNode completeSearchObject,
			ITreeNode completeMatchedObject, ITreeNode matchedObject) {
		Double completeMatchedObjectSize = new Double(TreeUtil
				.calculateSize(completeMatchedObject));
		Double completeSearchObjectSize = new Double(TreeUtil
				.calculateSize(completeSearchObject));
		Double matchedObjectSize = new Double(TreeUtil
				.calculateSize(matchedObject));
		if (completeMatchedObjectSize == 0 || completeSearchObjectSize == 0)
			return 0.D;

		return (4 * (matchedObjectSize / completeSearchObjectSize) + (matchedObjectSize / completeMatchedObjectSize)) / 5;
	}

	/**
	 * Similarity measure, weighting searchObjectSize*1+matchedObjectSize*1
	 * 
	 * @param completeSearchObject
	 * @param completeMatchedObject
	 * @param matchedObject
	 * @return similarity, between 0 and 1 where 1 is 100% match
	 */
	public static Double getSimilarity1to1(ITreeNode completeSearchObject,
			ITreeNode completeMatchedObject, ITreeNode matchedObject) {
		Double completeMatchedObjectSize = new Double(TreeUtil
				.calculateSize(completeMatchedObject));
		Double completeSearchObjectSize = new Double(TreeUtil
				.calculateSize(completeSearchObject));
		Double matchedObjectSize = new Double(TreeUtil
				.calculateSize(matchedObject));
		if (completeMatchedObjectSize == 0 || completeSearchObjectSize == 0)
			return 0.D;

		return (2 * matchedObjectSize / (completeSearchObjectSize + completeMatchedObjectSize));
	}

	/**
	 * Similarity measure, weighting appropriate to completeness of
	 * matchedObject match
	 * 
	 * @param completeSearchObject
	 * @param completeMatchedObject
	 * @param matchedObject
	 * @return similarity, between 0 and 1 where 1 is 100% match
	 */
	public static Double getSimilarityMatchedWeightened(
			ITreeNode completeSearchObject, ITreeNode completeMatchedObject,
			ITreeNode matchedObject) {
		Double completeMatchedObjectSize = new Double(TreeUtil
				.calculateSize(completeMatchedObject));
		Double completeSearchObjectSize = new Double(TreeUtil
				.calculateSize(completeSearchObject));
		Double matchedObjectSize = new Double(TreeUtil
				.calculateSize(matchedObject));
		Double percentMatchedSearchObject = matchedObjectSize
				/ completeSearchObjectSize;
		Double percentMatchedObject = matchedObjectSize
				/ completeMatchedObjectSize;

		// search object has more importance
		if (percentMatchedObject > 0.8) {
			return (2 * percentMatchedSearchObject + percentMatchedObject) / 3;
		}
		// matched object has more importance
		else if (percentMatchedObject < 0.2) {
			return (percentMatchedSearchObject + 2 * percentMatchedObject) / 3;
		}
		// equal importance of both objects
		return (percentMatchedSearchObject + percentMatchedObject) / 2;
	}
}
