import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainForm extends JFrame {
    private JTextField textField1;
    private JPanel panel1;
    private JCheckBox zakoupenoCheckBox;
    private JRadioButton a1RadioButton;
    private JRadioButton a2RadioButton;
    private JRadioButton a3RadioButton;
    private JButton předchozíButton;
    private JButton uložitButton;
    private JButton dalšíButton;
    private JButton přidatButton;
    ArrayList<Deskovka> deskovky = new ArrayList<Deskovka>();
    int index = 0;

    public static void main(String[] args) {
        MainForm mf = new MainForm();
        mf.předchozíButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.index--;
                try { update(mf); }
                catch(IndexOutOfBoundsException e1) { mf.index++; }
            }
        });
        mf.dalšíButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.index++;
                try { update(mf); }
                catch(IndexOutOfBoundsException e1) { mf.index--; }
            }
        });
        mf.přidatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mf.deskovky.add(mf.deskovky.size(), new Deskovka("", false, (short)1));
                mf.index++;
                update(mf);
            }
        });
        mf.uložitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                short s1 = 1;
                if(mf.a2RadioButton.isSelected()) { s1 = (short)2; }
                else if(mf.a3RadioButton.isSelected()) { s1 = (short)3; }
                mf.deskovky.set(mf.index, new Deskovka(mf.textField1.getText(), mf.zakoupenoCheckBox.isSelected(), s1));

                try(PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter("deskovky.txt"))))
                {
                    for(Deskovka hra : mf.deskovky)
                    {
                        pw1.println(hra.getNazev() + ";" + (hra.getZakoupeno() ? "ano" : "ne") + ";"  + hra.getOblibenost());
                    }
                }
                catch(Exception e1)
                {
                    //System.out.println("CHYBA: " + e1.getClass() + "  Příčina: " + e1.getCause() + "  Další podrobnosti: " + e1.getMessage());
                    ErrorForm.show1("Druh chyby: " + e1.getMessage() + "\nPříčina: " + e1.getCause() + "\nPodrobnosti: " + e1.getMessage());
                }
            }
        });
        mf.setContentPane(mf.panel1);
        mf.setSize(500, 500);
        mf.setTitle("Deskovky");
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setVisible(true);

        try(Scanner sc1 = new Scanner(new BufferedReader(new FileReader("deskovky.txt"))))
        {
            while(sc1.hasNextLine())
            {
                String[] tmp = sc1.nextLine().split(";");
                try { mf.deskovky.add(new Deskovka(tmp[0], tmp[1].equals("ano"), Short.parseShort(tmp[2]))); }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    //System.out.println("CHYBA: V souboru deskovky.txt chybí některý z parametrů.");
                    ErrorForm.show1("V souboru deskovky.txt chybí některý z parametrů.");
                }
                catch(ClassCastException ee)
                {
                    //System.out.println("CHYBA: Jako hodnota není zadáno platné celé číslo.");
                    ErrorForm.show1("Jako hodnota oblíbenosti není zadáno platné celé číslo.");
                }
            }
        }
        catch(FileNotFoundException e1)
        {
            //System.out.println("CHYBA: Soubor deskovky.txt nenalezen.");
            ErrorForm.show1("Soubor deskovky.txt nenalezen.");
        }
        catch(Exception e2)
        {
            //System.out.println("CHYBA: " + e2.getClass() + "  Příčina: " + e2.getCause() + "  Další podrobnosti: " + e2.getMessage());
            ErrorForm.show1("Druh chyby: " + e2.getMessage() + "\nPříčina: " + e2.getCause() + "\nPodrobnosti: " + e2.getMessage());
        }

        try { update(mf); }
        catch(IndexOutOfBoundsException e1) { //System.out.println("CHYBA: Soubor deskovky.txt neobsahuje žádné hodnoty.");
            ErrorForm.show1("Soubor deskovky.txt neobsahuje žádné hodnoty.");
        }
    }
    static void update(MainForm mf) {
        mf.textField1.setText(mf.deskovky.get(mf.index).getNazev());
        mf.zakoupenoCheckBox.setSelected(mf.deskovky.get(mf.index).getZakoupeno());
        switch (mf.deskovky.get(mf.index).getOblibenost()) {
            case 1:
                mf.a1RadioButton.setSelected(true);
                break;
            case 2:
                mf.a2RadioButton.setSelected(true);
                break;
            case 3:
                mf.a3RadioButton.setSelected(true);
                break;
            default:
                //System.out.println("CHYBA: Oblíbenost nespadá do určeného rozsahu čísel.");
                ErrorForm.show1("Hodnota oblíbenosti nespadá do určeného rozsahu čísel (1 až 3).");
                break;
        }
    }
}

class Deskovka
{
    private String nazev;
    private boolean zakoupeno;
    private short oblibenost;
    public Deskovka(String nazev, boolean zakoupeno, short oblibenost)
    {
        this.nazev = nazev;
        this.zakoupeno = zakoupeno;
        this.oblibenost = oblibenost;
    }
    public String getNazev()
    {
        return this.nazev;
    }
    public boolean getZakoupeno()
    {
        return this.zakoupeno;
    }
    public short getOblibenost()
    {
        return this.oblibenost;
    }
}