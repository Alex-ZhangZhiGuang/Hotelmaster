package com.hotelmaster.home.web.json;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hotelmaster.po.ReservOrder;

public class ReservOrderJson {
	private int totalCount;
	private List<ReservOrder> reservOrderList;
	private ReservOrder reservOrder;
	public ReservOrderJson(){
	}
	public ReservOrderJson(List<ReservOrder> reservOrderList,int totalCount){
		this.reservOrderList=reservOrderList;
		this.totalCount=totalCount;
	}
	public ReservOrderJson(ReservOrder reservOrder){
		this.reservOrder=reservOrder;
	}
	public JSONObject getJSONObject() throws Exception{
		JSONObject jsonBack=new JSONObject();
		jsonBack.put("totalCount", totalCount);
		JSONArray jsonItems=new JSONArray();
		for(ReservOrder item:reservOrderList){
			JSONObject itemJson=new JSONObject();
			String roCreateTime=String.valueOf(item.getRoCreateTime());
			itemJson.put("roCreateTime",roCreateTime);
			String roEarliestTime=String.valueOf(item.getRoEarliestTime());
			itemJson.put("roEarliestTime",roEarliestTime);
			itemJson.put("roEmail",item.getRoEmail());
			itemJson.put("roFax",item.getRoFax());
			//itemJson.put("roGroupName",item.getRoGroupName());
			itemJson.put("roGuestCardCatalog",item.getRoGuestCardCatalog());
			itemJson.put("roGuestCardId",item.getRoGuestCardId());
			itemJson.put("roGuestGender",item.getRoGuestGender());
			itemJson.put("roGuestName",item.getRoGuestName());
			
			String roInDateTime=String.valueOf(
					new SimpleDateFormat("yyyy-MM-dd").format(item.getRoInDateTime())
					);
			itemJson.put("roInDate",roInDateTime);
			String roLatestTime=String.valueOf(item.getRoLatestTime());
			itemJson.put("roLatestTime",roLatestTime);
			itemJson.put("roOperator",item.getRoOperator());
			itemJson.put("roPaidMoney",item.getRoPaidMoney());
			itemJson.put("roPaymentModel",item.getRoPaymentModel());
			itemJson.put("roId",item.getRoId());
			//itemJson.put("",item.getRoPreAssignRoom());
			String roPreOutDateTime=String.valueOf(
					new SimpleDateFormat("yyyy-MM-dd").format(item.getRoPreOutDateTime())
					);
			itemJson.put("roOutDate",roPreOutDateTime);
			itemJson.put("roRemark",item.getRoRemark());
			itemJson.put("roReservModel",item.getRoReservModel());
			itemJson.put("roReservState",item.getRoReservState());
			itemJson.put("roTelphone",item.getRoTelphone());
			itemJson.put("roTotalRate",item.getRoTotalRate());
			itemJson.put("roOrderId",item.getRoOrderId());
			jsonItems.add(itemJson);
		}
		jsonBack.put("reservOrders", jsonItems);
		return jsonBack;
	}
	public JSONObject getInfoJSONObject() throws Exception{
		JSONObject jsonBack=new JSONObject();
		JSONObject itemJson=new JSONObject();
		String roCreateTime=String.valueOf(reservOrder.getRoCreateTime());
		itemJson.put("i_roCreateTime",roCreateTime);
		String roEarliestTime=String.valueOf(reservOrder.getRoEarliestTime());
		itemJson.put("i_roEarliestTime",roEarliestTime);
		itemJson.put("i_roEmail",reservOrder.getRoEmail());
		itemJson.put("i_roFax",reservOrder.getRoFax());
		//itemJson.put("roGroupName",item.getRoGroupName());
		itemJson.put("i_roGuestCardCatalog",reservOrder.getRoGuestCardCatalog());
		itemJson.put("i_roGuestCardId",reservOrder.getRoGuestCardId());
		itemJson.put("i_roGuestGender",reservOrder.getRoGuestGender());
		itemJson.put("i_roGuestName",reservOrder.getRoGuestName());
			
		String roInDateTime=String.valueOf(
			new SimpleDateFormat("yyyy-MM-dd").format(reservOrder.getRoInDateTime())
		);
		itemJson.put("i_roInDate",roInDateTime);
		String roLatestTime=String.valueOf(reservOrder.getRoLatestTime());
		itemJson.put("i_roLatestTime",roLatestTime);
		itemJson.put("i_roOperator",reservOrder.getRoOperator());
		itemJson.put("i_roPaidMoney",reservOrder.getRoPaidMoney());
		itemJson.put("i_roPaymentModel",reservOrder.getRoPaymentModel());
		itemJson.put("i_roId",reservOrder.getRoId());
		//itemJson.put("",item.getRoPreAssignRoom());
		String roPreOutDateTime=String.valueOf(
				new SimpleDateFormat("yyyy-MM-dd").format(reservOrder.getRoPreOutDateTime())
		);
		itemJson.put("i_roOutDate",roPreOutDateTime);
		itemJson.put("i_roRemark",reservOrder.getRoRemark());
		itemJson.put("i_roReservModel",reservOrder.getRoReservModel());
		itemJson.put("i_roReservState",reservOrder.getRoReservState());
		itemJson.put("i_roTelphone",reservOrder.getRoTelphone());
		itemJson.put("i_roTotalRate",reservOrder.getRoTotalRate());
		itemJson.put("i_roOrderId",reservOrder.getRoOrderId());
		jsonBack.put("data", itemJson);
		return jsonBack;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<ReservOrder> getReservOrderList() {
		return reservOrderList;
	}
	public void setReservOrderList(List<ReservOrder> reservOrderList) {
		this.reservOrderList = reservOrderList;
	}
	
}
