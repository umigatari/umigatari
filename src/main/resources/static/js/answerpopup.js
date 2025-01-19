// ポップアップを表示する
document.getElementById('surveyPopup').style.display = 'flex';

// ×ボタンでポップアップを閉じる
document.getElementById('closePopup').addEventListener('click', function() {
    document.getElementById('surveyPopup').style.display = 'none';
});

document.addEventListener('DOMContentLoaded', () => {
    const answerContainer = document.getElementById('answer-container');
    const questionContainer = document.getElementById('question-container');
    const showAnswerButton = document.getElementById('show-answer');
    
    showAnswerButton.addEventListener('click', () => {
        if (questionContainer.style.display === 'none') {
            // 問題と答えを表示
            questionContainer.style.display = 'block';
            showAnswerButton.textContent = 'クイズを閉じる'; // ボタンのテキスト変更
        } else {
            // 問題と答えを隠す
            questionContainer.style.display = 'none';
            showAnswerButton.textContent = '答えを見る'; // ボタンのテキスト変更
        }
    });
});