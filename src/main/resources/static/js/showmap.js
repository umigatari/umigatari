const mapButton = document.getElementById('mapButton');
const mapModal = document.getElementById('mapModal');
const closeButton = document.getElementById('closeButton');
const mapImages = document.querySelectorAll('.maps img');

// Show modal
mapButton.addEventListener('click', () => {
  mapModal.classList.add('active');
});

// Close modal
closeButton.addEventListener('click', () => {
  mapModal.classList.remove('active');
});

