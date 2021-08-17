package assessment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CSVReader {
	
	Scanner scanner = new Scanner(System.in);
	private final String airportsPath = "src/resources/airports.csv";
	private final String countriesPath = "src/resources/countries.csv";
	private final String runwaysPath = "src/resources/runways.csv";
	private ArrayList<Airport> airports = new ArrayList<Airport>();
	private ArrayList<Runway> runways = new ArrayList<Runway>();
	private ArrayList<Country> countries = new ArrayList<Country>();
	
	public static void main(String[] args) {
		
		CSVReader test = new CSVReader();
		
		
	}
	
	public CSVReader() {
		System.out.println("This is a tool for retrieving information stored in CSV files. If anything goes wrong right now, please consult the readme file and "
				+ "make sure the CSV files are placed correctly and are not corrupted.");
		loadInfo();
		programLoop();
		
		
	}
	
	public void programLoop() {
		System.out.println("For commands, consult the readme file or type help");
		String command = "";
		for(;;) {
			command = scanner.nextLine();
			System.out.println(commandHandler(command));
		}
		
		
		
		
	}
	
	public String commandHandler(String command) {
		if(command == null) {
			return "invalid command";
		}
		String result = "";
		String[] splitted = command.split("\\s+");
		switch(splitted[0]) {
			case "help":
				result = "To see how many runways a country has, type runways followed by a space, then the country name or country code \n"
						+ "If you want to see countries with the most airports, type top followed by how many countries youd like to see (default is 10)\n"
						+ "If you wish to stop this program type stop";
				break;
			case "runways":
				if(splitted.length == 2 && determineCountryId(splitted[1]) != null) {
					ArrayList<AirportTuple> runwaysList = getRunways(splitted[1]);
					if(!runwaysList.isEmpty()) {
						result = determineCountryName(splitted[1]).replace("\"", "") + " has the following airports:\n";
						for(AirportTuple a :runwaysList) {
							result += a.getAirport().replace("\"", "") + " has ";
							if(!a.getRunways().isEmpty()) {
								result += "runway";
								if(a.getRunways().size() > 1) {
									result += "s ";
									for(String r:a.getRunways()) {
										result += r + ", ";
									}
								} else {
									result += " " + a.getRunways().get(0) + "  ";
								}
								
								result = result.substring(0, result.length()-2);
							} else {
								result += "no runways";
							}
							result += "\n";
						}
						
					} else {
						result = "The given country name could not be found or was not correctly entered, please try again";
					}
				} else {
					result = "You have entered too many words or the country could not be found, please try again";
				}
				
				break;
			case "top":
				if(splitted.length <= 2) {
					if (splitted.length == 1) {
						result = "The top 10 countries with highest number of airports are:\n";
						result += prettyPrintCountryAirports(10);
					} else {
						try {
							result += prettyPrintCountryAirports(Integer.parseInt(splitted[1]));
						} catch (NumberFormatException e) {
							result = "Please input a valid number";
						}
					}
				} else {
					result = "You have entered too many words, please try again";
				}
				break;
			case "stop":
				System.out.println("Closing the program");
				scanner.close();
				System.exit(0);
			default:
				result = "Invalid command, please try again or type help";
		}
		return result;
	}
	
	public String prettyPrintCountryAirports(int amount) {
		int counter = 1;
		String result = "";
		List<CountryAirports> list = mostAirports(amount);		
		for(CountryAirports ca:list) {
			result += counter + ". " + ca.getCountry().replace("\"", "") + " has " + ca.getAirports() + " airports\n";
			counter++;
		}
		return result;
	}
	
	/**
	 * Creates a list of how many airports every country has, ordered from high to low
	 * @param amount the amount of countries to display
	 * @return a list of amount long of countries and how many airports they have
	 */
	public List<CountryAirports> mostAirports(int amount){
		ArrayList<CountryAirports> result = new ArrayList<CountryAirports>();
		int counter = 0;
		for(Country country: countries) {
			for(Airport airport: airports) {
				if(country.getCode().equals(airport.getCountry())){
					counter++;
				}
			}
			result.add(new CountryAirports(country.getName(), counter));
			counter = 0;
		}
		Collections.sort(result);
		Collections.reverse(result);
		
		return result.subList(0, amount);
	}
	
	/**
	 * Loads the info from the CSV files
	 */
	public void loadInfo() {
		
		try {
			BufferedReader bra = new BufferedReader(new FileReader(airportsPath));
			String line;
			String[] values;
			
			while((line = bra.readLine()) != null) {
				values = line.split(",");
				airports.add(new Airport(values[1], values[3], values[8]));	
			}
			bra.close();
			
			BufferedReader brr = new BufferedReader(new FileReader(runwaysPath));
			
			while((line = brr.readLine()) != null) {
				values = line.split(",");
				runways.add(new Runway(values[0], values[2]));	
			}
			brr.close();
			
			BufferedReader brc = new BufferedReader(new FileReader(countriesPath));
			
			while((line = brc.readLine()) != null) {
				values = line.split(",");
				countries.add(new Country(values[1], values[2]));	
			}
			brc.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find the files, please carefully read the readme file. Please exit the program and try again");
			scanner.next();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * 
	 * @param country this can be the name of the country or the country id
	 * @return a list of tuples containing the airport name and their runways, if the country can not be found, returns null
	 */
	public ArrayList<AirportTuple> getRunways(String country){
		ArrayList<AirportTuple> result = new ArrayList<AirportTuple>();
		String countryId = determineCountryId(country);
		
		for(Airport airport:airports) {
			if(airport.getCountry().equals(countryId)) {
				result.add(new AirportTuple(airport.getName(), getRunwaysHelper(airport.getId())));
			}
		}
		
		return result;
	}
	
	/** Helper fuction
	 * Gives the ids of the runaways a given airport has
	 * If the given airport does not have runways or does not exist, returns an empty string list
	 * @param airport the airport id
	 * @return the runway ids from given airport
	 */
	public ArrayList<String> getRunwaysHelper(String airport){
		ArrayList<String> result = new ArrayList<String>();
		for(Runway runway: runways) {
			if(runway.getAirportId().equals(airport)) {
				result.add(runway.getId());				
			}
		}			
		return result;
	}
	
	/**
	 * This method will return the id of the country, regardless of if the input was a name or the id itself
	 * @param country which can be the country name or the id
	 * @return the country id, if this does not exist, it will return null
	 */
	public String determineCountryId(String country) {
		String result = null;
		String toFind = "\"" + country + "\"";
		for(Country c:countries) {
			if(toFind.equals(c.getCode()) || toFind.equals(c.getName())) {
				result = c.getCode();
			}
		}
			
		return result;
	}
	
	/**
	 * Same as the determineCountryId method, but for country names
	 * @param country which can be the country name or the id
	 * @return the country name, if this does not exist, it will return null
	 */
	public String determineCountryName(String country) {
		String result = null;
		String toFind = "\"" + country + "\"";
		for(Country c:countries) {
			if(toFind.equals(c.getCode()) || toFind.equals(c.getName())) {
				result = c.getName();
			}
		}
			
		return result;
	}
	
	
	
	
}
