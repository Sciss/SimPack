/*
 * $Id: AbstractCollectionAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl;

import java.util.Collection;

import simpack.api.IAccessor;
import simpack.exception.InvalidElementException;
import simpack.util.Vector;

/**
 * This is the abstract base class for all accessor that access collections,
 * i.e., sets, lists, and maps for instance. It implements basic operations for
 * this collections.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractCollectionAccessor<E> implements IAccessor {

	/**
	 * Data structure to hold the element of this collection.
	 */
	protected Collection<E> elements;

	/**
	 * Return the number of elements in the collection.
	 * 
	 * @return size of collection
	 */
	public int getSize() {
		return elements.size();
	}

	/**
	 * Checks if the specific element <code>object</code> exists in the
	 * collection this accessor provides access to.
	 * 
	 * @param object
	 *            collection element to be checked for
	 * @return true if <code>object</code> occurs in collection, false
	 *         otherwise
	 */
	public boolean containsElement(E object) {
		if (elements.contains(object)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the the weight of an element of the collection this accessor
	 * gives access to (default implementation). Extending class should probably
	 * override this method.
	 * 
	 * @return weight of object
	 */
	public double getWeight(E object) {
		if (containsElement(object)) {
			return 1d / getSize();
		} else {
			return 0d;
		}
	}

	/**
	 * Extending class must override this method.
	 * 
	 * @param object
	 * @return vector of object weights
	 * @throws InvalidElementException
	 */
	public Vector<Double> getWeights(E object) throws InvalidElementException {
		return new Vector<Double>(0);
	}

	/**
	 * Returns the elements of this collection.
	 * 
	 * @return a collection of the elements this accessor gives access to
	 */
	public Collection<E> getElements() {
		return elements;
	}

	/**
	 * Returns the frequency of this object within the collection.
	 * 
	 * @param object
	 * @return frequency of the element in the collection
	 */
	public int getElementFrequency(E object) {
		return 0;
	}

	/**
	 * List of all object frequencies this accessor gives access to.
	 * 
	 * @return list of object frequencies
	 */
	public int[] getElementFrequencies() {
		return null;
	}
}
