package system.main;

import java.io.File;
import java.util.Calendar;

import system.data.CategoryType;
import system.data.CouponType;
import system.data.MemberType;
import system.data.MenuType;

public class FileCheck {
	// 필수 데이터 경로
	private static final String MENU_FILE = MenuType.FILE_NAME;				// 메뉴
	private static final String CATEGORY_FILE = CategoryType.FILE_NAME;		// 카테고리
	private static final String MEMBER_FILE = MemberType.FILE_NAME;			// 회원
	private static final String COUPON_FILE = CouponType.FILE_NAME;			// 쿠폰

	// 주문 처리 파일 존재 여부 체크
	/* 메소드명: filesCheck
	 * 파라미터: 없음
	 * 반환값: boolean fileCheck (true: 모든 파일이 존재, false: 파일이 1개라도 존재하지 않음)
	 * 기능 설명: 주문 처리 파일이 모두 존재하는지 검사한다.
	 * 		   (주문 처리 파일: menu.txt, category.txt, member.txt, coupon.txt)
	 */
	public static boolean filesCheck() {
		boolean fileCheck = true;
		
		System.out.println(Calendar.getInstance().getTime().toString() + ":: Main :: 시스템 실행 전 주문 처리 파일을 확인합니다.");
		
		if(!new File(MENU_FILE).exists()) {
			System.out.println(MENU_FILE + "이 존재하지 않습니다.");
			fileCheck = false;
		} else {
			System.out.println(MENU_FILE + "이 존재합니다.");
		}
		
		if(!new File(CATEGORY_FILE).exists()) {
			System.out.println(CATEGORY_FILE + "이 존재하지 않습니다.");
			fileCheck = false;
		} else {
			System.out.println(CATEGORY_FILE + "이 존재합니다.");
		}
		
		if(!new File(MEMBER_FILE).exists()) {
			System.out.println(MEMBER_FILE + "이 존재하지 않습니다.");
			fileCheck = false;
		} else {
			System.out.println(MEMBER_FILE + "이 존재합니다.");
		}
		
		if(!new File(COUPON_FILE).exists()) {
			System.out.println(COUPON_FILE + "이 존재하지 않습니다.");
			fileCheck = false;
		} else {
			System.out.println(COUPON_FILE + "이 존재합니다.");
		}
		
		return fileCheck;
	}
}