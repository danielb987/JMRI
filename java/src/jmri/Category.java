package jmri;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A category of something.
 * <P>
 * Category was created for LogixNG actions and expressions but it can be used
 * for everything in JMRI that needs "extendable enums".
 * <P>
 * This class is intended to be an Enum, but implemented as an abstract class
 * to allow adding more categories later without needing to change this class.
 * For example, external programs using JMRI as a lib might want to add their
 * own categories.
 *
 * @author Daniel Bergqvist Copyright 2018
 */
public abstract class Category implements Comparable<Category> {

    /**
     * Other things.
     * This category is always at the bottom of the list.
     */
    public static final Other OTHER = new Other();

    static {
        // It's not often any item is added to this list so we use CopyOnWriteArrayList
        _categories = new CopyOnWriteArrayList<>();
        registerCategory(OTHER);
    }

    /**
     * Get all the registered Categories
     * @return a list of categories
     */
    public static List<Category> values() {
        return Collections.unmodifiableList(_categories);
    }

    /**
     * Register a category
     * @param category the category
     */
    public static void registerCategory(Category category) {
        for (Category c : _categories) {
            if (c.equals(category)) {
                throw new IllegalArgumentException(String.format("Category '%s' is already registered", category._name));
            }
        }
        _categories.add(category);
    }


    private static final List<Category> _categories;

    private final String _name;
    private final String _description;
    private final int _order;


    protected Category(String name, String description, int order) {
        _name = name;
        _description = description;
        _order = order;
    }

    public final String name() {
        return _name;
    }

    @Override
    public final String toString() {
        return _description;
    }

    public final int order() {
        return _order;
    }

    @Override
    public final boolean equals(Object o) {
        if (o instanceof Category) {
            Category c = (Category)o;
            return _description.equals(c._description) && _name.equals(c._name);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return _description.hashCode();
    }

    @Override
    public final int compareTo(Category c) {
        if (_order < c.order()) return -1;
        if (_order > c.order()) return 1;
        return toString().compareTo(c.toString());
    }


    public static final class Other extends Category {

        public Other() {
            super("OTHER", Bundle.getMessage("CategoryOther"), Integer.MAX_VALUE);
        }
    }

}
