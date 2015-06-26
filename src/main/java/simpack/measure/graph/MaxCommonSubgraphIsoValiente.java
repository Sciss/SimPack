/*
 * $Id: MaxCommonSubgraphIsoValiente.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.api.impl.AbstractGraphSimilarityMeasure;
import simpack.util.graph.MappedVertex;
import simpack.util.graph.comparator.MappedVertexComparator;

/**
 * This class implements Valiente's maximum common subgraph algorithm as
 * described in the book "Algorithms on Trees and Graphs", Springer 2002, ISBN
 * 3-540-43550-6. The algorithm finds maximal cliques which not necessarily need
 * to be fully connected. The similarity between two graphs is computed based on
 * the maximum common clique of both graphs.
 * 
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class MaxCommonSubgraphIsoValiente extends AbstractGraphSimilarityMeasure {

	public static Logger logger = LogManager
			.getLogger(MaxCommonSubgraphIsoValiente.class);

	public static String DEFAULT_NODE_LABEL_SIMILARITY_MEASURE = "Levenshtein";

	public static int DEFAULT_MIN_CLIQUE_SIZE = 2;

	public static double DEFAULT_STRUCTURE_WEIGHT = 0.5d;

	public static double DEFAULT_LABEL_WEIGHT = 0.5d;

	public static boolean NODE_GROUPING = false;

	public static String DEFAULT_DENOMINATOR = "average";

	protected int minCliqueSize = DEFAULT_MIN_CLIQUE_SIZE;

	protected double structureWeight = DEFAULT_STRUCTURE_WEIGHT;

	protected double labelWeight = DEFAULT_LABEL_WEIGHT;

	protected IGraphAccessor graphAccessor1, graphAccessor2;

	protected double graphsSize = 0d;

	protected int countCliq = 0, countLoop = 0, countMV = 0;

	protected TreeSet<IGraphNode> nodeSet1 = new TreeSet<IGraphNode>();

	protected TreeSet<IGraphNode> nodeSet2 = new TreeSet<IGraphNode>();

	protected ArrayList<MappedVertex> mappedVertexList = new ArrayList<MappedVertex>();

	protected TreeMap<MappedVertex, TreeSet<MappedVertex>> adjacentMap = new TreeMap<MappedVertex, TreeSet<MappedVertex>>(
			new MappedVertexComparator());

	protected TreeSet<String> cliqueList = new TreeSet<String>();

	protected double maxSimilarity = 0.0, tmpMaxSimilarity = 0.0;

	protected int maxVertex = 0, tmpMaxVertex = 0;

	protected String maxVertexPathWithSimNumber,
			maxSimilarityPathWithSimNumber;

	protected long startMillis;

	protected Timestamp ts;

	protected long millis;

	protected Calendar eDate;

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given accessors.
	 * 
	 * @param graphAccessor1
	 *            first graph accessor
	 * @param graphAccessor2
	 *            second graph accessor
	 */
	public MaxCommonSubgraphIsoValiente(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2) {
		this(graphAccessor1, graphAccessor2,
				MaxCommonSubgraphIsoValiente.DEFAULT_MIN_CLIQUE_SIZE,
				MaxCommonSubgraphIsoValiente.DEFAULT_STRUCTURE_WEIGHT,
				MaxCommonSubgraphIsoValiente.DEFAULT_LABEL_WEIGHT,
				MaxCommonSubgraphIsoValiente.DEFAULT_DENOMINATOR);
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
	 */
	public MaxCommonSubgraphIsoValiente(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2, int minCliqueSize,
			double structureWeight, double labelWeight, String denominator) {
		this.graphAccessor1 = graphAccessor1;
		this.graphAccessor2 = graphAccessor2;
		this.minCliqueSize = minCliqueSize;
		this.structureWeight = structureWeight;
		this.labelWeight = labelWeight;

		if (denominator.equals("first")) {
			graphsSize = graphAccessor1.getNodeSet().size();
		} else if (denominator.equals("small")) {
			if (graphAccessor1.getNodeSet().size() > graphAccessor2
					.getNodeSet().size()) {
				graphsSize = graphAccessor2.getNodeSet().size();
			} else {
				graphsSize = graphAccessor1.getNodeSet().size();
			}
		} else if (denominator.equals("big")) {
			if (graphAccessor1.getNodeSet().size() > graphAccessor2
					.getNodeSet().size()) {
				graphsSize = graphAccessor1.getNodeSet().size();
			} else {
				graphsSize = graphAccessor2.getNodeSet().size();
			}
		} else if (denominator.equals("average")) {
			graphsSize = (graphAccessor1.getNodeSet().size() + graphAccessor2
					.getNodeSet().size()) / 2d;
		} else {
			logger
					.debug("valid denominators are \"first\", \"small\", \"big\" and \"average\". default \"average\" is taken");
			graphsSize = (graphAccessor1.getNodeSet().size() + graphAccessor2
					.getNodeSet().size()) / 2d;
		}
	}

	/**
	 * Calculates the maximal common graph isomorphism.
	 * 
	 * @return true, if able to be calculated, false, else
	 */
	public boolean calculate() {
		setCalculated(false);
		TreeSet<MappedVertex> candidates = new TreeSet<MappedVertex>(
				new MappedVertexComparator());
		TreeSet<MappedVertex> clique = new TreeSet<MappedVertex>(
				new MappedVertexComparator());
		TreeSet<MappedVertex> visited = new TreeSet<MappedVertex>(
				new MappedVertexComparator());

		nodeSet1 = graphAccessor1.getNodeSet();
		nodeSet2 = graphAccessor2.getNodeSet();
		adjacentMap = map();

		if (logger.isDebugEnabled()) {
			for (IGraphNode n : graphAccessor1.getNodeSet()) {
				System.out.println("nodeG1: " + n);
				System.out.println("pre: " + n.getPredecessorSet());
				System.out.println("suc: " + n.getSuccessorSet());
			}
			for (IGraphNode n : graphAccessor2.getNodeSet()) {
				System.out.println("nodeG2: " + n);
				System.out.println("pre: " + n.getPredecessorSet());
				System.out.println("suc: " + n.getSuccessorSet());
			}
			Set<MappedVertex> set;
			set = adjacentMap.keySet();
			for (MappedVertex mv : set) {
				System.out.println(mv);
				if (mv.isGroup())
					System.out.println("grpsiz " + mv.getGroupSize()
							+ "  grpsim " + mv.getGroupSimilarity() + "  Map: "
							+ adjacentMap.get(mv));
				else
					System.out.println("labelsim " + mv.getLabelSimilarity()
							+ "  Map: " + adjacentMap.get(mv));
				for (MappedVertex n : adjacentMap.get(mv)) {
					System.out.println(n + " sim " + n.getLabelSimilarity());
				}
			}
		}

		for (MappedVertex m : mappedVertexList) {
			countMV++;
			if (Math.IEEEremainder(countMV, 10000) == 0) {
				eDate = Calendar.getInstance();
				millis = eDate.getTime().getTime();
				ts = new Timestamp(millis);
				System.out.println(countMV + ") " + m + " " + ts + " = "
						+ (millis - startMillis) / 1000 + " sec");
			}
			candidates.add(m);
			// nextMaximalClique(candidates, impossibleLeft, impossibleRight,
			// clique, shortest);
		}
		nextMaximalClique(candidates, visited, clique, minCliqueSize);
		// System.out.println("maxSimilarity: " +
		// maxSimilarityPathWithSimNumber);
		// System.out.println("maxVertex: " + maxVertexPathWithSimNumber);

		similarity = maxSimilarity;
		setCalculated(true);
		return true;
	}

	public TreeMap<MappedVertex, TreeSet<MappedVertex>> map() {
		for (IGraphNode node1 : nodeSet1) {
			for (IGraphNode node2 : nodeSet2) {
				MappedVertex mvOri = new MappedVertex(node1, node2);
				TreeSet<MappedVertex> adjacentSet = new TreeSet<MappedVertex>(
						new MappedVertexComparator());
				for (IGraphNode cand1 : nodeSet1) {
					for (IGraphNode cand2 : nodeSet2) {
						MappedVertex mvCand = new MappedVertex(cand1, cand2);
						if (mvOri.smaller(mvCand) < 0
								&& !node1.equals(cand1)
								&& !node2.equals(cand2)
								&& ((node1.getPredecessorSet().contains(cand1) && node2
										.getPredecessorSet().contains(cand2))
										|| (node1.getSuccessorSet().contains(
												cand1) && node2
												.getSuccessorSet().contains(
														cand2)) || (!node1
										.getAdjacentSet().contains(cand1) && !node2
										.getAdjacentSet().contains(cand2)))) {
							adjacentSet.add(mvCand);
						}

					}
				}
				mappedVertexList.add(mvOri);
				adjacentMap.put(mvOri, adjacentSet);
			}
		}
		return adjacentMap;
	}

	/**
	 * Algorithm Calculates for a certain clique all MappedVertices with which
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
		if (candidates.isEmpty()) {
			if (visited.isEmpty()) {
				if (clique2.size() >= shortest) {
					countCliq++;
					if (Math.IEEEremainder(countCliq, 200000) == 0) {
						logger.debug("Cliq: " + countCliq + " " + clique2);
					}
					checkIfMaxClique(clique2);
				}
			}
		} else {
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

	/**
	 * Calculates the similarity for a clique. The similarity consists of only a
	 * label component. This label component is calculated from the number of
	 * MappedVertices having equal labels divided by the total number of
	 * MappedVertices in the clique (The clique must contain all the nodes in
	 * both graphs. Only then the graphs are isomorphic).
	 * 
	 * @param clique
	 */
	public void checkIfMaxClique(TreeSet<MappedVertex> clique) {
		logger.debug("clique: " + clique);
		int tempInt;
		double labelSimilarity = 0.0, structureSimilarity, overallSimilarity;
		String pathWithSimNumber, path;
		pathWithSimNumber = "";

		for (MappedVertex m : clique) {
			labelSimilarity = labelSimilarity + m.calculateLabelSimilarity();
			if (pathWithSimNumber.equals("")) {
				pathWithSimNumber = pathWithSimNumber + m.toString();
			} else {
				pathWithSimNumber = pathWithSimNumber + ", " + m.toString();
			}
		}

		labelSimilarity = labelSimilarity / graphsSize;
		structureSimilarity = clique.size() / graphsSize;
		overallSimilarity = (labelWeight * labelSimilarity)
				+ (structureWeight * structureSimilarity);

		if (logger.isDebugEnabled()) {
			System.out.println("clique: " + pathWithSimNumber);
			System.out.println("labelSimilarity: " + labelSimilarity);
			System.out.println("structureSimilarity: " + structureSimilarity);
			System.out.println("overallSimilarity: " + overallSimilarity);
		}
		path = pathWithSimNumber;
		if (overallSimilarity == 1) {
			pathWithSimNumber = pathWithSimNumber + "; 1";
		} else {
			tempInt = (int) (overallSimilarity * 100);
			Integer conversion = new Integer(tempInt);
			pathWithSimNumber = pathWithSimNumber + "; 0."
					+ conversion.toString();
		}
		checkMax(clique.size(), overallSimilarity, path, pathWithSimNumber);

	}

	/**
	 * Checks whether the calculated similarity is maximal.
	 * 
	 * @param similarity
	 * @param clique
	 */
	public void checkMax(int numVertex, double similarity, String clique,
			String cliqueSim) {
		if (similarity >= maxSimilarity) {
			if (similarity > maxSimilarity)
				cliqueList.clear();
			cliqueList.add(clique);
		}
		if (numVertex > maxVertex
				|| (numVertex == maxVertex && similarity > tmpMaxSimilarity)) {
			maxVertexPathWithSimNumber = cliqueSim;
			logger.debug("maxVertexPathWithSimNumber: "
					+ maxVertexPathWithSimNumber);
			maxVertex = numVertex;
			tmpMaxSimilarity = Math.max(tmpMaxSimilarity, similarity);
		}
		if (similarity > maxSimilarity
				|| (similarity == maxSimilarity && numVertex > tmpMaxVertex)) {
			maxSimilarityPathWithSimNumber = cliqueSim;
			logger.debug("maxSimilarityPathWithSimNumber: "
					+ maxSimilarityPathWithSimNumber);
			maxSimilarity = similarity;
			tmpMaxVertex = Math.max(tmpMaxVertex, numVertex);
		}
	}

	/**
	 * Returns all the maximum cliques.
	 * 
	 * @return cliqueList
	 */
	public TreeSet<String> getCliqueList() {
		return cliqueList;
	}

}
