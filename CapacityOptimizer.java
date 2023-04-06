public class CapacityOptimizer {
	private static final int NUM_RUNS = 10;

	private static final double THRESHOLD = 5.0d;

	public static int getOptimalNumberOfSpots(int hourlyRate) {
	
		int lotSize = 1;
		int steps = 86400;
		int queuesize = 0;
		int averageQueue = 0;
		boolean simulation = true;

		while (simulation) {

			System.out.println();
			System.out.println("==== Setting lot capacity to: " + lotSize + " ====");

			for (int i = 0; i < 10; i++) {
				ParkingLot park = new ParkingLot(lotSize);
				long simustart = System.currentTimeMillis();
				Simulator simulate = new Simulator(park, hourlyRate, steps);
				simulate.simulate();
				long simuend = System.currentTimeMillis();
				queuesize = queuesize + simulate.getIncomingQueueSize();
				System.out.println("Simulation run " + (i+1) + " (" + (simuend-simustart) + "ms" + ") ; Queue length at the end of the simulation run: " + simulate.getIncomingQueueSize());
			}
			
			averageQueue = queuesize/10;
			queuesize = 0;

			if (averageQueue <= 5) {
				simulation = false;
			} else {
				lotSize++;
			}
		}

		return lotSize;

	}

	public static void main(String args[]) {
	
		StudentInfo.display();

		long mainStart = System.currentTimeMillis();

		if (args.length < 1) {
			System.out.println("Usage: java CapacityOptimizer <hourly rate of arrival>");
			System.out.println("Example: java CapacityOptimizer 11");
			return;
		}

		if (!args[0].matches("\\d+")) {
			System.out.println("The hourly rate of arrival should be a positive integer!");
			return;
		}

		int hourlyRate = Integer.parseInt(args[0]);

		int lotSize = getOptimalNumberOfSpots(hourlyRate);

		System.out.println();
		System.out.println("SIMULATION IS COMPLETE!");
		System.out.println("The smallest number of parking spots required: " + lotSize);

		long mainEnd = System.currentTimeMillis();

		System.out.println("Total execution time: " + ((mainEnd - mainStart) / 1000f) + " seconds");

	}
}