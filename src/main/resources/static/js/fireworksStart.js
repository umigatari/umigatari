document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('fireworks-container');
    const options = {
        maxRockets: 3,             // 最大ロケット数
        rocketSpawnInterval: 150,  // ロケット生成間隔（ミリ秒）
        numParticles: 100,         // パーティクル数
        explosionMinHeight: 0.2,   // 爆発の最小高さ
        explosionMaxHeight: 0.8,   // 爆発の最大高さ
        explosionChance: 0.08,     // 爆発の確率
        width: container.clientWidth,  // コンテナの幅
        height: container.clientHeight // コンテナの高さ
    };

    const fireworks = new Fireworks(container, options);
    fireworks.start();

    // 10秒後に花火を停止させる
    setTimeout(() => {
        fireworks.stop();
    }, 3000);  // 10000ミリ秒（10秒）
});
