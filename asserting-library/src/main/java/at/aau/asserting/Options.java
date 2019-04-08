package at.aau.asserting;

public class Options {

	private static Options instance;

	private boolean isVerbose = false;
	private boolean isPrintModel = false;

	private Options(){}

	private static Options getInstance(){
		if(instance == null){
			synchronized (Options.class) {
				if(instance == null){
					instance = new Options();
				}
			}
		}
		return instance;
	}

	public static boolean isVerbose() {
		return getInstance().isVerbose;
	}

	public static void setVerbose(boolean verbose) {
		getInstance().isVerbose = verbose;
	}

	public static boolean isPrintModel() {
		return getInstance().isPrintModel;
	}

	public static void setPrintModel(boolean isPrintModel) {
		getInstance().isPrintModel = isPrintModel;
	}

}
