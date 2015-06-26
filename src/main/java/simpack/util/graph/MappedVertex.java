/*
 * $Id: MappedVertex.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.graph;

import java.io.Serializable;
import java.util.TreeSet;

import simpack.accessor.string.StringAccessor;
import simpack.api.IGraphNode;
import simpack.measure.sequence.Levenshtein;
import simpack.util.graph.comparator.MappedVertexComparator;

/**
 * Accessor for graph structures. This defines a method set for basic graph
 * operations.
 * 
 * @author Daniel Baggenstos
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class MappedVertex implements Serializable {

	private static final long serialVersionUID = -111393192702009001L;

	private final static String DEFAULT_SIMILARITY_MEASURE = "Levenshtein";

	private String similarityMeasure = DEFAULT_SIMILARITY_MEASURE;

	private IGraphNode left;

	private IGraphNode right;

	private int groupSize = 0;

	private boolean groupFlag = false;

	private double labelSimilarity = 0.0, groupSimilarity = 0.0;

	private TreeSet<MappedVertex> groupMembers = new TreeSet<MappedVertex>(
			new MappedVertexComparator());

	public MappedVertex(IGraphNode left, IGraphNode right) {
		this(left, right, DEFAULT_SIMILARITY_MEASURE);
	}

	public MappedVertex(IGraphNode left, IGraphNode right,
			String similarityMeasure) {
		this.left = left;
		this.right = right;
		this.similarityMeasure = similarityMeasure;
	}

	/**
	 * Returns left graph node of this mapped vertex.
	 * 
	 * @return graph node
	 */
	public IGraphNode getLeftNode() {
		return left;
	}

	/**
	 * Returns right graph node of this mapped vertex.
	 * 
	 * @return graph node
	 */
	public IGraphNode getRightNode() {
		return right;
	}

	public void setGroupSize(int size) {
		groupSize = size;
		groupFlag = true;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroup() {
		groupFlag = true;
	}

	public boolean isGroup() {
		return groupFlag;
	}

	public void addGroupMember(MappedVertex mv) {
		groupMembers.add(mv);
	}

	public TreeSet<MappedVertex> getGroupMembers() {
		return groupMembers;
	}

	public void setLabelSimilarity(double similarity) {
		labelSimilarity = similarity;
	}

	public void setGroupSimilarity(double similarity) {
		groupSimilarity = similarity;
	}

	public double getGroupSimilarity() {
		return groupSimilarity;
	}

	public double getLabelSimilarity() {
		return labelSimilarity;
	}

	public double calculateLabelSimilarity() {
		double sim = 0d;
		if (similarityMeasure.equals("Levenshtein")) {
			Levenshtein<String> levenshtein = new Levenshtein<String>(
					new StringAccessor(this.getLeftNode().toString()),
					new StringAccessor(this.getRightNode().toString()));
			sim = levenshtein.getSimilarity();
		}
		return sim;
	}

	public int smaller(MappedVertex mv) {
		return this.toString().compareTo(mv.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		MappedVertex mv = (MappedVertex) o;
		// two non-group nodes
		if (!isGroup() && !mv.isGroup()) {
			// this uses the equals()-methods of the left and right user objects
			return getLeftNode().equals(mv.getLeftNode())
					&& getRightNode().equals(mv.getRightNode());
		} else if (isGroup() != mv.isGroup()) {
			// only one of the two nodes under comparison is a group
			// thus unequal
			// TODO: groups of size one!!! problem?
			return false;
		} else {
			// both nodes under comparison are groups
			// two groups are equal if every mapped vertex of one group also
			// appears in the other group
			TreeSet<MappedVertex> thisGroup = getGroupMembers();
			TreeSet<MappedVertex> otherGroup = mv.getGroupMembers();
			return thisGroup.equals(otherGroup);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// handle groups
		if (!isGroup()) {
			return getLeftNode().toString() + ":" + getRightNode().toString();
		} else {
			String result = "<";
			TreeSet<MappedVertex> group = getGroupMembers();
			for (MappedVertex v : group) {
				result += " " + v.getLeftNode().toString() + ":"
						+ v.getRightNode().toString();
			}
			result += " >";
			return result;
		}
	}
}
