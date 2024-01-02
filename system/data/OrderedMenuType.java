package system.data;

public class OrderedMenuType {
	//필드
	private static int TOTAL_QUANTITY = 0;	// 현재 주문 목록에 있는 상품의 총 수량
	private static int TOTAL_AMT = 0;		// 현재 주문 목록에 있는 상품의 총 금액
	
	private int realMenuIndex;			// 메뉴 리스트에서 해당 메뉴의 실제 위치를 나타내는 인덱스
	private String categoryId = null;	// 카테고리 ID
	private String menuNo = null;		// 메뉴 순번
	private String name = null;			// 메뉴명
	private int price = 0;				// 상품 단가
	private int quantity = 0;			// 주문 수량
	private int totalPrice = 0;			// 금액 (상품 단가 * 주문 수량)
	

	// 생성자
	public OrderedMenuType(int realMenuIndex, MenuType menu, int quantity) {
		this.realMenuIndex = realMenuIndex;
		
		this.categoryId = menu.getCategoryId();
		this.menuNo = menu.getMenuNo();
		this.name = menu.getName();
		this.price = menu.getPrice();
		
		this.quantity = quantity;
		totalPrice = price * quantity;	// 단가(price)와 주문 수량(quantity) 정보를 이용해 금액 설정
		
		TOTAL_QUANTITY += quantity;		// 주문 수량만큼 총 주문 수량 증가
		TOTAL_AMT += totalPrice;		// 주문한 메뉴의 금액만큼 총 금액 증가
	}

	public static int getTOTAL_QUANTITY() {
		return TOTAL_QUANTITY;
	}

	public static int getTOTAL_AMT() {
		return TOTAL_AMT;
	}

	public int getRealMenuIndex() {
		return realMenuIndex;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getMenuNo() {
		return menuNo;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	
	// 수량 업데이트
	public void updateQuantity(int difference) {
		this.quantity += difference;
		this.totalPrice += difference * price;

		TOTAL_QUANTITY += difference;
		TOTAL_AMT += difference * price;
	}
	
	// 총 주문 수량, 총 금액 초기화: 주문 취소, 주문 완료, 주문 목록 전체 삭제
	public static void reset() {
		TOTAL_QUANTITY = 0;
		TOTAL_AMT = 0;
	}
}
