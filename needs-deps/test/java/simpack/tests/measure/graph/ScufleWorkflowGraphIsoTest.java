/*
 * $Id: ScufleWorkflowGraphIsoTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.graph.GraphIsomorphism;
import simpack.measure.sequence.Levenshtein;

/**
 * @author Christoph Kiefer
 * @version $Id: ScufleWorkflowGraphIsoTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ScufleWorkflowGraphIsoTest extends TestCase {

	private String wf1 = "file:workflow" + File.separator
			+ "FetchDailyDilbertComic.xml";

	private String wf2 = wf1;

	private String wf3 = "file:workflow" + File.separator
			+ "FetchDailyDilbertComic-LabelChanged.xml";

	private ScuflXMLGraphAccessor scuf_dilbert_sub1, scuf_dilbert_sub2,
			scuf_dilbert_sub3;

	private double labelSimSum, labelSimSum2;

	public void setUp() {
		scuf_dilbert_sub1 = new ScuflXMLGraphAccessor(wf1);
		scuf_dilbert_sub2 = new ScuflXMLGraphAccessor(wf2);
		scuf_dilbert_sub3 = new ScuflXMLGraphAccessor(wf3);
		assertNotNull(scuf_dilbert_sub1);
		assertNotNull(scuf_dilbert_sub2);
		assertNotNull(scuf_dilbert_sub3);
		setUpGraphStuff();
	}

	/*
	 * For illustration purposes, please refer to the workflow directory in the
	 * SimPack root directory that provides some sample xml workflow files along
	 * with some graphics.
	 */
	public void testSimilarity() {
		GraphIsomorphism dilbert_gbrows = new GraphIsomorphism(
				scuf_dilbert_sub1, scuf_dilbert_sub2);

		double sim = 1d;
		// calculate similarity by measure
		assertNotNull(dilbert_gbrows.calculate());
		assertNotNull(dilbert_gbrows.isCalculated());
		assertEquals(dilbert_gbrows.getSimilarity(), sim);

		// TreeSet<String> cliqueList = new TreeSet<String>();
		// cliqueList = dilbert_gbrows.getCliqueList();
		// Iterator it = cliqueList.iterator();
		// while (it.hasNext()) {
		// System.out.println("clique: " + it.next());
		// }
	}

	public void testSimilarityLabelChanged() {
		GraphIsomorphism dilbert = new GraphIsomorphism(scuf_dilbert_sub1,
				scuf_dilbert_sub3);
		// maximum clique size
		double cliqueSize = 7d;
		// calculate similarity by measure
		assertNotNull(dilbert.calculate());
		assertNotNull(dilbert.isCalculated());
		assertEquals(dilbert.getSimilarity(), labelSimSum2 / cliqueSize);

		// TreeSet<String> cliqueList = new TreeSet<String>();
		// cliqueList = dilbert.getCliqueList();
		// Iterator it = cliqueList.iterator();
		// while (it.hasNext()) {
		// System.out.println("clique: " + it.next());
		// }
	}

	private void setUpGraphStuff() {
		// compute label similarities of mapped vertices of maximum clique
		labelSimSum = 0d;
		labelSimSum2 = 0d;
		Levenshtein<String> lev = new Levenshtein<String>(new StringAccessor(
				"dilbertURL"), new StringAccessor("dilbertURL"));
		labelSimSum += lev.getSimilarity();

		lev = new Levenshtein<String>(new StringAccessor("getPage"),
				new StringAccessor("getPage"));
		labelSimSum += lev.getSimilarity();

		lev = new Levenshtein<String>(new StringAccessor("getImageLinks"),
				new StringAccessor("getImageLinks"));
		labelSimSum += lev.getSimilarity();

		lev = new Levenshtein<String>(new StringAccessor("comicURLRegex"),
				new StringAccessor("comicURLRegex"));
		labelSimSum += lev.getSimilarity();

		lev = new Levenshtein<String>(new StringAccessor("findComicURL"),
				new StringAccessor("findComicURL"));
		labelSimSum += lev.getSimilarity();

		lev = new Levenshtein<String>(new StringAccessor("todaysDilbert"),
				new StringAccessor("todaysDilbert"));
		labelSimSum += lev.getSimilarity();

		lev = new Levenshtein<String>(new StringAccessor("getComicStrip"),
				new StringAccessor("getComicStrip"));
		labelSimSum += lev.getSimilarity();

		labelSimSum2 = labelSimSum - lev.getSimilarity();

		lev = new Levenshtein<String>(
				new StringAccessor("getDailyComicStrip"), new StringAccessor(
						"getComicStrip"));

		labelSimSum2 += lev.getSimilarity();
	}
}