package com.github.bluebridge.pclient.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.awt.*;

/**
 * Layout in one line (horizontally) 1 or more components.
 *
 * They are have same height that equal to height of container
 * (minus insets).
 * And width of each component dynamically stretched to
 * fill all width space of parent container but
 * maximum and minimum width of a component are taken into
 * account.
 *
 * So this layout manager require to specify all 3 sizes:
 * <ul>
 *     <li>minimum size</li>
 *     <li>preferred size. It cannot be less than minimum size.</li>
 *     <li>maximum size</li>
 * </ul>
 * Daneel Yaitskov
 */
public class LimitedWidthLayout implements LayoutManager {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LimitedWidthLayout.class);


    @Override
    public void addLayoutComponent(String s, Component component) {
        // do nothing
        LOGGER.debug("addLayoutComponent");
    }

    @Override
    public void removeLayoutComponent(Component component) {
        LOGGER.debug("removeLayoutComponent");
    }

    @Override
    public Dimension preferredLayoutSize(Container container) {
        Component[] children = container.getComponents();
        Insets insets = container.getInsets();
        double width = insets.left + insets.right;
        Double height = null;
        for (Component child : children) {
            Dimension pref = child.getPreferredSize();
            if (height == null) {
                height = pref.getHeight();
            } else {
                height = (height + pref.getHeight()) / 2.0;
            }
            width += pref.getWidth();
        }
        if (height == null) {
            height = 0.0;
        }
        height += insets.bottom + insets.top;
        LOGGER.debug("preferred layout width {} and height {}",
                width, height);
        return new Dimension((int) width, height.intValue());
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        Component[] children = container.getComponents();
        Insets insets = container.getInsets();
        double width = insets.left + insets.right;
        double height = 0.0;
        for (Component child : children) {
            Dimension minimumSize = child.getMinimumSize();
            height = Math.max(height, minimumSize.getHeight());
            width += minimumSize.getWidth();
        }
        height += insets.bottom + insets.top;
        LOGGER.debug("minimum layout width {} and height {}",
                width, height);
        return new Dimension((int) width, (int) height);
    }

    @Override
    public void layoutContainer(Container container) {
        new LimitedLayouter(container).compact();
    }
}
