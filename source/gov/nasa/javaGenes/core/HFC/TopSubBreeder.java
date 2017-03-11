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
package gov.nasa.javaGenes.core.HFC;

import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.*;

public class TopSubBreeder extends SubBreeder {

    public TopSubBreeder(ChildMakerProvider childMakerProvider, FitnessFunction fitnessFunction) {
        super(childMakerProvider, fitnessFunction);
        grimReaper = new AntiTournament(2);
    }

    public void promote(Individual individual, Population population) {
        Error.assertNotNull(individual);
        Error.assertNotNull(population);
        breeder.topSubPopulationReceivedNewBestIndividual(individual);
        setIndividual(population, getIndexToKill(population), individual);
    }

    public void promoteTooFitIndividuals(Population population) {
    }

    public void parentsOfPromoted(int[] parentIndices, Population population) {
    }

    public String toString() {
        return "TopSubBreeder " + super.toString();
    }
}