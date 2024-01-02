package system.data;

public class SalesDetailType {
	//필드
	public static final String FILE_DIRECTORY = "src\\files\\sales_data\\";//상세 매출 내역 파일 경로
	
	public static final int INDEX_ORDER_NO = 0;		//주문 번호를 가지고 있는 인덱스 번호
	public static final int INDEX_TYPE = 1;			//상품 유형(메뉴 M, 쿠폰 C, 포인트 P)을 가지고 있는 인덱스 번호
	public static final int INDEX_ID = 2;			//상품 ID를 가지고 있는 인덱스 번호
	public static final int INDEX_NAME = 3;			//상품명을 가지고 있는 인덱스 번호
	public static final int INDEX_PRICE = 4;		//상품 단가를 가지고 있는 인덱스 번호
	public static final int INDEX_QUANTITY = 5;		//주문 수량을 가지고 있는 인덱스 번호
	public static final int INDEX_TOTAL_PRICE = 6;	//금액을 가지고 있는 인덱스 번호
	
	private String fileName = "sales_detail";//파일명
	
	private String orderNo = " ";	//주문 번호
	private String type = " ";		//상품 유형
	private String id = " ";		//상품 ID
	private String name = " ";		//상품명
	private int price = 0;			//단가
	private int quantity = 0;		//주문 수량
	private int totalPrice = 0;		//금액 (단가 * 주문 수량)
	
	// 생성자
	public SalesDetailType(String orderNo, String type, String id, String name, int price, int quantity) {
		this.orderNo = orderNo;
		this.type = type;
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		
		this.fileName = FILE_DIRECTORY + this.fileName + "(" + orderNo.substring(0, 8) + ").txt"; // 주문번호로 매출일자를 확인하여 해당일의 파일명을 알아냄
		
		totalPrice = price * quantity;	// 단가(prce)와 주문 수량(quantity) 정보를 이용해 금액 설정
	}
	
	// getter
	public String getFileName() {
		return fileName;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public String getType() {
		return type;
	}
	
	public String getId() {
		return id;
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
	
	// txt 파일 데이터 양식에 맞는 문자열로 변환하여 반환
	/* 메소드명: toDataTemplate
	 * 파라미터: 없음
	 * 반환값: String str
	 * 		 str: 실제 sales_detail(YYYYMMDD).txt 파일에 작성되는 형식으로 구성된 한 줄의 문자열
	 * 기능 설명: 현재 객체가 가진 멤버 변수들의 값을 이용해 데이터 파일 형식에 맞는 한 줄의 문자열을 만들어 반환한다.
	 */
	public String toDataTemplate() {
		String str = "";
		str += orderNo + "|" + type + "|" + id + "|" + name + "|";
		str += price + "|" + quantity + "|" + totalPrice;
		
		return str;
	}
	
}
