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
	static String target; //���� ���
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
			System.out.println("db ���� ����");
			
			while(isContinue){
				System.out.println("�޴��� �����ϼ��� : ");
				System.out.println("1. ���   2. ����  3. ��ȸ  4. ����   5. ����");
				int select = scan.nextInt();
				switch(select){
				case 1:
					System.out.println("ȸ�� ������ ����մϴ�.");
					System.out.print("�̸�: ");
					name = scan.next();
					System.out.print("�ڵ��� ��ȣ: ");
					phoneNumber = scan.next();
					System.out.println("�̸��� " + name);
					System.out.println("�ڵ��� ��ȣ�� " + phoneNumber);
					
					pstmt = con.prepareStatement("INSERT INTO book VALUES(?,?)");
					pstmt.setString(1, name);
					pstmt.setString(2, phoneNumber);
					pstmt.executeQuery();
					
					break;
					
				case 2:
					System.out.println("ȸ�� ������ �����մϴ�.");
					System.out.print("�̸��� �����Ϸ��� 1����, �ڵ��� ��ȣ�� �����Ϸ��� 2���� �Է��ϼ���: ");
					target = null;
					choice = scan.nextInt();
					if(choice == 1){
						System.out.print("�����ϱ� ���� �̸��� �Է��ϼ���: ");
						name = scan.next();
						pstmt = con.prepareStatement("SELECT * FROM book WHERE name = '" + name + "'");
						rs = pstmt.executeQuery();
						
						while(rs.next()){
							target = rs.getString("name");
						}
						
						if(target == null){
							// ��ġ�ϴ� ������ ���� ���
							System.out.println("��ġ�ϴ� ������ �����ϴ�.");
						}else{
							System.out.print("�����ϱ� ���ϴ� �̸��� �Է��Ͻÿ�: ");
							String changeName = scan.next();
							// ��ġ�ϴ� ������ �ִ� ���
							pstmt = con.prepareStatement("UPDATE book SET name =? WHERE name = '"+ name + "'");
							pstmt.setString(1, changeName);
							pstmt.executeQuery();
							System.out.println("�̸� ������ �Ϸ�Ǿ����ϴ�.");
						}
						
					}else if(choice == 2){
						System.out.print("�����ϱ� ���� �ڵ��� ��ȣ�� �Է��ϼ���: ");
						phoneNumber = scan.next();
						pstmt = con.prepareStatement("SELECT * FROM book WHERE phone_number = '" + phoneNumber + "'");
						rs = pstmt.executeQuery();
						
						while(rs.next()){
							target = rs.getString("phone_number");
						}
						
						if(target == null){
							// ��ġ�ϴ� ������ ���� ���
							System.out.println("��ġ�ϴ� ������ �����ϴ�.");
							
						}else{
							// ��ġ�ϴ� ������ �ִ� ���
							System.out.print("�����ϱ� ���ϴ� ��ȣ�� �Է��Ͻÿ�: ");
							String changePhone = scan.next();
							// ��ġ�ϴ� ������ �ִ� ���
							pstmt = con.prepareStatement("UPDATE book SET phone_number =? WHERE phone_number = '"+ phoneNumber + "'");
							pstmt.setString(1, changePhone);
							pstmt.executeQuery();
							System.out.println("�ڵ��� ��ȣ ������ �Ϸ� �Ǿ����ϴ�.");
						}
					}else{
						System.out.println("�߸� �Է��ϼ̽��ϴ�.");
						System.out.println("�ʱ� �޴��� ���ư��ϴ�.");
					}
					break;
				case 3:
					System.out.println("ȸ�� ������ ��ȸ�մϴ�.");
					pstmt = con.prepareStatement("SELECT * FROM book");
					rs = pstmt.executeQuery();
					
					System.out.println("�̸�\t ��ȭ��ȣ");
					
					while(rs.next()){
						System.out.print(rs.getString("name") + "\t");
						System.out.println(rs.getString("phone_number") + "\t");
						System.out.println();
					}
					break;
				case 4:
					System.out.println("ȸ�� ������ �����մϴ�.");
					System.out.println("������ ���ϴ� ȸ���� �̸��� �Է��ϼ���: ");
					target = null;
					name = scan.next();
					// ���� ����� �����ͺ��̽��� ��ϵǾ� �ִ��� Ȯ���Ѵ�.
					pstmt = con.prepareStatement("SELECT * FROM book WHERE name = '" + name + "'");
					rs = pstmt.executeQuery();					

					while(rs.next()){
						target = rs.getString("name");
					}
					
					if(target == null){
						System.out.println("�����Ϸ��� ȸ�� ������ �����ϴ�.");						
					}else{
						pstmt = con.prepareStatement("DELETE FROM book WHERE name = '"+ target +"'");
						pstmt.executeQuery();
						System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
					}
					
					
					break;
				case 5:
					System.out.println("ȸ�� ���� ���� �ý����� �����մϴ�.");
					isContinue = false;
					break;
				default:
					System.out.println("�߸� �����ϼ̽��ϴ�. �ٽ� �������ּ���.");
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
