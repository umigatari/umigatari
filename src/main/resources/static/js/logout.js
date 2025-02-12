
    document.getElementById('logout-link').addEventListener('click', function() {
        document.getElementById('confirmationDialog').style.display = 'flex';
    });

    document.getElementById('cancelLogout').addEventListener('click', function() {
        document.getElementById('confirmationDialog').style.display = 'none';
    });
