/*
 * $Id: StringTFIDF.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.weightingscheme;

import java.util.List;

import com.wcohen.ss.BasicStringWrapperIterator;
import com.wcohen.ss.api.StringWrapper;
import com.wcohen.ss.api.StringWrapperIterator;
import com.wcohen.ss.api.Tokenizer;
import com.wcohen.ss.tokens.SimpleTokenizer;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class StringTFIDF extends AbstractTFIDF {

	private com.wcohen.ss.TFIDF TFIDF;

	private String str1, str2;

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param corpus
	 */
	public StringTFIDF(List<StringWrapper> corpus) {
		this(corpus, "", "", SimpleTokenizer.DEFAULT_TOKENIZER);
	}

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param corpus
	 * @param tokenizer
	 */
	public StringTFIDF(List<StringWrapper> corpus, Tokenizer tokenizer) {
		this(corpus, "", "", tokenizer);
	}

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param corpus
	 * @param str1
	 * @param str2
	 */
	public StringTFIDF(List<StringWrapper> corpus, String str1, String str2) {
		this(corpus, str1, str2, SimpleTokenizer.DEFAULT_TOKENIZER);
	}

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param corpus
	 * @param str1
	 * @param str2
	 * @param tokenizer
	 */
	public StringTFIDF(List<StringWrapper> corpus, String str1, String str2,
			Tokenizer tokenizer) {
		this.str1 = str1;
		this.str2 = str2;
		TFIDF = new com.wcohen.ss.TFIDF(tokenizer);
		StringWrapperIterator iterator = new BasicStringWrapperIterator(corpus
				.iterator());
		TFIDF.train(iterator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.SimilarityMeasure#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		StringWrapper w1 = TFIDF.prepare(str1);
		StringWrapper w2 = TFIDF.prepare(str2);
		similarity = new Double(TFIDF.score(w1, w2));
		setCalculated(true);
		return true;
	}

	public Double getSimilarity(String str1, String str2) {
		this.str1 = str1;
		this.str2 = str2;
		calculate();
		if (isCalculated()) {
			return this.similarity;
		} else {
			return -1d;
		}
	}
}