export function register() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/api/members/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password})
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            if (data.status === 'success') {
                alert('Register Success');
                window.location.href = '/';
            } else {
                alert('Register failed: ' + data.error);
            }
        })
}