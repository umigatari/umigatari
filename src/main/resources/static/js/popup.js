function openPopup() {
    var popup = document.getElementById("popup");
    if (!popup) {
        console.error("Popup element not found.");
    }

    // 各タグの値を取得
    var quizId = document.getElementById('quiz-id').textContent;
    var quizQuestion = document.getElementById('quiz-question').textContent;
    var quizCorrect = document.getElementById('quiz-correct').textContent;
    var quizOtherOne = document.getElementById('quiz-other_one').textContent;
    var quizOtherTwo = document.getElementById('quiz-other_two').textContent;
    var quizType = document.getElementById('quiz-type').textContent;
    var quizConfirmation = document.getElementById('quiz-confirmation').textContent;

    // フォームに値をセット
    document.getElementById("id").value = quizId;
    document.getElementById("question").value = quizQuestion;
    document.getElementById("correct").value = quizCorrect;
    document.getElementById("other_one").value = quizOtherOne;
    document.getElementById("other_two").value = quizOtherTwo;
    document.getElementById("type").value = quizType;
    document.getElementById("confirmation").value = quizConfirmation;

    // ポップアップを表示
    popup.style.display = "block";
}

function closePopup() {
    const popup = document.getElementById("popup");
    if (popup) {
        // フォームのリセット
        document.getElementById("update-form").reset();
        popup.style.display = "none";
    } else {
        console.error("Popup element not found.");
    }
}