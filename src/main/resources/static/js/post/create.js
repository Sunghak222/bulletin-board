export function createPost() {
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    fetch('/api/posts/new', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({title, content})
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            if (data.status === 'success') {
                alert('Post Success');
                window.location.href = '/';
            } else {
                alert('Post failed: ' + data.error);
            }
            window.location.href = '/';
        })
}