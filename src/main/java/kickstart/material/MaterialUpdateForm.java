package kickstart.material;

/**
 * Repräsentiert das Eingabeformular bei der Aktualisierung eines Materials
 */
public class MaterialUpdateForm {
	private String name;
	private int amount;
	private int price;
	private int limit;

	/**
	 *
	 * @param name Eingabefeld für den Namen des Materials
	 * @param amount Eingabefeld für die Stückzahl des Materials
	 * @param price Eingabefeld für den Preis des Materials pro Stück
	 * @param limit Eingabefeld für die Mindeststückzahl des Materials
	 */
	public MaterialUpdateForm(String name, int amount, int price , int limit) {
		this.name = name ;
		this.amount = amount ;
		this.price = price ;
		this.limit = limit ;
	}

	/**
	 *
	 * @return Gibt den Namen des Materials zurück
	 */
	public String getName() {
		return name;
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
	 * @return Gibt den Preis des Materials zurück
	 */
	public int getPrice() {
		return price;
	}

	/**
	 *
	 * @return Gibt die Mindeststückzahl des Materials zurück
	 */
	public int getLimit() {
		return limit ;
	}
} 
