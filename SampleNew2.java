import java.util.Vector;

public class SampleNew2 {
	static int count=0;
	int [] line;
	static Vector<String> [] dictionary=null;

	SampleNew2(String [] line0) {
		count = line0.length;

		if(dictionary==null) {
			dictionary=new Vector[count];
			for(int i=0;i<count;i++)
				dictionary[i]=new Vector<String>();
		}
		
		line=new int [count];
		for(int i=0;i<count;i++) {
			int index=dictionary[i].indexOf(line0[i]);
			if(index==-1) {
				index=dictionary[i].size();
				dictionary[i].add(line0[i]);
			}
			line[i]=index;
		}		
	}

	int returns(int id) {
		return line[id];
	}

	int getClassLabel() {
		return line[count-1];
	}
	

	int returncount(){
		return count;
	}

}
