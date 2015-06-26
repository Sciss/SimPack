/*
 * $Id: FastByteArrayInputStream.java 757 2008-04-17 17:42:53Z kiefer $
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

/**
 * ByteArrayInputStream implementation that does not synchronize methods.
 * 
 * @author Jeff Schnitzer
 * @author Scott Hernandez
 * @author Jim Moore
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class FastByteArrayInputStream extends InputStream {
	/*
	 * Our byte buffer
	 */
	protected byte[] buf = null;

	/*
	 * Number of bytes that we can read from the buffer
	 */
	protected int count = 0;

	/*
	 * Number of bytes that have been read from the buffer
	 */
	protected int pos = 0;

	public FastByteArrayInputStream(byte[] buf, int count) {
		this.buf = buf;
		this.count = count;
	}

	public final int available() {
		return count - pos;
	}

	public final int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	public final int read(byte[] b, int off, int len) {
		if (pos >= count)
			return -1;

		if ((pos + len) > count)
			len = (count - pos);

		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	public final long skip(long n) {
		if ((pos + n) > count)
			n = count - pos;
		if (n < 0)
			return 0;
		pos += n;
		return n;
	}
}