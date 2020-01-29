package kickstart.orders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import kickstart.material.MaterialController;
import kickstart.staff.Skill;
import kickstart.staff.Staff;
import kickstart.staff.StaffManagement;
import kickstart.time.TimeSlot;
import kickstart.time.TimeSlotManagement;

@Controller
public class OrdersController {
	private final OrdersManagement ordersManagement;
	private int selectedSkill;
	private Long orderId ;
	private Staff authenticatedStaff ;
	private final TimeSlotManagement timeSlotManagement;
	private int weekOffset;
	private final MaterialController materialController;
	
	OrdersController(OrdersManagement ordersManagement, TimeSlotManagement timeSlotManagement,
					 MaterialController materialController){
		
		Assert.notNull(ordersManagement, "OrderManagement must not be null!");
		Assert.notNull(timeSlotManagement, "TimeSlotManagement must not be null!");
		Assert.notNull(materialController, "MaterialController must not be null!");
		
		weekOffset = 0;
		this.ordersManagement = ordersManagement;
		this.timeSlotManagement = timeSlotManagement;
		this.materialController = materialController;
	}

	
	
	/*****  SHOW PAGES  *****/
	
	@GetMapping("/services")
	String showServicesPage(Model model , Authentication authentication){	
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}
		return "services";
	}
	


	@GetMapping("/orderpage/{order_Id}")
	String showOrderPage(Model model , @PathVariable long order_Id , Authentication authentication ){
		
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}
			
		orderId = order_Id ;
		model.addAttribute("order", ordersManagement.getOrderById(orderId).get());
		model.addAttribute("created" , Status.CREATED);
		model.addAttribute("accepted" , Status.ACCEPTED);
		model.addAttribute("done" , Status.DONE);
		model.addAttribute("stored" , Status.STORED);
		return "orderpage";
	}

	@GetMapping("/ordertable")
	String showOrderTable(Model model, Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}
		this.authenticatedStaff = StaffManagement.getStaffByName(authentication.getName());

		model.addAttribute("time1Slot1", toShowList(organizeList(ordersManagement.findByHoursRoom1(1))));
		model.addAttribute("time2Slot1", toShowList(organizeList(ordersManagement.findByHoursRoom1(2))));
		model.addAttribute("time3Slot1", toShowList(organizeList(ordersManagement.findByHoursRoom1(3))));
		model.addAttribute("time4Slot1", toShowList(organizeList(ordersManagement.findByHoursRoom1(4))));
		
		
		model.addAttribute("time1Slot2", toShowList(organizeList(ordersManagement.findByHoursRoom2(1))));
		model.addAttribute("time2Slot2", toShowList(organizeList(ordersManagement.findByHoursRoom2(2))));
		model.addAttribute("time3Slot2", toShowList(organizeList(ordersManagement.findByHoursRoom2(3))));
		model.addAttribute("time4Slot2", toShowList(organizeList(ordersManagement.findByHoursRoom2(4))));

		int startOfWeek = timeSlotManagement.getTime().toLocalDate().getDayOfWeek().getValue() - 1;
		LocalDate week = timeSlotManagement.getTime().toLocalDate().minusDays(startOfWeek).plusWeeks(weekOffset);
		model.addAttribute("week", "Woche vom " + week.getDayOfMonth() + "."
				+ week.getMonthValue() + "." + week.getYear() + " bis "
				+ week.plusDays(4).getDayOfMonth() + "." + week.plusDays(4).getMonthValue() + "."
				+ week.plusDays(4).getYear());
		
		return "ordertable";
	}


	/******* PREVIOUS NEXT *********/
	
	@GetMapping("/previous")
	String getPrevious(){
		weekOffset -= 1;
		return "redirect:/ordertable";
	}
	
	@GetMapping("/next")
	String getNext(){
		weekOffset += 1;
		return "redirect:/ordertable";
	}

	@GetMapping("/today")
	String resetOffset(){
		weekOffset = 0;
		return "redirect:/ordertable";
	}
	
	/*****  DELETE ORDERS  *****/
	@GetMapping("/deleteOrder/{orderId}")
	String deleteOrder(@PathVariable Long orderId){
		
		for( TimeSlot t : timeSlotManagement.findAll()) {
			Orders order = ordersManagement.getOrderById(orderId).get();
			if(t.getTimeOrders().equals(order)) {
				timeSlotManagement.deleteTimeSlot(t);
			}
		}
		ordersManagement.deleteOrder(orderId);
		return "redirect:/ordertable";
	}
	
	
	
	/*****  UPDATE ORDERS  *****/
	
	@GetMapping("/updateOrder/{id}")
	String updateOrder(@PathVariable Long id , Model model , Authentication authentication){
		
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}
		
		this.orderId = id ;
		model.addAttribute("order",ordersManagement.getOrderById(id).get());
		return "updateOrder";
	}
	
	@PostMapping("/updateOrder") 
	String updateOrderForm(OrderCreationFrom form, Model model, Authentication authentication){

		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		Orders orderNew = ordersManagement.getOrderById(orderId).get();
		
		
		orderNew.setCustomerContact(form.getCustomerContact());
		orderNew.setCustomerName(form.getCustumerName());
		orderNew.setDescription(form.getDescription());
		
		
		ordersManagement.updateOrder(orderNew);
		
		
		return "redirect:/ordertable";
	}

	
	@GetMapping("/acceptStatus/{id}")
	String acceptStatusForm(@PathVariable Long id , Authentication authentication){
		ordersManagement.getOrderById(id).get().setStatus(Status.ACCEPTED);
		ordersManagement.getOrderById(id).get().setIsAcceptedBy(authentication.getName());
		
		System.err.println(ordersManagement.getOrderById(id).get().getIsAcceptedBy());
		return "redirect:/ordertable";
	}
	
	@GetMapping("/declineStatus/{id}")
	String declineStatusForm(@PathVariable Long id){
		
		ordersManagement.getOrderById(id).get().setStatus(Status.DECLINED);
		//timeSlotManagement.getFreeTimeSlot(ordersManagement.getOrderById(id).get());
		ordersManagement.finishOrder(ordersManagement.getOrderById(id).get());
		
		return "redirect:/ordertable";
	}

	@GetMapping("/pickedupStatus/{id}")
	String pickedupStatusForm(@PathVariable Long id){

		ordersManagement.getOrderById(id).get().setStatus(Status.PICKED_UP);
		ordersManagement.finishOrder(ordersManagement.getOrderById(id).get());
		return "redirect:/ordertable";
	}
	
	
	
	
	/***** CREATE (SERVICE) ORDERS *****/
	
	@GetMapping("/ordercreation/{skillId}")
	String showOrderCreationPage(Model model , @PathVariable int skillId , Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name", authentication.getName());
			model.addAttribute("authorities", authentication.getAuthorities().toString());
		}
		
		selectedSkill = skillId ;
		model.addAttribute("subcategories" , toShowCategory(skillId));
		return "ordercreation";
	}
	
	
	@PostMapping("/ordercreation")
	String getCreationForm(OrderCreationFrom form){
		if(ordersManagement.updateMaterialAmount(form.getSubcategory())) {
			form.setSkill(recognizeSkill(selectedSkill));
			Orders order = ordersManagement.addOrder(form);
			timeSlotManagement.getFreeTimeSlot(order);
			materialController.setError(false);
			
			return "redirect:/ordertable";
		}
		
		materialController.setError(true);
		return "redirect:/materials" ; 
	}


	
	/*****  FUNKTIONS  *****/

	private Skill recognizeSkill (int skillId) {
		return Skill.values()[skillId];
	}
	
	public Set<SubCategory> toShowCategory(int skillId){
		Set<SubCategory> toShowList = new HashSet<>();
		Skill skill = recognizeSkill(skillId);
		
		switch (skill) {
		case PATCH:
			toShowList.add(SubCategory.ABSAETZE);
			toShowList.add(SubCategory.SOHLEN);
			toShowList.add(SubCategory.NAEHTE);
			break;
			
		case SEW:
			toShowList.add(SubCategory.FLICKEN);
			toShowList.add(SubCategory.KNOEPFE);
			toShowList.add(SubCategory.NAEHTE);
			break;
			
		case KEY:
			toShowList.add(SubCategory.SCHLUESSEL_KOPIEREN);
			toShowList.add(SubCategory.SCHILDER_GRAVIEREN);
			break;
			
		case CLEAN:
			toShowList.add(SubCategory.WAESCHE);
			toShowList.add(SubCategory.ANZUEGE);
			toShowList.add(SubCategory.LEDER);
			break;
			
		case GRIND:
			toShowList.add(SubCategory.SCHEREN);
			toShowList.add(SubCategory.MESSER_SCHAERFEN);
			break;
			
		default:
			toShowList.add(SubCategory.KABEL_ERSETZEN);
			toShowList.add(SubCategory.LOETEN);
			break;
		}
		
		return toShowList ;
		
	}
	
	
	
	private List<Orders> organizeList(List<Orders> hours) {
		
		List<Orders> newList = new ArrayList<>();

		newList.add( new Orders (null, "", null, null, "nullcustomer" , ""));
		newList.add( new Orders (null, "", null, null, "nullcustomer" , ""));
		newList.add( new Orders (null, "", null, null, "nullcustomer" , ""));
		newList.add( new Orders (null, "", null, null, "nullcustomer" , ""));
		newList.add( new Orders (null, "", null, null, "nullcustomer" , ""));

		int startOfWeek = timeSlotManagement.getTime().toLocalDate().getDayOfWeek().getValue() - 1;
		LocalDate week = timeSlotManagement.getTime().toLocalDate().minusDays(startOfWeek);

		for (int i = 0 ; i < hours.size() ; i++) {
			if(hours.get(i).getFinishDateIntern().isAfter(week.minusDays(1).plusWeeks(weekOffset))
					&& hours.get(i).getFinishDateIntern().isBefore(week.plusDays(7).plusWeeks(weekOffset))) {
				if (hours.get(i).getDay() == 1) {
					newList.set(0, hours.get(i));
				} else if (hours.get(i).getDay() == 2) {
					newList.set(1, hours.get(i));
				} else if (hours.get(i).getDay() == 3) {
					newList.set(2, hours.get(i));
				} else if (hours.get(i).getDay() == 4) {
					newList.set(3, hours.get(i));
				} else {
					newList.set(4, hours.get(i));
				}
			}
		}

		return newList ;
	}
	
	
	private List<Orders> toShowList(List<Orders> newList) {
		List<Orders> toShow = new ArrayList<>();
		
		for(Orders o : newList) {
			if(authenticatedStaff.getSkills().contains(o.getSkill())) {
				toShow.add(o);
			}else {
				toShow.add(new Orders (null, "", null, null, "nullcustomer" , ""));
			}
		}
		return toShow ;
	}

}
