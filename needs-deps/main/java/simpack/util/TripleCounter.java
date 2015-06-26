/*
 * $Id: TripleCounter.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

/**
 * This tool allows counting triples of an underlying ontology
 * 
 * @author Markus Stocker
 * @version $Id: TripleCounter.java 757 2008-04-17 17:42:53Z kiefer $
 */

public class TripleCounter {

	/** The Jena model */
	private static Model model = ModelFactory.createDefaultModel();

	/**
	 * The main entry point to the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String inGraphFileName = null;

		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("--graph"))
					inGraphFileName = args[i + 1];
				else if (args[i].equals("--help"))
					usage();
			}
		} else
			usage();

		InputStream in = FileManager.get().open(inGraphFileName);

		if (in == null)
			throw new IllegalArgumentException("File: " + inGraphFileName
					+ " not found");

		// Load the model
		model.read(in, "");

		StmtIterator stmtIter = model.listStatements();

		int count = 0;

		while (stmtIter.hasNext()) {
			stmtIter.nextStatement();
			count++;
		}

		System.out.println("Number of triples: " + count);
	}

	private static void usage() {
		System.out
				.println("ch.unizh.ifi.isparql.tools.TripleCounter [options]");
		System.out.println("Options:");
		System.out.println("--graph [filename]");

		System.exit(0);
	}

}
