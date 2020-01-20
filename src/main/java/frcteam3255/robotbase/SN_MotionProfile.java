package frcteam3255.robotbase;

import java.io.*;
import java.util.Scanner;

import edu.wpi.first.wpilibj.Filesystem;


/*SN motion profile contains the functions necessary to take a CSV file put in deploy and load it into
TalonSRX motion profiling*/
public class SN_MotionProfile {


	//counter taken from stackoverflow: https://stackoverflow.com/questions/18009416/how-to-count-total-rows-in-csv-using-java
	public static int count(String filename) throws IOException {
		InputStream is = new BufferedInputStream(
				new FileInputStream(Filesystem.getDeployDirectory() + "/paths/" + filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static double[][] reader(String fileName) throws IOException {
		// Get scanner instance
		File toScan = new File(Filesystem.getDeployDirectory()+"/paths", fileName);
		System.out.println(Filesystem.getDeployDirectory()+"/paths");
		Scanner scanner = new Scanner(toScan);
		double[][] output = new double[count(fileName)+1][3];

		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(!line.contains("Position")){
				int j = 0;
				String[] fields = line.split(",");
					for (String field : fields){
						if(j < 3){
							if(fileName.contains("neg")&&(j==0||j==1)){
							
								output[i][j] = Double.parseDouble(field.replaceAll("\\s+",""));
							}else{
								output[i][j] = -Double.parseDouble(field.replaceAll("\\s+",""));
							}
						}
						j++;
				} 
				i++;
			}else{

				output = new double[count(fileName)-1][3];
			}
		}
		scanner.close();
		return output;

	}


}
