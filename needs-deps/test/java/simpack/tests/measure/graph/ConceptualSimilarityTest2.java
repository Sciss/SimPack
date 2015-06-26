/*
 * $Id: ConceptualSimilarityTest2.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.graph.ConceptualSimilarity;
import simpack.util.graph.GraphNode;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: ConceptualSimilarityTest2.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ConceptualSimilarityTest2 extends TestCase {

	private String ontologyURI = "http://www.area/owl";

	private String ontologyBaseURI = "http://www.area/owl#";

	private String ontologyRootURI = "http://www.semco/owl#SemcoRoot";

	private String altOntologyURL = "file:ontology" + File.separator
			+ "semco-ws.owl";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;

	private String ontClassA = "http://www.area/owl#Village";
	private String ontClassB = "http://www.area/owl#Town";
	private String ontClassC = "http://www.area/owl#Address";
	private String ontClassD = "http://www.meteo/owl#TotalPrecipitation";
	private String ontClassE = "http://www.semco/owl#SemcoRoot";

	public void testSimilarity() {
		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));

			conSim.calculate();
			assertEquals(conSim.isCalculated(), true);
			
			double sim = conSim.getSimilarity();
			assertEquals(sim, (2d * 3d) / (2d + (2d * 3d)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}

	public void testSimilarity2() {
		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassC)));

			conSim.calculate();
			assertEquals(conSim.isCalculated(), true);
			
			double sim = conSim.getSimilarity();
			assertEquals(sim, (1d) / (6d + (1d)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}
	
	public void testSimilarity3() {
		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassD)));

			conSim.calculate();
			assertEquals(conSim.isCalculated(), true);
			
			double sim = conSim.getSimilarity();
			assertEquals(sim, (1d) / (9d + (1d)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}
	
	public void testSimilarity4() {
		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassE)));

			conSim.calculate();
			assertEquals(conSim.isCalculated(), true);
			
			double sim = conSim.getSimilarity();
			assertEquals(sim, (1d) / (4d + (1d)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}
}