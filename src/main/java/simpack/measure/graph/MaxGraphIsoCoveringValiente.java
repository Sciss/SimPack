/*
 * $Id: MaxGraphIsoCoveringValiente.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.util.graph.Clique;
import simpack.util.graph.MappedVertex;
import simpack.util.graph.comparator.MappedVertexComparator;
import simpack.util.graph.comparator.NamedGraphNodeComparator;

/**
 * @author Christoph Kiefer
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class MaxGraphIsoCoveringValiente extends MaxCommonSubgraphIsoValiente {

	public static Logger logger = LogManager
			.getLogger(MaxGraphIsoCoveringValiente.class);

	public static String DEFAULT_GRAPH_TO_COVER = "larger";

	public static boolean DEFAULT_COVERAGE_STYLE = false;

	private String graphToCover = DEFAULT_GRAPH_TO_COVER;

	private boolean bothDisjointCoverage = DEFAULT_COVERAGE_STYLE;

	private TreeMap<Integer, ArrayList<Clique>> iso;

	private ArrayList<Clique> covering;

	/**
	 * Constructor.
	 * 
	 * @param graphAccessor1
	 *            first graph accessor
	 * @param graphAccessor2
	 *            second graph accessor
	 */
	public MaxGraphIsoCoveringValiente(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2) {

		super(graphAccessor1, graphAccessor2,
				MaxCommonSubgraphIsoValiente.DEFAULT_MIN_CLIQUE_SIZE,
				MaxCommonSubgraphIsoValiente.DEFAULT_STRUCTURE_WEIGHT,
				MaxCommonSubgraphIsoValiente.DEFAULT_LABEL_WEIGHT,
				MaxCommonSubgraphIsoValiente.DEFAULT_DENOMINATOR);

		iso = new TreeMap<Integer, ArrayList<Clique>>();
	}

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given accessors, the minimum of MappedVertices a maximal
	 * clique should contain, the weight of the structural component (between 0
	 * and 1) and the basis of the comparison (>0 = first graph, <0 = secong
	 * graph, 0 = average graph size).
	 * 
	 * @param graphAccessor1
	 *            first graph accessor
	 * @param graphAccessor2
	 *            second graph accessor
	 * @param minCliqueSize
	 *            minimum clique size
	 * @param structureWeight
	 *            weight of the structural component
	 * @param labelWeight
	 *            weight of the label component
	 * @param denominator
	 *            case(denominator) if "first", size of first graph. if "small",
	 *            size of smaller graph. if "big", size of bigger graph. if
	 *            "average", average size of both graphs.
	 * @param graphToCover
	 *            indicates which graph should be covered by the algorithm, if
	 *            "smaller" the smaller of the two graphs will be covered, if
	 *            "bigger" or anything else, the bigger of the graphs will be
	 *            covered. Note, that a complete graphToCover is only possible
	 *            if <code>minCliqueSize</code> is set to 1. In that case, the
	 *            graph to be covered can be 'filled up' with 1-to-1 node
	 *            isomorphism.
	 */
	public MaxGraphIsoCoveringValiente(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2, int minCliqueSize,
			double structureWeight, double labelWeight, String denominator,
			String graphToCover, boolean bothDisjointCoverage) {

		super(graphAccessor1, graphAccessor2, minCliqueSize, structureWeight,
				labelWeight, denominator);

		this.graphToCover = graphToCover;
		this.bothDisjointCoverage = bothDisjointCoverage;
		iso = new TreeMap<Integer, ArrayList<Clique>>();
	}

	/**
	 * Algorithm calculates for a certain clique all MappedVertices with which
	 * the clique can further be enhanced. This method is invoked recursively
	 * for each additional node in the clique.
	 * 
	 * @param candidates
	 * @param clique
	 */
	public void nextMaximalClique(TreeSet<MappedVertex> candidates,
			TreeSet<MappedVertex> visited, TreeSet<MappedVertex> clique,
			int shortest) {
		countLoop++;
		if (Math.IEEEremainder(countLoop, 2000000) == 0) {
			logger.debug("countLoop: " + countLoop);
		}
		MappedVertex v;
		TreeSet<MappedVertex> clique2 = new TreeSet<MappedVertex>(
				new MappedVertexComparator());
		for (MappedVertex copy : clique)
			clique2.add(copy);

		if (candidates.isEmpty()) { // no more candidates == clique is maximal
			if (visited.isEmpty()) { // was the clique already discovered
				// before in some other way
				if (clique2.size() >= shortest) {
					countCliq++;
					if (Math.IEEEremainder(countCliq, 200000) == 0) {
						logger.debug("Cliq: " + countCliq + " " + clique2);
					}

					double sim = getCliqueSimilarity(clique2);

					Clique c = new Clique(clique2, sim, true);
					int key = c.getClique().size();

					if (iso.containsKey(key)) {
						ArrayList<Clique> list = iso.get(c.getClique().size());
						list.add(c);
					} else {
						ArrayList<Clique> list = new ArrayList<Clique>();
						list.add(c);
						iso.put(key, list);
					}

				}
			}
		} else {

			if (clique2.size() >= shortest) {

				double sim = getCliqueSimilarity(clique2);

				Clique c = new Clique(clique2, sim, false);
				int key = c.getClique().size();

				if (iso.containsKey(key)) {
					ArrayList<Clique> list = iso.get(key);

					if (!list.contains(c)) {
						list.add(c);
					}

				} else {
					ArrayList<Clique> list = new ArrayList<Clique>();
					list.add(c);
					iso.put(key, list);
				}
			} else {
				// do nothing
			}

			while (!candidates.isEmpty()) {
				v = candidates.first();
				candidates.remove(v);
				TreeSet<MappedVertex> tempCandidates = new TreeSet<MappedVertex>(
						new MappedVertexComparator());
				TreeSet<MappedVertex> tempVisited = new TreeSet<MappedVertex>(
						new MappedVertexComparator());
				if (logger.isDebugEnabled()) {
					System.out.println("mappedVertex: " + v);
					System.out.println("adjacent for mappedVertex: "
							+ adjacentMap.get(v));
				}
				// System.out.println("mappedVertex: " + v);
				// System.out.println("candiSet1: " + candidates);
				for (MappedVertex w : adjacentMap.get(v)) {// candidates
					if (candidates.contains(w)) {
						tempCandidates.add(w);
						// System.out.println("yes: " + w);
					} else {
						// System.out.println("no: " + w);
					}
					if (visited.contains(w))
						tempVisited.add(w);
				}
				// System.out.println("candiSet2: " + tempCandidates);
				clique.add(v);
				if (logger.isDebugEnabled()) {
					System.out.println("clique: " + clique);
					System.out.println("new candidates: " + tempCandidates);
				}
				nextMaximalClique(tempCandidates, tempVisited, clique, shortest);
				clique.remove(v);
				visited.add(v);
			}
		}
	}

	public void findDisjointCliques(ArrayList<Clique> resultCliques) {
		// get list of largest cliques
		ArrayList<Clique> cliques = iso.get(iso.lastKey());
		// sort in descending order of the clique's similarity
		Collections.sort(cliques);
		// get biggest clique
		Clique max = cliques.get(0);
		
		logger.debug("Max clique found " + max.getClique().toString());

		resultCliques.add(max);

		// store nodes of clique
		TreeSet<IGraphNode> nodes = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		for (MappedVertex mv : max.getClique()) {
			if (graphToCover.equals("smaller")) {
				if (graphAccessor1.size() < graphAccessor2.size()) {
					nodes.add(mv.getLeftNode());
				} else {
					nodes.add(mv.getRightNode());
				}
			} else {
				if (graphAccessor1.size() < graphAccessor2.size()) {
					nodes.add(mv.getRightNode());
				} else {
					nodes.add(mv.getLeftNode());
				}
			}
		}

		// remove all other cliques having at least one same left (right) node
		Set<Entry<Integer, ArrayList<Clique>>> entries = iso.entrySet();
		Iterator<Entry<Integer, ArrayList<Clique>>> it = entries.iterator();
		// iterator over the keys, i.e., the size of the cliques
		while (it.hasNext()) {
			Entry<Integer, ArrayList<Clique>> entry = it.next();
			int key = entry.getKey();
			ArrayList<Clique> list = entry.getValue();

			// iterator over cliques of that size
			Iterator<Clique> iter = list.iterator();
			while (iter.hasNext()) {
				Clique c = iter.next();

				// iterate over the nodes of the clique
				for (MappedVertex mv : c.getClique()) {
					if (graphToCover.equals("smaller")) {
						if (graphAccessor1.size() < graphAccessor2.size()) {
							if (nodes.contains(mv.getLeftNode())) {
								iter.remove();
								break;
							}
						} else {
							if (nodes.contains(mv.getRightNode())) {
								iter.remove();
								break;
							}
						}
					} else {
						if (graphAccessor1.size() < graphAccessor2.size()) {
							if (nodes.contains(mv.getRightNode())) {
								iter.remove();
								break;
							}
						} else {
							if (nodes.contains(mv.getLeftNode())) {
								iter.remove();
								break;
							}
						}
					}
				}

			}

			if (list.isEmpty()) {
				it.remove();
			}

		}

		if (!iso.isEmpty()) {
			findDisjointCliques(resultCliques);
		}

	}

	public void findBothDisjointCliques(ArrayList<Clique> resultCliques) {
		// get list of largest cliques
		ArrayList<Clique> cliques = iso.get(iso.lastKey());
		// sort in descending order of the clique's similarity
		Collections.sort(cliques);
		// get biggest clique
		Clique max = cliques.get(0);
		logger.debug("Max clique found " + max.getClique().toString());

		resultCliques.add(max);

		// store nodes of clique
		TreeSet<IGraphNode> leftNodes = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		TreeSet<IGraphNode> rightNodes = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());

		for (MappedVertex mv : max.getClique()) {
			leftNodes.add(mv.getLeftNode());
			rightNodes.add(mv.getRightNode());
		}

		// remove all other cliques having at least one same left node
		Set<Entry<Integer, ArrayList<Clique>>> entries = iso.entrySet();
		Iterator<Entry<Integer, ArrayList<Clique>>> it = entries.iterator();
		// iterator over the keys, i.e., the size of the cliques
		while (it.hasNext()) {
			Entry<Integer, ArrayList<Clique>> entry = it.next();
			int key = entry.getKey();
			ArrayList<Clique> list = entry.getValue();

			// iterator over cliques of that size
			Iterator<Clique> iter = list.iterator();
			while (iter.hasNext()) {
				Clique c = iter.next();

				// iterate over the nodes of the clique
				for (MappedVertex mv : c.getClique()) {
					if (leftNodes.contains(mv.getLeftNode())) {
						iter.remove();
						break;
					} else if (rightNodes.contains(mv.getRightNode())) {
						iter.remove();
						break;
					}

				}

			}

			if (list.isEmpty()) {
				it.remove();
			}

		}

		if (!iso.isEmpty()) {
			findBothDisjointCliques(resultCliques);
		}

	}

	public TreeMap<Integer, ArrayList<Clique>> getAllIsomorphisms() {
		return iso;
	}

	public ArrayList<Clique> getCovering() {
		return covering;
	}

	public boolean calculate() {
		if (super.calculate()) {
			setCalculated(true);

			covering = new ArrayList<Clique>();
			if (bothDisjointCoverage) {
				findBothDisjointCliques(covering);
			} else {
				findDisjointCliques(covering);
			}

			double numCliques = covering.size();
			double sim = 0d;

			if (graphToCover.equals("larger")) {
				if (graphAccessor1.size() >= graphAccessor2.size()) {
					sim = (-numCliques / graphAccessor1.size()) + 1d;
				} else {
					sim = (-numCliques / graphAccessor2.size()) + 1d;
				}
			} else {
				if (graphAccessor1.size() <= graphAccessor2.size()) {
					sim = (-numCliques / graphAccessor1.size()) + 1d;
				} else {
					sim = (-numCliques / graphAccessor2.size()) + 1d;
				}
			}

			similarity = new Double(sim);

			setCalculated(true);
			return true;
		} else {
			setCalculated(false);
			return false;
		}
	}

	/**
	 * Calculates the similarity for a clique. The similarity consists of only a
	 * label component. This label component is calculated from the number of
	 * MappedVertices having equal labels divided by the total number of
	 * MappedVertices in the clique (The clique must contain all the nodes in
	 * both graphs. Only then the graphs are isomorphic).
	 * 
	 * @param clique
	 *            the clique to measure its similarity
	 */
	private double getCliqueSimilarity(TreeSet<MappedVertex> clique) {
		logger.debug("clique: " + clique);
		double labelSimilarity = 0.0, structureSimilarity, overallSimilarity;

		for (MappedVertex m : clique) {
			labelSimilarity = labelSimilarity + m.calculateLabelSimilarity();
		}

		labelSimilarity = labelSimilarity / graphsSize;
		structureSimilarity = clique.size() / graphsSize;
		overallSimilarity = (labelWeight * labelSimilarity)
				+ (structureWeight * structureSimilarity);

		return overallSimilarity;
	}

}
