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
        String sql = "INSERT INTO account (name, password, mail) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, account.getName(), account.getPassword(), account.getMail());
    }

    //正答数の多い順から取り出す
    public List<account> rankingAccount(int limit){
        String sql = "SELECT name,count FROM account ORDER BY count DESC LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            account account = new account();
            account.setName(rs.getString("name"));
            account.setCount(rs.getInt("count"));
            return account;
        }, limit);
    }

    //順位を表示する
    public int myRanking(Long id) {
        String sql = "SELECT rank FROM (SELECT ID, ROW_NUMBER() OVER (ORDER BY count DESC) AS rank FROM account) AS RankedData WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }
    
    //名前に対応するパスワードを表示する
    public String readPassword(String name) {
        String sql = "SELECT password FROM account WHERE name = ?";
        List<String> passwords = jdbcTemplate.queryForList(sql, String.class, name);
        
        if (passwords.isEmpty()) {
            return null; // パスワードが見つからなかった場合
        }
        
        return passwords.get(0); // 見つかった場合、最初のパスワードを返す
    }

    //メールアドレスに対応するパスワードを変更する
    public void updatePassword(String mail,String password){
        String sql = "UPDATE account SET password = ? WHERE mail = ?";
        jdbcTemplate.update(sql,password, mail);
    }

    //idに対応するアカウントを削除する
    public void deleteAccount(Long id){
        String sql = "DELETE FROM account WHERE id = ?";
        jdbcTemplate.update(sql, id);      
    }

    //名前に対応するメールを取得する
    public String readMail(String name){
        String sql = "SELECT mail FROM account WHERE name = ?";
        return jdbcTemplate.queryForObject(sql,String.class,name);      
    }

    //カウントに+１する
    public void countUp(Long id){
        String sql = "update account set count = count + 1 where id = ?";
        jdbcTemplate.update(sql, id);
    }

    //nameに対するIdを表示する
    public Long redId(String name) {
    String sql = "SELECT id FROM account WHERE name = ?";
    List<Long> ids = jdbcTemplate.queryForList(sql, Long.class, name);
    
    if (ids.isEmpty()) {
        return null; // IDが見つからなかった場合
    }
    
    return ids.get(0); // 見つかった場合、最初のIDを返す
    }

    //IDをもとに正答数を表示する
    public int getCount(Long id){
        String sql = "select count from account where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,id);
    }
    //IDをもとに名前を表示する
    public String getName(Long id){
        String sql = "select name from account where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class,id);
    }
}
