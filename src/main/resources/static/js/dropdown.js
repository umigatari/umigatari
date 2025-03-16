// ポップアップの要素を取得
const logoutPopup = document.getElementById('logout-popup');
const deletePopup = document.getElementById('delete-popup');
const finalDeletePopup = document.getElementById('final-delete-popup');

// 各ボタンにイベントリスナーを設定

// ログアウトボタンが押されたとき、ログアウト確認ポップアップを表示
document.getElementById('logout-link').addEventListener('click', function() {
   logoutPopup.style.display = 'block'; // ログアウト確認ポップアップを表示
});

// タッチイベント対応
document.getElementById('logout-link').addEventListener('touchstart', function(event) {
    event.preventDefault(); // ダブルタップズーム防止
    logoutPopup.style.display = 'block';
});

// アカウント削除ボタンが押されたとき、アカウント削除確認ポップアップを表示
document.getElementById('delete-link').addEventListener('click', function() {
    deletePopup.style.display = 'block'; // アカウント削除確認ポップアップを表示
});

// タッチイベント対応
document.getElementById('delete-link').addEventListener('touchstart', function(event) {
    event.preventDefault();
    deletePopup.style.display = 'block';
});

// ログアウトの「はい」ボタンが押されたとき
document.getElementById('confirm-logout-yes').addEventListener('click', function() {
fetch('/logout', { method: 'POST' });
window.location.href = '/'; // ログアウト後、ログイン画面にリダイレクト
});

// ログアウトの「いいえ」ボタンが押されたとき
document.getElementById('confirm-logout-no').addEventListener('click', function() {
    logoutPopup.style.display = 'none'; // ポップアップを非表示
});
    
// アカウント削除の「はい」ボタンが押されたとき
document.getElementById('confirm-delete-yes').addEventListener('click', function() {
    finalDeletePopup.style.display = 'block'; // 最終確認ポップアップを表示
    deletePopup.style.display = 'none'; // アカウント削除確認ポップアップを非表示
});

// タッチイベント対応
document.getElementById('confirm-delete-yes').addEventListener('touchstart', function(event) {
    event.preventDefault();
    finalDeletePopup.style.display = 'block';
    deletePopup.style.display = 'none';
});
        
// アカウント削除の「いいえ」ボタンが押されたとき
document.getElementById('confirm-delete-no').addEventListener('click', function() {
    deletePopup.style.display = 'none'; // ポップアップを非表示
});
            
// 最終確認の「はい」ボタンが押されたとき
document.getElementById('confirm-final-delete-yes').addEventListener('click', function() {
    fetch('/deleteaccount', { method: 'GET' });
    window.location.href = '/';
});

// 最終確認の「いいえ」ボタンが押されたとき
document.getElementById('confirm-final-delete-no').addEventListener('click', function() {
    finalDeletePopup.style.display = 'none'; // ポップアップを非表示
});

document.addEventListener('DOMContentLoaded', function() {
    // ページ読み込み時にドロップダウンメニューを非表示に設定
    const dropdownMenu = document.getElementById('dropdown-menu');
    dropdownMenu.style.display = 'none';

    // メニューアイコンがクリックされたときにドロップダウンメニューの表示・非表示を切り替える
    const menuIcon = document.getElementById('menu-icon');
    menuIcon.addEventListener('click', function() {
        if (dropdownMenu.style.display === 'block') {
            dropdownMenu.style.display = 'none'; 
        } else {
            dropdownMenu.style.display = 'block'; 
        }
    });

    // ドロップダウンメニュー外をクリックした場合、閉じる
    window.addEventListener('click', function(event) {
        if (!dropdownMenu.contains(event.target) && event.target !== menuIcon) {
            dropdownMenu.style.display = 'none'; 
        }
    });

    // ×ボタンでドロップダウンを閉じる
    document.getElementById('closePopup').addEventListener('click', function() {
        dropdownMenu.style.display = 'none';
    });

    // 「いいえ」ボタンを押した場合にドロップダウンメニューを表示
    function showDropdownMenu() {
        // ロゴやスタンプカードの画面を非表示にする
        const logo = document.getElementById('logo');
        const stampCard = document.getElementById('stamp-card');
        
        if (logo) logo.style.display = 'none';
        if (stampCard) stampCard.style.display = 'none';

        // ポップアップを非表示にする
        const logoutPopup = document.getElementById('logout-popup');
        const deletePopup = document.getElementById('delete-popup');
        const finalDeletePopup = document.getElementById('final-delete-popup');
        if (logoutPopup) logoutPopup.style.display = 'none';
        if (deletePopup) deletePopup.style.display = 'none';
        if (finalDeletePopup) finalDeletePopup.style.display = 'none';

        // ドロップダウンメニューを表示
        dropdownMenu.style.display = 'flex';
    }

    // ログアウト「いいえ」を押したとき
    document.getElementById('confirm-logout-no').addEventListener('click', showDropdownMenu);

    // アカウント削除「いいえ」を押したとき
    document.getElementById('confirm-delete-no').addEventListener('click', showDropdownMenu);

    // 最終確認「いいえ」を押したとき
    document.getElementById('confirm-final-delete-no').addEventListener('click', showDropdownMenu);
});
