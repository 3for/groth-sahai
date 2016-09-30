package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.Vector;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MatrixTest extends BaseTest {
    @Test
    public void testRandom() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                assertEquals(pairing.getG1(), random.get(i, j).getField());
    }

    @Test
    public void testRandom_noRows() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 0;

        assertNull(Matrix.random(pairing.getG1(), rows, cols));
    }

    @Test
    public void testRandom_noCols() {
        final Pairing pairing = createPairing();
        final int cols = 0;
        final int rows = 2;

        assertNull(Matrix.random(pairing.getG1(), rows, cols));
    }

    @Test
    public void testGetNumberOfCols() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);

        assertEquals(cols, random.getNumberOfCols());
    }

    @Test
    public void testGetNumberOfRows() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);

        assertEquals(rows, random.getNumberOfRows());
    }

    @Test
    public void testTranspose() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);
        final Matrix transpose = random.getTranspose();

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                assertEquals(random.get(i, j), transpose.get(j, i));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_wrongDimensions() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 2;
        final Matrix m1 = Matrix.random(pairing.getG1(), rows, cols);
        final Matrix m2 = Matrix.random(pairing.getG1(), rows, cols);

        m1.multiply(m2);
    }

    @Test
    public void testMultiply() {
        final Pairing pairing = createPairing();
        final int cols = 4;
        final int rows = 2;
        final Matrix m1 = Matrix.random(pairing.getG1(), rows, 3);
        final Matrix m2 = Matrix.random(pairing.getG1(), 3, cols);

        final Matrix multiply = m1.multiply(m2);
        assertEquals(rows, multiply.getNumberOfRows());
        assertEquals(cols, multiply.getNumberOfCols());
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                assertEquals(pairing.getG1(), multiply.get(i, j).getField());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyVector_wrongDimensions() {
        final Pairing pairing = createPairing();
        final int cols = 3;
        final int rows = 2;
        final Matrix m = Matrix.random(pairing.getZr(), rows, cols);
        final Vector v = generateVector(rows, pairing.getG1());

        m.multiply(v);
    }

    @Test
    public void testMultiplyVector() {
        final Pairing pairing = createPairing();
        final int cols = 4;
        final int rows = 2;
        final Matrix m = Matrix.random(pairing.getZr(), rows, cols);
        final Vector v = generateVector(cols, pairing.getG1());

        final Vector multiply = m.multiply(v);
        assertEquals(rows, multiply.getLength());
        for (int i = 0; i < rows; i++)
            assertEquals(pairing.getG1(), multiply.get(i).getField());
    }
}