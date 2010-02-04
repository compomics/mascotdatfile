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

package com.computationalomics.mascotdatfile.research.tool.spectrumviewer.spectrumviewer_model;

import com.computationalomics.mascotdatfile.util.mascot.PeptideHit;
import com.computationalomics.mascotdatfile.util.mascot.Query;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Kenni
 * Date: 1-jun-2006
 * Time: 17:14:36
 * To change this template use File | Settings | File Templates.
 */

/**
 * A TreecellRender for MascotDatfile. Can render a QueryToPeptideMap with QueryNumber as nodes and PeptideHitSequences with score as leafs.
 */
public class DatfileTreeCellRenderer extends DefaultTreeCellRenderer {


    /** {@inheritDoc} */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Object temp = value;
        if(value instanceof Query){
            temp = "Query " + ((Query)temp).getQueryNumber();
        } else if(value instanceof PeptideHit){
            PeptideHit lPeptideHit = (PeptideHit) value;
            temp = lPeptideHit.getSequence() + " (" +  lPeptideHit.getIonsScore() + ")";

        }
        return super.getTreeCellRendererComponent(tree, temp, sel, expanded, leaf, row, hasFocus);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
