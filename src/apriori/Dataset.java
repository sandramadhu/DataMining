/*
 * Date: 10/02/2010
 * Version 1
 * 
 * Author: Madhubabu Sandara
 * Course CSE 601 : Data Mininig and Bio-informatics
 * Fall 2010
 * 
 */

package apriori;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This class represents the whole dataset of all gene expression data
 * transactions.
 */

public class Dataset {

    private LinkedList transactionList = new LinkedList();

    /**
     * Creates and initializes the dataset with the data from a file
     * 
     * @param filename
     *            the name of the file to be loaded
     * @throws IOException
     */
    public Dataset(String filename) throws IOException {
	LineNumberReader lineReader = new LineNumberReader(
		new InputStreamReader(new FileInputStream(filename)));
	String line = null;

	while ((line = lineReader.readLine()) != null) {
	    Itemset newItemset = new Itemset();
	    int cnt = 0;

	    StringTokenizer tokenizer = new StringTokenizer(line, " ,\t");
	    while (tokenizer.hasMoreTokens()) {
		String strItem = tokenizer.nextToken();
		newItemset.addItem(new Item(strItem));
	    }
	    // ignore all empty itemsets
	    if (newItemset.size() != 0) {
		transactionList.add(newItemset);
	    }
	}
    }

    public void dumpItemsets() {
	Iterator itItemset = getTransactionIterator();
	while (itItemset.hasNext()) {
	    Itemset itemset = (Itemset) itItemset.next();
	    System.out.println(itemset.toString());
	}
    }

    /**
     * 
     * @return the iterator that allows to go over all the transactions in the
     *         dataset The transactions are <code> Itemset </code> objects
     */
    public Iterator getTransactionIterator() {
	return transactionList.iterator();
    }

    /**
     * 
     * @return the number of transactions in the dataset
     */
    public int getNumTransactions() {
	return transactionList.size();
    }

    /**
     * 
     * @param itemset
     * @return the support value for a given itemset in the context of the
     *         current dataset
     */
    public double computeSupportForItemset(Itemset itemset) {
	int occurrenceCount = 0;
	Iterator itItemset = getTransactionIterator();
	while (itItemset.hasNext()) {
	    Itemset itemsets = (Itemset) itItemset.next();
	    if (itemsets.intersectWith(itemset).size() == itemset.size()) {
		occurrenceCount++;
	    }
	}
	return ((double) occurrenceCount) / getNumTransactions();
    }

    /**
     * 
     * @param associationRule
     * @return the confidence value for a given association rule in the context
     *         of the current dataset
     */
    public double computeConfidenceForAssociationRule(
	    AssociationRule associationRule) {
	Itemset union = associationRule.getItemsetA().unionWith(
		associationRule.getItemsetB());
	return computeSupportForItemset(union)
		/ computeSupportForItemset(associationRule.getItemsetA());
    }

    /**
     * 
     * @return all possible itemsets of size one based on the current dataset
     */
    public Set getAllItemsetsOfSizeOne(double minSupport) {
	Iterator itItemset = getTransactionIterator();
	Itemset bigUnion = new Itemset();
	while (itItemset.hasNext()) {
	    Itemset itemset = (Itemset) itItemset.next();
	    bigUnion = bigUnion.unionWith(itemset);
	}

	// break up the big unioned itemset into one element itemsets
	HashSet allItemsets = new HashSet();
	Iterator itItem = bigUnion.getItemIterator();
	while (itItem.hasNext()) {
	    Item item = (Item) itItem.next();
	    Itemset itemset = new Itemset();
	    itemset.addItem(item);

	    double supp = computeSupportForItemset(itemset);
	    itemset.setSupport(supp);

	    if (supp >= minSupport) {
		allItemsets.add(itemset);
	    }
	}
//	System.out.println(allItemsets);
//	System.out.println("Count: " + allItemsets.size());
	return allItemsets;
    }

    /**
     * The core of the association rule mining algorithm. This is what needs to
     * be implemented. This is the only piece of code that you need to modify to
     * complete the exercise.
     * 
     * @param minSupport
     *            minimal support value below which itemsets should not be
     *            considered when generating candidate itemsets
     * @param minConfidence
     *            minimal support value for the association rules output by the
     *            algorithm
     * @return a collection of <code> AssociationRule </code> instances
     */
    public Collection run(double minSupport, double minConfidence) {
	Collection discoveredAssociationRules = new LinkedList();

	int k_items = 0;
	// generate candidate itemsets
	final int MAX_NUM_ITEMS = 100;
	Set[] candidates = new Set[MAX_NUM_ITEMS];
	candidates[1] = getAllItemsetsOfSizeOne(minSupport);

	for (int numItems = 1; numItems < MAX_NUM_ITEMS
		&& !candidates[numItems].isEmpty(); numItems++) {
	    candidates[numItems + 1] = new HashSet();
	    for (Iterator itItemset1 = candidates[numItems].iterator(); itItemset1
		    .hasNext();) {
		Itemset itemset1 = (Itemset) itItemset1.next();
		for (Iterator itItemset2 = candidates[numItems].iterator(); itItemset2
			.hasNext();) {
		    Itemset itemset2 = (Itemset) itItemset2.next();
		    if (itemset1.intersectWith(itemset2).size() == numItems - 1) {
			Itemset candidateItemset = itemset1.unionWith(itemset2);
			assert (candidateItemset.size() == numItems + 1);

			double supp = computeSupportForItemset(candidateItemset);
			candidateItemset.setSupport(supp);

			if (supp >= minSupport) {
			    candidates[numItems + 1].add(candidateItemset);
			}
		    }
		}
	    }
	    k_items = numItems;
	}

	for (int x = 1; x <= k_items; x++) {
	    Set iset = candidates[x];
	    Iterator it = iset.iterator();
	    System.out.println("\n\nPrinting " + x + "-itemsets : Total count -> "+ iset.size() + "\n");
	    System.out.println("Support\tItems");
	    while (it.hasNext()) {
		Itemset myitemset = (Itemset) it.next();
		Iterator it_item = myitemset.getItemIterator();

		System.out.print(myitemset.getSupport() + "\t");

		while (it_item.hasNext()) {
		    Item item = (Item) it_item.next();
		    System.out.print(item.toString() + "  ");
		}
		System.out.println(" ");
	    }
	}

	System.out.println("\n\nAssociation Rules... ");
	// generate association rules from candidate itemsets
	for (int numItems = 1; numItems < MAX_NUM_ITEMS
		&& !candidates[numItems].isEmpty(); numItems++) {
	    for (Iterator itItemsetCandidate = candidates[numItems].iterator(); itItemsetCandidate
		    .hasNext();) {
		Itemset itemsetCandidate = (Itemset) itItemsetCandidate.next();
		for (Iterator itItemsetSub = itemsetCandidate
			.generateAllNonEmptySubsets().iterator(); itItemsetSub
			.hasNext();) {
		    Itemset itemsetSub = (Itemset) itItemsetSub.next();
		    Itemset itemsetA = itemsetSub;
		    Itemset itemsetB = itemsetCandidate.minusAllIn(itemsetSub);
		    AssociationRule candidateAssociationRule = new AssociationRule(
			    itemsetA, itemsetB);
		    if (computeConfidenceForAssociationRule(candidateAssociationRule) >= minConfidence) {
			discoveredAssociationRules.add(candidateAssociationRule);

		    }
		}
	    }
	}
	return discoveredAssociationRules;
    }

    /**
     * Loads the dataset from a default file, runs the apriori algorithm and
     * outputs the result to the console.
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    if (args == null || args.length < 3) {
		System.err.println("Please input correct arguments....");
//		System.exit(1);
	    }

//	    String inFile = "C:\\Users\\Madhu\\Desktop\\Fall2010\\DM\\hw2\\apriori\\apriori\\xactions_new.txt";
	    String inFile = args[2];
	    double inSupport = Double.parseDouble(args[0]);
	    double inConfidence = Double.parseDouble(args[1]);

	    Dataset dataset = new Dataset(inFile);
	    Collection discoveredAssociationRules = dataset.run(inSupport,
		    inConfidence);

	    Iterator itAssociationRule = discoveredAssociationRules.iterator();
	    while (itAssociationRule.hasNext()) {
		AssociationRule associationRule = (AssociationRule) itAssociationRule
			.next();
		System.out
			.println("assoctiation rule: "
				+ associationRule
				+ "\tsupport: "
				+ dataset
					.computeSupportForItemset(associationRule
						.getItemsetA().unionWith(
							associationRule
								.getItemsetB()))
				+ "\tconfidence: "
				+ dataset
					.computeConfidenceForAssociationRule(associationRule));

	    }

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
