package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MiniBoardTest {

    @Test
    public void testPlace() {
        int[] expectedBoard = {0, 0, 2,
                               1, 0, 1,
                               0, 0, 2};
        MiniBoard expected = new MiniBoard(expectedBoard);

        MiniBoard test = new MiniBoard().place(1, 0, 1).place(1, 2, 1).place(0, 2, 2).place(2, 2, 2);

        assertEquals(expected, test, "Mini board place functionality not holding in equivalence case.");
        assertThrows(UnsupportedOperationException.class, () -> test.place(1, 0, 1), "Mini board not throwing expected exception.");
    }

    @Test
    public void testWinner() {

    }

    @Test
    public void testIsBoardFull() {

    }

}
