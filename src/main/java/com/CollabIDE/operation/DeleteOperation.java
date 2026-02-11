package com.CollabIDE.operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteOperation extends EditOperation{
    private int position;
    private int length;

    @Override
    public Type getType() {
        return Type.DELETE;
    }
}
