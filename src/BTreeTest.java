import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


public class BTreeTest {
	private BTree<KeyInteger, RecordString> tree = null;
	KeyInteger key7 = new KeyInteger(7);
	KeyInteger key4 = new KeyInteger(4);
	KeyInteger key12 = new KeyInteger(12);
	KeyInteger key16 = new KeyInteger(16);
	KeyInteger key9 = new KeyInteger(9);
	
	RecordString recordA = new RecordString("A");
	RecordString recordB = new RecordString("B");
	RecordString recordC = new RecordString("C");
	RecordString recordD = new RecordString("D");
	RecordString recordE = new RecordString("E");
	
	private void initTree() {
		tree = new BTree<KeyInteger, RecordString>(2);
		tree.insert(key4, recordA);
		tree.insert(key7, recordB);
		tree.insert(key16, recordC);
		tree.insert(key12, recordD);
		
	}
	
	@Test
	public void testInsert() {
		initTree();
		
		assertTrue(tree.getRoot().isLeaf());
		tree.insert(key9, recordE);
		assertFalse(tree.getRoot().isLeaf());
		
		List<KeyInteger> keys = tree.getRoot().getKeys();
		List<Node<KeyInteger, RecordString>> children = tree.getRoot().getChildren();
		assertTrue(keys.size() == 1);
		
		
		assertTrue(keys.contains(key7));
		assertTrue(children.size() == 2);
		Node<KeyInteger, RecordString> leftChild = children.get(0);
		Node<KeyInteger, RecordString> rightChild = children.get(1);
		assertTrue(leftChild.getKeysNumber() == 1);
		assertTrue(leftChild.getKeys().contains(key4));
		assertTrue(rightChild.getKeysNumber() == 3);
		assertTrue(rightChild.getKeys().contains(key9));
		assertTrue(rightChild.getKeys().contains(key12));
		assertTrue(rightChild.getKeys().contains(key16));
//		
//		tree.insert(8);
//		assertTrue(rightChild.getKeys().contains(8));
//		assertTrue(rightChild.getKeysNumber() == 4);
//		assertTrue(rightChild.isMaxFull());
	}
	
//	@Test
//	public void testDelete() {
//		testInsert();
//		tree.delete(7);
//		assertTrue(tree.getRoot().getKeys().contains(8));
//		assertTrue(tree.getRoot().getChild(1).getKeysNumber() == 3);
//		assertFalse(tree.getRoot().getChild(1).getKeys().contains(8));
//		tree.delete(4);
//		assertTrue(tree.getRoot().getKeys().contains(9));
//	}
//	
//	@Test 
//	public void testSearch() {
//		testDelete();
//		NodeIndexPair pair = tree.searchKey(12);
//		assertTrue(pair.getNode().getKeys().contains(16));
//	}
	
//	@Test 
//	public void testGetNext() {
//		testDelete();
//		int nextKey = tree.getNextKey(12);
//		assertTrue(nextKey == 16);
//	}
	



}
