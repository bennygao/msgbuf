package JFlex.tests;

import java.io.File;

import JFlex.Options;
import JFlex.anttask.JFlexTask;

public class TestEncoding {
	public static void main(String[] args) {
		JFlexTask task = new JFlexTask();
		task.setFile(new File("../MessageCompiler/grammar/mc.x"));
		task.setDestdir(new File("../MessageCompiler/src"));
		task.setEncoding("utf8");
		Options.verbose = true;
		Options.no_backup = true;
		task.execute();
	}

}
