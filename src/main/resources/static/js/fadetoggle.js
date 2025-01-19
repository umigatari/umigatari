document.addEventListener("DOMContentLoaded", () => {
    const quizItems = document.querySelectorAll(".quiz-item");

    // 各クイズ項目を順番にフェードイン
    quizItems.forEach((item, index) => {
        setTimeout(() => {
            item.classList.add("show"); // フェードイン適用
        }, index * 300); // 300ms 間隔
    });

    const questionButtons = document.querySelectorAll(".question-button");

    questionButtons.forEach((button) => {
        button.addEventListener("click", (event) => {
            event.preventDefault();
            const confirmButton = button.nextElementSibling.querySelector(".confirm-button");

            // 他の「これにする！」ボタンを非表示にする
            document.querySelectorAll(".confirm-button").forEach((btn) => {
                if (btn !== confirmButton) {
                    btn.classList.remove("show");
                    setTimeout(() => {
                        btn.style.display = "none";
                    }, 500); // フェードアウト後に非表示
                }
            });

            // ボタンが表示されていなければフェードインで表示
            if (confirmButton.style.display === "none" || !confirmButton.classList.contains("show")) {
                confirmButton.style.display = "inline-block"; // 表示を有効化
                setTimeout(() => {
                    confirmButton.classList.add("show"); // フェードイン
                }, 10);
            } else {
                // 表示されている場合は非表示に戻す
                confirmButton.classList.remove("show");
                setTimeout(() => {
                    confirmButton.style.display = "none"; // 非表示
                }, 500); // フェードアウト後に非表示
            }
        });
    });
});
