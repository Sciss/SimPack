/*
 * $Id: TFIDFTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.external.secondstring;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import simpack.measure.external.secondstring.TFIDF;

import com.wcohen.ss.BasicStringWrapper;
import com.wcohen.ss.api.StringWrapper;

/**
 * @author Christoph Kiefer
 * @version $Id: TFIDFTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TFIDFTest extends TestCase {

	private String str1, str2, str3;

	private List<StringWrapper> corpus;

	private TFIDF TFIDF;

	private double w_service, w_hotel, w_city, w_country, w_capital, w_locator;

	private double str1_normalizer, str2_normalizer;

	public void setUp() {
		str1 = "service hotel city locator";
		str2 = "service country capital";
		str3 = "country hospital finder";

		corpus = new ArrayList<StringWrapper>();
		String[] words = { str1, str2, str3 };
		for (int i = 0; i < words.length; i++) {
			corpus.add(new BasicStringWrapper(words[i]));
		}

		w_service = Math.log(1d + 1d) * Math.log(3d / 2d);
		w_hotel = Math.log(1d + 1d) * Math.log(3d / 1d);
		w_city = Math.log(1d + 1d) * Math.log(3d / 1d);
		w_locator = Math.log(1d + 1d) * Math.log(3d / 1d);
		w_country = Math.log(1d + 1d) * Math.log(3d / 2d);
		w_capital = Math.log(1d + 1d) * Math.log(3d / 1d);

		str1_normalizer = Math.sqrt((w_service * w_service)
				+ (w_hotel * w_hotel) + (w_city * w_city)
				+ (w_locator * w_locator));

		str2_normalizer = Math.sqrt((w_service * w_service)
				+ (w_country * w_country) + (w_capital * w_capital));
	}

	public void testCalculateSimilarity() {
		TFIDF = new TFIDF(str1, str1, corpus);
		assertNotNull(TFIDF);
		assertTrue(TFIDF.calculate());
		assertTrue(TFIDF.isCalculated());
		assertEquals(TFIDF.getSimilarity(), new Double(1));
	}

	public void testCalculateSimilarity2() {
		TFIDF = new TFIDF(str1, str2, corpus);
		assertNotNull(TFIDF);
		assertTrue(TFIDF.calculate());
		assertTrue(TFIDF.isCalculated());

		double sim = (w_service / str1_normalizer)
				* (w_service / str2_normalizer);

		assertEquals(TFIDF.getSimilarity(), new Double(sim));
	}
}