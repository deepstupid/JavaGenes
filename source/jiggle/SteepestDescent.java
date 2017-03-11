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
/*
Copyright Daniel Tunkelang (quixote@alum.mit.edu), 1999. 
Permission is granted for anyone to use, redistribute, and/or modify the software 
for any purpose including commercial. 
Daniel Tunkelang retains the rights to use the software in any way 
and to assign those rights to others.
*/
package jiggle;

/* Class for method of steepest descent. */

public class SteepestDescent extends FirstOrderOptimizationProcedure {

    public SteepestDescent(Graph g, ForceModel fm, double accuracy) {
        super(g, fm, accuracy);
    }

    protected void computeDescentDirection() {
        int n = graph.numberOfVertices, d = graph.getDimensions();
        if ((descentDirection == null) || (descentDirection.length != n))
            descentDirection = new double[n][d];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                descentDirection[i][j] = negativeGradient[i][j];
            }
        }
    }
}