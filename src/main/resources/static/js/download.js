
document.addEventListener("DOMContentLoaded", function () {
    const images = document.querySelectorAll(".image");
 
    images.forEach(img => {
        img.addEventListener("contextmenu", function (event) {
            event.preventDefault(); // デフォルトの右クリックメニューを無効化
            downloadImage(img);
        });
    });
 
    function downloadImage(img) {
        const canvas = document.createElement("canvas");
        const ctx = canvas.getContext("2d");
 
        const imgElement = new Image();
        imgElement.crossOrigin = "anonymous"; // CORS 対策
        imgElement.src = img.src;
 
        imgElement.onload = function () {
            // Canvas のサイズを画像サイズに合わせる
            canvas.width = imgElement.width;
            canvas.height = imgElement.height;
 
            // ぼかしのチェック
            const isBlurred = img.style.filter.includes("blur");
 
            if (isBlurred) {
                ctx.filter = "blur(15px)"; // ぼかし適用
            }
 
            // Canvas に画像を描画
            ctx.drawImage(imgElement, 0, 0, canvas.width, canvas.height);
 
            // ダウンロード用のリンクを作成
            const link = document.createElement("a");
            link.href = canvas.toDataURL("image/png");
            link.download = "download.png"; // ダウンロード名
            link.click();
        };
    }
});
 
 