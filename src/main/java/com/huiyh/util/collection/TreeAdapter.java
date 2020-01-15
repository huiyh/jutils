package com.huiyh.util.collection;


import java.util.Collection;

public interface TreeAdapter<E> {

    Collection getChildren(E node);

    void onNext(E node, int deep);
}