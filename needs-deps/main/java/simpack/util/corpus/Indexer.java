/*
 * $Id: Indexer.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Indexer {

	public static Logger logger = LogManager.getLogger(Indexer.class);

	public static Analyzer DEFAULT_ANALYZER = new SnowballAnalyzer("Porter",
			StopAnalyzer.ENGLISH_STOP_WORDS);

	public final static String DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME = "contents";

	public final static String DEFAULT_DOCUMENT_TITLE_FIELD_NAME = "title";

	public final static String DEFAULT_DOCUMENT_URL_FIELD_NAME = "url";

	public final static String[] DEFAULT_FIELD_NAMES = new String[] { DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME };

	public static void generateIndex(String indexDir, String dataDir) {
		try {
			// create directory if not already existing
			FSDirectory directory = FSDirectory.getDirectory(indexDir, true);
			// create or overwrite existing index
			IndexWriter writer = new IndexWriter(directory, DEFAULT_ANALYZER,
					true);
			indexDirectory(writer, new File(dataDir));
			writer.optimize();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Index all files in an entire directory.
	 * 
	 * @param writer
	 *            the index writer
	 * @param dataDir
	 *            directory from where to read source files to be indexed
	 */
	private static void indexDirectory(IndexWriter writer, File dataDir) {
		File[] files = dataDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				indexDirectory(writer, f); // recurse
			} else {
				indexFile(writer, f);
			}
		}
	}

	/**
	 * @param writer
	 * @param f
	 */
	private static void indexFile(IndexWriter writer, File f) {
		try {
			String docName = f.getName();

			Document doc = new Document();

			String content = "";
			BufferedReader br = new BufferedReader(new FileReader(f));
			content = br.readLine();
			String temp = "";
			while ((temp = br.readLine()) != null) {
				content = content + " " + temp;
			}
			br.close();

			doc.add(new Field(DEFAULT_DOCUMENT_CONTENTS_FIELD_NAME, content,
					Field.Store.YES, Field.Index.TOKENIZED,
					Field.TermVector.YES));

			doc
					.add(new Field(DEFAULT_DOCUMENT_TITLE_FIELD_NAME, docName,
							Field.Store.YES, Field.Index.TOKENIZED,
							Field.TermVector.NO));

			doc.add(new Field(DEFAULT_DOCUMENT_URL_FIELD_NAME, f
					.getAbsolutePath(), Field.Store.YES, Field.Index.TOKENIZED,
					Field.TermVector.NO));

			logger.info("Indexing document " + docName);
			writer.addDocument(doc);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Indexer.generateIndex("data/index", "data/corpus");
	}
}
