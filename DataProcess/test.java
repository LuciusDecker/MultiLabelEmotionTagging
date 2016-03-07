import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.io.InputStreamReader;
import java.io.FileInputStream;



public class test {

	public static ArrayList<String> wordlist = new ArrayList<String>();
	public static ArrayList<Integer> wordcount= new ArrayList<Integer>();	
	public static Integer[] filecountlist = null;	
	public static Integer[][] numlist = null;
	public static Integer[][] emotions = null;
	public static Integer filesInteger = 0;	
	public static Integer Threholdup = 60000;
	public static Integer Threholddown = 5;	
	
	public static void addword(String fileName) {
        BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			reader = new BufferedReader(isr);
			//reader = new BufferedReader(new FileReader(new File(fileName)));
			String tempString = "";
			while ((tempString = reader.readLine()) != null) {
				if (tempString.trim().equals(""))
					continue;
				filesInteger++;
				String[] tempString2 = tempString.trim().split("\t");
				if (tempString2.length >= 4 && (!tempString2[2].equals(" "))) {
					String[] wordString = tempString2[2].trim().split(" ");
					for (String str : wordString) {
						if ((!wordlist.contains(str)) && (!str.equals(" ")) && (!str.equals("")) && (str!= null)) {
 							wordlist.add(str);
 							wordcount.add(1);							
						}
						else if(wordlist.contains(str)){
							int tempint = wordlist.indexOf(str);
							if (tempint == -1)
								System.err.println("error");
							else 
								wordcount.set(tempint, wordcount.get(tempint)+1);
						}
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void countword(String fileName) {

		BufferedReader reader = null;
		
		filecountlist =new Integer [wordlist.size()];
		for(int i=0;i<wordlist.size();i++){
			filecountlist[i]=0;
		}
		numlist = new Integer[filesInteger][wordlist.size()];
		for (int i = 0; i < filesInteger; i++) {
			for (int j = 0; j < wordlist.size(); j++) {
				numlist[i][j] = 0;
			}
		}
		emotions = new Integer[filesInteger][8];
		for (int i = 0; i < filesInteger; i++) {
			for (int j = 0; j < 8; j++) {
				emotions[i][j] = 0;
			}
		}
		
		
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			reader = new BufferedReader(isr);
			//reader = new BufferedReader(new FileReader(new File(fileName)));
			String tempString = "";
			int lines = 0;
			while ((tempString = reader.readLine()) != null) {
				if (tempString.trim().equals(""))
					continue;
				String[] tempString2 = tempString.trim().split("\t");
				if (tempString2.length >= 4) {
					if (tempString2[3].length() != 0) {
						String[] tempString3 = tempString2[3].split(" ");
						if (tempString3.length == 8) {
							int i = 0;
							for (i = 0; i < 8; i++) {
								emotions[lines][i] = Integer.parseInt(tempString3[i]);
							}
						}
						/*for (int i = 0; i < 8; i++) {
							System.out.println(tempString2[i+3]);
							emotions[lines][i] = Integer.parseInt(tempString2[i+3]);
						}*/
					}
					if (tempString2[2].length() != 0) {
						String[] tempString3 = tempString2[2].split(" ");
						ArrayList<String> tempList = new ArrayList<String>();
						for (String str : tempString3) {
							int tempint = wordlist.indexOf(str);
							if (tempint == -1)
								//System.err.println("error");
								continue;
							else {
								numlist[lines][tempint]++;
								wordcount.set(tempint, wordcount.get(tempint)+1);
							}
							if (!tempList.contains(str))
								tempList.add(str);
						}
						for (String str : tempList) {
							int tempint = wordlist.indexOf(str);
							if (tempint == -1)
								//System.err.println("error");
								continue;
							else {
								filecountlist[tempint]++;
							}
						}
					}
				}
				else{
					System.err.println("error format " + lines  +" "+  tempString );
				}
				lines++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static String writeline(int line){
		int sum=0;
		String resultString="";
		for(int i=0;i<wordlist.size();i++){
			sum+=numlist[line][i];
		}
		for(int i=0;i<wordlist.size();i++){
			float idf=(float)Math.log(((float)filesInteger)/filecountlist[i]);
			float tf=((float)numlist[line][i])/sum;
			float multi=tf*idf;
			String tempString=Float.toString(multi);
			resultString+=tempString+",";
		}
		/*sum=0;
		for(int i=0;i<8;i++){
			sum+=emotions[line][i];
		}
		for(int i=0;i<8;i++){
			if(((float)emotions[line][i])/sum>=Threhold){
				resultString+="1";
			}
			else {
				resultString+="0";
			}
			if(i!=7)
				resultString+=",";
		}*/
		for(int i=0;i<8;i++){
			resultString+=Integer.toString(emotions[line][i]);
			if(i!=7)
				resultString+=",";
		}
		return resultString;
	}
	
	public static void write(String path) {
		String nameString="SinaEmotion.arff";
		String relation="@relation";
		String attribute="@attribute";
		String data="@data";
		String numeric="numeric";
		String String01="{0,1}";
		BufferedWriter writer=null;
		BufferedWriter tw=null;
		try {
			tw = new BufferedWriter(new FileWriter(new File("wordlistEmotionSina.txt")));
			writer = new BufferedWriter(new FileWriter(new File(path+nameString)));
			writer.write(relation+" emotions");
			writer.newLine();
			writer.newLine();
			int location=0;
			for (String word : wordlist) {
				tw.write("f"+String.valueOf(location)+" = "+word);
				tw.newLine();
				location++;
			}
			location=0;
			for (String word : wordlist) {
				//writer.write(attribute+" f"+String.valueOf(location)+" "+numeric);
				writer.write(attribute+" f"+String.valueOf(location)+" " +numeric);
				writer.newLine();
				location++;
			}
			for(int i=0;i<8;i++){
				writer.write(attribute+" c"+String.valueOf(i)+" "+String01);
				writer.newLine();
			}
			writer.newLine();
			writer.write(data);
			writer.newLine();
			for(int i=0;i<filesInteger;i++){
				String tempString=writeline(i);
				writer.write(tempString);
				writer.newLine();
				if(i%50==0)
					System.out.println(i+" complete ");
			}
			writer.close();
			tw.close();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	public static void writeTestARFF(String path)	{
		String nameString="test.arff";
		String relation="@relation";
		String attribute="@attribute";
		String data="@data";
		String numeric="numeric";
		String String01="{0,1}";
		BufferedWriter writer=null;
		BufferedWriter tw=null;
		try {
			tw = new BufferedWriter(new FileWriter(new File("wordlist.txt")));
			writer = new BufferedWriter(new FileWriter(new File(path+nameString)));
			writer.write(relation+" emotions");
			writer.newLine();
			writer.newLine();
			int location=0;
			for (String word : wordlist) {
				tw.write("f"+String.valueOf(location)+" = "+word);
				tw.newLine();
				location++;
			}
			location=0;
			for (String word : wordlist) {
				writer.write(attribute+" f"+String.valueOf(location)+" " +numeric);
				writer.newLine();
				location++;
			}
			for(int i=0;i<8;i++){
				writer.write(attribute+" c"+String.valueOf(i)+" "+String01);
				writer.newLine();
			}
			writer.newLine();
			writer.write(data);
			writer.newLine();
			for(int i=0;i<filesInteger;i++){
				String tempString=writeline(i);
				writer.write(tempString);
				writer.newLine();
				if(i%50==0)
					System.out.println(i+" complete ");
			}
			writer.close();
			tw.close();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	public static void filter(){
		for (int i= wordcount.size()-1;i>=0;i--) {
			if(wordcount.get(i)>Threholdup||wordcount.get(i)<Threholddown){
				wordcount.remove(i);
				wordlist.remove(i);
			}				
		}
		
		
	}


	public static void main(String[] args) throws Exception {
		
	/*	FilterTest filter = new FilterTest();
        filter.getFileInstances( "D:\\data\\20131220\\weka_5.arff");
        filter.selectAttUseFilter();
        filter.selectAttUseMC();
    */    
		
		String path="";
		String pathString = path+"labeledData.txt";
		long start=System.nanoTime();
		addword(pathString);
		long t1=System.nanoTime();
		System.out.println("add_cost:"+((double) (t1 - start) / (1000 * 1000 * 1000))+ "s");
		if (wordlist.size() == 0)
			System.err.println("error");
		System.err.println("all:"+wordlist.size());
		System.out.println(wordlist.size()+"\t"+wordcount.size());
		filter();
		System.out.println(wordlist.size()+"\t"+wordcount.size());
		countword(pathString);		
		long t2=System.nanoTime();
		System.out.println("count_cost:"+((double) (t2 - t1) / (1000 * 1000 * 1000))+ "s");
 		write(path);
		long t3=System.nanoTime();
		System.out.println("write_cost:"+((double) (t3 - t2) / (1000 * 1000 * 1000))+ "s"); 
		
		
	}
	
}
