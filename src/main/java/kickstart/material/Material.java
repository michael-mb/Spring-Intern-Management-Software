package kickstart.material;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Repräsentiert ein Material zur Verwendung in einem Auftrag
 */
@Entity
public class Material {

	@Id
	@GeneratedValue
	private long id ;
	private int amount ;
	private String name ;
	private int price ;
	private int allowedLimit = 0 ;
	
	
	@SuppressWarnings("unused")
	private Material() {}

	/**
	 *
	 * @param name Name des Materials
	 * @param amount Stückzahl des Materials am Anfang
	 * @param price Preis des Materials pro Stück
	 * @param allowedLimit Mindeststückzahl des Materials vor erforderlicher Auffüllung
	 */
	Material(String name , int amount  , int price  , int allowedLimit ){
		this.name = name ;
		this.amount = amount ;
		this.price = price ;
		this.allowedLimit = allowedLimit ;
	}

	/**
	 *
	 * @return Gibt die Identifikationsnummer des Materials zurück
	 */
	public long getId() {
		return this.id;
	}

	/**
	 *
	 * @return Gibt die Stückzahl des Materials zurück
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 *
	 * @return Gibt den Namen des Materials zurück
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen des Materials neu
	 * @param name neuer Name des Materials
	 */
	public void setName(String name) {
		this.name = name ; 
	}

	/**
	 *
	 * @return Gibt die Mindeststückzahl des Materials zurück
	 */
	public int getAllowedLimit() {
		return allowedLimit;
	}

	/**
	 *
	 * @return Gibt den Preis des Materials pro Stück zurück
	 */
	public int getPrice() {
		return this.price;
	}

	/**
	 * Setzt die Mindeststückzahl des Materials neu
	 * @param allowedLimit neue Mindeststückzahl des Materials
	 */
	public void setAllowedLimit(int allowedLimit) {
		this.allowedLimit = allowedLimit;
	}

	/**
	 * Setzt die Stückzahl des Materials neu
	 * @param amount neue Stückzahl des Materials
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Setzt die Identifikationsnummer des Materials neu
	 * @param id neue Identifikationsnummer des Materials
	 */
	public void setId(Long id) {
		this.id = id ;
	}

	/**
	 *
	 * @return Gibt die Gesamtkosten der Materialkosten dieses Materials für einen Auftrag zurück
	 */
	public int newOder() {
		int order = 0 ;
		if(this.amount < allowedLimit) {
			order = allowedLimit - amount ;
		}
		return order * price ;
	}

	/**
	 *
	 * @return Gibt die Identifikationsnummer, den Namen und die Stückzahl des Materials aufgelistet zurück
	 */
	public String toString() {
		String str = "";
		
		str = "ID: "+ id + " Name: "+name+ " Amount: "+amount;
		
		return str ;
		
	}
}
