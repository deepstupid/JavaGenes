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
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.FitnessFunctionMultiObjective;
import gov.nasa.javaGenes.core.WeightedSumFitness;
import gov.nasa.javaGenes.permutation.PermutationParameters;

public class EOSschedulingParameters extends PermutationParameters {
    static {
        // RandomNumber.setSeed(990639400901L); // used to generate repeatable runs. See seed.txt for seed of last run
    }

    public EOSModel model;
    public Scheduler scheduler;
    public int takeImageTime = 24; // seconds
    public int timeBetweenSamples = 60; // seconds
    public double slewRate = 0.02; // degrees per second
    public int SSRcapacity = 20;
    public int taskSSRuse = 1;
    public int timeBetweenGroundStationDownload = 60 * 90;
    public String inputDirectory = "accessFiles/";

    public int maxNumberOfSwapsPerMutation = 9;
    public double initialTemperature = 100;
    public double temperatureChange = 0.92;
    public int callsPerTermperatureChange = 1000;

    public double lowestTaskPriority = 1.0;
    public double taskPriorityIncrement = 0.5;
    public int numberOfTaskPriorityLevels = 5;

    public double taskPriorityWeight = 1;
    public double minimizeSlewingWeight = 0.0001;
    public double smallSlewFactor = 0.5;
    public double smallSlewWeight = smallSlewFactor * minimizeSlewingWeight * (1 / slewRate);

    public double smallSlewNormalizedWeight = .01;

    public float priorityWeight = 10;
    public float accessWindowWeight = 1;
    public float ssrWeight = 3;
    public SensorType sensorType = null;

    /**
     * Sets up all parameters
     */
    public EOSschedulingParameters() {
        setParameters();
    }

    public EOSschedulingParameters(int dummy) {
        // do nothing, for use by subclasses
    }

    private void setParameters() {
        populationSize = gov.nasa.javaGenes.hillClimbing.Breeder.requiredPopulationSize();
        kidsPerGeneration = 1;
        maximumGenerations = 100;
        frequencyOfASCIIPopulations = 100000;
        separateLogAndEvolvableFiles = true;
        stopAtPerfection = false;

        model = new EOSModel();
        STKAccessFile[] csvFiles = new STKAccessFile[1];
        csvFiles[0] = new STKAccessFile(inputDirectory + "2100Targets1Sat1.csv", timeBetweenSamples);
        //csvFiles[1] = new STKAccessFile(inputDirectory+"2100Targets1Sat2.csv",timeBetweenSamples);
        model.setHorizon(csvFiles[0].getHorizon());
        Sensor[] sensors = new Sensor[1];
        sensors[0] = createSatellite(1, "Sat1ToGroundStationWindows.csv");
        //sensors[1] = createSatellite(2,"Sat2ToGroundStationWindows.csv");
        TaskAndAccessWindowGenerator.generateTakeImages(model, takeImageTime, sensors, taskSSRuse, csvFiles);
        //csvFiles[0] = new STKAccessFile(inputDirectory+"2100Targets2Sat1.csv",timeBetweenSamples);
        //csvFiles[1] = new STKAccessFile(inputDirectory+"2100Targets2Sat2.csv",timeBetweenSamples);
        //TaskAndAccessWindowGenerator.generateTakeImages(model,takeImageTime,sensors,taskSSRuse,csvFiles);
        model.removeUnexecutableTasks();
        model.setTaskPriorities(lowestTaskPriority, taskPriorityIncrement, model.getNumberOfTasks() / numberOfTaskPriorityLevels);
        model.report("ModelDirectory");

        permutationLength = model.getNumberOfTasks();
        scheduler = new gov.nasa.javaGenes.EOSscheduling.HBSS.contention.Scheduler(model, 1, 1, 1);

        // Fitness objectives
        FitnessFunction numberOfTakeImages = new NumberOfTakeImagesFitness(scheduler);
        numberOfTakeImages.setName("numberOfTakeImages");
        FitnessFunction minimizeSlewing = new MinimizeSlewingFitness(scheduler, model);
        minimizeSlewing.setName("minimizeSlewing");
        FitnessFunction smallSlew = new SmallSlewsFitness(scheduler, model);
        smallSlew.setName("smallSlew");
        SchedulingFitnessFunction smallSlewNormalized = new SmallSlewsFitness(scheduler, model);
        smallSlewNormalized.setNormalization(SchedulingFitnessFunction.NORMALIZE_BY_NUMBER_OF_SCHEDULED_TAKEIMAGES);
        smallSlewNormalized.setName("smallSlewNormalized");
        FitnessFunction taskPriority = new TaskPriorityFitness(scheduler, model);
        taskPriority.setName("taskPriority");

        WeightedSumFitness weightedSum = new WeightedSumFitness();
        weightedSum.add(taskPriorityWeight, taskPriority);
        weightedSum.add(minimizeSlewingWeight, minimizeSlewing);
        weightedSum.add(smallSlewWeight, smallSlew);
        weightedSum.setName("weightedSum");

        WeightedSumFitness weightedSumNormalized = new WeightedSumFitness();
        weightedSumNormalized.add(taskPriorityWeight, taskPriority);
        weightedSumNormalized.add(minimizeSlewingWeight, minimizeSlewing);
        weightedSumNormalized.add(smallSlewNormalizedWeight, smallSlewNormalized);
        weightedSumNormalized.setName("weightedSumNormalized");

        FitnessFunctionMultiObjective eval = new FitnessFunctionMultiObjective();
        eval.add(1.0, numberOfTakeImages);
        eval.add(1.0, taskPriority);
        eval.add(1.0, minimizeSlewing);
        eval.add(1.0, smallSlew);
        eval.add(1.0, weightedSum); // put here to allow easy comparison with earlier runs
        eval.add(1.0, smallSlewNormalized);

        FitnessFunctionMultiObjective pareto = new FitnessFunctionMultiObjective();
        pareto.add(1.0, numberOfTakeImages);
        pareto.add(1.0, taskPriority);
        pareto.add(1.0, minimizeSlewing);
        pareto.add(1.0, smallSlewNormalized);

        fitnessFunction = weightedSumNormalized;
        evaluationFunction = eval;
        paretoFitness = pareto;

        scheduler = new gov.nasa.javaGenes.EOSscheduling.HBSS.Scheduler(model, priorityWeight, accessWindowWeight, ssrWeight);

        childMakerProvider = new gov.nasa.javaGenes.core.ChildMakerProvider();
        childMakerProvider.add(new gov.nasa.javaGenes.EOSscheduling.HBSS.ChildMaker());
        breeder = new gov.nasa.javaGenes.EOSscheduling.HBSS.Breeder(this);
    }

    private Sensor createSatellite(int number, String groundStationAccessFile) {
        Satellite satellite = new Satellite("Satellite" + number);
        satellite.generateGroundStationAccessWindows(new STKAccessFile(inputDirectory + groundStationAccessFile, timeBetweenSamples));
        satellite.setSSR(SSRcapacity, model.getHorizon(), timeBetweenGroundStationDownload);
        model.addSatellite(satellite);
        if (sensorType == null)
            sensorType = new SensorType("sensorType");
        Sensor sensor = new Sensor(satellite, sensorType, model.getHorizon(), takeImageTime);
        satellite.addSensor(sensor);
        SlewMotor slewMotor = new SlewMotor(slewRate, -90, 90, model.getHorizon(), takeImageTime); // modeled after ETM/ASTER
        sensor.setSlewMotor(slewMotor);
        satellite.addSlewMotor(slewMotor);
        slewMotor.addSensor(sensor);
        return sensor;
    }

    public void makeFiles() {
    }

}
