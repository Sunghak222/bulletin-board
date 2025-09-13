export function displayPost() {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    const url = `/api/posts/${postId}`;

    fetch(url, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            //data.error is not implemented in controller
            if (data.status !== "success") {
                alert('Posts loading failed: ' + data.error);
            }

            let divPost = document.getElementById('post-section');
            let post = data.post;
            divPost.innerHTML = '';

            let title = document.createElement('h2');
            title.textContent = post.title;
            title.className = 'card-title fw-bold mb-3';
            title.id = 'post-title';
            divPost.appendChild(title);

            let author = document.createElement('h6');
            author.textContent = `Author: ${post.authorName}`;
            author.className = 'card-subtitle mb-2 text-muted';
            author.id = 'post-author';
            divPost.appendChild(author);

            let date = document.createElement('h6');
            date.textContent = `Date: ${post.createdAt.split('T')[0]}`;
            date.className = 'card-subtitle mb-3 text-muted';
            date.id = 'post-date';
            divPost.appendChild(date);

            let views = document.createElement('h6');
            views.textContent = `Views: ${post.views}`;
            views.className = 'card-subtitle mb-3 text-muted';
            views.id = 'post-views';
            divPost.appendChild(views);

            let content = document.createElement('p');
            content.innerHTML = post.content.replace(/\n/g, '<br>');
            content.className = 'card-text fs-5 mt-4';
            content.id = 'post-content';
            divPost.appendChild(content);

            let postActionsDiv = document.createElement('div');
            postActionsDiv.id = 'post-actions';
            postActionsDiv.className = 'mt-4 d-flex gap-2';

            const likeBtn = document.createElement('button');
            likeBtn.id = 'btn-like';
            likeBtn.className = 'btn btn-outline-success';
            likeBtn.innerHTML = `👍 Like <span id="like-count">${data.reactions?.likeCount ?? 0}</span>`;

            const dislikeBtn = document.createElement('button');
            dislikeBtn.id = 'btn-dislike';
            dislikeBtn.className = 'btn btn-outline-secondary';
            dislikeBtn.innerHTML = `👎 Dislike <span id="dislike-count">${data.reactions?.dislikeCount ?? 0}</span>`;

            likeBtn.onclick = () => react(post.id, 'like', likeBtn, dislikeBtn);
            dislikeBtn.onclick = () => react(post.id, 'dislike', likeBtn, dislikeBtn);

            postActionsDiv.append(likeBtn, dislikeBtn);

            let postCardBody = document.createElement('div');
            postCardBody.className = 'card-body';
            postCardBody.append(title, author, date, views, content, postActionsDiv);

            let postCard = document.createElement('div');
            postCard.className = 'card shadow-sm mb-4';
            postCard.appendChild(postCardBody);

            divPost.appendChild(postCard);

            addUpdateDeleteButtons(post);
        })
}

async function react(postId, kind, likeBtn, dislikeBtn) {
    try {
        likeBtn.disabled = true;
        dislikeBtn.disabled = true;
        const res = await fetch(`/api/posts/${postId}/${kind}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await res.json().catch(() => ({}));

        if (!res.ok || data.status !== 'success') {
            if (res.status === 401) {
                alert('Login required to react')
            } else {
                alert(data.error || 'Reaction failed');
            }
            return;
        }

        updateReactionCounts(data.likeCount, data.dislikeCount);
    } catch (e) {
        console.error(e);
        alert('Network error');
    } finally {
        likeBtn.disabled = false;
        dislikeBtn.disabled = false;
    }
}
function updateReactionCounts(likeCount, dislikeCount) {
    const likeEl = document.getElementById('like-count');
    const dislikeEl = document.getElementById('dislike-count');
    if (likeEl) likeEl.textContent = likeCount ?? 0;
    if (dislikeEl) dislikeEl.textContent = dislikeCount ?? 0;
}

import { getLoginMember } from '/js/auth/auth.js';
import { deletePost } from '/js/post/delete.js';

async function addUpdateDeleteButtons(post) {
    const loginMember = await getLoginMember();

    if (!loginMember || loginMember.id !== post.authorId) return;
    const container = document.getElementById('post-actions');
    if (!container) return;

    const editBtn = document.createElement('button');
    editBtn.textContent = 'Edit';
    editBtn.className = 'btn btn-outline-primary';
    editBtn.onclick = () => {
        window.location.href = `/post/update.html?id=${post.id}`;
    };

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Delete';
    deleteBtn.className = 'btn btn-danger';
    deleteBtn.onclick = () => deletePost(post.id);

    container.appendChild(editBtn);
    container.appendChild(deleteBtn);
}