/*
 * $Id: ISimilarityMeasure.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api;

/**
 * This defines the interface structure of any measure.
 * 
 * <p>
 * All measures do set up the calculation in the constructor, receive parameters
 * either via constructor invocation or via set methods after creating the
 * similarity measure object. The similarity is queried via the method
 * {@link #getSimilarity() getSimilarity}.
 * 
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public interface ISimilarityMeasure {

	/**
	 * This returns an unique (in the complete package) name of the measure.
	 * Usually this is something like "similarity.measure.string.Levenshtein".
	 * 
	 * @return name of the measure
	 */
	public String getName();

	/**
	 * Returning the result from the similarity calculation. Does calculate
	 * similarity by calling {@link ICalculator#calculate() calculate} if it was
	 * never calculated before.
	 * 
	 * <p>
	 * Will return null if calculation was not successful.
	 * 
	 * @return calculated similarity value, null if calculation was not
	 *         successful.
	 */
	public Double getSimilarity();
}
