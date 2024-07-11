package bitcamp.project3.util;

import bitcamp.project3.command.BookCommand;
import bitcamp.project3.command.UserCommand;
import bitcamp.project3.vo.Book;
import bitcamp.project3.vo.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Menu {
  String[] loginMenu = {"로그인", "회원가입"};
  String[] userMainMenu = {"도서검색", "신간도서", "대출현황", "이용안내"};
  String[] adminMainMenu = {"사용자관리", "도서관리", "대출기록"};
  Login loginUser;
  Stack<String> menuPath = new Stack<>();

  List<User> userList = new ArrayList<>();
  List<Book> bookList = new ArrayList<>();

  UserCommand userCommand = new UserCommand("사용자관리", userList);
  BookCommand bookCommand = new BookCommand("도서관리", bookList);

  Menu() {

  }

  private static Menu m;

  public static Menu getInstance() {
    if (m == null) {
      m = new Menu();
    }

    return m;
  }

  public static void freeInstance() {
    m = null;
  }

  public void menu() {
    //dummy
    new DummyData().addDummyUser();

    //loginMenu
    for (;;) {
      for(int i = 0; i < loginMenu.length; i++) {
        System.out.printf("%d. %s\n", (i + 1), loginMenu[i]);
      }
      System.out.println("0. 종료");

      switch (Prompt.input("입력>")){
        case "1": //login
          if(login()){
            mainMenu();
          }
          continue;
        case "2": //join
          userCommand.addUser();
          continue;
        case "0":
          System.out.println("시스템을 종료합니다.");
          return;
        default:
          Prompt.printNumberException();
      }
    }
  }

  public boolean login() {
    String id = Prompt.input("id?");
    String pw = Prompt.input("pw?");

    User user = new User();
    user.setId(id);

    if(!userList.contains(user)) {
      System.out.println("로그인 정보를 확인하세요.");
      return false;
    }

    user = userList.get(userList.indexOf(user));

    if(user.getPw().equals(pw)) {
      Login login = Login.getInstance();
      login.setId(id);
      login.setName(user.getName());
      login.setAdmin(user.isAdmin());
      loginUser = login;
      return true;
    } else {
      System.out.println("로그인 정보를 확인하세요.");
      return false;
    }
  }

  public void mainMenu() {
    menuPath.push("메인");
    if(loginUser.isAdmin()) {
      System.out.println("관리자 계정으로 로그인합니다.\n");

      adminMainMenu();
    } else {
      System.out.println("사용자 계정으로 로그인합니다.\n");

      userMainMenu();
    }
  }

  public void adminMainMenu() {
    for (; ; ) {
      for (int i = 0; i < adminMainMenu.length; i++) {
        System.out.printf("%d. %s\n", (i + 1), adminMainMenu[i]);
      }
      System.out.println("0. 로그아웃");

      switch (Prompt.input("입력>")) {
        case "1":
          userCommand.execute(menuPath);
          continue;
        case "2":
          bookCommand.execute(menuPath);
          continue;
        case "3":
          System.out.println("대출기록");
          continue;
        case "0":
          Prompt.printLogout();
          System.out.println("로그아웃합니다.");
          Logout.performLogout();
          this.loginUser = null;
          return;
        default:
          Prompt.printNumberException();
      }
    }
  }

  public void userMainMenu() {
    for (;;) {
      for(int i = 0; i < userMainMenu.length; i++) {
        System.out.printf("%d. %s\n", (i + 1), userMainMenu[i]);
      }
      System.out.println("0. 로그아웃");

      switch (Prompt.input("입력>")){
        case "1":
          System.out.println("도서검색");
          continue;
        case "2":
          System.out.println("신간도서");
          continue;
        case "3":
          System.out.println("대출현황");
          continue;
        case "4":
          System.out.println("이용안내");
          continue;
        case "0":
          Prompt.printLogout();
          return;
        default:
          Prompt.printNumberException();
      }
    }
  }

  private String getMenuPathTitle(Stack<String> menuPath) {
    StringBuilder strBuilder = new StringBuilder();
    for (int i = 0; i < menuPath.size(); i++) {
      if (strBuilder.length() > 0) {
        strBuilder.append("/");
      }
      strBuilder.append(menuPath.get(i));
    }
    return strBuilder.toString();
  }

  public class DummyData {
    public void addDummyUser() {
      User user;
      user = new User("root", "0000", "관리자", true, LocalDate.now(), new ArrayList<>());
      userList.add(user);
      user = new User("test", "0000", "사용자1", false, LocalDate.now(), new ArrayList<>());
      userList.add(user);
    }
  }
}
