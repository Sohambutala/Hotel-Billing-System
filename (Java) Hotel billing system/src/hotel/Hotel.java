package hotel;

import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

public class Hotel
{
    public static void main(String[] args) 
    {
        Billing b=new Billing();
        b.init();
    }
}

class Billing implements ActionListener,ItemListener
{
    Frame f=new Frame();
    
    Button tab[]=new Button[10]; 
    Button add=new Button("ADD");
    Button print=new Button("PRINT");
    Button exit=new Button("EXIT");
    Button rm=new Button("REMOVE");
    Button srch=new Button("SEARCH");
    
    TextField search= new TextField();
    TextField quan= new TextField();
    
    List menu=new List();
    List fibill=new List();
    
    Label l1=new Label("TABLE");
    Label l2=new Label("MENU");
    Label l3=new Label("BILL");
    Label l4=new Label("DESCRIPTION");
    Label l5 =new Label("PRICE");
    Label l6=new Label("QUANTITY");
    Label l7=new Label("FINAL AMOUNT");
    Label l8=new Label("TOTAL QUANTITY");
    Label description=new Label();
    Label bill=new Label();
    Label price=new Label();
    Label quant=new Label();
    Label fa=new Label();
    Label head=new Label("HOTEL MANAGEMENT SYSTEM");
    
    int i=0;
        
    Connection con;
    Statement stmt = null;  
    ResultSet rs = null;  
    String url = "jdbc:sqlserver://localhost:1433;databaseName=hotel;integratedSecurity=true"; 
    Billing()
    {
        try
        {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
        con = DriverManager.getConnection(url);  
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void init()
    {
        f.setVisible(true);
        f.setLayout(null);
        f.setSize(1000, 900);
        
        int x=50,y=50;
        head.setBounds(x, y, 950, 100);
        f.add(head);
        
        l1.setBounds(50, 150, 70, 40);
        f.add(l1);
        
        l2.setBounds(170, 150, 200, 40);
        f.add(l2);
        
        y=200;
        
        for(i=0;i<10;i++)
        {
            tab[i]=new Button("Table "+(i+1));
            tab[i].setBounds(x,y,70,40);
            y=y+50;
            tab[i].addActionListener(this);
            f.add(tab[i]);
        }
        
        try
        {
        stmt = con.createStatement();  
        rs = stmt.executeQuery("select * from menu");  
        while (rs.next()) 
        {  
            menu.add(rs.getString(2));
        }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        menu.setBackground(Color.black);
        menu.setForeground(Color.white);
        menu.setBounds(x+120, 200, 200,300 );
        menu.addItemListener(this);
        f.add(menu);
        
        search.setBounds(x+120, 520, 200, 40);
        f.add(search);
        
        l6.setBounds(x+340, 520, 200, 40);
        f.add(l6);
        
        quan.setBounds(x+340, 580, 200, 40);
        f.add(quan);
        
        srch.setBounds(x+120, 580, 200, 40);
        srch.addActionListener(this);
        f.add(srch);
        
        exit.setBounds(x+120, 640, 200, 40);
        exit.addActionListener(this);
        f.add(exit);
        
        add.setBounds(x+340, 640, 200, 40);
        add.addActionListener(this);
        f.add(add);
        
        l4.setBounds(x+340, 150, 200, 40);
        f.add(l4);
        description.setBounds(x+340, 200, 200, 110);
        description.setBackground(Color.black);
        description.setForeground(Color.white);
        f.add(description);
        
        l5.setBounds(x+340, 310, 200, 40);
        f.add(l5);
        price.setBounds(x+340, 360, 200, 110);
        price.setBackground(Color.black);
        price.setForeground(Color.white);
        f.add(price);
        
        
        bill.setBounds(x+560, 200, 200, 50);
        bill.setBackground(Color.black);
        bill.setForeground(Color.white);
        f.add(bill);
        
        
        fibill.setBounds(x+560, 260, 200, 80);
        fibill.setBackground(Color.black);
        fibill.setForeground(Color.white);
        f.add(fibill);
        
        l8.setBounds(x+560, 350, 200, 40);
        f.add(l8);
        
        quant.setBounds(x+560, 400, 200, 40);
        quant.setBackground(Color.black);
        quant.setForeground(Color.white);
        f.add(quant);
        
        l7.setBounds(x+560, 450, 200, 40);
        f.add(l7);
        
        fa.setBounds(x+560, 500, 200, 40);
        fa.setBackground(Color.black);
        fa.setForeground(Color.white);
        f.add(fa);
        
        l3.setBounds(x+560, 150, 200, 40);
        f.add(l3);
        
        print.setBounds(x+560,580 , 200, 40);
        print.addActionListener(this);
        f.add(print);
     
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==exit)
        {
            System.exit(0);
        }        
        else if(e.getSource()==print)
        {
            if(bill.getText().length()>1)
            {
            String sel=bill.getText();
            String qu="delete from tab where tabname=?";
            try
            {
            
            PreparedStatement ps=con.prepareStatement(qu);
            ps.setString(1, sel);
            int n=ps.executeUpdate();
            
            qu="insert into tab(tabname) values(?)";
            ps=con.prepareStatement(qu);
            ps.setString(1, sel);
            n=ps.executeUpdate();
            
            System.out.println(n);
            
            fibill.removeAll();
            quant.setText("");
            fa.setText("");
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            }
        }
        else if(e.getSource()==add)
        {
            if(menu.getSelectedItem()!=null && bill.getText()!=null)
            {
                int q=1,p,fa,id=0;
                int cfa=0;
                String cid="",cq="";
                String sel=menu.getSelectedItem();
                String ta=bill.getText();
                String qu="select * from menu where name=?";
                p=Integer.parseInt(price.getText());
                if(quan.getText()!=null && quan.getText().length()>=1)
                {
                q=Integer.parseInt(quan.getText());
                }
                fa=p*q;
                try
                {
                    PreparedStatement ps=con.prepareStatement(qu);
                    ps.setString(1, sel);
                    rs=ps.executeQuery();
                    while(rs.next())
                    {
                        id=rs.getInt(1);
                    }
                    qu="select * from tab where tabname=?";
                    ps=con.prepareStatement(qu);
                    ps.setString(1, ta);
                    rs=ps.executeQuery();
                    while(rs.next())
                    {
                        cfa=rs.getInt(4);
                        cq=rs.getString(3);
                        cid=rs.getString(2);
                    }
                    if(cid==null)
                    {
                        cfa=fa;
                        cq=""+q;
                        cid=""+id;
                    }
                    else
                    {
                    cfa=cfa+fa;
                    cq=cq+" "+q;
                    cid=cid+" "+id;
                    }
                    qu="update tab set id=?,quan=?,am=? where tabname=?";
                    ps=con.prepareStatement(qu);
                    ps.setString(1, cid);
                    ps.setString(2, cq);
                    ps.setInt(3, cfa);
                    ps.setString(4, ta);
                    int n=ps.executeUpdate();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                fibill.add(q+" "+sel);
                String tq[]=cq.split(" ");
                int k=0,temp=0;
                while(k<tq.length)
                {
                    temp=temp+Integer.parseInt(tq[k]);
                    k++;
                }
                quant.setText(String.valueOf(temp));
                this.fa.setText(String.valueOf(cfa));
                
            }
            else
            {
                System.out.println("no");
            }
        }
        else if(e.getSource()==tab[0] || e.getSource()==tab[1] || 
                e.getSource()==tab[2] || e.getSource()==tab[3] || 
                e.getSource()==tab[4] || e.getSource()==tab[5] ||
                e.getSource()==tab[6] || e.getSource()==tab[7] || 
                e.getSource()==tab[8] || e.getSource()==tab[9] )
        {
            String temp=e.getActionCommand();
            bill.setText(temp);
            String qu="select * from tab where tabname=?";
            ResultSet rsp;
            String na="";
            try
            {
            PreparedStatement ps=con.prepareStatement(qu);
            ps.setString(1, temp);
            rs=ps.executeQuery();
            String item="",quantity="";
            while(rs.next())
            {
                item=rs.getString(2);
                quantity=rs.getString(3);
                fa.setText(String.valueOf(rs.getInt(4)));
            }
            fibill.removeAll();
            if(item!=null && quantity!=null)
            {
            String itema[]=item.split(" ");
            String qa[]=quantity.split(" ");
            int k=0,temps=0,id;
            qu="select * from menu where id=?";
            ps=con.prepareStatement(qu);
            while(k<qa.length)
            {
                id=Integer.parseInt(itema[k]);
                ps.setInt(1, id);
                rsp=ps.executeQuery();
                while(rsp.next())
                {
                    na=rsp.getString(2);
                }
                fibill.add(qa[k]+" "+na);
                    temps=temps+Integer.parseInt(qa[k]);
                    k++;
            }
            quant.setText(String.valueOf(temps));
            }
            else
            {
                quant.setText("");
            }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) 
    {
        String sel=menu.getSelectedItem();
        String p="",t="";
        String q="select * from menu where name=?";
        try
        {
        PreparedStatement ps=con.prepareStatement(q);
        ps.setString(1, sel);
        rs=ps.executeQuery();
        while(rs.next())
        {
             description.setText(rs.getString(3));
             price.setText(rs.getString(4));
        }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
}
