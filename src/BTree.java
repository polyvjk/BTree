
public class BTree<K extends Key, R extends Record> {
	private int height = -1;
	private int t = -1; // minimum degree
	private Node<K,R> root = null;

	public BTree(int t) {
		height = 0;
		this.t = t;
		root = makeRootNode();
	}

	private Node<K,R> makeRootNode() {
		Node<K,R> rootNode = new Node<K,R>(t);
		rootNode.setLeaf(true);
		rootNode.setRoot(true);
		return rootNode;
	}

	public R get(K key) {
		NodeIndexPair pair = searchKey(key);
		int index = pair.index;
		Node<K,R> node = pair.node;
		R record = node.getRecord(index);
		return record;
	}
	
	public NodeIndexPair searchKey(K key) {
		return doSearchKey(root, key);
	}
	
	private NodeIndexPair doSearchKey(Node<K,R> node, K key) {
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

	public void insert(K key, R record) {
		if (root.isMaxFull()) {
			Node<K,R> newRoot = new Node<K,R>(t);
			newRoot.addChild(root);
			root = newRoot;
			height++;

			splitNode(newRoot, 0);
			doInsert(newRoot, key, record);
		} else {
			doInsert(root, key, record);
		}
	}

	private void doInsert(Node<K,R> node, K key, R record) {
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

	private int findIndexToInsertKey(Node<K,R> node, K key) {
		int i = 0;
		while ((i < node.getKeysNumber()) && (key.compareTo(node.getKey(i)) > 0)) {
			i++;
		}
		return i;
	}

	private void splitNode(Node<K,R> parent, int childIndex) {
		parent.setRoot(false);
		Node<K,R> child = parent.getChild(childIndex);
		Node<K,R> newChild = new Node<K,R>(t);
		newChild.setLeaf(child.isLeaf());

		int medianIndex = t - 1; // begins from 0
		KeyRecordPair<K,R> medianKeyRecordPair = child.getKeyRecordPair(medianIndex);

		moveKeysAndChildrenToNode(child, newChild, medianIndex + 1);
		addMedianKeyAndNewChildToParent(parent, childIndex, newChild, medianKeyRecordPair);
		removeMedianKeyAndChildFromNode(child, medianIndex);
	}

	private void removeMedianKeyAndChildFromNode(Node<K,R> child, int medianIndex) {
		child.removeKeyRecordPair(medianIndex);
		removeChildIfNotLeaf(child, medianIndex);
	}

	private void addMedianKeyAndNewChildToParent(Node<K,R> parent, int childIndex,
			Node<K,R> newChild, KeyRecordPair<K,R> medianKeyRecordPair) {
		parent.addKeyRecordPair(childIndex, medianKeyRecordPair);
		int newChildIndex = childIndex + 1;
		parent.addChild(newChild, newChildIndex);
	}

	private void moveKeysAndChildrenToNode(Node<K,R> fromNode, Node<K,R> toNode,
			int startIndex) {
		int i = startIndex;
		for (; i < fromNode.getKeysNumber(); i++) {
			KeyRecordPair<K,R> pair = fromNode.getKeyRecordPair(i);
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

	private void copyChildIfNotLeaf(Node<K,R> node, Node<K,R> newNode, int i) {
		if (!node.isLeaf()) {
			newNode.addChild(node.getChild(i));
		}
	}

	private void removeChildIfNotLeaf(Node<K,R> node, int i) {
		if (!node.isLeaf()) {
			node.removeChild(i);
		}
	}

	public void delete(K key) {
		doDelete(root, key);
	}

	private void doDelete(Node<K,R> node, K key) {
		int keyIndex = findKeyIndex(node, key);
		if (keyIndex != -1) {
			if (node.isLeaf()) {
				node.removeKeyRecordPair(keyIndex);
			} else {
				keyIndex = findIndexToInsertKey(node, key);
				Node<K,R> leftChild = node.getChild(keyIndex);
				Node<K,R> rightChild = node.getChild(keyIndex + 1);
				if (!leftChild.isMinFull()) {					
					KeyRecordPair<K,R> previousPair = leftChild.getLastKeyRecordPair();
					K previousKey = previousPair.getKey();
					doDelete(leftChild, previousKey);
					node.removeKeyRecordPair(keyIndex);
					node.addKeyRecordPair(keyIndex, previousPair);
				} else if (!rightChild.isMinFull()) {
					KeyRecordPair<K,R> nextPair = rightChild.getFirstKeyRecordPair();
					K nextKey = nextPair.getKey();
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
			Node<K,R> child = node.getChild(keyIndex);
			if (!child.isMinFull()) {
				doDelete(child, key);
			} else {
				Node<K,R> rightSibling = node.getChild(keyIndex + 1);
				if (!rightSibling.isMinFull()) {
					KeyRecordPair<K,R> pairForChild = node.removeKeyRecordPair(keyIndex);
					child.addKeyRecordPair(pairForChild);
					KeyRecordPair<K,R> pairForParent = rightSibling.removeFirstKeyRecordPair();
					node.addKeyRecordPair(keyIndex, pairForParent);

					copyChildIfNotLeaf(rightSibling, child, 0);
					removeChildIfNotLeaf(rightSibling, 0);
					doDelete(child, key);

				} else {
					Node<K,R> leftSibling = null;
					if (keyIndex != 0) {
						leftSibling = node.getChild(keyIndex - 1);
					}
					if ((leftSibling != null) && !leftSibling.isMinFull()) {
						KeyRecordPair<K,R> pairForChild = node.removeKeyRecordPair(keyIndex - 1);
						child.addKeyRecordPair(pairForChild);
						KeyRecordPair<K,R> pairForParent = leftSibling.removeLastKeyRecordPair();
						node.addKeyRecordPair(keyIndex, pairForParent);

						Node<K,R> childToMove = rightSibling.removeLastChild();
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

	private void moveKeyIntoChild(Node<K,R> parent, Node<K,R> child, int keyIndex) {
		KeyRecordPair<K,R> pair = parent.removeKeyRecordPair(keyIndex);
		K key = pair.getKey();
		parent.removeChild(keyIndex + 1);
		int newKeyIndex = findIndexToInsertKey(child, key);
		child.addKeyRecordPair(newKeyIndex, pair);
	}

	private void mergeNodes(Node<K,R> leftNode, Node<K,R> rightNode) {
		moveKeysAndChildrenToNode(rightNode, leftNode, 0);
	}

	private int findKeyIndex(Node<K,R> node, K key) {
		int index = -1;
		for (int i = 0; i < node.getKeysNumber(); i++) {
			if (node.getKey(i) == key) {
				index = i;
				break;
			}
		}
		return index;
	}

	public K getNextKey(K key) {
		K nextKey = null;
		NodeIndexPair nodeIndexPair = doSearchKey(root, key);
		int index = nodeIndexPair.index;
		Node<K,R> node = nodeIndexPair.node;
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
	
	public Node<K,R> getRoot() {
		return root;
	}
	private class NodeIndexPair {
		private Node<K,R> node = null;
		private int index = -1;

		private NodeIndexPair(Node<K,R> node, int index) {
			this.node = node;
			this.index = index;
		}
	}
}
