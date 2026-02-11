package com.CollabIDE.operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertOperation extends EditOperation{
    private int position;
    private String text;

   @Override
    public Type getType(){
       return Type.INSERT;
   }
}
