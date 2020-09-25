package Game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;

public class MyJframes extends JFrame {
    JFrame frame;
    private JPanel panel1 = new JPanel();
    private JButton buttona = new JButton("开始");
    private JLabel label1 = new JLabel("分数");
    private JTextField textarea1 = new JTextField(10);
    private JLabel buttonc = new JLabel("时间");
    private JProgressBar jindu = new JProgressBar();
    private Timer timer;
    private JButton Pausebutton = new JButton("暂停");

    private JButton buttonb = new JButton("退出");
    private JPanel panel2 = new JPanel();
    private JButton button[][] = new JButton[8][8];
    private int animal[][] = new int[8][8];
    private ImageIcon Iocn[] = new ImageIcon[7];
    // 标记是否有三个以上的连接
    private final int EMPTY = -1;// 无为0，有为1
    // 随机数
    private Random rand = new Random();// new 一个监听器
    // 标记是否有三个以上的连接
    private boolean isThreeLinked;
    // 标记单击次数
    private boolean isDoubleClicked;
    private int x1;// 记录第一次被点击按钮的X坐标
    private int y1;// 记录第一次被点击按钮的Y坐标
    private int grade = 0; //得分
    private int score[]={0,0,0,0,0};
    private JTextField textarea2= new JTextField(8);
    String users[]={"User1","User2","User3","User4","User5"};
    int count = 0;

    private boolean flagPause = false;

    MyJframes() {
        // 加载图片
        Iocn[0] = new ImageIcon("src/images/1.jpg");
        Iocn[1] = new ImageIcon("src/images/2.jpg");
        Iocn[2] = new ImageIcon("src/images/3.jpg");
        Iocn[3] = new ImageIcon("src/images/4.jpg");
        Iocn[4] = new ImageIcon("src/images/5.jpg");
        Iocn[5] = new ImageIcon("src/images/6.jpg");
        Iocn[6] = new ImageIcon("src/images/7.jpg");
        panel1.setLayout(new FlowLayout());
        panel1.add(textarea2);
        panel1.add(buttona);
        buttona.setFocusPainted(false);
        panel1.add(label1);
        panel1.add(textarea1);
        textarea1.setEditable(false);
        textarea1.setText(Integer.toString(grade));//显示分数
        panel1.add(buttonc);
        jindu.setMaximum(100);
        panel1.add(jindu);
        panel1.add(Pausebutton);
        Pausebutton.setFocusPainted(false);
        panel1.add(buttonb);
        buttona.setFocusPainted(false);
        this.setLayout(new BorderLayout());
        this.add(panel1, BorderLayout.NORTH);
        panel2.setLayout(new GridLayout(8, 8, 1, 1));
        MyListener mylisten = new MyListener();
        int m;
        // 初始化动物数组
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                m = (int) (Math.random() * 7);
                button[i][j] = new JButton(Iocn[m]);
                button[i][j].setSize(50, 50);
                button[i][j].addActionListener(mylisten);
                button[i][j].setEnabled(false); //图形按钮无效
                panel2.add(button[i][j]);
            }
        this.add(panel2, BorderLayout.CENTER);
        buttona.addActionListener(mylisten);
        buttonb.addActionListener(mylisten);
        Pausebutton.addActionListener(mylisten);
    }
    // 初始化动物数组
    private void initAnimalMatrix() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // 随机选取动物
                animal[i][j] = rand.nextInt(7);
            }
        }
    }
    private void removeLinked(int x, int y) {
        if(animal[x][y] == EMPTY)return;
        int n=0;
        int tmp;
        int linked = 1;
        if (x + 1 < 8) {
            tmp = x + 1;
            while (tmp < 8 && animal[x][y] == animal[tmp][y]) {
                linked++;
                tmp++;
            }
        }
        if (x - 1 >= 0) {
            tmp = x - 1;
            while (tmp >= 0 && animal[x][y] == animal[tmp][y]) {
                linked++;
                tmp--;
            }
        }
        if (linked >= 3) {
            n=n+linked;
            tmp = x + 1;
            while (tmp < 8 && animal[tmp][y] == animal[x][y]) {
                animal[tmp][y] = EMPTY;
                tmp++;
            }
            tmp = x - 1;
            while (tmp >= 0 && animal[tmp][y] == animal[x][y]) {
                animal[tmp][y] = EMPTY;
                tmp--;
            }
            // 当前交换过来的点
            animal[x][y] = EMPTY;
        }
        tmp = 0;
        linked = 1;
        if (y + 1 < 8) {
            tmp = y + 1;
            while (tmp < 8 && animal[x][y] == animal[x][tmp]) {
                linked++;
                tmp++;
            }
        }
        if (y - 1 >= 0) {
            tmp = y - 1;
            while (tmp >= 0 && animal[x][y] == animal[x][tmp]) {
                linked++;
                tmp--;
            }
        }
        if (linked >= 3) {
            n=n+linked;
            tmp = y + 1;
            while (tmp < 8 && animal[x][y] == animal[x][tmp]) {
                animal[x][tmp] = EMPTY;
                tmp++;
            }
            tmp = y - 1;
            while (tmp >= 0 && animal[x][y] == animal[x][tmp]) {
                animal[x][tmp] = EMPTY;
                tmp--;
            }
            // 当前交换过来的点
            animal[x][y] = EMPTY;
        }
        grade+=n*10;
        textarea1.setText(Integer.toString(grade));
    }
    private boolean globalSearch(int flag) {
        if (1 == flag) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isThreeLinked(i, j)) {
                        return true;
                    }
                }
            }
        } else if (2 == flag) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    // 删除三个相连的点
                    removeLinked(i, j);
                }
            }
        }
        return false;
    }
    // 是否有三个以上连接的点
    private boolean isThreeLinked(int x, int y) {
        int tmp;
        int linked = 1;
        if (x + 1 < 8) {
            tmp = x + 1;
            while (tmp < 8 && animal[x][y] == animal[tmp][y]) {
                linked++;
                tmp++;
            }
        }
        if (x - 1 >= 0) {
            tmp = x - 1;
            while (tmp >= 0 && animal[x][y] == animal[tmp][y]) {
                linked++;
                tmp--;
            }
        }
        if (linked >= 3) {
            return true;
        }
        linked = 1;
        if (y + 1 < 8) {
            tmp = y + 1;
            while (tmp < 8 && animal[x][y] == animal[x][tmp]) {
                linked++;
                tmp++;
            }
        }
        if (y - 1 >= 0) {
            tmp = y - 1;
            while (tmp >= 0 && animal[x][y] == animal[x][tmp]) {
                linked++;
                tmp--;
            }
        }
        if (linked >= 3) {
            return true;
        }
        return false;
    }
    // 更新动物矩阵
    private void updateAnimal() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (animal[i][j] == EMPTY) {
                    // 将矩阵中为空值的点再进行随机赋值
                    animal[i][j] = rand.nextInt(7);
                }
            }
        }
    }

    // 动物下降
    private void downAnimal() {
        int tmp;
        for (int j = 8 - 1; j >= 0; j--) {
            for (int i = 0; i < 8; i++) {
                if (animal[j][i] == EMPTY) {
                    for (int k = j - 1; k >= 0; k--) {
                        if (animal[k][i] != EMPTY) {
                            tmp = animal[k][i];
                            animal[k][i] = animal[j][i];
                            animal[j][i] = tmp;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void print() { //重新显示按钮图形
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                button[i][j].setIcon(Iocn[animal[i][j]]);
            }
        }
    }

    private void swapAnimal(int x, int y) {
        if ((x >= 0 && x <= 8) && (y >= 0 && y <= 8)) {
            // 被点击的动物的坐标
            int x2;
            int y2;
            if (!isDoubleClicked) { //第一次单击
                isDoubleClicked = true;
                x1 = x;
                y1 = y;
            } else{               //第2次单击
                x2 = x;
                y2 = y;
                isDoubleClicked = false;
                //两点的坐标绝对值等于1时视为相邻的两点
                if (1 == Math.abs(x2 - x1) && y2 == y1
                        || 1 == Math.abs(y2 - y1)&& x2 == x1) {
                    //-------------交换矩阵中相邻的两点的值--------------
                    int tmp;
                    tmp = animal[y2][x2];
                    animal[y2][x2] = animal[y1][x1];
                    animal[y1][x1] = tmp;
                    if (isThreeLinked(y2, x2)||isThreeLinked(y1, x1)) {
                        if(isThreeLinked(y2, x2))
                            removeLinked(y2, x2);
                        if(isThreeLinked(y1, x1))
                            removeLinked(y1, x1);
                        downAnimal();//被削去处上方的动物下降
                        updateAnimal();//该列上方重新随机产生新的动物，更新动物矩阵
                        print();
                        //全局扫描判断是否有新的三个以上连接点，有则删除
                        while (globalSearch(1)) {
                            //全局扫描消除三个以上相连的点
                            globalSearch(2);
                            //动物再次下落
                            downAnimal();
                            //再次更新动物矩阵
                            updateAnimal();
                            print();
                        }
                    }
                    else {//没有三个以上相同的点，交换回来
                        System.out.println("交换回来");
                        tmp = animal[y1][x1];
                        animal[y1][x1] = animal[y2][x2];
                        animal[y2][x2] = tmp;
                        print();
                    }
                }
            }
        }
    }
    public void init() {
        do {
            initAnimalMatrix();
        } while (globalSearch(1));
        print();
        pack();
        setResizable(true);
        setVisible(true);
    }
    public static void main(String[] args) {
        MyJframes frame = new MyJframes();
        frame.setTitle("消消乐游戏");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.init();
    }


    // 监听器
    class MyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttona) {
                buttona.setEnabled(false);
                jindu.setStringPainted(true);
                jindu.setMaximum(100);
                jindu.setMinimum(0);
                timer = new Timer(100, new TimeListener());
                timer.start();
                grade = 0;
                textarea1.setText(Integer.toString(grade));
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++) {
                        button[i][j].setEnabled(true); //图形按钮有效
                    }
            }
            if(e.getSource() == Pausebutton){
                if (flagPause) {
                    Pausebutton.setText("暂停");
                    flagPause = false;
                    timer.start();
                    panel2.setVisible(true);
                } else {
                    Pausebutton.setText("继续");
                    flagPause = true;
                    timer.stop();
                    panel2.setVisible(false);
                }
            }
            if (e.getSource() == buttonb) {
                System.out.println("end");
                System.exit(1);
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (e.getSource() == button[i][j]) {
                        swapAnimal(j, i);
                    }
                }
            }
        }


    }
    class TimeListener implements ActionListener {
        int times = 0;
        public void actionPerformed(ActionEvent e) {
            jindu.setValue(times++);
            if (times > 100) {
                timer.stop();
                //定时器结束
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++) {
                        button[i][j].setEnabled(false); //图形按钮无效
                    }
                score[count] = grade;
                count++;

                if(count > 9)
                    count = 0;
                try {
                    FileWriter fw = new FileWriter("D:\\文件\\outputt.txt",false);
                    for(int i = 0;i < 9;i++) {
                        fw.write(users[i] + ":" + score[i] + "\n");
                    }

                    fw.flush();
                    fw.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                for(int i = 0;i < 9;i++) {
                    System.out.println(users[i] + ":" + score[i]);
                }

                buttona.setEnabled(true);
            }
        }
    }
}

