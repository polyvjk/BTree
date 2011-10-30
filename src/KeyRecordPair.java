
public class KeyRecordPair<K extends Key, R extends Record> {
	private K key = null;
	private R record = null;
	
	public KeyRecordPair(K key, R record) {
		this.key = key;
		this.record = record;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public R getRecord() {
		return record;
	}

	public void setRecord(R record) {
		this.record = record;
	}		
}
