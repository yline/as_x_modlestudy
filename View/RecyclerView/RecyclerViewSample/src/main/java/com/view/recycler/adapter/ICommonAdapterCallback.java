package com.view.recycler.adapter;

import java.util.Collection;

/**
 * Created by yline on 2016/9/22.
 */
public interface ICommonAdapterCallback<E>
{
	/**
	 * Attempts to add {@code object} to the contents of this
	 * {@code Collection} (optional).
	 * <p>
	 * After this method finishes successfully it is guaranteed that the object
	 * is contained in the collection.
	 * <p>
	 * If the collection was modified it returns {@code true}, {@code false} if
	 * no changes were made.
	 * <p>
	 * An implementation of {@code Collection} may narrow the set of accepted
	 * objects, but it has to specify this in the documentation. If the object
	 * to be added does not meet this restriction, then an
	 * {@code IllegalArgumentException} is thrown.
	 * <p>
	 * If a collection does not yet contain an object that is to be added and
	 * adding the object fails, this method <i>must</i> throw an appropriate
	 * unchecked Exception. Returning false is not permitted in this case
	 * because it would violate the postcondition that the element will be part
	 * of the collection after this method finishes.
	 * @param object the object to add.
	 * @return {@code true} if this {@code Collection} is
	 * modified, {@code false} otherwise.
	 * @throws UnsupportedOperationException if adding to this {@code Collection} is not supported.
	 * @throws ClassCastException            if the class of the object is inappropriate for this
	 *                                       collection.
	 * @throws IllegalArgumentException      if the object cannot be added to this {@code Collection}.
	 * @throws NullPointerException          if null elements cannot be added to the {@code Collection}.
	 */
	public boolean add(E object);

	/**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation).  Shifts the element currently at that position
	 * (if any) and any subsequent elements to the right (adds one to their
	 * indices).
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws UnsupportedOperationException if the <tt>add</tt> operation
	 *                                       is not supported by this list
	 * @throws ClassCastException            if the class of the specified element
	 *                                       prevents it from being added to this list
	 * @throws NullPointerException          if the specified element is null and
	 *                                       this list does not permit null elements
	 * @throws IllegalArgumentException      if some property of the specified
	 *                                       element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException     if the index is out of range
	 *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
	 */
	public void add(int index, E element);

	/**
	 * Attempts to add all of the objects contained in {@code Collection}
	 * to the contents of this {@code Collection} (optional). If the passed {@code Collection}
	 * is changed during the process of adding elements to this {@code Collection}, the
	 * behavior is not defined.
	 * @param collection the {@code Collection} of objects.
	 * @return {@code true} if this {@code Collection} is modified, {@code false}
	 * otherwise.
	 * @throws UnsupportedOperationException if adding to this {@code Collection} is not supported.
	 * @throws ClassCastException            if the class of an object is inappropriate for this
	 *                                       {@code Collection}.
	 * @throws IllegalArgumentException      if an object cannot be added to this {@code Collection}.
	 * @throws NullPointerException          if {@code collection} is {@code null}, or if it
	 *                                       contains {@code null} elements and this {@code Collection} does
	 *                                       not support such elements.
	 */
	public boolean addAll(Collection<? extends E> collection);

	/**
	 * Removes all elements from this {@code Collection}, leaving it empty (optional).
	 * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
	 * @see #isEmpty
	 * @see #size
	 */
	public void clear();

	/**
	 * Tests whether this {@code Collection} contains the specified object. Returns
	 * {@code true} if and only if at least one element {@code elem} in this
	 * {@code Collection} meets following requirement:
	 * {@code (object==null ? elem==null : object.equals(elem))}.
	 * @param object the object to search for.
	 * @return {@code true} if object is an element of this {@code Collection},
	 * {@code false} otherwise.
	 * @throws ClassCastException   if the object to look for isn't of the correct
	 *                              type.
	 * @throws NullPointerException if the object to look for is {@code null} and this
	 *                              {@code Collection} doesn't support {@code null} elements.
	 */
	public boolean contains(Object object);

	/**
	 * Tests whether this {@code Collection} contains all objects contained in the
	 * specified {@code Collection}. If an element {@code elem} is contained several
	 * times in the specified {@code Collection}, the method returns {@code true} even
	 * if {@code elem} is contained only once in this {@code Collection}.
	 * @param collection the collection of objects.
	 * @return {@code true} if all objects in the specified {@code Collection} are
	 * elements of this {@code Collection}, {@code false} otherwise.
	 * @throws ClassCastException   if one or more elements of {@code collection} isn't of the
	 *                              correct type.
	 * @throws NullPointerException if {@code collection} contains at least one {@code null}
	 *                              element and this {@code Collection} doesn't support {@code null}
	 *                              elements.
	 * @throws NullPointerException if {@code collection} is {@code null}.
	 */
	public boolean containsAll(Collection<?> collection);

	/**
	 * Returns if this {@code Collection} contains no elements.
	 * @return {@code true} if this {@code Collection} has no elements, {@code false}
	 * otherwise.
	 * @see #size
	 */
	public boolean isEmpty();

	/**
	 * Removes the element at the specified position in this list (optional
	 * operation).  Shifts any subsequent elements to the left (subtracts one
	 * from their indices).  Returns the element that was removed from the
	 * list.
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @throws UnsupportedOperationException if the <tt>remove</tt> operation
	 *                                       is not supported by this list
	 * @throws IndexOutOfBoundsException     if the index is out of range
	 *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	E remove(int index);

	/**
	 * Removes one instance of the specified object from this {@code Collection} if one
	 * is contained (optional). The element {@code elem} that is removed
	 * complies with {@code (object==null ? elem==null : object.equals(elem)}.
	 * @param object the object to remove.
	 * @return {@code true} if this {@code Collection} is modified, {@code false}
	 * otherwise.
	 * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
	 * @throws ClassCastException            if the object passed is not of the correct type.
	 * @throws NullPointerException          if {@code object} is {@code null} and this {@code Collection}
	 *                                       doesn't support {@code null} elements.
	 */
	public boolean remove(Object object);

	/**
	 * Removes all occurrences in this {@code Collection} of each object in the
	 * specified {@code Collection} (optional). After this method returns none of the
	 * elements in the passed {@code Collection} can be found in this {@code Collection}
	 * anymore.
	 * @param collection the collection of objects to remove.
	 * @return {@code true} if this {@code Collection} is modified, {@code false}
	 * otherwise.
	 * @throws UnsupportedOperationException if removing from this {@code Collection} is not supported.
	 * @throws ClassCastException            if one or more elements of {@code collection}
	 *                                       isn't of the correct type.
	 * @throws NullPointerException          if {@code collection} contains at least one
	 *                                       {@code null} element and this {@code Collection} doesn't support
	 *                                       {@code null} elements.
	 * @throws NullPointerException          if {@code collection} is {@code null}.
	 */
	public boolean removeAll(Collection<?> collection);

	/**
	 * Returns a count of how many objects this {@code Collection} contains.
	 * @return how many objects this {@code Collection} contains, or Integer.MAX_VALUE
	 * if there are more than Integer.MAX_VALUE elements in this
	 * {@code Collection}.
	 */
	public int size();
}
