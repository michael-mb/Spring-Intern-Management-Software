package kickstart.orders;

import kickstart.staff.Skill;

public class OrderCreationFrom {
	private String description;
	private Skill skill;
	private String customerName ;
	private String customerContact;
	private SubCategory subcategory ;
	private Status status ;


	public OrderCreationFrom(String description, Skill skill , String customerName ,
							 String customerContact , SubCategory subcategory) {
		this.description = description;
		this.skill = skill;
		this.customerName = customerName;
		this.customerContact = customerContact ;
		this.subcategory = subcategory ;
	}



	public String getCustumerName() {
		return this.customerName;
	}
	
	public String getDescription() {
		return description;
	}

	public Skill getSkill() {
		return skill;
	}
	
	public String getCustomerContact() {
		return customerContact;
	}
	
	public void setSkill(Skill skill) {
		this.skill = skill ;
	}
	
	public SubCategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(SubCategory subcategory) {
		this.subcategory = subcategory;
	}
	
	public Status getStatus() {
		return status ;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
}
