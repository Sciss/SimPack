/*
 * $Id: LuceneIndexAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.list;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.PriorityQueue;

import simpack.api.impl.AbstractCollectionAccessor;
import simpack.exception.InvalidElementException;
import simpack.measure.weightingscheme.AbstractTFIDF;
import simpack.util.Vector;
import simpack.util.corpus.Indexer;

/**
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class LuceneIndexAccessor<E> extends AbstractCollectionAccessor<E> {

	public static Logger logger = LogManager.getLogger(LuceneIndexAccessor.class);

	public static Analyzer analyzer = Indexer.DEFAULT_ANALYZER;

	private String[] fieldNames = Indexer.DEFAULT_FIELD_NAMES;

	private String index;

	private IndexReader ir;

	private IndexSearcher is;

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param index directory where a Lucene index is stored
	 */
	public LuceneIndexAccessor(String index) throws IOException {
		this.index = index;
		createReader();
		createSearcher();
		// if (fieldNames == null) {
		// // gather list of valid fields from lucene
		// Collection fields = ir
		// .getFieldNames(IndexReader.FieldOption.INDEXED);
		// fieldNames = (String[]) fields.toArray(new String[fields.size()]);
		// }
	}

	/**
	 * Returns the current index reader.
	 * 
	 * @return index reader
	 */
	public IndexReader getIndexReader() {
		return ir;
	}

	/**
	 * Returns the current index searcher.
	 * 
	 * @return index searcher
	 */
	public IndexSearcher getIndexSearcher() {
		return is;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.CollectionAccessor#getSize()
	 */
	public int getSize() {
		return ir.numDocs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.CollectionAccessor#containsElement(null)
	 */
	public boolean containsElement(E object) {
		try {
			QueryParser qp = new QueryParser(
					Indexer.DEFAULT_DOCUMENT_TITLE_FIELD_NAME, analyzer);
			// Search for the query
			Hits hits = is.search(qp.parse((String) object));
			// Examine the Hits object to see if there were any matches
			int hitCount = hits.length();
			if (hitCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.CollectionAccessor#getWeights(null)
	 */
	public Vector<Double> getWeights(E object) throws InvalidElementException {
		try {
			Vector<Double> weights = new Vector<Double>();
			int docID;
			docID = getDocID((String) object);
			if (docID == -1) {
				throw new InvalidElementException("Document '"
						+ (String) object + "' not in index.");
			} else {
				double max = getMaxTF(getTermFrequencies(docID));
				TermEnum te = ir.terms();
				while (te.next()) {
					Term currentTerm = te.term();
					double tf = 0, idf;
					if (!currentTerm.field().equals(
							Indexer.DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME)) {
						// do nothing at the moment
					} else {
						idf = AbstractTFIDF.idf2(ir.numDocs(), te.docFreq());
						// skip to requested document
						TermDocs td = ir.termDocs(currentTerm);
						if (td.skipTo(docID)) {
							// term exists in the requested document
							tf = AbstractTFIDF.tfSaltonBuckley(td.freq(), max);
							weights.add(tf * idf);
						} else {
							// term does not exist in the requested document
							weights.add(0d);
						}
						// System.out.println(currentTerm.text() + "\t" + "1"
						// + "\t" + tf + "\t" + idf + "\t" + (tf * idf));
					}
				}
				return weights;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Find words for a more-like-this query former.
	 * 
	 * @param docNum
	 *            the id of the lucene document from which to find terms
	 */
	public PriorityQueue retrieveTerms(int docNum) throws IOException {
		Map<String, Int> termFreqMap = getTermFrequencies(docNum);
		return createQueue(termFreqMap);
	}

	/**
	 * @param docNum
	 * @return
	 * @throws IOException
	 */
	private Map<String, Int> getTermFrequencies(int docNum) throws IOException {
		Map<String, Int> termFreqMap = new HashMap<String, Int>();
		String fieldName = Indexer.DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME;
		TermFreqVector vector = ir.getTermFreqVector(docNum, fieldName);
		// field does not store term vector info
		if (vector == null) {
			Document d = ir.document(docNum);
			String text[] = d.getValues(fieldName);
			if (text != null) {
				for (int j = 0; j < text.length; j++) {
					addTermFrequencies(new StringReader(text[j]), termFreqMap,
							fieldName);
				}
			} else {
				// do nothing at the moment
			}
		} else {
			addTermFrequencies(termFreqMap, vector);
		}
		return termFreqMap;
	}

	/**
	 * @throws IOException
	 */
	private void createReader() throws IOException {
		Directory fsDir = FSDirectory.getDirectory(index, false);
		ir = IndexReader.open(fsDir);
	}

	/**
	 * @throws IOException
	 */
	private void createSearcher() throws IOException {
		Directory fsDir = FSDirectory.getDirectory(index, false);
		is = new IndexSearcher(fsDir);
	}

	/**
	 * @param docName
	 *            i.e., fileXYZ.txt
	 * @return the id of the document in the Lucene index
	 */
	private int getDocID(String docName) throws Exception {
		QueryParser qp = new QueryParser(
				Indexer.DEFAULT_DOCUMENT_TITLE_FIELD_NAME, analyzer);
		Hits hits = is.search(qp.parse(docName));
		if (hits.length() == 1) {
			return hits.id(0);
		} else {
			return -1;
		}
	}

	/**
	 * @param words
	 * @return
	 */
	private double getMaxTF(Map words) {
		double max = 0d;
		Iterator it = words.keySet().iterator();
		while (it.hasNext()) {
			// for every word
			String word = (String) it.next();
			// term freq in the source doc
			int tf = ((Int) words.get(word)).x;
			if (tf > max) {
				max = tf;
			}
		}
		return max;
	}

	/**
	 * @param words
	 * @return
	 * @throws IOException
	 */
	private double getNormalization(Map words) throws IOException {
		int numDocs = ir.numDocs();
		double denominator = 0d;
		Iterator it = words.keySet().iterator();
		while (it.hasNext()) { // for every word
			String word = (String) it.next();
			// term freq in the source doc
			int tf = ((Int) words.get(word)).x;
			// go through all the fields and find the largest document frequency
			int docFreq = 0;
			int freq = ir.docFreq(new Term(
					Indexer.DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME, word));
			docFreq = (freq > docFreq) ? freq : docFreq;
			if (docFreq == 0) {
				continue; // index update problem?
			}
			double idf = AbstractTFIDF.idf2(docFreq, numDocs);
			denominator += (tf * tf) * (idf * idf);
		}
		return Math.sqrt(denominator);
	}

	/**
	 * Create a PriorityQueue from a word->tf map.
	 * 
	 * @param words
	 *            a map of words keyed on the word(String) with Int objects
	 *            asaddTermFrequencies the values.
	 */
	private PriorityQueue createQueue(Map words) throws IOException {
		// have collected all words in doc and their freqs
		int numDocs = ir.numDocs();
		// will order words by score
		FreqQ res = new FreqQ(words.size());
		// calculate maximum term frequency in the document (for idf2())
		double max = getMaxTF(words);
		// for every word
		Iterator it = words.keySet().iterator();
		while (it.hasNext()) {
			String word = (String) it.next();
			// term freq in the source doc
			double tf = ((Int) words.get(word)).x;
			tf = AbstractTFIDF.tfSaltonBuckley(tf, max);
			// go through all the fields and find the largest document frequency
			String topField = fieldNames[0];
			int docFreq = 0;
			int freq = ir.docFreq(new Term(
					Indexer.DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME, word));
			topField = Indexer.DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME;
			docFreq = (freq > docFreq) ? freq : docFreq;
			if (docFreq == 0) {
				// index update problem?
				continue;
			}
			// float idf = defaultSimilarity.idf(numDocs, docFreq);
			double idf = AbstractTFIDF.idf2(numDocs, docFreq);
			double score = tf * idf;
			res.insert(new Object[] { word, // the word
					topField, // the top field
					new Double(score), // overall score
					new Double(idf), // idf
					new Integer(docFreq), // freq in all docs
					new Double(tf), new Double(0d) });
		}
		return res;
	}

	/**
	 * Adds term frequencies found by tokenizing text from reader into the Map
	 * words
	 * 
	 * @param r
	 *            a source of text to be tokenized
	 * @param termFreqMap
	 *            a Map of terms and their frequencies
	 * @param fieldName
	 *            Used by DEFAULT_ANALYZER for any special per-field analysis
	 */
	private void addTermFrequencies(Reader r, Map<String, Int> termFreqMap,
			String fieldName) throws IOException {
		TokenStream ts = Indexer.DEFAULT_ANALYZER.tokenStream(fieldName, r);
		org.apache.lucene.analysis.Token token;
		while ((token = ts.next()) != null) { // for every token
			String word = token.termText();
			// increment frequency
			Int cnt = (Int) termFreqMap.get(word);
			if (cnt == null) {
				termFreqMap.put(word, new Int());
			} else {
				cnt.x++;
			}
		}
	}

	/**
	 * Adds terms and frequencies found in vector into the Map termFreqMap
	 * 
	 * @param termFreqMap
	 *            a Map of terms and their frequencies
	 * @param vector
	 *            List of terms and their frequencies for a doc/field
	 */
	private void addTermFrequencies(Map<String, Int> termFreqMap,
			TermFreqVector vector) {
		String[] terms = vector.getTerms();
		int freqs[] = vector.getTermFrequencies();
		for (int j = 0; j < terms.length; j++) {
			String term = terms[j];
			// increment frequency
			Int cnt = (Int) termFreqMap.get(term);
			if (cnt == null) {
				cnt = new Int();
				cnt.x = freqs[j];
				termFreqMap.put(term, cnt);
			} else {
				cnt.x += freqs[j];
			}
		}
	}

	/**
	 * Use for frequencies and to avoid renewing Integers.
	 * 
	 */
	private static class Int {
		int x;

		Int() {
			x = 1;
		}
	}

	private static class FreqQ extends PriorityQueue {
		FreqQ(int s) {
			initialize(s);
		}

		protected boolean lessThan(Object a, Object b) {
			Object[] aa = (Object[]) a;
			Object[] bb = (Object[]) b;
			Double fa = (Double) aa[2];
			Double fb = (Double) bb[2];
			return fa.doubleValue() > fb.doubleValue();
		}
	}
}
