document.getElementById('addquiz').addEventListener('click', function () {
    // 入力内容を取得
    const question = document.getElementById('question').value;
    const correct = document.getElementById('correct').value;
    const otherOne = document.getElementById('other_one').value;
    const otherTwo = document.getElementById('other_two').value;

    // モーダルに反映
    document.getElementById('modal-question').textContent = question;
    document.getElementById('modal-correct').textContent = correct;
    document.getElementById('modal-other-one').textContent = otherOne;
    document.getElementById('modal-other-two').textContent = otherTwo;

    // モーダルを表示
    document.getElementById('confirmation-modal').style.display = 'block';
});

// モーダルのキャンセルボタン
document.getElementById('cancel-modal').addEventListener('click', function () {
    document.getElementById('confirmation-modal').style.display = 'none';
});

// モーダルの送信ボタン
document.getElementById('confirm-submit').addEventListener('click', function () {
    document.getElementById('quiz-form').submit(); // 本当に送信！
});
