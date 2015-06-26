/*
 * $Id: ScaledShortestPathTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.graph;

import java.io.File;

import junit.framework.TestCase;
import simpack.accessor.graph.JenaOntologyAccessor;
import simpack.exception.InvalidElementException;
import simpack.measure.graph.ScaledShortestPath;
import simpack.util.graph.GraphNode;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: ScaledShortestPathTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ScaledShortestPathTest extends TestCase {

	private String ontologyURI = "http://127.0.0.1/ontology/simplified_sumo.owl";

	private String ontologyBaseURI = "http://127.0.0.1/ontology/simplified_sumo.owl#";

	private String ontologyRootURI = "http://127.0.0.1/ontology/simplified_sumo.owl#SUMORoot";

	private String altOntologyURL = "file:ontology" + File.separator
			+ "simplified_sumo.owl";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;

	public void testScaledShortestPath() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#HoofedMammal";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#BodyPart";

		ScaledShortestPath ssp = null;
		try {
			ssp = new ScaledShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(ssp);
		assertTrue(ssp.calculate());
		assertTrue(ssp.isCalculated());
		assertEquals(ssp.getSimilarity(), new Double(-1d
				* Math.log(8d / (2d * 12d))));
	}

	public void testScaledShortestPathInvalidNodeException() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#HoofedMammalalalal";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#BodyPart";

		try {
			ScaledShortestPath ssp = new ScaledShortestPath(
					new JenaOntologyAccessor(ontologyURI, altOntologyURL,
							ontologyBaseURI, ontologyRootURI, ontModelSpec),
					new GraphNode(new String(ontClassA)), new GraphNode(
							new String(ontClassB)));
			fail("Should raise an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
	}

	public void testScaledShortestPathMultiplePathPossible() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#IndependentState";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Industry";

		ScaledShortestPath ssp = null;
		try {
			ssp = new ScaledShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(ssp);
		assertTrue(ssp.calculate());
		assertTrue(ssp.isCalculated());
		assertEquals(ssp.getSimilarity(), new Double(-1d
				* Math.log(6d / (2d * 12d))));
	}

	public void testScaledShortestPathSameNode() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#IndependentState";
		String ontClassB = ontClassA;

		ScaledShortestPath ssp = null;
		try {
			ssp = new ScaledShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(ssp);
		assertTrue(ssp.calculate());
		assertTrue(ssp.isCalculated());
		assertEquals(ssp.getSimilarity(), new Double(0));
	}

	public void testScaledShortestPathOneIsAncestorOfOther() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#Vertebrate";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Organism";

		ScaledShortestPath ssp = null;
		try {
			ssp = new ScaledShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(ssp);
		assertTrue(ssp.calculate());
		assertTrue(ssp.isCalculated());
		assertEquals(ssp.getSimilarity(), new Double(-1d
				* Math.log(2d / (2d * 12d))));
	}

	public void testScaledShortestPathOneIsRootOfOntology() {
		String ontClassA = ontologyRootURI;
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Object";

		ScaledShortestPath ssp = null;
		try {
			ssp = new ScaledShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(ssp);
		assertTrue(ssp.calculate());
		assertTrue(ssp.isCalculated());
		assertEquals(ssp.getSimilarity(), new Double(-1d
				* Math.log(3d / (2d * 12d))));
	}

	public void testScaledShortestPathOneIsRootOfOntologyMutliplePathsPossible() {
		String ontClassA = ontologyRootURI;
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Transaction";

		ScaledShortestPath ssp = null;
		try {
			ssp = new ScaledShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(ssp);
		assertTrue(ssp.calculate());
		assertTrue(ssp.isCalculated());
		assertEquals(ssp.getSimilarity(), new Double(-1d
				* Math.log(2d / (2d * 12d))));
	}
}