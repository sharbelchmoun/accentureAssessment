package assessment;

public class Runway {
	private String id;
	private String airport_ident;
	
	public Runway(String id, String airport_ident) {
		this.id = id;
		this.airport_ident = airport_ident;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAirportId() {
		return airport_ident;
	}
	
}
