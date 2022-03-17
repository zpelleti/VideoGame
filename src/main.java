import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.*;

import net.proteanit.sql.DbUtils;

import javax.swing.JPanel;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
 
import net.proteanit.sql.DbUtils;

public class main {

	//static JFrame frame;	
	private JFrame frame;
	// ex class:
	public static class ex{
		public static int days = 0;
	}
	
	public JFrame getFrame() {
		return frame; 
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public main() {
		//initialize();
		login();
	}	 
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//login();
		create();
	}
	
//	private void initialize() {
////		setFrame(new JFrame());
////		getFrame().setBounds(100, 100, 450, 300);
////		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		login();
//	}

	private static Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loading...");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql?user=root&password=Pa55word01");
			System.out.println("Connected to MySQL");
			return connection;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


private void login() {
		frame = new JFrame("Login");
		
		frame.getContentPane().setBackground(new Color(119, 136, 153));
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBounds(77, 102, 1, 1);
		frame.getContentPane().add(horizontalBox);
		
		JLabel l_title = new JLabel("Video Game Management");
		l_title.setForeground(new Color(255, 255, 255));
		l_title.setFont(new Font("Tahoma", Font.BOLD, 18));
		l_title.setBounds(123, 25, 243, 45);
		frame.getContentPane().add(l_title);
		
		JLabel l_username = new JLabel("Username");
		l_username.setBounds(104, 74, 76, 14);
		l_username.setForeground(new Color(255, 215, 0));
		l_username.setFont(new Font("Tahoma", Font.BOLD, 12));
		frame.getContentPane().add(l_username);
		
		JLabel l_password = new JLabel("Password");
		l_password.setForeground(new Color(255, 215, 0));
		l_password.setFont(new Font("Tahoma", Font.BOLD, 12));
		l_password.setBounds(104, 134, 76, 14);
		frame.getContentPane().add(l_password);

		JTextField inputUserName = new JTextField();
		inputUserName.setBounds(104, 94, 285, 29);
		inputUserName.setForeground(new Color(112, 128, 144));
		inputUserName.setColumns(10);
		inputUserName.setFont(new Font("Tahoma", Font.BOLD, 14));
		frame.getContentPane().add(inputUserName);
		
		JPasswordField inputPassword = new JPasswordField();
		inputPassword.setToolTipText("Please enter your password");
		inputPassword.setBounds(104, 155, 285, 29);
		inputPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		inputPassword.setForeground(new Color(112, 128, 144));
		frame.getContentPane().add(inputPassword);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.setBounds(269, 195, 120, 30);
		loginBtn.setBackground(Color.WHITE);
		loginBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		frame.getContentPane().add(loginBtn);

	// create event listener for button:
		loginBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			// accept + save user inputs:
				String username = inputUserName.getText();
				String password = inputPassword.getText();
				
				if(username.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter UserName");
				}
				else if(password.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter Password");
				}
		
				else {
				// if user fields exist:
					System.out.println("Logging in...");
				// Connection to Database:
					Connection connection = connect();
					
					try {
						Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);   
						statement.executeUpdate("USE GAMES_LIBRARY");
					// retrieve user name and password:
						String str = ("Select * from USERS where USERNAME = '" + 
										username + "' and PASSWORD = '" + password + "'");
					// Query execution:
						ResultSet rs = statement.executeQuery(str);
						if(rs.next() == false) {
							System.out.println("User does not exist");
							JOptionPane.showMessageDialog(null, "Wrong UserName-Password combination");					
						}
						else {
						// to clear all field:
							frame.dispose();
						// moves cursor in front of result set:
							rs.beforeFirst();
							while(rs.next()) {
							// if user = admin:
								String Admin = rs.getString("ADMIN");
								//System.out.println(admin);
							// if user:
								String user = rs.getString("USERID");
							//set bool. value for admin:
								if(Admin.equals("1")) {
									admin_menu();
								} else {
									userMenu(user);			
									}
								}
							}
						}
				
					catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			
			}
		});	
	}
		
	
	private static void create() {
		try {
			Connection connection = connect();
			ResultSet rs = connection.getMetaData().getCatalogs();
		// to iterate catalogs in the ResultSet:
			while(rs.next()) {
		// database name = position 1:
				String DB = rs.getString(1);
				if(DB.equals("GAMES_LIBRARY")) {		//GAMES_LIBRARY
					System.out.println("Connecting to Games_Library...");
					Statement statement = connection.createStatement();
				// Drop to reset the database if it already exists:
					String mysql = "Drop Database...";
					statement.executeUpdate(mysql);			
				}
			}
			Statement statement = connection.createStatement();
			
			String mysql = "CREATE DATABASE IF NOT EXISTS GAMES_LIBRARY";	
			statement.executeUpdate(mysql);
			statement.executeUpdate("USE GAMES_LIBRARY");
			// to create users table :
			String mysql2 = "CREATE TABLE IF NOT EXISTS USERS(USERID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30), PASSWORD VARCHAR(30), ADMIN BOOLEAN)";
			statement.executeUpdate(mysql2);
			// to insert into user table :
			statement.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) VALUES('admin', 'admin', TRUE)");
			// create Games Table :
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS GAMES(GAMEID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,GAMENAME VARCHAR(50), PLATFORM VARCHAR(30), PRICE INT)");
			// create 'issued' table :
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS ISSUED(ISSUEID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,USERID INT,GAMEID INT,ISSUED_DATE VARCHAR(20),RETURN_DATE VARCHAR(20),PERIOD_DAYS INT, FINE INT)"); 
			// insert new Games table :
			statement.executeUpdate("INSERT INTO GAMES(GAMENAME, PLATFORM, PRICE)VALUES ('Minecraft', 'Xbox One', 45)"); 
			
				rs.close();
			}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void admin_menu() {
		// Create dialog box:
		frame = new JFrame("Admin Menu");
		// preset closing...:
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(119, 136, 153));
		frame.setBounds(100, 100, 500, 300);
		frame.getContentPane().setLayout(null);
		
		// title
		JLabel l_title = new JLabel("Video Game Admin Login");
		l_title.setForeground(new Color(255, 255, 255));
		l_title.setFont(new Font("Tahoma", Font.BOLD, 18));
		l_title.setBounds(25, 10, 243, 45);
		frame.getContentPane().add(l_title);
		
		// button to add new users:
		JButton btnAddUser = new JButton("Add User");
        btnAddUser.setBounds(150, 105, 110, 30);
        btnAddUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnAddUser.setBackground(new Color(255, 215, 0));
        frame.getContentPane().add(btnAddUser);
 
        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame newUser = new JFrame("Enter User Details");
                newUser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
                JLabel labelOne = new JLabel("UserName");
                labelOne.setBounds(30, 15, 100, 30);
                labelOne.setBackground(new Color(255, 215, 0));
                labelOne.setForeground(new Color(255, 255, 255));
                JLabel labelTwo = new JLabel("Password");
                labelTwo.setBounds(30, 50, 200, 30);
                labelTwo.setBackground(new Color(255, 215, 0));
                labelTwo.setForeground(new Color(255, 255, 255));

                // set text fields: 
                JTextField userField = new JTextField();
                userField.setBounds(110, 15, 200, 30);
                userField.setForeground(new Color(112, 128, 144));
                userField.setFont(new Font("Tahoma", Font.BOLD, 14));
                JPasswordField passField = new JPasswordField();
                passField.setBounds(110,  50, 200, 30);
                passField.setForeground(new Color(112, 128, 144));
                passField.setFont(new Font("Tahoma", Font.BOLD, 14));

                // set radio buttons:
                JRadioButton r1 = new JRadioButton("Admin");
                r1.setBounds(120, 80, 200, 30);
                r1.setBackground(new Color(0,0,0,0));
                r1.setForeground(new Color(255, 255, 255));
                r1.setOpaque(false);
                JRadioButton r2 = new JRadioButton("User");
                r2.setBounds(200, 80, 200, 30);
                r2.setBackground(new Color(0,0,0,0));
                r2.setForeground(new Color(255, 255, 255));
                r2.setOpaque(false);
                ButtonGroup radio = new ButtonGroup();
                radio.add(r1);
                radio.add(r2);

                JButton createUserBtn = new JButton("Create User");
                createUserBtn.setBounds(110, 120, 130, 30);
                createUserBtn.setBackground(new Color(255, 215, 0));
                createUserBtn.addActionListener(new ActionListener() {
 
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO Auto-generated method stub
                        String username = userField.getText();
                        String password = passField.getText();
                        Boolean admin = false;

                        if(r1.isSelected()) {
                            admin = true;
                        }

                        Connection connection = connect();

                        try {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("USE GAMES_LIBRARY");
                            statement.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) "
                                                  + "VALUES ('" + username + "', '"
                                                                  + password + "'," 
                                                                  + admin + ")");
                            JOptionPane.showMessageDialog(null, "Adding User...");
                            newUser.dispose();
                        }
                        catch(SQLException e1) {
                            JOptionPane.showMessageDialog(null, e1);
                        }                        
                    }
                }
            );

                //newUser.add(loginBtn);
                frame.getContentPane().setBackground(new Color(119, 136, 153));
                frame.getContentPane().setLayout(null);
                newUser.getContentPane().setBackground(new Color(119, 136, 153));
                newUser.add(r2);
                newUser.add(r1);
                newUser.add(labelOne);
                newUser.add(labelTwo);
                newUser.add(userField);
                newUser.add(passField);
                newUser.add(createUserBtn);
                newUser.setSize(350, 200);
                newUser.setLayout(null);
                newUser.setVisible(true);
                newUser.setLocationRelativeTo(null);
            }

        }
    );   
		// button to view DB:
		JButton viewBtn = new JButton("View Games");
		viewBtn.setBounds(20, 60, 110, 30);
		viewBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewBtn.setBackground(new Color(255, 215, 0));
		viewBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Games Available");

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// connect to database:
				Connection connection = connect();
				String msql = "SELECT * FROM GAMES";
				
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate("USE GAMES_LIBRARY");
					statement = connection.createStatement();
					
					ResultSet r = statement.executeQuery(msql);
					// view as table:
					JTable game_list = new JTable();
					game_list.setModel(DbUtils.resultSetToTableModel(r));
					// add scroll bar:
					JScrollPane sp = new JScrollPane(game_list);
				// add arguments for frame:
					frame.add(sp);
					frame.setSize(800, 400);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}	
		}
	);
	
		// button to view users:
		JButton usersBtn = new JButton("View Users");
		usersBtn.setBounds(150, 60, 110, 30);
		usersBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		usersBtn.setBackground(new Color(255, 215, 0));
		usersBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Our Users");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// connect to DB:
				Connection connection = connect();
				String msql = "SELECT * FROM USERS";
				
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate("USE GAMES_LIBRARY");
					statement = connection.createStatement();
					ResultSet r = statement.executeQuery(msql);
					JTable game_list = new JTable();
					game_list.setModel(DbUtils.resultSetToTableModel(r));
					JScrollPane sp = new JScrollPane(game_list);
					
					frame.add(sp);
					frame.setSize(800, 400);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
			
		}
	);
	// button to view issued games:
		JButton issuedBtn = new JButton("View Issued Games");
		issuedBtn.setBounds(280, 60, 150, 30);
		issuedBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		issuedBtn.setBackground(new Color(255, 215, 0));
		issuedBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame("Issued Games");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				Connection connection = connect();
				String msql = "SELECT * FROM ISSUED";
				
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate("USE GAMES_LIBRARY");
					statement = connection.createStatement();
					ResultSet r = statement.executeQuery(msql);
					JTable game_list = new JTable();
					game_list.setModel(DbUtils.resultSetToTableModel(r));
					
					JScrollPane sp = new JScrollPane(game_list);
					
					frame.add(sp);
					frame.setSize(800, 400);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
			
		}
	);
		
		
	// button to add new games:
		JButton addGameBtn = new JButton("Add New Game");
		addGameBtn.setBounds(280, 105, 150, 30);
		addGameBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addGameBtn.setBackground(new Color(255, 215, 0));
		addGameBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFrame newGame = new JFrame("Enter Game Details");
				newGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//-----------------------------------------------------------------------------			
				JLabel gameName = new JLabel("Game Name");
				gameName.setBounds(30, 15, 100, 30);
				gameName.setBackground(new Color(255, 215, 0));
				gameName.setForeground(new Color(255, 255, 255));
				
				JLabel gamePlat = new JLabel("Platform");
				gamePlat.setBounds(30, 53, 100, 30);
				gamePlat.setBackground(new Color(255, 215, 0));
				gamePlat.setForeground(new Color(255, 255, 255));
				
				JLabel gamePrice = new JLabel("Price");
				gamePrice.setBounds(30, 90, 100, 30);
				gamePrice.setBackground(new Color(255, 215, 0));
				gamePrice.setForeground(new Color(255, 255, 255));
	//------------------------------------------------------------------------------			
				JTextField nameField = new JTextField();
				nameField.setBounds(110, 15, 200, 30);
				nameField.setForeground(new Color(112, 128, 144));
				nameField.setFont(new Font("Tahoma", Font.BOLD, 14));
				JTextField platField = new JTextField();
				platField.setBounds(110, 53, 200, 30);
				platField.setForeground(new Color(112, 128, 144));
				platField.setFont(new Font("Tahoma", Font.BOLD, 14));
				JTextField priceField = new JTextField();
				priceField.setBounds(110, 90, 200, 30);
				priceField.setForeground(new Color(112, 128, 144));
				priceField.setFont(new Font("Tahoma", Font.BOLD, 14));
				
				JButton addNew = new JButton("Add Game");
				addNew.setBounds(110, 130, 130, 30);
				addNew.setBackground(new Color(255, 215, 0));
				addNew.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String gName = nameField.getText();
						String platform = platField.getText();
						String price = priceField.getText();
						int price_int = Integer.parseInt(price);
						
						Connection connection = connect();
						
						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");
							statement.executeUpdate("INSERT INTO GAMES(GAMENAME, PLATFORM, PRICE) "
															+ "VALUES ('" +gName+ "', '"
																			+platform+ "', "
																			+price_int+ ")");  
							
							
							
							JOptionPane.showMessageDialog(null, "Adding Game...");
							newGame.dispose();
							}
						catch(SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}								
					}						
				});
				
				frame.getContentPane().setBackground(new Color(119, 136, 153));
				frame.getContentPane().setLayout(null);
				newGame.getContentPane().setBackground(new Color(119, 136, 153));
				newGame.add(addNew);
				newGame.add(gameName);
				newGame.add(gamePlat);
				newGame.add(gamePrice);
				newGame.add(nameField);
				newGame.add(platField);
				newGame.add(priceField);
				newGame.setSize(350, 220);
				newGame.setLayout(null);
				newGame.setVisible(true);
				newGame.setLocationRelativeTo(null);				
			}
		}
	);
		 JButton issueGame = new JButton("Issue Game");
	        issueGame.setBounds(450, 60, 110, 30);
	        issueGame.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        issueGame.setBackground(new Color(255, 215, 0));

	        issueGame.addActionListener(new ActionListener() {
	 
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                JFrame gmDetails = new JFrame("Enter Details");
	                
	                JLabel gId = new JLabel("GAMEID");
	                gId.setBounds(30, 15, 180, 30);
	                gId.setBackground(new Color(255, 215, 0));
	                gId.setForeground(new Color(255, 255, 255));
	                
	                JLabel uId = new JLabel("USERID");
	                uId.setBounds(30, 53, 180, 30);
	                uId.setForeground(new Color(255, 255, 255));
	                uId.setBackground(new Color(255, 215, 0));
	                
	                JLabel period = new JLabel("PERIOD(num. Days)");
	                period.setBounds(30, 90, 180, 30);
	                period.setForeground(new Color(255, 255, 255));
	                period.setBackground(new Color(255, 215, 0));
	                
	                JLabel isDate = new JLabel("ISSUED_DATE(DD-MM-YYYY)");
	                isDate.setBounds(30, 127, 180, 30);
	                isDate.setForeground(new Color(255, 255, 255));
	                isDate.setBackground(new Color(255, 215, 0));
	                      	                	                
	              //-------Game ID:
	                JTextField gameText = new JTextField();
	                gameText.setBounds(200, 15, 200, 30);
	                gameText.setForeground(new Color(112, 128, 144));
	                gameText.setFont(new Font("Tahoma", Font.BOLD, 14));
	               
	                
	             //-------User ID:
	                JTextField userText = new JTextField();
	                userText.setBounds(200, 53, 200, 30);
	                userText.setForeground(new Color(112, 128, 144));
	                userText.setFont(new Font("Tahoma", Font.BOLD, 14));
	                
	             
	             //-------Period:
	                JTextField periodText = new JTextField();
	                periodText.setBounds(200, 90, 200, 30);
	                periodText.setForeground(new Color(112, 128, 144));
	                periodText.setFont(new Font("Tahoma", Font.BOLD, 14));
	             
	                JTextField issuedDateText = new JTextField();
	                issuedDateText.setBounds(200, 130, 130, 30);
	                issuedDateText.setForeground(new Color(112, 128, 144));
	                issuedDateText.setFont(new Font("Tahoma", Font.BOLD, 14));

	                JButton newIssued = new JButton("SUBMIT");
	                newIssued.setBounds(200, 260, 100, 30);
	                newIssued.setBackground(new Color(255, 215, 0));
	                newIssued.addActionListener(new ActionListener() {
	                    @Override
	                    public void actionPerformed(ActionEvent e) {
	                        String user = userText.getText();
	                        String game = gameText.getText();
	                        String p_int = periodText.getText();
	                        String date = issuedDateText.getText();
	                       
	                        int period_int = Integer.parseInt(p_int);
	                        
	                        Connection connection = connect();

	                        try {
	                            Statement statement = connection.createStatement();
	                            statement.executeUpdate("USE GAMES_LIBRARY;");
	                            statement.executeUpdate("INSERT INTO ISSUED (USERID, GAMEID, ISSUED_DATE, PERIOD_DAYS)" 
	                                                                + "VALUES ('" + user + "', '"
	                                                                              + game + "', '" 
	                                                                              + date + "', "
	                                                                              + p_int +")");
	                                                                             
	                            
	                            JOptionPane.showMessageDialog(null, "Game Issued Successfully");
	                            gmDetails.dispose();    
	                        }
	                        catch(SQLException e1) {
	                            JOptionPane.showMessageDialog(null, e1);
	                        }
	                    }
	                }
	            );
	                frame.getContentPane().setBackground(new Color(119, 136, 153));
	                frame.getContentPane().setLayout(null);
	                gmDetails.getContentPane().setBackground(new Color(119, 136, 153));
	                gmDetails.add(period);
	                gmDetails.add(isDate);
	                gmDetails.add(issueGame);
	                gmDetails.add(gId);
	                gmDetails.add(uId);
	                gmDetails.add(userText);
	                gmDetails.add(gameText);
	                gmDetails.add(periodText);
	                gmDetails.add(issuedDateText);
	                gmDetails.add(newIssued);
	                gmDetails.setSize(440, 350);
	                gmDetails.setLayout(null);
	                gmDetails.setVisible(true);
	                gmDetails.setLocationRelativeTo(null);
	            }
	        }
	    );
		
		JButton returnGame = new JButton("Return Game");
		returnGame.setBounds(20, 105, 110, 30);
		returnGame.setFont(new Font("Tahoma", Font.PLAIN, 12));
		returnGame.setBackground(new Color(255, 215, 0));
		returnGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame retrn = new JFrame("Enter Details");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JLabel issueId = new JLabel("ISSUEID");
				issueId.setBounds(30, 15, 150, 30);
				JTextField idField = new JTextField();
				idField.setBounds(110, 15, 200, 30);
				
				JLabel returnDate = new JLabel("RETURN_DATE (DD-MM-YYYY)");
				returnDate.setBounds(30, 50, 150, 30);
				JTextField rdField = new JTextField();
				rdField.setBounds(180, 50, 130, 30);
				
				
				JLabel l3;
				JLabel l4;
				
				JButton retBtn = new JButton("Return");
				retBtn.setBounds(130, 170, 80, 25);
				retBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String iid = idField.getText();
						String rdate = rdField.getText();
						
						Connection connection = connect();
						
						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");
							
							String d1 = null;
							String d2 = rdate;
							// select issue date:
							ResultSet r = statement.executeQuery("SELECT ISSUED_DATE FROM ISSUED WHERE ISSUEID="+iid);
							while(r.next()) {
								d1 = r.getString(1);
							}
							try {
								java.util.Date date1 = new SimpleDateFormat("DD-MM-YYYY").parse(d1);
								java.util.Date date2 = new SimpleDateFormat("DD-MM-YYYY").parse(d2);
								// subtract dates + save difference:
								long difference = date2.getTime() - date1.getTime();
								// convert from milliseconds to days:
								ex.days = (int)(TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));	
							}
							catch(ParseException e1) {
								e1.printStackTrace();
							}
							
							// update return date:
							statement.executeUpdate("UPDATE ISSUED SET RETURN_DATE = '" + rdate + "' WHERE ISSUEID="+iid);
							retrn.dispose();
							
							Connection connection2 = connect();
							Statement statement2 = connection2.createStatement();
							statement2.executeUpdate("USE GAMES_LIBRARY");
							
							ResultSet r2 = statement2.executeQuery("SELECT PERIOD_DAYS FROM ISSUED WHERE ISSUEID ="+iid);
							String dif = null;
							while(r2.next()) {
								dif = r2.getString(1);
							}
							int dif_int = Integer.parseInt(dif);
							if(ex.days > dif_int) {
								System.out.println(ex.days);
								// fine for each day after period = r2*10
								int fine = (ex.days - dif_int)*10;
								// update fine :
								statement2.executeUpdate("UPDATE ISSUED SET FINE = " + fine
														+ "WHERE ISSUEID="+iid);
								String fineStr = ("Fine: $" + fine);
								JOptionPane.showMessageDialog(null, fineStr);
						}
							JOptionPane.showMessageDialog(null,  "Game Returned Successfully");

					}
						catch(SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				}
			);
				retrn.add(returnDate);
				retrn.add(retBtn);
				retrn.add(issueId);
				retrn.add(idField);
				retrn.add(rdField);
				retrn.setSize(350, 250);
				retrn.setLayout(null);
				retrn.setVisible(true);
				retrn.setLocationRelativeTo(null);				
			}
		}
	);
		frame.getContentPane().setBackground(new Color(119, 136, 153));
		frame.getContentPane().setLayout(null);
		frame.add(returnGame);
		frame.add(issueGame);
		frame.add(addGameBtn);
		frame.add(issuedBtn);
		frame.add(usersBtn);
		frame.add(viewBtn);
		frame.add(btnAddUser);
		frame.add(l_title);
		frame.setSize(600, 200);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		
}


	private static void userMenu(String user) {
		// Create Dialog Box:
				JFrame frame = new JFrame("User Menu");
				// preset closing operation:
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// create instance of button:
				JButton viewBtn = new JButton("View Games");
				viewBtn.setBounds(60, 60, 110, 30);
				viewBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
				viewBtn.setBackground(new Color(255, 215, 0));
				
				// title
				JLabel l_title = new JLabel("Video Game User Login");
				l_title.setForeground(new Color(255, 255, 255));
				l_title.setFont(new Font("Tahoma", Font.BOLD, 18));
				l_title.setBounds(65, 10, 243, 45);
				
				// create event listener:
				viewBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFrame frame = new JFrame("Available Games Today");
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						
						// connect to DB:
						Connection connection = connect();
						// retrieve data :
						String sql = "SELECT * FROM GAMES";
						// try connecting to database:
						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");
							statement = connection.createStatement();
							ResultSet rs = statement.executeQuery(sql);
						// to see database in a table format:
							JTable game_list = new JTable();
							game_list.setModel(DbUtils.resultSetToTableModel(rs));
						// add scroll bar to window:
							JScrollPane sp = new JScrollPane(game_list);
							frame.add(sp);
						// dimensions of the games pane:
							frame.setSize(800, 400);
							frame.setVisible(true);
							frame.setLocationRelativeTo(null);							
						}
						catch(SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
						
					}
					
				});
				
				JButton my_games = new JButton("My Games");
				my_games.setBounds(200, 60, 110, 30);
				my_games.setFont(new Font("Tahoma", Font.PLAIN, 12));
				my_games.setBackground(new Color(255, 215, 0));
				
				
				my_games.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame frame = new JFrame("User Menu - My Game List"); //view book
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						Connection conn = connect();
						String sql = "SELECT issueid,userid,gameid,issued_date,return_date,PERIOD_DAYS,fine FROM issued";
						String sqlt = "SELECT userid FROM issued";
						try {
							Statement stmt = conn.createStatement();
							stmt.executeUpdate("USE games_library");
							stmt = conn.createStatement();
							ArrayList games_list = new ArrayList(); 
							
							ResultSet rs = stmt.executeQuery(sql);
							JTable game_list = new JTable();
							game_list.setModel(DbUtils.resultSetToTableModel(rs));
							JScrollPane scroll = new JScrollPane(game_list);
			                game_list.setBackground(new Color(119, 136, 153));
			                game_list.setForeground(new Color(255, 255, 255));
							
							frame.getContentPane().add(scroll);
							frame.setSize(800,400);
							frame.setVisible(true);
							frame.setLocationRelativeTo(null);
						} catch (SQLException ex) {
							JOptionPane.showMessageDialog(null, ex);
						}
					}
				});
				frame.getContentPane().setBackground(new Color(119, 136, 153));
				frame.getContentPane().setLayout(null);
				frame.add(my_games);
				frame.add(viewBtn);
				frame.setSize(400, 150);
				frame.setLayout(null);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				frame.getContentPane().add(l_title);

	
}
}


				
				


	
