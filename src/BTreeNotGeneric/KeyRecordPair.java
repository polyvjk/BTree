package BTreeNotGeneric;


public class KeyRecordPair {
	private Key key = null;
	private Record record = null;
	
	public KeyRecordPair(Key key, Record record) {
		this.key = key;
		this.record = record;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}		
}
