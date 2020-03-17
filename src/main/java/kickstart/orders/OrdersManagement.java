package kickstart.orders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.salespointframework.time.BusinessTime;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import kickstart.Accountancy.AccountancyManagement;
import kickstart.material.MaterialManagement;

@Service
@Transactional
public class OrdersManagement {

	private final OrdersRepository orderPlan;
	private final AccountancyManagement accountancyManagement;
	private final BusinessTime businessTime;
	private final MaterialManagement materialManagement;

	public OrdersManagement(OrdersRepository orderPlan, AccountancyManagement accountancyManagement,
							BusinessTime businessTime , MaterialManagement materialManagement) {
		Assert.notNull(orderPlan, "orderPlan must not be null!");
		Assert.notNull(accountancyManagement, "AccountancyManagement must not be null!");
		
		this.orderPlan = orderPlan;
		this.accountancyManagement = accountancyManagement;
		this.businessTime = businessTime;
		this.materialManagement = materialManagement;
		
	}

	public Orders addOrder(OrderCreationFrom orderCreationForm) {
		
		Orders temporder = new Orders(setDate(), orderCreationForm.getDescription(),
				orderCreationForm.getSkill() , orderCreationForm.getSubcategory() ,
				orderCreationForm.getCustumerName() , orderCreationForm.getCustomerContact());
		
		temporder = orderPlan.save(temporder);
		accountancyManagement.addBill(temporder);
		return temporder;
	}

	public void updateOrder(OrderCreationFrom orderCreationForm , Long id) {
		
		Orders temporder = new Orders(setDate(), orderCreationForm.getDescription(),
				orderCreationForm.getSkill() , orderCreationForm.getSubcategory(),
				orderCreationForm.getCustumerName() ,
				orderCreationForm.getCustomerContact());
			
		temporder.updateTimeSlot(getOrderById(id).get().getHour(), getOrderById(id).get().getDay() ,
				getOrderById(id).get().getRoom());
		deleteOrder(id);
		temporder.setOrder_id(id);
			
		orderPlan.save(temporder);
	}

	
	public void updateOrder(Orders order) {
		orderPlan.save(order);
	}
	
	public void changeStatus(Long id, Status status) {
		Optional<Orders> order = getOrderById(id);
		if(order.isPresent()) {
			order.get().setStatus(status);
			orderPlan.save(order.get());
		}
	}

	public void finishOrder(Orders order) {
		order.setPickUpDate(setDate());
		orderPlan.save(order);
		accountancyManagement.addBill(order);
	}
	
	public Streamable<Orders> findAll() {
		return orderPlan.findAll();
	}

	public Optional<Orders> getOrderById(Long id) {
		return orderPlan.findById(id);
	}
	

	public void deleteOrder(Long id) {
		orderPlan.deleteById(id);
		
	}

	public LocalDate setDate() {
		return businessTime.getTime().toLocalDate();
	}

	
	public void setFinishDate(Orders order, LocalDate date) {
		if(orderPlan.findAll().toList().contains(order)) {
			if (order.getFinishDateCustomer() == null) {
				order.setFinishDateCustomer(date);
				order.setFinishDateIntern(date);
			} else {
				order.setFinishDateIntern(date);
			}
		}
	}
	
	public List<Orders> findByHoursRoom1(int hour){
		List<Orders> orders = new ArrayList<>();
			for(Orders o : this.findAll()) {
				if(o.getHour() == hour && o.getRoom() == 1) {
					orders.add(o);
				}
			}
		return orders ;
	}
	
	public List<Orders> findByHoursRoom2(int hour){
		List<Orders> orders = new ArrayList<>();
			for(Orders o : this.findAll()) {
				if(o.getHour() == hour && o.getRoom() == 2) {
					orders.add(o);
				}
			}
		
		return orders ;
	}
	
	
    public boolean updateMaterialAmount(SubCategory sub) {
		
		return materialManagement.updateMaterialAmount(sub) ;
	}
	
}
