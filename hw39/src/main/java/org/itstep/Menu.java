package org.itstep;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu implements Serializable, Comparable<Menu> {
    private String title;
    private Action action;
    private List<Menu> submenu;

    public Menu (String title) {
        this(title, null);
    }

    public Menu(String title, Action action) {
        this.title = title;
        this.action = action;
        submenu = new ArrayList<>();
    }

    public Menu add(Menu menu) {
        submenu.add(menu);
        return menu;
    }

    public void show() throws IOException {
        if (action != null) {
            action.doIt();
        }
        else {
            System.out.println(title);
            for(int i = 0; i < submenu.size(); i++) {
                System.out.println((i+1) + ") " +submenu.get(i).title);
            }
            Scanner scanner = new Scanner(System.in);
            String choose = scanner.nextLine();
            try {
                int m = Integer.parseInt(choose) - 1;
                submenu.get(m).show();
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int compareTo(Menu o) {
        return title.compareTo(o.title);
    }
}
