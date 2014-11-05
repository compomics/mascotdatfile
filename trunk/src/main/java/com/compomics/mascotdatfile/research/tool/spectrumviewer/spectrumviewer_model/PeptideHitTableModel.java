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

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.PeptideHit;

import javax.swing.table.AbstractTableModel;
import java.util.Hashtable;

/**
 * <b>Created by IntelliJ IDEA.</b> User: Kenni Date: 7-jul-2006 Time: 11:48:08
 * <p>
 * <br>This Class <br>
 */
public class PeptideHitTableModel extends AbstractTableModel {
    // Class specific log4j logger for PeptideHitTableModel instances.
    private static Logger logger = Logger.getLogger(PeptideHitTableModel.class);

    private Hashtable iRowElements = null;
    private PeptideHit[] iPh;
    private static PeptideHitTableModel iPeptideHitTableModel = null;

    private PeptideHitTableModel(PeptideHit[] aPh) {
        iPh = aPh;
        if (iRowElements == null) {
            iRowElements = new Hashtable();
            constructRowElements();
        }
    }

    /**
     * Singleton pattern makes sure ther's only one PeptideHitTableModel.
     *
     * @param aPh Array with peptidehits that you want to show in the table.
     * @return PeptideHitTableModel     a tablemodel that can handle peptidehits and shows them in a vertical table.
     */
    public static PeptideHitTableModel getPeptideHitTableModel(PeptideHit[] aPh) {
        if (iPeptideHitTableModel == null) {
            iPeptideHitTableModel = new PeptideHitTableModel(aPh);
        }
        return iPeptideHitTableModel;
    }

    /**
     * Singleton pattern makes sure ther's only one PeptideHitTableModel.
     *
     * @param aPh One peptidehit that you want to show in the table.
     * @return PeptideHitTableModel     a tablemodel that can handle the peptidehit and shows it in a vertical table.
     */
    public static PeptideHitTableModel getPeptideHitTableModel(PeptideHit aPh) {
        if (iPeptideHitTableModel == null) {
            iPeptideHitTableModel = new PeptideHitTableModel(new PeptideHit[]{aPh});
        }
        return iPeptideHitTableModel;
    }

    /**
     * Returns the number of rows, in this case the number of peptidehit information rows.
     *
     * @return int the number of rows with peptidehitinformation.
     */
    public int getRowCount() {
        return iRowElements.size();
    }

    /**
     * Returns the number of colums, in this case the number of peptidehits that are passed by the constructor.
     *
     * @return int the number of collums with peptidehits
     */
    public int getColumnCount() {
        return iPh.length;
    }

    /**
     * Returns the value of one cell
     *
     * @param rowIndex    the index of the row that needs to be accessed
     * @param columnIndex the index of the collumn that needs to be accessed
     * @return value of the requested cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object lValue = null;
        if (columnIndex == 0) {
            lValue = getRowElementInformation(rowIndex);
        } else if (columnIndex > 0) {
            lValue = getPeptideHitInformation(iPh[columnIndex - 1], rowIndex);
        }
        return lValue;
    }

    /**
     * This method builds the first collumn with row-names.
     *
     * @param rowIndex
     * @return String with the row-name
     */
    private String getRowElementInformation(int rowIndex) {
        String lValue = null;
        switch (rowIndex) {
            case 0:
                lValue = (String) iRowElements.get(PeptideHit.SEQUENCE);
                break;

            case 1:
                lValue = (String) iRowElements.get(PeptideHit.MODIFIED_SEQUENCE);
                break;

            case 2:
                lValue = (String) iRowElements.get(PeptideHit.IONS_SCORE);
                break;

            case 3:
                lValue = (String) iRowElements.get(PeptideHit.THRESHOLD);
                break;

            default:
                throw new IllegalArgumentException("Number of table rows is bigger then the RowElementInformation supply!!");

        }
        return lValue;
    }

    /**
     * This method builds the peptidehitinformation columns
     *
     * @param aPh      Peptidehit wherefrom you want the information
     * @param rowIndex The index of the information row you want the information from
     * @return String with peptidehit-information in this row
     */
    private String getPeptideHitInformation(PeptideHit aPh, int rowIndex) {
        String lValue = null;
        switch (rowIndex) {
            case 0:
                lValue = aPh.getSequence();
                break;

            case 1:
                lValue = aPh.getModifiedSequence();
                break;

            case 2:
                lValue = Double.toString(aPh.getIonsScore());
                break;

            case 3:
                lValue = Double.toString(aPh.calculateIdentityThreshold());
                break;

            default:
                throw new IllegalArgumentException("Number of table rows is bigger then the RowElementInformation supply!!");


        }
        return lValue;
    }

    /**
     * This is a HashTable for easy handling the rows.
     */
    private void constructRowElements() {
        iRowElements.put(PeptideHit.SEQUENCE, "Sequence");
        iRowElements.put(PeptideHit.MODIFIED_SEQUENCE, "Modified sequence");
        iRowElements.put(PeptideHit.IONS_SCORE, "Score");
        iRowElements.put(PeptideHit.THRESHOLD, "Threshold");
    }


}
