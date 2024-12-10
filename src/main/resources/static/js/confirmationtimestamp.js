document.addEventListener("DOMContentLoaded", () => {
    // 現在の時刻（秒単位タイムスタンプ）を取得
    const currentTimestamp = Math.floor(Date.now() / 1000);

    // 全ての hidden input[id="timestamp"] を取得
    const timestampFields = document.querySelectorAll('input[id="timestamp"]');

    // 各 timestamp フィールドに値をセット
    timestampFields.forEach(field => {
        field.value = currentTimestamp;
    });
});