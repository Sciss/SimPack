# SimPack

This is a clone of the [SimPack similarity library](https://files.ifi.uzh.ch/ddis/oldweb/ddis/research/simpack/index.html).
All changes by Hanns Holger Rutz released under the original LGPL v2.1+ license.
The following changes have been made

- use sbt to build
- use standard sbt directory structure
- currently only core is in `src/main`, all code that depends on third party libraries is in `needs-dep`
  and will be released at a later point.
- minor code clean up.
- new org/version for publishing. version 0.1.0 is based on original version 0.91.

## Original SimPack Project Notes #

To obtain general information about SimPack, visit the SimPack project web
page at http://www.ifi.uzh.ch/ddis/simpack.html

There are some Javadocs available at
http://www.ifi.uzh.ch/ddis/simpack.html

If you are looking for notes about the SimPack installation, read the INSTALL
file that comes along with this distribution.

If you have questions or remarks about SimPack, please do not hesitate to
contact one of the project members of SimPack or send an email to
simpack@ifi.uzh.ch