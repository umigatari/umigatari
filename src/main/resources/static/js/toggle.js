document.querySelectorAll('.rmd-quiz-item').forEach(item => {
  item.addEventListener('click', function() {
    // すべての選択肢を非選択状態にする
    document.querySelectorAll('.rmd-quiz-item').forEach(otherItem => {
      otherItem.classList.remove('selected');
      otherItem.classList.add('unselected');
      const form = otherItem.querySelector('form');
      form.style.display = 'none'; // ボタンを非表示にする
    });
    
    // クリックした選択肢を選択状態にする
    this.classList.add('selected');
    this.classList.remove('unselected');
    
    // 「この問題を解く」ボタンを表示
    const form = this.querySelector('form');
    form.style.display = 'block';
  });
});
