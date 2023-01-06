package Editor;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.swing.*;

public class FileWindow extends JFrame implements ActionListener,Runnable {
    Thread compiler = null;
    Thread runProm = null;
    boolean bn = true;
    CardLayout mycard;
    File file_saved = null;
    JButton buttonInputTxt,
            buttonCompilerText,
            buttonCompiler,
            buttonRunProm,
            buttonSeeDoswin;
    JPanel p = new JPanel();
    JTextArea inputText = new JTextArea();      // 程序输入区
    JTextArea compilerText = new JTextArea();   // 编译错误显示区
    JTextArea dosOutText = new JTextArea();     // 程序的输出信息
    JTextField inputFileNameText = new JTextField();
    JTextField runFileNameText = new JTextField();

    public FileWindow() {
        super("Java语言编译器");
        mycard = new CardLayout();
        compiler = new Thread(this);
        runProm = new Thread(this);
        buttonInputTxt = new JButton("程序输入区(白色)");
        buttonCompilerText = new JButton("编译结果区(粉红色)");
        buttonSeeDoswin = new JButton("程序运行结果(浅蓝色)");
        buttonCompiler = new JButton("编译程序");
        buttonRunProm = new JButton("运行程序");
        // 设置卡片面板
        p.setLayout(mycard);
        p.add("input", inputText);
        p.add("compiler",compilerText);
        p.add("dos",dosOutText);
        add(p,"Center");

        compilerText.setBackground(Color.pink);
        dosOutText.setBackground(Color.cyan);
        JPanel p1 = new JPanel();
        // 设置3*3的网格面板
        p1.setLayout(new GridLayout(3,3));
        p1.add(buttonInputTxt);
        p1.add(buttonCompilerText);
        p1.add(buttonSeeDoswin);
        p1.add(new JLabel("输入编译文件名(.java):"));
        p1.add(inputFileNameText);
        p1.add(buttonCompiler);
        p1.add(new JLabel("输入应用程序主类名"));
        p1.add(runFileNameText);
        p1.add(buttonRunProm);
        add(p1,"North");

        // 设置坐标，窗口大小，可见度
//        setLocation(400,200);
//        setSize(500,400);
//        setVisible(true);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 定义事件
        buttonInputTxt.addActionListener(this);
        buttonCompiler.addActionListener(this);
        buttonCompilerText.addActionListener(this);
        buttonRunProm.addActionListener(this);
        buttonSeeDoswin.addActionListener(this);

    }

    @Override
    public void run() {
        //TODO Auto-generated method stub
        if(Thread.currentThread()==compiler)
        {
            compilerText.setText(null);
            String temp=inputText.getText().trim();
            byte [] buffer=temp.getBytes();
            int b=buffer.length;
            String file_name=null;
            file_name=inputFileNameText.getText().trim();

            try {
                file_saved=new File(file_name);
                FileOutputStream writefile=null;
                writefile=new FileOutputStream(file_saved);
                writefile.write(buffer, 0, b);
                writefile.close();
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("ERROR");
            }
            try {

                //获得该进程的错误流，才可以知道运行结果到底是失败了还是成功。
                Runtime rt=Runtime.getRuntime();
                InputStream in=rt.exec("javac "+file_name).getErrorStream(); //通过Runtime调用javac命令。注意：“javac ”这个字符串是有一个空格的！！

                BufferedInputStream bufIn=new BufferedInputStream(in);

                byte[] shuzu=new byte[100];
                int n=0;
                boolean flag=true;

                //输入错误信息
                while((n=bufIn.read(shuzu, 0,shuzu.length))!=-1)
                {
                    String s=null;
                    s=new String(shuzu,0,n);
                    compilerText.append(s);
                    if(s!=null)
                    {
                        flag=false;
                    }
                }
                //判断是否编译成功
                if(flag)
                {
                    compilerText.append("Compile Succeed!");
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        else if(Thread.currentThread()==runProm)
        {
            //运行文件，并将结果输出到dos_out_text

            dosOutText.setText(null);

            try {
                Runtime rt=Runtime.getRuntime();
                String path=runFileNameText.getText().trim();
                Process stream=rt.exec("java "+path);//调用java命令

                InputStream in=stream.getInputStream();
                BufferedInputStream bisErr=new BufferedInputStream(stream.getErrorStream());
                BufferedInputStream bisIn=new BufferedInputStream(in);

                byte[] buf=new byte[150];
                byte[] err_buf=new byte[150];

                @SuppressWarnings("unused")
                int m=0;
                @SuppressWarnings("unused")
                int i=0;
                String s=null;
                String err=null;

                //打印编译信息及错误信息
                while((m=bisIn.read(buf, 0, 150))!=-1)
                {
                    s=new String(buf,0,150);
                    dosOutText.append(s);
                }
                while((i=bisErr.read(err_buf))!=-1)
                {
                    err=new String(err_buf,0,150);
                    dosOutText.append(err);
                }
            }
            catch (Exception e) {
                // TODO: handle exception
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonInputTxt) {
            //显示程序输入区
            mycard.show(p,"input");
        }
        else if(e.getSource()==buttonCompilerText) {
            //显示编译结果显示区
            mycard.show(p,"compiler");
        }
        else if(e.getSource()==buttonSeeDoswin) {
            //显示程序运行结果区
            mycard.show(p,"dos");
        }
        else if(e.getSource()==buttonCompiler) {
            //如果是编译按钮，执行编译文件的方法
            if(!(compiler.isAlive())) {
                compiler=new Thread(this);
            }
            try {
                compiler.start();

            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }

            mycard.show(p,"compiler");

        }
        else if(e.getSource()==buttonRunProm) {
            //如果是运行按钮，执行运行文件的方法
            if(!(runProm.isAlive())) {
                runProm=new Thread(this);
            }
            try {
                runProm.start();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            mycard.show(p,"dos");
        }

    }
}
