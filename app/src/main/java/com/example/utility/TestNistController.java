package com.example.utility;

import net.stamfest.randomtests.bits.*;
import net.stamfest.randomtests.nist.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestNistController implements ViewVerificaAleatorieta{

    final private int M = 520;
    final private int template_lenght = 9;
    final private int m = 2;
    final private int block_size = 16;
    //istanze dei metodi
    private Frequency frequency;
    private BlockFrequency blockFrequency;
    private CumulativeSums cumulativeSums;
    private Runs runs;
    private LongestRunOfOnes longestRunOfOnes;
    //private Rank rank;
    private DiscreteFourierTransform discreteFourierTransform;
    private NonOverlappingTemplateMatchings nonOverlappingTemplateMatchings;
    private OverlappingTemplateMatching overlappingTemplateMatching;
    private Universal universal;
    private ApproximateEntropy approximateEntropy;
    private RandomExcursions randomExcursions;
    private RandomExcursionsVariant randomExcursionsVariant;
    private Serial serial;
    private LinearComplexity linearComplexity;

    private List<Boolean> results;

    public TestNistController(){
        this.frequency = new Frequency();
        this.blockFrequency = new BlockFrequency(block_size);
        this.cumulativeSums = new CumulativeSums();
        this.runs = new Runs();
        this.longestRunOfOnes = new LongestRunOfOnes();
        //this.rank = new Rank();
        this.discreteFourierTransform = new DiscreteFourierTransform();
        this.nonOverlappingTemplateMatchings = new NonOverlappingTemplateMatchings(template_lenght);
        this.overlappingTemplateMatching = new OverlappingTemplateMatching(template_lenght);
        this.universal = new Universal();
        this.approximateEntropy = new ApproximateEntropy(m);
        this.randomExcursions = new RandomExcursions();
        this.randomExcursionsVariant = new RandomExcursionsVariant();
        this.serial = new Serial(m);
        this.linearComplexity = new LinearComplexity(M);

        this.results = new ArrayList<Boolean>();
    }

    @Override
    public void testRandomness(String toTest){
        //pulisco la lista dagli elementi salvati
        this.results.clear();

        byte[] input = toTest.getBytes(StandardCharsets.UTF_8);
        //bits è la struttura dati apposita per passare i dati ai metodi di testing
        Bits bits = new ArrayBits(input);

        //eseguo i test e metto gli esiti nella list di boolean
        results.add(this.frequency.runTest(bits)[0].isPassed());
        results.add(this.blockFrequency.runTest(bits)[0].isPassed());
        results.add(this.cumulativeSums.runTest(bits)[0].isPassed());
        //results.add(this.runs.runTest(bits)[0].isPassed());
        results.add(this.longestRunOfOnes.runTest(bits)[0].isPassed());
        //results.add(rank.runTest(bits));
        results.add(this.discreteFourierTransform.runTest(bits)[0].isPassed());
        results.add(this.nonOverlappingTemplateMatchings.runTest(bits)[0].isPassed());
        results.add(this.overlappingTemplateMatching.runTest(bits)[0].isPassed());
        results.add(this.universal.runTest(bits)[0].isPassed());
        results.add(this.approximateEntropy.runTest(bits)[0].isPassed());
        results.add(this.randomExcursions.runTest(bits)[0].isPassed());
        results.add(this.randomExcursionsVariant.runTest(bits)[0].isPassed());
        results.add(this.serial.runTest(bits)[0].isPassed());
        results.add(this.linearComplexity.runTest(bits)[0].isPassed());
    }

    public String toString(){
        String output = new String();

        output += "Frequency Test: ";
        if(results.get(0)) output += "passed\n";
        else output += "failed\n";

        output += "Block Frequency Test: ";
        if(results.get(1)) output += "passed\n";
        else output += "failed\n";

        output += "Cumulative Sums Test: ";
        if(results.get(2)) output += "passed\n";
        else output += "failed\n";

        output += "Runs Test: ";
        if(results.get(3)) output += "passed\n";
        else output += "failed\n";

        output += "Longest Run of Ones Test: ";
        if(results.get(4)) output += "passed\n";
        else output += "failed\n";

        /*output += "Rank Test: ";
        if(results.get(5)) output += "passed\n";
        else output += "failed\n";*/

        output += "Discrete Fourier Transform Test: ";
        if(results.get(5)) output += "passed\n";
        else output += "failed\n";

        output += "Non Overlapping Template Matching Test: ";
        if(results.get(6)) output += "passed\n";
        else output += "failed\n";

        output += "Overlapping Template Matching Test: ";
        if(results.get(7)) output += "passed\n";
        else output += "failed\n";

        output += "Maurer's Test: ";
        if(results.get(8)) output += "passed\n";
        else output += "failed\n";

        output += "Approximate Entropy Test: ";
        if(results.get(9)) output += "passed\n";
        else output += "failed\n";

        output += "Random Excursions Test: ";
        if(results.get(10)) output += "passed\n";
        else output += "failed\n";

        output += "Random Excursions Variant Test: ";
        if(results.get(11)) output += "passed\n";
        else output += "failed\n";

        output += "Serial Test: ";
        if(results.get(12)) output += "passed\n";
        else output += "failed\n";

        output += "Linear Complexity Test: ";
        if(results.get(13)) output += "passed\n";
        else output += "failed\n";

        return output;
    }

    public String securityLevel(){
        /*definisco tre livelli di sicurezza:
         * Low -> passati da 0 a 4 test
         * Medium -> passati da 5 a 9 test
         * High -> passati da 10 a 14 */

        String secLevel = new String();

        int numPassed = 0;
        for(int k = 0; k < this.results.size(); k++){
            if(this.results.get(k))
                numPassed++;
        }

        if (numPassed <= 4){
            secLevel = "Not Secure";
        }else if(numPassed <= 9){
            secLevel = "Moderately Secure";
        }else if(numPassed > 9){
            secLevel = "Highly Secure";
        }
        return secLevel;
    }

}
