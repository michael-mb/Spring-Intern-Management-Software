package kickstart.orders;

import static kickstart.staff.Skill.CLEAN;
import static kickstart.staff.Skill.ELEKTRO;
import static kickstart.staff.Skill.GRIND;
import static kickstart.staff.Skill.KEY;
import static kickstart.staff.Skill.PATCH;
import static kickstart.staff.Skill.SEW;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
public class OrdersDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(OrdersDataInitializer.class);

	private final OrdersManagement ordersManagement;

	OrdersDataInitializer(OrdersManagement ordersManagement) {

		Assert.notNull(ordersManagement, "orderManagement must not be null!");

		this.ordersManagement = ordersManagement;
	}

	@Override
	public void initialize() {

		LOG.info("Creating default Orders");
		/*
		OrderCreationFrom init1 = new OrderCreationFrom("Musterbeschreibung 1", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" ,  SubCategory.NAEHTE);
		ordersManagement.addOrder(init1).updateTimeSlot(1 , 2 , 1);
		
		OrderCreationFrom init2 = new OrderCreationFrom("Musterbeschreibung 2", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ABSAETZE);
		ordersManagement.addOrder(init2).updateTimeSlot(1 , 2 , 1);
		
		OrderCreationFrom init3 = new OrderCreationFrom("Musterbeschreibung 3", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SOHLEN);
		ordersManagement.addOrder(init3).updateTimeSlot(2 , 2 , 1);
		
		OrderCreationFrom init4 = new OrderCreationFrom("Musterbeschreibung 4", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KNOEPFE);
		ordersManagement.addOrder(init4).updateTimeSlot(2 , 2 , 2);
		
		OrderCreationFrom init5 = new OrderCreationFrom("Musterbeschreibung 5", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.FLICKEN);
		ordersManagement.addOrder(init5).updateTimeSlot(3 , 2 , 1);
		
		OrderCreationFrom init6 = new OrderCreationFrom("Musterbeschreibung 6", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.NAEHTE);
		ordersManagement.addOrder(init6).updateTimeSlot(3 , 2 , 2);
		
		OrderCreationFrom init7 = new OrderCreationFrom("Musterbeschreibung 7", KEY,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHILDER_GRAVIEREN);
		ordersManagement.addOrder(init7).updateTimeSlot(4 , 2 , 1);

		OrderCreationFrom init8 = new OrderCreationFrom("Musterbeschreibung 8", KEY ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHLUESSEL_KOPIEREN);
		ordersManagement.addOrder(init8).updateTimeSlot(4, 2 , 2);
		
		OrderCreationFrom init9 = new OrderCreationFrom("Musterbeschreibung 9", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LEDER);
		ordersManagement.addOrder(init9).updateTimeSlot(1 , 3 , 1);
		
		OrderCreationFrom init10 = new OrderCreationFrom("Musterbeschreibung 10", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.WAESCHE);
		ordersManagement.addOrder(init10).updateTimeSlot(1 , 3 , 2);

		OrderCreationFrom init11 = new OrderCreationFrom("Musterbeschreibung 11", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ANZUEGE);
		ordersManagement.addOrder(init11).updateTimeSlot(2 , 3 , 1);

		OrderCreationFrom init12 = new OrderCreationFrom("Musterbeschreibung 12", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.MESSER_SCHAERFEN);
		ordersManagement.addOrder(init12).updateTimeSlot(2 , 3 , 2);

		OrderCreationFrom init13 = new OrderCreationFrom("Musterbeschreibung 13", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHEREN);
		ordersManagement.addOrder(init13).updateTimeSlot(3 , 3 , 1);

		OrderCreationFrom init14 = new OrderCreationFrom("Musterbeschreibung 14", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KABEL_ERSETZEN);
		ordersManagement.addOrder(init14).updateTimeSlot(3 , 3 , 2);

		OrderCreationFrom init15 = new OrderCreationFrom("Musterbeschreibung 15", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LOETEN);
		ordersManagement.addOrder(init15).updateTimeSlot(4 , 3 , 1);

		OrderCreationFrom init16 = new OrderCreationFrom("Musterbeschreibung 16", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" ,  SubCategory.NAEHTE);
		ordersManagement.addOrder(init16).updateTimeSlot(4 , 3 , 2);

		OrderCreationFrom init17 = new OrderCreationFrom("Musterbeschreibung 17", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ABSAETZE);
		ordersManagement.addOrder(init17).updateTimeSlot(1 , 4 , 1);

		OrderCreationFrom init18 = new OrderCreationFrom("Musterbeschreibung 18", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SOHLEN);
		ordersManagement.addOrder(init18).updateTimeSlot(1 , 4 , 2);

		OrderCreationFrom init19 = new OrderCreationFrom("Musterbeschreibung 19", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KNOEPFE);
		ordersManagement.addOrder(init19).updateTimeSlot(2 , 4 , 1);

		OrderCreationFrom init20 = new OrderCreationFrom("Musterbeschreibung 20", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.FLICKEN);
		ordersManagement.addOrder(init20).updateTimeSlot(2 , 4 , 2);

		OrderCreationFrom init21 = new OrderCreationFrom("Musterbeschreibung 21", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.NAEHTE);
		ordersManagement.addOrder(init21).updateTimeSlot(3 , 4 , 1);

		OrderCreationFrom init22 = new OrderCreationFrom("Musterbeschreibung 22", KEY,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHILDER_GRAVIEREN);
		ordersManagement.addOrder(init22).updateTimeSlot(3 , 4 , 2);

		OrderCreationFrom init23 = new OrderCreationFrom("Musterbeschreibung 23", KEY ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHLUESSEL_KOPIEREN);
		ordersManagement.addOrder(init23).updateTimeSlot(4, 4 , 1);

		OrderCreationFrom init24 = new OrderCreationFrom("Musterbeschreibung 24", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LEDER);
		ordersManagement.addOrder(init24).updateTimeSlot(4 , 4 , 2);

		OrderCreationFrom init25 = new OrderCreationFrom("Musterbeschreibung 25", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.WAESCHE);
		ordersManagement.addOrder(init25).updateTimeSlot(1 , 5 , 1);

		OrderCreationFrom init26 = new OrderCreationFrom("Musterbeschreibung 26", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ANZUEGE);
		ordersManagement.addOrder(init26).updateTimeSlot(1 , 5 , 2);

		OrderCreationFrom init27 = new OrderCreationFrom("Musterbeschreibung 27", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.MESSER_SCHAERFEN);
		ordersManagement.addOrder(init27).updateTimeSlot(2 , 5 , 1);

		OrderCreationFrom init28 = new OrderCreationFrom("Musterbeschreibung 28", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHEREN);
		ordersManagement.addOrder(init28).updateTimeSlot(2 , 5 , 2);

		OrderCreationFrom init29 = new OrderCreationFrom("Musterbeschreibung 29", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KABEL_ERSETZEN);
		ordersManagement.addOrder(init29).updateTimeSlot(3 , 5 , 1);

		OrderCreationFrom init30 = new OrderCreationFrom("Musterbeschreibung 30", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LOETEN);
		ordersManagement.addOrder(init30).updateTimeSlot(3 , 5 , 2);
		*/
	}
}