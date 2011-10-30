import java.util.ArrayList;
import java.util.List;

public class Node<K extends Key, R extends Record> {
	private int minTreeDegree = -1;
	private int minKeysNumber = -1;
	private int maxKeysNumber = -1;
	private int keysNumber = -1;
	private boolean isLeaf = false;
	private List<KeyRecordPair<K,R>> keyRecordPairs = null;
	private List<Node<K,R>> children = null;
	private boolean isRoot = false;

	public Node(int minDegree) {
		keysNumber = 0;
		minTreeDegree = minDegree;
		setMinKeysNumber();
		setMaxKeysNumber();

		keyRecordPairs = new ArrayList<KeyRecordPair<K,R>>();
		children = new ArrayList<Node<K,R>>();
	}
	
	//keys ------------------------------------------------------------------------
	public K getKey(int index) {
		return keyRecordPairs.get(index).getKey();
	}
	
	public KeyRecordPair<K,R> getKeyRecordPair(int index) {
		return keyRecordPairs.get(index);
	}
	
	public void addKeyRecordPair(K key, R record) {
		keyRecordPairs.add(new KeyRecordPair<K,R>(key, record));
		keysNumber++;
	}
	
	public void addKeyRecordPair(int index, K key, R record) {
		keyRecordPairs.add(index, new KeyRecordPair<K,R>(key, record));
		keysNumber++;
	}

	public void addKeyRecordPair(int index, KeyRecordPair<K,R> pair) {
		keyRecordPairs.add(index, pair);
		keysNumber++;
	}
	
	public void addKeyRecordPair(KeyRecordPair<K,R> pair) {
		keyRecordPairs.add(pair);
		keysNumber++;
	}
	
	public KeyRecordPair<K,R> getLastKeyRecordPair() {
		return keyRecordPairs.get(keysNumber - 1);
	}
	
	public KeyRecordPair<K,R> getFirstKeyRecordPair() {
		return keyRecordPairs.get(0);
	}
	
	public boolean isLastKey(int index) {
		return index == keysNumber - 1;
	}

	public boolean isFirstKey(int index) {
		return index == 0;
	}

	public K getFirstKey() {
		return keyRecordPairs.get(0).getKey();
	}
	
	public int getKeysNumber() {
		return keysNumber;
	}

	public KeyRecordPair<K,R> removeFirstKeyRecordPair() {
		keysNumber --;
		return keyRecordPairs.remove(0);
	}

	public KeyRecordPair<K,R> removeLastKeyRecordPair() {
		keysNumber --;
		return keyRecordPairs.remove(keyRecordPairs.size() - 1);
	}
	
	public KeyRecordPair<K,R> removeKeyRecordPair(int index) {
		keysNumber --;
		return keyRecordPairs.remove(index);
	}
	
	public List<KeyRecordPair<K,R>> getKeyRecordPairs() {
		return keyRecordPairs;
	}
	
	public List<K> getKeys() {
		List<K> keys = new ArrayList<K>();
		for(KeyRecordPair<K, R> pair : keyRecordPairs) {
			keys.add(pair.getKey());
		}
		return keys;
	}
	
	//children -----------------------------------------------------------------------
		
	public void addChild(Node<K,R> child) {
		children.add(child);
	}

	public void addChild(Node<K,R> child, int index) {
		children.add(index, child);
	}

	public Node<K,R> removeChild(int index) {
		return children.remove(index);
	}

	public Node<K,R> getChild(int index) {
		return children.get(index);
	}

	public Node<K,R> getLastChild() {
		return children.get(children.size() - 1);
	}

	public Node<K,R> getFirstChild() {
		return children.get(0);
	}

	public Node<K,R> removeLastChild() {
		return children.remove(children.size() - 1);
	}

	public Node<K,R> removeFirstChild() {
		return children.remove(0);
	}

	public int getChildIndex(Node<K,R> child) {
		return children.indexOf(child);
	}

	public List<Node<K,R>> getChildren() {
		return children;
	}
	//record -----------------------------------------------------------------
	
	public R getRecord(int index) {
		return keyRecordPairs.get(index).getRecord();
	}

	public List<R> getRecords() {
		List<R> records = new ArrayList<R>();
		for(KeyRecordPair<K, R> pair : keyRecordPairs) {
			records.add(pair.getRecord());
		}
		return records;
	}
	//------------------------------------------------------------------------
	
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
