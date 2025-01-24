document.addEventListener("DOMContentLoaded", () => {
    const headlines = document.querySelectorAll(".headline");
    const contents = document.querySelectorAll(".content");

    headlines.forEach((headline) => {
        headline.addEventListener("click", () => {
            const targetId = headline.getAttribute("data-target");
            const targetContent = document.getElementById(targetId);

            if (targetContent) {
                const isVisible = targetContent.classList.contains("show");

                // 他のコンテンツを閉じる
                contents.forEach((content) => {
                    content.classList.remove("show");
                });

                // 他のヘッドラインのクラスをリセット
                headlines.forEach((hl) => {
                    hl.classList.remove("open");
                });

                // クリックした要素をトグル
                if (!isVisible) {
                    targetContent.classList.add("show");
                    headline.classList.add("open");
                }
            }
        });
    });
});

// すべての画像を取得
const images = document.querySelectorAll('.image-item img');
images.forEach(img => {
    img.addEventListener('click', function() {
        // もしクリックされた画像がすでに拡大されていたら
        if (this.classList.contains('expanded')) {
            // 拡大されている画像を元に戻す
            this.classList.remove('expanded');
        } else {
            // すべての画像から 'expanded' クラスを削除
            images.forEach(image => image.classList.remove('expanded'));
            // 現在クリックされた画像に 'expanded' クラスを追加して拡大
            this.classList.add('expanded');
        }
    });
});






