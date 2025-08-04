export function deletePost(postId) {
    const url = `/api/posts/${postId}`;

    fetch(url, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            if (data.status === 'success') {
                alert(data.message);
                window.location.href = '/';
            } else {
                alert('Posts loading failed: ' + data.error);
            }
        })
}