/*
 * $Id: ScuflXMLGraphAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.net.URL;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.embl.ebi.escience.scufl.DataConstraint;
import org.embl.ebi.escience.scufl.Port;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.parser.XScuflParser;

import simpack.api.IGraphNode;
import simpack.api.impl.AbstractGraphAccessor;
import simpack.util.graph.GraphNode;
import ch.unibern.graphmatching2.Graph;
import ch.unibern.graphmatching2.Vertex;

/**
 * This class provides access to the graphical representation of a Scufle
 * workflow.
 * 
 * @author Antoon Goderis
 * @author Daniel Baggenstos
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ScuflXMLGraphAccessor extends AbstractGraphAccessor {

	public static Logger logger = LogManager
			.getLogger(ScuflXMLGraphAccessor.class);

	public ScuflXMLGraphAccessor(String filename) {
		BuildServiceGraph(filename);
	}

	public ScuflModel loadScufl(String WorkflowLocation) {
		// taken from TestScuflParser
		ScuflModel mymodel = new ScuflModel();
		mymodel.setEventStatus(false);
		try {
			// Load workflow in off-line mode
			mymodel.setOffline(true);
			// System.out.println("offline");

			// Create a new ScuflModel and add the trivial listener
			// to print out all events on it
			// ScuflModel mymodel = new ScuflModel();
			// mymodel.addListener(new ScuflModelEventPrinter(null));

			// ClassLoader loader =
			// Thread.currentThread().getContextClassLoader();
			// URL location = loader.getResource(WorkflowLocation);

			URL location = new URL(WorkflowLocation);

			logger.debug("Loading definition from : " + location.toString());
			// Use it to populate the model, names do not have
			// prefixes applied to them.
			XScuflParser.populate(location.openStream(), mymodel, null);

		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return mymodel;
	}

	/**
	 * Build up a graph representation suitable for the GUB tool, starting from
	 * a SCUFL representation. SCUFL talks about models, processors and
	 * DataConstraints, GUB takes graphs, vertices (nodes) and edges
	 * 
	 * @param WorkflowLocation
	 *            location of the workflow
	 * 
	 * @return a graph encoding of the workflow
	 */
	public Graph BuildServiceGraph(String WorkflowLocation) {
		ScuflModel model;
		Graph graphmodel = new Graph();
		try {
			// Load the workflow in the Scufl skeleton model
			model = loadScufl(WorkflowLocation);
			// Create a graph for the GUB package
			// Assign the vertex labels
			logger.debug("\n" + "Nodes\n");
			Processor[] myprocessors;
			Port[] mysourceports;
			Port[] mysinkports;

			// 1. Initialize a vertexlist with the size of the sum of all
			// source, sink and intermediate processors
			myprocessors = model.getProcessors();
			Processor mysource = model.getWorkflowSourceProcessor();
			mysourceports = mysource.getOutputPorts();
			Processor mysink = model.getWorkflowSinkProcessor();
			mysinkports = mysink.getInputPorts();
			int graphsize = myprocessors.length + mysourceports.length
					+ mysinkports.length;
			Vertex[] v = new Vertex[graphsize];

			// 2. Assign processor names to vertices
			for (int node = 0; node < myprocessors.length; node++) {
				logger.debug(myprocessors[node].getName().toString()
						.toLowerCase());
				v[node] = new Vertex(myprocessors[node].getName().toString()
						.toLowerCase());
			}
			for (int node = 0; node < mysourceports.length; node++) {
				logger.debug(mysourceports[node].toString().toLowerCase());
				v[myprocessors.length + node] = new Vertex(mysourceports[node]
						.getName().toString().toLowerCase());
			}
			for (int node = 0; node < mysinkports.length; node++) {
				logger.debug(mysinkports[node].toString().toLowerCase());
				v[myprocessors.length + mysourceports.length + node] = new Vertex(
						mysinkports[node].getName().toString().toLowerCase());
			}
			for (int vert = 0; vert < graphsize; vert++) {
				graphmodel.addVertex(v[vert]);
			}

			// Print the vertex labels
			if (logger.isDebugEnabled()) {
				for (int j = 0; j < graphsize; j++) {
					System.out.println("v[" + j + "].getLabel()");
					System.out.println(v[j].getLabel());
				}
			}

			// Assign the edge labels
			logger.debug("\n" + "Links\n");

			// Initialize list of DataConstraints
			DataConstraint[] mylinks = model.getDataConstraints();

			for (int link = 0; link < mylinks.length; link++) {
				// Print to screen the source of the DataConstraint
				if (logger.isDebugEnabled()) {
					System.out.println("\n link = " + link);
					if (mylinks[link].getSource().getProcessor().getName() == "SCUFL_INTERNAL_SOURCEPORTS") {
						System.out.println("mylinks[" + link
								+ "].getSource().getName()");
						System.out.println(mylinks[link].getSource().getName());

					} else {
						System.out.println("mylinks[" + link
								+ "].getSource().getProcessor().toString()");
						System.out.println(mylinks[link].getSource()
								.getProcessor().getName());
					}
				}

				String source = "";
				if (mylinks[link].getSource().getProcessor().getName() == "SCUFL_INTERNAL_SOURCEPORTS") {
					source = mylinks[link].getSource().getName();
				} else {
					source = mylinks[link].getSource().getProcessor().getName();
				}

				// Test for each vertex whether it fits the source of the
				// DataConstraint.
				for (int j = 0; j < graphsize; j++) {
					logger.debug("j = " + j);
					// Search within the Source Processor for possible matches
					// with the vertex
					// If the source of a link is equal to a label of the vertex
					if (mylinks[link].getSource().getProcessor().getName() == "SCUFL_INTERNAL_SOURCEPORTS") {
						logger
								.debug("mylinks["
										+ link
										+ "].getSource().getProcessor().toString() == SCUFL_INTERNAL_SOURCEPORTS");
						// Get the matching sinks for that source, by selecting
						// all links which have that sourcename
						// (Processors can have multiple links)
						logger.debug("l = " + link);
						if (v[j].getLabel() == mylinks[link].getSource()
								.getName()) {
							logger.debug("v[" + j + "].getLabel() == mylinks["
									+ link + "].getSource().getName()");
							// Find the label of the vertex corresponding to the
							// sinks and use that
							// for establishing a new edge
							assignEdge(graphsize, v, mylinks, link, j);
						}
					}

					// Search the other Processors for possible matches with the
					// vertex
					// If the source of a link is equal to a label of the vertex
					if (mylinks[link].getSource().getProcessor().getName() == v[j]
							.getLabel()) {
						logger
								.debug("mylinks["
										+ link
										+ "].getSource().getProcessor().toString() == v["
										+ j + "].getLabel()");
						// Get the matching sinks for that source, by selecting
						// all links which have that sourcename
						// (Processors can have multiple links)
						logger.debug("l = " + link);
						if (v[j].getLabel() == mylinks[link].getSource()
								.getProcessor().getName()) {
							logger.debug("v[" + j + "].getLabel() == mylinks["
									+ link + "].getSource().toString()");
							// Find the label of the vertex corresponding to the
							// sinks and use that
							// for establishing a new edge
							assignEdge(graphsize, v, mylinks, link, j);
						}
					}
				}

				// Print some test data
				if (logger.isDebugEnabled()) {
					if (mylinks[link].getSink().getProcessor().toString() == "SCUFL_INTERNAL_SINKPORTS") {
						// Port [] mylinksink =
						// mylinks[link].getSink().getProcessor().getInputPorts();
						System.out.println("mylinks[" + link
								+ "].getSink().toString()");
						System.out.println(mylinks[link].getSink().toString());
						// System.out.println("mylinksink[link].toString()");
						// System.out.println(mylinksink[link].toString());
					} else {
						System.out.println("mylinks[" + link
								+ "].getSink().getProcessor().toString()");
						System.out.println(mylinks[link].getSink()
								.getProcessor().toString());
					}
				}

				String target = "";
				if (mylinks[link].getSink().getProcessor().toString() == "SCUFL_INTERNAL_SINKPORTS") {
					// Port [] mylinksink =
					// mylinks[link].getSink().getProcessor().getInputPorts();
					target = mylinks[link].getSink().toString();
				} else {
					target = mylinks[link].getSink().getProcessor().toString();
				}
				setEdge(new GraphNode(source), new GraphNode(target));

			}

			if (logger.isDebugEnabled()) {
				System.out.println("************all nodes**************");
				for (IGraphNode n : getNodeSet()) {
					System.out.println("node: " + n);
					System.out.println("pre: " + n.getPredecessorSet());
					System.out.println("suc: " + n.getSuccessorSet());
				}
			}

		} catch (Exception exp) {
			exp.printStackTrace();
		}

		return graphmodel;

	}

	public Vertex[] assignEdge(int graphsize, Vertex[] inputVertex,
			DataConstraint[] inputDC, int link, int j) {
		// find the label of the vertex corresponding to the sinks linked to
		// this source
		for (int z = 0; z < graphsize; z++) {
			if (logger.isDebugEnabled()) {
				System.out.println("v[z].getLabel()");
				System.out.println(inputVertex[z].getLabel());
				System.out
						.println("mylinks[link].getSink().getProcessor().getName()");
				System.out.println(inputDC[link].getSink().getProcessor()
						.getName());
			}
			if (inputVertex[z].getLabel() == inputDC[link].getSink()
					.getProcessor().getName()) {
				// add this vertex to the source vertex
				inputVertex[j].addEdge(inputVertex[z]);
			} else if (inputDC[link].getSink().getProcessor().toString() == "SCUFL_INTERNAL_SINKPORTS"
					&& inputVertex[z].getLabel() == inputDC[link].getSink()
							.toString()) {
				inputVertex[j].addEdge(inputVertex[z]);
			}
			logger.debug("z = " + z);
		}
		return inputVertex;
	}

	public double getShortestPath(IGraphNode nodeA, IGraphNode nodeB) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMaximumDirectedPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Set<IGraphNode> getPredecessors(IGraphNode node, boolean direct) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IGraphNode> getSuccessors(IGraphNode node, boolean direct) {
		// TODO Auto-generated method stub
		return null;
	}

	public IGraphNode getMostRecentCommonAncestor(IGraphNode nodeA,
			IGraphNode nodeB) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMaxDepth() {
		// TODO Auto-generated method stub
		return 0;
	}
}