/*
 * $Id: ConceptualSimilarity.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.api.impl.AbstractSimilarityMeasure;
import simpack.exception.InvalidElementException;

/**
 * This class implements the conceptual similarity measure proposed by Z.Wu &
 * M.Palmer in "Verb Semantics and Lexical Selection" (1994).
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ConceptualSimilarity extends AbstractSimilarityMeasure {

	static Logger logger = LogManager.getLogger(ConceptualSimilarity.class);

	private double N1, N2, N3;

	private IGraphAccessor graphAccessor;

	private IGraphNode nodeN1, nodeN2;

	/**
	 * Constructor.
	 * <p>
	 * Direclty pass the three distance values used by this measure.
	 * 
	 * @param N1
	 *            length of path from node1 to the most recent common ancestor
	 *            of node1 and node2
	 * @param N2
	 *            length of path from node2 to the most recent common ancestor
	 *            of node2 and node1
	 * @param N3
	 *            length of path from the most recent common ancestor of node1
	 *            and node2 to the root of the tree
	 */
	public ConceptualSimilarity(double N1, double N2, double N3) {
		this.N1 = N1;
		this.N2 = N2;
		this.N3 = N3;
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes a tree accessor and two nodes of the tree. The accessor is used to
	 * access the tree structur of the tree nodes.
	 * 
	 * @param accessor
	 *            tree accessor
	 * @param nodeN1
	 *            first node
	 * @param nodeN2
	 *            second node
	 * @throws InvalidElementException
	 *             this exceptionis thrown if the first or second tree node is
	 *             invalid, i.e., does not occur in the tree for instance
	 */
	public ConceptualSimilarity(IGraphAccessor accessor, IGraphNode nodeN1,
			IGraphNode nodeN2) throws InvalidElementException {
		this.graphAccessor = accessor;
		this.nodeN1 = nodeN1;
		this.nodeN2 = nodeN2;

		if (!accessor.contains(nodeN1)) {
			throw new InvalidElementException(nodeN1.toString()
					+ " not in graph");
		}

		if (!accessor.contains(nodeN2)) {
			throw new InvalidElementException(nodeN2.toString()
					+ " not in graph");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		if (nodeN1 != null) {
			IGraphNode mrca = null;
			try {
				mrca = graphAccessor
						.getMostRecentCommonAncestor(nodeN1, nodeN2);
				IGraphNode root = graphAccessor.getRoot();
				N3 = graphAccessor.getShortestPath(mrca, root);
				logger.debug("N3 = " + N3); 
				double N1N2 = graphAccessor.getShortestPath(nodeN1, nodeN2);
				logger.debug("Shortest Path = " + N1N2);

				// if N3 is 0, mrca IS the root of the ontology
				if (N3 == 0d) {
					similarity = new Double(1d / (N1N2 + 1d));
				} else {
					similarity = new Double((2d * N3) / (N1N2 + 2d * N3));
				}
				setCalculated(true);
				return true;
			} catch (InvalidElementException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			similarity = new Double((2d * N3) / (N1 + N2 + 2d * N3));
			setCalculated(true);
			return true;
		}
	}
}