package assessment;

public class CountryAirports implements Comparable<CountryAirports> {

	private String country;
	private int airports;
	
	public CountryAirports(String country, int airports) {
		this.country = country;
		this.airports = airports;
	}
	
	public String getCountry() {
		return country;
	}
	
	public int getAirports() {
		return airports;
	}
	
	@Override
	public int compareTo(CountryAirports o) {
		return this.airports - o.airports;
	}
	
	public String toString() {
		return country + " " + airports;
	}
}
