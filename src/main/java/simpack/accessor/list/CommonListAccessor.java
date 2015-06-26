/*
 * $Id: CommonListAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.ArrayList;
import java.util.List;

import simpack.api.impl.AbstractCollectionAccessor;

/**
 * This is a generic set accessor for a set of elements of type <code>E</code>.
 * 
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class CommonListAccessor<E> extends AbstractCollectionAccessor<E> {

	private ArrayList<E> elements;

	/**
	 * 
	 * 
	 * TODO Comment me
	 */
	public CommonListAccessor() {
		this(new ArrayList<E>());
	}

	/**
	 * @param elements
	 * 
	 * TODO Comment me
	 */
	public CommonListAccessor(ArrayList<E> elements) {
		this.elements = elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ISetAccessor#getNumberOfOccurrence(java.lang.Object)
	 */
	public int getNumberOfOccurrence(E object) {
		int ret = 0;
		for (E o : elements) {
			if (o.equals(object)) {
				ret++;
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ListAccessor#getElements(null)
	 */
	public List<E> getElements(E object) {
		if (containsElement(object)) {
			ArrayList<E> result = new ArrayList<E>();
			for (E o : elements) {
				if (o.equals(object)) {
					result.add(o);
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * @param object
	 * @return
	 * 
	 * TODO Comment me
	 */
	public List<E> getElementsBefore(E object) {
		if (containsElement(object)) {
			ArrayList<E> result = new ArrayList<E>();
			int index = elements.indexOf(object);
			for (int i = 0; i < index; i++) {
				result.add(elements.get(i));
			}
			return result;
		}
		return null;
	}

	/**
	 * @param object
	 * @return
	 * 
	 * TODO Comment me
	 */
	public List<E> getElementsAfter(E object) {
		if (containsElement(object)) {
			ArrayList<E> result = new ArrayList<E>();
			int index = elements.indexOf(object);
			int size = getSize();
			for (int i = index + 1; i < size; i++) {
				result.add(elements.get(i));
			}
			return result;
		}
		return null;
	}
}