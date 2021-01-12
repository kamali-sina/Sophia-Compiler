package main.ast.types.list;

import main.ast.types.Type;

import java.util.ArrayList;

public class ListType extends Type {
    private ArrayList<ListNameType> elementsTypes = new ArrayList<>();

    public ListType() {
    }

    public ListType(ArrayList<ListNameType> elementsTypes) {
        this.elementsTypes = elementsTypes;
    }

    public ListType(int listSize, ListNameType listNameType) {
        for(int i=0; i < listSize; i++) {
            elementsTypes.add(listNameType);
        }
    }

    public ArrayList<ListNameType> getElementsTypes() {
        return elementsTypes;
    }

    public void setElementsTypes(ArrayList<ListNameType> elementsTypes) {
        this.elementsTypes = elementsTypes;
    }

    public void addElementType(ListNameType listNameType) {
        this.elementsTypes.add(listNameType);
    }

    @Override
    public String toString() {
        return "ListType";
    }
}
