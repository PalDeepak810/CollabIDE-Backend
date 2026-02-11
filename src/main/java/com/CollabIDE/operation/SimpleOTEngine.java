package com.CollabIDE.operation;

import org.springframework.stereotype.Component;

@Component
public class SimpleOTEngine implements OTEngine {

    @Override
    public EditOperation transform(EditOperation incoming, EditOperation concurrent) {

        if (incoming instanceof InsertOperation i1 && concurrent instanceof InsertOperation i2) {
            return transformInsertInsert(i1, i2);
        }

        if (incoming instanceof InsertOperation i && concurrent instanceof DeleteOperation d) {
            return transformInsertDelete(i, d);
        }

        if (incoming instanceof DeleteOperation d && concurrent instanceof InsertOperation i) {
            return transformDeleteInsert(d, i);
        }

        if (incoming instanceof DeleteOperation d1 && concurrent instanceof DeleteOperation d2) {
            return transformDeleteDelete(d1, d2);
        }

        return incoming;
    }

    // ---------------- INSERT vs INSERT ----------------
    private EditOperation transformInsertInsert(
            InsertOperation incoming,
            InsertOperation concurrent
    ) {
        InsertOperation result = cloneInsert(incoming);

        if (concurrent.getPosition() < result.getPosition()) {
            result.setPosition(result.getPosition() + concurrent.getText().length());
        } else if (concurrent.getPosition() == result.getPosition()) {
            // deterministic tie-breaker
            if (concurrent.getUserId().compareTo(result.getUserId()) < 0) {
                result.setPosition(result.getPosition() + concurrent.getText().length());
            }
        }
        return result;
    }

    // ---------------- INSERT vs DELETE ----------------
    private EditOperation transformInsertDelete(
            InsertOperation incoming,
            DeleteOperation concurrent
    ) {
        InsertOperation result = cloneInsert(incoming);

        if (concurrent.getPosition() < result.getPosition()) {
            int shift = Math.min(
                    concurrent.getLength(),
                    result.getPosition() - concurrent.getPosition()
            );
            result.setPosition(result.getPosition() - shift);
        }
        return result;
    }

    // ---------------- DELETE vs INSERT ----------------
    private EditOperation transformDeleteInsert(
            DeleteOperation incoming,
            InsertOperation concurrent
    ) {
        DeleteOperation result = cloneDelete(incoming);

        int insertPos = concurrent.getPosition();
        int insertLen = concurrent.getText().length();

        if (insertPos < result.getPosition()) {
            result.setPosition(result.getPosition() + insertLen);
        } else if (insertPos >= result.getPosition()
                && insertPos < result.getPosition() + result.getLength()) {
            // insert inside delete range → extend delete
            result.setLength(result.getLength() + insertLen);
        }

        return result;
    }

    // ---------------- DELETE vs DELETE ----------------
    private EditOperation transformDeleteDelete(
            DeleteOperation incoming,
            DeleteOperation concurrent
    ) {
        DeleteOperation result = cloneDelete(incoming);

        int aStart = result.getPosition();
        int aEnd = aStart + result.getLength();
        int bStart = concurrent.getPosition();
        int bEnd = bStart + concurrent.getLength();

        if (bEnd <= aStart) {
            result.setPosition(aStart - concurrent.getLength());
            return result;
        }

        if (bStart >= aEnd) {
            return result;
        }

        int overlapStart = Math.max(aStart, bStart);
        int overlapEnd = Math.min(aEnd, bEnd);

        result.setLength(result.getLength() - (overlapEnd - overlapStart));
        result.setPosition(Math.min(aStart, bStart));

        return result;
    }

    // ---------------- APPLY OPERATION ----------------
    @Override
    public String apply(String content, EditOperation operation) {

        if (operation instanceof InsertOperation insert) {
            int pos = Math.max(0, Math.min(insert.getPosition(), content.length()));
            return content.substring(0, pos)
                    + insert.getText()
                    + content.substring(pos);
        }

        if (operation instanceof DeleteOperation delete) {

            int start = delete.getPosition();
            int end = start + delete.getLength();

            //  SAFETY GUARDS (CRITICAL)
            if (start < 0) start = 0;
            if (end > content.length()) end = content.length();
            if (start >= end) return content; // no-op delete

            return content.substring(0, start) + content.substring(end);
        }

        return content;
    }

    // ---------------- CLONERS ----------------
    private InsertOperation cloneInsert(InsertOperation op) {
        InsertOperation clone = new InsertOperation();
        clone.setSessionId(op.getSessionId());
        clone.setFilePath(op.getFilePath());
        clone.setUserId(op.getUserId());
        clone.setBaseVersion(op.getBaseVersion());
        clone.setTimestamp(op.getTimestamp());
        clone.setPosition(op.getPosition());
        clone.setText(op.getText());
        return clone;
    }

    private DeleteOperation cloneDelete(DeleteOperation op) {
        DeleteOperation clone = new DeleteOperation();
        clone.setSessionId(op.getSessionId());
        clone.setFilePath(op.getFilePath());
        clone.setUserId(op.getUserId());
        clone.setBaseVersion(op.getBaseVersion());
        clone.setTimestamp(op.getTimestamp());
        clone.setPosition(op.getPosition());
        clone.setLength(op.getLength());
        return clone;
    }
}
