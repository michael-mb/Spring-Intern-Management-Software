package kickstart.orders;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import kickstart.staff.Skill;

@Entity
public class Orders {

	private @Id @GeneratedValue long order_id;
	private String description;
	private String customerName;
	private String customerContact; 
	private LocalDate date;
	private LocalDate finishDateCustomer;
	private LocalDate finishDateIntern;
	private LocalDate pickUpDate;
	private Skill skill;
	private Status status;
	private SubCategory subcategory ;
	private Color skillColor = null;
	private Color statusColor = null;
	private String isAcceptedBy ;
	
	/*
	 * HOUR
	 * 
	 * 08:00 - 10:00 -> 1
	 * 
	 * 10:00 - 12:00 -> 2
	 * 
	 * 12:00 - 14:00 -> 3
	 *
	 * 14:00 - 16:00 -> 4
	 *
	 * DAY
	 * 
	 * Montag     -> 1
	 * 
	 * Dienstag   -> 2
	 * 
	 * Mittwoch   -> 3
	 * 
	 * Donnerstag -> 4
	 * 
	 * Freitag    -> 5
	 * 
	 * 
	 */
	
	private int hour = -1;
	private int day = -1;
	private int room = -1;
	
	@SuppressWarnings("unused")
	private Orders() {}

	public Orders(LocalDate date, String description, Skill skill , SubCategory subcategory ,
				  String customerName , String customerContact) {

		this.date = date;
		this.status = Status.CREATED;
		setStatusColor(status);

		this.finishDateCustomer = null;

		this.description = description;
		this.skill = skill;
		setSkillColor(skill);
		this.customerName = customerName ;
		this.customerContact = customerContact ;
		this.subcategory = subcategory ;
		this.isAcceptedBy = null ;
	}

	

	public LocalDate getDate() {
		return date;
	}

	public long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long id) {
		this.order_id = id ;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getFinishDateCustomer() {
		return finishDateCustomer;
	}
	public void setFinishDateCustomer(LocalDate finishDateCustomer) {
		this.finishDateCustomer = finishDateCustomer;
	}

	public LocalDate getFinishDateIntern() {
		return finishDateIntern;
	}
	public void setFinishDateIntern(LocalDate finishDateIntern) {
		this.finishDateIntern = finishDateIntern;
	}

	public LocalDate getPickUpDate() {
		return pickUpDate;
	}
	public void setPickUpDate(LocalDate pickUpDate) { this.pickUpDate = pickUpDate; }

	public Skill getSkill() {
		return skill;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerContact() {
		return customerContact;
	}
	
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	
	public Status getStatus() {
		return this.status ;
	}
	public void setStatus(Status status) {
		this.status = status;
		setStatusColor(status);
	}
	
	public SubCategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(SubCategory subcategory) {
		this.subcategory = subcategory;
	}
	
	
	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}
	
	public int getRoom() {
		return room;
	}

	public String getSkillColor() {
		String result = "#" + decodeColor(skillColor.getRed())
				+ decodeColor(skillColor.getGreen())
				+ decodeColor(skillColor.getBlue());
		return result;
	}

	public void setSkillColor(Skill skill) {
		if(skill != null) {
			switch (skill) {
				case CLEAN:
					skillColor = Color.BLUE;
					break;
				case ELEKTRO:
					skillColor = Color.YELLOW;
					break;
				case PATCH:
					skillColor = Color.WHITE;
					break;
				case GRIND:
					skillColor = Color.BLACK;
					break;
				case KEY:
					skillColor = Color.GRAY;
					break;
				case SEW:
					skillColor = Color.LIGHT_GRAY;
					break;
			}
		}
	}

	public String getStatusColor() {
		String result = "#" + decodeColor(statusColor.getRed())
				+ decodeColor(statusColor.getGreen())
				+ decodeColor(statusColor.getBlue());
		return result;
	}

	public void setStatusColor(Status status) {
		if(status != null) {
			switch (status) {
				case CREATED:
					statusColor = Color.RED;
					break;
				case ACCEPTED:
					statusColor = Color.YELLOW;
					break;
				case DECLINED:
					statusColor = Color.BLACK;
					break;
				case DONE:
					statusColor = Color.GREEN;
					break;
				case PICKED_UP:
					statusColor = Color.MAGENTA;
					break;
				case STORED:
					statusColor = Color.CYAN;
					break;
				case DONATED:
					statusColor = Color.WHITE;
					break;
			}
		}
	}

	public String decodeColor(int color) {
		String result = Integer.toHexString(color).toUpperCase();
		if(result.length() == 1) {
			result = "0" + result;
		}
		return result;
	}

	public void updateTimeSlot(int hour , int day , int room) {
		this.day = day ;
		this.hour = hour ;
		this.room = room ;
	}
	
	public String getIsAcceptedBy() {
		return isAcceptedBy;
	}

	public void setIsAcceptedBy(String isAcceptedBy) {
		this.isAcceptedBy = isAcceptedBy;
	}
	
	public String toString() {
		String str = "";
		
		str ="customerName : " + customerName;
		return str ;
	}
}
