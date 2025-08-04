export function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/api/members/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password})
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            if (data.status === 'success') {
                alert('Login Success');
                window.location.href = '/';
            } else {
                alert('Login failed: ' + data.error);
            }
        })
}