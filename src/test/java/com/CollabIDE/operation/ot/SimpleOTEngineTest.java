package com.CollabIDE.operation.ot;

import com.CollabIDE.operation.DeleteOperation;
import com.CollabIDE.operation.EditOperation;
import com.CollabIDE.operation.InsertOperation;
import com.CollabIDE.operation.SimpleOTEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleOTEngineTest {

    private SimpleOTEngine otEngine;

    @BeforeEach
    void setup() {
        otEngine = new SimpleOTEngine();
    }

    @Test
    void sanityCheck() {
        assertNotNull(otEngine);
    }

    //verifies positional shift
    @Test
    void insertInsert_differentPositions() {

        InsertOperation op1 = new InsertOperation();
        op1.setPosition(5);
        op1.setText("A");
        op1.setUserId("user1");

        InsertOperation op2 = new InsertOperation();
        op2.setPosition(2);
        op2.setText("B");
        op2.setUserId("user2");

        EditOperation transformed = otEngine.transform(op1, op2);

        assertEquals(6, ((InsertOperation) transformed).getPosition());
    }

    //proves determinism
    @Test
    void insertInsert_samePosition_tieBreaker() {

        InsertOperation op1 = new InsertOperation();
        op1.setPosition(3);
        op1.setText("X");
        op1.setUserId("userB");

        InsertOperation op2 = new InsertOperation();
        op2.setPosition(3);
        op2.setText("Y");
        op2.setUserId("userA"); // lexicographically smaller

        EditOperation transformed = otEngine.transform(op1, op2);

        assertEquals(4, ((InsertOperation) transformed).getPosition());
    }


    @Test
    void insertDelete_shiftLeft() {

        InsertOperation insert = new InsertOperation();
        insert.setPosition(6);
        insert.setText("X");

        DeleteOperation delete = new DeleteOperation();
        delete.setPosition(3);
        delete.setLength(2);

        EditOperation transformed = otEngine.transform(insert, delete);

        assertEquals(4, ((InsertOperation) transformed).getPosition());
    }


    @Test
    void deleteInsert_shiftRight() {

        DeleteOperation delete = new DeleteOperation();
        delete.setPosition(4);
        delete.setLength(2);

        InsertOperation insert = new InsertOperation();
        insert.setPosition(2);
        insert.setText("XX");

        EditOperation transformed = otEngine.transform(delete, insert);

        assertEquals(6, ((DeleteOperation) transformed).getPosition());
    }


    @Test
    void applyInsert() {

        String content = "abcd";

        InsertOperation insert = new InsertOperation();
        insert.setPosition(2);
        insert.setText("XX");

        String result = otEngine.apply(content, insert);

        assertEquals("abXXcd", result);
    }


    @Test
    void applyDelete() {

        String content = "abcdef";

        DeleteOperation delete = new DeleteOperation();
        delete.setPosition(2);
        delete.setLength(3);

        String result = otEngine.apply(content, delete);

        assertEquals("abf", result);
    }



}
