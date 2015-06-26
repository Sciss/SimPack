/*
 * $Id: JenaOntologyAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IGraphNode;
import simpack.api.impl.AbstractGraphAccessor;
import simpack.exception.InvalidElementException;
import simpack.util.graph.GraphNode;
import simpack.util.graph.comparator.NamedGraphNodeComparator;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

/**
 * This is the accessor to access ontologies by the use of the <a
 * href="http://jena.sourceforge.net">Jena API</a>.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class JenaOntologyAccessor extends AbstractGraphAccessor {

	public static Logger logger = LogManager.getLogger(JenaOntologyAccessor.class);

	private OntModel model;

	private Filter anonFilter, sameNamespaceFilter;

	private Filter RDFSchemaResource, OWLResource;

	private String ontologyRootURI;

	private TreeMap<String, Integer> classURIsToIDsMap;

	private TreeMap<Integer, String> idsToClassURIsMap;

	private SparseDoubleMatrix2D shortestDistanceToDescendantOrChildMatrix;

	private SparseDoubleMatrix2D longestDistanceToDescendantOrChildMatrix;

	/**
	 * Constructor.
	 * <p>
	 * Takes the ontology's URI, its base URI, and its root class as inputs.
	 * <code>ontModelspec</code> determines the ontology language as well as
	 * the level of inference in the constructed model.
	 * 
	 * @param ontologyURI
	 *            URI of ontology
	 * @param altOntologyURL
	 *            an alternative (local) location of the ontology document
	 * @param ontologyBaseURI
	 *            ontology's base URI
	 * @param ontologyRootURI
	 *            the root class of the ontology
	 * @param ontModelSpec
	 *            language and level of inference in model
	 */
	public JenaOntologyAccessor(String ontologyURI, String altOntologyURL,
			String ontologyBaseURI, String ontologyRootURI,
			OntModelSpec ontModelSpec) {
		setUpAccessor(ontologyURI, altOntologyURL, ontologyBaseURI,
				ontologyRootURI, ontModelSpec);
		parseHierarchy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#size()
	 */
	public int size() {
		int size = 0;
		ExtendedIterator it = model.listClasses().filterDrop(anonFilter);
		while (it.hasNext()) {
			it.next();
			size++;
		}
		logger.debug("Size of set is " + size);
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getShortestPath(simpack.api.IGraphNode,
	 *      simpack.api.IGraphNode)
	 */
	public double getShortestPath(IGraphNode nodeA, IGraphNode nodeB)
			throws InvalidElementException {

		if (!contains(nodeA)) {
			throw new InvalidElementException("Node " + nodeA.toString()
					+ " is invalid");
		} else if (!contains(nodeB)) {
			throw new InvalidElementException("Node " + nodeB.toString()
					+ " is invalid");
		}

		if (nodeA.toString().equals(nodeB.toString())) {
			return 0d;
		}

		IGraphNode mrca = getMostRecentCommonAncestor(nodeA, nodeB);

		int idMRCA = classURIsToIDsMap.get(mrca.toString());
		int idA = classURIsToIDsMap.get(nodeA.toString());
		int idB = classURIsToIDsMap.get(nodeB.toString());

		double dist = shortestDistanceToDescendantOrChildMatrix.getQuick(
				idMRCA, idA)
				+ shortestDistanceToDescendantOrChildMatrix.getQuick(idMRCA,
						idB);

		logger.debug("Shortest path between " + nodeA.toString() + " and "
				+ nodeB.toString() + " is " + dist);

		return dist;
	}

	/**
	 * Return the maximum depth in the ontology starting from the ontology's
	 * root concept down the branches to its leaves.
	 * 
	 * @return maximum depth
	 */
	public double getMaxDepth() {

		int ontologyRootID = classURIsToIDsMap.get(ontologyRootURI);
		double maxDepth = Integer.MIN_VALUE;
		int idMax = 0;

		logger.debug(longestDistanceToDescendantOrChildMatrix
				.viewRow(ontologyRootID));

		for (int i = 0; i < longestDistanceToDescendantOrChildMatrix.columns(); i++) {
			double temp = longestDistanceToDescendantOrChildMatrix.getQuick(
					ontologyRootID, i);
			if (temp > maxDepth) {
				maxDepth = temp;
				idMax = i;
			}
		}

		logger.debug("Maximum depth in ontology is " + maxDepth + " from "
				+ ontologyRootURI + " to " + idsToClassURIsMap.get(idMax));

		return maxDepth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getMaximumDirectedPathLength()
	 */
	public double getMaximumDirectedPathLength() {

		int ontologyRootID = classURIsToIDsMap.get(ontologyRootURI);

		logger.debug(longestDistanceToDescendantOrChildMatrix
				.viewRow(ontologyRootID));

		double[] lengths = new double[longestDistanceToDescendantOrChildMatrix
				.rows()];

		for (int i = 0; i < longestDistanceToDescendantOrChildMatrix.columns(); i++) {
			lengths[i] = longestDistanceToDescendantOrChildMatrix.getQuick(
					ontologyRootID, i);

		}

		Arrays.sort(lengths);

		double maxLength;
		// single-concept ontology (does that make sense at all???)
		if (lengths.length == 1) {
			maxLength = 0d;
			return 0d;
		} else {
			// at least two concepts
			maxLength = lengths[lengths.length - 1]
					+ lengths[lengths.length - 2];
		}

		logger.debug("Maximum path length in ontology is " + maxLength);
		return maxLength;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getRoot()
	 */
	public IGraphNode getRoot() {
		return new GraphNode(ontologyRootURI);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getMostRecentCommonAncestor(simpack.api.IGraphNode,
	 *      simpack.api.IGraphNode)
	 */
	public IGraphNode getMostRecentCommonAncestor(IGraphNode nodeA,
			IGraphNode nodeB) throws InvalidElementException {

		if (!contains(nodeA)) {
			throw new InvalidElementException("Node " + nodeA.toString()
					+ " is invalid");
		} else if (!contains(nodeB)) {
			throw new InvalidElementException("Node " + nodeB.toString()
					+ " is invalid");
		}

		Set<String> superClassesA = new TreeSet<String>();
		Set<String> superClassesB = new TreeSet<String>();
		// get classes from model
		String classAURI = nodeA.getUserObject().toString();
		String classBURI = nodeB.getUserObject().toString();
		final OntClass ontClassA = model.getOntClass(classAURI);
		final OntClass ontClassB = model.getOntClass(classBURI);

		// get an iterator over all the super classes of classA
		ExtendedIterator i = ontClassA.listSuperClasses()
				.filterDrop(anonFilter);

		// additional filter to 'stay' in the same ontology, i.e., this filter
		// removes super classes of the root of the ontology this accessor
		// provides access to

		// i = i.filterDrop(sameNamespaceFilter);
		i = i.filterDrop(RDFSchemaResource);
		i = i.filterDrop(OWLResource);

		// put superclasses of classA into set
		while (i.hasNext()) {
			OntClass next = (OntClass) i.next();
			// System.out.println("Adding " + next.getURI() + " to A's
			// supers.");
			superClassesA.add(next.getURI());
		}

		// same for classB
		i = ontClassB.listSuperClasses().filterDrop(anonFilter);

		// additional filter to 'stay' in the same ontology, i.e., this filter
		// removes super classes of the root of the ontology this accessor
		// provides access to

		// i = i.filterDrop(sameNamespaceFilter);
		i = i.filterDrop(RDFSchemaResource);
		i = i.filterDrop(OWLResource);

		while (i.hasNext()) {
			OntClass next = (OntClass) i.next();
			// System.out.println("Adding " + next.getURI() + " to B's
			// supers.");
			superClassesB.add(next.getURI());
		}

		// check if classA is super class of classB, i.e. IS most recent common
		// ancestor of classA and classB, and vice versa
		if (superClassesA.contains(classBURI)) {
			return new GraphNode(classBURI);
		}

		if (superClassesB.contains(classAURI)) {
			return new GraphNode(classAURI);
		}

		// retains only the elements in superClassesA that are contained in
		// superClassesB, i.e., superClassesA contains the common ancestors
		// afterwards
		superClassesA.retainAll(superClassesB);

		// determine MOST RECENT common ancestor of classA and classB
		int classAID = classURIsToIDsMap.get(classAURI);
		int classBID = classURIsToIDsMap.get(classBURI);
		int mrcaID = 0;
		double pathLength = Integer.MAX_VALUE;

		for (String commonAncestorURI : superClassesA) {
			logger.debug(commonAncestorURI);
			int commonAncestorID = classURIsToIDsMap.get(commonAncestorURI);
			double d = shortestDistanceToDescendantOrChildMatrix.getQuick(
					commonAncestorID, classAID)
					+ shortestDistanceToDescendantOrChildMatrix.getQuick(
							commonAncestorID, classBID);
			if (d < pathLength) {
				pathLength = d;
				mrcaID = commonAncestorID;
			}
		}

		if (logger.isDebugEnabled()) {
			OntClass mrca = model.getOntClass(idsToClassURIsMap.get(mrcaID));
			System.out.println("MRCA of "
					+ ontClassA.getModel().shortForm(ontClassA.getURI())
					+ " and "
					+ ontClassB.getModel().shortForm(ontClassB.getURI())
					+ " is " + mrca.getModel().shortForm(mrca.getURI()));
		}

		return new GraphNode(idsToClassURIsMap.get(mrcaID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getPredecessors(simpack.api.IGraphNode,
	 *      boolean)
	 */
	public Set<IGraphNode> getSuccessors(IGraphNode node, boolean direct)
			throws InvalidElementException {

		OntResource resource = model.getOntResource(node.toString());

		if (resource != null && resource.isClass()) {

			TreeSet<IGraphNode> descendants = new TreeSet<IGraphNode>(
					new NamedGraphNodeComparator());
			OntClass clazz = resource.asClass();

			ExtendedIterator it = clazz.listSubClasses(direct).filterDrop(
					anonFilter);

			while (it.hasNext()) {
				descendants.add(new GraphNode(((OntClass) it.next()).getURI()));
			}

			return descendants;
		} else {
			throw new InvalidElementException("Node " + node.toString()
					+ " is invalid");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IGraphAccessor#getSuccessors(simpack.api.IGraphNode,
	 *      boolean)
	 */
	public Set<IGraphNode> getPredecessors(IGraphNode node, boolean direct)
			throws InvalidElementException {

		OntResource resource = model.getOntResource(node.toString());

		if (resource != null && resource.isClass()) {

			TreeSet<IGraphNode> ancestors = new TreeSet<IGraphNode>(
					new NamedGraphNodeComparator());
			final OntClass clazz = resource.asClass();

			ExtendedIterator it = clazz.listSuperClasses(direct).filterDrop(
					anonFilter);

			it = it.filterDrop(sameNamespaceFilter);

			while (it.hasNext()) {
				OntClass superClass = (OntClass) it.next();
				ancestors.add(new GraphNode(superClass.getURI()));
			}

			return ancestors;
		} else {
			throw new InvalidElementException("Node " + node.toString()
					+ " is invalid");
		}
	}

	/**
	 * Sets up the Jena model of the ontology at the given URI.
	 * 
	 * @param ontologyURI
	 *            the ontology's URI
	 * @param altOntologyURL
	 *            an alternative (local) location of the ontology document
	 * @param ontologyBaseURI
	 *            base URI of the ontology
	 * @param ontModelSpec
	 *            language and inference level of the constructed model
	 */
	private void setUpAccessor(String ontologyURI, String altOntologyURL,
			String ontologyBaseURI, final String ontologyRootURI,
			OntModelSpec ontModelSpec) {
		model = ModelFactory.createOntologyModel(ontModelSpec);

		if (altOntologyURL != null) {
			model.getDocumentManager().addAltEntry(ontologyURI, altOntologyURL);
		}

		model.read(ontologyURI, ontologyBaseURI, null);

		this.ontologyRootURI = ontologyRootURI;

		anonFilter = new Filter() {
			public boolean accept(Object o) {
				return ((Resource) o).isAnon();
			}
		};

		sameNamespaceFilter = new Filter() {
			public boolean accept(Object o) {
				return !((Resource) o).getNameSpace().equals(
						model.getOntClass(ontologyRootURI).getNameSpace());
			}
		};

		RDFSchemaResource = new Filter() {
			public boolean accept(Object o) {
				return ((Resource) o).getNameSpace().equals(
						"http://www.w3.org/2000/01/rdf-schema#");
			}
		};

		OWLResource = new Filter() {
			public boolean accept(Object o) {
				return ((Resource) o).getNameSpace().equals(
						"http://www.w3.org/2002/07/owl#");
			}
		};

		calculateShortestAndLongestDistanceToDescendantOrChildMatrix();
	}

	/**
	 * Computes two matrices: One that holds shortest and one that holds longest
	 * distances between any two classes which are connected by parent
	 * (ancestor)--child (descendant) relastionships. The distance between
	 * classes which are not on parent--child paths is +MAX_INTEGER.
	 */
	private void calculateShortestAndLongestDistanceToDescendantOrChildMatrix() {
		TreeSet<String> classes = new TreeSet<String>();

		// get an iterator over all the classes in the model
		Iterator i = model.listNamedClasses().filterDrop(anonFilter);

		// temporarily store the classes URI
		while (i.hasNext()) {
			classes.add(((OntClass) i.next()).getURI());
		}

		// store (and sort) the class URIs into tree map
		// give each class an id
		classURIsToIDsMap = new TreeMap<String, Integer>();
		int id = 0;
		for (String ontClassURI : classes) {
			classURIsToIDsMap.put(ontClassURI, id++);
		}

		if (logger.isDebugEnabled()) {
			Set<String> keys = classURIsToIDsMap.keySet();
			for (String ontClassURI : keys) {
				int classID = classURIsToIDsMap.get(ontClassURI);
				System.out.println("[" + ontClassURI + "," + classID + "]");
			}
		}

		// construct reverse map (id to classURI mapping)
		idsToClassURIsMap = new TreeMap<Integer, String>();
		Set<String> keys = classURIsToIDsMap.keySet();
		for (String ontClassURI : keys) {
			int classID = classURIsToIDsMap.get(ontClassURI);
			idsToClassURIsMap.put(classID, ontClassURI);
		}

		if (logger.isDebugEnabled()) {
			Set<Integer> set = idsToClassURIsMap.keySet();
			for (Integer classID : set) {
				String ontClassURI = idsToClassURIsMap.get(classID);
				System.out.println("[" + classID + "," + ontClassURI + "]");
			}
		}

		// initialize distance matrices
		// note that rows are said to be 'parents', children/descendants occur
		// on columns
		shortestDistanceToDescendantOrChildMatrix = new SparseDoubleMatrix2D(
				classes.size(), classes.size());
		shortestDistanceToDescendantOrChildMatrix.viewPart(0, 0,
				shortestDistanceToDescendantOrChildMatrix.rows(),
				shortestDistanceToDescendantOrChildMatrix.columns()).assign(
				Integer.MAX_VALUE);

		longestDistanceToDescendantOrChildMatrix = new SparseDoubleMatrix2D(
				classes.size(), classes.size());
		longestDistanceToDescendantOrChildMatrix.viewPart(0, 0,
				longestDistanceToDescendantOrChildMatrix.rows(),
				longestDistanceToDescendantOrChildMatrix.columns()).assign(
				Integer.MIN_VALUE);

		// initialize diagonal elements to zero
		for (int row = 0; row < shortestDistanceToDescendantOrChildMatrix
				.rows(); row++) {
			for (int column = 0; column < shortestDistanceToDescendantOrChildMatrix
					.columns(); column++) {
				if (row == column) {
					shortestDistanceToDescendantOrChildMatrix.setQuick(row,
							column, 0);
					longestDistanceToDescendantOrChildMatrix.setQuick(row,
							column, 0);
				}
			}
		}

		Vector<Vector<Integer>> indexSet = new Vector<Vector<Integer>>();

		keys = classURIsToIDsMap.keySet();
		// first iteration, take each class of the model and check for children
		// store matrix indexes of classes having children in indexSet
		for (String ontClassURI : keys) {

			// get ID of parent class
			int parentID = classURIsToIDsMap.get(ontClassURI);

			OntClass clazz = model.getOntClass(ontClassURI);
			Iterator it = clazz.listSubClasses(true);

			while (it.hasNext()) {
				OntClass child = (OntClass) it.next();
				if (logger.isDebugEnabled()) {
					System.out.println("Class "
							+ clazz.getModel().shortForm(clazz.getURI())
							+ " has child "
							+ child.getModel().shortForm(child.getURI()));
				}

				// get ID of child class
				int childID = classURIsToIDsMap.get(child.getURI());

				// distance to child is 1 per definition
				shortestDistanceToDescendantOrChildMatrix.setQuick(parentID,
						childID, 1d);
				longestDistanceToDescendantOrChildMatrix.setQuick(parentID,
						childID, 1d);

				// store this pair of indexes of child in indexSet
				Vector<Integer> pair = new Vector<Integer>();
				pair.add(parentID);
				pair.add(childID);
				indexSet.add(pair);
			}
		}

		// process the classes having children, i.e., all non-leaf classes
		while (!indexSet.isEmpty()) {
			// set to store matrix indexes of new children
			Vector<Vector<Integer>> tempIndexSet = new Vector<Vector<Integer>>();

			for (Vector<Integer> pair : indexSet) {
				int ancestorID = pair.get(0);
				int descendantID = pair.get(1);

				// distance of ancestor to descendant
				double valueShortestPath = shortestDistanceToDescendantOrChildMatrix
						.getQuick(ancestorID, descendantID);

				double valueLongestPath = longestDistanceToDescendantOrChildMatrix
						.getQuick(ancestorID, descendantID);

				// get descendant from model
				String descendantURI = idsToClassURIsMap.get(descendantID);
				OntClass descendant = model.getOntClass(descendantURI);

				// iterator over children of descendant, i.e., over the
				// 'grand-descendants' of ancestor
				Iterator it = descendant.listSubClasses(true);
				while (it.hasNext()) {
					OntClass grandDescendant = (OntClass) it.next();
					int grandDescendantID = classURIsToIDsMap
							.get(grandDescendant.getURI());

					double tempShortestPath = shortestDistanceToDescendantOrChildMatrix
							.getQuick(ancestorID, grandDescendantID);

					double tempLongestPath = longestDistanceToDescendantOrChildMatrix
							.getQuick(ancestorID, grandDescendantID);

					// check if this path to descendant is shortest
					if (valueShortestPath + 1 < tempShortestPath) {
						shortestDistanceToDescendantOrChildMatrix.setQuick(
								ancestorID, grandDescendantID,
								valueShortestPath + 1);
					}

					// check if this path to descendant is longest
					if (valueLongestPath + 1 > tempLongestPath) {
						longestDistanceToDescendantOrChildMatrix.setQuick(
								ancestorID, grandDescendantID,
								valueLongestPath + 1);
					}

					// store new indexes to be processed later
					Vector<Integer> p = new Vector<Integer>();
					p.add(ancestorID);
					p.add(grandDescendantID);
					tempIndexSet.add(p);
				}
			}

			indexSet = tempIndexSet;
		}
	}

	private void parseClassDescription(OntClass c, String parent) {
		if (c.isRestriction()) {
		} else {
			if (!c.isAnon()) {
				if (parent != null) {
					setEdge(new GraphNode(parent), new GraphNode(c.getURI()));
				} else {
					// ontology root class
					// add root
					addNode(new GraphNode(c.getURI()));
				}
			} else {

			}
		}
	}

	private void parseClass(OntClass cls, List<OntClass> occurs, String parent) {
		parseClassDescription(cls, parent);
		// recurse to the next level down
		if (cls.canAs(OntClass.class) && !occurs.contains(cls)) {
			for (Iterator i = cls.listSubClasses(true); i.hasNext();) {
				OntClass sub = (OntClass) i.next();
				String newParent = cls.getURI();
				// we push this expression on the occurs list before we recurse
				occurs.add(cls);
				parseClass(sub, occurs, newParent);
				occurs.remove(cls);
			}
		}
	}

	private void parseHierarchy() {
		Iterator i = model.listHierarchyRootClasses().filterDrop(anonFilter);

		while (i.hasNext()) {
			OntClass root = (OntClass) i.next();
			parseClass(root, new ArrayList<OntClass>(), null);
		}
	}
}