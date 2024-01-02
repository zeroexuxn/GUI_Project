package system.data;

public class MemberType {
	//필드
	public static final String FILE_NAME = "src/files/data/member.txt";			//회원 파일 경로
	public static final String TMP_FILE_NAME = "src/files/data/member_tmp.txt";	//수정된 회원 파일 임시로 저장할 경로
	
	public static final int INDEX_MEMBER_ID = 0;	//회원 ID를 가지고 있는 인덱스 번호
	public static final int INDEX_POINT = 1;		//누적 포인트를 가지고 있는 인덱스 번호
	public static final int INDEX_BIRTH = 2;		//생일 정보를 가지고 있는 인덱스 번호
	
	private String memberId = null;	//휴대전화 번호 
	private int point = 0;			//누적 포인트
	private String birth = null;	//생년월일
	
	// 생성자
	public MemberType (String memberId, int point, String birth) {
		this.memberId=memberId;
		this.point=point;
		this.birth=birth;
	}

	//getter setter <= phoneNumber, point, dateOfBirth 선택
	public String getPhoneNumber() {
		return memberId;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.memberId = phoneNumber;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getbirth() {
		return birth;
	}

	public void setbirth(String birth) {
		this.birth = birth;
	}
	
	// 관리자 모드: 회원 목록 출력 메소드
	/* 메소드명: printAdminMember
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 관리자 모드에서 회원 목록을 양식에 맞게 출력한다. 
	 */
	public void printAdminMember() {
		System.out.print("      ");
		System.out.printf("%11s", this.memberId);
		System.out.print("\t");
		System.out.printf("%,6d", this.point);
		System.out.print("\t\t  ");
		System.out.printf("%4s", this.birth);
		System.out.println();
	}
}
