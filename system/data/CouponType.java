package system.data;

public class CouponType {
	//필드
	public static final String FILE_NAME = "src/files/data/coupon.txt";			//쿠폰 파일 경로
	public static final String TMP_FILE_NAME = "src/files/data/coupon_tmp.txt";	//수정된 쿠폰 파일 임시로 저장할 경로
	
	public static final int INDEX_COUPON_ID = 0;	//쿠폰ID를 가지고 있는 인덱스 번호
	public static final int INDEX_PRICE = 1;		//쿠폰 금액을 가지고 있는 인덱스 번호
	public static final int INDEX_USED = 2;			//쿠폰 사용 여부를 가지고 있는 인덱스 번호
		
	private String id = null;		//쿠폰ID
	private int price = 0;			//쿠폰 금액
	private String used = null;		//쿠폰 사용 여부

	// 생성자
	public CouponType(String id, int price, String used) {
		super();
		this.id = id;
		this.price = price;
		this.used = used;
	}

	// getter & setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	// 관리자 모드: 쿠폰 목록 출력 메소드
	/* 메소드명: printAdminCoupon
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 관리자 모드에서 쿠폰 목록을 양식에 맞게 출력한다. 
	 */
	public void printAdminCoupon() {
		System.out.print("      ");
		System.out.printf("%12s", this.id);
		System.out.print("\t\t");
		System.out.printf("%,5d", this.price);
		System.out.print("\t\t");
		System.out.printf("%s", this.used);
		System.out.println();
	}

}
