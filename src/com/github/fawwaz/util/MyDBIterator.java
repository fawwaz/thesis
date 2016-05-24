/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fawwaz.util;

import cc.mallet.types.Instance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Fawwaz
 */
public class MyDBIterator implements Iterator<Instance> {

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedstatement = null;
    private ResultSet resultset = null;

    ArrayList<anotasi_tweet_final> anotated_list; // digunakan untuk menyimpan sementara setelah mengambil dari database;
    public Integer lastid; // untuk kebutuhan track, gunakan variabel ini..
    ArrayList<String> sesuai_format; // 
    String currentTweetGroup;
    Integer currentStart, currentEnd, nextStart;
    boolean isTest;

    public MyDBIterator(boolean isTest, int fold_num, int how_many_fold) {
        // default constructor
        this("root", "", "jdbc:mysql://localhost:3306/mytomcatapp", isTest, fold_num, how_many_fold);
    }

    public MyDBIterator(String username, String password, String url, boolean isTest, int fold_num, int how_many_fold) {
        this.isTest = isTest;
        anotated_list = new ArrayList<anotasi_tweet_final>();
        sesuai_format = new ArrayList<String>();

        startConnection(url, username, password);

        // Agar otomatis, dibuat fungsi ini..
        Integer jumlah = getCount();
        Integer unit = Math.floorDiv(jumlah, how_many_fold);
        // ambil data dulu, simpan di array list
        getData(fold_num * unit, unit);

        CloseConnection();

        System.out.println("Anotasi silahkan dilanjutkan dengan ukuran anotated_list" + anotated_list.size());
        lastid = 0;
        nextStart = 1;

        //setNextTweetGroup();
        //printSeusaiFormat();
        //System.exit(2);
    }

    @Override
    public boolean hasNext() {
        return lastid <= (anotated_list.size() - 1);
    }

    @Override
    public Instance next() {
        assert (this.hasNext());
        setNextTweetGroup();
        String name = String.valueOf(currentStart);
        Instance carrier = new Instance(currentTweetGroup, null, name, "defaultsource");
        return carrier;
    }

    /*
     * =======================================================================================================
     * ------------ UTILITY Function, fungsi di bawah ini hanya sebagai helper fungsi utama ------------------
     * =======================================================================================================
     */
    
    private void setNextTweetGroup() {
        StringBuffer sb = new StringBuffer();
        anotasi_tweet_final line, nextline;
        Integer i = lastid;
        if (i == anotated_list.size()) {
            return;
        }
        currentStart = nextStart;

        while (true) {
            //System.out.println("iterasi : "+i);
            line = anotated_list.get(i);
            if (i.equals(anotated_list.size() - 1)) {
                lastid = i + 1;
                currentEnd = anotated_list.size();

                // Kalau test
                if (isTest) {
                    System.out.println("[INFO] for Test");
                    sb.append(line.token);
                } else {
                    System.out.println("[INFO] for Training");
                    sb.append(line.token + " " + line.label);
                }
                sb.append("\n");

                currentTweetGroup = sb.toString();
                System.out.println("keluar dari loop karena sudah sampai akhir database dengan lastid :" + lastid);
                return;
            }
            nextline = anotated_list.get(i + 1);

            // System.out.println("LINE IS "+line.token);
            // System.out.println("NEXTLINE IS"+nextline.token);
            // System.out.println("LASTID : " + lastid);
            // System.out.println("I :"+i);
            // System.out.println("=====");
            if (!line.twitter_tweet_id.equals(nextline.twitter_tweet_id)) {
                currentEnd = i + 1;
                lastid = i + 1;
                nextStart = lastid + 1;

                // Kalau test
                if (isTest) {
                    sb.append(line.token);
                } else {
                    sb.append(line.token + " " + line.label);
                }
                sb.append("\n");
                //System.out.println("~~ LASTId :" + lastid);
                currentTweetGroup = sb.toString();
                return;
            } else {

                // Kalau test
                if (isTest) {
                    sb.append(line.token);
                } else {
                    sb.append(line.token + " " + line.label);
                }

                sb.append("\n");
            }
            i++;
        }

    }

    private void startConnection(String URL, String DB_USERNAME, String DB_PASSWORD) {
        System.out.println("[INFO] Getting environment variables");
        System.out.println("DB_USERNAME \t: " + DB_USERNAME);
        System.out.println("DB_PASSWORD \t: " + DB_PASSWORD);
        System.out.println("URL \t\t:" + URL);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
        }catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getData(int offset, int width) {
        System.out.println("Is Test Data  " + String.valueOf(isTest));
        System.out.println("Offset : " + offset + " Width : " + width);
        try {
            if (isTest) {
                // JANGAN LUPA DIGANTI COY --> maksudnya apa ya dulu lupa ... 
                //preparedstatement = connection.prepareStatement("SELECT * from anotasi_tweet_final");
                System.out.println("Selecting For Test Data");
                preparedstatement = connection.prepareStatement("select * from anotasi_tweet_final where twitter_tweet_id in (select * from (select twitter_tweet_id from filtered_tweet_final where label = 1 limit ?,?) as t)");
                preparedstatement.setInt(1, offset);
                preparedstatement.setInt(2, width);
                // --> ini juga maksudnya apa ya di bawah ini .. 
                //preparedstatement = connection.prepareStatement("select * from anotasi_tweet_final where twitter_tweet_id in (select twitter_tweet_id from filtered_tweet_final where label = 1)");
            } else {
                System.out.println("[INFO] Selecting for Training Data");
                preparedstatement = connection.prepareStatement("select * from anotasi_tweet_final where twitter_tweet_id in (select twitter_tweet_id from filtered_tweet_final where label = 1 and twitter_tweet_id not in (select * from (select twitter_tweet_id from filtered_tweet_final where label = 1 limit ?,?) as t))");
                preparedstatement.setInt(1, offset);
                preparedstatement.setInt(2, width);
                // --> lupa juga euy ini mkasudnya apa ...
                //preparedstatement = connection.prepareStatement("select * from anotasi_tweet_final where twitter_tweet_id in (select twitter_tweet_id from filtered_tweet_final where label = 1)");
            }

            resultset = preparedstatement.executeQuery();

            while (resultset.next()) {
                anotasi_tweet_final anotate_data = new anotasi_tweet_final();
                anotate_data.sequence_num = resultset.getInt("sequence_num");
                anotate_data.twitter_tweet_id = resultset.getString("twitter_tweet_id");
                anotate_data.token = resultset.getString("token");
                anotate_data.label = resultset.getString("label2");
                anotated_list.add(anotate_data);
            }
            System.out.println("[INFO] Successful inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Integer getCount() {
        Integer hasil = 0;
        try {
            preparedstatement = connection.prepareStatement("SELECT count(*) from filtered_tweet_final where label=1");
            resultset = preparedstatement.executeQuery();
            if (resultset.next()) {
                hasil = resultset.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Hasil : " + hasil);
        return hasil;
    }

    private void CloseConnection() {
        try {
            if (resultset != null) {
                resultset.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printData() {
        System.out.println("Data yang diload");
        for (int i = 0; i < anotated_list.size(); i++) {
            anotasi_tweet_final a = anotated_list.get(i);
            System.out.println("Token : " + a.token + " \n\tTwitter_tweet_id : " + a.twitter_tweet_id + " \n\tLabel : " + a.label);
        }
    }

    private void printSeusaiFormat() {
        for (int i = 0; i < sesuai_format.size(); i++) {
            System.out.println("Sequence ke -" + i);
            System.out.println(sesuai_format.get(i));
            System.out.println("===###===###===###===");
        }
    }

}
