function setCustomMessage(input, message) {
    input.setCustomValidity(message);
}

function clearCustomMessage(input) {
    input.setCustomValidity('');
}

function validateForm() {
    return true; // 通常の送信処理を許可
}