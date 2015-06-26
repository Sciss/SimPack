/*
 * $Id: StringTFIDFTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.tokenizer.SplittedStringTokenizerCohen;
import simpack.util.corpus.StringUtils;

import com.wcohen.ss.BasicStringWrapper;
import com.wcohen.ss.api.StringWrapper;

/**
 * @author Christoph Kiefer
 * @version $Id: StringTFIDFTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class StringTFIDFTest extends TestCase {

	private ArrayList<StringWrapper> corpus1, corpus2;

	private String str1 = "Auditory scene analysis based on time-frequency integration of shared FM and AM (II): Optimum time-domain integration and stream sound reconstruction.";

	private String str2 = "Auditory scene analysis based on time-frequency integration of shared FM and AM (I): Lagrange differential features and frequency-axis integration.";

	private String str3 = "Auditory stimulus optimization with feedback from fuzzy clustering of neuronal responses.";

	// ############

	private String str4 = "Returns food of grocery store";

	private String str5 = "This service returns store food";

	private String str6 = "A grocery service selling food";

	private StringTFIDF tfidf;

	protected void setUp() throws Exception {
		corpus1 = new ArrayList<StringWrapper>();
		corpus1.add(new BasicStringWrapper(StringUtils.clean(str1)));
		corpus1.add(new BasicStringWrapper(StringUtils.clean(str2)));
		corpus1.add(new BasicStringWrapper(StringUtils.clean(str3)));

		corpus2 = new ArrayList<StringWrapper>();
		corpus2.add(new BasicStringWrapper(StringUtils.clean(str4)));
		corpus2.add(new BasicStringWrapper(StringUtils.clean(str5)));
		corpus2.add(new BasicStringWrapper(StringUtils.clean(str6)));
	}

	public void testTFIDF() {
		String str4Clean = StringUtils.clean(str4);
		String str6Clean = StringUtils.clean(str6);

		// System.out.println(str4Clean + "\n" + str6Clean);

		tfidf = new StringTFIDF(corpus1, str4Clean, str6Clean);
		assertNotNull(tfidf);

		double score = 0.5;

		assertEquals(tfidf.getSimilarity(), score);
	}

	public void testTFIDF2() {
		String str1Clean = StringUtils.clean(str1);
		String str2Clean = StringUtils.clean(str2);

		// System.out.println(str1Clean + "\n" + str2Clean);

		tfidf = new StringTFIDF(corpus1, str1Clean, str2Clean,
				new SplittedStringTokenizerCohen("\\s+"));
		assertNotNull(tfidf);

		double score = 0.9718253158075502;

		assertEquals(tfidf.getSimilarity(), score);
	}

	public void testTFIDFEquality() {
		tfidf = new StringTFIDF(corpus1, str1, str1);
		assertNotNull(tfidf);
		assertEquals(tfidf.getSimilarity(), 0.9999999999999999);
	}
}
