package system.user;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import system.data.MenuType;
import system.data.OrderedMenuType;
import system.data.SalesDetailType;
import system.data.SalesTotalType;

public class UserModePanel extends JPanel {
	static HashMap<String, String> categoryMap = new HashMap<String, String>();	// {카테고리 ID : 카테고리명}으로 매핑되어 있는 맵
	static ArrayList<Integer> firstIdxByCatList = new ArrayList<Integer>();		// 카테고리별 첫번째 메뉴의 인덱스 목록
	static ArrayList<MenuType> menuList = new ArrayList<MenuType>();			// 메뉴 목록
	static ArrayList<OrderedMenuType> cart = new ArrayList<OrderedMenuType>();	// 주문 목록

	public static SalesTotalType salesTotal = new SalesTotalType();								// 통합 매출 내역 처리를 위한 객체
	public static ArrayList<SalesDetailType> salesDetailList = new ArrayList<SalesDetailType>();	// 상세 매출 내역 처리를 위한 리스트

	public static SelectHereOrTakeoutPanel selectHereOrTakeoutPanel;
	public static MenuPanel menuPanel;
	public static SelectPointOrCouponPanel selectPointOrCouponPanel;
	public static UsePointOrCouponPanel usePointOrCouponPanel;
	public static OrderDetailsPanel orderDetailsPanel;
	public static SelectPayMethodPanel selectPayMethodPanel;
	public static PayCardOrSimplePanel payCardOrSimplePanel;
	public static EarnPointPanel earnPointPanel;
	public static UpdateDataProcess updateDataProcess;
	public static SelectPrintRecieptPanel selectPrintReceiptPanel;
	public static FinishOrderPanel finishOrderPanel;
	
	// 싱글톤 객체 생성
	private static UserModePanel userModePanel = new UserModePanel();
	
	// 생성자
	private UserModePanel() {
		this.setLayout(null);
		
		selectHereOrTakeoutPanel = new SelectHereOrTakeoutPanel();
		selectHereOrTakeoutPanel.setBounds(0, 0, 720, 1000);
		this.add(selectHereOrTakeoutPanel);
		
		menuPanel = new MenuPanel();
		menuPanel.setVisible(false);
		menuPanel.setBounds(0, 0, 720, 1000);
		this.add(menuPanel);
		
		selectPointOrCouponPanel = new SelectPointOrCouponPanel();
		selectPointOrCouponPanel.setVisible(false);
		selectPointOrCouponPanel.setBounds(0, 0, 720, 1000);
		this.add(selectPointOrCouponPanel);
		
		usePointOrCouponPanel = new UsePointOrCouponPanel();
		usePointOrCouponPanel.setVisible(false);
		usePointOrCouponPanel.setBounds(0, 0, 720, 1000);
		this.add(usePointOrCouponPanel);
		
		orderDetailsPanel = new OrderDetailsPanel();
		orderDetailsPanel.setVisible(false);
		orderDetailsPanel.setBounds(0, 0, 720, 1000);
		this.add(orderDetailsPanel);
		
		selectPayMethodPanel = new SelectPayMethodPanel();
		selectPayMethodPanel.setVisible(false);
		selectPayMethodPanel.setBounds(0, 0, 720, 1000);
		this.add(selectPayMethodPanel);
		
		payCardOrSimplePanel = new PayCardOrSimplePanel();
		payCardOrSimplePanel.setVisible(false);
		payCardOrSimplePanel.setBounds(0, 0, 720, 1000);
		this.add(payCardOrSimplePanel);
		
		earnPointPanel = new EarnPointPanel();
		earnPointPanel.setVisible(false);
		earnPointPanel.setBounds(0, 0, 720, 1000);
		this.add(earnPointPanel);

		selectPrintReceiptPanel = new SelectPrintRecieptPanel();
		selectPrintReceiptPanel.setVisible(false);
		selectPrintReceiptPanel.setBounds(0, 0, 720, 1000);
		this.add(selectPrintReceiptPanel);
		
		updateDataProcess = new UpdateDataProcess();
		
		finishOrderPanel = new FinishOrderPanel();
		finishOrderPanel.setVisible(false);
		finishOrderPanel.setBounds(0, 0, 720, 1000);
		this.add(finishOrderPanel);
	}
	
	// 싱글톤 객체 반환 메소드
	public static UserModePanel getInstance() {
		return userModePanel;
	}
	
	/* 메소드명: initialize
	 * 파라미터: 없음
	 * 반화값: 없음
	 * 기능 설명: 주문이 진행되는 동안 저장되어 있던 정보를 모두 초기화(삭제)한다.
	 */
	public void initialize() {
		categoryMap.clear();
		firstIdxByCatList.clear();
		menuList.clear();
		cart.clear();
		
		salesTotal.reset();
		salesDetailList.clear();
	}
}
