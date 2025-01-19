let currentIndex = 0;
const intervalTime = 2000; // 自動スライドの間隔（ミリ秒）

function showSlide(index) {
    const slides = document.querySelectorAll(".slide");
    const slideshowContainer = document.querySelector(".slideshow-container");
    const indicators = document.querySelectorAll(".indicator");
    const totalSlides = slides.length;

    if (index >= totalSlides) {
        currentIndex = 0; // 最初のスライドに戻る
    } else if (index < 0) {
        currentIndex = totalSlides - 1; // 最後のスライドに戻る
    } else {
        currentIndex = index;
    }

    // スライドを表示
    const offset = -currentIndex * 100;
    slideshowContainer.style.transform = `translateX(${offset}%)`;

    // インジケーターを更新
    indicators.forEach((indicator, i) => {
        indicator.classList.toggle("active", i === currentIndex);
    });
}

function currentSlide(index) {
    showSlide(index);
    resetAutoSlide(); // 手動切り替え時に自動スライドをリセット
}

// 自動スライド機能
function autoSlide() {
    currentIndex++;
    showSlide(currentIndex);
}

// 自動スライドをリセットする（インジケータークリック時）
function resetAutoSlide() {
    clearInterval(slideInterval);
    slideInterval = setInterval(autoSlide, intervalTime);
}

// 初期化
showSlide(0);
let slideInterval = setInterval(autoSlide, intervalTime);

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

