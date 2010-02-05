package com.compomics.mascotdatfile.util.mascot.iterator;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.Query;

import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 16-jul-2008 Time: 11:18:54 To change this template use File | Settings |
 * File Templates.
 */
public class QueryEnumerator implements Enumeration {
    /**
     * Reference to the MascotDatfile implementing object.
     */
    private MascotDatfileInf iMascotDatfileInf;

    /**
     * The number of queries availlable.
     */
    private int iNumberOfQueries;

    /**
     * The counter of the enumaration.
     */
    private int iCounter = 0;

    /**
     * Constructs a new Query enumerating object for the given MascotDatfileInf.
     * @param aMascotDatfileInf MascotDatfileInf instance holding the queries to enumerate.
     */
    public QueryEnumerator(MascotDatfileInf aMascotDatfileInf) {
        iMascotDatfileInf = aMascotDatfileInf;
        iNumberOfQueries = aMascotDatfileInf.getNumberOfQueries();
    }

    /**
     * Returns the status if there are more queries in the MascotDatfile.
     * @return <code>true</code> if more Queries. <code>false</code> if no more Queries.
     */
    public boolean hasMoreElements() {
        // True as long as counter will return another query. (Note that the getQueryMethod
        return (iCounter < iNumberOfQueries);
    }

    /**
     * Returns the next Query instance of the MascotDatfile.
     * @return Query
     */
    public Query nextElement() {
        // The getQuery() method is '1' based while the counter is '0' based.
        Query lQuery = iMascotDatfileInf.getQuery(iCounter + 1);

        // Increase the counter;
        iCounter++;

        return lQuery;
    }
}
