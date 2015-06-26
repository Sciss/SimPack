/*
 * $Id: AbstractSequenceSimilarityMeasure.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.api.ISequenceAccessor;

/**
 * This is an abstract base class for all sequence-based similarity measures
 * that allows to measure similarity between sequences (strings, nodes, etc.)
 * 
 * @author Christoph Kiefer
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractSequenceSimilarityMeasure<E> extends AbstractSimilarityMeasure {

	protected ISequenceAccessor<E> sequenceAccessor1;

	protected ISequenceAccessor<E> sequenceAccessor2;

	/**
	 * Constructor.
	 * <p>
	 */
	public AbstractSequenceSimilarityMeasure() {
	}

	/**
	 * Constructor.
	 * <p>
	 * Two sequence accessors are passed. The similarity between two sequences
	 * is computed by the concrete sequence-based similarity measure which
	 * extends this abstract base class.
	 * 
	 * @param sequenceAccessor1
	 *            the first sequence
	 * @param sequenceAccessor2
	 *            the second sequence
	 */
	public AbstractSequenceSimilarityMeasure(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2) {
		if (sequenceAccessor1 == null || sequenceAccessor2 == null) {
			throw new NullPointerException("Invalid ISequenceAccessors!");
		}
		this.sequenceAccessor1 = sequenceAccessor1;
		this.sequenceAccessor2 = sequenceAccessor2;
	}
}