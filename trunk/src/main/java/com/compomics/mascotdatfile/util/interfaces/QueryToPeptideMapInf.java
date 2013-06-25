package com.compomics.mascotdatfile.util.interfaces;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.Query;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 10-jul-2008 Time: 17:26:47 To change this template use File | Settings |
 * File Templates.
 */
public interface QueryToPeptideMapInf {
    PeptideHit getPeptideHitOfOneQuery(int aQueryNumber, int aPeptideHitNumber);

    List<PeptideHit> getAllPeptideHits(int aQueryNumber);

    int getNumberOfPeptideHits(int aQueryNumber);

    int getNumberOfQueries();

    List<PeptideHit> getBestPeptideHits();

    List<PeptideHit> getPeptideHits(int aPeptideHitNumber);

    PeptideHit getPeptideHitOfOneQuery(int aQueryNumber);

    List<PeptideHit> getAllPeptideHitsAboveIdentityThreshold();

    List<PeptideHit> getAllPeptideHitsAboveIdentityThreshold(double aConfidence);

    List<PeptideHit> getPeptideHitsAboveIdentityThreshold(int aQueryNumber);

    List<PeptideHit> getPeptideHitsAboveIdentityThreshold(int aQueryNumber, double aConfidenceInterval);

    List getIdentifiedQueries(double aConfidence, List<Query> aQueryList);

    void buildProteinMap();
}
