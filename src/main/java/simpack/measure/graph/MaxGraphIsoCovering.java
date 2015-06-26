/*
 * $Id: MaxGraphIsoCovering.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.util.DeepCopy;
import simpack.util.graph.Clique;
import simpack.util.graph.MappedVertex;
import simpack.util.graph.comparator.MappedVertexComparator;
import simpack.util.graph.comparator.NamedGraphNodeComparator;

/**
 * @author Christoph Kiefer
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class MaxGraphIsoCovering extends SubgraphIsomorphism {

	public static Logger logger = LogManager.getLogger(MaxGraphIsoCovering.class);

	public static String DEFAULT_COVERING = "smaller";

	private String graphToCover = DEFAULT_COVERING;

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
	public MaxGraphIsoCovering(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2) {

		super(graphAccessor1, graphAccessor2,
				DEFAULT_NODE_LABEL_SIMILARITY_MEASURE, DEFAULT_MIN_CLIQUE_SIZE,
				DEFAULT_LABEL_WEIGHT, DEFAULT_STRUCTURE_WEIGHT,
				DEFAULT_DENOMINATOR, NODE_GROUPING);

		iso = new TreeMap<Integer, ArrayList<Clique>>();
	}

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given accessors, the minimum of MappedVertices a maximal
	 * clique should contain, the weight of the structural component (between 0
	 * and 1) and the basis of the comparison (>0 = first graph, <0 = second
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
	 * @param groupNodes
	 *            if true, grouping leave nodes
	 */
	public MaxGraphIsoCovering(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2, String similarityMeasure,
			int minCliqueSize, double structureWeight, double labelWeight,
			String denominator, boolean groupNodes, String graphToCover) {

		super(graphAccessor1, graphAccessor2, similarityMeasure, minCliqueSize,
				structureWeight, labelWeight, denominator, groupNodes);

		this.graphToCover = graphToCover;
		iso = new TreeMap<Integer, ArrayList<Clique>>();

	}

	/**
	 * Algorithm calculates for a certain clique all possible adjacent
	 * MappedVertices to enhance the clique. This method is invoked recursively
	 * for each additional node in the clique.
	 * 
	 * @param candidates
	 * @param impossibleLeft
	 * @param impossibleRight
	 * @param clique
	 */
	public void nextMaximalClique(TreeSet<MappedVertex> candidates,
			TreeSet<IGraphNode> impossibleLeft,
			TreeSet<IGraphNode> impossibleRight, TreeSet<MappedVertex> clique,
			int shortest) {

		countLoop++;
		if (Math.IEEEremainder(countLoop, 2000000) == 0) {
			logger.debug("countLoop: " + countLoop);
		}

		MappedVertex v;
		int cliqueSize = 0;
		TreeSet<MappedVertex> clique2 = new TreeSet<MappedVertex>(
				new MappedVertexComparator());

		for (MappedVertex mv : clique) {
			// TODO: correct???
			// clique2.add(mv);
			MappedVertex copy = (MappedVertex) DeepCopy.copy(mv);
			clique2.add(copy);
		}

		for (MappedVertex mv : clique2) {
			if (mv.isGroup()) {
				cliqueSize = cliqueSize + mv.getGroupSize();
			} else {
				cliqueSize++;
			}
		}

		// no more candidates == clique is maximal
		if (candidates.isEmpty()) {

			if (cliqueSize >= shortest) {
				countCliq++;
				if (Math.IEEEremainder(countCliq, 200000) == 0) {
					logger.debug("Cliq: " + countCliq + " " + clique2);
				}

				double sim = getCliqueSimilarity(clique2);

				Clique c = new Clique(clique2, sim, true);
				int key = cliqueSize;

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

		} else {

			if (clique2.size() >= shortest) {

				double sim = getCliqueSimilarity(clique2);

				Clique c = new Clique(clique2, sim, false);
				int key = cliqueSize;

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
				node1 = v.getLeftNode();
				node2 = v.getRightNode();
				TreeSet<MappedVertex> tempCandidates = new TreeSet<MappedVertex>(
						new MappedVertexComparator());
				TreeSet<IGraphNode> tempImpossibleLeft = new TreeSet<IGraphNode>(
						new NamedGraphNodeComparator());
				TreeSet<IGraphNode> tempImpossibleRight = new TreeSet<IGraphNode>(
						new NamedGraphNodeComparator());

				if (logger.isDebugEnabled()) {
					System.out.println("mappedVertex: " + v);
					System.out
							.println("impossible before l: " + impossibleLeft);
					System.out.println("impossible before r: "
							+ impossibleRight);
					System.out.println("adjacent for mappedVertex: "
							+ adjacentMap.get(v));
				}

				for (MappedVertex w : adjacentMap.get(v)) {// new candidates
					if (!impossibleLeft.contains(w.getLeftNode())
							&& !impossibleRight.contains(w.getRightNode())
							&& !clique.contains(w)) {
						tempCandidates.add(w);
					} else if (logger.isDebugEnabled()) {
						System.out.println("new candidate not added: " + w);
						System.out.println(!impossibleLeft.contains(node1)
								+ " " + !impossibleRight.contains(node2) + " "
								+ !clique.contains(w));
					}
				}

				logger.debug("only new candidates: " + tempCandidates);
				for (MappedVertex w : candidates) {// old candidates
					if (logger.isDebugEnabled()) {
						System.out.println("old candidate: " + w);
						System.out.println(adjacentMap.get(v).contains(w)
								+ " "
								+ !w.getLeftNode().equals(node1)
								+ " "
								+ !w.getRightNode().equals(node2)
								+ " "
								+ !node1.getAdjacentSet().contains(
										w.getLeftNode())
								+ " "
								+ !node2.getAdjacentSet().contains(
										w.getRightNode()));
					}
					if (adjacentMap.get(v).contains(w)
							|| !w.getLeftNode().equals(node1)
							&& !w.getRightNode().equals(node2)
							&& !node1.getAdjacentSet()
									.contains(w.getLeftNode())
							&& !node2.getAdjacentSet().contains(
									w.getRightNode())) {
						tempCandidates.add(w);

					}
				}
				for (IGraphNode node : impossibleLeft)
					tempImpossibleLeft.add(node);
				for (IGraphNode node : impossibleRight)
					tempImpossibleRight.add(node);
				for (IGraphNode node : node1.getAdjacentSet())
					tempImpossibleLeft.add(node);
				for (IGraphNode node : node2.getAdjacentSet())
					tempImpossibleRight.add(node);
				tempImpossibleLeft.add(node1);
				tempImpossibleRight.add(node2);
				clique.add(v);

				if (logger.isDebugEnabled()) {
					System.out.println("clique: " + clique);
					System.out.println("all candidates: " + tempCandidates);
					System.out.println("impossible after l: "
							+ tempImpossibleLeft);
					System.out.println("impossible after r: "
							+ tempImpossibleRight);
				}

				nextMaximalClique(tempCandidates, tempImpossibleLeft,
						tempImpossibleRight, clique, shortest);
				clique.remove(v);
			}
		}
	}

	public void findDisjointCliques(ArrayList<Clique> resultCliques) {
		// get list of largest cliques
		ArrayList<Clique> cliques = iso.get(iso.lastKey());
		// sort in descending order of the clique's similarity
		Collections.sort(cliques);
		// get largest clique with highest similarity
		Clique max = cliques.get(0);

		logger.debug("Max clique found " + max.getClique().toString());

		resultCliques.add(max);

		// store nodes of clique
		TreeSet<IGraphNode> nodes = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		for (MappedVertex mv : max.getClique()) {
			// cover the smaller graph
			if (graphToCover.equals("smaller")) {
				if (graphAccessor1.size() < graphAccessor2.size()) {
					// handle groups
					if (mv.isGroup()) {
						TreeSet<MappedVertex> group = mv.getGroupMembers();
						for (MappedVertex v : group) {
							nodes.add(v.getLeftNode());
						}
					} else {
						nodes.add(mv.getLeftNode());
					}
				} else {
					if (mv.isGroup()) {
						TreeSet<MappedVertex> group = mv.getGroupMembers();
						for (MappedVertex v : group) {
							nodes.add(v.getRightNode());
						}
					} else {
						nodes.add(mv.getRightNode());
					}
				}
			} else {
				if (graphAccessor1.size() < graphAccessor2.size()) {
					if (mv.isGroup()) {
						System.out
								.println("Adding group nodes" + mv.toString());
						TreeSet<MappedVertex> group = mv.getGroupMembers();
						for (MappedVertex v : group) {
							nodes.add(v.getRightNode());
						}
					} else {
						nodes.add(mv.getRightNode());
					}
				} else {
					if (mv.isGroup()) {
						System.out
								.println("Adding group nodes" + mv.toString());
						TreeSet<MappedVertex> group = mv.getGroupMembers();
						for (MappedVertex v : group) {
							nodes.add(v.getLeftNode());
						}
					} else {
						nodes.add(mv.getLeftNode());
					}
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
							// handle groups
							if (mv.isGroup()) {
								TreeSet<MappedVertex> group = mv
										.getGroupMembers();
								boolean found = false;
								for (MappedVertex v : group) {
									if (nodes.contains(v.getLeftNode())) {
										found = true;
										break;
									}
								}
								if (found) {
									iter.remove();
									break;
								}
							} else {
								if (nodes.contains(mv.getLeftNode())) {
									iter.remove();
									break;
								}
							}
						} else {
							if (mv.isGroup()) {
								TreeSet<MappedVertex> group = mv
										.getGroupMembers();
								boolean found = false;
								for (MappedVertex v : group) {
									if (nodes.contains(v.getRightNode())) {
										found = true;
										break;
									}
								}
								if (found) {
									iter.remove();
									break;
								}
							} else {
								if (nodes.contains(mv.getRightNode())) {
									iter.remove();
									break;
								}
							}
						}
					} else {
						if (graphAccessor1.size() < graphAccessor2.size()) {
							if (mv.isGroup()) {
								TreeSet<MappedVertex> group = mv
										.getGroupMembers();
								boolean found = false;
								for (MappedVertex v : group) {
									if (nodes.contains(v.getRightNode())) {
										found = true;
										System.out.println("Group found: "
												+ mv.toString());
										break;
									}
								}
								if (found) {
									System.out.println("Removing group node: "
											+ mv.toString());
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
							if (mv.isGroup()) {
								TreeSet<MappedVertex> group = mv
										.getGroupMembers();
								boolean found = false;
								for (MappedVertex v : group) {
									if (nodes.contains(v.getLeftNode())) {
										found = true;
										System.out.println("Group found: "
												+ mv.toString());
										break;
									}
								}
								if (found) {
									System.out.println("Removing group node: "
											+ mv.toString());
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

			}

			if (list.isEmpty()) {
				it.remove();
			}

		}

		if (!iso.isEmpty()) {
			findDisjointCliques(resultCliques);
		}

	}

	public boolean calculate() {
		if (super.calculate()) {
			setCalculated(false);

			// check for empty graphs
			if (graphAccessor1.getNodeSet().isEmpty()
					|| graphAccessor2.getNodeSet().isEmpty()) {
				similarity = new Double(0d);
				setCalculated(true);
				return true;
			}

			// System.out.println(groupNodes);
			if (groupNodes) {
				// System.out.println("node grouping active");
				TreeMap<Integer, ArrayList<Clique>> map = getAllIsomorphisms();

				// Set<Entry<Integer, ArrayList<Clique>>> entries =
				// map.entrySet();
				// Iterator<Entry<Integer, ArrayList<Clique>>> it = entries
				// .iterator();
				// while (it.hasNext()) {
				// Entry<Integer, ArrayList<Clique>> entry = it.next();
				// int key = entry.getKey();
				// ArrayList<Clique> list = entry.getValue();
				//
				// System.out.println("### Cliques of size " + key + " ###");
				//
				// for (Clique c : list) {
				// System.out.println(c.getClique().toString() + " "
				// + c.getSimilarity());
				// }
				// }

				ArrayList<Clique> one = map.get(1);
				for (IGraphNode n1 : graphAccessor1.getNodeSet()) {
					if (n1.getIsGroup()) {
						continue;
					}
					for (IGraphNode n2 : graphAccessor2.getNodeSet()) {
						if (n2.getIsGroup()) {
							continue;
						}
						MappedVertex mv = new MappedVertex(n1, n2);
						TreeSet<MappedVertex> set = new TreeSet<MappedVertex>();
						set.add(mv);
						double sim = (mv.calculateLabelSimilarity() + 1d)
								/ getGraphsSize() * 0.5d;
						Clique c = new Clique(set, sim, false);
						if (!one.contains(c)) {
							one.add(c);
							logger.debug("Adding clique " + c.toString());
						}
					}
				}
			}

			covering = new ArrayList<Clique>();
			findDisjointCliques(covering);

			double numCliques = covering.size();

			// System.out.println("disjoint cliques = " + numCliques);

			double sim;
			if (graphToCover.equals("larger")) {
				if (graphAccessor1.size() >= graphAccessor2.size()) {
					sim = new Double((-numCliques / graphAccessor1.size()) + 1d);
				} else {
					sim = new Double((-numCliques / graphAccessor2.size()) + 1d);
				}
			} else {
				if (graphAccessor1.size() <= graphAccessor2.size()) {
					sim = new Double((-numCliques / graphAccessor1.size()) + 1d);
				} else {
					sim = new Double((-numCliques / graphAccessor2.size()) + 1d);
				}
			}

			similarity = new Double(sim);
			// System.out.println("sim calculated! " + sim);

			setCalculated(true);
			return true;

		} else {
			setCalculated(false);
			logger.error("Error");
			return false;
		}
	}

	public TreeMap<Integer, ArrayList<Clique>> getAllIsomorphisms() {
		return iso;
	}

	public ArrayList<Clique> getCovering() {
		return covering;
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
		int groupVertexSize = 0;
		double labelSimilarity = 0d;
		double structureSimilarity = 0d;
		double overallSimilarity = 0d;

		for (MappedVertex m : clique) {
			if (m.isGroup()) { // two mapped group nodes
				labelSimilarity = labelSimilarity + m.getGroupSimilarity();
				groupVertexSize = groupVertexSize + m.getGroupSize() - 1;
			} else {
				labelSimilarity = labelSimilarity + m.getLabelSimilarity();
			}

		}
		// labelSimilarity = labelSimilarity/(clique.size()+groupVertexSize);
		labelSimilarity = labelSimilarity / graphsSize;
		structureSimilarity = ((clique.size() + groupVertexSize)) / graphsSize;
		// overallSimilarity = structureSimilarity;
		overallSimilarity = (labelWeight * labelSimilarity)
				+ (structureWeight * structureSimilarity);

		return overallSimilarity;
	}
}