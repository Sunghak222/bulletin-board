export async function loadPostData(postId) {
    try {
        const res = await fetch(`/api/posts/${postId}`);
        const data = await res.json();

        if (data.status === 'success') {
            document.getElementById('title').value = data.post.title;
            document.getElementById('content').value = data.post.content;
        } else {
            alert(data.error);
        }
    } catch (err) {
        console.error('Error loading post:', err);
    }
}

export function bindUpdateButton(postId) {
    const btn = document.getElementById('updateBtn');
    btn.addEventListener('click', async () => {
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;

        try {
            const res = await fetch(`/api/posts/${postId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({title, content})
            });

            const result = await res.json();

            if (result.status === 'success') {
                alert(result.message);
                window.location.href = `/post/post.html?id=${postId}`;
            } else {
                alert(result.error);
            }
        } catch (err) {
            console.error('Update failed:', err);
        }
    });
}