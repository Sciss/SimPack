/*
 * $Id: EquivalenceClassCalculator.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import simpack.api.ITreeNode;
import simpack.api.impl.AbstractCalculator;
import simpack.exception.InvalidElementException;

/**
 * This object is used to calculate the equivalence classes of two given trees.
 * 
 * The algorithms herein are adapted from a C++ implementation (based on LEDA)
 * by Gabriel Valiente. See http://www.lsi.upc.es/~valiente/algorithm/combin.cpp
 * for more information.
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class EquivalenceClassCalculator extends AbstractCalculator {

	/**
	 * Contains the first tree
	 */
	private ITreeNode tree1;

	/**
	 * The first trees preordered list of nodes
	 */
	private List<ITreeNode> list1 = null;

	/**
	 * Contains the second tree
	 */
	private ITreeNode tree2;

	/**
	 * The second trees preordered list of nodes
	 */
	private List<ITreeNode> list2 = null;

	/**
	 * Indicates whether the trees are ordered or not
	 */
	private boolean ordered;

	/**
	 * Indicates whether the trees are labeled or not
	 */
	private boolean labeled;

	/**
	 * Global equivalence codes
	 */
	private LinkedHashMap<LinkedList<Integer>, Integer> globalCodes = new LinkedHashMap<LinkedList<Integer>, Integer>();

	/**
	 * Counter for the current maximum equivalence code
	 */
	private int currentMaxEquivalence = 1;

	/**
	 * Contains the equivalence class codes for each tree1
	 */
	private LinkedHashMap<ITreeNode, Integer> code1 = new LinkedHashMap<ITreeNode, Integer>();

	/**
	 * Contains the equivalence class codes for each tree2
	 */
	private LinkedHashMap<ITreeNode, Integer> code2 = new LinkedHashMap<ITreeNode, Integer>();

	/**
	 * Construct new calculation object with the given unordered, unlabeled
	 * trees.
	 * 
	 * @param tree1
	 * @param tree2
	 */
	public EquivalenceClassCalculator(ITreeNode tree1, List<ITreeNode> list1,
			ITreeNode tree2, List<ITreeNode> list2)
			throws NullPointerException, InvalidElementException {
		this(tree1, list1, tree2, list2, false, false);
	}

	/**
	 * Construct new calculation object with the given unlabeled trees.
	 * 
	 * @param tree1
	 * @param tree2
	 */
	public EquivalenceClassCalculator(ITreeNode tree1, List<ITreeNode> list1,
			ITreeNode tree2, List<ITreeNode> list2, boolean ordered)
			throws NullPointerException, InvalidElementException {
		this(tree1, list1, tree2, list2, ordered, false);
	}

	/**
	 * Construct new calculation object with the given trees.
	 * 
	 * @param tree1
	 * @param tree2
	 * @param ordered
	 *            treat the trees as ordered trees if true
	 * @param labeled
	 *            treat the trees as labeled trees if true
	 */
	public EquivalenceClassCalculator(ITreeNode tree1, List<ITreeNode> list1,
			ITreeNode tree2, List<ITreeNode> list2, boolean ordered,
			boolean labeled) throws NullPointerException,
			InvalidElementException {
		super();
		// verify data not null
		if (tree1 == null || tree2 == null || list1 == null || list2 == null)
			throw new NullPointerException();

		this.ordered = ordered;
		this.labeled = labeled;

		this.tree1 = tree1;
		this.list1 = list1;
		this.tree2 = tree2;
		this.list2 = list2;
	}

	public boolean calculate() {
		setCalculated(false);
		try {
			calculateEquivalenceClass(list1, code1);
			calculateEquivalenceClass(list2, code2);
		} catch (InvalidElementException e) {
			return false;
		}
		setCalculated(true);
		return true;
	}

	/**
	 * Calculates the evidence class for the given node list.
	 * 
	 * @param list
	 * @param code
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 */
	private void calculateEquivalenceClass(List<ITreeNode> list,
			LinkedHashMap<ITreeNode, Integer> code)
			throws InvalidElementException {

		for (ListIterator<ITreeNode> iter = list.listIterator(); iter.hasNext();) {
			ITreeNode node = iter.next();
			// a leaf has an equivalence class = 1
			if (node.isLeaf()) {
				// TODO: need adding label for different node types? analogue to
				// code for non-leafs
				code.put(node, 1);
			} else {
				LinkedList<Integer> childCodeList = new LinkedList<Integer>();
				for (Enumeration enumer = node.children(); enumer
						.hasMoreElements();) {
					Object o = enumer.nextElement();
					if (o instanceof ITreeNode) {
						ITreeNode child = (ITreeNode) o;
						// need not checking for not existant code here as the
						// list is postordered
						childCodeList.add(code.get(child));
					} else {
						throw new InvalidElementException(
								"Unexpected child type in Tree while calculating child codes.");
					}
				}

				// order of childs is not important if tree is unordered
				if (!ordered) {
					Collections.sort(childCodeList);
				}

				// put Node type (FAMIXConstants.getType(node) on top of list to
				// do labeled matching
				if (labeled) {
					// TODO need to label the elements somehow
					// childCodeList.add(0, new
					// Integer(FAMIXConstants.getType(node.getUserObject())));
				}

				if (globalCodes.containsKey(childCodeList)) {
					code.put(node, globalCodes.get(childCodeList));
				} else {
					globalCodes.put(childCodeList, ++currentMaxEquivalence);
					code.put(node, currentMaxEquivalence);
				}
			}
		}
	}

	/**
	 * @return Returns the equivalence classes for tree1
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public LinkedHashMap<ITreeNode, Integer> getEquivalenceClassesTree1()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return code1;
	}

	/**
	 * @return Returns the equivalence classes for tree2
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public LinkedHashMap<ITreeNode, Integer> getEquivalenceClassesTree2()
			throws NullPointerException {
		if (!isCalculated())
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		return code2;
	}

	/**
	 * @return Returns the tree1.
	 */
	public ITreeNode getTree1() {
		return tree1;
	}

	/**
	 * @return Returns the tree2.
	 */
	public ITreeNode getTree2() {
		return tree2;
	}
}
