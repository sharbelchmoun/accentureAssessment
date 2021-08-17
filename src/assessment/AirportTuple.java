package assessment;

import java.util.List;

public class AirportTuple {
	private String airport;
	private List<String> runways;
	
	
	public AirportTuple(String airport, List<String> runways) {
		this.airport = airport;
		this.runways = runways;
	}
	
	public String getAirport() {
		return airport;
	}

	public List<String> getRunways(){
		return runways;
	}
	
	public String toString() {
		return airport + ": " + runways.toString();
	}
}
