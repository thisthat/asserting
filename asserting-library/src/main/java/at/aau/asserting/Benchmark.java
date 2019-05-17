package at.aau.asserting;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Benchmark {
	static long startApproach;
	static long endApproach;
	static long startRecovery;
	static long endRecovery;
	static long id;

	static boolean enable = false;

	public static void id(long id){
		Benchmark.id = id;
		Benchmark.enable = true;
	}
	public static void startApproach(){
		startApproach = System.currentTimeMillis();
	}
	public static void startRecovery(){
		endApproach = System.currentTimeMillis();
		startRecovery = System.currentTimeMillis();
	}
	public static void termination(){
		endRecovery = System.currentTimeMillis();
	}

	public static void write(){
		if(!enable) return;
		String path = System.getProperty("user.home") + "/dataset.csv";
		Path file = Paths.get(path);
		try {
			new File(file.toAbsolutePath().toString()).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long durationRecovery = startRecovery - endRecovery;
		long durationApproach = startApproach - endApproach;
		String data = String.format("%d,%d,%d,%d,%d,%d,%d", id,
			startApproach, endApproach, durationApproach,
			startRecovery, endRecovery, durationRecovery
		);
		List<String> lines = Arrays.asList(data);
		try {
			Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
