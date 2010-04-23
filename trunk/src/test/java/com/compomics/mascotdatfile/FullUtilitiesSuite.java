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

package com.compomics.mascotdatfile;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA. User: Kenni Date: 22-jun-2006 Time: 15:51:07 This Suite tests all the components that are
 * build on the MascotDatfile library.
 */
public class FullUtilitiesSuite extends TestCase {
    // Class specific log4j logger for FullUtilitiesSuite instances.
    private static Logger logger = Logger.getLogger(FullUtilitiesSuite.class);

    public FullUtilitiesSuite() {
        super("Test the components that are build on the MascotDatfile library...");
    }

    public FullUtilitiesSuite(String aName) {
        super(aName);
    }

}
