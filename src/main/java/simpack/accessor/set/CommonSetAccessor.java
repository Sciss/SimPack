/*
 * $Id: CommonSetAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.set;

import java.util.Set;
import java.util.TreeSet;

import simpack.api.impl.AbstractCollectionAccessor;

/**
 * This is a generic set accessor for a set of elements of type
 * <code>Object</code>.
 * 
 * @author Tobias Sager
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class CommonSetAccessor<E> extends AbstractCollectionAccessor<E> {

	private Set<E> elements;

	public CommonSetAccessor() {
		this.elements = new TreeSet<E>();
	}
	
	public CommonSetAccessor(Set<E> elements) {
		this.elements = elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ISetAccessor#getElement(null)
	 */
	public E getElement(E object) {
		if (containsElement(object)) {
			for (E o : elements) {
				if (o.equals(object)) {
					return o;
				}
			}
		}
		return null;
	}
}