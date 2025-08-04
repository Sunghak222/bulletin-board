export function deleteComment(commentId) {

    const params = new URLSearchParams(window.location.search);
    const url = `/api/comments/${commentId}`;

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
                const deletedComment = document.getElementById(`comment-${commentId}`);
                if (deletedComment) {
                    deletedComment.remove();
                }
            } else {
                alert(data.error);
            }
        })}
