document.addEventListener('DOMContentLoaded', () => {
    const surveyPopup = document.getElementById('surveyPopup');
    const questionContainer = document.getElementById('question-container');
    const showAnswerButton = document.getElementById('show-answer');

    // ポップアップを表示する
    surveyPopup.style.display = 'flex';

    // ポップアップ全体をクリックすると閉じる
    surveyPopup.addEventListener('click', () => {
        surveyPopup.style.display = 'none';
    });

    // モーダル内部をクリックした場合は閉じない
    surveyPopup.querySelector('h1').addEventListener('click', (e) => {
        e.stopPropagation();
    });

    // 答えを表示/非表示切り替え
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