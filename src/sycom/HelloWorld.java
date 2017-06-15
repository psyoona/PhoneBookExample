package sycom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HelloWorld {
	static{
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
	}
	
	static Scanner scan;
	static String phoneNumber;
	static String name;
	static String target; //삭제 대상
	static int choice;
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		String dburl = null;
		Connection con = null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		
		scan = new Scanner(System.in);
		boolean isContinue = true;
		
		
		try{
			dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			con = DriverManager.getConnection(dburl, "scott","tiger");
			System.out.println("db 연결 성공");
			
			while(isContinue){
				System.out.println("메뉴를 선택하세요 : ");
				System.out.println("1. 등록   2. 수정  3. 조회  4. 삭제   5. 종료");
				int select = scan.nextInt();
				switch(select){
				case 1:
					System.out.println("회원 정보를 등록합니다.");
					System.out.print("이름: ");
					name = scan.next();
					System.out.print("핸드폰 번호: ");
					phoneNumber = scan.next();
					System.out.println("이름은 " + name);
					System.out.println("핸드폰 번호는 " + phoneNumber);
					
					pstmt = con.prepareStatement("INSERT INTO book VALUES(?,?)");
					pstmt.setString(1, name);
					pstmt.setString(2, phoneNumber);
					pstmt.executeQuery();
					
					break;
					
				case 2:
					System.out.println("회원 정보를 수정합니다.");
					System.out.print("이름을 변경하려면 1번을, 핸드폰 번호를 변경하려면 2번을 입력하세요: ");
					target = null;
					choice = scan.nextInt();
					if(choice == 1){
						System.out.print("변경하기 이전 이름을 입력하세요: ");
						name = scan.next();
						pstmt = con.prepareStatement("SELECT * FROM book WHERE name = '" + name + "'");
						rs = pstmt.executeQuery();
						
						while(rs.next()){
							target = rs.getString("name");
						}
						
						if(target == null){
							// 일치하는 정보가 없는 경우
							System.out.println("일치하는 정보가 없습니다.");
						}else{
							System.out.print("변경하기 원하는 이름을 입력하시오: ");
							String changeName = scan.next();
							// 일치하는 정보가 있는 경우
							pstmt = con.prepareStatement("UPDATE book SET name =? WHERE name = '"+ name + "'");
							pstmt.setString(1, changeName);
							pstmt.executeQuery();
							System.out.println("이름 변경이 완료되었습니다.");
						}
						
					}else if(choice == 2){
						System.out.print("변경하기 이전 핸드폰 번호를 입력하세요: ");
						phoneNumber = scan.next();
						pstmt = con.prepareStatement("SELECT * FROM book WHERE phone_number = '" + phoneNumber + "'");
						rs = pstmt.executeQuery();
						
						while(rs.next()){
							target = rs.getString("phone_number");
						}
						
						if(target == null){
							// 일치하는 정보가 없는 경우
							System.out.println("일치하는 정보가 없습니다.");
							
						}else{
							// 일치하는 정보가 있는 경우
							System.out.print("변경하기 원하는 번호를 입력하시오: ");
							String changePhone = scan.next();
							// 일치하는 정보가 있는 경우
							pstmt = con.prepareStatement("UPDATE book SET phone_number =? WHERE phone_number = '"+ phoneNumber + "'");
							pstmt.setString(1, changePhone);
							pstmt.executeQuery();
							System.out.println("핸드폰 번호 변경이 완료 되었습니다.");
						}
					}else{
						System.out.println("잘못 입력하셨습니다.");
						System.out.println("초기 메뉴로 돌아갑니다.");
					}
					break;
				case 3:
					System.out.println("회원 정보를 조회합니다.");
					pstmt = con.prepareStatement("SELECT * FROM book");
					rs = pstmt.executeQuery();
					
					System.out.println("이름\t 전화번호");
					
					while(rs.next()){
						System.out.print(rs.getString("name") + "\t");
						System.out.println(rs.getString("phone_number") + "\t");
						System.out.println();
					}
					break;
				case 4:
					System.out.println("회원 정보를 삭제합니다.");
					System.out.println("삭제를 원하는 회원의 이름을 입력하세요: ");
					target = null;
					name = scan.next();
					// 삭제 대상이 데이터베이스에 등록되어 있는지 확인한다.
					pstmt = con.prepareStatement("SELECT * FROM book WHERE name = '" + name + "'");
					rs = pstmt.executeQuery();					

					while(rs.next()){
						target = rs.getString("name");
					}
					
					if(target == null){
						System.out.println("삭제하려는 회원 정보가 없습니다.");						
					}else{
						pstmt = con.prepareStatement("DELETE FROM book WHERE name = '"+ target +"'");
						pstmt.executeQuery();
						System.out.println("정보 삭제가 완료되었습니다.");
					}
					
					
					break;
				case 5:
					System.out.println("회원 정보 관리 시스템을 종료합니다.");
					isContinue = false;
					break;
				default:
					System.out.println("잘못 선택하셨습니다. 다시 선택해주세요.");
					break;
				}				
			} // End of While
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(con != null){try{con.close();} catch(Exception e){}}
			if(pstmt != null){try{pstmt.close();}catch(Exception e){}}
			if(rs != null){try{rs.close();}catch(Exception e){}}
		}
	}
}
