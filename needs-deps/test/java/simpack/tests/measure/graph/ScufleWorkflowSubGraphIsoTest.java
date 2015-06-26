/*
 * $Id: ScufleWorkflowSubGraphIsoTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.accessor.graph.ScuflXMLGraphAccessor;
import simpack.accessor.string.StringAccessor;
import simpack.measure.graph.SubgraphIsomorphism;
import simpack.measure.sequence.Levenshtein;

/**
 * @author Christoph Kiefer
 * @version $Id: ScufleWorkflowSubGraphIsoTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ScufleWorkflowSubGraphIsoTest extends TestCase {

	private String wf1 = "file:workflow" + File.separator
			+ "FetchDailyDilbertComic.xml";

	private String wf2 = "file:workflow" + File.separator
			+ "FetchGbrowseImageSelectTracks.xml";

	private ScuflXMLGraphAccessor scuf_dilbert_sub, scuf_gbrows_sub;

	private int v1, v2;

	private double labelSimSum;

	private double wS, wL;

	private int maximumCliqueSize;

	public void setUp() {
		scuf_dilbert_sub = new ScuflXMLGraphAccessor(wf1);
		scuf_gbrows_sub = new ScuflXMLGraphAccessor(wf2);
		assertNotNull(scuf_dilbert_sub);
		assertNotNull(scuf_gbrows_sub);
		setUpGraphStuff();
	}

	//
	// Should find the following clique:
	//
	// comicURLRegex:detailedViewURLRegex
	// findComicURL:finddetailedViewImage
	// getComicStrip:getDetailedView
	// getImageLinks:getImageLinks
	// getPage:GetPage
	// todaysDilbert:gbrowseImage
	//
	// For illustration purposes, please refer to the workflow directory in the
	// SimPack root directory that provides some sample xml workflow files along
	// with some graphics.
	//
	public void testSimilarity() {
		SubgraphIsomorphism dilbert_gbrows = new SubgraphIsomorphism(
				scuf_dilbert_sub, scuf_gbrows_sub);

		// compute label similarity
		double labelSim = labelSimSum / ((double) (v1 + v2) / 2d);
		// compute structure similarity
		double structureSim = (maximumCliqueSize) / ((double) (v1 + v2) / 2d);
		// compute overall similarity
		double sim = wL * labelSim + wS * structureSim;
		// calculate similarity by measure
		dilbert_gbrows.calculate();
		assertEquals(dilbert_gbrows.getSimilarity(), sim);

		// TreeSet<String> cliqueList = new TreeSet<String>();
		// cliqueList = dilbert_gbrows.getCliqueList();
		// Iterator it = cliqueList.iterator();
		// while (it.hasNext()) {
		// System.out.println("clique: " + it.next());
		// }
	}

	public void testSimilarityGroupNodes() {
		SubgraphIsomorphism dilbert_gbrows = new SubgraphIsomorphism(
				scuf_dilbert_sub, scuf_gbrows_sub,
				SubgraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE,
				SubgraphIsomorphism.DEFAULT_MIN_CLIQUE_SIZE,
				SubgraphIsomorphism.DEFAULT_LABEL_WEIGHT,
				SubgraphIsomorphism.DEFAULT_STRUCTURE_WEIGHT,
				SubgraphIsomorphism.DEFAULT_DENOMINATOR, true);

		// compute label similarity
		double labelSim = labelSimSum / ((double) (v1 + v2) / 2d);
		// compute structure similarity
		double structureSim = (maximumCliqueSize) / ((double) (v1 + v2) / 2d);
		// compute overall similarity
		double sim = wL * labelSim + wS * structureSim;
		// calculate similarity by measure
		dilbert_gbrows.calculate();
		assertEquals(dilbert_gbrows.getSimilarity(), sim);
	}

	public void testSimilarityNormalizeWRTSmallerGraph() {
		SubgraphIsomorphism dilbert_gbrows = new SubgraphIsomorphism(
				scuf_dilbert_sub, scuf_gbrows_sub,
				SubgraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE,
				SubgraphIsomorphism.DEFAULT_MIN_CLIQUE_SIZE,
				SubgraphIsomorphism.DEFAULT_LABEL_WEIGHT,
				SubgraphIsomorphism.DEFAULT_STRUCTURE_WEIGHT, "small", false);

		// compute label similarity
		double labelSim = labelSimSum / ((double) v1);
		// compute structure similarity
		double structureSim = (maximumCliqueSize) / ((double) v1);
		// compute overall similarity
		double sim = wL * labelSim + wS * structureSim;
		// calculate similarity by measure
		dilbert_gbrows.calculate();
		assertEquals(dilbert_gbrows.getSimilarity(), sim);
	}

	public void testSimilarityNormalizeWRTBiggerGraph() {
		SubgraphIsomorphism dilbert_gbrows = new SubgraphIsomorphism(
				scuf_dilbert_sub, scuf_gbrows_sub,
				SubgraphIsomorphism.DEFAULT_NODE_LABEL_SIMILARITY_MEASURE,
				SubgraphIsomorphism.DEFAULT_MIN_CLIQUE_SIZE,
				SubgraphIsomorphism.DEFAULT_LABEL_WEIGHT,
				SubgraphIsomorphism.DEFAULT_STRUCTURE_WEIGHT, "big", false);

		// compute label similarity
		double labelSim = labelSimSum / ((double) v2);
		// compute structure similarity
		double structureSim = (maximumCliqueSize) / ((double) v2);
		// compute overall similarity
		double sim = wL * labelSim + wS * structureSim;
		// calculate similarity by measure
		dilbert_gbrows.calculate();
		assertEquals(dilbert_gbrows.getSimilarity(), sim);
	}

	private void setUpGraphStuff() {
		// weights of structure and label similarities
		wL = 0.5d;
		wS = 0.5d;
		// graph sizes
		v1 = 7;
		v2 = 17;
		// maximum clique size
		maximumCliqueSize = 6;
		// compute label similarities of mapped vertices of maximum clique
		labelSimSum = 0d;

		Levenshtein<String> lev = new Levenshtein<String>(new StringAccessor(
				"comicURLRegex"), new StringAccessor("detailedViewURLRegex"));
		labelSimSum += lev.getSimilarity();
		lev = new Levenshtein<String>(new StringAccessor("findComicURL"),
				new StringAccessor("finddetailedViewImage"));
		labelSimSum += lev.getSimilarity();
		lev = new Levenshtein<String>(new StringAccessor("getComicStrip"),
				new StringAccessor("getDetailedView"));
		labelSimSum += lev.getSimilarity();
		lev = new Levenshtein<String>(new StringAccessor("getImageLinks"),
				new StringAccessor("getImageLinks"));
		labelSimSum += lev.getSimilarity();
		lev = new Levenshtein<String>(new StringAccessor("getPage"),
				new StringAccessor("GetPage"));
		labelSimSum += lev.getSimilarity();
		lev = new Levenshtein<String>(new StringAccessor("todaysDilbert"),
				new StringAccessor("gbrowseImage"));
		labelSimSum += lev.getSimilarity();
	}
}