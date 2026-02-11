package com.CollabIDE.operation;

import com.CollabIDE.operation.EditOperation;

public interface OTEngine {

    EditOperation transform(EditOperation incoming, EditOperation concurrent);

    String apply(String content, EditOperation operation);
}
