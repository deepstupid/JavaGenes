//
// Copyright (C) 2005 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA.txt at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
//  Created by Al Globus on Thu Jul 18 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;

public class NodeTest extends TestCase {

    private final int startTime = 5;
    private final int endTime = 10;
    private Node list;
    public NodeTest(String name) {
        super(name);
    }

    protected void setUp() {
        NodeFactory factory = new NodeFactory();
        list = (Node) factory.newAvailableNode(startTime, null, null);
        Node next = (Node) factory.newUnAvailableNode(endTime, null, null);
        next.setPrevious(list);
        list.setNext(next);
        list.setEnd(endTime);
    }

    protected void tearDown() {
    }

    public void testIsDurationAvailable() {
        assertTrue("1", list.isDurationAvailable(5, 5));
        assertTrue("2", !list.isDurationAvailable(5, 6));
        assertTrue("3", list.isDurationAvailable(6, 2));
        assertTrue("4", list.isDurationAvailable(9, 1));
        assertTrue("5", !list.isDurationAvailable(4, 2));
    }

    public void testIncludes() {
        assertTrue("1", list.includes(7));
        assertTrue("2", list.includes(5));
        assertTrue("3", list.includes(9));
        assertTrue("3.5", !list.includes(2));
        assertTrue("4", !list.includes(4));
        assertTrue("5", list.includes(9));
        assertTrue("6", !list.includes(10));
        assertTrue("7", !list.includes(17));
    }
}
