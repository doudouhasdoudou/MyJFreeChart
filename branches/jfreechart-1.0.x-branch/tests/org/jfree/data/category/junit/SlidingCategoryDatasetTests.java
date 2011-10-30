/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2011, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * --------------------------------
 * SlidingCategoryDatasetTests.java
 * --------------------------------
 * (C) Copyright 2008, 2009, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 08-May-2008 : Version 1 (DG);
 * 15-Mar-2009 : Added testGetColumnKeys() (DG);
 *
 */

package org.jfree.data.category.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

/**
 * Tests for the {@link SlidingCategoryDataset} class.
 */
public class SlidingCategoryDatasetTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(SlidingCategoryDatasetTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public SlidingCategoryDatasetTests(String name) {
        super(name);
    }

    /**
     * Some checks for the equals() method.
     */
    public void testEquals() {
        DefaultCategoryDataset u1 = new DefaultCategoryDataset();
        u1.addValue(1.0, "R1", "C1");
        u1.addValue(2.0, "R1", "C2");
        SlidingCategoryDataset d1 = new SlidingCategoryDataset(u1, 0, 5);
        DefaultCategoryDataset u2 = new DefaultCategoryDataset();
        u2.addValue(1.0, "R1", "C1");
        u2.addValue(2.0, "R1", "C2");
        SlidingCategoryDataset d2 = new SlidingCategoryDataset(u2, 0, 5);
        assertTrue(d1.equals(d2));

        d1.setFirstCategoryIndex(1);
        assertFalse(d1.equals(d2));
        d2.setFirstCategoryIndex(1);
        assertTrue(d1.equals(d2));

        d1.setMaximumCategoryCount(99);
        assertFalse(d1.equals(d2));
        d2.setMaximumCategoryCount(99);
        assertTrue(d1.equals(d2));

        u1.addValue(3.0, "R1", "C3");
        assertFalse(d1.equals(d2));
        u2.addValue(3.0, "R1", "C3");
        assertTrue(d1.equals(d2));
    }

    /**
     * Confirm that cloning works.
     */
    public void testCloning() {
        DefaultCategoryDataset u1 = new DefaultCategoryDataset();
        u1.addValue(1.0, "R1", "C1");
        u1.addValue(2.0, "R1", "C2");
        SlidingCategoryDataset d1 = new SlidingCategoryDataset(u1, 0, 5);
        SlidingCategoryDataset d2 = null;
        try {
            d2 = (SlidingCategoryDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));

        // basic check for independence
        u1.addValue(3.0, "R1", "C3");
        assertFalse(d1.equals(d2));
        DefaultCategoryDataset u2
                = (DefaultCategoryDataset) d2.getUnderlyingDataset();
        u2.addValue(3.0, "R1", "C3");
        assertTrue(d1.equals(d2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {
        DefaultCategoryDataset u1 = new DefaultCategoryDataset();
        u1.addValue(1.0, "R1", "C1");
        u1.addValue(2.0, "R1", "C2");
        SlidingCategoryDataset d1 = new SlidingCategoryDataset(u1, 0, 5);
        SlidingCategoryDataset d2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            d2 = (SlidingCategoryDataset) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(d1, d2);

        // basic check for independence
        u1.addValue(3.0, "R1", "C3");
        assertFalse(d1.equals(d2));
        DefaultCategoryDataset u2
                = (DefaultCategoryDataset) d2.getUnderlyingDataset();
        u2.addValue(3.0, "R1", "C3");
        assertTrue(d1.equals(d2));
    }

    /**
     * Some checks for the getColumnCount() method.
     */
    public void testGetColumnCount() {
        DefaultCategoryDataset underlying = new DefaultCategoryDataset();
        SlidingCategoryDataset dataset = new SlidingCategoryDataset(underlying,
                10, 2);
        assertEquals(0, dataset.getColumnCount());
        underlying.addValue(1.0, "R1", "C1");
        assertEquals(0, dataset.getColumnCount());
        underlying.addValue(1.0, "R1", "C2");
        assertEquals(0, dataset.getColumnCount());
        dataset.setFirstCategoryIndex(0);
        assertEquals(2, dataset.getColumnCount());
        underlying.addValue(1.0, "R1", "C3");
        assertEquals(2, dataset.getColumnCount());
        dataset.setFirstCategoryIndex(2);
        assertEquals(1, dataset.getColumnCount());
        underlying.clear();
        assertEquals(0, dataset.getColumnCount());
    }

    /**
     * Some checks for the getRowCount() method.
     */
    public void testGetRowCount() {
        DefaultCategoryDataset underlying = new DefaultCategoryDataset();
        SlidingCategoryDataset dataset = new SlidingCategoryDataset(underlying,
                10, 5);
        assertEquals(0, dataset.getRowCount());
        underlying.addValue(1.0, "R1", "C1");
        assertEquals(1, dataset.getRowCount());

        underlying.clear();
        assertEquals(0, dataset.getRowCount());
    }

    /**
     * Some checks for the getColumnIndex() method.
     */
    public void testGetColumnIndex() {
        DefaultCategoryDataset underlying = new DefaultCategoryDataset();
        underlying.addValue(1.0, "R1", "C1");
        underlying.addValue(2.0, "R1", "C2");
        underlying.addValue(3.0, "R1", "C3");
        underlying.addValue(4.0, "R1", "C4");
        SlidingCategoryDataset dataset = new SlidingCategoryDataset(underlying,
                1, 2);
        assertEquals(-1, dataset.getColumnIndex("C1"));
        assertEquals(0, dataset.getColumnIndex("C2"));
        assertEquals(1, dataset.getColumnIndex("C3"));
        assertEquals(-1, dataset.getColumnIndex("C4"));
    }

    /**
     * Some checks for the getRowIndex() method.
     */
    public void testGetRowIndex() {
        DefaultCategoryDataset underlying = new DefaultCategoryDataset();
        underlying.addValue(1.0, "R1", "C1");
        underlying.addValue(2.0, "R2", "C1");
        underlying.addValue(3.0, "R3", "C1");
        underlying.addValue(4.0, "R4", "C1");
        SlidingCategoryDataset dataset = new SlidingCategoryDataset(underlying,
                1, 2);
        assertEquals(0, dataset.getRowIndex("R1"));
        assertEquals(1, dataset.getRowIndex("R2"));
        assertEquals(2, dataset.getRowIndex("R3"));
        assertEquals(3, dataset.getRowIndex("R4"));
    }

    /**
     * Some checks for the getValue() method.
     */
    public void testGetValue() {
        DefaultCategoryDataset underlying = new DefaultCategoryDataset();
        underlying.addValue(1.0, "R1", "C1");
        underlying.addValue(2.0, "R1", "C2");
        underlying.addValue(3.0, "R1", "C3");
        underlying.addValue(4.0, "R1", "C4");
        SlidingCategoryDataset dataset = new SlidingCategoryDataset(underlying,
                1, 2);
        assertEquals(new Double(2.0), dataset.getValue("R1", "C2"));
        assertEquals(new Double(3.0), dataset.getValue("R1", "C3"));
        boolean pass = false;
        try {
            dataset.getValue("R1", "C1");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            dataset.getValue("R1", "C4");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getColumnKeys() method.
     */
    public void testGetColumnKeys() {
        DefaultCategoryDataset underlying = new DefaultCategoryDataset();
        underlying.addValue(1.0, "R1", "C1");
        underlying.addValue(2.0, "R1", "C2");
        underlying.addValue(3.0, "R1", "C3");
        underlying.addValue(4.0, "R1", "C4");
        SlidingCategoryDataset dataset = new SlidingCategoryDataset(underlying,
                1, 2);
        List keys = dataset.getColumnKeys();
        assertTrue(keys.contains("C2"));
        assertTrue(keys.contains("C3"));
        assertEquals(2, keys.size());

        dataset.setFirstCategoryIndex(3);
        keys = dataset.getColumnKeys();
        assertTrue(keys.contains("C4"));
        assertEquals(1, keys.size());
    }

}
