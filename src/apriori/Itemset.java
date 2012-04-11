package apriori;

import java.util.*;


/**
 * This class represents a set of <code> Item</code>s. Basic set operations
 * are supported.
 * 
 */
public class Itemset {
	private Set items;
	private double support;

	/**
	 * Creates an empty itemset.
	 * 
	 */
	public Itemset() {
		this.items = new HashSet();
	}

	/**
	 * Creates a clone of an itemset.
	 * 
	 * @param itemset
	 *            the itemset to be cloned
	 */
	private Itemset(Itemset itemset) {
		this.items = new HashSet(itemset.items);
	}

	/**
	 * Adds an item to the itemset. Warning: the itemset is changed.
	 * 
	 * @param item
	 *            an item to be added to the itemset
	 */

	public void addItem(Item item) {
		items.add(item);
	}

	/**
	 * Implements the set INTERSECTION operator. This function leaves the current Itemset unchanged.
	 * 
	 * @param otherSet
	 *            the itemset to intersect with the current itemset
	 * @return an itemset containing all the items that are in the current
	 *         itemset and at the same time also in the
	 *         <code> otherSet</otherSet>
	 * 
	 * 
	 */
	public Itemset intersectWith(Itemset otherSet) {
		Itemset newItemset = new Itemset(this);
		newItemset.items.retainAll(otherSet.items);
		return newItemset;
	}

	/**
	 * Implements the set UNION operator. This function leaves the current Itemset unchanged.
	 * 
	 * @param otherSet
	 *            the itemset to union with the current itemset
	 * @return an itemset containing all the items in the current itemset or in
	 *         <code> otherSet</otherSet>
	 * 
	 * 
	 */
	public Itemset unionWith(Itemset otherSet) {
		Itemset newItemset = new Itemset(this);
		newItemset.items.addAll(otherSet.items);
		return newItemset;
	}

	/**
	 * Implements the set MINUS operator. This function leaves the current Itemset unchanged.
	 * 
	 * @param otherSet
	 *            the itemset to exclude from the current itemset
	 * @return an itemset containing all the items in the current itemset except
	 *         the items in <code> otherSet</otherSet>
	 * 
	 * 
	 */
	public Itemset minusAllIn(Itemset otherSet) {
		Itemset newItemset = new Itemset(this);
		newItemset.items.removeAll(otherSet.items);
		return newItemset;
	}

	private void generateAllNonEmptySubsets(Vector itemsVector, int level,
			Set allNonEmptySubsets, Itemset currentItemset) {
		// make a copy of the currentItemset
		currentItemset = new Itemset(currentItemset);
		// this loop always has two iterations only
		// in first iteration the item at itemsVector is not inlcuded in
		// currentItemset, in the second it is
		boolean itemAdded = false;
		while (true) {
			if (level == itemsVector.size() - 1) {
				// check if it's a proper subset before adding
				if (currentItemset.size() != 0
						&& currentItemset.size() != itemsVector.size()) {
					allNonEmptySubsets.add(currentItemset);
				}
			} else {
				generateAllNonEmptySubsets(itemsVector, level + 1,
						allNonEmptySubsets, currentItemset);
			}
			if (itemAdded) {
				break;
			} else {
				// have to copy before adding to avoid modifying itemsets
				// already added to allNonEmptySubsets
				currentItemset = new Itemset(currentItemset);
				currentItemset.addItem((Item) itemsVector.elementAt(level));
				itemAdded = true;
			}
		}
	}

	/**
	 * 
	 * @return a set of all subsets of the current itemset, except empty subset
	 *         and the current itemset itself
	 */
	public Set generateAllNonEmptySubsets() {
		HashSet allNonEmptySubsets = new HashSet();
		generateAllNonEmptySubsets(new Vector(items), 0, allNonEmptySubsets,
				new Itemset());
		return allNonEmptySubsets;
	}

	/**
	 * 
	 * @return the size of the current itemset
	 */
	public int size() {
		return items.size();
	}

	/**
	 * 
	 * @return an iterator that allows to iterate over all the items of the
	 *         itemset
	 */
	public Iterator getItemIterator() {
		return items.iterator();
	}

	public String toString() {
		StringBuffer out = new StringBuffer();
		Iterator itItem = items.iterator();
		while (itItem.hasNext()) {
			Item item = (Item) itItem.next();
			out.append(item.toString());
			if (itItem.hasNext()) {
				out.append(" ");
			}
		}
		return out.toString();
	}

	public boolean equals(Object o) {
		return ((Itemset) o).items.equals(items);
	}

	public int hashCode() {
		return items.hashCode();
	}

	public double getSupport() {
		return support;
	}

	public void setSupport(double support) {
		this.support = support;
	}
	
	
}
