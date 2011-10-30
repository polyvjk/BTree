package BTreeNotGeneric;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private int minTreeDegree = -1;
	private int minKeysNumber = -1;
	private int maxKeysNumber = -1;
	private int keysNumber = -1;
	private boolean isLeaf = false;
	private List<KeyRecordPair> keyRecordPairs = null;
	private List<Node> children = null;
	private boolean isRoot = false;

	public Node(int minDegree) {
		keysNumber = 0;
		minTreeDegree = minDegree;
		setMinKeysNumber();
		setMaxKeysNumber();

		keyRecordPairs = new ArrayList<KeyRecordPair>();
		children = new ArrayList<Node>();
	}
	
	//keys ------------------------------------------------------------------------
	public Key getKey(int index) {
		return keyRecordPairs.get(index).getKey();
	}
	
	public KeyRecordPair getKeyRecordPair(int index) {
		return keyRecordPairs.get(index);
	}
	
	public void addKeyRecordPair(Key key, Record record) {
		keyRecordPairs.add(new KeyRecordPair(key, record));
		keysNumber++;
	}
	
	public void addKeyRecordPair(int index, Key key, Record record) {
		keyRecordPairs.add(index, new KeyRecordPair(key, record));
		keysNumber++;
	}

	public void addKeyRecordPair(int index, KeyRecordPair pair) {
		keyRecordPairs.add(index, pair);
		keysNumber++;
	}
	
	public void addKeyRecordPair(KeyRecordPair pair) {
		keyRecordPairs.add(pair);
		keysNumber++;
	}
	
	public KeyRecordPair getLastKeyRecordPair() {
		return keyRecordPairs.get(keysNumber - 1);
	}
	
	public KeyRecordPair getFirstKeyRecordPair() {
		return keyRecordPairs.get(0);
	}
	
	public boolean isLastKey(int index) {
		return index == keysNumber - 1;
	}

	public boolean isFirstKey(int index) {
		return index == 0;
	}

	public Key getFirstKey() {
		return keyRecordPairs.get(0).getKey();
	}
	
	public int getKeysNumber() {
		return keysNumber;
	}

	public KeyRecordPair removeFirstKeyRecordPair() {
		keysNumber --;
		return keyRecordPairs.remove(0);
	}

	public KeyRecordPair removeLastKeyRecordPair() {
		keysNumber --;
		return keyRecordPairs.remove(keyRecordPairs.size() - 1);
	}
	
	public KeyRecordPair removeKeyRecordPair(int index) {
		keysNumber --;
		return keyRecordPairs.remove(index);
	}
	
	public List<KeyRecordPair> getKeyRecordPairs() {
		return keyRecordPairs;
	}
	
	//children -----------------------------------------------------------------------
		
	public void addChild(Node child) {
		children.add(child);
	}

	public void addChild(Node child, int index) {
		children.add(index, child);
	}

	public Node removeChild(int index) {
		return children.remove(index);
	}

	public Node getChild(int index) {
		return children.get(index);
	}

	public Node getLastChild() {
		return children.get(children.size() - 1);
	}

	public Node getFirstChild() {
		return children.get(0);
	}

	public Node removeLastChild() {
		return children.remove(children.size() - 1);
	}

	public Node removeFirstChild() {
		return children.remove(0);
	}

	public int getChildIndex(Node child) {
		return children.indexOf(child);
	}

	public List<Node> getChildren() {
		return children;
	}
		
	public Record getRecord(int index) {
		return keyRecordPairs.get(index).getRecord();
	}

	public boolean isMaxFull() {
		return keysNumber == maxKeysNumber;
	}

	public boolean isMinFull() {
		return keysNumber == minKeysNumber;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
		setMaxKeysNumber();
	}

	
	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
		setMinKeysNumber();
	}	
	
	public int getMinKeysNumber() {
		return minKeysNumber;
	}

	public void setMinKeysNumber() {
		if (isRoot) {
			minKeysNumber = 1;
		} else {
			minKeysNumber = minTreeDegree - 1;
		}
	}

	public int getMaxKeysNumber() {
		return maxKeysNumber;
	}

	public void setMaxKeysNumber() {
		if (isLeaf) {
			maxKeysNumber = 2 * minTreeDegree;
		} else {
			maxKeysNumber = 2 * minTreeDegree - 1;
		}
	}

}
