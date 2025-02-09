document.addEventListener('DOMContentLoaded', function() {
  // 要素の取得
  const tabs = document.querySelectorAll('.tab-button');
  const sections = document.querySelectorAll('.content-section');
  const quizForm = document.getElementById('quiz-form');
  const submitButton = document.getElementById('submit-button');
  const confirmationModal = document.getElementById('confirmation-modal');
  const cancelButton = document.getElementById('cancelButton');
  const modalForm = document.getElementById('confirmation-form');

  // タブ切り替え機能
  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      // 全てのタブとセクションから active クラスを削除
      tabs.forEach(t => t.classList.remove('active'));
      sections.forEach(s => s.classList.remove('active'));

      // クリックされたタブと対応するセクションに active クラスを追加
      tab.classList.add('active');
      const targetSection = document.querySelector(`#${tab.dataset.section}`);
      if (targetSection) {
        targetSection.classList.add('active');
      }
    });
  });

  // フォームバリデーション
  function validateForm() {
    const inputs = quizForm.querySelectorAll('input[required]');
    const isValid = Array.from(inputs).every(input => input.value.trim() !== '');
    submitButton.disabled = !isValid;
  }

  // 入力フィールドの変更を監視
  quizForm.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', validateForm);
  });

  // モーダル表示処理
  submitButton.addEventListener('click', () => {
    const formData = {
      question: document.getElementById('question').value,
      correct: document.getElementById('correct').value,
      other_one: document.getElementById('other_one').value,
      other_two: document.getElementById('other_two').value
    };

    // モーダル内の表示を更新
    document.getElementById('modal-question').textContent = formData.question;
    document.getElementById('modal-correct').textContent = formData.correct;
    document.getElementById('modal-other-one').textContent = formData.other_one;
    document.getElementById('modal-other-two').textContent = formData.other_two;

    // hidden フィールドの値を更新
    document.getElementById('hidden-question').value = formData.question;
    document.getElementById('hidden-correct').value = formData.correct;
    document.getElementById('hidden-other-one').value = formData.other_one;
    document.getElementById('hidden-other-two').value = formData.other_two;

    confirmationModal.showModal();
  });

  // モーダルのキャンセルボタン
  cancelButton.addEventListener('click', () => {
    confirmationModal.close();
  });

  // モーダル外クリックで閉じる
  confirmationModal.addEventListener('click', (event) => {
    if (event.target === confirmationModal) {
      confirmationModal.close();
    }
  });

  // フォーム送信後の処理
  modalForm.addEventListener('submit', () => {
    quizForm.reset();
    submitButton.disabled = true;
    confirmationModal.close();
  });
});