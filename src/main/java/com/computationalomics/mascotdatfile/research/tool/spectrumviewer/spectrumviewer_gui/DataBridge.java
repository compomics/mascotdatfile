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

package com.computationalomics.mascotdatfile.research.tool.spectrumviewer.spectrumviewer_gui;

import com.computationalomics.mascotdatfile.util.mascot.MascotDatfile;

/**
 * <b>Created by IntelliJ IDEA.</b> User: Kenni Date: 10-jul-2006 Time: 15:06:08
 * <p/>
 * <br>This Class </br>
 */
public interface DataBridge {

    /**
     * This method will be used by the dialogs to pass the the MascotDatfile to the Spectrumviewer_gui.
     *
     * @param aMdf MascotDatfile instance with a DatfileTreePanel passed by a dialog.
     */
    public void passMascotDatfile(MascotDatfile aMdf, String aFilename);

    /**
     * This method will be used by the dialogs to pass the FilterSettings to the GUI that contains the DatfileTreePanel.
     */
    public void passFilterSettings(double aIdentityThreshold);
}
