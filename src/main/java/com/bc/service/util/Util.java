package com.bc.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author hp
 */
public class Util {
    
    public final static <E> List<E> asList(E [] array) {
        final List<E> output;
            switch (array.length) {
                case 0:
                    output = Collections.EMPTY_LIST;
                    break;
                case 1:
                    output = Collections.singletonList(array[0]);
                    break;
                default:
                    output = Arrays.asList(array);
                    break;
        }
        return output;
    }

    public final static <E> List<E> unmodifiableList(Collection<E> collection) {
        final List<E> output;
        if(collection.isEmpty()) {
            output = Collections.EMPTY_LIST;
        }else if(collection.size() == 1) {
            output = Collections.singletonList(collection.iterator().next());
        }else{
            final List list = collection instanceof List ? (List)collection : new ArrayList(collection);
            output = Collections.unmodifiableList(list);
        }
      return output;
    }

    public final static long availableMemory() {
        final Runtime runtime = Runtime.getRuntime();
        final long max = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
        final long availableHeapMemory = max - _usedMemory(runtime); // available memory i.e. Maximum heap size minus the current amount used
        return availableHeapMemory;
    }  

    public final static long usedMemory() {
        return _usedMemory(Runtime.getRuntime());
    }
    
    private static long _usedMemory(Runtime runtime) {
        final long total = runtime.totalMemory(); // current heap allocated to the VM process
        final long free = runtime.freeMemory(); // out of the current heap, how much is free
        final long used = total - free; // how much of the current heap the VM is using
        return used;
    }  
    
    public final static long usedMemory(long bookmarkMemory) {
        return bookmarkMemory - availableMemory();
    }
}
