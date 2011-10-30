package BTreeNotGeneric;

public class BTree {
	private int height = -1;
	private int t = -1; // minimum degree
	private Node root = null;

	public BTree(int t) {
		height = 0;
		this.t = t;
		root = makeRootNode();
	}

	private Node makeRootNode() {
		Node rootNode = new Node(t);
		rootNode.setLeaf(true);
		rootNode.setRoot(true);
		return rootNode;
	}

	public Record get(Key key) {
		NodeIndexPair pair = searchKey(key);
		int index = pair.index;
		Node node = pair.node;
		Record record = node.getRecord(index);
		return record;
	}
	
	public NodeIndexPair searchKey(Key key) {
		return doSearchKey(root, key);
	}
	
	private NodeIndexPair doSearchKey(Node node, Key key) {
		int index = 0;
		while ((index < node.getKeysNumber()) && (key.compareTo(node.getKey(index)) > 0)) {
			index++;
		}				
		if (index != node.getKeysNumber() &&  key == node.getKey(index)) {
			return  new NodeIndexPair(node, index);
		} else if (!node.isLeaf()) {
			return doSearchKey(node.getChild(index), key);
		} else {
			return null;
		}
	}

	public void insert(Key key, Record record) {
		if (root.isMaxFull()) {
			Node newRoot = new Node(t);
			newRoot.addChild(root);
			root = newRoot;
			height++;

			splitNode(newRoot, 0);
			doInsert(newRoot, key, record);
		} else {
			doInsert(root, key, record);
		}
	}

	private void doInsert(Node node, Key key, Record record) {
		int index = findIndexToInsertKey(node, key);
		if (node.isLeaf()) {
			node.addKeyRecordPair(index, key, record);
		} else {
			if (node.isMaxFull()) {
				splitNode(node, index);
				if (key.compareTo(node.getKey(index)) > 0) {
					index++;
				}
			}

			doInsert(node.getChild(index), key, record);
		}
	}

	private int findIndexToInsertKey(Node node, Key key) {
		int i = 0;
		while ((i < node.getKeysNumber()) && (key.compareTo(node.getKey(i)) > 0)) {
			i++;
		}
		return i;
	}

	private void splitNode(Node parent, int childIndex) {
		parent.setRoot(false);
		Node child = parent.getChild(childIndex);
		Node newChild = new Node(t);
		newChild.setLeaf(child.isLeaf());

		int medianIndex = t - 1; // begins from 0
		KeyRecordPair medianKeyRecordPair = child.getKeyRecordPair(medianIndex);

		moveKeysAndChildrenToNode(child, newChild, medianIndex + 1);
		addMedianKeyAndNewChildToParent(parent, childIndex, newChild, medianKeyRecordPair);
		removeMedianKeyAndChildFromNode(child, medianIndex);
	}

	private void removeMedianKeyAndChildFromNode(Node child, int medianIndex) {
		child.removeKeyRecordPair(medianIndex);
		removeChildIfNotLeaf(child, medianIndex);
	}

	private void addMedianKeyAndNewChildToParent(Node parent, int childIndex,
			Node newChild, KeyRecordPair medianKeyRecordPair) {
		parent.addKeyRecordPair(childIndex, medianKeyRecordPair);
		int newChildIndex = childIndex + 1;
		parent.addChild(newChild, newChildIndex);
	}

	private void moveKeysAndChildrenToNode(Node fromNode, Node toNode,
			int startIndex) {
		int i = startIndex;
		for (; i < fromNode.getKeysNumber(); i++) {
			KeyRecordPair pair = fromNode.getKeyRecordPair(i);
			toNode.addKeyRecordPair(pair);
			copyChildIfNotLeaf(fromNode, toNode, i);
		}
		copyChildIfNotLeaf(fromNode, toNode, i);		
		removeChildIfNotLeaf(fromNode, i);
		i--;
		for (; i >= startIndex; i--) {
			fromNode.removeKeyRecordPair(i);
			removeChildIfNotLeaf(fromNode, i);
		}
	}

	private void copyChildIfNotLeaf(Node node, Node newNode, int i) {
		if (!node.isLeaf()) {
			newNode.addChild(node.getChild(i));
		}
	}

	private void removeChildIfNotLeaf(Node node, int i) {
		if (!node.isLeaf()) {
			node.removeChild(i);
		}
	}

	public void delete(Key key) {
		doDelete(root, key);
	}

	private void doDelete(Node node, Key key) {
		int keyIndex = findKeyIndex(node, key);
		if (keyIndex != -1) {
			if (node.isLeaf()) {
				node.removeKeyRecordPair(keyIndex);
			} else {
				keyIndex = findIndexToInsertKey(node, key);
				Node leftChild = node.getChild(keyIndex);
				Node rightChild = node.getChild(keyIndex + 1);
				if (!leftChild.isMinFull()) {					
					KeyRecordPair previousPair = leftChild.getLastKeyRecordPair();
					Key previousKey = previousPair.getKey();
					doDelete(leftChild, previousKey);
					node.removeKeyRecordPair(keyIndex);
					node.addKeyRecordPair(keyIndex, previousPair);
				} else if (!rightChild.isMinFull()) {
					KeyRecordPair nextPair = rightChild.getFirstKeyRecordPair();
					Key nextKey = nextPair.getKey();
					doDelete(rightChild, nextKey);
					node.removeKeyRecordPair(keyIndex);
					node.addKeyRecordPair(keyIndex, nextPair);
				} else {
					mergeNodes(leftChild, rightChild);
					moveKeyIntoChild(node, leftChild, keyIndex);
					doDelete(leftChild, key);
				}
			}
		} else {
			keyIndex = findIndexToInsertKey(node, key);
			Node child = node.getChild(keyIndex);
			if (!child.isMinFull()) {
				doDelete(child, key);
			} else {
				Node rightSibling = node.getChild(keyIndex + 1);
				if (!rightSibling.isMinFull()) {
					KeyRecordPair pairForChild = node.removeKeyRecordPair(keyIndex);
					child.addKeyRecordPair(pairForChild);
					KeyRecordPair pairForParent = rightSibling.removeFirstKeyRecordPair();
					node.addKeyRecordPair(keyIndex, pairForParent);

					copyChildIfNotLeaf(rightSibling, child, 0);
					removeChildIfNotLeaf(rightSibling, 0);
					doDelete(child, key);

				} else {
					Node leftSibling = null;
					if (keyIndex != 0) {
						leftSibling = node.getChild(keyIndex - 1);
					}
					if ((leftSibling != null) && !leftSibling.isMinFull()) {
						KeyRecordPair pairForChild = node.removeKeyRecordPair(keyIndex - 1);
						child.addKeyRecordPair(pairForChild);
						KeyRecordPair pairForParent = leftSibling.removeLastKeyRecordPair();
						node.addKeyRecordPair(keyIndex, pairForParent);

						Node childToMove = rightSibling.removeLastChild();
						child.addChild(childToMove);
						doDelete(child, key);

					} else {
						mergeNodes(child, rightSibling);
						moveKeyIntoChild(node, child, keyIndex);
						doDelete(child, key);
					}

				}

			}

		}
	}

	private void moveKeyIntoChild(Node parent, Node child, int keyIndex) {
		KeyRecordPair pair = parent.removeKeyRecordPair(keyIndex);
		Key key = pair.getKey();
		parent.removeChild(keyIndex + 1);
		int newKeyIndex = findIndexToInsertKey(child, key);
		child.addKeyRecordPair(newKeyIndex, pair);
	}

	private void mergeNodes(Node leftNode, Node rightNode) {
		moveKeysAndChildrenToNode(rightNode, leftNode, 0);
	}

	private int findKeyIndex(Node node, Key key) {
		int index = -1;
		for (int i = 0; i < node.getKeysNumber(); i++) {
			if (node.getKey(i) == key) {
				index = i;
				break;
			}
		}
		return index;
	}

	public Key getNextKey(Key key) {
		Key nextKey = null;
		NodeIndexPair nodeIndexPair = doSearchKey(root, key);
		int index = nodeIndexPair.index;
		Node node = nodeIndexPair.node;
		if (node.isLastKey(index)){
			nextKey = node.getLastChild().getFirstKey();
		} else {
			nextKey = node.getKey(index + 1);
		}
		return nextKey;
	}

//	private int getPrevKey(int key) {
//		int prevKey = -1;
//		NodeIndexPair nodeIndexPair = searchKey(key);
//		int index = nodeIndexPair.index;
//		Node node = nodeIndexPair.node;
//		if(node.isFirstKey(index)) {
//			Node parent = getParentOfChild(node, root, key);
//			int childIndex = parent.getChildIndex(node);
//			if(chi)
//		}
//			
//		return prevKey;
//	}
//	
//	private Node getParentOfChild(Node node, Node parent, int key) { 
//		int index = 0;
//		while ((index < node.getKeysNumber()) && (key > node.getKey(index))) {
//			index++;
//		}
//
//		if (index != node.getKeysNumber() && key == node.getKey(index)) {
//			return parent;
//		} else if (!node.isLeaf()) {
//			return getParentOfChild(node.getChild(index), node, key);
//		} else {
//			return null;		
//		}
//	}
	
	public Node getRoot() {
		return root;
	}
	private class NodeIndexPair {
		private Node node = null;
		private int index = -1;

		private NodeIndexPair(Node node, int index) {
			this.node = node;
			this.index = index;
		}
	}
}
