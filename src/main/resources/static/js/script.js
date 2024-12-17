const codeReader = new ZXing.BrowserQRCodeReader();
const audio = new Audio('beep.mp3');
const videoElement = document.getElementById('video');
const resultElement = document.getElementById('result');
const startButtonElement = document.getElementById('startButton');

const getBackCamera = (devices) => {
    return devices.find((device) => device.label.toLowerCase().includes('back'));
};

const startScan = () => {
    codeReader.listVideoInputDevices()
        .then((videoInputDevices) => {
            if (videoInputDevices.length === 0) {
                alert('カメラが見つかりませんでした。');
                return;
            }

            const backCamera = getBackCamera(videoInputDevices);
            const selectedDeviceId = backCamera ? backCamera.deviceId : videoInputDevices[videoInputDevices.length - 1].deviceId;

            codeReader.decodeFromVideoDevice(selectedDeviceId, videoElement, (result, err) => {
                if (result) {
                    resultElement.textContent = '結果: ' + result.text;
                    audio.play();
                    codeReader.reset();
                }
                if (err && !(err instanceof ZXing.NotFoundException)) {
                    console.error('エラー:', err);
                    resultElement.textContent = 'エラー: ' + err;
                }
            });
        })
        .catch((err) => {
            console.error('エラー:', err);
            alert('カメラへのアクセス中にエラーが発生しました。');
        });
};

startButtonElement.addEventListener('click', startScan);