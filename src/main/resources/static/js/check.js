let formIdToDelete = null; // 削除するフォームのIDを保持

function confirmDeletion(formId, event) {
    event.preventDefault();
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

function removeEmptyParams() {
    // 'p_cocid'と'dord'の選択された値を取得
    const pCocid = document.getElementById('p_cocid').value;
    const dord = document.getElementById('dord').value;

    // 'p_cocid'が空なら、このパラメータを削除（送信しない）
    if (pCocid === "") {
        const pCocidParam = document.querySelector('select[name="p_cocid"]');
        pCocidParam.removeAttribute('name'); // 'p_cocid'パラメータを送信しない
    }

    // 'dord'が空なら、このパラメータを削除（送信しない）
    if (dord === "") {
        const dordParam = document.querySelector('select[name="dord"]');
        dordParam.removeAttribute('name'); // 'dord'パラメータを送信しない
    }
}