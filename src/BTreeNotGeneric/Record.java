package BTreeNotGeneric;


public class Record<T> {
	private T record = null;
	
	public Record(T record){
		this.record = record;
	}
	
	public T getRecord() {
		return record; 
	}
}
