/*
 * $Id: SubgraphIsomorphism.java 757 2008-04-17 17:42:53Z kiefer $
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
 * This class implements a variation of Valiente's maximum common subgraph
 * algorithm as described in the book "Algorithms on Trees and Graphs", Springer
 * 2002. The algorithm calculates the similarity between two graphs based on the
 * *connected* maximum common clique of the two graphs under comparison.
 * Additionally the algorithm implements optimizations to reduce complexity,
 * such as grouping of leaf nodes and a reduction of entry points for the
 * algorithm.
 * 
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class SubgraphIsomorphism extends AbstractGraphSimilarityMeasure {

	public static Logger logger = LogManager.getLogger(SubgraphIsomorphism.class);

	public static String DEFAULT_NODE_LABEL_SIMILARITY_MEASURE = "Levenshtein";

	public static int DEFAULT_MIN_CLIQUE_SIZE = 2;

	public static double DEFAULT_STRUCTURE_WEIGHT = 0.5d;

	public static double DEFAULT_LABEL_WEIGHT = 0.5d;

	public static boolean NODE_GROUPING = false;

	public static String DEFAULT_DENOMINATOR = "average";

	protected String similarityMeasure = DEFAULT_NODE_LABEL_SIMILARITY_MEASURE;

	protected int minCliqueSize = DEFAULT_MIN_CLIQUE_SIZE;

	protected double structureWeight = DEFAULT_STRUCTURE_WEIGHT;

	protected double labelWeight = DEFAULT_LABEL_WEIGHT;

	protected boolean groupNodes = NODE_GROUPING;

	protected IGraphAccessor graphAccessor1, graphAccessor2;

	protected double graphsSize = 0d;

	protected int countCliq = 0, countLoop = 0, countMV = 0,
			countLoopAdjacentMap = 0;

	protected TreeSet<IGraphNode> nodeSet1 = new TreeSet<IGraphNode>(
			new NamedGraphNodeComparator());

	protected TreeSet<IGraphNode> nodeSet2 = new TreeSet<IGraphNode>(
			new NamedGraphNodeComparator());

	protected ArrayList<MappedVertex> mappedVertexList = new ArrayList<MappedVertex>();

	protected ArrayList<MappedVertex> startCandidateList = new ArrayList<MappedVertex>();

	protected TreeMap<MappedVertex, TreeSet<MappedVertex>> adjacentMap = new TreeMap<MappedVertex, TreeSet<MappedVertex>>(
			new MappedVertexComparator());

	protected TreeMap<IGraphNode, Double> deleteSet = new TreeMap<IGraphNode, Double>(
			new NamedGraphNodeComparator());

	protected TreeSet<String> cliqueList = new TreeSet<String>();

	protected IGraphNode node1, node2;

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
	public SubgraphIsomorphism(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2) {
		this(graphAccessor1, graphAccessor2,
				DEFAULT_NODE_LABEL_SIMILARITY_MEASURE, DEFAULT_MIN_CLIQUE_SIZE,
				DEFAULT_LABEL_WEIGHT, DEFAULT_STRUCTURE_WEIGHT,
				DEFAULT_DENOMINATOR, NODE_GROUPING);
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
	public SubgraphIsomorphism(IGraphAccessor graphAccessor1,
			IGraphAccessor graphAccessor2, String similarityMeasure,
			int minCliqueSize, double structureWeight, double labelWeight,
			String denominator, boolean groupNodes) {
		this.graphAccessor1 = graphAccessor1;
		this.graphAccessor2 = graphAccessor2;
		this.similarityMeasure = similarityMeasure;
		this.minCliqueSize = minCliqueSize;
		this.labelWeight = labelWeight;
		this.structureWeight = structureWeight;
		this.groupNodes = groupNodes;

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
			// TODO add exception handling
			// for the moment just print out an error message
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

		nodeSet1 = graphAccessor1.getNodeSet();
		nodeSet2 = graphAccessor2.getNodeSet();
		if (groupNodes) {
			setGroups(graphAccessor1, "G1");
			setGroups(graphAccessor2, "G2");
		}
		mappedVertexList = getVertexList();
		TreeSet<IGraphNode> impossibleLeft = new TreeSet<IGraphNode>();
		TreeSet<IGraphNode> impossibleRight = new TreeSet<IGraphNode>();
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
							+ "  grpsim " + mv.getGroupSimilarity() + "  Map: "
							+ adjacentMap.get(mv));
				} else {
					System.out.println("labelsim " + mv.getLabelSimilarity()
							+ "  Map: " + adjacentMap.get(mv));
				}
				for (MappedVertex n : adjacentMap.get(mv)) {
					System.out.println(n + " sim " + n.getLabelSimilarity());
				}
			}
		}

		for (MappedVertex m : startCandidateList) {
			countMV++;
			if (Math.IEEEremainder(countMV, 100) == 0) {
				eDate = Calendar.getInstance();
				millis = eDate.getTime().getTime();
				ts = new Timestamp(millis);
				logger.debug(countMV + ") " + m + " " + ts + " = "
						+ (millis - startMillis) / 1000 + " sec");
			}
			candidates.add(m);
			nextMaximalClique(candidates, impossibleLeft, impossibleRight,
					clique, minCliqueSize);
		}
		logger.debug("maxSimilarity: " + maxSimilarityPathWithSimNumber);
		logger.debug("maxVertex: " + maxVertexPathWithSimNumber);

		similarity = maxSimilarity;
		setCalculated(true);
		return true;
	}

	/**
	 * Creates new group nodes for each graph by aggregating structural
	 * identical nodes. A number of nodes qualify for a certain group if the
	 * share the following properties: 1) A node n1 has more than one successor.
	 * 2) All successor nodes of n1 are leaf nodes. 3) All these successor nodes
	 * have the same predecessor nodes. 4) All these predecessor nodes have only
	 * those successor nodes. Then all of those predecessors (n1 is one of them)
	 * have this newly created group node as an additional successor.
	 * 
	 * @param graphAccessor
	 * @param graph
	 */
	public void setGroups(IGraphAccessor graphAccessor, String graph) {
		boolean group;
		Integer groupCount = 0;
		TreeSet<IGraphNode> groupSet = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		for (IGraphNode node : graphAccessor.getNodeSet()) {
			logger.debug("node: " + node);
			group = true;
			if (!node.getHasGroup() && node.getSuccessorSet() != null
					&& node.getSuccessorSet().size() > 1) {
				TreeSet<IGraphNode> successorSet = new TreeSet<IGraphNode>(
						new NamedGraphNodeComparator());
				TreeSet<IGraphNode> predecessorSet = new TreeSet<IGraphNode>(
						new NamedGraphNodeComparator());
				TreeSet<IGraphNode> memberSet = new TreeSet<IGraphNode>(
						new NamedGraphNodeComparator());
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
					} else {
						group = false;
					}
				}
				for (IGraphNode predecessor : predecessorSet) {
					if (predecessor.getSuccessorSet().size() != successorSet
							.size()) {
						group = false;
					}
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
	 * Maps all vertices in the first graph to vertices in the second graph and
	 * adds these vertices to the mappedVertexList. Group nodes can only be
	 * mapped to other group nodes and non group nodes can only be mapped to non
	 * group nodes. Mapped non-leaf nodes which are not contained in the
	 * deleteSet are added to the startCandidateList (which are then starting
	 * points in the calculator).
	 * 
	 * @return mappedVertexList
	 */
	public ArrayList<MappedVertex> getVertexList() {
		TreeSet<IGraphNode> nodeSet = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		logger.debug("set1 size: " + nodeSet1.size() + " set2 size: "
				+ nodeSet2.size());
		for (IGraphNode n : nodeSet1) {
			if (n.getSuccessorSet().size() == 0)
				nodeSet.add(n);
		}
		logger.debug(nodeSet);
		markNodes(nodeSet, 0.0);
		if (logger.isDebugEnabled()) {
			Set<IGraphNode> set;
			set = deleteSet.keySet();
			for (IGraphNode n : set) {
				if (deleteSet.get(n) == 0.0)
					System.out.println("delete: " + n);
			}
		}
		for (IGraphNode n1 : nodeSet1) {
			for (IGraphNode n2 : nodeSet2) {
				if (n1.getIsGroup() == n2.getIsGroup()) {
					MappedVertex mv = new MappedVertex(n1, n2);
					logger.debug("mv: " + mv + " groups: " + n1.getIsGroup());
					mappedVertexList.add(mv);
					logger.debug(n1.getSuccessorSet().size() + " "
							+ deleteSet.get(n1) + " "
							+ n2.getSuccessorSet().size());
					if (n1.getSuccessorSet().size() > 0
							&& deleteSet.get(n1) == 1.0
							&& n2.getSuccessorSet().size() > 0) {
						startCandidateList.add(mv);
					}
					if (n1.getIsGroup()) {
						if (n1.getGroupSize() > n2.getGroupSize()) {
							groupSimilarity(mv, n2.getGroupNodes(), n1
									.getGroupNodes(), "reverse");
						} else {
							groupSimilarity(mv, n1.getGroupNodes(), n2
									.getGroupNodes(), "normal");
						}
					} else {
						if (similarityMeasure.equals("Levenshtein")) {
							Levenshtein<String> levenshtein = new Levenshtein<String>(
									new StringAccessor(n1.toString()),
									new StringAccessor(n2.toString()));
							mv.setLabelSimilarity(levenshtein.getSimilarity());
						} else if (similarityMeasure.equals("AlwaysTrue")) {
							mv.setLabelSimilarity(1d);
						} else {
							// TODO fix this
							// for the moment just print out an error message
							logger.error("Not yet implemented!");
						}
					}
				}

			}
		}
		if (logger.isDebugEnabled()) {
			for (MappedVertex mv : mappedVertexList) {
				System.out.println("mappedVertex: " + mv);
			}
		}
		return mappedVertexList;
	}

	/**
	 * Marks nodes which can be omitted as starting points in the calculator.
	 * All leaf nodes can be omitted and each second level in the bigger graph
	 * (beginning at the leaf nodes). This method is invoked recursively for
	 * each level.
	 * 
	 * @param nodeSet
	 * @param mark
	 */
	public void markNodes(TreeSet<IGraphNode> nodeSet, Double mark) {
		TreeSet<IGraphNode> newNodeSet = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		TreeSet<IGraphNode> passedSet = new TreeSet<IGraphNode>(
				new NamedGraphNodeComparator());
		if (mark == 0.0) {
			for (IGraphNode node : nodeSet) {
				if (deleteSet.containsKey(node))
					passedSet.add(node);
				else
					deleteSet.put(node, 0.0);
			}
		} else {
			for (IGraphNode node : nodeSet) {
				if (deleteSet.containsKey(node))
					passedSet.add(node);
				deleteSet.put(node, 1.0);
			}
		}
		for (IGraphNode node : nodeSet) {
			if (!passedSet.contains(node)) {
				for (IGraphNode predecessor : node.getPredecessorSet()) {
					newNodeSet.add(predecessor);
				}
			}
		}
		mark = Math.IEEEremainder(mark + 1, 2);
		logger.debug(mark + " " + newNodeSet);
		if (newNodeSet.size() > 0)
			markNodes(newNodeSet, mark);
	}

	/**
	 * assigns to each MappedVertex in the mappedVertexList all its adjacent
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
			if (Math.IEEEremainder(countLoopAdjacentMap, 1000) == 0)
				System.out.println("loop adjacency: " + countLoopAdjacentMap
						+ " mv: " + mv);
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
	 * Defines the common successors and predecessors for each MappedVertex. The
	 * adjacentSets of both graphs have to be either both source or both target
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
						System.out.println("group1: " + node1.getGroupNodes());
						System.out.println("group2: " + node2.getGroupNodes());
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

		for (MappedVertex copy : clique) {
			clique2.add(copy);
		}

		if (candidates.isEmpty()) {

			for (MappedVertex mv : clique2) {
				if (mv.isGroup()) {
					cliqueSize = cliqueSize + mv.getGroupSize();
				} else {
					cliqueSize++;
				}
			}

			if (cliqueSize >= shortest) {
				countCliq++;
				if (Math.IEEEremainder(countCliq, 200000) == 0) {
					logger.debug("Cliq: " + countCliq + " " + clique2);
				}
				checkIfMaxClique(clique2);
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
						tempImpossibleRight, clique, shortest);
				clique.remove(v);
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
		int tempInt;
		int groupVertexSize = 0;
		double labelSimilarity = 0d;
		double structureSimilarity = 0d;
		double overallSimilarity = 0d;
		String pathWithSimNumber = "";
		String path;

		for (MappedVertex m : clique) {
			if (m.isGroup()) { // two mapped group nodes
				labelSimilarity = labelSimilarity + m.getGroupSimilarity();
				groupVertexSize = groupVertexSize + m.getGroupSize() - 1;
				for (MappedVertex mv : m.getGroupMembers()) {
					if (pathWithSimNumber.equals("")) {
						pathWithSimNumber = pathWithSimNumber + mv.toString();
					} else {
						pathWithSimNumber = pathWithSimNumber + ", "
								+ mv.toString();
					}
				}
			} else {
				labelSimilarity = labelSimilarity + m.getLabelSimilarity();
				if (pathWithSimNumber.equals("")) {
					pathWithSimNumber = pathWithSimNumber + m.toString();
				} else {
					pathWithSimNumber = pathWithSimNumber + ", " + m.toString();
				}
			}

		}
		// labelSimilarity = labelSimilarity/(clique.size()+groupVertexSize);
		labelSimilarity = labelSimilarity / graphsSize;
		structureSimilarity = ((clique.size() + groupVertexSize)) / graphsSize;
		// overallSimilarity = structureSimilarity;
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
		// if ((clique.size()+groupVertexSize)>8)
		// System.out.println(clique.size()+" "+clique);
		checkMax((clique.size() + groupVertexSize), overallSimilarity, path,
				pathWithSimNumber);

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
			if (similarity > maxSimilarity) {
				cliqueList.clear();
			}
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

	public double getGraphsSize() {
		return graphsSize;
	}

}
