package pl.jw.mbank.biz.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptConverter {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("mBankSfiDb.script")));

		int id = 1;

		String line = null;
		while ((line = br.readLine()) != null) {
			String newLine = line;

			if (line.startsWith("INSERT INTO STOCKQUOTES VALUES(")) {
				String[] parts = line.split("[,')(]");
				String date = parts[2].substring(0, 10);
				String sfiId = parts[4];
				String value = parts[5];
				String delta = parts[6];

				newLine = parts[0] + "(" + id + ", '" + date + "' ," + value + ", " + delta + ", " + sfiId + ")";
			}

			System.out.println(newLine);

			++id;
		}
	}
}
