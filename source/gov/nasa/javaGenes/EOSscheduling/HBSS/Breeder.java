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
package gov.nasa.javaGenes.EOSscheduling.HBSS;

import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.hillClimbing.AcceptImprovement;
import gov.nasa.javaGenes.hillClimbing.Accepter;
import gov.nasa.javaGenes.hillClimbing.RestartNever;
import gov.nasa.javaGenes.hillClimbing.RestartPolicy;

public class Breeder extends gov.nasa.javaGenes.hillClimbing.Breeder {
    public Breeder(Parameters p) {
        this(p, new AcceptImprovement(), new RestartNever());
    }

    public Breeder(Parameters p, Accepter inAccepter, RestartPolicy inRestartPolicy) {
        super(p, inAccepter, inRestartPolicy, new ChildMaker());
    }
}
