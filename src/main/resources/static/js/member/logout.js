export function logout() {
    fetch('/api/members/logout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(function(response){
            return response.json();
        })
        .then(function(data) {
            if (data.status === 'success') {
                alert('Logout Success');
                window.location.href = '/';
            } else {
                alert('Logout failed: ' + data.error);
            }
        })
}