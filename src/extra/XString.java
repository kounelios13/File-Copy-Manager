package extra;
public class XString{
	private StringBuilder text = new StringBuilder();
	private int length = 0;
	public int length(){
		return length;
	}
	public void setText(String txt){
		/**
		 * Clear the current string builder and the append the received string
		 * */
		this.text.delete(0,length).append(txt);
		length = txt.length();
	}
	public String getText(){
		return toString();
	}
	public XString append(String name) {
		if(name == null)
			throw new NullPointerException();
		this.text.append(name);
		length +=name.length(); 
		return this;
	}
	public void newLine(){
		this.text.append("\n");
	}
	public void newLine(int times){
		for(int i=0;i<Math.abs(times);i++)
			newLine();
	}
	public StringBuilder reverse(){
		return text.reverse();
	}
	@Override
	public String toString(){
		return text.toString();
	}
	public XString(){}
	public XString(String message){
		this.text.append(message);
		length = message.length();
	}
	public boolean isEmpty(){
		return length < 1;
	}
}
