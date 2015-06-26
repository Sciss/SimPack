/*
 * $Id: AbstractTFIDF.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.api.impl.AbstractSimilarityMeasure;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractTFIDF extends AbstractSimilarityMeasure {

	/**
	 * Normalized query term weights as suggested by Salton and Buckley.
	 * 
	 * @param tf
	 *            raw frequency of a term k_i in a document d_j
	 * @param maxTF
	 *            maximum term frequency, computed over all terms which are
	 *            mentioned in the text of document d_j
	 * @return query term weights
	 */
	public static double tfSaltonBuckley(double tf, double maxTF) {
		return 0.5d + (0.5d * tf / maxTF);
	}

	/**
	 * Implemented as log(numDocs/(docFreq))
	 * 
	 * @param N
	 *            total number of documents in the collection
	 * @param ni
	 *            number of documents in which the index term i appears
	 * @return inverse document frequency
	 */
	public static double idf(double N, double ni) {
		return Math.log(N / ni);
	}

	/**
	 * Implemented as log(numDocs/(docFreq+1)) + 1
	 * 
	 * @param N
	 *            total number of documents in the collection
	 * @param ni
	 *            number of documents in which the index term i appears
	 * @return inverse document frequency
	 */
	public static double idf2(double N, double ni) {
		return Math.log(N / (ni + 1d)) + 1d;
	}
}