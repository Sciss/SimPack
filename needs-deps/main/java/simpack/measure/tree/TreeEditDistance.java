/*
 * $Id: TreeEditDistance.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.alg.DijkstraShortestPath;
import org._3pq.jgrapht.graph.SimpleDirectedWeightedGraph;

import simpack.api.ITreeAccessor;
import simpack.api.ITreeNodeComparator;
import simpack.api.ITreeNode;
import simpack.api.impl.AbstractDistanceConversion;
import simpack.api.impl.AbstractTreeSimilarityMeasure;
import simpack.exception.InvalidElementException;
import simpack.util.conversion.WorstCaseDistanceConversion;
import simpack.util.tree.GraphVertexTuple;
import simpack.util.tree.TreeNodeTuple;
import simpack.util.tree.TreeUtil;
import simpack.util.tree.comparator.AlwaysTrueTreeNodeComparator;

/**
 * This implements an edit distance calculation for trees. The trees are rooted
 * and ordered. The algorithm is taken from Gabriel Valientes book "Algorithms
 * on trees and graphs" (Springer) and described in chapter 2.1 "The tree edit
 * distance problem".
 * 
 * @author Tobias Sager
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class TreeEditDistance extends AbstractTreeSimilarityMeasure {

	/**
	 * Default weight of delete operation
	 */
	public static double DEFAULT_WEIGHT_DELETE = 1d;

	/**
	 * Default weight of insert operation
	 */
	public static double DEFAULT_WEIGHT_INSERT = 1d;

	/**
	 * Default weight of substitute operation for non-equal objects. Equality is
	 * determined by comparator.
	 */
	public static double DEFAULT_WEIGHT_SUBSTITUE = 1d;

	/**
	 * Default weight of substitute operation for equal objects. Equality is
	 * determined by comparator.
	 */
	public static double DEFAULT_WEIGHT_SUBSTITUE_EQUAL = 0d;

	/**
	 * Default path length limit is infinity
	 */
	public static double DEFAULT_PATH_LENGTH_LIMIT = Double.POSITIVE_INFINITY;

	/**
	 * Contains the first tree
	 */
	private ITreeNode tree1;

	/**
	 * The first trees preordered list of nodes
	 */
	private List<ITreeNode> list1 = null;

	/**
	 * This stores the depth for each element of tree1
	 */
	private HashMap<ITreeNode, Integer> depth1 = new HashMap<ITreeNode, Integer>();

	/**
	 * Contains the second tree
	 */
	private ITreeNode tree2;

	/**
	 * The second trees preordered list of nodes
	 */
	private List<ITreeNode> list2 = null;

	/**
	 * This stores the depth for each element of tree2
	 */
	private HashMap<ITreeNode, Integer> depth2 = new HashMap<ITreeNode, Integer>();

	/**
	 * Default path length limit is infinity.
	 */
	private double pathLengthLimit = DEFAULT_PATH_LENGTH_LIMIT;

	/**
	 * Contains the edit graph
	 */
	private SimpleDirectedWeightedGraph editDistanceGraph;

	/**
	 * Saves the first vertex of the editDistanceGraph
	 */
	private GraphVertexTuple firstVertex;

	/**
	 * Saves the last vertex of the editDistanceGraph
	 */
	private GraphVertexTuple lastVertex;

	/**
	 * Default weight of delete operation
	 */
	private double weightDelete = DEFAULT_WEIGHT_DELETE;

	/**
	 * Default weight of insert operation
	 */
	private double weightInsert = DEFAULT_WEIGHT_INSERT;

	/**
	 * Default weight of substitute operation for non-equal objects. Equality is
	 * determined by comparator.
	 */
	private double weightSubstitute = DEFAULT_WEIGHT_SUBSTITUE;

	/**
	 * Default weight of substitute operation for equal objects. Equality is
	 * determined by comparator.
	 */
	private double weightSubstituteEqual = DEFAULT_WEIGHT_SUBSTITUE_EQUAL;

	/**
	 * This is the calculated shortest path
	 */
	private DijkstraShortestPath shortestPath;

	/**
	 * 
	 */
	private AbstractDistanceConversion conversion;

	/**
	 * Constructor.
	 * <p>
	 * Pass two <code>ITreeAccessor</code> and expect the edit distance in
	 * {@link #getTreeEditDistance()} after calling {@link #calculate()} and
	 * {@link simpack.api.impl.AbstractCalculator#isCalculated()} is true.
	 * <p>
	 * The AlwaysTrueTreeNodeComparator will be used for compare.
	 * </p>
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 * @throws NullPointerException
	 *             if tree1 or tree2 are null
	 */
	public TreeEditDistance(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2) throws NullPointerException,
			InvalidElementException {
		this(treeAccessor1, treeAccessor2, new AlwaysTrueTreeNodeComparator(),
				new WorstCaseDistanceConversion());
	}

	/**
	 * Constructor.
	 * <p>
	 * Pass two <code>ITreeAccessor</code> and expect the edit distance in
	 * {@link #getTreeEditDistance()} after calling {@link #calculate()} and
	 * {@link simpack.api.impl.AbstractCalculator#isCalculated()} is true.
	 * <p>
	 * Use the given comparator for compares.
	 * </p>
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator
	 * @param conversion
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 * @throws NullPointerException
	 *             if tree1 or tree2 are null
	 */
	public TreeEditDistance(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator,
			AbstractDistanceConversion conversion) throws NullPointerException,
			InvalidElementException {
		this(treeAccessor1, treeAccessor2, comparator, conversion,
				DEFAULT_PATH_LENGTH_LIMIT, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_SUBSTITUE);
	}

	/**
	 * Constructor.
	 * <p>
	 * Pass two <code>ITreeAccessor</code> and expect the edit distance in
	 * {@link #getTreeEditDistance()} after calling {@link #calculate()} and
	 * {@link simpack.api.impl.AbstractCalculator#isCalculated()} is true.
	 * <p>
	 * You can limit the search for a path by length when passing the
	 * pathLengthLimit argument. Manipulate the weigth assigned with transitions
	 * (edges) with the other parameters.
	 * </p>
	 * <p>
	 * The AlwaysTrueTreeNodeComparator will be used for compare.
	 * </p>
	 * <p>
	 * Any of pathLengthLimit and the weigth arguments can be null upon which
	 * default values will be used.
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param pathLengthLimit
	 * @param weigthInsert
	 * @param weigthDelete
	 * @param weigthSubstitute
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 * @throws NullPointerException
	 *             if tree1 or tree2 are null
	 */
	public TreeEditDistance(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2, Double pathLengthLimit,
			Double weigthInsert, Double weigthDelete, Double weigthSubstitute)
			throws NullPointerException, InvalidElementException {
		this(treeAccessor1, treeAccessor2, new AlwaysTrueTreeNodeComparator(),
				new WorstCaseDistanceConversion(), pathLengthLimit,
				weigthInsert, weigthDelete, weigthSubstitute);
	}

	/**
	 * Constructor.
	 * <p>
	 * Pass two <code>ITreeAccessor</code> and expect the edit distance in
	 * {@link #getTreeEditDistance()} after calling {@link #calculate()} and
	 * {@link simpack.api.impl.AbstractCalculator#isCalculated()} is true.
	 * <p>
	 * You can limit the search for a path by length when passing the
	 * pathLengthLimit argument. Manipulate the weigth assigned with transitions
	 * (edges) with the other parameters.
	 * </p>
	 * <p>
	 * Use the given comparator as comparator
	 * </p>
	 * <p>
	 * Any of pathLengthLimit and the weigth arguments can be null upon which
	 * default values will be used.
	 * </p>
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator
	 * @param conversion
	 * @param pathLengthLimit
	 * @param weigthInsert
	 * @param weigthDelete
	 * @param weigthSubstitute
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 * @throws NullPointerException
	 *             if tree1 or tree2 are null
	 */
	public TreeEditDistance(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator,
			AbstractDistanceConversion conversion, Double pathLengthLimit,
			Double weigthInsert, Double weigthDelete, Double weigthSubstitute)
			throws NullPointerException, InvalidElementException {
		this(treeAccessor1, treeAccessor2, comparator, conversion,
				pathLengthLimit, weigthInsert, weigthDelete, weigthSubstitute,
				null);
	}

	/**
	 * Constructor.
	 * <p>
	 * Pass two <code>ITreeAccessor</code> and expect the edit distance in
	 * {@link #getTreeEditDistance()} after calling {@link #calculate()} and
	 * {@link simpack.api.impl.AbstractCalculator#isCalculated()} is true.
	 * <p>
	 * You can limit the search for a path by length when passing the
	 * pathLengthLimit argument. Manipulate the weigth assigned with transitions
	 * (edges) with the other parameters.
	 * </p>
	 * <p>
	 * Use the given comparator as comparator
	 * </p>
	 * <p>
	 * Any of pathLengthLimit and the weigth arguments can be null upon which
	 * default values will be used.
	 * </p>
	 * 
	 * @param treeAccessor1
	 * @param treeAccessor2
	 * @param comparator
	 * @param conversion
	 * @param pathLengthLimit
	 * @param weigthInsert
	 * @param weigthDelete
	 * @param weigthSubstitute
	 * @param weigthSubstituteEqual
	 * @throws InvalidElementException
	 *             when a tree contains an invalid structure or childs
	 * @throws NullPointerException
	 *             if tree1 or tree2 are null
	 */
	public TreeEditDistance(ITreeAccessor treeAccessor1,
			ITreeAccessor treeAccessor2,
			ITreeNodeComparator<ITreeNode> comparator,
			AbstractDistanceConversion conversion, Double pathLengthLimit,
			Double weigthInsert, Double weigthDelete, Double weigthSubstitute,
			Double weigthSubstituteEqual) throws NullPointerException,
			InvalidElementException {
		super(comparator);

		this.conversion = conversion;

		if (treeAccessor1 == null || treeAccessor2 == null
				|| treeAccessor1.getRoot() == null
				|| treeAccessor2.getRoot() == null)
			throw new NullPointerException("Invalid accessors passed!");

		this.tree1 = treeAccessor1.getRoot();
		this.tree2 = treeAccessor2.getRoot();

		list1 = TreeUtil.enumerationToList(tree1.preorderEnumeration());
		list2 = TreeUtil.enumerationToList(tree2.preorderEnumeration());

		if (pathLengthLimit != null)
			this.pathLengthLimit = pathLengthLimit.doubleValue();
		if (weigthInsert != null)
			this.weightInsert = weigthInsert.doubleValue();
		if (weigthDelete != null)
			this.weightDelete = weigthDelete.doubleValue();
		if (weigthSubstitute != null)
			this.weightSubstitute = weigthSubstitute.doubleValue();
		if (weigthSubstituteEqual != null)
			this.weightSubstituteEqual = weigthSubstituteEqual.doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		if (calculateGraph()) {
			shortestPath = new DijkstraShortestPath(editDistanceGraph,
					firstVertex, lastVertex, pathLengthLimit);
			if (shortestPath != null) {
				setCalculated(true);

				if (conversion instanceof WorstCaseDistanceConversion) {
					((WorstCaseDistanceConversion) conversion)
							.setWorstCaseDistance(getWorstCaseSumOfNodes());
				}

				similarity = new Double(conversion
						.convert(getTreeEditDistance()));

				return true;
			}
		}
		return false;
	}

	/**
	 * This method corresponds to "tree_edit_graph(tree&, tree&, GRAPH<string,string>&)"
	 * from http://www.lsi.upc.es/~valiente/algorithm/combin.cpp
	 * 
	 * @return true upon success, false otherwise
	 */
	private boolean calculateGraph() {
		// cache size of lists
		int list1size = list1.size();
		int list2size = list2.size();

		// this stores the order number for each Node
		HashMap<ITreeNode, Integer> orderNum1 = new HashMap<ITreeNode, Integer>();
		HashMap<ITreeNode, Integer> orderNum2 = new HashMap<ITreeNode, Integer>();

		// calculate preorder numeration and depth information for each node
		preorderTreeDepth(tree1, orderNum1, depth1);
		preorderTreeDepth(tree2, orderNum2, depth2);

		// put all depth information into array; ordering is by preorder
		int[] d1 = new int[list1size + 1];
		int[] d2 = new int[list2size + 1];
		Iterator<ITreeNode> iter = list1.listIterator();
		while (iter.hasNext()) {
			ITreeNode a = iter.next();
			d1[orderNum1.get(a)] = depth1.get(a);
		}
		iter = list2.listIterator();
		while (iter.hasNext()) {
			ITreeNode a = iter.next();
			d2[orderNum2.get(a)] = depth2.get(a);
		}

		// clear graph
		editDistanceGraph = new SimpleDirectedWeightedGraph();

		// create vertexes for all tree1/tree2 crossings
		GraphVertexTuple[][] vertexArray = new GraphVertexTuple[list1size + 1][list2size + 1];
		for (int i = 0; i <= list1size; i++) {
			for (int j = 0; j <= list2size; j++) {
				GraphVertexTuple t = new GraphVertexTuple(new Integer(i),
						new Integer(j));
				if (i > 0 && j > 0)
					t.setTreeNodeTuple(new TreeNodeTuple(list1.get(i - 1),
							list2.get(j - 1)));
				vertexArray[i][j] = t;
				if (!editDistanceGraph.addVertex(t))
					return false;
			}
		}

		// save eckpunkte
		firstVertex = vertexArray[0][0];
		lastVertex = vertexArray[list1size][list2size];

		// delete edges at outer right
		for (int i = 0; i < list1size; i++) {
			Edge e = editDistanceGraph.addEdge(vertexArray[i][list2size],
					vertexArray[i + 1][list2size]);
			if (e == null)
				return false;
			e.setWeight(weightDelete);
		}
		// insert edges at bottom
		for (int j = 0; j < list2size; j++) {
			Edge e = editDistanceGraph.addEdge(vertexArray[list1size][j],
					vertexArray[list1size][j + 1]);
			if (e == null)
				return false;
			e.setWeight(weightInsert);
		}
		for (int i = 0; i < list1size; i++) {
			for (int j = 0; j < list2size; j++) {
				if (d1[i + 1] >= d2[j + 1]) {
					Edge e = editDistanceGraph.addEdge(vertexArray[i][j],
							vertexArray[i + 1][j]);
					if (e == null)
						return false;
					e.setWeight(weightDelete);
				}
				if (d1[i + 1] == d2[j + 1]) {
					Edge e = editDistanceGraph.addEdge(vertexArray[i][j],
							vertexArray[i + 1][j + 1]);
					if (e == null)
						return false;
					if (comparator.compare(list1.get(i), list2.get(j)) == 0) {
						e.setWeight(weightSubstituteEqual);
					} else {
						e.setWeight(weightSubstitute);
					}
				}
				if (d1[i + 1] <= d2[j + 1]) {
					Edge e = editDistanceGraph.addEdge(vertexArray[i][j],
							vertexArray[i][j + 1]);
					if (e == null)
						return false;
					e.setWeight(weightInsert);
				}
			}
		}

		return true;
	}

	/**
	 * Implementing method "preorder_tree_depth" by Gabriel Valiente: See
	 * http://www.lsi.upc.es/~valiente/algorithm/combin.cpp
	 * 
	 * @param tree
	 * @param order
	 * @param depth
	 */
	private void preorderTreeDepth(ITreeNode tree,
			HashMap<ITreeNode, Integer> order, HashMap<ITreeNode, Integer> depth) {
		order.clear();
		depth.clear();

		Stack<ITreeNode> stack = new Stack<ITreeNode>();
		stack.push((ITreeNode) tree.getRoot());
		int num = 1;
		ITreeNode v, w;
		do {
			v = stack.pop();
			order.put(v, num++);
			if (v.isRoot())
				depth.put(v, 0);
			else
				depth.put(v, depth.get(v.getParent()) + 1);
			try {
				w = (ITreeNode) v.getLastChild();
			} catch (NoSuchElementException e) {
				continue;
			}
			while (w != null) {
				stack.push(w);
				w = w.getPreviousSibling();
			}

		} while (!stack.isEmpty());
	}

	/**
	 * Checks if a valid path and therefore a valid edit distance has been
	 * found.
	 * 
	 * @return true if a path exists, false otherwise
	 */
	public boolean hasValidEditDistance() {
		return isCalculated()
				&& (getTreeEditDistance() != Double.POSITIVE_INFINITY)
				&& (shortestPath.getPathEdgeList() != null);
	}

	/**
	 * @return Returns the treeEditDistance.
	 * @throws NullPointerException
	 *             if instance is not calculated
	 */
	public double getTreeEditDistance() throws NullPointerException {
		if (!isCalculated()) {
			throw new NullPointerException(
					"Instance did not sucessfully calculate!");
		}
		return shortestPath.getPathLength();
	}

	/**
	 * This worst-case is the sum of nodes in both trees.
	 * 
	 * @return the worst-case scenario of edit operations
	 */
	public double getWorstCaseSumOfNodes() {
		return list1.size() + list2.size();
	}

	/**
	 * This worst-case is computed as follows: We look at the original graph
	 * <code>editDistanceGraph</code>, and change the weights of all diagonal
	 * edges to {@link #weightSubstitute}. Previously their weights depended on
	 * whether the node-tuple is equal or not. But now we look at it as if all
	 * the labels in both trees were different. Then we compute again the
	 * shortest path through this altered graph. By considering the
	 * shortestPath, we are still able to insert nodes prior to delete others.
	 * This is not possible in: {@link #getWorstCaseRetainStructure()}.
	 * 
	 * @return the worst-case scenario of edit operations
	 */
	public double getWorstCaseSubstituteAll() {
		double worstCase = -1;

		// make a copy of editDistanceGraph
		SimpleDirectedWeightedGraph worstCaseGraph = new SimpleDirectedWeightedGraph();
		Set vertices = editDistanceGraph.vertexSet();
		Set edges = editDistanceGraph.edgeSet();
		worstCaseGraph.addAllVertices(vertices);
		worstCaseGraph.addAllEdges(edges);

		edges = worstCaseGraph.edgeSet();
		Iterator edgeIterator = edges.iterator();

		while (edgeIterator.hasNext()) {
			Edge edge = (Edge) edgeIterator.next();
			GraphVertexTuple vertex1 = (GraphVertexTuple) edge.getSource();
			GraphVertexTuple vertex2 = (GraphVertexTuple) edge.getTarget();
			// check if this edge is a diagonal
			if (vertex2.getLeft() == vertex1.getLeft() + 1
					&& vertex2.getRight() == vertex1.getRight() + 1) {
				edge.setWeight(weightSubstitute);
			}
		}
		DijkstraShortestPath shortestPath = new DijkstraShortestPath(
				worstCaseGraph, firstVertex, lastVertex, pathLengthLimit);
		worstCase = shortestPath.getPathLength();
		return worstCase;
	}

	/**
	 * This worst-case is computed as follows: Replace all weights of the
	 * diagonal edges with {@link #weightSubstitute} (in case that this edge had
	 * received a weight of zero because of equal labels). Walk from firstVertex
	 * (top left corner) to lastVertex (bottom right corner) with the following
	 * priority: 1. diagonal edges, 2. vertical edges, 3. horizontal edges. In
	 * different words: Basically we delete all nodes from the first tree and
	 * add all nodes from the second tree. But the nodes that are at the same
	 * location in both trees, we will consider as having different labels. That
	 * means, we count as if we would substitute their labels. The main
	 * difference of this method to {@link #getWorstCaseSubstituteAll()} is,
	 * that here it is not possible to insert nodes before we have deleted all
	 * wrong nodes.
	 * 
	 * @return the worst-case scenario of edit operations
	 */
	public double getWorstCaseRetainStructure() {
		double pathLength = 0;
		Edge verticalEdge;
		Edge horizontalEdge;
		Edge diagonalEdge;

		GraphVertexTuple vertex = firstVertex;

		while (vertex != lastVertex) {
			verticalEdge = null;
			horizontalEdge = null;
			diagonalEdge = null;
			List adjacentEdges = editDistanceGraph.outgoingEdgesOf(vertex);
			Iterator edgeIterator = adjacentEdges.iterator();

			// in this loop gather all available edges outgoing from a vertex
			// and
			// assign them to the corresponding variable '...Edge'.
			while (edgeIterator.hasNext()) {
				Edge edge = (Edge) edgeIterator.next();
				GraphVertexTuple oppositeVertex = (GraphVertexTuple) edge
						.oppositeVertex(vertex);
				int left = vertex.getLeft();
				int right = vertex.getRight();
				int oppositeLeft = oppositeVertex.getLeft();
				int oppositeRight = oppositeVertex.getRight();
				// first check if edge is a diagonal
				if ((oppositeLeft == left + 1) && (oppositeRight == right + 1)) {
					diagonalEdge = edge;
					break;
				}
				// then check if this edge is a vertical (which means to delete
				// a node)
				else if ((oppositeLeft == left + 1) && (oppositeRight == right)) {
					verticalEdge = edge;
				} else
					horizontalEdge = edge; // it is a horizontal edge (which
				// means to add a node)
			}
			Edge edgeToWalk;
			double weight = 0;
			if (diagonalEdge != null) {
				edgeToWalk = diagonalEdge;
				weight = weightSubstitute;
			} else if (verticalEdge != null) {
				edgeToWalk = verticalEdge;
				weight = edgeToWalk.getWeight();
			} else {
				edgeToWalk = horizontalEdge;
				weight = edgeToWalk.getWeight();
			}

			pathLength += weight;
			vertex = (GraphVertexTuple) edgeToWalk.oppositeVertex(vertex);
		}
		return pathLength;
	}

	/**
	 * @return Returns the weightDelete.
	 */
	public double getWeightDelete() {
		return weightDelete;
	}

	/**
	 * @param weightDelete
	 *            The weightDelete to set.
	 */
	public void setWeightDelete(double weightDelete) {
		this.weightDelete = weightDelete;
	}

	/**
	 * @return Returns the weightInsert.
	 */
	public double getWeightInsert() {
		return weightInsert;
	}

	/**
	 * @param weightInsert
	 *            The weightInsert to set.
	 */
	public void setWeightInsert(double weightInsert) {
		this.weightInsert = weightInsert;
	}

	/**
	 * @return Returns the weightSubstitute.
	 */
	public double getWeightSubstitute() {
		return weightSubstitute;
	}

	/**
	 * @param weightSubstitute
	 *            The weightSubstitute to set.
	 */
	public void setWeightSubstitute(double weightSubstitute) {
		this.weightSubstitute = weightSubstitute;
	}

	/**
	 * @return Returns the shortestPath.
	 */
	public DijkstraShortestPath getShortestPath() {
		return shortestPath;
	}
}
