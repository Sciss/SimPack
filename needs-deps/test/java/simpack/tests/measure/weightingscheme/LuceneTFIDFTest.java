/*
 * $Id: LuceneTFIDFTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.weightingscheme;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import simpack.accessor.list.LuceneIndexAccessor;
import simpack.exception.InvalidElementException;
import simpack.exception.InvalidVectorSizeException;
import simpack.measure.vector.Cosine;
import simpack.measure.weightingscheme.LuceneTFIDF;
import simpack.util.Vector;
import simpack.util.corpus.Indexer;

/**
 * @author Christoph Kiefer
 * @version $Id: LuceneTFIDFTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class LuceneTFIDFTest extends TestCase {

	// paths need to be UNIX style (document titles are indexed this way)
	private String file1 = "data/corpus/SWEET_1998734.txt";

	private String file2 = "data/corpus/SWEET_1998395.txt";

	private String file3 = "data/corpus/SWEET_2096309.txt";

	private String index = "data" + File.separator + "index";

	/**
	 * 1/ The contents of the three indexed documents:
	 * <ol>
	 * <li>(SWEET_1998395.txt) Auditory scene analysis based on time-frequency
	 * integration of shared FM and AM (I): Lagrange differential features and
	 * frequency-axis integration. </li>
	 * <li>(SWEET_1998734.txt) Auditory scene analysis based on time-frequency
	 * integration of shared FM and AM (II): Optimum time-domain integration and
	 * stream sound reconstruction. </li>
	 * <li>(SWEET_2096309.txt) Auditory stimulus optimization with feedback
	 * from fuzzy clustering of neuronal responses. </li>
	 * </ol>
	 * 2/ Those are all the stemmed terms in the Lucene index of the three
	 * specified documents:
	 * <p>
	 * am analysi auditori axi base cluster differenti domain featur feedback fm
	 * frequenc from fuzzi i ii integr lagrang neuron optim optimum reconstruct
	 * respons scene share sound stimulus stream time
	 * <p>
	 * 
	 * 3/ In the following table, tf-idf weights for all terms with respect to
	 * the first document are give:
	 * <p>
	 * Refer to {@link Indexer} to get details about the tf/idf computations.
	 * <p>
	 * 
	 * term doc tf idf tfidf<br>
	 * ==============================================<br>
	 * am 1 0.75 1.0 0.75<br>
	 * analysi 1 0.75 1.0 0.75<br>
	 * auditori 1 0.75 0.7123179275482191 0.5342384456611643<br>
	 * axi 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * base 1 0.75 1.0 0.75<br>
	 * cluster 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * differenti 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * domain 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * featur 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * feedback 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * fm 1 0.75 1.0 0.75<br>
	 * frequenc 1 0.75 1.0 0.75<br>
	 * from 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * fuzzi 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * i 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * ii 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * integr 1 1.0 1.0 1.0<br>
	 * lagrang 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * neuron 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * optim 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * optimum 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * reconstruct 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * respons 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * scene 1 0.75 1.0 0.75<br>
	 * share 1 0.75 1.0 0.75<br>
	 * sound 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * stimulus 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * stream 1 0.75 1.4054651081081644 1.0540988310811232<br>
	 * time 1 1.0 1.0 1.0<br>
	 * <p>
	 */

	private LuceneIndexAccessor<String> accessor;

	public void setUp() {
		try {
			accessor = new LuceneIndexAccessor<String>(index);
			assertNotNull(accessor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testTFIDF() {
		LuceneTFIDF tfidf;
		try {
			tfidf = new LuceneTFIDF(accessor, file1, file2);
			assertNotNull(tfidf);
			assertTrue(tfidf.calculate());
			assertTrue(tfidf.isCalculated());

			// relevant vectors, zero values omitted
			Vector<Double> v1 = new Vector<Double>(new Double[] { 0.75,
					0.5342384456611643, 0.75, 1.0, 0.75, 0.75, 0.75, 1.0, 0.75,
					0.75 });
			Vector<Double> v2 = new Vector<Double>(new Double[] { 1.0,
					0.5342384456611643, 0.75, 0.75, 0.75, 0.75, 0.75, 1.0,
					0.75, 0.75 });

			assertNotNull(v1);
			assertNotNull(v2);

			Cosine cosine = new Cosine(v1, v2);
			assertNotNull(cosine);
			assertTrue(cosine.calculate());
			assertTrue(cosine.isCalculated());
			assertEquals(tfidf.getSimilarity(), cosine.getSimilarity());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}
	}

	public void testTFIDF2() {
		LuceneTFIDF tfidf;
		try {
			tfidf = new LuceneTFIDF(accessor, file1, file3);
			assertNotNull(tfidf);
			assertTrue(tfidf.calculate());
			assertTrue(tfidf.isCalculated());

			// relevant vectors, zero values omitted
			Vector<Double> v1 = new Vector<Double>(
					new Double[] { 0.5342384456611643 });
			Vector<Double> v3 = new Vector<Double>(
					new Double[] { 0.7123179275482191 });

			assertNotNull(v1);
			assertNotNull(v3);

			Cosine cosine = new Cosine(v1, v3);
			assertNotNull(cosine);
			assertTrue(cosine.calculate());
			assertTrue(cosine.isCalculated());
			assertEquals(tfidf.getSimilarity(), cosine.getSimilarity());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}

	}

	public void testTFIDF3() {
		LuceneTFIDF tfidf = null;
		try {
			tfidf = new LuceneTFIDF(accessor, file2, file3);
			assertNotNull(tfidf);
			assertTrue(tfidf.calculate());
			assertTrue(tfidf.isCalculated());

			// relevant vectors, zero values omitted
			Vector<Double> v2 = new Vector<Double>(
					new Double[] { 0.5342384456611643 });
			Vector<Double> v3 = new Vector<Double>(
					new Double[] { 0.7123179275482191 });

			assertNotNull(v2);
			assertNotNull(v3);

			Cosine cosine = new Cosine(v2, v3);
			assertNotNull(cosine);
			assertTrue(cosine.calculate());
			assertTrue(cosine.isCalculated());
			assertEquals(tfidf.getSimilarity(), cosine.getSimilarity());
		} catch (InvalidElementException e1) {
			e1.printStackTrace();
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}
	}

	public void testTFIDFSameDocument() {
		LuceneTFIDF tfidf;
		try {
			tfidf = new LuceneTFIDF(accessor, file1, file1);
			assertNotNull(tfidf);
			assertTrue(tfidf.calculate());
			assertTrue(tfidf.isCalculated());

			// relevant vectors, zero values omitted
			Vector<Double> v1 = new Vector<Double>(new Double[] { 0.75,
					1.0540988310811232, 0.5342384456611643, 0.75, 1.0,
					1.0540988310811232, 1.0540988310811232, 1.0540988310811232,
					0.75, 0.75, 0.75, 1.0, 0.75, 1.0540988310811232, 0.75,
					1.0540988310811232 });
			Vector<Double> v2 = new Vector<Double>(new Double[] { 0.75,
					1.0540988310811232, 0.5342384456611643, 0.75, 1.0,
					1.0540988310811232, 1.0540988310811232, 1.0540988310811232,
					0.75, 0.75, 0.75, 1.0, 0.75, 1.0540988310811232, 0.75,
					1.0540988310811232 });

			assertNotNull(v1);
			assertNotNull(v2);

			Cosine cosine = new Cosine(v1, v2);
			assertNotNull(cosine);
			assertTrue(cosine.calculate());
			assertTrue(cosine.isCalculated());
			assertEquals(tfidf.getSimilarity(), cosine.getSimilarity());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
		}
	}

	public void testTFIDFInvalidName() {
		try {
			LuceneTFIDF tfidf = new LuceneTFIDF(accessor, file1, "file2.txxt");
			fail("Should throw an InvalidElementException.");
		} catch (InvalidElementException ex) {
			assertTrue(true);
		}
	}
}
