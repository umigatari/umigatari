document.addEventListener('DOMContentLoaded', function() {
    // Delete confirmation dialog
    let selectedQuizId = null;
    const dialogOverlay = document.getElementById('confirmationDialog');
    const deleteButtons = document.querySelectorAll('.button-delete');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            selectedQuizId = this.closest('form').querySelector('input[name="id"]').value;
            dialogOverlay.classList.add('active');
        });
    });

    // Dialog buttons
    const cancelButton = document.getElementById('cancelDelete');
    if (cancelButton) {
        cancelButton.addEventListener('click', function() {
            dialogOverlay.classList.remove('active');
            selectedQuizId = null;
        });
    }

    const confirmButton = document.getElementById('confirmDelete');
    if (confirmButton) {
        confirmButton.addEventListener('click', function() {
            if (selectedQuizId) {
                const form = document.querySelector(`#delete-form-${selectedQuizId}`);
                if (form) {
                    form.submit();
                }
            }
            dialogOverlay.classList.remove('active');
        });
    }

    // Close dialog when clicking outside
    dialogOverlay.addEventListener('click', function(e) {
        if (e.target === dialogOverlay) {
            dialogOverlay.classList.remove('active');
            selectedQuizId = null;
        }
    });
});