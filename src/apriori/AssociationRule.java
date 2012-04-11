package apriori;


/**
 * Represents an association rule: itemsetA => itemsetB 
 * 
 */
public class AssociationRule {

	private Itemset itemsetA;

	private Itemset itemsetB;

	public AssociationRule(Itemset itemsetA, Itemset itemsetB) {
		this.itemsetA = itemsetA;
		this.itemsetB = itemsetB;
	}

	public Itemset getItemsetA() {
		return itemsetA;
	}

	public Itemset getItemsetB() {
		return itemsetB;
	}

	public String toString() {
		return "{" + itemsetA + "} => {" + itemsetB + "}";
	}

}
