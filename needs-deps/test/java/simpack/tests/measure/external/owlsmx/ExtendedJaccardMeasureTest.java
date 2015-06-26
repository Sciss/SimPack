/*
 * $Id: ExtendedJaccardMeasureTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.external.owlsmx;

import simpack.exception.InvalidVectorSizeException;
import simpack.measure.external.owlsmx.ExtendedJaccardMeasure;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: ExtendedJaccardMeasureTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ExtendedJaccardMeasureTest extends TestCase {

	private String profileDesc1 = new String(
			"It is the most used service for acknowledgement of the hotel in a city of a country.");

	private String profileDesc2 = new String(
			"This service returns accomodation information, hotel, restaurant etc in the city of the country.");

	/*
	 * Term frequencies str1: the 2 of 2 most 1 acknowledgement 1 service 1
	 * country 1 a 2 it 1 hotel 1 is 1 in 1 city 1 used 1 for 1
	 */

	/*
	 * Term frequencies str2: the 2 of 1 returns 1 service 1 restaurant 1
	 * country 1 information 1 etc 1 hotel 1 accomodation 1 this 1 in 1 city 1
	 */

	public void testCalculationSimilarity() {
		ExtendedJaccardMeasure jaccard = new ExtendedJaccardMeasure(
				profileDesc1, profileDesc2);
		assertNotNull(jaccard);
		assertTrue(jaccard.calculate());
		assertTrue(jaccard.isCalculated());
		assertEquals(jaccard.getSimilarity(), new Double(computeResultByHand()));
	}

	/**
	 * Cumbersomely reverse engineered from the OWLS-MX measures.
	 * 
	 * @return the similarity of the input strings
	 */
	private double computeResultByHand() {
		simpack.util.Vector<Double> v1 = new simpack.util.Vector<Double>();
		simpack.util.Vector<Double> v2 = new simpack.util.Vector<Double>();

		v1.add((Math.log(1d) + 1d) * 1d); // it
		v2.add(0d * 1d); // it

		v1.add((Math.log(1d) + 1d) * 1d); // is
		v2.add(0d * 1d); // is

		v1.add((Math.log(2d) + 1d) * 1d); // the
		v2.add((Math.log(2d) + 1d) * 1d); // the

		v1.add((Math.log(1d) + 1d) * 1d); // most
		v2.add(0d * 1d); // most

		v1.add((Math.log(1d) + 1d) * 1d); // used
		v2.add(0d * 1d); // used

		v1.add((Math.log(1d) + 1d) * 1d); // for
		v2.add(0d * 1d); // for

		v1.add((Math.log(2d) + 1d) * 1d); // of
		v2.add((Math.log(1d) + 1d) * 1d); // of

		v1.add((Math.log(1d) + 1d) * 1d); // in
		v2.add((Math.log(1d) + 1d) * 1d); // in

		v1.add((Math.log(2d) + 1d) * 1d); // a
		v2.add(0d * 1d); // a

		v1.add(0d * 1d); // this
		v2.add((Math.log(1d) + 1d) * 1d); // this

		v1.add(0d * 1d); // etc
		v2.add((Math.log(1d) + 1d) * 1d); // etc

		v1.add((Math.log(1d) + 1d) * 1d); // acknowledgement
		v2.add(0d * 1d); // acknowledgement

		v1.add((Math.log(1d) + 1d) * 1d); // service
		v2.add((Math.log(1d) + 1d) * 1d); // service

		v1.add((Math.log(1d) + 1d) * 1d); // country
		v2.add((Math.log(1d) + 1d) * 1d); // country

		v1.add((Math.log(1d) + 1d) * 1d); // hotel
		v2.add((Math.log(1d) + 1d) * 1d); // hotel

		v1.add((Math.log(1d) + 1d) * 1d); // city
		v2.add((Math.log(1d) + 1d) * 1d); // city

		v1.add(0d * 1d); // returns
		v2.add((Math.log(1d) + 1d) * 1d); // returns

		v1.add(0d * 1d); // restaurant
		v2.add((Math.log(1d) + 1d) * 1d); // restaurant

		v1.add(0d * 1d); // information
		v2.add((Math.log(1d) + 1d) * 1d); // information

		v1.add(0d * 1d); // accomodation
		v2.add((Math.log(1d) + 1d) * 1d); // accomodation

		try {
			double dot = v1.getDotProduct(v2);
			double n1 = v1.getDotProduct(v1);
			double n2 = v2.getDotProduct(v2);
			return (dot / (n1 + n2 - dot));
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
			return -1d;
		}
	}
}