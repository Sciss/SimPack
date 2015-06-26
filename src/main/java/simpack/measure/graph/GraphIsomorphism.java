/*
 * $Id: GraphIsomorphism.java 757 2008-04-17 17:42:53Z kiefer $
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
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import simpack.accessor.string.StringAccessor;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.api.impl.AbstractGraphSimilarityMeasure;
import simpack.measure.sequence.Levenshtein;
import simpack.util.graph.GroupNode;
import simpack.util.graph.MappedVertex;
import simpack.util.graph.comparator.MappedVertexComparator;
import simpack.util.graph.comparator.NamedGraphNodeComparator;

/**
 * This class implements Valiente's graph isomorphism algorithm as described in
 * the book "Algorithms on Trees and Graphs", Springer 2002. Additionally the
 * algorithm implements optimizations to reduce complexity, such as grouping of
 * leaf nodes and a reduction of entry points for the algorithm.
 * 
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class GraphIsomorphism extends AbstractGraphSimilarityMeasure {

	public static Logger logger = LogManager.getLogger(GraphIsomorphism.class);

	public static boolean NODE_GROUPING = false;

	public static String DEFAULT_NODE_LABEL_SIMILARITY_MEASURE = "Levenshtein";

	private boolean groupNodes = NODE_GROUPING;

	private String similarityMeasure = DEFAULT_NODE_LABEL_SIMILARITY_MEASURE;

	private TreeSet<IGraphNode> nodeSet1 = new TreeSet<IGraphNode>();

	private TreeSet<IGraphNode> nodeSet2 = new TreeSet<IGraphNode>();

	private IGraphNode node1, node2;

	private TreeMap<String, Integer> groupDegreeMap1 = new TreeMap<String, Integer>();

	private TreeMap<String, Integer> groupDegreeMap2 = new TreeMap<String, Integer>();

	private TreeMap<MappedVertex, TreeSet<MappedVertex>> adjacentMap = new TreeMap<MappedVertex, TreeSet<MappedVertex>>(
			new MappedVertexComparator());

	private ArrayList<MappedVertex> mappedVertexList = new ArrayList<MappedVertex>();

	private ArrayList<MappedVertex> startCandidateList = new ArrayList<MappedVertex>();

	private TreeSet<String> cliqueList = new TreeSet<String>();

	private IGraphAccessor graphAccessor1 = null, graphAccessor2 = null;

	private int graphSize = 0;

	private double maxSimilarity = 0.0;

	private String maxSimilarityPathWithSimNumber;

	private long startMillis;

	private int countCliq = 0, countLoop = 0, countMV = 0,
			countLoopAdjacentMap = 0;

	private Timestamp ts;

	private long millis;

	private Calendar eDate;

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
	public GraphIsomorphism(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2) {
		this(graphAccessor1, graphAccessor2,
				GraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE,
				GraphIsomorphism.NODE_GROUPING);
	}

	/**
	 * Constructor.
	 * <p>
	 * Constructor with given accessors.
	 * 
	 * @param graphAccessor1
	 *            first graph accessor
	 * @param graphAccessor2
	 *            second graph accessor
	 * @param groupNodes
	 *            if true, grouping leave nodes
	 */
	public GraphIsomorphism(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2, String similarityMeasure,
			boolean groupNodes) {
		this.graphAccessor1 = graphAccessor1;
		this.graphAccessor2 = graphAccessor2;
		this.groupNodes = groupNodes;
		this.similarityMeasure = similarityMeasure;
		this.nodeSet1 = graphAccessor1.getNodeSet();
		this.nodeSet2 = graphAccessor2.getNodeSet();
	}

	/**
	 * Calculates the maximal common graph isomorphism.
	 * 
	 * @return true, if able to be calculated, false, else
	 */
	public boolean calculate() {
		setCalculated(false);
		if (getGraphIsomorphism() == 1) {
			graphSize = nodeSet1.size();
			if (groupNodes) {
				setGroups(graphAccessor1, "G1");
				setGroups(graphAccessor2, "G2");
			}
			mapSameDegree();
			TreeSet<MappedVertex> candidates = new TreeSet<MappedVertex>(
					new MappedVertexComparator());
			TreeSet<IGraphNode> impossibleLeft = new TreeSet<IGraphNode>(
					new NamedGraphNodeComparator());
			TreeSet<IGraphNode> impossibleRight = new TreeSet<IGraphNode>(
					new NamedGraphNodeComparator());
			TreeSet<MappedVertex> clique = new TreeSet<MappedVertex>(
					new MappedVertexComparator());
			adjacentMap = getAdjacentMap();
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
					if (mv.isGroup()) {
						System.out.println("grpsiz " + mv.getGroupSize()
								+ "  grpsim " + mv.getGroupSimilarity()
								+ "  Map: " + adjacentMap.get(mv));
					} else {
						System.out.println("labelsim "
								+ mv.getLabelSimilarity() + "  Map: "
								+ adjacentMap.get(mv));
					}
					for (MappedVertex n : adjacentMap.get(mv)) {
						System.out
								.println(n + " sim " + n.getLabelSimilarity());
					}
				}
			}
			for (MappedVertex m : startCandidateList) {
				countMV++;
				if ((Math.IEEEremainder(countMV, 3) == 0)
						&& logger.isDebugEnabled()) {
					eDate = Calendar.getInstance();
					millis = eDate.getTime().getTime();
					ts = new Timestamp(millis);
					System.out.println(countMV + ") " + m + " " + ts + " = "
							+ (millis - startMillis) / 1000 + " sec");
				}
				candidates.add(m);
				nextMaximalClique(candidates, impossibleLeft, impossibleRight,
						clique);
			}
			logger.debug("maxSimilarity: " + maxSimilarityPathWithSimNumber);
			similarity = maxSimilarity;
		} else {
			similarity = 0.0;
		}
		setCalculated(true);
		return true;
	}

	/**
	 * Checks whether the number of vertices having the same number of in- and
	 * out-degrees are the same in both graphs.
	 * 
	 * @return 1, if both graphs are structural isomorph, 0, else.
	 */
	public int getGraphIsomorphism() {
		if (!equalNumberOfVertices())
			return 0;
		else {
			groupDegreeMap1 = groupDegree(nodeSet1);
			groupDegreeMap2 = groupDegree(nodeSet2);
			String s;
			while (!groupDegreeMap1.isEmpty()) {
				s = groupDegreeMap1.firstKey();
				if (!groupDegreeMap2.containsKey(s)
						|| groupDegreeMap1.get(s).intValue() != groupDegreeMap2.get(s).intValue()) {
					return 0;
				}
				groupDegreeMap1.remove(s);
			}
			return 1;
		}
	}

	/**
	 * Compares the number of vertices in both graphs
	 * 
	 * @return true, if number of vertices is the same in both graphs
	 */
	public boolean equalNumberOfVertices() {
		return (nodeSet1.size() == nodeSet2.size());
	}

	/**
	 * Aggregates the vertices having the same number of in- and out-degrees.
	 * 
	 * @param vertexSet
	 * @return groupDegreeMap
	 */
	public TreeMap<String, Integer> groupDegree(TreeSet<IGraphNode> vertexSet) {
		TreeMap<String, Integer> groupDegreeMap = new TreeMap<String, Integer>();
		String conversion;
		int number;
		for (IGraphNode node : vertexSet) {
			conversion = node.getInDegree().toString() + ","
					+ node.getOutDegree().toString();
			if (groupDegreeMap.containsKey(conversion))
				number = groupDegreeMap.get(conversion) + 1;
			else
				number = 1;
			groupDegreeMap.put(conversion, number);
		}
		return groupDegreeMap;
	}

	/**
	 * Creates new group nodes for each graph by aggregating structural
	 * identical nodes. A number of nodes qualify for a certain group if the
	 * share the following properties: 1) A node n1 has more than one successor.
	 * 2) All successor nodes of n1 are leaf nodes. 3) All these successor nodes
	 * have the same predecessor nodes. 4) All these predecessor nodes have only
	 * those successor nodes. then all of those predecessors (n1 is one of them)
	 * have this newly created group node as an additional successor.
	 * 
	 * @param graphAccessor
	 * @param graph
	 */
	public void setGroups(IGraphAccessor graphAccessor, String graph) {
		boolean group;
		Integer groupCount = 0;
		TreeSet<IGraphNode> groupSet = new TreeSet<IGraphNode>();
		for (IGraphNode node : graphAccessor.getNodeSet()) {
			logger.debug("node: " + node);
			group = true;
			if (!node.getHasGroup() && node.getSuccessorSet() != null
					&& node.getSuccessorSet().size() > 1) {
				TreeSet<IGraphNode> successorSet = new TreeSet<IGraphNode>();
				TreeSet<IGraphNode> predecessorSet = new TreeSet<IGraphNode>();
				TreeSet<IGraphNode> memberSet = new TreeSet<IGraphNode>();
				successorSet = node.getSuccessorSet();
				IGraphNode firstNode = successorSet.first();
				predecessorSet = firstNode.getPredecessorSet();
				logger.debug("predecessorSet of 1st successor: "
						+ predecessorSet);
				for (IGraphNode successor : successorSet) {
					if ((successor.getSuccessorSet() == null || successor
							.getSuccessorSet().size() == 0)
							&& successor.getPredecessorSet().size() == predecessorSet
									.size()) {
						for (IGraphNode predecessor : predecessorSet) {
							if (!successor.getPredecessorSet().contains(
									predecessor)) {
								group = false;
							}
						}
					} else
						group = false;
				}
				for (IGraphNode predecessor : predecessorSet) {
					if (predecessor.getSuccessorSet().size() != successorSet
							.size())
						group = false;
				}
				logger.debug("is groupable: " + group);
				if (group) {
					node.setHasGroup();
					groupCount++;
					GroupNode g = new GroupNode(graph.concat("_GROUP_").concat(
							groupCount.toString()));
					groupSet.add(g);
					logger.debug("groupNode: " + g.toString());
					g.setGroupSize(successorSet.size());
					for (IGraphNode member : successorSet) {
						if (!member.getIsGroup())
							memberSet.add(member);
					}
					g.setGroupNodes(memberSet);
					logger.debug("memberSet: " + memberSet);
					for (IGraphNode predecessor : predecessorSet) {
						g.addPredecessor(predecessor);
						predecessor.setHasGroup();
						predecessor.addSuccessor(g);
					}
				}
			}
		}
		for (IGraphNode node : groupSet)
			graphAccessor.addNode(node);
	}

	/**
	 * Maps all vertices in the first graph to vertices in the second graph
	 * having the same number of predecessors and successors and adds these
	 * vertices to the mappedVertexList. Group nodes can only be mapped to other
	 * group nodes and non group nodes can only be mapped to non group nodes.
	 * Two mapped root nodes are added to the startCandidateList (which are then
	 * starting points in the calculator).
	 */
	public void mapSameDegree() {
		for (IGraphNode node1 : nodeSet1) {
			for (IGraphNode node2 : nodeSet2) {
				if (node1.getIsGroup() == node2.getIsGroup()
						&& node1.getGroupSize() == node2.getGroupSize()
						&& node1.getInDegree().intValue() == node2.getInDegree().intValue()
						&& node1.getOutDegree().intValue() == node2.getOutDegree()) {
					MappedVertex mv = new MappedVertex(node1, node2);
					mappedVertexList.add(mv);
					if (node1.getIsGroup()) {
						if (node1.getGroupSize() > node2.getGroupSize())
							groupSimilarity(mv, node2.getGroupNodes(), node1
									.getGroupNodes(), "reverse");
						else
							groupSimilarity(mv, node1.getGroupNodes(), node2
									.getGroupNodes(), "normal");
					} else {
						if (node1.getInDegree() == 0) {
							startCandidateList.add(mv);
						}
						if (similarityMeasure.equals("Levenshtein")) {
							Levenshtein<String> levenshtein = new Levenshtein<String>(
									new StringAccessor(node1.toString()),
									new StringAccessor(node2.toString()));
							mv.setLabelSimilarity(levenshtein.getSimilarity());
						} else if (similarityMeasure.equals("AlwaysTrue")) {
							mv.setLabelSimilarity(1d);
						} else {
							// TODO fix this
							// for the moment just print out an error message
							logger.error("Not yet implemented!");
						}
						// if (node1.toString().equals(node2.toString()))
						// mv.setLabelSimilarity(1.0);
						// else mv.setLabelSimilarity(0.0);
					}
				}
			}
		}
	}

	/**
	 * Assignes to each MappedVertex in the mappedVertexList all its adjacent
	 * mapped vertices.
	 * 
	 * @return adjacentMap
	 */
	public TreeMap<MappedVertex, TreeSet<MappedVertex>> getAdjacentMap() {

		TreeSet<IGraphNode> sourceSet1 = new TreeSet<IGraphNode>();
		TreeSet<IGraphNode> sourceSet2 = new TreeSet<IGraphNode>();
		TreeSet<IGraphNode> targetSet1 = new TreeSet<IGraphNode>();
		TreeSet<IGraphNode> targetSet2 = new TreeSet<IGraphNode>();

		for (MappedVertex mv : mappedVertexList) {
			logger.debug("mappedVertex: " + mv);
			countLoopAdjacentMap++;

			if ((Math.IEEEremainder(countLoopAdjacentMap, 1000) == 0)
					&& (logger.isDebugEnabled())) {
				System.out.println("loop adjacency: " + countLoopAdjacentMap
						+ " mv: " + mv);
			}
			TreeSet<MappedVertex> commonAdjacentSet = new TreeSet<MappedVertex>(
					new MappedVertexComparator());
			IGraphNode node1 = mv.getLeftNode();
			IGraphNode node2 = mv.getRightNode();
			sourceSet1 = node1.getPredecessorSet();
			sourceSet2 = node2.getPredecessorSet();
			targetSet1 = node1.getSuccessorSet();
			targetSet2 = node2.getSuccessorSet();
			if (logger.isDebugEnabled()) {
				System.out.println("nodes: " + node1 + ", " + node2);
				System.out.println("sourceSet1: " + sourceSet1);
				System.out.println("sourceSet2: " + sourceSet2);
				System.out.println("targetSet1: " + targetSet1);
				System.out.println("targetSet2: " + targetSet2);
			}
			if (sourceSet1 != null && sourceSet2 != null) {
				commonAdjacentSet = adjacentNodes(commonAdjacentSet,
						sourceSet1, sourceSet2);
			}
			if (targetSet1 != null && targetSet2 != null) {
				if (node1.getHasGroup() && node2.getHasGroup()) {
					logger.debug("have group!");
					commonAdjacentSet = adjacentGroups(commonAdjacentSet,
							targetSet1, targetSet2);
				} else {
					commonAdjacentSet = adjacentNodes(commonAdjacentSet,
							targetSet1, targetSet2);
				}
			}
			logger.debug("commonAdjacentSet: " + commonAdjacentSet);
			adjacentMap.put(mv, commonAdjacentSet);
			if (logger.isDebugEnabled()) {
				for (MappedVertex m : commonAdjacentSet)
					System.out.println("MV: " + mv + " " + mv.isGroup()
							+ " m: " + m + " " + m.isGroup());
			}
		}
		return adjacentMap;
	}

	/**
	 * Defines the common successors and predecessors for a MappedVertex. The
	 * adjacentSets of both nodes have to be either both source or both target
	 * Sets. These common successors and predecessors are added to the
	 * commonAdjacentSet.
	 * 
	 * @param commonAdjacentSet
	 * @param adjacentSet1
	 * @param adjacentSet2
	 * @return commonAdjacentSet
	 */
	public TreeSet<MappedVertex> adjacentNodes(
			TreeSet<MappedVertex> commonAdjacentSet,
			TreeSet<IGraphNode> adjacentSet1, TreeSet<IGraphNode> adjacentSet2) {
		for (IGraphNode node1 : adjacentSet1) {
			for (IGraphNode node2 : adjacentSet2) {
				if (!node1.getIsGroup() && !node2.getIsGroup()) {
					MappedVertex mv = new MappedVertex(node1, node2);
					for (MappedVertex m : mappedVertexList) {
						if (mv.equals(m))
							commonAdjacentSet.add(m);
					}
					if (logger.isDebugEnabled()) {
						System.out.println("mappedVertex: " + mv);
						System.out.println("equal similarity: "
								+ (node1.toString().equals(node2.toString())));
					}
				}
			}
		}
		return commonAdjacentSet;
	}

	/**
	 * Defines the common successor group for a MappedVertex. The adjacentSets
	 * of both nodes have to be both target Sets. These common successors group
	 * is added to the commonAdjacentSet.
	 * 
	 * @param commonAdjacentSet
	 * @param adjacentSet1
	 * @param adjacentSet2
	 * @return commonAdjacentSet
	 */
	public TreeSet<MappedVertex> adjacentGroups(
			TreeSet<MappedVertex> commonAdjacentSet,
			TreeSet<IGraphNode> adjacentSet1, TreeSet<IGraphNode> adjacentSet2) {
		for (IGraphNode node1 : adjacentSet1) {
			for (IGraphNode node2 : adjacentSet2) {
				if (node1.getIsGroup() && node2.getIsGroup()) {
					MappedVertex mv = new MappedVertex(node1, node2);
					for (MappedVertex m : mappedVertexList) {
						if (mv.equals(m))
							commonAdjacentSet.add(m);
					}
					if (logger.isDebugEnabled()) {
						System.out.println("mappedVertex: " + mv + " "
								+ mv.isGroup());
						// System.out.println("group1: " +
						// node1.getGroupNodes());
						// System.out.println("group2: " +
						// node2.getGroupNodes());
						System.out.println("size: " + mv.getGroupSize());
					}
				}
			}
		}
		return commonAdjacentSet;
	}

	/**
	 * Determines the best label based mapping between two groups. The common
	 * group will have the size of the smaller group. For each node in the
	 * smaller group, the most similar node from the larger group is assigned.
	 * The group similarity is between 0 (non matched) and the size of the
	 * smaller group (all matched).
	 * 
	 * @param mv
	 * @param groupSmall
	 * @param groupLarge
	 * @param order
	 */
	public void groupSimilarity(MappedVertex mv,
			TreeSet<IGraphNode> groupSmall, TreeSet<IGraphNode> groupLarge,
			String order) {
		HashMap<IGraphNode, ArrayList<IGraphNode>> nodeMapping = new HashMap<IGraphNode, ArrayList<IGraphNode>>();
		HashMap<IGraphNode, ArrayList<Double>> simMapping = new HashMap<IGraphNode, ArrayList<Double>>();
		ArrayList<MappedVertex> groupMembers = new ArrayList<MappedVertex>();
		Double tmpMax, groupSim = 0.0;
		int ind = 0;
		MappedVertex groupMem;
		for (IGraphNode n1 : groupSmall) {
			ArrayList<Double> currentSim = new ArrayList<Double>();
			ArrayList<IGraphNode> currentNodes = new ArrayList<IGraphNode>();
			for (IGraphNode n2 : groupLarge) {
				if (similarityMeasure.equals("Levenshtein")) {
					Levenshtein<String> levenshtein = new Levenshtein<String>(
							new StringAccessor(n1.toString()),
							new StringAccessor(n2.toString()));
					currentSim.add(levenshtein.getSimilarity());
					currentNodes.add(n2);
				} else if (similarityMeasure.equals("AlwaysTrue")) {
					currentSim.add(1d);
					currentNodes.add(n2);
				} else {
					// TODO fix this
					// for the moment just print out an error message
					logger.error("Not yet implemented!");
				}
			}
			nodeMapping.put(n1, currentNodes);
			simMapping.put(n1, currentSim);
		}
		for (IGraphNode n1 : groupSmall) {
			ArrayList<Double> currentSim = new ArrayList<Double>();
			ArrayList<IGraphNode> currentNodes = new ArrayList<IGraphNode>();
			currentSim = simMapping.get(n1);
			currentNodes = nodeMapping.get(n1);
			tmpMax = 0.0;
			for (Double d : currentSim) {
				tmpMax = Math.max(tmpMax, d);
			}
			ind = currentSim.indexOf(tmpMax);
			groupSim = groupSim + tmpMax;
			IGraphNode n2 = currentNodes.get(ind);

			if (order.equals("normal"))
				groupMem = new MappedVertex(n1, n2);
			else
				groupMem = new MappedVertex(n2, n1);

			groupMembers.add(groupMem);
			for (IGraphNode n : groupSmall) {
				simMapping.get(n).remove(ind);
				nodeMapping.get(n).remove(ind);
			}
		}
		for (MappedVertex m : groupMembers) {
			mv.addGroupMember(m);
		}
		mv.setGroupSimilarity(groupSim);
		mv.setGroupSize(Math.min(groupSmall.size(), groupLarge.size()));
		if (logger.isDebugEnabled()) {
			System.out.println("MappedVertex: " + mv);
			System.out.println("groupMembers: " + groupMembers);
			System.out.println("groupSimilarity: " + groupSim);
		}
	}

	/**
	 * Algorithm Calculates for a certain clique all possible adjacent
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
			TreeSet<IGraphNode> impossibleRight, TreeSet<MappedVertex> clique) {
		MappedVertex v;
		TreeSet<MappedVertex> clique2 = new TreeSet<MappedVertex>(
				new MappedVertexComparator());
		int cliqueSize = 0;
		countLoop++;
		if (Math.IEEEremainder(countLoop, 100000) == 0)
			System.out.println("countLoop: " + countLoop);
		for (MappedVertex copy : clique)
			clique2.add(copy);
		if (candidates.isEmpty()) {
			for (MappedVertex mv : clique2) {
				if (mv.isGroup()) {
					cliqueSize = cliqueSize + mv.getGroupSize();
					logger.debug(mv.isGroup() + " group size: "
							+ mv.getGroupSize());
				} else
					cliqueSize++;
			}
			logger.debug("cliq size: " + cliqueSize + " " + graphSize);
			if (cliqueSize == graphSize) {
				checkIfMaxClique(clique2);
				logger.debug("max cliq!!!!!!!!!!!!!!");
				countCliq++;
				if (Math.IEEEremainder(countCliq, 50000) == 0)
					System.out.println("Cliq: " + countCliq + " " + clique2);
			}
		} else {
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
						tempImpossibleRight, clique);
				clique.remove(v);
			}
		}
	}

	/**
	 * Calculates the similarity for a clique. The similarity consists of a
	 * structural and a label component. The structural component is calculated
	 * from the size of the clique divided by the avarage size of the two graphs
	 * (this result is weighted with 50%). The label component is calculated
	 * from the number of MappedVertices having equal labels divided by the
	 * total number of MappedVertices in the clique (this result is weighted
	 * with 50%).
	 * 
	 * @param clique
	 */
	public void checkIfMaxClique(TreeSet<MappedVertex> clique) {
		int tempInt, groupVertexSize = 0;
		double labelSimilarity = 0.0;
		String pathWithSimNumber, path;
		pathWithSimNumber = "";
		logger.debug("clique: " + clique);
		for (MappedVertex m : clique) {
			logger.debug(m + " isgroup: " + m.isGroup());
			if (m.isGroup()) {
				logger.debug(" groupsiz: " + m.getGroupSize() + " groupsim: "
						+ m.getGroupSimilarity());
				labelSimilarity = labelSimilarity + m.getGroupSimilarity();
				groupVertexSize = groupVertexSize + m.getGroupSize() - 1;
				for (MappedVertex mv : m.getGroupMembers()) {
					if (pathWithSimNumber.equals(""))
						pathWithSimNumber = pathWithSimNumber + mv.toString();
					else
						pathWithSimNumber = pathWithSimNumber + ", "
								+ mv.toString();
				}
			} else {
				logger.debug("labelsim: " + m.getLabelSimilarity());
				labelSimilarity = labelSimilarity + m.getLabelSimilarity();
				if (pathWithSimNumber.equals(""))
					pathWithSimNumber = pathWithSimNumber + m.toString();
				else
					pathWithSimNumber = pathWithSimNumber + ", " + m.toString();
			}
			logger.debug("totalSimilarity: " + labelSimilarity);
		}
		logger.debug("labelSimilarity absolute: " + labelSimilarity);
		labelSimilarity = labelSimilarity / (clique.size() + groupVertexSize);
		if (logger.isDebugEnabled()) {
			System.out.println("clique: " + pathWithSimNumber);
			System.out.println("labelSimilarity: " + labelSimilarity);
		}
		path = pathWithSimNumber;
		if (labelSimilarity == 1) {
			pathWithSimNumber = pathWithSimNumber + "; 1";
		} else {
			tempInt = (int) (labelSimilarity * 100);
			Integer conversion = new Integer(tempInt);
			pathWithSimNumber = pathWithSimNumber + "; 0."
					+ conversion.toString();
		}
		checkMax(labelSimilarity, path, pathWithSimNumber);
	}

	/**
	 * Checks whether the calculated similarity is maximal.
	 * 
	 * @param similarity
	 * @param clique
	 */
	public void checkMax(double similarity, String clique, String cliqueSim) {
		if (similarity >= maxSimilarity) {
			if (similarity > maxSimilarity)
				cliqueList.clear();
			cliqueList.add(clique);
		}
		if (similarity > maxSimilarity) {
			maxSimilarityPathWithSimNumber = cliqueSim;
			logger.debug("maxSimilarityPathWithSimNumber: "
					+ maxSimilarityPathWithSimNumber);
		}
		maxSimilarity = Math.max(maxSimilarity, similarity);
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
