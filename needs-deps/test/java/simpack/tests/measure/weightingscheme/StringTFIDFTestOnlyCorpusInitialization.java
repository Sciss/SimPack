/*
 * $Id: StringTFIDFTestOnlyCorpusInitialization.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.ArrayList;

import junit.framework.TestCase;
import simpack.measure.weightingscheme.StringTFIDF;
import simpack.util.corpus.StringUtils;

import com.wcohen.ss.BasicStringWrapper;
import com.wcohen.ss.api.StringWrapper;

/**
 * @author Christoph Kiefer
 * @version $Id: StringTFIDFTestOnlyCorpusInitialization.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class StringTFIDFTestOnlyCorpusInitialization extends TestCase {

	private ArrayList<StringWrapper> corpus;

	private String str1 = "Auditory scene analysis based on time-frequency integration of shared FM and AM (II): Optimum time-domain integration and stream sound reconstruction.";

	private String str2 = "Auditory scene analysis based on time-frequency integration of shared FM and AM (I): Lagrange differential features and frequency-axis integration.";

	private String str3 = "Auditory stimulus optimization with feedback from fuzzy clustering of neuronal responses.";

	private StringTFIDF tfidf;

	protected void setUp() throws Exception {
		corpus = new ArrayList<StringWrapper>();
		corpus.add(new BasicStringWrapper(StringUtils.clean(str1)));
		corpus.add(new BasicStringWrapper(StringUtils.clean(str2)));
		corpus.add(new BasicStringWrapper(StringUtils.clean(str3)));
		assertNotNull(corpus);
	}

	public void testTFIDF() {
		tfidf = new StringTFIDF(corpus);
		assertNotNull(tfidf);

		// double score = 0.08517628471065235 * 0.08449023868260493
		// + 0.13500121721713274 * 0.08449023868260493
		// + 0.08517628471065235 * 0.08449023868260493
		// + 0.08517628471065235 * 0.08449023868260493
		// + 0.2307861051788788 * 0.22892725571949749 + 0.2307861051788788
		// * 0.22892725571949749 + 0.2307861051788788
		// * 0.22892725571949749 + 0.2307861051788788
		// * 0.22892725571949749 + 0.2307861051788788 * 0.3628411157084063
		// + 0.36578732239601147 * 0.3628411157084063 + 0.2307861051788788
		// * 0.22892725571949749 + 0.2307861051788788
		// * 0.22892725571949749 + 0.36578732239601147
		// * 0.3628411157084063;

		double score = 0.22347915256519232;

		assertEquals(tfidf.getSimilarity(StringUtils.clean(str1), StringUtils
				.clean(str2)), score);
	}

	public void testTFIDFEquality() {
		tfidf = new StringTFIDF(corpus);
		assertNotNull(tfidf);
		assertEquals(tfidf.getSimilarity(str1, str1), 0.9999999999999999);
	}
}
