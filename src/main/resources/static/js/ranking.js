// ページ読み込み時に更新日時を表示
document.addEventListener("DOMContentLoaded", function() {
    const updateTimeElement = document.getElementById("update-time");
    
    // 現在の時刻を取得して表示
    const now = new Date();
    const formattedTime = now.toLocaleString("ja-JP", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false
    });

    updateTimeElement.textContent = formattedTime;
});
