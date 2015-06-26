/*
 * $Id: LuceneTFIDF.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.util.PriorityQueue;

import simpack.accessor.list.LuceneIndexAccessor;
import simpack.exception.InvalidElementException;
import simpack.exception.InvalidVectorSizeException;
import simpack.measure.vector.Cosine;
import simpack.util.Vector;
import simpack.util.corpus.Indexer;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class LuceneTFIDF extends AbstractTFIDF {

	public static Logger logger = LogManager.getLogger(LuceneTFIDF.class);

	private String d1, d2;

	private int id1, id2;

	private LuceneIndexAccessor indexAccessor;

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param indexAccessor
	 *            an index accesor
	 * @param d1
	 *            the first string
	 * @param d2
	 *            the second string
	 */
	public LuceneTFIDF(LuceneIndexAccessor<String> indexAccessor, String d1,
			String d2) throws InvalidElementException {
		this.indexAccessor = indexAccessor;
		this.d1 = d1;
		this.d2 = d2;
		// check if document names are valid
		Document doc1, doc2;
		// use url field to find document id
		QueryParser qp = new QueryParser("url", Indexer.DEFAULT_ANALYZER);
		try {
			Hits hits = indexAccessor.getIndexSearcher().search(qp.parse(d1));
			// assume unique document urls, if two hits are returned with the
			// same document url, then throw a RuntimeException
			if (hits.length() > 1) {
				throw new RuntimeException("Document name " + d1
						+ " not unique");
			} else if (hits.length() == 0) {
				throw new InvalidElementException("Document name " + d1
						+ " not found in index");
			} else {
				doc1 = hits.doc(0);
				id1 = hits.id(0);
			}
			hits = indexAccessor.getIndexSearcher().search(qp.parse(d2));
			if (hits.length() > 1) {
				throw new RuntimeException("Document name " + d2
						+ " not unique");
			} else if (hits.length() == 0) {
				throw new InvalidElementException("Document name " + d2
						+ " not found in index");
			} else {
				doc2 = hits.doc(0);
				id2 = hits.id(0);
			}
			if (logger.isInfoEnabled()) {
				getDocumentInfo(doc1);
				getDocumentInfo(doc2);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.SimilarityMeasure#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		similarity = new Double(getTFIDF(d1, d2));
		setCalculated(true);
		return true;
	}

	/**
	 * @param d1
	 * @param d2
	 * @return
	 */
	private double getTFIDF(String d1, String d2) {
		double score = 0d;
		try {
			// compute list of words and frequencies
			PriorityQueue queue1 = indexAccessor.retrieveTerms(id1);
			PriorityQueue queue2 = indexAccessor.retrieveTerms(id2);
			score = score(queue1, queue2);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return score;
	}

	/**
	 * @param queue1
	 * @param queue2
	 * @return
	 */
	private double score(PriorityQueue queue1, PriorityQueue queue2) {
		Vector<Double> v1 = new Vector<Double>();
		Vector<Double> v2 = new Vector<Double>();
		HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();

		Object cur;
		while (((cur = queue1.pop()) != null)) {
			Object[] ar = (Object[]) cur;
			String word = (String) ar[0];
			double tfidf = ((Double) ar[2]).doubleValue();
			if (map.containsKey(word)) {
				map.get(word).add(tfidf);
			} else {
				ArrayList<Double> list = new ArrayList<Double>();
				list.add(tfidf);
				map.put(word, list);
			}
		}
		while (((cur = queue2.pop()) != null)) {
			Object[] ar = (Object[]) cur;
			String word = (String) ar[0];
			double tfidf = ((Double) ar[2]).doubleValue();
			if (map.containsKey(word)) {
				map.get(word).add(tfidf);
			} else {
				ArrayList<Double> list = new ArrayList<Double>();
				list.add(tfidf);
				map.put(word, list);
			}
		}

		Set<Entry<String, ArrayList<Double>>> entries = map.entrySet();
		Iterator<Entry<String, ArrayList<Double>>> it = entries.iterator();
		while (it.hasNext()) {
			Entry<String, ArrayList<Double>> entry = it.next();
			if (entry.getValue().size() == 2) {
				logger.debug(entry);
				v1.add(entry.getValue().get(0));
				v2.add(entry.getValue().get(1));
			}
		}

		// System.out.println("v1 = " + v1.toString());
		// System.out.println("v2 = " + v2.toString());

		try {
			return new Cosine(v1, v2).getSimilarity();
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
			return 0d;
		}
	}

	/**
	 * @param d
	 */
	private static void getDocumentInfo(Document d) {
		PrintStream o = System.out;
		o.println("url : " + d.get("url"));
		o.println("title : " + d.get("title"));
		o.println("contents : " + d.get("contents"));
		o.println();
	}
}