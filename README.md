[![Build Status](https://travis-ci.org/johnhearn/cb2java.svg?branch=master)](https://travis-ci.org/johnhearn/cb2java)

The CB2Java project's goal is to simplify the lives of developers charged 
with writing Java applications that communicate
with COBOL applications. The main motivation for writing this library was
that in the limited number of available (free) libraries, none had been
designed around a dynamic approach. While it may seem strange to write about
dynamic approaches in Java as it is a statically typed, compiled language
but it solves a lot of issues that arise in a enterprise environment where
almost nothing stays the same for very long.

> CB2Java is *not* a standalone tool for editing
> and viewing COBOL data.

With tools that require class generation (or worse, hand-coded
classes) to parse data defined in COBOL copybooks, a lot of changes require
regenerating and recompiling the code even when application logic does not
change. For example, if an element in a copybook is defined as being a 6
digit integer, you will most likely end up using an int to represent that
value in Java. If later that element is increased to 8 digits, your Java
code is still correct. An int will still hold the value. But if you
generated the code to parse the message, you need to regenerate the classes
and recompile. Some readers might be thinking "that's great but it almost
never happens because it would break other applications." This is true to
some extent but often a secondary copybook is defined that differs only in
one element. With CB2Java, one Java module can use two different copybooks
by merely changing the copybook instance. With a generated approach, you
need two sets of generated classes.

__Copyright &copy; 2006 James Watson__


History
-------

Initial fork of project https://sourceforge.net/projects/cb2java/ by dubwai (https://sourceforge.net/users/dubwai)
- added LICENSE file
- added LICENSE header to all sources

Forked again at https://github.com/devstopfix/cb2java
- conversion into a self-contained Maven project (v1.0)
- copied the classes required from http://cb2xml.sourceforge.net/
- converted source code to Java 5 and added types to collections (v5.0)


Documentation
=============

* [CB2Java Documentation](http://cb2java.sourceforge.net/cb2javadoc.html)
* [JavaDoc API](http://cb2java.sourceforge.net/javadoc/index.html)

Build
=====

Build the JAR file using [Maven](http://maven.apache.org/):

    mvn install

Include the JAR file in your Java project pom.xml:

    <dependency>
        <groupId>net.sf.cb2java</groupId>
        <artifactId>cb2java</artifactId>
        <version>5.4.0</version>
    </dependency>

or your [Leiningen](https://github.com/technomancy/leiningen) project.clj:

    [net.sf.cb2java/cb2java "5.4.0"]


Licence
=======

_This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 1, or (at your option)
any later version._

_This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details._

_You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA._
