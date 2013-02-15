package com.github.bluebridge.pclient.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is part of {@link LimitedWidthLayout}.
 * It's a core algorithm. It's to complex to be a method.
 *
 *
 * Daneel Yaitskov
 */
public class LimitedLayouter {

    private Container container;
    private Insets insets;
    private int finalHeight;
    private int withLeft;
    private Component[] children;
    private int[] finalWidth;
    private List<Integer> withNoWidth;
    private boolean removedSome;

    public LimitedLayouter(Container container) {
        this.container = container;
        insets = container.getInsets();
        withLeft = container.getWidth() - (insets.left + insets.right);
        finalHeight= container.getHeight() - (insets.bottom + insets.top);

        children = container.getComponents();
        finalWidth = new int[children.length];
        withNoWidth = new ArrayList<Integer>(children.length);
        for (int i = 0; i < children.length; ++i) {
            withNoWidth.add(i);
        }
    }

    /**
     * Gets average width of an child component.
     * Set width for components whose limits don't fit
     * average width select their max or min.
     * If there isn't any component with violated width
     * set rest components average width.
     */
    public void compact() {
        while (!withNoWidth.isEmpty()) {
            Iterator<Integer> iter = withNoWidth.iterator();
            int itemWidth = withLeft / withNoWidth.size();
            removedSome = false;
            while (iter.hasNext()) {
                findItemWith(iter, itemWidth);
            }
            if (!removedSome) {
                for (Integer i : withNoWidth) {
                    finalWidth[i] = itemWidth;
                }
                break;
            }
        }
        applySizes();
    }

    private void findItemWith(Iterator<Integer> iter, int itemWidth) {
        Integer i = iter.next();
        Dimension max = children[i].getMaximumSize();
        if (max.getWidth() < itemWidth) {
            finalWidth[i] = (int) max.getWidth();
            withLeft -= max.getWidth();
            iter.remove();
            removedSome = true;
        } else {
            Dimension min = children[i].getMinimumSize();
            if (min.getWidth() > itemWidth) {
                finalWidth[i] = (int) min.getWidth();
                withLeft -= min.getWidth();
                iter.remove();
                removedSome = true;
            }
        }
    }

    private void applySizes() {
        int x = insets.left;
        for (int i = 0; i < finalWidth.length; ++i) {
            children[i].setBounds(x, insets.top,
                    finalWidth[i],
                    finalHeight);
            x += finalWidth[i];
        }
    }
}
