/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Storage {
    private final Storage enclosing;
    private final Map<String, Object> vars = new HashMap<String, Object>();

    public Storage() { // global vars
        enclosing = null;
    }

    public Storage(Storage enclosing) { // local vars
        this.enclosing = enclosing;
    }

    public void define(String name, Object val) {
        vars.put(name, val);
    }

    public Object grab(String name, int line) {
        if (vars.containsKey(name)) {
            return vars.get(name);
        }
        else if (enclosing != null) { // search further up
            return enclosing.grab(name, line); 
        }
        else {
            throw new RuntimeError(name, "Undefined variable '" + name + "'.", line);
        }
    }

    @SuppressWarnings("unchecked")
    public Object grabArr(String name, int index, int line) {
        if (vars.containsKey(name)) {
            ArrayList<Object> list = (ArrayList<Object>) vars.get(name);
            try {
                return list.get(index);
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeError(name, "Index " + index + " out of bounds for array '" + name + "' with length " + list.size() + ".", line);
            }
        }
        else if (enclosing != null) {
            return enclosing.grabArr(name, index, line);
        }
        else {
            throw new RuntimeError(name, "Undefined array '" + name + "'.", line);
        }
    }

    public void assignVar(String name, Object val, int line) {
        if (vars.containsKey(name)) {
            vars.put(name, val);
        }
        else if (enclosing != null) { // search further up
            enclosing.assignVar(name, val, line);
        }
        else {
            throw new RuntimeError(name, "Undefined variable '" + name + "'.", line);
        }
    }

    @SuppressWarnings("unchecked")
    public void assignArr(String name, Object val, int index, int line) {
        if (vars.containsKey(name)) {
            ArrayList<Object> list = (ArrayList<Object>) vars.get(name);
            try {
                int pointer = list.size();
                while (pointer <= index) {
                    list.add(0);
                    pointer++;
                }
                list.set(index, val);
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeError(name, "Index " + index + " out of bounds for array '" + name + "' with length " + list.size() + ".", line);
            }
        }
        else if (enclosing != null) {
            enclosing.assignArr(name, val, index, line);
        }
        else {
            throw new RuntimeError(name, "Undefined array '" + name + "'.", line);
        }
    }
}