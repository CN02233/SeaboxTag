package com.seabox.tagsys.usertags.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * @author Changhua, Wu
 *         Created on: 2/5/16,12:49 PM
 */
public class BeanSorter<T> implements Comparator<T> {

    private static final Logger logger = LoggerFactory.getLogger(BeanSorter.class);

    private String      sortColumn;
    private boolean     isAsc;
    Method              columnGetter;

    private BeanSorter() {
    }

    public static <T> BeanSorter<T>  createASorter(String sortColumn, boolean isAsc, Class<T> beanClass) {
        BeanSorter sorter = new BeanSorter();
        sorter.sortColumn = sortColumn;
        sorter.isAsc = isAsc;
        try {
            PropertyDescriptor prodDesc = new PropertyDescriptor(sortColumn, beanClass);
            sorter.columnGetter = prodDesc.getReadMethod();

            Class<?> propType = prodDesc.getPropertyType();

            boolean isComparable = true;
            try {
                if(!propType.isPrimitive()) { // all primitive is already Comparable
                    Class<? extends Comparable> compareClass = propType.asSubclass( Comparable.class );
                    if(compareClass == null) {
                        isComparable = false;
                    }
                }
            } catch (Throwable e) {
                isComparable = false;
            }

            if(!isComparable) {
                sorter = null;
            }

            return sorter;

        } catch (IntrospectionException e) {
            logger.debug("failed to access Bean field: {}, details exception:", sortColumn, e);
            return null;
        }

    }

    @Override
    public int compare(T o1, T o2) {
        try {

            Object value1 = columnGetter.invoke(o1);
            Object value2 = columnGetter.invoke(o2);

            if(value1 == null) {
                if(value2 != null) {
                     return isAsc ? -1 : 1;
                } else {
                    return 0;
                }

            } else if(value2 == null) { // v1 not null, v2 is null
                return isAsc ? 1 : -1;
            }

            // both are not null
            if(value1 instanceof Comparable) {
                int result = ((Comparable)value1).compareTo( value2 );
                if(!isAsc) {
                    result = 0 - result;
                }
                return result;
            } else {
                logger.error("requested sort field {} is not Comparable!", sortColumn);
            }

        } catch (InvocationTargetException e) {
            logger.error("failed to access Bean getter for field: {}, details exception:", sortColumn, e);
        } catch (IllegalAccessException e) {
            logger.error("failed to access Bean getter for field: {}, details exception:", sortColumn, e);
        }
        return 0;
    }

}
