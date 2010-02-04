package com.compomics.mascotdatfile.util.interfaces;

import com.compomics.mascotdatfile.util.mascot.PeptideHit;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 10-jul-2008 Time: 17:26:47 To change this template use File | Settings |
 * File Templates.
 */
public interface QueryToPeptideMapInf {
    PeptideHit getPeptideHitOfOneQuery(int aQueryNumber, int aPeptideHitNumber);

    Vector getAllPeptideHits(int aQueryNumber);

    int getNumberOfPeptideHits(int aQueryNumber);

    int getNumberOfQueries();

    Vector getBestPeptideHits();

    Vector getPeptideHits(int aPeptideHitNumber);

    PeptideHit getPeptideHitOfOneQuery(int aQueryNumber);

    Vector getAllPeptideHitsAboveIdentityThreshold();

    Vector getAllPeptideHitsAboveIdentityThreshold(double aConfidence);

    Vector getPeptideHitsAboveIdentityThreshold(int aQueryNumber);

    Vector getPeptideHitsAboveIdentityThreshold(int aQueryNumber, double aConfidenceInterval);

    Vector getIdentifiedQueries(double aConfidence, Vector aQueryList);

    void buildProteinMap();
}
