package system.data;

public class MenuType {
	//필드
	public static final String FILE_NAME = "src/files/data/menu.txt";			//메뉴 파일 경로
	public static final String TMP_FILE_NAME = "src/files/data/menu_tmp.txt";	//수정된 메뉴 파일을 임시로 저장할 경로
	
	public static final int INDEX_CATEGORY_ID = 0;	//카테고리 ID를 가지고 있는 인덱스 번호
	public static final int INDEX_MENU_NO = 1;	  	//메뉴 순번을 가지고 있는 인덱스 번호
	public static final int INDEX_NAME = 2;		  	//메뉴명을 가지고 있는 인덱스 번호
	public static final int INDEX_PRICE = 3;	  	//메뉴별 가격을 가지고 있는 인덱스 번호
	public static final int INDEX_STOCK = 4;	  	//메뉴별 재고 현황을 가지고 있는 인덱스 번호
	public static final int INDEX_ENABLE = 5;		//메뉴 활성화 여부를 가지고 있는 인덱스 번호 
	
	private String categoryId = null;	//카테고리 ID
	private String menuNo = null;		//메뉴 순번
	private String name = null;			//메뉴명
	private int price = 0;				//가격
	private int stock = 0;				//재고
	private String enable = "Y";		//활성화 여부 (기본값 Y)
	
	//생성자
	public MenuType(String categoryId, String menuNo, String name, int price, int stock, String enable) {
		super();
		this.categoryId = categoryId;
		this.menuNo = menuNo;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.enable = enable;
	}

	//getter & setter
	public String getCategoryId() {
		return categoryId;
	}

	public void setcategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getMenuNo() {
		return menuNo;
	}

	public void setMenuNo(String menuNo) {
		this.menuNo = menuNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public String getEnable() {
		return enable;
	}
	
	public void setEnable(String enable) {
		this.enable=enable;
	}

	// 관리자 모드: 메뉴 목록 출력 메소드
	/* 메소드명: printAdminMenu
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 관리자 모드에서 메뉴 목록을 양식에 맞게 출력한다. 
	 */
	public void printAdminMenu() {
		System.out.printf("%6s", this.categoryId);
		System.out.print("\t\t");
		System.out.printf("%6s", this.menuNo);
		System.out.print("\t");
		System.out.printf("%25s", this.name);
		System.out.print("\t");
		System.out.printf("%,4d", this.price);
		System.out.print("\t");
		System.out.printf("%4d", this.stock);
		System.out.print("\t");
		System.out.printf("%4s", this.enable);
		System.out.println();
	}
	
	// 사용자 모드: 사용자가 주문을 진행하는 동안 변동되는 재고량 처리
	/* 메소드명: updateUserMenuStock
	 * 파라미터: int quantity
	 * 		  quantity: 사용자로부터 입력받은 주문 수량
	 * 반환값: 없음
	 * 기능 설명: 사용자가 주문을 진행하는 동안 발생한 수량 변경 사항에 대해 메뉴 재고량을 변경한다.
	 */
	public void updateUserMenuStock(int quantity) {
		stock -= quantity;
	}

	// txt 파일 데이터 양식에 맞는 문자열로 변환하여 반환
	/* 메소드명: toDataTemplate
	 * 파라미터: 없음
	 * 반환값: String str
	 * 		 str: 실제 menu.txt 파일에 작성되는 형식으로 구성된 한 줄의 문자열
	 * 기능 설명: 현재 객체가 가진 멤버 변수들의 값을 이용해 데이터 파일 형식에 맞는 한 줄의 문자열을 만들어 반환한다.
	 */
	public String toDataTemplate() {
		String str = "";
		str += categoryId + "|" + menuNo + "|" + name + "|" + price + "|" + stock;
		
		return str;
	}
}
