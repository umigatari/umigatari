package com.example.umigatari.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.umigatari.model.account;
@Repository
public class UserRepository {
    
    @Autowired
    public JdbcTemplate jdbcTemplate;

    //アカウントをつくる
    public void createAccount(account account){
        String sql = "INSERT INTO accounts (username, password, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, account.getName(), account.getPassword(), account.getMail());
    }

    //正答数の多い順から取り出す
    public List<account> rankingAccount(int limit){
        String sql = "SELECT name,count FROM accounts ORDER BY count DESC LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            account account = new account();
            account.setName(rs.getString("name"));
            account.setCount(rs.getInt("count"));
            return account;
        }, limit);
    }

    //順位を表示する
    public int myRanking(Long id) {
        String sql = "SELECT rank FROM (SELECT ID, ROW_NUMBER() OVER (ORDER BY count DESC) AS rank FROM テーブル名) AS RankedData WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }
    
    //名前に対応するパスワードを表示する
    public String readPassword(String name){
        String sql ="SELECT password FROM account WHERE username = ?";
        return jdbcTemplate.queryForObject(sql,String.class,name);
    }

    //名前に対応するパスワードを変更する
    public void updatePassword(String name,String password){
        String sql = "UPDATE account SET password = ? WHERE name = ?";
        jdbcTemplate.update(sql,password, name);
    }

    //idに対応するアカウントを削除する
    public void deleteAccount(Long id){
        String sql = "DELETE FROM account WHERE id = ?";
        jdbcTemplate.update(sql, id);      
    }

    //名前に対応するメールを取得する
    public String readMail(String name){
        String sql = "SELECT mail FROM account WHERE username = ?";
        return jdbcTemplate.queryForObject(sql,String.class,name);      
    }
}
