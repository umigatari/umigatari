function combineParams(event) {
    event.preventDefault();
    
    var keyword = document.getElementById('search').value;
    var dord = document.getElementById('dord').value;

    var query = '';

    if (keyword) {
        query += 'keyword=' + encodeURIComponent(keyword);
    }

    if (dord) {
        if (query.length > 0) {
            query += '&';
        }
        query += 'dord=' + encodeURIComponent(dord);
    }

    var action = event.target.action;
    var finalUrl = action + (query.length > 0 ? '?' + query : '');

    window.location.href = finalUrl;
}
let formIdToDelete = null; // 削除するフォームのIDを保持

function confirmDeletion(formId) {
    formIdToDelete = `delete-form-${formId}`; // フォームのIDを保存
    const dialog = document.getElementById('confirmation-dialog');
    dialog.style.display = 'block'; // ポップアップを表示
}

function proceedDeletion() {
    if (formIdToDelete) {
        const form = document.getElementById(formIdToDelete);
        form.submit(); // 保存したフォームを送信
    }
    cancelDeletion(); // ポップアップを閉じる
}

function cancelDeletion() {
    formIdToDelete = null; // フォームIDをリセット
    const dialog = document.getElementById('confirmation-dialog');
    dialog.style.display = 'none'; // ポップアップを非表示
}
