/*
 * $Id: JenaOntologyTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import java.util.TreeSet;

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.accessor.graph.JenaOntologyAccessor;
import simpack.measure.graph.SubgraphIsomorphism;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: JenaOntologyTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JenaOntologyTest extends TestCase {

	public static Logger logger = LogManager.getLogger(JenaOntologyTest.class);

	private JenaOntologyAccessor sumoSub, sumoRealignSub, sumoRenameSub,
			swetoSub;

	private String ontologyURI1 = "http://127.0.0.1/ontology/simplified_sumo_owl_test.owl";

	private String ontologyURI1_realign = "http://127.0.0.1/ontology/simplified_sumo_owl_test_realign.owl";

	private String ontologyURI1_rename = "http://127.0.0.1/ontology/simplified_sumo_owl_test_rename.owl";

	private String ontologyBaseURI1 = "http://127.0.0.1/ontology/simplified_sumo.owl#";

	private String ontologyRootURI1 = ontologyBaseURI1 + "SUMORoot";

	private String altOntologyURL1 = "file:ontology" + File.separator
			+ "simplified_sumo.owl";

	private String ontologyURI2 = "http://127.0.0.1/ontology/testbed_v1_4_new_owl_test.owl";

	private String ontologyBaseURI2 = "http://lsdis.cs.uga.edu/proj/semdis/testbed/#";

	private String ontologyRootURI2 = ontologyBaseURI2 + "SWETORoot";

	private String altOntologyURL2 = "file:ontology" + File.separator
			+ "testbed_v1_4_new_owl_test.owl";

	public void setUp() {
		sumoSub = new JenaOntologyAccessor(ontologyURI1, altOntologyURL1,
				ontologyBaseURI1, ontologyRootURI1,
				OntModelSpec.OWL_MEM_RDFS_INF);
		sumoRealignSub = new JenaOntologyAccessor(ontologyURI1_realign,
				altOntologyURL1, ontologyBaseURI1, ontologyRootURI1,
				OntModelSpec.OWL_MEM_RDFS_INF);
		sumoRenameSub = new JenaOntologyAccessor(ontologyURI1_rename,
				altOntologyURL1, ontologyBaseURI1, ontologyRootURI1,
				OntModelSpec.OWL_MEM_RDFS_INF);
		swetoSub = new JenaOntologyAccessor(ontologyURI2, altOntologyURL2,
				ontologyBaseURI2, ontologyRootURI2,
				OntModelSpec.OWL_MEM_RDFS_INF);
		assertNotNull(sumoSub);
		assertNotNull(sumoRealignSub);
		assertNotNull(sumoRenameSub);
		assertNotNull(swetoSub);
	}

	public void testSimilarity1() {
		SubgraphIsomorphism sumoSweto = new SubgraphIsomorphism(sumoSub,
				swetoSub,
				SubgraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE, 7,
				SubgraphIsomorphism.DEFAULT_LABEL_WEIGHT,
				SubgraphIsomorphism.DEFAULT_STRUCTURE_WEIGHT,
				SubgraphIsomorphism.DEFAULT_DENOMINATOR, true);

		assertNotNull(sumoSweto);
		assertTrue(sumoSweto.calculate());
		assertTrue(sumoSweto.isCalculated());
		assertEquals(sumoSweto.getSimilarity(), 0.20728261352858296);

		if (logger.isDebugEnabled()) {
			System.out.println("subgraph similarity: "
					+ sumoSweto.getSimilarity());
			TreeSet<String> cliqueList = sumoSweto.getCliqueList();
			for (String clique : cliqueList) {
				System.out.println("clique: " + clique);
			}
		}
	}

	public void testSimilarity2() {
		SubgraphIsomorphism sumoSweto = new SubgraphIsomorphism(sumoRenameSub,
				swetoSub,
				SubgraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE, 7,
				SubgraphIsomorphism.DEFAULT_LABEL_WEIGHT,
				SubgraphIsomorphism.DEFAULT_STRUCTURE_WEIGHT,
				SubgraphIsomorphism.DEFAULT_DENOMINATOR, true);

		assertNotNull(sumoSweto);
		assertTrue(sumoSweto.calculate());
		assertTrue(sumoSweto.isCalculated());
		assertEquals(sumoSweto.getSimilarity(), 0.20728261352858296);

		if (logger.isDebugEnabled()) {
			System.out.println("subgraph similarity: "
					+ sumoSweto.getSimilarity());
			TreeSet<String> cliqueList = sumoSweto.getCliqueList();
			for (String clique : cliqueList) {
				System.out.println("clique: " + clique);
			}
		}
	}

	public void testSimilarity3() {
		SubgraphIsomorphism sumoRealignSweto = new SubgraphIsomorphism(
				sumoRealignSub, swetoSub,
				SubgraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE, 7,
				SubgraphIsomorphism.DEFAULT_LABEL_WEIGHT,
				SubgraphIsomorphism.DEFAULT_STRUCTURE_WEIGHT,
				SubgraphIsomorphism.DEFAULT_DENOMINATOR, true);

		assertNotNull(sumoRealignSweto);
		assertTrue(sumoRealignSweto.calculate());
		assertTrue(sumoRealignSweto.isCalculated());
		assertEquals(sumoRealignSweto.getSimilarity(), 0.21015793604876457);

		if (logger.isDebugEnabled()) {
			System.out.println("subgraph similarity: "
					+ sumoRealignSweto.getSimilarity());
			TreeSet<String> cliqueList = sumoRealignSweto.getCliqueList();
			for (String clique : cliqueList) {
				System.out.println("clique: " + clique);
			}
		}
	}
}