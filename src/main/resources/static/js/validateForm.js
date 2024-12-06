function validateForm(event) {
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    if (password !== confirmPassword) {
        alert("パスワードが一致しません。");
        event.preventDefault(); // フォームの送信をキャンセル
    }
}
