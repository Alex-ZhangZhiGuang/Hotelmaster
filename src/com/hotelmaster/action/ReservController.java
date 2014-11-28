package com.hotelmaster.action;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.hotelmaster.home.web.json.CheckinOrderJson;
import com.hotelmaster.home.web.json.ReservOrderJson;
import com.hotelmaster.home.web.util.ReservOrderUtil;
import com.hotelmaster.po.BaseInfo;
import com.hotelmaster.po.CheckinOrder;
import com.hotelmaster.po.Page;
import com.hotelmaster.po.ReservItem;
import com.hotelmaster.po.ReservOrder;
import com.hotelmaster.po.Room;
import com.hotelmaster.service.BusinessService;

public class ReservController extends MultiActionController{
	private final static Logger log=Logger.getLogger(ReservController.class);
	private BusinessService businessService;
	
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Someone come from ip address <"
				+ request.getRemoteAddr() + ">");
		System.out.println("reach ReservController");
		return null;
	}
	/**
	 * 
	 * 查找可供预定的房间
	 *
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public ModelAndView findAvailRooms(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Someone come from ip address <"
				+ request.getRemoteAddr() + ">");
		System.out.println("reach ReservController findAvailRooms");
		String rmCatalog="",pageStart="0",pageLimit="10";
		Timestamp toDate=null,fromDate=null;
		if(request.getParameter("from")!=null){
			String fromDateString=(String) request.getParameter("from").trim();
			fromDateString=fromDateString+" 12:00:00";
			fromDate=Timestamp.valueOf(fromDateString);
		}
		if(request.getParameter("to")!=null){
			String toDateString=(String) request.getParameter("to").trim();
			toDateString=toDateString+" 12:00:00";
			toDate=Timestamp.valueOf(toDateString);
		}
		if(request.getParameter("catalog")!=null){
			rmCatalog=(String) request.getParameter("catalog").trim();
		}
		if(request.getParameter("start")!=null){ //pageindex
			pageStart=(String) request.getParameter("start").trim();
		}
		if(request.getParameter("limit")!=null){ //pagesize
			pageLimit=(String) request.getParameter("limit").trim();
		}
		JSONArray jsonItems=new JSONArray();
		//有Bug For input string: ""
		Page page=new Page(Integer.parseInt(pageStart),Integer.parseInt(pageLimit));
		List<Room> roomList=businessService.findAvailReservRooms(fromDate,toDate
					,rmCatalog,page);//出错处理
		Iterator i=roomList.iterator();
		Room room=new Room();
		while(i.hasNext()){
			room=(Room)i.next();
			jsonItems.add(JSONObject.fromObject(room));
		}
		JSONObject jsonBack=new JSONObject();
		jsonBack.put("rooms", jsonItems);
		System.out.println(jsonBack);
		request.setCharacterEncoding("utf-8");   
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsonBack);
		
		return null;
	}
	/**
	 * 
	 * 创建预定信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView createReserv(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Someone come from ip address <"
				+ request.getRemoteAddr() + ">");
		System.out.println("reach ReservController createUpdateReserv()");
		
		ReservOrder reservOrder=ReservOrderUtil.createReservOrder(request);
		String rooms=(String) request.getParameter("roomDataes").trim();
		JSONArray jsonArray=JSONArray.fromObject(rooms);//[{},{}]	
		Iterator iterator=jsonArray.iterator();
		JSONObject jsonObject=new JSONObject();
		List<ReservItem> reservItemList=new ArrayList();
		while(iterator.hasNext()){
			jsonObject=JSONObject.fromObject(iterator.next());//{}
			ReservItem reservItem=new ReservItem();
			String roomId=jsonObject.getString("rmId");
			System.out.println(roomId);
			Room room=businessService.findRoomById(roomId); //异常处理
			//room.setRmState(1);		//更改房态
			//businessService.updateRoom(room);
			reservItem.setRoom(room);
			reservItemList.add(reservItem);
		}
		reservOrder.setRoOrderId(createReservOrderId());
		//System.out.println(jsonArray.getString(0));// -{}
		if(businessService.createReservOrder(reservOrder, reservItemList)){
			log.info("预定信息创建成功");
			request.setCharacterEncoding("UTF-8");   
            response.setContentType("text/json;charset=utf-8"); 
            response.getWriter().write("{success: true}");
		}else
		{
			response.getWriter().write("{errors:'预定信息添加失败，请重新尝试!'}");
		}
		return null;
	}
	/**
	 * 
	 * 更新预定信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateReserv(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Someone come from ip address <"
				+ request.getRemoteAddr() + ">");
		System.out.println("reach ReservController updateReserv()");
		
		ReservOrder newReservOrder=ReservOrderUtil.createReservOrder(request);
		String rooms=(String) request.getParameter("roomDataes").trim();
		JSONArray jsonArray=JSONArray.fromObject(rooms);//[{},{}]	
		Iterator iterator=jsonArray.iterator();
		JSONObject jsonObject=new JSONObject();
		List<ReservItem> reservItemList=new ArrayList();
		while(iterator.hasNext()){
			jsonObject=JSONObject.fromObject(iterator.next());//{}
			ReservItem reservItem=new ReservItem();
			String roomId=jsonObject.getString("rmId");
			System.out.println(roomId);
			Room room=businessService.findRoomById(roomId); //异常处理
			//room.setRmState(1);		//更改房态
			//businessService.updateRoom(room);
			reservItem.setRoom(room);
			reservItemList.add(reservItem);
		}
		/*if(businessService.updateReservOrder(reservOrder, reservItemList)){
			log.info("预定信息更新成功");
			request.setCharacterEncoding("UTF-8");   
            response.setContentType("text/json;charset=utf-8"); 
            response.getWriter().write("{success: true}");
		}else
		{
			response.getWriter().write("{errors:'预定信息更新失败，请重新尝试!'}");
		}*/
		//businessService.deleteReservOrderItem(reservOrder.getRoId());
		//异常处理
		ReservOrder reservOrder=businessService.findReservById(newReservOrder.getRoId());
		reservOrder=ReservOrderUtil.updateReservOrder(reservOrder, newReservOrder);
		businessService.updateReservOrder(reservOrder, reservItemList);
		log.info("预定信息更新成功");
		request.setCharacterEncoding("UTF-8");   
        response.setContentType("text/json;charset=utf-8"); 
        response.getWriter().write("{success: true}");
		return null;
	}
	/**
	 * 
	 * 查找当日后可用预定 ->过期预定查询
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView findAvailReservOrders(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pageStart="",pageLimit="";
		Page page;
		if(request.getParameter("start")!=null){ //pageindex
			pageStart=(String) request.getParameter("start").trim();
		}
		if(request.getParameter("limit")!=null){ //pagesize
			pageLimit=(String) request.getParameter("limit").trim();
		}
		if(!pageStart.equals("")&&!pageLimit.equals("")){
			page=new Page(Integer.parseInt(pageStart),Integer.parseInt(pageLimit));
		}else{
			page=new Page(0);
		}
		List<ReservOrder> reservOrderList=businessService.findAvailReservOrders(page);
		/*JSONArray jsonItems=new JSONArray();
		for(ReservOrder item :reservOrderList){
			item.getRoEarliestTime();
			item.getRoLatestTime();
			//jsonItems.add(JSONObject.fromObject(item));
		}
		JSONObject jsonBack=new JSONObject();
		jsonBack.put("reservOrders", jsonItems);*/
		ReservOrderJson reservOrderJson=new ReservOrderJson(reservOrderList,page.getTotalCount());
		JSONObject jsonBack=reservOrderJson.getJSONObject();
		
		System.out.println(jsonBack);
		request.setCharacterEncoding("utf-8");   
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsonBack);
		return null;
	}
	/**
	 * 
	 * 获取某房间的预定信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getReservInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(request.getParameter("rmId")!=null){
			String rmId=(String)request.getParameter("rmId").trim();
			
			ReservOrder reservOrder=businessService.findReservOrderByRmId(rmId);
			
			ReservOrderJson reservOrderJson=new ReservOrderJson(reservOrder);
			JSONObject jsonBack=reservOrderJson.getInfoJSONObject();
			jsonBack.put("success", true);
			System.out.println(jsonBack);
			request.setCharacterEncoding("utf-8");   
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonBack);
		}else{
			log.error("getCheckinInfo failed,rmId==null");
		}
		return null;
	}
	/**
	 * 
	 * 删除预定信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteReserv(HttpServletRequest request,	//单条记录删除 
			HttpServletResponse response) throws Exception {
		String roId="";
		if(request.getParameter("roId")!=null){
			roId=(String)request.getParameter("roId").trim();
		}else{
			//get reservData failed
		}
		ReservOrder reservOrder=businessService.findReservById(roId);
		//待更改
		List<ReservItem> reservItemList=businessService.findReservItemByOrder(reservOrder);
		businessService.deleteReservOrder(reservOrder,reservItemList);	//异常处理
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=utf-8"); 
        response.getWriter().write("{success:true}");
        return null;
	}
	/**
	 * 
	 * 获得预定单对应的房间信息
	 *
	 * @return
	 */
	public ModelAndView findReservRooms(HttpServletRequest request,	//单条记录删除 
			HttpServletResponse response) throws Exception {
		String roId="";
		if(request.getParameter("roId")!=null){
			roId=(String)request.getParameter("roId").trim();
			ReservOrder reservOrder=businessService.findReservById(roId);
			List<ReservItem> reservItemList=reservOrder.getReservItems();
			List<Room> roomList=new ArrayList();
			JSONArray jsonItems=new JSONArray();
			for(ReservItem item : reservItemList){
				Room room=item.getRoom();
				jsonItems.add(JSONObject.fromObject(room));
			}
			JSONObject jsonBack=new JSONObject();
			jsonBack.put("rooms", jsonItems);
			System.out.println(jsonBack);
			request.setCharacterEncoding("utf-8");   
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonBack);
		}else if(request.getParameter("rmId")!=null){
			String rmId=(String)request.getParameter("rmId").trim();
			ReservOrder reservOrder=businessService.findReservOrderByRmId(rmId);
			List<ReservItem> reservItemList=reservOrder.getReservItems();
			List<Room> roomList=new ArrayList();
			JSONArray jsonItems=new JSONArray();
			for(ReservItem item : reservItemList){
				Room room=item.getRoom();
				jsonItems.add(JSONObject.fromObject(room));
			}
			JSONObject jsonBack=new JSONObject();
			jsonBack.put("rooms", jsonItems);
			System.out.println(jsonBack);
			request.setCharacterEncoding("utf-8");   
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonBack);
		}else{
			
		}
		return null;
	}
	private String createReservOrderId(){
		String orderId="RO",baseString="";
		BaseInfo orderIdBaseInfo=businessService.findBaseInfoByName("ReservOrderID");
		String baseId=orderIdBaseInfo.getBioValue();
		System.out.println(baseId);
		for(int i=baseId.length();i<8;i++){
			baseString=baseString+"0";
		}
		orderId=orderId+baseString+baseId;
		int baseIdNum=Integer.parseInt(baseId);
		int updateIdNum=baseIdNum+1;
		orderIdBaseInfo.setBioValue(String.valueOf(updateIdNum));
		businessService.updateBaseInfoCioOrder(orderIdBaseInfo);
		System.out.println(orderId);
		return orderId;
	}
	public BusinessService getBusinessService() {
		return businessService;
	}
	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}
	
}
