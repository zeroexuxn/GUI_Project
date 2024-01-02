package system.admin.data;

import java.io.*;
import java.util.*;

import system.data.MemberType;

public class MemberList {

	/* 메소드명: printWholeMember
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: MemberType 클래스에 지정된 경로에 존재하는 src//files//data//member.txt파일의 내용 전체 출력
	 */
	public void printWholeMember() {
		ArrayList<MemberType> memberList=new ArrayList<MemberType>();//MemberType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MemberType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return;
		}
		
		try {
			while ((s = in.readLine()) != null) {
				// System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				MemberType member = new MemberType(tmp[MemberType.INDEX_MEMBER_ID], 
						Integer.parseInt(tmp[MemberType.INDEX_POINT]), tmp[MemberType.INDEX_BIRTH]);

				memberList.add(member);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		//출력
		System.out.println("현재 저장되어 있는 회원 목록을 출력합니다.");
		System.out.println("=================================================");
		System.out.println("         회원ID             포인트           생일");
		System.out.println("=================================================");
		for (int i = 0; i < memberList.size(); i++) {
			memberList.get(i).printAdminMember();
		}
		System.out.println("=================================================");
	}
	
	/* 메소드명: searchMemberById
	 * 파라미터: String searchedMemberId (찾고자 하는 회원의 회원ID)
	 * 반환값: 없음
	 * 기능 설명: MemberType 클래스에 지정된 경로에 존재하는 src//files//data//member.txt파일의 내용 중
	 * 		   searchedMemberId와 회원ID가 같은 회원 목록 출력
	 */
	public void searchMemberById(String searchedMemberId) {
		ArrayList<MemberType> memberList=new ArrayList<MemberType>();//MemberType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;//searchedMemberId와 회원ID가 같은 회원을 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MemberType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[MemberType.INDEX_MEMBER_ID].equals(searchedMemberId)) {//searchedMemberId와 회원ID가 같은 회원 발견
					found=true;
					MemberType member = new MemberType(tmp[MemberType.INDEX_MEMBER_ID], 
							Integer.parseInt(tmp[MemberType.INDEX_POINT]),tmp[MemberType.INDEX_BIRTH]);

					memberList.add(member);
				}

			}
			in.close();
			if (found==false) {//searchedMemberId와 회원 ID가 같은 회원을 발견하지 못하면
				System.out.printf("회원 ID가 %s인 회원을 찾을 수 없습니다.\n",searchedMemberId);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
			
		}
		if (found==true) {//searchedMemberId와 회원ID가 같은 회원 발견했으면
			System.out.printf("회원 ID가 %s인 회원 목록입니다.\n",searchedMemberId);
			System.out.println("=================================================");
			System.out.println("         회원ID             포인트           생일");
			System.out.println("=================================================");
			for (int i = 0; i < memberList.size(); i++) {
				memberList.get(i).printAdminMember();
			}
			System.out.println("=================================================");
		}
	}
	
	/* 메소드명: searchMemberByBirth
	 * 파라미터: String searchedBirth (찾고자 하는 회원의 생일 MMDD)
	 * 반환값: 없음
	 * 기능 설명: MemberType 클래스에 지정된 경로에 존재하는 src//files//data//member.txt파일의 내용 중
	 * 		   searchedBirth와 생일이 같은 회원 목록 출력
	 */
	public void searchMemberByBirth(String searchedBirth) {
		ArrayList<MemberType> memberList=new ArrayList<MemberType>();//MemberType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;//searchedBirth와 생일이 같은 회원을 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MemberType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return;
		}
		
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[MemberType.INDEX_BIRTH].equals(searchedBirth)) {//searchedBirth와 생일이 같은 회원 발견
					found=true;
					MemberType member = new MemberType(tmp[MemberType.INDEX_MEMBER_ID], 
							Integer.parseInt(tmp[MemberType.INDEX_POINT]),tmp[MemberType.INDEX_BIRTH]);

					memberList.add(member);
				}

			}
			in.close();
			if (found==false) {//searchedBirth와 생일이 같은 회원 발견하지 못하면
				System.out.printf("생일이 %s인 회원을 찾을 수 없습니다.\n",searchedBirth);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//searchedBirth와 생일이 같은 회원 발견했으면
			System.out.printf("생일이 %s인 회원 목록입니다.\n",searchedBirth);
			System.out.println("=================================================");
			System.out.println("         회원ID             포인트           생일");
			System.out.println("=================================================");
			for (int i = 0; i < memberList.size(); i++) {
				memberList.get(i).printAdminMember();
			}
			System.out.println("=================================================");
		}
	}

}
