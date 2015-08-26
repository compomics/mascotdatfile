/*
 * Copyright (C) 2006 - Helsens Kenny and Martens Lennart
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied.
 * 
 * See the License for the specific language governing permissions 
 * and limitations under the License.
 * 
 * 
 * 
 * Contact: 
 * kenny.helsens@ugent.be 
 * lennart.martens@ebi.ac.uk
 */

package com.compomics.mascotdatfile.research.tool.spectrumviewer.spectrumviewer_model;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.Query;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Created by IntelliJ IDEA. User: Kenni Date: 1-jun-2006 Time: 14:18:41 To change this template use File | Settings |
 * File Templates.
 */
public class DatfileTreeModel implements TreeModel {
    // Class specific log4j logger for DatfileTreeModel instances.
    private static Logger logger = Logger.getLogger(DatfileTreeModel.class);
    /**
     * MascotDatfile instance passes by the constructor.
     */
    private MascotDatfileInf iMascotDatfile = null;

    /**
     * Root of the Tree.
     */
    private String iRoot = null;

    private List iFilteredQueries = null;

    // Default, threshold is 0.05.
    private double iFilterSettingThreshold = 0.05;

    public DatfileTreeModel(final MascotDatfileInf aMascotDatfile, final String aRoot) {
        iMascotDatfile = aMascotDatfile;
        iRoot = aRoot;
    }

    /**
     * {@inheritDoc}
     */
    public Object getRoot() {
        return iRoot;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public Object getChild(Object parent, int index) {
        Object result = new Object();
        if (parent instanceof String) {
            if (iFilteredQueries == null) {
                CalculateFilteredQueries();
            }
            result = iFilteredQueries.get(index);
        } else if (parent instanceof Query) {
            int lQueryNumber = ((Query) parent).getQueryNumber();
            List<PeptideHit> lPeptidehits = iMascotDatfile.getQueryToPeptideMap().getPeptideHitsAboveIdentityThreshold(lQueryNumber, iFilterSettingThreshold);
            result = lPeptidehits.get(index);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int getChildCount(Object parent) {
        int lChildCount = 0;
        if (parent instanceof String) {
            if (iFilteredQueries == null) {
                CalculateFilteredQueries();
            }
            lChildCount = iFilteredQueries.size();
        } else if (parent instanceof Query) {
            int lQueryNumber = ((Query) parent).getQueryNumber();
            List<PeptideHit> lPeptidehits = iMascotDatfile.getQueryToPeptideMap().getPeptideHitsAboveIdentityThreshold(lQueryNumber, iFilterSettingThreshold);
            lChildCount = lPeptidehits.size();
        }
        return lChildCount;

    }

    /**
     * {@inheritDoc}
     */
    public boolean isLeaf(Object node) {
        boolean isLeaf = false;
        if (node instanceof PeptideHit) {
            isLeaf = true;
        }
        return isLeaf;
    }

    /**
     * {@inheritDoc}
     */
    public int getIndexOfChild(Object parent, Object child) {
        int lChildCount = getChildCount(parent);
        int result = 0;
        for (int i = 0; i < lChildCount; i++) {
            if (getChild(parent, i).equals(child)) {
                result = i;
            }
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    public void addTreeModelListener(TreeModelListener l) {

    }

    /**
     * {@inheritDoc}
     */
    public void removeTreeModelListener(TreeModelListener l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * Sets the setting to filter the threshold.
     *
     * @param aFilterSettingThreshold the filter threshold
     */
    public void setFilterSettingThreshold(double aFilterSettingThreshold) {
        iFilterSettingThreshold = aFilterSettingThreshold;
    }

    /**
     * Get the threshold setting.
     *
     * @return double threshold setting (alpha).
     */
    public double getFilterSettingThreshold() {
        return iFilterSettingThreshold;
    }

    public String getFilterSettingThresholdString() {
        return (new Double((1 - iFilterSettingThreshold) * 100)).intValue() + "%";
    }

    /**
     * Calculate filteredQueries by the current iFilterSettingThreshold.
     *
     * @return Vector with Filtered
     */
    private List CalculateFilteredQueries() {
        iFilteredQueries = iMascotDatfile.getQueryToPeptideMap().getIdentifiedQueries(iFilterSettingThreshold, iMascotDatfile.getQueryList());
        return iFilteredQueries;
    }
}
