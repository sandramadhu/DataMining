package apriori;

/**
 * Represents an item occuring in the transactions in the <code> Dataset </code>
 * 
 */
public class Item {

	private String name;

	/**
	 * 
	 * @param name string representation of the item
	 */
	public Item(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public boolean equals(Object o) {
		return ((Item) o).name.equals(name);
	}

	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString()
	{
		return name;
	}

}
