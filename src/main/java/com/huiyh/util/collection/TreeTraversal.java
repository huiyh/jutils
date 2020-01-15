package com.huiyh.util.collection;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author : 惠远航
 * @date : 2020/1/14 16:28
 */
public class TreeTraversal<E> {

    public void exec(E root, TreeAdapter<E> adapter) {
        final int deep = 0;
        adapter.onNext(root, deep);

        Collection children = adapter.getChildren(root);
        if (children != null && !children.isEmpty()) {
            exec(children, adapter, deep + 1);
        }
    }

    public void exec(Collection<E> collection, TreeAdapter<E> adapter) {
        final int deep = 0;
        exec(collection, adapter, deep);
    }

    private void exec(Collection<E> collection, TreeAdapter<E> adapter, int deep) {
        Iterator<E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            E node = iterator.next();
            adapter.onNext(node, deep);
            Collection children = adapter.getChildren(node);
            boolean hasChildren = children != null && !children.isEmpty();
            if (hasChildren) {
                exec(children, adapter, deep + 1);
            }
        }
    }

}
