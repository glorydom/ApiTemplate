package com.dto.huiyi.meeting.entity.participantDto;

public class ParticipantStatisticsDto {

	private String interestedProduct;
	private int total;
	private int charged;
	private int invoiced;
	private int hoteled;
	
	public int getUncharged() {
		return total-charged;
	}
	public int getUninvoiced() {
		return total-invoiced;
	}
	public int getUnhoteled() {
		return total-hoteled;
	}
	
	
	public String getInterestedProduct() {
		return interestedProduct;
	}
	public void setInterestedProduct(String interestedProduct) {
		this.interestedProduct = interestedProduct;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCharged() {
		return charged;
	}
	public void setCharged(int charged) {
		this.charged = charged;
	}
	public int getInvoiced() {
		return invoiced;
	}
	public void setInvoiced(int invoiced) {
		this.invoiced = invoiced;
	}
	public int getHoteled() {
		return hoteled;
	}
	public void setHoteled(int hoteled) {
		this.hoteled = hoteled;
	}
	
}
