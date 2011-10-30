
public class KeyInteger implements Key{
	private int key = -1;
	
	public KeyInteger(int key) {
		this.key = key;
	}

	@Override
	public int compareTo(Key arg0) {
		KeyInteger compareToKey = (KeyInteger) arg0;
		int returnValue = 0;
		if(key > compareToKey.getKey()) {
			returnValue = 1;
		} else if(key < compareToKey.getKey()) {
			returnValue = -1;
		}
		
		return returnValue;
	}
	
	public int getKey(){
		return key;
	}
	
}
