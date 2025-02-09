document.addEventListener('DOMContentLoaded', function() {
    // Form handling
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
      searchForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const keyword = document.getElementById('search').value;
        const sortOrder = document.getElementById('dord').value;
        
        // Here you would typically make an AJAX call or submit the form
        console.log('Search:', keyword, 'Sort Order:', sortOrder);
        this.submit();
      });
    }
  
    // Delete confirmation dialog
    let selectedQuizId = null;
    const dialogOverlay = document.getElementById('confirmationDialog');
    const deleteButtons = document.querySelectorAll('.button-delete');
  
    deleteButtons.forEach(button => {
      button.addEventListener('click', function(e) {
        e.preventDefault();
        selectedQuizId = this.dataset.quizId;
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