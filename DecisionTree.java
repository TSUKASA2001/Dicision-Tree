import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class DecisionTree {
	public static int counti = 0;
	
	static Vector<SampleNew2> readData(String fileName) throws IOException {// ファイルからメモリに格納するメソッド
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		Vector<SampleNew2> re = new Vector<SampleNew2>();
		String buffer;

		while ((buffer = br.readLine()) != null) {
			String[] s = buffer.split(" ");

			SampleNew2 Sample =new SampleNew2(s);	
			re.add(Sample);
		}

		br.close();

		return re;
	}

	static double Info(int p, int n) {
		if (p == 0 || n == 0)
			return 0;

		double sum = p + n;
		double ans = -(p / sum) * Math.log(p / sum) - (n / sum) * Math.log(n / sum);
		return ans / Math.log(2);
	}

	static double pn(Vector<SampleNew2>  elements) {// 分割前の情報量
		int countp = 0;

		for (SampleNew2 sample: elements) {
			if (sample.getClassLabel()==0) 
				countp++;
		}

		int countn = elements.size() - countp; 
		return Info(countp, countn);
	}

	static double cul(double[] data, int[] num) {// 平均情報量
		double sum = 0; 
		
		for (int i = 0; i < num.length; i++) {
			sum += num[i];
		}

		double s = 0;

		for (int i = 0; i < num.length; i++) {
			s += (num[i] / sum) * data[i];
		}

		return s;
	}

	static double countpn(Vector<SampleNew2> elements, int line) {// elementsのline列にあるsの情報量を返す

		int distinctValues=SampleNew2.dictionary[line].size();
		int[][] count = new int[distinctValues][2];

		for (SampleNew2 sample : elements) 
			count[sample.returns(line)][sample.getClassLabel()]++;
		
		double ans=0;
		for(int i=0;i<distinctValues;i++)
			ans=ans+Info(count[i][0], count[i][1])*(count[i][0]+ count[i][1])/elements.size();

		return ans;
	}


	static int getAttributeOfMaximumInformationGain_imp(Vector<SampleNew2>  elements) {// サンプルの集合 data が入力として与えられ，情報利得が最大となるIDを返す
		int length = elements.get(0).returncount();
		double max = 0;
		int record = 0;

		double InfEntBefore = pn(elements); 
		
		for (int i = 0; i < length - 1; i++) {
			double InfEntAfter = countpn(elements, i);

			if (max < InfEntBefore - InfEntAfter) {
				max = InfEntBefore - InfEntAfter;
				record = i;
			}
		}
		return record;
	}
	
	static Vector<SampleNew2>[] divideData(Vector<SampleNew2> elements, int attributeID) {// attributeIDで分ける
		int distinctValues=SampleNew2.dictionary[attributeID].size();
		Vector<SampleNew2>[] result = new Vector[distinctValues];
		for(int i=0;i<result.length;i++)
			result[i]=new Vector<SampleNew2>();
		
		for (SampleNew2 sample: elements) {
			result[sample.returns(attributeID)].add(sample);
		}
		return result;
	}

	static void dt(Vector<SampleNew2> elements) {
		int id = getAttributeOfMaximumInformationGain_imp(elements);
		System.out.println(id);
		Vector<SampleNew2>[] datasets = divideData(elements, id);
		if (counti == elements.get(0).returncount()) {
			System.exit(0);
		}

		counti++;
		for (int i = 0; i < datasets.length; i++) {
			if (pn(datasets[i]) != 0) {
				dt(datasets[i]);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String fileName = "data.txt";
		Vector<SampleNew2> elements = readData(fileName);
		
		dt(elements);
	}
}