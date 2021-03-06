/*
 * $Id: TFIDF.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.external.secondstring;

import java.util.List;

import simpack.api.ISequenceAccessor;
import simpack.api.impl.external.AbstractSecondStringSimilarityMeasure;

import com.wcohen.ss.BasicStringWrapperIterator;
import com.wcohen.ss.api.StringWrapper;
import com.wcohen.ss.api.StringWrapperIterator;
import com.wcohen.ss.api.Tokenizer;
import com.wcohen.ss.tokens.SimpleTokenizer;

/**
 * This is a wrapper class for <a
 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/StringTFIDF.html">com.wcohen.ss.TFIDF</a>
 * of the <a href="http://secondstring.sourceforge.net/">SecondString Project</a>.
 * It computes the string edit distance of two strings.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class TFIDF extends AbstractSecondStringSimilarityMeasure {

	static com.wcohen.ss.TFIDF TFIDF;

	private StringWrapper w1, w2;

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/StringTFIDF.html">com.wcohen.ss.TFIDF</a>.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param corpus
	 *            the whole corpus of documents
	 */
	public TFIDF(String str1, String str2, List<StringWrapper> corpus) {
		this(str1, str2, corpus, SimpleTokenizer.DEFAULT_TOKENIZER);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/StringTFIDF.html">com.wcohen.ss.TFIDF</a>.
	 * Additional constructor arguments specify how the distance is calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param corpus
	 *            the corpus of documents
	 * @param tokenizer
	 *            the tokenizer to tokenize a document/string
	 */
	public TFIDF(String str1, String str2, List<StringWrapper> corpus,
			Tokenizer tokenizer) {
		super();

		TFIDF = new com.wcohen.ss.TFIDF(tokenizer);

		StringWrapperIterator iterator = new BasicStringWrapperIterator(corpus
				.iterator());

		TFIDF.train(iterator);

		w1 = TFIDF.prepare(str1);
		w2 = TFIDF.prepare(str2);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessors and returns the string edit distance as
	 * computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/TFIDF.html">com.wcohen.ss.TFIDF</a>.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * @param corpus
	 *            the whole corpus of documents
	 */
	public TFIDF(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor, List<StringWrapper> corpus) {
		this(leftAccessor.toString(), rightAccessor.toString(), corpus);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessors and returns the string edit distance as
	 * computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/TFIDF.html">com.wcohen.ss.TFIDF</a>.
	 * Additional constructor arguments specify how the distance is calculated.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * @param corpus
	 *            the corpus of documents
	 * @param tokenizer
	 *            the tokenizer to tokenize a document/string
	 */
	public TFIDF(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			List<StringWrapper> corpus, Tokenizer tokenizer) {
		this(leftAccessor.toString(), rightAccessor.toString(), corpus,
				tokenizer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.SimilarityMeasure#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		if (TFIDF != null) {
			similarity = TFIDF.score(w1, w2);
			setCalculated(true);
			return true;
		}
		return false;
	}
}