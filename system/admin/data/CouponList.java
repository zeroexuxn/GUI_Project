package system.admin.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import system.data.CouponType;
import system.data.MemberType;

public class CouponList {
	private Scanner sc = null;

	// 생성자
	public CouponList(Scanner sc) {
		this.sc = sc;
	}

	/*
	 * 메소드명: printWholeCoupon
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는
	 * 			src//files//data//coupon.txt파일의 내용 전체 출력
	 */
	public void printWholeCoupon() {
		ArrayList<CouponType> couponList = new ArrayList<CouponType>();// CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in; // 입력 스트림
		String s;
		String[] tmp;

		try {
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("쿠폰 파일을 찾을 수 없습니다.");
			return;
		}

		// 파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID],
						Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

				couponList.add(coupon);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		// 출력
		System.out.println("현재 저장되어 있는 쿠폰 리스트를 출력합니다.");
		System.out.println("====================================================");
		System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
		System.out.println("====================================================");
		for (int i = 0; i < couponList.size(); i++) {
			couponList.get(i).printAdminCoupon();
		}
		System.out.println("====================================================");

	}

	/*
	 * 메소드명: searchCouponById
	 * 파라미터: String couponByUsed
	 * 반환값: 없음
	 * 기능 설명: CouponType
	 * 클래스에 지정된 경로에 존재하는 src//files//data//coupon.txt파일의 내용 중 searchedId와
	 * CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록 출력
	 */
	public void searchCouponById(String searchedId) {
		ArrayList<CouponType> couponList = new ArrayList<CouponType>();// CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in; // 입력 스트림
		boolean found = false;// searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견했는지 확인할 변수
		String s;
		String[] tmp;

		try {
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("쿠폰 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[CouponType.INDEX_COUPON_ID].equals(searchedId)) {// searchedId와 CouponType.INDEX_COUPON_ID가
																			// 동일한 쿠폰 목록을 발견
					found = true;
					CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID],
							Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

					couponList.add(coupon);
				}

			}
			in.close();
			if (found == false) {// searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견하지 못하면
				System.out.printf("쿠폰 ID가 %s인 쿠폰을 찾을 수 없습니다.\n", searchedId);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found == true) {// searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견했으면 출력 시작
			System.out.printf("쿠폰 ID가 %s인 쿠폰 목록입니다.\n", searchedId);
			System.out.println("====================================================");
			System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
			System.out.println("====================================================");
			for (int i = 0; i < couponList.size(); i++) {
				couponList.get(i).printAdminCoupon();
			}
			System.out.println("====================================================");
		}
	}

	/*
	 * 메소드명: searchCouponByPrice
	 * 파라미터: int searchedPrice
	 * 반환값: 없음
	 * 기능 설명: CouponType클래스에 지정된 경로에 존재하는 src//files//datas//coupon.txt파일의 내용 중 
	 * 			searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록 출력
	 */
	public void searchCouponByPrice(int searchedPrice) {
		ArrayList<CouponType> couponList = new ArrayList<CouponType>();// CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in; // 입력 스트림
		boolean found = false;// searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견했는지 확인할 변수
		String s;
		String[] tmp;

		try {
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("쿠폰 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				int PriceInt = Integer.parseInt(tmp[CouponType.INDEX_PRICE]);

				if (searchedPrice == PriceInt) {// searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견
					found = true;
					CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID],
							Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

					couponList.add(coupon);
				}
			}
			in.close();
			if (found == false) {// searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견하지 못하면
				System.out.printf("쿠폰 금액이 %d인 쿠폰을 찾을 수 없습니다.\n", searchedPrice);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found == true) {// searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견했으면 출력 시작
			System.out.printf("현재 쿠폰 금액이 %s인 목록입니다.\n", searchedPrice);
			System.out.println("====================================================");
			System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
			System.out.println("====================================================");
			for (int i = 0; i < couponList.size(); i++) {
				couponList.get(i).printAdminCoupon();
			}
			System.out.println("====================================================");
		}
	}

	/*
	 * 메소드명: searchCouponByUsed
	 * 파라미터: String searchedUsed
	 * 반환값: 없음
	 * 기능 설명: CouponType
	 * 클래스에 지정된 경로에 존재하는 src//files//data//coupon.txt파일의 내용 중 
	 * 							searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 출력
	 */
	public void searchCouponByUsed(String searchedUsed) {
		ArrayList<CouponType> couponList = new ArrayList<CouponType>();// CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in; // 입력 스트림
		boolean found = false;// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견했는지 확인할 변수
		String s;
		String[] tmp;

		try {
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("쿠폰 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[CouponType.INDEX_USED].equals(searchedUsed)) {// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견
					found = true;
					CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID],
							Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

					couponList.add(coupon);
				}
			}
			in.close();
			if (found == false) {// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견하지 못하면
				System.out.printf("사용 여부가 %s인 쿠폰을 찾을 수 없습니다.\n", searchedUsed);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found == true) {// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견했으면 출력 시작
			System.out.printf("현재 사용 여부가 %s인 목록입니다.\n", searchedUsed);
			System.out.println("====================================================");
			System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
			System.out.println("====================================================");
			for (int i = 0; i < couponList.size(); i++) {
				couponList.get(i).printAdminCoupon();
			}
			System.out.println("====================================================");
		}
	}

	/*
	 * 메소드명: findLastCouponNo
	 * 파라미터: String temp_member_id
	 * 			temp_member_id = addCoupon에서 입력받은 회원Id값
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는 src//files//data//coupon.txt파일에 입력한 내용을 추가한다.
	 */
	// 카테고리별로 마지막 쿠폰 순번을 찾는 메소드
	private int findLastCouponNo(String temp_member_id) {
		int lastCouponNo = 0;
		
		try (BufferedReader in = new BufferedReader(new FileReader(CouponType.FILE_NAME))) {
			String s;
			String[] tmp;
			int couponNo = 0; // 쿠폰 순번
			
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				try {
					String substring = tmp[CouponType.INDEX_COUPON_ID].substring(12, 15);
					if (temp_member_id.equals(tmp[CouponType.INDEX_COUPON_ID].substring(1, 12)))
						couponNo = Integer.parseInt(substring);
					
				} catch (IndexOutOfBoundsException | NumberFormatException e) {
				    couponNo = 0; // 정보를 못 찾았거나 변환에 실패한 경우 0으로 초기화
				}				  // 뽑아서 변수 couponNo에 대입
				if (couponNo > lastCouponNo) { // 만약에
					lastCouponNo = couponNo;
				}
//				System.out.println(substring);
			}
		} catch (IOException e) {
			System.out.println("입출력 오류가 발생했습니다.");
		} catch (Exception e) {
			System.out.println("예상하지 못한 오류가 발생했습니다.");
		}

		return lastCouponNo;
	}
	
	/*
	 * 메소드명: addCoupon
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는
	 * 			src//files//data//coupon.txt파일에 입력한 내용을 쿠폰 테이블의 형식으로 추가한다.
	 */
	public void addCoupon() {
		BufferedReader in; // 입력 스트림
		BufferedReader inMember;//멤버 파일 읽어올 입력 스트림
		PrintWriter out; // 출력 스트림

		// 새로 추가할 쿠폰의 정보 담아둘 배열
		String[] newMenuData = new String[3];
		String temp_member_id;

		// 카테고리 ID 입력
		System.out.println("쿠폰을 추가하실 회원의 회원Id를 입력하세요.(잘못된 회원Id를 입력하여도 쿠폰 금액까지 입력해야 합니다.)");
		while (true) {
			System.out.print(" > ");
			try {
				temp_member_id = sc.nextLine();
				if (temp_member_id.length() != 11) {
					System.out.println("회원ID는 11자리의 숫자로만 구성되어 있어야 합니다. 다시 입력해주세요.");
				}else
					break;
			} catch (NumberFormatException e) {
				System.out.println("회원ID는 숫자로만 구성되어 있어야 합니다. 다시 입력해주세요.");
			}
		}

		// 마지막 쿠폰 순번에 +1 해서 새로운 쿠폰 순번 정하기
		int lastCouponNo = findLastCouponNo(temp_member_id);
		int newCouponNo = lastCouponNo + 1;
		newMenuData[CouponType.INDEX_COUPON_ID] = "C" + temp_member_id + String.format("%03d", newCouponNo);
		
		// 쿠폰 금액 입력
		System.out.println("추가하실 쿠폰의 쿠폰 금액을 입력하세요.");
		int addCouponPrice = 0;
		while (true) {
			System.out.print(" > ");
			try {
				addCouponPrice = sc.nextInt();
				if (addCouponPrice < 0) {
					System.out.println("쿠폰 금액은 0보다 큰 정수만 입력 가능합니다. 다시 입력해주세요.");
				} else
					break;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println("쿠폰 금액은 숫자만 입력 가능합니다. 다시 입력해주세요.");
			}

		}
		newMenuData[CouponType.INDEX_PRICE] = Integer.toString(addCouponPrice);
		newMenuData[CouponType.INDEX_USED] = "Y";

		String str = String.join("|", newMenuData);

		String s;//쿠폰 파일 읽을 문자열
		String memberString;//회원 파일 읽을 문자열
		String[] members;//회원 1명의 모든 정보가 담길 배열
		
		String[] tmp;
		boolean found = false;

		
		try {
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));
			inMember=new BufferedReader(new FileReader(MemberType.FILE_NAME));
			out = new PrintWriter(new FileWriter(CouponType.TMP_FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return;
		}
		
		try {	
			while ((memberString=inMember.readLine())!=null) {
				members=memberString.split("\\|");
				if (members[MemberType.INDEX_MEMBER_ID].equals(temp_member_id)) {
					found = true;
				}
			}

			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				// 해당 카테고리를 가진 행이 나타났으면
				if (tmp[CouponType.INDEX_COUPON_ID].substring(1,12).equals(temp_member_id)) {
					found = true;
				} 	
				out.println(s);		
			}
			if (found == true) {
				out.println(str);
				System.out.println(str);
				System.out.println("쿠폰이 추가되었습니다.");
			} else {
				System.out.printf("맴버 ID가 %s인 맴버를 찾지 못했습니다. 쿠폰를 추가할 수 없습니다.\n", temp_member_id);
				in.close();
				inMember.close();
				out.close();
				
				return;

			}
			in.close();
			inMember.close();
			out.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		File file = new File(CouponType.FILE_NAME);
		file.delete();

		file = new File(CouponType.TMP_FILE_NAME);
		file.renameTo(new File(CouponType.FILE_NAME));
	}
	
}