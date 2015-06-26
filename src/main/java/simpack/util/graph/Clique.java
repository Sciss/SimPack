/*
 * $Id: Clique.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.TreeSet;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Clique implements Comparable<Clique> {

	private TreeSet<MappedVertex> clique;

	private double similarity;

	private boolean isMaximal;

	public Clique(TreeSet<MappedVertex> clique, double similarity,
			boolean isMaximal) {
		this.clique = clique;
		this.similarity = similarity;
		this.isMaximal = isMaximal;
	}

	public TreeSet<MappedVertex> getClique() {
		return clique;
	}

	public void setClique(TreeSet<MappedVertex> clique) {
		this.clique = clique;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public boolean isMaximal() {
		return isMaximal;
	}

	public void setMaximal(boolean isMaximal) {
		this.isMaximal = isMaximal;
	}

	public boolean equals(Object o) {
		Clique cl = (Clique) o;
		// this uses the equals()-method of MappedVertex
		if (cl.getClique().equals(this.clique)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return getClique().toString();
	}
	
	public int compareTo(Clique c) {
		if (similarity < c.getSimilarity()) {
			return 1;
		} else if (similarity == c.getSimilarity()) {
			return 0;
		} else {
			return -1;
		}
	}

}
