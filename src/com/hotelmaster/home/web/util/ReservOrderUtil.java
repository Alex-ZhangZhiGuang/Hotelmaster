package com.hotelmaster.home.web.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.hotelmaster.po.ReservOrder;

public class ReservOrderUtil{
	private String roId;				//uupkid
	private String roGroupName;			//团队名称(协议单位)
	private String roPreAssignRoom;		//预分房(备用)
	private Timestamp roCreateTime;		//创建时间

	public static ReservOrder createReservOrder(HttpServletRequest request)
		throws Exception{
		ReservOrder reservOrder=new ReservOrder();
		if(request.getParameter("roGuestName")!=null){	//客人姓名
			String roGuestName=(String) request.getParameter("roGuestName").trim();
			reservOrder.setRoGuestName(roGuestName);
		}
		if(request.getParameter("roTelphone")!=null){	//客人电话
			String roTelphone=(String) request.getParameter("roTelphone").trim();
			reservOrder.setRoTelphone(roTelphone);
		}
		if(request.getParameter("roEmail")!=null){	//客人Email
			String roEmail=(String) request.getParameter("roEmail").trim();
			reservOrder.setRoEmail(roEmail);
		}
		if(request.getParameter("roFax")!=null){	//客人Fox
			String roFax=(String) request.getParameter("roFax").trim();
			reservOrder.setRoFax(roFax);
		}
		if(request.getParameter("roInDate")!=null){	//预计住店日期
			String roInDateTime=(String) request.getParameter("roInDate").trim();
			/*String temp[]=roInDateTime.split("/");
			if(temp.length>0){
				roInDateTime=temp[2]+"-"+temp[0]+"-"+temp[1];
			}*/
			//roInDateTime.replace("/", "-");
			roInDateTime=roInDateTime+" 12:00:00";
			reservOrder.setRoInDateTime(Timestamp.valueOf(roInDateTime));
		}
		if(request.getParameter("roOutDate")!=null){	//预计离店日期 设置为12:00:00
			String roPreOutDateTime=(String) request.getParameter("roOutDate").trim();
			/*String temp[]=roPreOutDateTime.split("/");
			if(temp.length>0){
				roPreOutDateTime=temp[2]+"-"+temp[0]+"-"+temp[1];
			}*/
			//roPreOutDateTime.replace("/", "-");
			roPreOutDateTime=roPreOutDateTime+" 12:00:00";
			reservOrder.setRoPreOutDateTime(Timestamp.valueOf(roPreOutDateTime));
		}
		if(request.getParameter("roEarliestTime")!=null){	//最早到达时间
			String roEarliestTime=(String) request.getParameter("roEarliestTime").trim();
			if(roEarliestTime.equals("")){
				reservOrder.setRoEarliestTime(Time.valueOf("12:00:00"));
			}else{
				reservOrder.setRoEarliestTime(Time.valueOf(roEarliestTime));
			}
		}
		if(request.getParameter("roLatestTime")!=null){	//最晚到达时间
			String roLatestTime=(String) request.getParameter("roLatestTime").trim();
			if(roLatestTime.equals(""))
				reservOrder.setRoLatestTime(Time.valueOf("12:00:00"));
			else
				reservOrder.setRoLatestTime(Time.valueOf(roLatestTime));
		}
		if(request.getParameter("roPaymentModel")!=null){//支付方式
			String roPaymentModel=(String) request.getParameter("roPaymentModel").trim();
			reservOrder.setRoPaymentModel(roPaymentModel);
		}
		if(request.getParameter("roPaidMoney")!=null){//已付押金
			String roPaidMoney=(String) request.getParameter("roPaidMoney").trim();
			reservOrder.setRoPaidMoney(new BigDecimal(roPaidMoney));
		}	
		if(request.getParameter("roGroupName")!=null){//团队名称
			String roGroupName=(String) request.getParameter("roGroupName").trim();
			reservOrder.setRoGroupName(roGroupName);
		}else{
			reservOrder.setRoGroupName("");
		}
		if(request.getParameter("roPreAssignRoom")!=null){//预分房 无用*
			String roPreAssignRoom=(String) request.getParameter("roPreAssignRoom").trim();
			reservOrder.setRoPreAssignRoom(roPreAssignRoom);
		}else{
			reservOrder.setRoPreAssignRoom("");
		}
		if(request.getParameter("roPaidMoney")!=null){//已付押金
			String roPaidMoney=(String) request.getParameter("roPaidMoney").trim();
			reservOrder.setRoPaidMoney(new BigDecimal(roPaidMoney));
		}	
		if(request.getParameter("roRemark")!=null){//备注
			String roRemark=(String) request.getParameter("roRemark").trim();
			reservOrder.setRoRemark(roRemark);
		}
		if(request.getParameter("roReservModel")!=null){//预定方式
			String roReservModel=(String) request.getParameter("roReservModel").trim();
			reservOrder.setRoReservModel(roReservModel);
		}
		if(request.getParameter("roReservState")!=null){//预定状态
			String roReservState=(String) request.getParameter("roReservState").trim();
			reservOrder.setRoReservState(roReservState);
		}
		if(request.getParameter("roGuestGender")!=null){//客人性别
			String roGuestGender=(String) request.getParameter("roGuestGender").trim();
			reservOrder.setRoGuestGender(roGuestGender);
		}	
		if(request.getParameter("roGuestCardCatalog")!=null){//客人证件类型
			String roGuestCardCatalog=(String) request.getParameter("roGuestCardCatalog").trim();
			reservOrder.setRoGuestCardCatalog(roGuestCardCatalog);
		}
		if(request.getParameter("roGuestCardId")!=null){//客人证件号码
			String roGuestCardId=(String) request.getParameter("roGuestCardId").trim();
			reservOrder.setRoGuestCardId(roGuestCardId);
		}
		if(request.getParameter("roTotalRate")!=null){//总费用
			String roTotalRate=(String) request.getParameter("roTotalRate").trim();
			reservOrder.setRoTotalRate(new BigDecimal(roTotalRate));
		}	
		if(request.getParameter("roOperator")!=null){//操作员session取用户
			String roOperator=(String) request.getParameter("roOperator").trim();
			reservOrder.setRoOperator(roOperator);
		}else{
			reservOrder.setRoOperator("Admin");
		}
		if(request.getParameter("roOrderId")!=null){//操作员session取用户
			String roOrderId=(String) request.getParameter("roOrderId").trim();
			reservOrder.setRoOrderId(roOrderId);
		}else{
			reservOrder.setRoOrderId("RO00000001");
		}
		if(request.getParameter("roCreateTime")!=null){////预定单创建时间
			String roCreateTime=(String) request.getParameter("roCreateTime").trim();
			reservOrder.setRoCreateTime(Timestamp.valueOf(roCreateTime));
		}else{
			Calendar tCal = Calendar.getInstance();
			reservOrder.setRoCreateTime(new Timestamp(tCal.getTime().getTime()));
		}
		if(request.getParameter("roId")!=null){//预定单PKID 修改预定时获得
			String roId=(String) request.getParameter("roId").trim();
			reservOrder.setRoId(roId);
		}
		return reservOrder;
	}
	//22
	public static ReservOrder createReservFromJson(JSONObject jsonObject)
		throws Exception{
		ReservOrder reservOrder=new ReservOrder();
		reservOrder.setRoCreateTime(Timestamp.valueOf(jsonObject.getString("roCreateTime")));
		reservOrder.setRoEarliestTime(Time.valueOf(jsonObject.getString("roEarliestTime")));
		reservOrder.setRoEmail(jsonObject.getString("roEmail"));
		reservOrder.setRoFax(jsonObject.getString("roFax"));
		//reservOrder.setRoGroupName(roGroupName);
		reservOrder.setRoGuestCardCatalog(jsonObject.getString("roGuestCardCatalog"));
		reservOrder.setRoGuestCardId(jsonObject.getString("roGuestCardId"));
		reservOrder.setRoGuestGender(jsonObject.getString("roGuestGender"));
		reservOrder.setRoGuestName(jsonObject.getString("roGuestName"));
		reservOrder.setRoId(jsonObject.getString("roId"));
		reservOrder.setRoInDateTime(Timestamp.valueOf(jsonObject.getString("roInDateTime")));
		reservOrder.setRoLatestTime(Time.valueOf(jsonObject.getString("roLatestTime")));
		
		reservOrder.setRoOperator(jsonObject.getString("roOperator"));
		reservOrder.setRoOrderId(jsonObject.getString("roOrderId"));
		
		reservOrder.setRoPaidMoney(new BigDecimal(jsonObject.getString("roPaidMoney")));
		reservOrder.setRoPaymentModel(jsonObject.getString("roPaymentModel"));
		//reservOrder.setRoPreAssignRoom(jsonObject.getString("roPreAssignRoom"));
		reservOrder.setRoPreOutDateTime(Timestamp.valueOf(jsonObject.getString("roPreOutDateTime")));
		reservOrder.setRoRemark(jsonObject.getString("roRemark"));
		reservOrder.setRoReservModel(jsonObject.getString("roReservModel"));
		reservOrder.setRoReservState(jsonObject.getString("roReservState"));
		reservOrder.setRoTelphone(jsonObject.getString("roTelphone"));
		reservOrder.setRoTotalRate(new BigDecimal(jsonObject.getString("roTotalRate")));
		
		return reservOrder;
	}
	//22
	public static ReservOrder updateReservOrder(ReservOrder oldOrder,ReservOrder newOrder){
		if(!oldOrder.getRoCreateTime().equals(newOrder.getRoCreateTime())){
			oldOrder.setRoCreateTime(newOrder.getRoCreateTime());
		}
		
		if(!oldOrder.getRoEarliestTime().equals(newOrder.getRoEarliestTime())){
			oldOrder.setRoEarliestTime(newOrder.getRoEarliestTime());
		}
		
		if(!oldOrder.getRoEmail().equals(newOrder.getRoEmail())){
			oldOrder.setRoEmail(newOrder.getRoEmail());
		}
		
		if(!oldOrder.getRoFax().equals(newOrder.getRoFax())){
			oldOrder.setRoFax(newOrder.getRoFax());
		}
		
		//GroupName
		if(!oldOrder.getRoGroupName().equals(newOrder.getRoGroupName())){
			oldOrder.setRoGroupName(newOrder.getRoGroupName());
		}
		
		if(!oldOrder.getRoGuestCardCatalog().equals(newOrder.getRoGuestCardCatalog())){
			oldOrder.setRoGuestCardId(newOrder.getRoGuestCardCatalog());
		}
		if(!oldOrder.getRoGuestCardId().equals(newOrder.getRoGuestCardId())){
			oldOrder.setRoGuestCardId(newOrder.getRoGuestCardId());
		}
		if(!oldOrder.getRoGuestGender().equals(newOrder.getRoGuestGender())){
			oldOrder.setRoGuestGender(newOrder.getRoGuestGender());
		}
		if(!oldOrder.getRoGuestName().equals(newOrder.getRoGuestName())){
			oldOrder.setRoGuestName(newOrder.getRoGuestName());
		}
		//
		if(!oldOrder.getRoId().equals(newOrder.getRoId())){
			oldOrder.setRoId(newOrder.getRoId());
		}
		if(!oldOrder.getRoInDateTime().equals(newOrder.getRoInDateTime())){
			oldOrder.setRoInDateTime(newOrder.getRoInDateTime());
		}
		if(!oldOrder.getRoLatestTime().equals(newOrder.getRoLatestTime())){
			oldOrder.setRoLatestTime(newOrder.getRoLatestTime());
		}
		//
		if(!oldOrder.getRoOperator().equals(newOrder.getRoOperator())){
			oldOrder.setRoOperator(newOrder.getRoOperator());
		}
		if(!oldOrder.getRoOrderId().equals(newOrder.getRoOrderId())){
			oldOrder.setRoOrderId(newOrder.getRoOrderId());
		}
		if(!oldOrder.getRoPaidMoney().equals(newOrder.getRoPaidMoney())){
			oldOrder.setRoPaidMoney(newOrder.getRoPaidMoney());
		}
		if(!oldOrder.getRoPaymentModel().equals(newOrder.getRoPaymentModel())){
			oldOrder.setRoPaymentModel(newOrder.getRoPaymentModel());
		}
		//
		if(!oldOrder.getRoPreAssignRoom().equals(newOrder.getRoPreAssignRoom())){
			oldOrder.setRoPreAssignRoom(newOrder.getRoPreAssignRoom());
		}
		if(!oldOrder.getRoPreOutDateTime().equals(newOrder.getRoPreOutDateTime())){
			oldOrder.setRoPreOutDateTime(newOrder.getRoPreOutDateTime());
		}
		if(!oldOrder.getRoRemark().equals(newOrder.getRoRemark())){
			oldOrder.setRoRemark(newOrder.getRoRemark());
		}
		if(!oldOrder.getRoReservModel().equals(newOrder.getRoReservModel())){
			oldOrder.setRoReservModel(newOrder.getRoReservModel());
		}
		if(!oldOrder.getRoReservState().equals(newOrder.getRoReservState())){
			oldOrder.setRoReservState(newOrder.getRoReservState());
		}
		if(!oldOrder.getRoTelphone().equals(newOrder.getRoTelphone())){
			oldOrder.setRoTelphone(newOrder.getRoTelphone());
		}
		if(!oldOrder.getRoTotalRate().equals(newOrder.getRoTotalRate())){
			oldOrder.setRoTotalRate(newOrder.getRoTotalRate());
		}
		return oldOrder;
	}
}
