package com.example.umigatari.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    //メールアドレスチェック    
    public int checkMail(String mail){
        String sql = "SELECT COUNT(*) FROM account WHERE mail = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mail);
    return count;
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
    public String readPasswordByname(String name) {
        String sql = "SELECT password FROM account WHERE name = ?";
        List<String> passwords = jdbcTemplate.queryForList(sql, String.class, name);
        
        if (passwords.isEmpty()) {
            return null; // パスワードが見つからなかった場合
        }
        
        return passwords.get(0); // 見つかった場合、最初のパスワードを返す
    }

    //メールアドレスに対応するパスワードを表示する
    public String readPasswordBymail(String mail) {
        String sql = "SELECT password FROM account WHERE mail = ?";
        List<String> passwords = jdbcTemplate.queryForList(sql, String.class, mail);
        
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

    //メールアドレスに対応する名前を取得
public String mailToName(String mail) {
    String sql = "SELECT name FROM account WHERE mail = ?";
    try {
        return jdbcTemplate.queryForObject(sql, String.class, mail);
    } catch (EmptyResultDataAccessException e) {
        // メールアドレスに対応する名前がない場合
        return null; // または特定の値や例外を返す
    } catch (DataAccessException e) {
        // その他のデータベースアクセスエラー
        throw new RuntimeException("データベースエラーが発生しました", e);
    }
}

    //カウントに+１する
    public void countUp(Long id){
        String sql = "update account set count = count + 1 where id = ?";
        jdbcTemplate.update(sql, id);
    }

    //nameに対するIdを表示する
    public Long redIdByname(String name) {
    String sql = "SELECT id FROM account WHERE name = ?";
    List<Long> ids = jdbcTemplate.queryForList(sql, Long.class, name);
    
    if (ids.isEmpty()) {
        return null; // IDが見つからなかった場合
    }
    
    return ids.get(0); // 見つかった場合、最初のIDを返す
    }

    //mailに対するIdを表示する
    public Long redIdBymail(String mail) {
        String sql = "SELECT id FROM account WHERE mail = ?";
        List<Long> ids = jdbcTemplate.queryForList(sql, Long.class, mail);
        
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

    //登録した人数を数える
    public int getMember(){
        String sql ="SELECT COUNT(*) AS row_count FROM account";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    //IDをもとにログインしたかどうかチェックする
    public LocalDateTime getQuizDayById(Long id) {
        String sql = "SELECT quiz_day FROM solved WHERE account_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, LocalDateTime.class, id);
       } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    // IDに基づいて行を削除
    public void deleteById(Long id) {
        String Sql = "DELETE FROM solved WHERE account_id = ?";
        jdbcTemplate.update(Sql, id);
    }

    public Map<String, Object> getS1ToS5ById(Long id) {
        String sql = "SELECT s1, s2, s3, s4, s5 FROM solved WHERE account_id = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }

    public Map<String, Object> getquiz1Toquiz5ById(Long id) {
        String sql = "SELECT quiz1, quiz2, quiz3, quiz4, quiz5 FROM solved WHERE account_id = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }
    // 引数で指定されたクイズ番号（s1からs5）をtrueに更新
    public void updateStatus(Long id, String quizColumn, boolean status) {
        String sql = "UPDATE solved SET " + quizColumn + " = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, status, id);
    } 

    public void addSolvedEntry(Long accountId) {
        String sql = "INSERT INTO solved (account_id,quiz_day,s1, s2, s3, s4, s5, quiz1, quiz2, quiz3, quiz4, quiz5) " +
                     "VALUES (?,?, false, false, false, false, false, false, false, false, false, false)";
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")).toLocalDateTime(); 
        jdbcTemplate.update(sql, accountId,now);
    } 
    // 引数で指定されたクイズ番号（quiz1からquiz5）をtrueに更新
    public void updateQuizStatus(Long id, String quizColumn, boolean status) {
        String sql = "UPDATE solved SET " + quizColumn + " = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, status, id);
    } 
    }