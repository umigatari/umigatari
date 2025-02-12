document.addEventListener('DOMContentLoaded', function() {
  const searchForm = document.getElementById('searchForm');
  if (searchForm) {
      searchForm.addEventListener('submit', function(e) {
          e.preventDefault();
          const keyword = document.getElementById('search').value;
          const sortOrder = document.getElementById('dord').value;
          this.submit();
      });
  }

  let selectedQuizId = null;
  let actionType = null;
  const dialogOverlay = document.getElementById('confirmationDialog');
  const deleteButtons = document.querySelectorAll('.button-delete');
  const unpostButtons = document.querySelectorAll('.button-unpost');

  const deleteMessage = document.getElementById('deleteMessage');
  const unpostMessage = document.getElementById('unpostMessage');

  // 削除ボタンのイベント
  deleteButtons.forEach(button => {
      button.addEventListener('click', function(e) {
          e.preventDefault();
          selectedQuizId = this.dataset.quizId;
          actionType = 'delete'; // 削除フラグON

          deleteMessage.style.display = 'block';
          unpostMessage.style.display = 'none';

          dialogOverlay.classList.add('active');
      });
  });

  // 非公開ボタンのイベント
  unpostButtons.forEach(button => {
      button.addEventListener('click', function(e) {
          e.preventDefault();
          selectedQuizId = this.dataset.quizId;
          actionType = 'unpost'; // 非公開フラグON

          deleteMessage.style.display = 'none';
          unpostMessage.style.display = 'block';

          dialogOverlay.classList.add('active');
      });
  });

  // キャンセルボタン
  const cancelButton = document.getElementById('cancelDelete');
  if (cancelButton) {
      cancelButton.addEventListener('click', function() {
          dialogOverlay.classList.remove('active');
          selectedQuizId = null;
          actionType = null;
      });
  }

  // 確認ボタン
  const confirmButton = document.getElementById('confirmDelete');
  if (confirmButton) {
      confirmButton.addEventListener('click', function() {
          if (selectedQuizId && actionType) {
              let form;

              if (actionType === 'delete') {
                  // 削除フォームを取得
                  form = document.querySelector(`#delete-form-${selectedQuizId}`);
              } else if (actionType === 'unpost') {
                  // 非公開フォームを正しく取得
                  form = document.querySelector(`.button-unpost[data-quiz-id="${selectedQuizId}"]`).closest('form');
              }

              if (form) {
                  form.submit();
              }
          }
          dialogOverlay.classList.remove('active');
          selectedQuizId = null;
          actionType = null;
      });
  }

  // モーダル外クリックで閉じる
  dialogOverlay.addEventListener('click', function(e) {
      if (e.target === dialogOverlay) {
          dialogOverlay.classList.remove('active');
          selectedQuizId = null;
          actionType = null;
      }
  });
});
